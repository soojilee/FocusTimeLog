package com.leegacy.sooji.realm_data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by soo-ji on 2016-03-01.
 */
// Define you model class by extending the RealmObject
public class CategoryRealmObject extends RealmObject {
    public static final String CATEGORY_OBJECT_PRIMARY_KEY = "CATEGORY_OBJECT_PRIMARY_KEY";
    public static final String IS_NEW_SESSION = "IS_NEW_SESSION";

    //    @Required // Name cannot be null
    @PrimaryKey
    private String name;
    private RealmList<GroupRealmObject> groups;
    private int numGroups = 0;


    public CategoryRealmObject() {
//        this.name = ""+Math.random(); // TODO: Make it so that name will not be the same as an exiting datapoint.
        groups = new RealmList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RealmList<GroupRealmObject> getGroups() {
        return groups;
    }

    public void setGroups(RealmList<GroupRealmObject> groups) {
        this.groups = groups;
    }

    public int getNumGroups() {
        return numGroups;
    }

    public void setNumGroups(int numGroups) {
        this.numGroups = numGroups;
    }




    // ... Generated getters and setters ...
}
