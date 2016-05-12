package com.leegacy.sooji.focustimelog;

import android.view.View;
import android.widget.TextView;

import com.leegacy.sooji.model.LabelRowModel;
import com.leegacy.sooji.model.RowModel;

/**
 * Created by soo-ji on 2016-03-03.
 */
public class LabelViewHolder extends RowViewHolder {
    private final TextView labelTextView;
    private LabelRowModel model;

    public LabelViewHolder(View v) {
        super(v);

        labelTextView = (TextView) itemView.findViewById(R.id.labelTextView);

    }

    @Override
    public void update(RowModel rowModel) {
        model = (LabelRowModel)rowModel;

        labelTextView.setText(model.getLabel());
    }


}
