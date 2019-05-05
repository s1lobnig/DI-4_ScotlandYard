package com.example.scotlandyard.Map.Roadmap;

public enum Vehicle {

    FOOT("foot"),BICYCLE("bicycle"),BUS("bus"),TAXI_DRAGAN("taxi dragan");

    private final String sign;
    Vehicle(String sign){
        this.sign = sign;
    }

    public Vehicle get(String sign){
        switch(sign){
            case "foot":
                return FOOT;
            case "bicycle":
                return BICYCLE;
            case "bus":
                return BUS;
            case "taxi dragan":
                return TAXI_DRAGAN;
            default:
                return null;
        }
    }

    public String getSign() {
        return sign;
    }
}
