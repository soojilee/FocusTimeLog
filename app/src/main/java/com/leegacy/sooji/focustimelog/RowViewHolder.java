package com.leegacy.sooji.focustimelog;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.leegacy.sooji.model.RowModel;

/**
 * Created by soo-ji on 2016-03-03.
 */
public abstract class RowViewHolder extends RecyclerView.ViewHolder{
    public RowViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void update(RowModel rowModel);

}
