package com.ntu.cz3003.CMS.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.List;

public class Incidents {
    @Exclude
    private String id;
    private Date createAt;
    private String createBy;
    private String description;
    private String incidentNo;
    private GeoPoint location;
    private String locationDescription;
    private String title;
    private String type;
    private Date updateAt;
    private String status;

    public Incidents(){}

    public Incidents(Date createAt, String createBy, String description, GeoPoint location, String locationDescription, String title, String type, String status) {
        this.createAt = createAt;
        this.createBy = createBy;
        this.description = description;
        this.location = location;
        this.locationDescription = locationDescription;
        this.title = title;
        this.type = type;
        this.status = status;
    }

    /**
     * Gets unique ID.
     * @return unique ID.
     */
    @Exclude
    public String getId() {
        return id;
    }

    /**
     * Changes unique id
     * @param id set unique id.
     */
    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIncidentNo() {
        return incidentNo;
    }

    public void setIncidentNo(String incidentNo) {
        this.incidentNo = incidentNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
