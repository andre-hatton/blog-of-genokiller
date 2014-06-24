package com.genokiller.blogofgenokiller.controllers;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.genokiller.blogofgenokiller.fragments.CommentAcceptedFragment;
import com.genokiller.blogofgenokiller.fragments.CommentVerifiedFragment;

/**
 * Created by yoshizuka on 24/06/14.
 */
public class CommentAdmin_Controller extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comment_fragment);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("comment_verified").setIndicator("Commentaires à vérifier", getResources().getDrawable(android.R.drawable.star_on)),
                CommentVerifiedFragment.class, null);

        mTabHost.addTab(
                mTabHost.newTabSpec("comment_accepted").setIndicator("Commentaires acceptés", getResources().getDrawable(android.R.drawable.star_on)),
                CommentAcceptedFragment.class, null);
    }

}
