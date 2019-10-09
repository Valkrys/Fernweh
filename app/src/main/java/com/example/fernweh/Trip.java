package com.example.fernweh;

public class Trip {
    private String tripName;
    private String tripDestination;
    private String tripStartDate;
    private String tripEndDate;

    public Trip(String tripName, String tripDestination, String tripStartDate, String tripEndDate) {
        this.tripName = tripName;
        this.tripDestination = tripDestination;
        this.tripStartDate= tripStartDate;
        this.tripEndDate = tripEndDate;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(String tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public String getTripEndDate() {
        return tripEndDate;
    }

    public void setTripEndDate(String tripEndDate) {
        this.tripEndDate = tripEndDate;
    }
}

