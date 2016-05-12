package com.leegacy.sooji.focustimelog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leegacy.sooji.extras.StopWatchFactory;
import com.leegacy.sooji.fragment.CategoryFragment;
import com.leegacy.sooji.listeners.OnCategoryFragmentListener;
import com.leegacy.sooji.model.RowModel;
import com.leegacy.sooji.model.SessionTotalRowModel;
import com.leegacy.sooji.realm_data.CategoryRealmObject;
import com.leegacy.sooji.realm_data.GroupRealmObject;

import io.realm.Realm;

/**
 * Created by soo-ji on 2016-03-07.
 */
public class SessionTotalViewHolder extends RowViewHolder {
    private static final String TAG = "SessionTotalViewHolder";
    private final TextView sessionTotalStartDateTextView;
    private final TextView sessionTotalEndDateTextView;
    private final View line;
    private final RelativeLayout sessionTotalViewContainer;
    private OnCategoryFragmentListener onCategoryFragmentListener;
    private final TextView sessionTotalTextView;
    private final TextView sessionTotalTitleTextView;
    private SessionTotalRowModel model;
    private SessionAdapter sessionAdapter;


    public SessionTotalViewHolder(final View itemView) {
        super(itemView);
        onCategoryFragmentListener = new CategoryFragment();
        sessionTotalTextView = (TextView) itemView.findViewById(R.id.sessionTotalTextView);
        sessionTotalTitleTextView = (TextView) itemView.findViewById(R.id.sessionTitleTextView);
        sessionTotalStartDateTextView = (TextView) itemView.findViewById(R.id.sessionTotalStartDate);
        sessionTotalEndDateTextView = (TextView) itemView.findViewById(R.id.sessionTotalEndDate);
        line = (View) itemView.findViewById(R.id.line);
        sessionTotalViewContainer = (RelativeLayout) itemView.findViewById(R.id.sessionTotalViewContainer);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), SessionActivity.class);
                intent.putExtra(SessionTotalRowModel.IS_NEW_SESSION, false);

//                intent.putExtra(SessionTotalRowModel.CATEGORY_NAME, model.getCategoryName());
                Log.e(TAG, "cat name:  " +  model.getCategoryName());

                intent.putExtra(CategoryRealmObject.CATEGORY_OBJECT_PRIMARY_KEY, model.getCategoryName());
                intent.putExtra(SessionTotalRowModel.SESSION_NUMBER, model.getSessionOriginalIndex());
                itemView.getContext().startActivity(intent);

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage(R.string.session_delete_dialog_message)
                        .setTitle(R.string.session_delete_dialog_title)
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

    private void deleteSession() {
        Realm realm = Realm.getDefaultInstance();
        CategoryRealmObject categoryRealmObject = realm.where(CategoryRealmObject.class).equalTo("name", model.getCategoryName()).findFirst();

//        Log.e(TAG, "number of groups before deleting: " + categoryRealmObject.getGroups().size());
        for (GroupRealmObject gro : categoryRealmObject.getGroups()) {
            if (gro.getOriginalIndex() == model.getSessionOriginalIndex()) {
                realm.beginTransaction();
                gro.removeFromRealm();
                realm.commitTransaction();
                break;
            }
        }

        getSessionAdapter().getOnCategoryFragmentListener().onUpdateRequested();

    }

    @Override
    public void update(RowModel rowModel) {
        model = (SessionTotalRowModel) rowModel;
        sessionTotalTextView.setText("" + StopWatchFactory.convertSecondsToTime(model.getTotalTime()));
        sessionTotalTitleTextView.setText(model.getSessionNumber());
        sessionTotalStartDateTextView.setText(model.getStartDate());
        line.setVisibility(View.INVISIBLE);
//        sessionTotalEndDateTextView.setText(model.getEndDate());
        if (model.getEndDate() != null && !model.getEndDate().equals("")) {
            line.setVisibility(View.VISIBLE);
            sessionTotalEndDateTextView.setText(model.getEndDate());
        }
        if (model.getIsActive()) {
            sessionTotalViewContainer.setBackgroundColor(Color.parseColor("#FDE3A7"));


            sessionTotalTextView.setTextColor(Color.parseColor("#F9690E"));

            sessionTotalTitleTextView.setTextColor(Color.parseColor("#F9690E"));

            sessionTotalStartDateTextView.setTextColor(Color.parseColor("#F9690E"));

            sessionTotalTextView.setText("Timer Active");


        } else {
            sessionTotalViewContainer.setBackgroundColor(Color.parseColor("#f2f8f8"));
            sessionTotalTextView.setTextColor(Color.parseColor("#2C3E50"));
            sessionTotalTitleTextView.setTextColor(Color.parseColor("#2C3E50"));
            sessionTotalStartDateTextView.setTextColor(Color.parseColor("#2C3E50"));
        }


    }

    //TODO: implement onResume so that when background pressed while alert is up, it won't crash


    public SessionAdapter getSessionAdapter() {
        return sessionAdapter;
    }

    public void setSessionAdapter(SessionAdapter sessionAdapter) {
        this.sessionAdapter = sessionAdapter;
    }
}
