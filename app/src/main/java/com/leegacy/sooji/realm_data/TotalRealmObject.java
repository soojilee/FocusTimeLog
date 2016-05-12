package com.leegacy.sooji.realm_data;

import io.realm.RealmObject;

/**
 * Created by soo-ji on 16-03-30.
 */
public class TotalRealmObject extends RealmObject{
    private int atPage;

    public int getAtPage() {
        return atPage;
    }

    public void setAtPage(int atPage) {
        this.atPage = atPage;
    }
}
