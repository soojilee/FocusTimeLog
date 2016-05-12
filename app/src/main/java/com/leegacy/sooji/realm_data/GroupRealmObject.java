package com.leegacy.sooji.realm_data;


import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by soo-ji on 2016-03-01.
 */
// Define you model class by extending the RealmObject
public class GroupRealmObject extends RealmObject {
    @PrimaryKey
    private String name;
    private int index;
    private int originalIndex;
    private RealmList<SessionRealmObject> sessions;
    private Date startDate;
    private Date endDate;
    private Boolean isDone = true;
    private String catName;
    private int activeSeconds;
    private long pausedAt;
    private Date lastStartDate;
    private int numSessions=0;




    public GroupRealmObject() {
        //this.name = (int) Math.random();
        sessions = new RealmList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RealmList<SessionRealmObject> getSessions() {
        return sessions;
    }

    public void setSessions(RealmList<SessionRealmObject> sessions) {
        this.sessions = sessions;
    }



    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }



    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public int getActiveSeconds() {
        return activeSeconds;
    }

    public void setActiveSeconds(int activeSeconds) {
        this.activeSeconds = activeSeconds;
    }

    public long getPausedAt() {
        return pausedAt;
    }

    public void setPausedAt(long pausedAt) {
        this.pausedAt = pausedAt;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getLastStartDate() {
        return lastStartDate;
    }

    public void setLastStartDate(Date lastStartDate) {
        this.lastStartDate = lastStartDate;
    }

    public int getNumSessions() {
        return numSessions;
    }

    public void setNumSessions(int numSessions) {
        this.numSessions = numSessions;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }


    // ... Generated getters and setters ...
}
