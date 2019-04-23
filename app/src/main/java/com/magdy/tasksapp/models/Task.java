package com.magdy.tasksapp.models;

import java.io.Serializable;
import java.util.Calendar;

public class Task implements Serializable {

    String title, key;
    boolean done;
    int priority;

    long createdAt, updatedAt;

    public Task() {
        createdAt = Calendar.getInstance().getTimeInMillis();
        updatedAt = Calendar.getInstance().getTimeInMillis();
    }

    public Task(String title) {
        this.title = title;
        createdAt = Calendar.getInstance().getTimeInMillis();
        updatedAt = Calendar.getInstance().getTimeInMillis();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
