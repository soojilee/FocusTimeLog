package com.leegacy.sooji.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leegacy.sooji.extras.RowModelFactory;
import com.leegacy.sooji.extras.StopWatchFactory;
import com.leegacy.sooji.focustimelog.AddActivity;
import com.leegacy.sooji.focustimelog.R;
import com.leegacy.sooji.focustimelog.SessionActivity;
import com.leegacy.sooji.focustimelog.SessionAdapter;
import com.leegacy.sooji.listeners.OnCategoryFragmentListener;
import com.leegacy.sooji.listeners.OnCategoryObjectListener;
import com.leegacy.sooji.model.RowModel;
import com.leegacy.sooji.model.SessionTotalRowModel;
import com.leegacy.sooji.realm_data.CategoryRealmObject;

import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by soo-ji on 2016-02-29.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, OnCategoryFragmentListener {

    private View root;
    private TextView titleTextView;
    private ImageView startButton;

    public CategoryRealmObject categoryRealmObject;
    private TextView deleteButton;
    private OnCategoryObjectListener onCategoryObjectListener;
    private RecyclerView recyclerView;
    private SessionAdapter sessionAdapter;
    private TextView totalTimeTextView;
    private String TAG = "CF";
    private RelativeLayout titleContainer;
    private TextView titleEditText;
    private Boolean hasActiveGroup;
    private int cat_total;
    private List<RowModel> rowModels;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.group_fragment, null);
        Log.e(TAG, "oncreateview is called");
        init();

        updateRecyclerview();
//        if (root != null && categoryRealmObject != null) {
//            for (GroupRealmObject gro : categoryRealmObject.getGroups()) {
//                if (gro.getIsPaused()) {
//                    setHasActiveGroup(true);
//                    int size = gro.getSessions().size();
//                    activeSeconds += gro.getSessions().get(size - 1).getActiveSeconds();
//
//
//                }
//
//            }
//            startTimer();
//        }
        return root;
    }

//    private void startTimer() {
//        timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (GroupRealmObject gro : categoryRealmObject.getGroups()) {
//                                if (gro.getIsPaused()) {
//
//                                }
//                            }
//                            totalTimeTextView.setText("Total " + StopWatchFactory.convertSecondsToTime(cat_total + activeSeconds));
//                            totalTimeTextView.invalidate();
//                            activeSeconds++;
//                        }
//                    });
//                }
//            }
//        },0, 1000);
//    }

//    private updateActiveSession(){
//
//    }


    private void init() {


        titleTextView = (TextView) root.findViewById(R.id.titleEditText);

        startButton = (ImageView) root.findViewById(R.id.addSessionButton);
        startButton.setOnClickListener(this);

        deleteButton = (TextView) root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);


        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        totalTimeTextView = (TextView) root.findViewById(R.id.totalTimeCategory);

        titleContainer = (RelativeLayout) root.findViewById(R.id.titleContainer);
        titleContainer.setOnLongClickListener(this);

        titleEditText = (TextView) root.findViewById(R.id.titleEditText);
        titleEditText.setOnClickListener(this);


        sessionAdapter = new SessionAdapter();
        recyclerView.setAdapter(sessionAdapter);

        registerForContextMenu(deleteButton);

    }

    private void updateRecyclerview() {
        if (root != null && categoryRealmObject != null) {
            titleTextView.setText(categoryRealmObject.getName());

            rowModels = RowModelFactory.categoryFragmentToRowModel(categoryRealmObject);

            //total time for category total time
            cat_total = 0;
            for (RowModel strm : rowModels) {
                //Log.e(TAG, "session Total model index after deleting: "+((SessionTotalRowModel)strm).getSessionIndex());
                cat_total += ((SessionTotalRowModel) strm).getTotalTime();
            }
            //if(categoryRealmObject.getGroups().size() > 0) {
//                LabelRowModel cat_total_label = new LabelRowModel();
//                cat_total_label.setLabel("total king: " + cat_total + "seconds");
//                rowModels.add(cat_total_label);
            totalTimeTextView.setText("Total " + StopWatchFactory.convertSecondsToTime(cat_total));

            //}

            sessionAdapter.setModels(rowModels);
            sessionAdapter.setCategoryFragment(this);
            sessionAdapter.setOnCategoryFragmentListener(this);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (item.getItemId()) {
            case R.id.deletePage:
                builder.setTitle(R.string.delete_page)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                if (onCategoryObjectListener != null) {
                                    onCategoryObjectListener.onCategoryDeleteRequested();
                                }
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
            case R.id.addNewPage:
                final EditText editTitle = new EditText(getActivity());
                builder.setTitle(R.string.add_page)
                        .setView(editTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String newPageName = editTitle.getText().toString();
                                CategoryRealmObject category = new CategoryRealmObject();
                                category.setName(newPageName);

                                Realm realm = Realm.getDefaultInstance();

                                try {
                                    realm.beginTransaction();
                                    realm.copyToRealm(category);
                                    realm.commitTransaction();


                                } catch (RealmPrimaryKeyConstraintException e) {
                                    Log.e(TAG, "Realm primary key already exists.");
                                    Toast.makeText(getActivity(), "Realm Primary Key " + category.getName() + " Already Exists. Try another name", Toast.LENGTH_SHORT).show();
                                    realm.cancelTransaction();
                                }
                                onCategoryObjectListener.onCategoryAddRequested();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog addDialog = builder.create();
                addDialog.show();
//                startActivity(new Intent(getActivity(), AddActivity.class));

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addSessionButton:
                Intent intent = new Intent(getActivity(), SessionActivity.class);

                intent.putExtra(CategoryRealmObject.CATEGORY_OBJECT_PRIMARY_KEY, categoryRealmObject.getName());
                intent.putExtra(CategoryRealmObject.IS_NEW_SESSION, true);
                startActivity(intent);

                break;
            //give two options: delete and add page
            case R.id.deleteButton:
                getActivity().openContextMenu(v);
//                Realm realm = Realm.getDefaultInstance();
                //TODO: I don't know why it's crashing here. Need to fix it
//                if(realm.where(CategoryRealmObject.class).findAll().size() == 0){
//                    startActivity(new Intent(getActivity(), AddActivity.class));
//                    Log.e(TAG, "hey");
//                }
                break;

            case R.id.titleEditText:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final EditText editTitle = new EditText(getActivity());
                builder.setTitle(R.string.change_title)
                        .setView(editTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                String newCatName = editTitle.getText().toString();
                                Realm realm = Realm.getDefaultInstance();

                                try {
                                    realm.beginTransaction();
                                    categoryRealmObject.setName(newCatName);
                                    realm.commitTransaction();

                                }catch (RealmPrimaryKeyConstraintException e){
                                    Log.e(TAG, "Realm primary key already exists.");
                                    Toast.makeText(getActivity(), "Realm Primary Key " + newCatName + " Already Exists. Try another name", Toast.LENGTH_SHORT).show();
                                    realm.cancelTransaction();
                                }

                                updateRecyclerview();

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
        }
    }

    public void setModel(CategoryRealmObject categoryRealmObject) {

        this.categoryRealmObject = categoryRealmObject;
        updateRecyclerview();
    }

    public void setOnCategoryObjectListener(OnCategoryObjectListener onCategoryObjectListener) {
        this.onCategoryObjectListener = onCategoryObjectListener;
    }

    public OnCategoryObjectListener getOnCategoryObjectListener() {
        return onCategoryObjectListener;
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.titleContainer:
                startActivity(new Intent(getActivity(), AddActivity.class));
                break;
            //TODO: delete all pages
//            case R.id.deleteButton:
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage(R.string.delete_all_dialog_message)
//                        .setTitle(R.string.delete_all_dialog_title);
//                AlertDialog dialog = builder.create();
//                dialog.show();
////                if (onCategoryObjectListener != null) {
////                    onCategoryObjectListener.onCategoryDeleteRequested(categoryRealmObject);
////                }
////                Realm realm = Realm.getDefaultInstance();
////                //TODO: I don't know why it's crashing here. Need to fix it
////                if(realm.where(CategoryRealmObject.class).findAll().size() == 0){
////                    startActivity(new Intent(getActivity(), AddActivity.class));
////                    Log.e(TAG, "hey");
////                }
//                break;
        }
        return false;
    }

    @Override
    public void onUpdateRequested() {
        //change group index in category realm object for all groups after the group deleted
//        Realm realm = Realm.getDefaultInstance();
////        for(GroupRealmObject gro: categoryRealmObject.getGroups()){
////            if(gro.getIndex() > deletedIndex){
////                realm.beginTransaction();
////                gro.setIndex(gro.getIndex()-1);
////                realm.commitTransaction();
////            }
////        }
//        for(GroupRealmObject gro: categoryRealmObject.getGroups()){
//            if(gro.getIndex()>sessionIndex){
//                realm.beginTransaction();
//                gro.setIndex(gro.getIndex()-1);
//                realm.commitTransaction();
//            }
//        }
        updateRecyclerview();
    }

    public Boolean getHasActiveGroup() {
        return hasActiveGroup;
    }

    public void setHasActiveGroup(Boolean hasActiveGroup) {
        this.hasActiveGroup = hasActiveGroup;
    }


}
