package com.thomas.studybuddy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by thomas on 4/15/17.
 */
@IgnoreExtraProperties
public class ClassModel implements Parcelable {
    private String course;
    private String type;
    private Long numPeople;
    private Long capacity;
    private String building;
    private Long roomNumber;
    private String description;
    private Double lat;
    private Double lng;
    private String hostUID;
    private String host;
    private List<String> participants;
    private String date;
    private Long cost;
    private String key;
    private Long offer;

    public ClassModel() {

    }

    public ClassModel(String course, String building, Long roomNumber, String description, Long numPeople, Long capacity) {
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
        numPeople = input.readLong();
        capacity = input.readLong();
        building = input.readString();
        roomNumber = input.readLong();
        description = input.readString();
        lat = input.readDouble();
        lng = input.readDouble();
        hostUID = input.readString();
        host = input.readString();
        participants = new ArrayList<>();
        input.readStringList(participants);
        date = input.readString();
        cost = input.readLong();
        key = input.readString();
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

    public Long getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(Long numPeople) {
        this.numPeople = numPeople;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Long roomNumber) {
        this.roomNumber = roomNumber;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public int describeContents() {
        return 9;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(course);
        dest.writeString(type);
        dest.writeLong(numPeople);
        dest.writeLong(capacity);
        dest.writeString(building);
        dest.writeLong(roomNumber);
        dest.writeString(description);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(hostUID);
        dest.writeString(host);
        dest.writeStringList(participants);
        dest.writeString(date);
        dest.writeLong(cost);
        dest.writeString(key);
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoneyView() {
        if (cost != null) {
            long reduced = (cost / 10) + 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < reduced; i++) {
                sb.append("$");
            }
            return sb.toString();
        }
        return null;
    }
    public String getCostView() {
        if (offer == null) {
            return "$-";
        }
        return "$" + (int)offer.doubleValue();
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getCost() {
        return cost;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setOffer(Long offer) {
        this.offer = offer;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ClassModel) {
            ClassModel other = (ClassModel) o;
            return other.course.equals(this.course) && other.description.equals(this.description);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }


}
