package com.ntu.cz3003.CMS.models;

import java.util.Date;
import java.util.List;

public class Incidents {
    List<String> agenciies;
    Date createAt;
    Date createBy;
    String description;
    String incidentNo;
    String title;
    String type;
    Date updateAt;

    public Incidents(){}

    public List<String> getAgenciies() {
        return agenciies;
    }

    public void setAgenciies(List<String> agenciies) {
        this.agenciies = agenciies;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Date createBy) {
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
}
