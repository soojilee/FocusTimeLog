package com.leegacy.sooji.focustimelog;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.leegacy.sooji.fragment.CategoryFragment;
import com.leegacy.sooji.listeners.OnCategoryObjectListener;
import com.leegacy.sooji.realm_data.CategoryRealmObject;
import com.leegacy.sooji.realm_data.TotalRealmObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements OnCategoryObjectListener, ViewPager.OnPageChangeListener{

    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private FragmentStatePagerAdapter vpAdap;
    private RealmResults<CategoryRealmObject> result;
    private CustomFragAdapter customFragAdapter;
    private int returnTo=0;
    private TotalRealmObject totalRealmObject;
//    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // The realm file will be located in Context.getFilesDir() with name "default.realm"
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();
        totalRealmObject = realm.where(TotalRealmObject.class).findFirst();
        if(totalRealmObject!=null) {
            returnTo = totalRealmObject.getAtPage();
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);


        //viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
        Log.e(TAG, "onCreate of MainActivity is called");
        updateViewPager();
        viewPager.setCurrentItem(returnTo);
        if(result.size() == 0){
            startActivity(new Intent(this, AddActivity.class));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("returnTo", returnTo);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        returnTo = savedInstanceState.getInt("returnTo");
    }

    private void updateViewPager() {
        Realm realm = Realm.getDefaultInstance();
        result = realm.where(CategoryRealmObject.class).findAll();
        for(int i=0;i<result.size();i++){
            Log.e(TAG, "Categories found: "+result.get(i).getName());
        }

        customFragAdapter = new CustomFragAdapter(getSupportFragmentManager());
        viewPager.setAdapter(customFragAdapter);
        viewPager.setCurrentItem(returnTo, true);
//                new FragmentStatePagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
////                if(position == 1){
////                    startActivity(new Intent(getBaseContext(), AddActivity.class));
////                }
//                CategoryFragment gf = new CategoryFragment();
//                //gf.setOnMainActivityListener(MainActivity);
//                gf.setModel(result.get(position));
//                gf.setOnCategoryObjectListener(MainActivity.this);
//
//
//                return gf;
//            }
//
//            @Override
//            public int getCount() {
//                return result.size();
//            }
//        });
        Log.e(TAG, "Size is " + result.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.e(TAG, "onDestroy"+ returnTo);
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        if(totalRealmObject!=null) {
//            totalRealmObject.setAtPage(returnTo);
//        }else{
//            TotalRealmObject t = new TotalRealmObject();
//            t.setAtPage(returnTo);
//            realm.copyToRealm(t);
//        }
//        realm.commitTransaction();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e(TAG, "page scrolled to: "+ position);
        returnTo = position;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if(totalRealmObject!=null) {
            totalRealmObject.setAtPage(returnTo);
        }else{
            TotalRealmObject t = new TotalRealmObject();
            t.setAtPage(returnTo);
            realm.copyToRealm(t);
        }
        realm.commitTransaction();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class CustomFragAdapter extends FragmentStatePagerAdapter{

        public CustomFragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
          Log.e(TAG, "position: "+position);
//                if(position == 1){
//                    startActivity(new Intent(getBaseContext(), AddActivity.class));
//                }
            CategoryFragment gf = new CategoryFragment();
            //gf.setOnMainActivityListener(MainActivity);
//            Realm realm = Realm.getDefaultInstance();
//            for(int i=0;i<result.get(position).getGroups().size();i++){
//                realm.beginTransaction();
//                result.get(position).getGroups().get(i).setIndex(i + 1);
//                realm.commitTransaction();
//                Log.e(TAG, "updated cro has groups: "+result.get(position).getGroups().get(i).getIndex());
//            }
            gf.setModel(result.get(position));
//            Log.e(TAG, "categoryRealmObject is updated");


            gf.setOnCategoryObjectListener(MainActivity.this);


            return gf;
        }

        @Override
        public int getCount() {
            return result.size();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Todo move to onActivityResult.
        updateViewPager();
        //viewPager.setCurrentItem(returnTo);
    }

    @Override
    public void onCategoryDeleteRequested() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        result.get(viewPager.getCurrentItem()).removeFromRealm();

        realm.commitTransaction();


        updateViewPager();
        if(result.size() == 0){
            startActivity(new Intent(this, AddActivity.class));
        }
    }

    @Override
    public void onCategoryAddRequested() {
        Realm realm = Realm.getDefaultInstance();
        result = realm.where(CategoryRealmObject.class).findAll();
        returnTo = result.size()-1;
        updateViewPager();
    }


}