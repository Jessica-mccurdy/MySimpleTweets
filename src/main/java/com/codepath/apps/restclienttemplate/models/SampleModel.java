package com.codepath.apps.restclienttemplate.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the Room guide for more details:
 * https://github.com/codepath/android_guides/wiki/Room-Guide
 *
 */
@Entity
public class SampleModel {

	@PrimaryKey(autoGenerate = true)
	Long id;

	// Define table fields
	@ColumnInfo
	private String name;

	public SampleModel() {
		super();
	}

	// Parse model from JSON
	public SampleModel(JSONObject object){
		super();

		try {
			this.name = object.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Getters
	public String getName() {
		return name;
	}

	// Setters
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Dao
	public static interface TwitterDao {
		// Record finders
		@Query("SELECT * FROM Tweet WHERE post_id = :tweetId")
		Tweet byTweetId(Long tweetId);

		@Query("SELECT * FROM Tweet ORDER BY created_at")
		List<Tweet> getRecentTweets();

		// Replace strategy is needed to ensure an update on the table row.  Otherwise the insertion will
		// fail.
		@Insert(onConflict = OnConflictStrategy.REPLACE)
		void insertTweet(Tweet... tweets);
	}
}
