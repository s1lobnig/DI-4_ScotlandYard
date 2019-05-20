package com.example.scotlandyard.map.motions;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.Route;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.map.roadmap.PositionEntry;
import com.example.scotlandyard.map.roadmap.TicketEntry;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Move implements Serializable {

    public static final int ANIMATION_DURATION = 3000;

    private String nickname;
    private int field;

    public Move(String nickname, int field) {
        this.nickname = nickname;
        this.field = field;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "Nickname = " + this.nickname + "; Field = " + this.field;
    }


    /**
     * @param r        Route-Object
     * @param startPos Current location-index
     * @return { ArrayList<LatLang> in the correct order (either original (if
     * current position = route.StartPos) or revered) ArrayList<Float> which
     * contains the animation-duration-slices according to the
     * route-part-length }
     */
    public static Object[] getRouteSlicesAndTimings(Route r, int startPos) {
        if (startPos == r.getStartPoint()) {
            return regularOrder(r);
        } else {
            return reverseOrder(r);
        }
    }

    private static Object[] reverseOrder(Route r) {
        float duration;
        ArrayList<Float> timeSlices = new ArrayList<>();
        ArrayList<LatLng> routePoints = new ArrayList<>();
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
        return new Object[]{routePoints, timeSlices};
    }

    private static Object[] regularOrder(Route r) {
        float duration;
        ArrayList<Float> timeSlices = new ArrayList<>();
        ArrayList<LatLng> routePoints = new ArrayList<>();
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
        return new Object[]{routePoints, timeSlices};
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

    public static int[] getIconAndTicket(int vehicle) {
        int icon;
        int ticket;
        switch (vehicle) {
            case 0:
                icon = R.drawable.pedestrian;
                ticket = R.drawable.ticket_yellow;
                break;
            case 1:
                icon = R.drawable.bicycle;
                ticket = R.drawable.ticket_orange;
                break;
            case 2:
                icon = R.drawable.bus;
                ticket = R.drawable.ticket_red;
                break;
            case 3:
                icon = R.drawable.taxi;
                ticket = R.drawable.ticket_black;
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
            entry = new PositionEntry(lastTurn + 1, Points.getIndex(newLocation) + 1);
        } else {
            entry = new TicketEntry(lastTurn + 1, ticket);
        }
        return entry;
    }

    public static void runMarkerAnimation(Player player, ArrayList<LatLng> intermediatePoints, ArrayList<Float> timeSlices, LatLng finalPosition, int icon, int finalIcon) {
        if (intermediatePoints == null || intermediatePoints.isEmpty()) {
            MarkerAnimation.moveMarkerToTarget(player.getMarker(), finalPosition, new LatLngInterpolator.Linear(), ANIMATION_DURATION, icon, finalIcon);
        } else {
            MarkerAnimation.moveMarkerToTarget(player.getMarker(), intermediatePoints, timeSlices, finalPosition, new LatLngInterpolator.Linear(), icon, finalIcon);
        }
    }
}
