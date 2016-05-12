package com.leegacy.sooji.extras;

import android.util.Log;

import com.leegacy.sooji.model.RowModel;
import com.leegacy.sooji.model.SessionRowModel;
import com.leegacy.sooji.model.SessionTotalRowModel;
import com.leegacy.sooji.realm_data.CategoryRealmObject;
import com.leegacy.sooji.realm_data.GroupRealmObject;
import com.leegacy.sooji.realm_data.SessionRealmObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by soo-ji on 2016-03-03.
 */
public class RowModelFactory {
    private static final String TAG = "RowModelFactory";

//    public static List<RowModel> categoryToRowModel(CategoryRealmObject cro){
//        List<RowModel> rowModels = new ArrayList<>();
//        long total_time = 0;
//        for(int i=0;i<cro.getGroups().size(); i++){
//            rowModels.addAll(groupToRowModel(cro.getGroups().get(i)));
//            total_time = total_time + getTotalTimeofSessions(realmListOfSessionsToListOfSessionRowModels(cro.getGroups().get(i).getSessions()));
//        }
//
////        //total time of groups
////        LabelRowModel total_time_label = new LabelRowModel();
////        total_time_label.setLabel("total king: "+ total_time +"seconds");
////        rowModels.add(total_time_label);
//
//        return rowModels;
//    }

    public static List<RowModel> categoryFragmentToRowModel(CategoryRealmObject cro){
        List<RowModel> rowModels = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        long total_time = 0;
        for(int i=0;i<cro.getGroups().size(); i++){
            realm.beginTransaction();
            cro.getGroups().get(i).setCatName(cro.getName());
            realm.commitTransaction();
            SessionTotalRowModel stm = groupToSessionTotalRowModel(cro.getGroups().get(i), cro.getName());
            rowModels.add(stm);
            total_time = total_time + stm.getTotalTime();
        }
//        LabelRowModel lrm = new LabelRowModel();
//        lrm.setLabel("total king: "+ total_time);
//        rowModels.add(lrm);
        return rowModels;
    }
    public static String formatDateString(Date date){
        if(date == null){
            return "";
        }else {
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String month = months[date.getMonth()];
            return month + " " + date.getDate();
        }
    }

    public static String formatDatetoTimeString(Date date){
        int hour = date.getHours();
        String ampm;
        if(hour < 12){
            ampm = "AM";
        }else{
            ampm = "PM";
        }

        if(hour>12){
            hour -= 12;
        }
        if(hour == 0){
            hour = 12;
        }
        String min = date.getMinutes()+"";
        if(min.length()<2){
            min = "0"+min;
        }
        return hour+":"+min+ampm;
    }

    public static SessionTotalRowModel groupToSessionTotalRowModel(GroupRealmObject group, String categoryName){
        RealmList<SessionRealmObject> sessions = group.getSessions();
        SessionTotalRowModel stm = new SessionTotalRowModel();
        int total_time=0;
        for(SessionRealmObject sro : sessions){
            total_time += sro.getEndTime()-sro.getStartTime();
        }

        stm.setStartDate(formatDateString(group.getStartDate()));
        if(group.getEndDate() != null) {
            stm.setEndDate(formatDateString(group.getEndDate()));
        }
        if(stm.getStartDate().equals(stm.getEndDate())){
            stm.setEndDate("");
        }
        stm.setIsActive(!group.getIsDone());
//        if(group.getIsPaused()) {
//            int size = group.getSessions().size();
//            stm.setActiveSeconds(group.getSessions().get(size-1).getActiveSeconds());
//        }
        stm.setSessionIndex(group.getIndex());
        stm.setSessionOriginalIndex(group.getOriginalIndex());
        //stm.setSessionNumber(group.getName());

        //TODO: uncomment later
//        if(group.getName().length() > 0){
            stm.setSessionNumber(group.getName());
//        }else {
//            stm.setSessionNumber("Session" +group.getOriginalIndex());
//        }
        stm.setTotalTime(total_time);

        stm.setCategoryName(categoryName);
        return stm;
    }



    public static int getTotalTimeofSessions(List<RowModel> sessions){
        int total = 0;
        for(RowModel sro : sessions){
            if(sro instanceof  SessionRowModel) {
                total += ((SessionRowModel) sro).getDuration();
            }else{
                Log.e(TAG,"Critial error. ");
            }
        }
        return total;
    }

    public static List<RowModel> realmListOfSessionsToListOfSessionRowModels(RealmList<SessionRealmObject> input, String catName){
        List<RowModel> rowModels = new ArrayList<>();

        for(SessionRealmObject sro : input){
//            if(sro.getIsDone()) {
                SessionRowModel srm = new SessionRowModel();
                srm.setCategoryName(catName);
                srm.setStartTime(sro.getStartTime());
                srm.setEndTime(sro.getEndTime());
                srm.setStartDate(sro.getStartDate());
                srm.setOriginalIndex(sro.getOriginalIndex());
                srm.setGroupOriginalIndex(sro.getGroupOrignalIndex());
                srm.setName(sro.getName());
                srm.setDurationString(StopWatchFactory.convertSecondsToTime(srm.getDuration()));
                rowModels.add(srm);
//            }
        }

        return rowModels;
    }
}
