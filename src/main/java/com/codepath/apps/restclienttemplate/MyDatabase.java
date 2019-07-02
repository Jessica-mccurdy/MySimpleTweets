package com.codepath.apps.restclienttemplate;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.codepath.apps.restclienttemplate.models.SampleModelDao;
import com.codepath.apps.restclienttemplate.models.Tweet;

// bump version number if your schema changes
@Database(entities={Tweet.class}, version=1)
public abstract class MyDatabase extends RoomDatabase {
    // Declare your data access objects as abstract
    public abstract TwitterDao twitterDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}