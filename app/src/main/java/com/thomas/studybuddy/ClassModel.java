package com.thomas.studybuddy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 4/15/17.
 */
@IgnoreExtraProperties
public class ClassModel implements Parcelable {
    private String course;
    private String type;
    private Double numPeople;
    private Double capacity;
    private String building;
    private Double roomNumber;
    private String description;
    private Double lat;
    private Double lng;
    private String hostUID;
    private String host;
    private List<String> participants;

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

    public ClassModel (Parcel input) {
        course = input.readString();
        type = input.readString();
        numPeople = input.readDouble();
        capacity = input.readDouble();
        building = input.readString();
        roomNumber = input.readDouble();
        description = input.readString();
        lat = input.readDouble();
        lng = input.readDouble();
        hostUID = input.readString();
        host = input.readString();
        participants = new ArrayList<>();
        input.readStringList(participants);
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
        return building + " " + (int)roomNumber.doubleValue();
    }
    public String getCapcityView() {
        return (int)numPeople.doubleValue() + "/" + (int)capacity.doubleValue();
    }

    @Override
    public int describeContents() {
        return 9;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(course);
        dest.writeString(type);
        dest.writeDouble(numPeople);
        dest.writeDouble(capacity);
        dest.writeString(building);
        dest.writeDouble(roomNumber);
        dest.writeString(description);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(hostUID);
        dest.writeString(host);
        dest.writeStringList(participants);
    }
    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new ClassModel(source);
        }

        @Override
        public ClassModel[] newArray(int size) {
            return new ClassModel[size];
        }
    };

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getHostUID() {
        return hostUID;
    }

    public void setHostUID(String hostUID) {
        this.hostUID = hostUID;
    }
}
