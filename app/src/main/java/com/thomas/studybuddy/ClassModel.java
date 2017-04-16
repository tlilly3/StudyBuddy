package com.thomas.studybuddy;

/**
 * Created by thomas on 4/15/17.
 */

public class ClassModel {
    private String course;
    private String type;
    private Double numPeople;
    private Double capacity;
    private String building;
    private Double roomNumber;
    private String description;
    private Double lat;
    private Double lng;

    public ClassModel() {

    }

    public ClassModel(String course, String building, Double roomNumber, String description, Double numPeople, Double capacity) {
        this.course = course;
        this.building = building;
        this.roomNumber = roomNumber;
        this.description = description;
        this.numPeople = numPeople;
        this. capacity = capacity;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(Double numPeople) {
        this.numPeople = numPeople;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Double roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
    public String getLocationView() {
        return building + " " + roomNumber;
    }
    public String getCapcityView() {
        return numPeople + "/" + capacity;
    }
}
