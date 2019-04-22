package com.magdy.tasksapp.models;

import java.io.Serializable;
import java.util.Calendar;

public class Task implements Serializable {

    String title;
    boolean isDone;
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

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
