package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


@Parcel
public class User {

    //@ColumnInfo
    //String name;

    // attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;


    // deserialize JSON
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();

        // extract and fill values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url_https");
        return user;

    }




/*


        // normally this field would be annotated @PrimaryKey because this is an embedded object
    // it is not needed
    @ColumnInfo
    Long twitter_id;

    // Parse model from JSON
    public static User parseJSON(JSONObject tweetJson) {

        User user = new User();
        this.twitter_id = tweetJson.getLong("id");
        this.name = tweetJson.getString("name");
        return user;
    }



    */

}

