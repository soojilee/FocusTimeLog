package com.leegacy.sooji.focustimelog;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by soo-ji on 16-03-20.
 */
public class PineApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/HelveticaNeue-Thin.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
