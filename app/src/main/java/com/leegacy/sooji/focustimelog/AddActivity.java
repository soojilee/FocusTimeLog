package com.leegacy.sooji.focustimelog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leegacy.sooji.realm_data.CategoryRealmObject;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by soo-ji on 2016-02-29.
 */
public class AddActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AddActivity";
    private EditText titleEditText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // TODO : Uncomment the following.
//        LData.addModel(titleEditText.getText().toString());

        CategoryRealmObject category = new CategoryRealmObject();
        category.setName(titleEditText.getText().toString());

        Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();
            realm.copyToRealm(category);
            realm.commitTransaction();

            finish();
        }catch (RealmPrimaryKeyConstraintException e){
            Log.e(TAG, "Realm primary key already exists.");
            Toast.makeText(getBaseContext(), "Realm Primary Key "+ category.getName() +" Already Exists. Try another name", Toast.LENGTH_SHORT).show();
            realm.cancelTransaction();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
