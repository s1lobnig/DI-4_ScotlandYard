package com.example.scotlandyard.map.motions;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.Route;
import com.example.scotlandyard.map.Routes;
import com.example.scotlandyard.map.ValidatedRoute;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.map.roadmap.PositionEntry;
import com.example.scotlandyard.map.roadmap.TicketEntry;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MovingLogic {

    public static final int ANIMATION_DURATION = 3000;

    private MovingLogic() {

    }

    /**
     * @param r        Route-Object
     * @param startPos Current location-index
     * @return { ArrayList<LatLang> in the correct order (either original (if
     * current position = route.StartPos) or revered) ArrayList<Float> which
     * contains the animation-duration-slices according to the
     * route-part-length }
     */
    public static ArrayList[] getRouteSlicesAndTimings(Route r, int startPos) {
        if (startPos == r.getStartPoint()) {
            return regularOrder(r);
        } else {
            return reverseOrder(r);
        }
    }

    private static ArrayList[] reverseOrder(Route r) {
        ArrayList<Float> timeSlices = new ArrayList<>();
        ArrayList<LatLng> routePoints = new ArrayList<>();
        if (r.getIntermediates() == null) {
            timeSlices.add((float) ANIMATION_DURATION);
        } else {
            float duration;
            for (int i = r.getIntermediates().length; i >= 0; i--) {
                double x1;
                double y1;
                double x2;
                double y2;
                if (i == r.getIntermediates().length) {
                    x1 = Points.POINTS[r.getEndPoint() - 1].getLatitude();
                    y1 = Points.POINTS[r.getEndPoint() - 1].getLongitude();
                } else {
                    x1 = r.getIntermediates()[i].getLatitude();
                    y1 = r.getIntermediates()[i].getLongitude();
                }
                if (i == 0) {
                    x2 = Points.POINTS[r.getStartPoint() - 1].getLatitude();
                    y2 = Points.POINTS[r.getStartPoint() - 1].getLongitude();
                } else {
                    x2 = r.getIntermediates()[i - 1].getLatitude();
                    y2 = r.getIntermediates()[i - 1].getLongitude();
                }
                LatLng intermediate = new LatLng(x2, y2);
                duration = (float) (ANIMATION_DURATION
                        * ((Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) / r.getLength()));
                timeSlices.add(duration);
                if (i != 0) {
                    routePoints.add(intermediate);
                }
            }
        }
        return new ArrayList[]{routePoints, timeSlices};
    }

    private static ArrayList[] regularOrder(Route r) {
        ArrayList<Float> timeSlices = new ArrayList<>();
        ArrayList<LatLng> routePoints = new ArrayList<>();
        if (r.getIntermediates() == null) {
            timeSlices.add((float) ANIMATION_DURATION);
        } else {
            float duration;
            for (int i = 0; i <= r.getIntermediates().length; i++) {
                double x1;
                double y1;
                double x2;
                double y2;
                if (i == 0) {
                    x1 = Points.POINTS[r.getStartPoint() - 1].getLatitude();
                    y1 = Points.POINTS[r.getStartPoint() - 1].getLongitude();
                } else {
                    x1 = r.getIntermediates()[i - 1].getLatitude();
                    y1 = r.getIntermediates()[i - 1].getLongitude();
                }
                if (i == r.getIntermediates().length) {
                    x2 = Points.POINTS[r.getEndPoint() - 1].getLatitude();
                    y2 = Points.POINTS[r.getEndPoint() - 1].getLongitude();
                } else {
                    x2 = r.getIntermediates()[i].getLatitude();
                    y2 = r.getIntermediates()[i].getLongitude();
                }
                LatLng intermediate = new LatLng(x2, y2);
                duration = (float) (ANIMATION_DURATION
                        * ((Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) / r.getLength()));
                timeSlices.add(duration);
                if (i != r.getIntermediates().length) {
                    routePoints.add(intermediate);
                }
            }
        }
        return new ArrayList[]{routePoints, timeSlices};
    }

    public static ArrayList[] createGoBackRoute(LatLng latLng) {
        ArrayList<Float> timeSlices = new ArrayList<>();
        timeSlices.add((float) ANIMATION_DURATION);
        timeSlices.add((float) ANIMATION_DURATION);
        ArrayList<LatLng> routePoints = new ArrayList<>();
        routePoints.add(latLng);
        return new ArrayList[]{timeSlices, routePoints};
    }

    public static ArrayList[] createGoBackRoute(ArrayList<Float> timeSlices, ArrayList<LatLng> routePoints, Point p) {
        int size = timeSlices.size();
        for (int i = size - 1; i >= 0; i--) {
            timeSlices.add(timeSlices.get(i));
        }
        size = routePoints.size();
        routePoints.add(p.getLatLng());
        for (int i = size - 1; i >= 0; i--) {
            routePoints.add(routePoints.get(i));
        }
        return new ArrayList[]{timeSlices, routePoints};
    }

    public static int[] getIconAndTicket(Player player, int vehicle) {
        int icon;
        int ticket;
        switch (vehicle) {
            case 0:
                icon = R.drawable.pedestrian;
                ticket = R.drawable.ticket_yellow;
                player.decreaseNumberOfTickets(R.string.PEDESTRIAN_TICKET_KEY);
                break;
            case 1:
                icon = R.drawable.bicycle;
                ticket = R.drawable.ticket_orange;
                player.decreaseNumberOfTickets(R.string.BICYCLE_TICKET_KEY);
                break;
            case 2:
                icon = R.drawable.bus;
                ticket = R.drawable.ticket_red;
                player.decreaseNumberOfTickets(R.string.BUS_TICKET_KEY);
                break;
            case 3:
                icon = R.drawable.taxi;
                ticket = R.drawable.ticket_blue;
                player.decreaseNumberOfTickets(R.string.TAXI_TICKET_KEY);
                break;
            default:
                icon = -1;
                ticket = -1;
        }
        return new int[]{icon, ticket};
    }

    public static Entry getRoadMapEntry(int lastTurn, Point newLocation, int ticket) {
        Entry entry;
        if (lastTurn == 2 || lastTurn == 6 || lastTurn == 11) {
            entry = new PositionEntry(lastTurn + 1, Points.getIndex(newLocation));
        } else {
            entry = new TicketEntry(lastTurn + 1, ticket);
        }
        return entry;
    }


    public static int getSpecialMrXMoveTicket(Player player, int ticket) {
        if (player.getSpecialMrXMoves()[0] && player.getTickets().get(R.string.BLACK_TICKET_KEY).intValue() > 0) {
            player.setSpecialMrXMoves(false, 0);
            player.decreaseNumberOfTickets(R.string.BLACK_TICKET_KEY);
            return R.drawable.ticket_black;
        } else {
            return ticket;
        }
    }

    public static MarkerMovingRoute prepareMove(Player player, boolean randomRoute, ValidatedRoute randRoute, Point p) {
        LatLng current = player.getMarker().getPosition();
        Point currentPoint = new Point(current.latitude, current.longitude);
        Point playerLoc = player.getPosition();
        Point newLocation = p;
        LatLng finalPos;
        ValidatedRoute routeToTake = Routes.getRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
        Route r = routeToTake.getRoute();
        if (randomRoute) {
            r = randRoute.getRoute();
            if (Points.getIndex(playerLoc) + 1 == r.getStartPoint()) {
                p = Points.POINTS[r.getEndPoint() - 1];
            } else {
                p = Points.POINTS[r.getStartPoint() - 1];
            }
        }
        int[] iconAndTicket = MovingLogic.getIconAndTicket(player, routeToTake.getRouteType());
        int icon = iconAndTicket[0];
        int ticket = MovingLogic.getSpecialMrXMoveTicket(player, iconAndTicket[1]);
        if (player.getPenalty() > 0)
            player.decreasePenalty();
        if (randomRoute) {
            switch (randRoute.getRouteType()) {
                case 0:
                    icon = R.drawable.pedestrian;
                    break;
                case 1:
                    icon = R.drawable.bicycle;
                    break;
                case 2:
                    icon = R.drawable.bus;
                    break;
                case 3:
                    icon = R.drawable.taxi;
                    break;
            }
        }
        return new MarkerMovingRoute(icon, ticket, currentPoint, newLocation, r);
    }

    public static MarkerMovingRoute createMove(MarkerMovingRoute markerMove, boolean goBack, Player player) {
        ArrayList[] routeSliceTimings = getRouteSlicesAndTimings(markerMove.getRoute(), Points.getIndex(markerMove.getCurrentPosition()) + 1);
        ArrayList<LatLng> routePoints = (ArrayList) routeSliceTimings[0];
        ArrayList<Float> timeSlices = (ArrayList) routeSliceTimings[1];
        if (goBack) {
            createGoBackRoute(timeSlices, routePoints, markerMove.getNewLocation());
            markerMove.setFinalPosition(player.getMarker().getPosition());
            markerMove.setNewLocation(player.getPosition());
        } else {
            markerMove.setFinalPosition(markerMove.getNewLocation().getLatLng());
        }
        markerMove.setIntermediates(routePoints);
        markerMove.setTimeSlices(timeSlices);
        player.setPosition(markerMove.getNewLocation());
        return markerMove;
    }
}
