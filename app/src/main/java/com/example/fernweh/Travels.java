package com.example.fernweh;

public class Travels {
    private String travelName, travelDestination, travelStartDate, travelEndDate, travelStartTime, travelEndTime;

    public Travels(String travelName, String dest, String stdate, String enddate, String sttime, String endtime) {
        this.travelName = travelName;
        this.travelDestination = dest;
        this.travelStartDate = stdate;
        this.travelEndDate = enddate;
        this.travelStartTime = sttime;
        this.travelEndTime = endtime;
    }

    public String getTravelName() {
        return travelName;
    }

    public void settravelName(String travelName) {
        this.travelName = travelName;
    }

    public String getTravelDestination() {
        return travelDestination;
    }

    public void setTravelDestination(String travelDestination) {
        this.travelDestination = travelDestination;
    }

    public String getTravelStartDate() {
        return travelStartDate;
    }

    public void setTravelStartDate(String travelStartDate) {
        this.travelStartDate = travelStartDate;
    }

    public String getTravelEndDate() {
        return travelEndDate;
    }

    public void setTravelEndDate(String travelEndDate) {
        this.travelEndDate = travelEndDate;
    }

    public String getTravelStartTime() {
        return travelStartTime;
    }

    public void setTravelStartTime(String travelStartTime) {
        this.travelStartTime = travelStartTime;
    }

    public String getTravelEndTime() {
        return travelEndTime;
    }

    public void setTravelEndTime(String travelEndTime) {
        this.travelEndTime = travelEndTime;
    }
}