package com.leegacy.sooji.focustimelog;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leegacy.sooji.fragment.CategoryFragment;
import com.leegacy.sooji.listeners.OnCategoryFragmentListener;
import com.leegacy.sooji.listeners.OnSessionActivityListener;
import com.leegacy.sooji.model.LabelRowModel;
import com.leegacy.sooji.model.RowModel;
import com.leegacy.sooji.model.SessionRowModel;
import com.leegacy.sooji.model.SessionTotalRowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soo-ji on 2016-03-01.
 */
public class SessionAdapter extends RecyclerView.Adapter  {
    private static final String TAG = "SessionAdapter";
    private List<RowModel> rowModels;
    private OnCategoryFragmentListener onCategoryFragmentListener;
    private CategoryFragment categoryFragment;
    private OnSessionActivityListener onSessionActivityListener;

    public SessionAdapter() {
        this.rowModels = new ArrayList<>();
    }

    public OnCategoryFragmentListener getOnCategoryFragmentListener() {
        return onCategoryFragmentListener;
    }

    public void setOnCategoryFragmentListener(OnCategoryFragmentListener onCategoryFragmentListener) {
        this.onCategoryFragmentListener = onCategoryFragmentListener;
    }

    public CategoryFragment getCategoryFragment() {
        return categoryFragment;
    }

    public void setCategoryFragment(CategoryFragment categoryFragment) {
        this.categoryFragment = categoryFragment;
    }

    public OnSessionActivityListener getOnSessionActivityListener() {
        return onSessionActivityListener;
    }

    public void setOnSessionActivityListener(OnSessionActivityListener onSessionActivityListener) {
        this.onSessionActivityListener = onSessionActivityListener;
    }

    enum ViewTypes{
        LABEL_ROW_MODEL,
        SESSION_ROW_MODEL,
        SESSION_TOTAL_MODEL
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // TODO We ignored viewType. It's a sin.
        if(viewType == ViewTypes.LABEL_ROW_MODEL.ordinal()) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_view, parent, false);
            return new LabelViewHolder(v);
        }else if(viewType == ViewTypes.SESSION_ROW_MODEL.ordinal()) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_view, parent, false);
            SessionViewHolder sessionViewHolder = new SessionViewHolder(v);
            sessionViewHolder.setSessionAdapter(this);
            return sessionViewHolder;

        }else if(viewType == ViewTypes.SESSION_TOTAL_MODEL.ordinal()) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_total_view, parent, false);
            SessionTotalViewHolder sessionTotalViewHolder = new SessionTotalViewHolder(v);
            sessionTotalViewHolder.setSessionAdapter(this);
            return sessionTotalViewHolder;
        }

        Log.e(TAG, "Critical Error Adapter could not find the viewholder to create for that model.");
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RowViewHolder) holder).update(rowModels.get(position));
    }

    @Override
    public int getItemCount() {
        return rowModels.size();
    }

    public void setModels(List<RowModel> rowModels){
        this.rowModels = rowModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        RowModel rm= rowModels.get(position);
        if(rm instanceof SessionRowModel){
            return ViewTypes.SESSION_ROW_MODEL.ordinal();
        }else if(rm instanceof LabelRowModel){
            return ViewTypes.LABEL_ROW_MODEL.ordinal();
        }else if(rm instanceof SessionTotalRowModel){
            return ViewTypes.SESSION_TOTAL_MODEL.ordinal();
        }

        Log.e(TAG, "Critical Error: Row item view type was not recognized.");
        return -1;
    }
}
