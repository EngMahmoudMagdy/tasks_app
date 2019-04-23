package com.magdy.tasksapp.models;

import java.io.Serializable;
import java.util.Calendar;

public class Comment implements Serializable {
    String details;
    long createdAt;
    long updatedAt;

    public Comment(String details) {
        this.details = details;
        createdAt = Calendar.getInstance().getTimeInMillis();
        updatedAt = Calendar.getInstance().getTimeInMillis();
    }

    public Comment() {
        createdAt = Calendar.getInstance().getTimeInMillis();
        updatedAt = Calendar.getInstance().getTimeInMillis();
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
