package com.example.scotlandyard.Map.Roadmap;

public class EntryVehicle extends Entry {
    private Vehicle vehicle;
    public EntryVehicle(int turn, Vehicle vehicle) {
        super(turn);
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
