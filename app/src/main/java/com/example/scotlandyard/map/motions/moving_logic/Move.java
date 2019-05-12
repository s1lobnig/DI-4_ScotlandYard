package com.example.scotlandyard.map.motions.moving_logic;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.Route;
import com.example.scotlandyard.map.Routes;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.map.roadmap.EntryPosition;
import com.example.scotlandyard.map.roadmap.EntryTicketTaken;
import com.example.scotlandyard.map.roadmap.RoadMap;
import com.example.scotlandyard.map.roadmap.Ticket;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Move {

    private Player p;
    private boolean isValid;
    private int vehicle;
    private Route route;
    private Entry entry;
    private int icon;

    public Move(Player p) {
        this.p = p;
        isValid = false;
        vehicle = -1;
        route = null;
        entry = null;
    }

    public void validateMove(Point from, Point to, RoadMap roadmap) {
        Object[] routeToTake = Routes.getRoute(Points.getIndex(from), Points.getIndex(to), p.isMrX());
        isValid = (boolean) routeToTake[0];
        route = (Route) routeToTake[1];
        vehicle = (int) routeToTake[2];
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
        if (p.isMrX()) {
            if (roadmap.getNumberOfEntries() == 2 || roadmap.getNumberOfEntries() == 6 || roadmap.getNumberOfEntries() == 11) {
                entry = new EntryPosition(roadmap.getNumberOfEntries() + 1, Points.getIndex(to));
            } else {
                if (ticket == -1) {
                    ticket = R.drawable.ticket_black;
                }
                entry = new EntryTicketTaken(roadmap.getNumberOfEntries() + 1, Ticket.get(ticket));
            }
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public Route getRoute() {
        return route;
    }

    public Entry getEntry() {
        return entry;
    }

    public int getIcon() {
        return icon;
    }

    /**
     * @param route             Route-Object
     * @param animationDuration Duration of the whole animation
     * @param startPos          Current location-index
     * @return { ArrayList<LatLang> in the correct order (either original (if
     * current position = route.StartPos) or revered) ArrayList<Float> which
     * contains the animation-duration-slices according to the
     * route-part-length }
     */
    public Object[] getRouteSlicesAndTimings(int animationDuration, int startPos) {
        if (startPos == route.getStartPoint()) {
            return regularOrder(animationDuration);
        } else {
            return reverseOrder(animationDuration);
        }
    }

    private Object[] reverseOrder(int animationDuration) {
        float duration;
        ArrayList<Float> timeSlices = new ArrayList<>();
        ArrayList<LatLng> routePoints = new ArrayList<>();
        for (int i = route.getIntermediates().length; i >= 0; i--) {
            double x1;
            double y1;
            double x2;
            double y2;
            if (i == route.getIntermediates().length) {
                x1 = Points.POINTS[route.getEndPoint() - 1].getLatitude();
                y1 = Points.POINTS[route.getEndPoint() - 1].getLongitude();
            } else {
                x1 = route.getIntermediates()[i].getLatitude();
                y1 = route.getIntermediates()[i].getLongitude();
            }
            if (i == 0) {
                x2 = Points.POINTS[route.getStartPoint() - 1].getLatitude();
                y2 = Points.POINTS[route.getStartPoint() - 1].getLongitude();
            } else {
                x2 = route.getIntermediates()[i - 1].getLatitude();
                y2 = route.getIntermediates()[i - 1].getLongitude();
            }
            LatLng intermediate = new LatLng(x2, y2);
            duration = (float) (animationDuration
                    * ((Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) / route.getLength()));
            timeSlices.add(duration);
            if (i != 0) {
                routePoints.add(intermediate);
            }
        }
        return new Object[]{routePoints, timeSlices};
    }

    private Object[] regularOrder(int animationDuration) {
        float duration;
        ArrayList<Float> timeSlices = new ArrayList<>();
        ArrayList<LatLng> routePoints = new ArrayList<>();
        for (int i = 0; i <= route.getIntermediates().length; i++) {
            double x1;
            double y1;
            double x2;
            double y2;
            if (i == 0) {
                x1 = Points.POINTS[route.getStartPoint() - 1].getLatitude();
                y1 = Points.POINTS[route.getStartPoint() - 1].getLongitude();
            } else {
                x1 = route.getIntermediates()[i - 1].getLatitude();
                y1 = route.getIntermediates()[i - 1].getLongitude();
            }
            if (i == route.getIntermediates().length) {
                x2 = Points.POINTS[route.getEndPoint() - 1].getLatitude();
                y2 = Points.POINTS[route.getEndPoint() - 1].getLongitude();
            } else {
                x2 = route.getIntermediates()[i].getLatitude();
                y2 = route.getIntermediates()[i].getLongitude();
            }
            LatLng intermediate = new LatLng(x2, y2);
            duration = (float) (animationDuration
                    * ((Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) / route.getLength()));
            timeSlices.add(duration);
            if (i != route.getIntermediates().length) {
                routePoints.add(intermediate);
            }
        }
        return new Object[]{routePoints, timeSlices};
    }
}
