import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

@Dao
public interface TwitterDao {
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