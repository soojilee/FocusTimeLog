package com.leegacy.sooji.model;

/**
 * Created by soo-ji on 2016-03-07.
 */
public class SessionTotalRowModel extends RowModel{

    private String sessionNumber;
    private int sessionIndex;
    private int sessionOriginalIndex;
    private String startDate;
    private String EndDate;

    private int totalTime;
    private String categoryName;
//    public static final String SESSION_NAME = "SESSION_NAME";
    public static final String IS_NEW_SESSION ="IS_NEW_SESSION";
    public static final String CATEGORY_NAME ="CATEGORY_NAME";
    public static final String SESSION_NUMBER = "SESSION_NUMBER";
    private Boolean isActive;
    private int activeSeconds;

    public String getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSessionIndex() {
        return sessionIndex;
    }

    public void setSessionIndex(int sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public int getActiveSeconds() {
        return activeSeconds;
    }

    public void setActiveSeconds(int activeSeconds) {
        this.activeSeconds = activeSeconds;
    }

    public int getSessionOriginalIndex() {
        return sessionOriginalIndex;
    }

    public void setSessionOriginalIndex(int sessionOriginalIndex) {
        this.sessionOriginalIndex = sessionOriginalIndex;
    }
}
