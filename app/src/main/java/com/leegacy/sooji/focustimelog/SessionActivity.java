package com.leegacy.sooji.focustimelog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leegacy.sooji.extras.RowModelFactory;
import com.leegacy.sooji.extras.StopWatchFactory;
import com.leegacy.sooji.listeners.OnSessionActivityListener;
import com.leegacy.sooji.model.RowModel;
import com.leegacy.sooji.model.SessionTotalRowModel;
import com.leegacy.sooji.realm_data.CategoryRealmObject;
import com.leegacy.sooji.realm_data.GroupRealmObject;
import com.leegacy.sooji.realm_data.SessionRealmObject;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;


/**
 * Created by soo-ji on 2016-03-07.
 */
public class SessionActivity extends BaseActivity implements View.OnClickListener, OnSessionActivityListener, View.OnLongClickListener {
    private static final String TAG = "SESSION_ACTIVITY";

    private Timer timer;
    private int seconds;
    private TextView stop_watch_text;

    private CategoryRealmObject categoryRealmObject;
    private TextView sessionNumberTextView;
    private int sessionNumber;

    private GroupRealmObject groupRealmObject;
    private RecyclerView recyclerView;
    private SessionAdapter sessionAdapter;

    private boolean isStarted;
    private Boolean new_session;


    private SessionRealmObject currentSession;
    private TextView totalTimeTextView;
    private TextView catName;

    private int totalTime;

    private boolean newGroupAdded;
    private boolean firstEntry=true;
    private LinearLayoutManager lm;
    private String namePrimaryKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_activity);

        init();

        //if new group, add current session to group then add group session to category
        //if existing group, check if group is active, then current session = last session in group. Else, add new current session to realm
        createSession();

        //if current


    }

    //TODO: after deleting a session total row, update session numbers so array index will be correct

    private void resumeStopWatch() {
        long currentTime = new Date().getTime();
        seconds = groupRealmObject.getActiveSeconds() + (int) (currentTime - groupRealmObject.getPausedAt())/1000;
        if(currentTime < groupRealmObject.getPausedAt()){
            System.err.print("now is before before");
        }
        currentSession = new SessionRealmObject();
        //currentSession.setIsDone(false);

        currentSession.setName("Session");

        currentSession.setStartTime(0);
        currentSession.setStartDate(groupRealmObject.getLastStartDate());

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //stuff that updates ui
                        if (seconds < 0) {
                            stop_watch_text.setText("resumeStopWatch seconds: " + seconds + ", active seconds: " + groupRealmObject.getActiveSeconds() + "current: " + new Date().getTime() + "before: " + groupRealmObject.getPausedAt());
                            stop_watch_text.invalidate();
                        } else {
                            stop_watch_text.setText(StopWatchFactory.convertSecondsToTime(seconds));
                            stop_watch_text.invalidate();
                        }

                        //if you want total time to increase as the stop watch is running
//                                totalTimeTextView.setText("Total " + StopWatchFactory.convertSecondsToTime(totalTime + seconds));
//                                totalTimeTextView.invalidate();
                    }
                });
                seconds++;

            }
        }, 0, 1000);
        isStarted = !isStarted;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Realm realm = Realm.getDefaultInstance();
        if (!groupRealmObject.getIsDone()) { //if timer is running

            realm.beginTransaction();
            groupRealmObject.setActiveSeconds(seconds);
            groupRealmObject.setPausedAt(new Date().getTime());
            realm.commitTransaction();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Realm realm = Realm.getDefaultInstance();

        if (!groupRealmObject.getIsDone()) { //if timer is running

            realm.beginTransaction();
            groupRealmObject.setActiveSeconds(seconds);
            groupRealmObject.setPausedAt(new Date().getTime());
            realm.commitTransaction();

        }
        if (new_session && !newGroupAdded) {
            realm.beginTransaction();
            categoryRealmObject.setNumGroups(categoryRealmObject.getNumGroups() - 1);
            realm.commitTransaction();
        }


    }

    private void createSession() {
        if (new_session) {
            newGroupAdded = false;
            Log.e(TAG, "new session");
            newSessionCreate();
        } else {
            Log.e(TAG, "existing session");
            existingSessionCreate();
        }
    }

    private void init() {
        totalTimeTextView = (TextView) findViewById(R.id.totalTimeSession);
        stop_watch_text = (TextView) findViewById(R.id.stop_watch);
        stop_watch_text.setOnClickListener(this);
        stop_watch_text.setOnLongClickListener(this);

        sessionNumberTextView = (TextView) findViewById(R.id.sessionNumberTextView);
        sessionNumberTextView.setOnClickListener(this);

        new_session = getIntent().getBooleanExtra(CategoryRealmObject.IS_NEW_SESSION, true);

        namePrimaryKey = getIntent().getStringExtra(CategoryRealmObject.CATEGORY_OBJECT_PRIMARY_KEY);

        catName = (TextView) findViewById(R.id.catName);
        catName.setText(namePrimaryKey);

        Realm realm = Realm.getDefaultInstance();

        categoryRealmObject = realm.where(CategoryRealmObject.class).equalTo("name", namePrimaryKey).findFirst();

        recyclerView = (RecyclerView) findViewById(R.id.session_recycler_view);
        lm = new LinearLayoutManager(getBaseContext());
        //lm.setReverseLayout(true);
        recyclerView.setLayoutManager(lm);

        sessionAdapter = new SessionAdapter();
        sessionAdapter.setOnSessionActivityListener(this);
        recyclerView.setAdapter(sessionAdapter);
    }

    private void newSessionCreate() { //create group but don't put into realm until session is created
        //get category from realm and then get session number
        //sessionNumber = categoryRealmObject.getNumGroups() - categoryRealmObject.getNumDeleted();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        categoryRealmObject.setNumGroups(categoryRealmObject.getNumGroups() + 1);
        realm.commitTransaction();
//        if (categoryRealmObject.getGroups() == null) {
//            sessionNumber = 1;
//        } else {
//            sessionNumber = categoryRealmObject.getGroups().size() + 1;
//        }


        groupRealmObject = new GroupRealmObject();


        //groupRealmObject.setIndex(sessionNumber);
        groupRealmObject.setOriginalIndex(categoryRealmObject.getNumGroups());
        groupRealmObject.setName("Session " + groupRealmObject.getOriginalIndex());
//        groupRealmObject.setName("Session " + );
        sessionNumberTextView.setText(groupRealmObject.getName());

        totalTimeTextView.setText("Total " + StopWatchFactory.convertSecondsToTime(0));
        //updateRecyclerView();
    }

    private void existingSessionCreate() {
        sessionNumber = getIntent().getIntExtra(SessionTotalRowModel.SESSION_NUMBER, -1);

        for(GroupRealmObject gro: categoryRealmObject.getGroups()){
            if(gro.getOriginalIndex() == sessionNumber){
                groupRealmObject = gro;
                break;
            }
        }
        //groupRealmObject = categoryRealmObject.getGroups().get(sessionNumber);
        sessionNumberTextView.setText(groupRealmObject.getName());

        //if existing group, check if group is active, then current session = last session in group. Else, add new current session to realm
        if (!groupRealmObject.getIsDone()) {//group is active
            resumeStopWatch();
        }

        updateRecyclerView();
    }

    private void updateRecyclerView() {
//        sessionNumberTextView.setText(groupRealmObject.getName());

        List<RowModel> models = RowModelFactory.realmListOfSessionsToListOfSessionRowModels(groupRealmObject.getSessions(), namePrimaryKey);
        sessionAdapter.setModels(models);

        totalTime = RowModelFactory.getTotalTimeofSessions(models);

        totalTimeTextView.setText("Total " + StopWatchFactory.convertSecondsToTime(totalTime));


    }


    private String formatTimeString(int n) {
        if (n < 10) {
            return "0" + n;
        }
        return "" + n;
    }

    @Override
    public void onClick(View v) {
        final Realm realm = Realm.getDefaultInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (v.getId()) {
            case R.id.sessionNumberTextView:

                final EditText editTitle = new EditText(this);
                builder.setTitle(R.string.change_title)
                        .setView(editTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                String newGroupName = editTitle.getText().toString();
                                Realm realm = Realm.getDefaultInstance();

                                try {
                                    realm.beginTransaction();
                                    groupRealmObject.setName(newGroupName);
                                    realm.commitTransaction();
                                }catch(RealmPrimaryKeyConstraintException e){
                                    e.printStackTrace();
                                    realm.cancelTransaction();
                                    Toast.makeText(getBaseContext(), "Title \""+newGroupName+"\" already exists", Toast.LENGTH_SHORT).show();

                                    return;
                                }
                                Log.e(TAG, "new session title is " + groupRealmObject.getName());
                                sessionNumberTextView.setText(groupRealmObject.getName());
                                //updateRecyclerView();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.stop_watch:

                if (!isStarted) {
                    if (new_session && !newGroupAdded) {
                        Log.e(TAG, "NEW GROUP ADDED new group number = " + groupRealmObject.getOriginalIndex());
                        realm.beginTransaction();
                        categoryRealmObject.getGroups().add(0, groupRealmObject);
                        int size = categoryRealmObject.getGroups().size();
                        groupRealmObject = categoryRealmObject.getGroups().get(0);
                        realm.commitTransaction();
                        newGroupAdded = true;
                    }
                    if (groupRealmObject.getStartDate() == null) {
                        realm.beginTransaction();
                        groupRealmObject.setStartDate(new Date(System.currentTimeMillis()));
                        realm.commitTransaction();
                    }
                    realm.beginTransaction();
                    groupRealmObject.setIsDone(false);
                    groupRealmObject.setLastStartDate(new Date(System.currentTimeMillis()));
                    realm.commitTransaction();
                    timer = new Timer();        // A thread of execution is instantiated
                    seconds = 0;
                    //if(currentSession == null){
                    currentSession = new SessionRealmObject();
                    //currentSession.setIsDone(false);
                    currentSession.setName("session");
                    currentSession.setStartTime(seconds);
                    currentSession.setStartDate(new Date(System.currentTimeMillis()));



//                    realm.beginTransaction();
//                    groupRealmObject.getSessions().add(currentSession);
//                    realm.commitTransaction();
//                    int sizeSessions = groupRealmObject.getSessions().size();
//                    currentSession = groupRealmObject.getSessions().get(sizeSessions - 1);
                    //}
//                    realm.beginTransaction();
//                    currentSession.setStartTime(seconds);
//                    currentSession.setStartDate(new Date(System.currentTimeMillis()));
//                    realm.commitTransaction();

                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            seconds++;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(seconds < 0){
                                        stop_watch_text.setText("something wrong from onClick seconds: "+seconds);
                                        stop_watch_text.invalidate();
                                    }else {
                                        //stuff that updates ui
                                        stop_watch_text.setText(StopWatchFactory.convertSecondsToTime(seconds));
                                        stop_watch_text.invalidate();
                                    }
                                    //to show total time go up as stop watch runs
//                                    totalTimeTextView.setText("Total " + StopWatchFactory.convertSecondsToTime(totalTime + seconds));
//                                    totalTimeTextView.invalidate();
                                }
                            });

                        }
                    }, 0, 1000);
                    isStarted = !isStarted;
                } else {
                    firstEntry = false;
                    timer.cancel();
                    timer.purge();
                    timer = null;
                    //pausedAt = new Date().getTime();

                    final EditText addTitle = new EditText(this);
                    builder.setTitle(R.string.create_title)
                            .setView(addTitle)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                    String newSessionName = addTitle.getText().toString();
                                    currentSession.setName(newSessionName);
                                    currentSession.setEndTime(seconds);
                                    currentSession.setCategoryName(categoryRealmObject.getName());
                                    currentSession.setGroupOrignalIndex(groupRealmObject.getOriginalIndex());
                                    Realm realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    groupRealmObject.setNumSessions(groupRealmObject.getNumSessions()+1);
                                    realm.commitTransaction();
                                    currentSession.setOriginalIndex(groupRealmObject.getNumSessions());
                                    seconds = 0;
                                    stop_watch_text.setText(StopWatchFactory.convertSecondsToTime(seconds));

                                    realm.beginTransaction();
                                    groupRealmObject.getSessions().add(0,currentSession);
                                    groupRealmObject.setEndDate(new Date(System.currentTimeMillis()));
                                    groupRealmObject.setIsDone(true);
                                    realm.commitTransaction();
                                    currentSession = null;
                                    isStarted = !isStarted;
                                    updateRecyclerView();


                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    negative();

                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    negative();
                                }
                            });

                    AlertDialog addTitleDialog = builder.create();
                    addTitleDialog.show();


                }

                //startButton.setText(isStarted? "Stop" : "Start");

                // 0 is the time in second from when this code is to be executed
                // 1000 is time in millisecond after which it has to repeat
                break;
//            case R.id.resetButton:
//                timer.cancel();
//                timer.purge();
//                seconds = 0;
//                stop_watch_text.setText(StopWatchFactory.convertSecondsToTime(seconds));
//                break;

        }
    }

    private void negative() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(seconds < 0){
                            stop_watch_text.setText("something wrong from negative() seconds: "+seconds);
                            stop_watch_text.invalidate();
                        }else {

                            //stuff that updates ui
                            stop_watch_text.setText(StopWatchFactory.convertSecondsToTime(seconds));
                            stop_watch_text.invalidate();
                        }
                    }
                });
                seconds++;

            }
        }, 0, 1000);
    }


    //    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState){
//        pausedAt = new Date().getTime();
//        Log.e(TAG, "save");
//        savedInstanceState.putBoolean("isStarted", isStarted);
//        savedInstanceState.putLong("pausedAt", pausedAt);
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onUpdateRequested() {
        updateRecyclerView();
    }



    @Override
    public boolean onLongClick(View v) {
        if(!groupRealmObject.getIsDone()){ //cancel timer
            timer.cancel();
            timer.purge();
            timer = null;
            seconds = 0;
            stop_watch_text.setText(StopWatchFactory.convertSecondsToTime(seconds));
            currentSession = null;
            isStarted = !isStarted;
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            groupRealmObject.setIsDone(true);
            if(firstEntry==true){
                groupRealmObject.setStartDate(null);
            }
            realm.commitTransaction();

        }
        return true;
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.e(TAG, "pausedAt onResume " + pausedAt);
//        if(isStarted) {
//            long currentTime = new Date().getTime();
//            seconds = seconds + (int) (currentTime - pausedAt) / 1000;
//            Log.e(TAG, "current time " + currentTime);
//            Log.e(TAG, "paused at " + pausedAt);
//            timer = new Timer();
//            timer.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            //stuff that updates ui
//                            stop_watch_text.setText(StopWatchFactory.convertSecondsToTime(seconds));
//                            stop_watch_text.invalidate();
//                        }
//                    });
//                    seconds++;
//
//                }
//            }, 0, 1000);
//            isStarted = !isStarted;
//        }
//    }
}
