package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {


        //pass in the Tweets array in the constructor
        private List<Tweet> mTweets;
        Context context;
        public TweetAdapter(List<Tweet> tweets) {
            mTweets = tweets;
        }
        // for each row inflate the layout and cache references into ViewHolder
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context); // TODO check this

            View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
            ViewHolder viewHolder = new ViewHolder(tweetView);
            return viewHolder;
        }
        // bind the values based on the position of the element
        @Override
        public void  onBindViewHolder(ViewHolder holder, int position) {
            // get data according to position.
            Tweet tweet = mTweets.get(position);

            //populate the views according to this data
            holder.tvUsername.setText("@" + tweet.user.screenName);
            holder.tvName.setText(tweet.user.name);
            holder.tvBody.setText(tweet.body);
            holder.tvTimeStamp.setText(getRelativeTimeAgo(tweet.createdAt));


            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .into(holder.ivProfileImage);
        }

        @Override
        public int getItemCount() {
            return mTweets.size();
        }


        //
        // create ViewHolder Class




        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView ivProfileImage;
            public TextView tvUsername;
            public TextView tvName;
            public TextView tvBody;
            public TextView tvTimeStamp;

            public ViewHolder(View itemView) {
                super(itemView);

                // perform findViewById lookups

                ivProfileImage = (ImageView) itemView.findViewById((R.id.ivProfileImage));
                tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvBody = (TextView) itemView.findViewById((R.id.tvBody));
                tvTimeStamp = (TextView) itemView.findViewById((R.id.tvTimeStamp));



            }
        }


    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
    }

