package com.leegacy.sooji.model;

import java.util.Date;

/**
 * Created by soo-ji on 2016-03-03.
 */
public class SessionRowModel extends RowModel{
    private int startTime;
    private int endTime;
    private Date startDate;
    private String durationString;
    private int originalIndex;
    private int groupOriginalIndex;
    private String categoryName;
    private String name;

    public int getDuration(){
        return endTime-startTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }

    public int getGroupOriginalIndex() {
        return groupOriginalIndex;
    }

    public void setGroupOriginalIndex(int groupOriginalIndex) {
        this.groupOriginalIndex = groupOriginalIndex;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
