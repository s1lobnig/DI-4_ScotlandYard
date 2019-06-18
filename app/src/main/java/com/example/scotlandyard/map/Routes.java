package com.example.scotlandyard.map;

import com.example.scotlandyard.Player;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static com.example.scotlandyard.map.routetypes.BicycleRoutes.getBicycle;
import static com.example.scotlandyard.map.routetypes.BusRoutes.getBUS;
import static com.example.scotlandyard.map.routetypes.FootRoutes.getByFoot;
import static com.example.scotlandyard.map.routetypes.TaxiRoutes.getTaxiDragan;

/**
 * @author René
 * <p>
 * this class contains the routes between the @FIELDS + additional methods
 */
public class Routes {

    private Routes() {

    }

    private static final Route[] BY_FOOT = getByFoot();
    private static final Route[] BICYCLE = getBicycle();
    private static final Route[] BUS = getBUS();
    private static final Route[] TAXI_DRAGAN = getTaxiDragan();
    public static final float ROUTE_WIDTH = 15f;

    public static Route[] getBicycleRoutes() {
        return BICYCLE;
    }

    public static Route[] getBusRoutes() {
        return BUS;
    }

    public static Route[] getByFootRoutes() {
        return BY_FOOT;
    }

    public static Route[] getTaxiDraganRoutes() {
        return TAXI_DRAGAN;
    }

    /**
     * get a route by BICYCLE between current and next point
     *
     * @param current ...current point (point number)
     * @param next    ......next point (point number)
     * @return route - if found
     * null - else
     */
    private static Route getByBicycleRoute(int current, int next) {
        for (Route r : BICYCLE) {
            if ((r.getStartPoint() == current && r.getEndPoint() == next) || (r.getStartPoint() == next && r.getEndPoint() == current)) {
                return r;
            }
        }
        return null;
    }

    /**
     * get a route by FOOT between current and next point
     *
     * @param current ...current point (point number)
     * @param next    ......next point (point number)
     * @return route - if found
     * null - else
     */
    private static Route getByFootRoute(int current, int next) {
        for (Route r : BY_FOOT) {
            if ((r.getStartPoint() == current && r.getEndPoint() == next) || (r.getStartPoint() == next && r.getEndPoint() == current)) {
                return r;
            }
        }
        return null;
    }

    /**
     * get a route by BUS between current and next point
     *
     * @param current ...current point (point number)
     * @param next    ......next point (point number)
     * @return route - if found
     * null - else
     */
    private static Route getByBusRoute(int current, int next) {
        for (Route r : BUS) {
            if ((r.getStartPoint() == current && r.getEndPoint() == next) || (r.getStartPoint() == next && r.getEndPoint() == current)) {
                return r;
            }
        }
        return null;
    }

    /**
     * get a route by TAXI_DRAGAN between current and next point
     *
     * @param current ...current point (point number)
     * @param next    ......next point (point number)
     * @return route - if found
     * null - else
     */
    private static Route getByTaxiDraganRoute(int current, int next) {
        for (Route r : TAXI_DRAGAN) {
            if ((r.getStartPoint() == current && r.getEndPoint() == next) || (r.getStartPoint() == next && r.getEndPoint() == current)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Returns a 3-Parameter-Object-[] corresponding to the inputs...
     *
     * @param current ...current Player-Location
     * @param next    ......new Player-Location
     * @return Object[]:
     * [0]: boolean isValid.....true - Valid new Location for current Location
     * false - else
     * [1]: Route route.........resulting route - if valid
     * null - else
     * [2]: int vehicleCode.....Vehicle code:
     * 0 = foot
     * 1 = bicycle
     * 2 = bus
     * 3 = taxi dragan
     * -1 = invalid
     */
    public static ValidatedRoute getRoute(int current, int next) {
        Route foot = getByFootRoute(current + 1, next + 1);
        if (foot != null)
            return new ValidatedRoute(foot, true, 0);
        Route bicycle = getByBicycleRoute(current + 1, next + 1);
        if (bicycle != null)
            return new ValidatedRoute(bicycle, true, 1);
        Route bus = getByBusRoute(current + 1, next + 1);
        if (bus != null)
            return new ValidatedRoute(bus, true, 2);
        Route taxiDragan = getByTaxiDraganRoute(current + 1, next + 1);
        if (taxiDragan != null)
            return new ValidatedRoute(taxiDragan, true, 3);
        return new ValidatedRoute(null, false, -1);
    }

    private static ArrayList<Route> getAllByFoot(int point, int notToInclude) {
        ArrayList<Route> routes = new ArrayList<>();
        for (Route r : BY_FOOT) {
            if ((r.getStartPoint() == point && r.getEndPoint() != notToInclude) || (r.getEndPoint() == point && r.getStartPoint() != notToInclude)) {
                routes.add(r);
            }
        }
        return routes;
    }

    private static ArrayList<Route> getAllByBicycle(int point, int notToInclude) {
        ArrayList<Route> routes = new ArrayList<>();
        for (Route r : BICYCLE) {
            if ((r.getStartPoint() == point && r.getEndPoint() != notToInclude) || (r.getEndPoint() == point && r.getStartPoint() != notToInclude)) {
                routes.add(r);
            }
        }
        return routes;
    }

    private static ArrayList<Route> getAllByBus(int point, int notToInclude) {
        ArrayList<Route> routes = new ArrayList<>();
        for (Route r : BUS) {
            if ((r.getStartPoint() == point && r.getEndPoint() != notToInclude) || (r.getEndPoint() == point && r.getStartPoint() != notToInclude)) {
                routes.add(r);
            }
        }
        return routes;
    }

    private static ArrayList<Route> getAllByTaxiDragan(int point, int notToInclude) {
        ArrayList<Route> routes = new ArrayList<>();
        for (Route r : TAXI_DRAGAN) {
            if ((r.getStartPoint() == point && r.getEndPoint() != notToInclude) || (r.getEndPoint() == point && r.getStartPoint() != notToInclude)) {
                routes.add(r);
            }
        }
        return routes;
    }

    private static ArrayList[] getAll(int current, int notNext) {
        ArrayList[] routes = new ArrayList[4];
        routes[0] = getAllByFoot(current, notNext);
        routes[1] = getAllByBicycle(current, notNext);
        routes[2] = getAllByBus(current, notNext);
        routes[3] = getAllByTaxiDragan(current, notNext);
        return routes;
    }

    public static ValidatedRoute getBotRoute(int current, List<Player> players) {
        int notNext = -1;
        ArrayList[] routes = getAll(current, notNext);
        ArrayList[] routesNoDetectives2 = new ArrayList[4];
        initialize(routesNoDetectives2);
        ArrayList[] routesNoDetectives1 = new ArrayList[4];
        initialize(routesNoDetectives1);

        for (int i = 0; i < routes.length; i++) {
            ArrayList<Route> routesI = routes[i];
            for (Route r : routesI) {
                if (noDetectiveOnTheRoute(r, players)) {
                    int next;
                    if (current == r.getStartPoint()) {
                        next = r.getEndPoint();
                    } else {
                        next = r.getStartPoint();
                    }
                    ArrayList[] stillPossible = getAll(next, notNext);
                    boolean isStillPossible = checkIfStillPossible(i, stillPossible, players);
                    if (isStillPossible) {
                        routesNoDetectives2[i].add(r);
                    } else {
                        routesNoDetectives1[i].add(r);
                    }
                }
            }
        }
        return getResultingBotMove(routes, routesNoDetectives2, routesNoDetectives1);
    }

    private static boolean checkIfStillPossible(int i, ArrayList[] stillPossible, List<Player> players) {
        for (int j = 0; j < stillPossible.length; j++) {
            ArrayList<Route> nextRoutes = stillPossible[i];
            for (Route r1 : nextRoutes) {
                if (!noDetectiveOnTheRoute(r1, players)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void initialize(ArrayList[] routes) {
        routes[0] = new ArrayList<Player>();
        routes[1] = new ArrayList<Player>();
        routes[2] = new ArrayList<Player>();
        routes[3] = new ArrayList<Player>();
    }

    private static ValidatedRoute getResultingBotMove(ArrayList[] routes, ArrayList[] noDetectivesIn1And2Moves, ArrayList[] noDetectivesIn1Move) {
        ArrayList<Integer> possibleRoutes = new ArrayList<>();
        SecureRandom secureRandom = new SecureRandom();
        int routeType;
        Route route;
        savePossibleRoutes(noDetectivesIn1And2Moves, possibleRoutes);
        if (!possibleRoutes.isEmpty()) {
            routeType = possibleRoutes.get(secureRandom.nextInt(possibleRoutes.size()));
            route = (Route) (noDetectivesIn1And2Moves[routeType]).get(secureRandom.nextInt(noDetectivesIn1And2Moves[routeType].size()));
        } else {
            savePossibleRoutes(noDetectivesIn1Move, possibleRoutes);
            if (!possibleRoutes.isEmpty()) {
                routeType = possibleRoutes.get(secureRandom.nextInt(possibleRoutes.size()));
                route = (Route) (noDetectivesIn1Move[routeType]).get(secureRandom.nextInt(noDetectivesIn1Move[routeType].size()));
            } else {
                savePossibleRoutes(routes, possibleRoutes);
                if (possibleRoutes.isEmpty()) {
                    return new ValidatedRoute(null, false, -1);
                }
                routeType = possibleRoutes.get(secureRandom.nextInt(possibleRoutes.size()));
                route = (Route) (routes[routeType]).get(secureRandom.nextInt(routes[routeType].size()));
            }
        }
        return new ValidatedRoute(route, true, routeType);
    }

    private static boolean noDetectiveOnTheRoute(Route r, List<Player> players) {
        for (Player p : players) {
            if (!p.isMrX()) {
                int playerLoc = Points.getIndex(p.getPosition()) + 1;
                if (playerLoc == r.getStartPoint() || playerLoc == r.getEndPoint()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Integer> savePossibleRoutes(ArrayList[] routes, List<Integer> possible) {
        for (int i = 0; i < routes.length; i++) {
            if (!routes[i].isEmpty()) {
                possible.add(i);
            }
        }
        return possible;
    }

    public static ValidatedRoute getRandomRoute(int current, int notNext) {
        ArrayList[] routes = getAll(current, notNext);
        ArrayList<Integer> possible = new ArrayList<>();
        for (int i = 0; i < routes.length; i++) {
            if (!routes[i].isEmpty()) {
                possible.add(i);
            }
        }
        SecureRandom secureRandom = new SecureRandom();
        int route = possible.get(secureRandom.nextInt(possible.size()));
        return new ValidatedRoute((Route) routes[route].get(secureRandom.nextInt(routes[route].size())), true, route);
    }

    public static boolean routesPossibleWithTickets(int current, Player player) {
        ArrayList[] routes = getAll(current, -1);
        int[] tickets = player.getRemainingTickets();
        for (int i = 0; i < tickets.length; i++) {
            if (tickets[i] > 0 && !routes[i].isEmpty() && (i == 1 && player.getPenalty() == 0 || i != 1)) {
                return true;
            }
        }
        player.setActive(false);
        return false;
    }
}
