package com.ntu.cz3003.CMS.models;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.List;

public class Incident {
    @Exclude
    private String id;
    private Date createdAt;
    private String createdBy;
    private String description;
    private String incidentNo;
    private GeoPoint location;
    private String locationDescription;
    private String status;
    private String title;
    private String type;
    private Date updatedAt;
    private String imageUri;

    public Incident(){}

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
