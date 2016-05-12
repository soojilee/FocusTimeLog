package com.leegacy.sooji.focustimelog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by soo-ji on 16-03-20.
 */
public class BaseActivity extends FragmentActivity{
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
