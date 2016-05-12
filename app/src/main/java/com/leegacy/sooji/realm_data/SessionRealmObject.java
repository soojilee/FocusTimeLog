package com.leegacy.sooji.realm_data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by soo-ji on 2016-03-01.
 */
// Define you model class by extending the RealmObject
public class SessionRealmObject extends RealmObject {
    @Required // Name cannot be null
    private String name;
    private int originalIndex;
    private int groupOrignalIndex;
    private String categoryName;
    private Date startDate;
    private int startTime;
    private int endTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }

    public int getGroupOrignalIndex() {
        return groupOrignalIndex;
    }

    public void setGroupOrignalIndex(int groupOrignalIndex) {
        this.groupOrignalIndex = groupOrignalIndex;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    // ... Generated getters and setters ...
}
