package com.leegacy.sooji.extras;

/**
 * Created by soo-ji on 2016-03-08.
 */

public class StopWatchFactory {
    private static final String TAG = "STOP_WATCH";
    //private long time_counter = 45;  // Seconds to set the Countdown from






//    public static String convertSecondsToTime(int seconds){
//        int hour = 0;
//        int minute = 0;
//        int second = 0;
//        if(seconds < 60){
//            if(seconds < 10){
//                return ("00:00:0"+seconds);
//            }
//            return ("00:00:"+seconds);
//        }
//        if(seconds/60 > 60){
//            hour = (seconds/60)/60;
//            minute = (int) ((((double)seconds)/60)%60);
//            second = seconds%60;
//            Log.e(TAG, "hour: "+hour+" min: "+minute+"second: "+second);
//
//            return formatTimeString(hour)+":"+formatTimeString(minute)+":"+formatTimeString(second);
//        }
//        Log.e(TAG, "seconds: "+seconds);
//        return "ERROR convertSecondsToTime is not working";
//    }

    public static String convertSecondsToTime(int seconds) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        hour = (seconds / 60) / 60;
        minute = (int) ((((double) seconds) / 60) % 60);
        second = seconds % 60;
        //Log.e(TAG, "hour: " + hour + " min: " + minute + "second: " + second);

        return formatTimeString(hour) + ":" + formatTimeString(minute) + ":" + formatTimeString(second);

        //return "ERROR convertSecondsToTime is not working";
    }

    private static String formatTimeString(int n){
        if(n<10){
            return "0"+n;
        }
        return ""+n;
    }

}
