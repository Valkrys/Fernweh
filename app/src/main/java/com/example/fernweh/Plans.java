package com.example.fernweh;

public class Plans {
    private String planName;
    private String plandestination;
    private String planstartDate;
    private String plansendDate;
    private String plansstartTime;
    private String plansendTime;

    public Plans(String planName, String plandestination, String planstartDate, String plansendDate, String plansstartTime, String plansendTime) {
        this.planName = planName;
        this.plandestination = plandestination;
        this.planstartDate = planstartDate;
        this.plansendDate = plansendDate;
        this.plansstartTime = plansstartTime;
        this.plansendTime = plansendTime;
    }

    public String getplanName() {
        return planName;
    }

    public void setplanName(String planName) {
        this.planName = planName;
    }

    public String getPlandestination() {
        return plandestination;
    }

    public void setPlandestination(String plandestination) {
        this.plandestination = plandestination;
    }

    public String getPlanstartDate() {
        return planstartDate;
    }

    public void setPlanstartDate(String planstartDate) {
        this.planstartDate = planstartDate;
    }

    public String getPlansendDate() {
        return plansendDate;
    }

    public void setPlansendDate(String plansendDate) {
        this.plansendDate = plansendDate;
    }

    public String getPlansstartTime() {
        return plansstartTime;
    }

    public void setPlansstartTime(String plansstartTime) {
        this.plansstartTime = plansstartTime;
    }

    public String getPlansendTime() {
        return plansendTime;
    }

    public void setPlansendTime(String plansendTime) {
        this.plansendTime = plansendTime;
    }
}