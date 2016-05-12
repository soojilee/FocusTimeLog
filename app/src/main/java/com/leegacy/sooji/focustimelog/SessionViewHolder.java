package com.leegacy.sooji.focustimelog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.leegacy.sooji.model.RowModel;
import com.leegacy.sooji.model.SessionRowModel;
import com.leegacy.sooji.realm_data.CategoryRealmObject;
import com.leegacy.sooji.realm_data.GroupRealmObject;
import com.leegacy.sooji.realm_data.SessionRealmObject;

import java.util.Date;

import io.realm.Realm;

/**
 * Created by soo-ji on 2016-03-01.
 */
public class SessionViewHolder extends RowViewHolder {
    private static final String TAG = "SESSION VIEW HOLDER";
    private final TextView sessionDurationTextView;
    private final TextView sessionStartDateTextView;
    private final TextView sessionViewDateTextView;
    private SessionAdapter sessionAdapter;


    private SessionRowModel model;

    public SessionViewHolder(final View itemView) {
        super(itemView);
        sessionDurationTextView = (TextView) itemView.findViewById(R.id.sessionDurationTextView);
        sessionStartDateTextView = (TextView) itemView.findViewById(R.id.sessionStartDateTextView);
        sessionViewDateTextView = (TextView) itemView.findViewById(R.id.sessionViewDate);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                final EditText editTitle = new EditText(itemView.getContext());
                builder.setTitle(R.string.change_title)
                        .setView(editTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                String newSessionName = editTitle.getText().toString();
                                Realm realm = Realm.getDefaultInstance();

                                realm.beginTransaction();
                                findCurrentSession().setName(newSessionName);
                                realm.commitTransaction();

                                getSessionAdapter().getOnSessionActivityListener().onUpdateRequested();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage(R.string.inner_session_delete_msg)
                        .setTitle(R.string.inner_session_delete_dialog_title)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                deleteSession();
                                //update MainActivity to update the category object on cat frag page
                                //        getSessionAdapter().getCategoryFragment().getOnCategoryObjectListener().onGroupDeleteRequested(getSessionAdapter().getCategoryFragment());
                                //update the cat frag page


                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

    }

    private SessionRealmObject findCurrentSession() {
        Realm realm = Realm.getDefaultInstance();
        //TODO: category에 original index쓰기 왜냐면 이름은 바꿜수 있으니까
        CategoryRealmObject categoryRealmObject = realm.where(CategoryRealmObject.class).equalTo("name", model.getCategoryName()).findFirst();
        GroupRealmObject groupRealmObject = new GroupRealmObject();
        Log.e(TAG, "cro: "+ categoryRealmObject);
        Log.e(TAG, "croName: "+ model.getCategoryName());


        for (GroupRealmObject gro : categoryRealmObject.getGroups()) {
            if (gro.getOriginalIndex() == model.getGroupOriginalIndex()) {
                groupRealmObject = gro;
                break;
            }
        }
        for (SessionRealmObject sro : groupRealmObject.getSessions()) {
            if (sro.getOriginalIndex() == model.getOriginalIndex()) {
                return sro;
            }
        }
        return null;
    }

    private void deleteSession() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        findCurrentSession().removeFromRealm();
        realm.commitTransaction();

        getSessionAdapter().getOnSessionActivityListener().onUpdateRequested();

    }

    @Override
    public void update(RowModel rowModel) {
        model = (SessionRowModel) rowModel;
        if (((SessionRowModel) rowModel).getName() != null) {
            sessionStartDateTextView.setText(((SessionRowModel) rowModel).getName());
        } else {
            sessionStartDateTextView.setText(formatDateString(model.getStartDate()));
        }
        sessionDurationTextView.setText(model.getDurationString());
        sessionViewDateTextView.setText(formatDateString(model.getStartDate()));


    }

    private String formatDateString(Date date) {
        String month = date.getMonth() + 1 + "";
        if (month.length() < 2) {
            month = "0" + month;
        }
        int hour = date.getHours();
        String ampm;
        if (hour < 12) {
            ampm = "AM";
        } else {
            ampm = "PM";
        }

        if (hour > 12) {
            hour -= 12;
        }
        if (hour == 0) {
            hour = 12;
        }
        String min = date.getMinutes() + "";
        if (min.length() < 2) {
            min = "0" + min;
        }
        return month + "/" + date.getDate() + "/" + (date.getYear() - 100) + " " + hour + ":" + min + ampm;

    }


    public SessionAdapter getSessionAdapter() {
        return sessionAdapter;
    }

    public void setSessionAdapter(SessionAdapter sessionAdapter) {
        this.sessionAdapter = sessionAdapter;
    }
}
