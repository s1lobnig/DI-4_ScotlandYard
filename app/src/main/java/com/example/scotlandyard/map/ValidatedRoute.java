package com.example.scotlandyard.map;

import java.io.Serializable;

public class ValidatedRoute implements Serializable {
    private Route route;
    private boolean valid;
    private int routeType;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public boolean isValid() {
        return valid;
    }

    public int getRouteType() {
        return routeType;
    }

    public ValidatedRoute(Route route, boolean valid, int routeType) {
        this.route = route;
        this.valid = valid;
        this.routeType = routeType;
    }
}
