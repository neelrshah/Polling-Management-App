package com.developer.pollingmanagementsystem;

public class PollingDataModel {

    public String time,boothNumber;
    public  int maleCount = 0,femaleCount = 0,totalCount = 0;
    public float percentage;

    public PollingDataModel() {
    }

    public PollingDataModel(String time, String boothNumber, int maleCount, int femaleCount, int totalCount, float percentage) {
        this.time = time;
        this.boothNumber = boothNumber;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.totalCount = totalCount;
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBoothNumber() {
        return boothNumber;
    }

    public void setBoothNumber(String boothNumber) {
        this.boothNumber = boothNumber;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
