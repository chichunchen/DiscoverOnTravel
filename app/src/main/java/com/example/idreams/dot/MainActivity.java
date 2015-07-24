package com.example.idreams.dot;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idreams.dot.chat.ChatListActivity;
import com.example.idreams.dot.localtopics.BoardActivity;
import com.example.idreams.dot.nearby.NearbyActivity;
import com.example.idreams.dot.utils.GetToken;
import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends BaseActivity
        implements MainFragment.MainFragmentCallbacks {

    public static final int INDEX_SIMPLE_LOGIN = 0;
    public static final String FRAGMENT_TAG = "fragment_tag";
    private static final int STATE_TOUR_WELCOME = 1;
    private static final int STATE_TOUR_1 = 2;
    private static final int STATE_TOUR_2 = 3;
    private static final int STATE_TOUR_3 = 4;
    private static final int STATE_TOUR_COMPLETE = 5;
    private static final int STATE_TOUR_DISABLE = 6;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static String sSerToken = "api_doc_token";
    public static int sState;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (true) {
            sState = STATE_TOUR_WELCOME;
        }
        setContentView(R.layout.activity_main);
        (new GetToken(this)).getToken();
    }

    @Override
    public void onFragmentViewCreated(View view) {
        ImageView backgroundPicture = (ImageView) findViewById(R.id.back_image);
        View rootView = view.getRootView();
        Button      nearbyPlace   = (Button) rootView.findViewById(R.id.button1);
        Button      whatsHot      = (Button) rootView.findViewById(R.id.button2);
        Button      chatWithLocal = (Button) rootView.findViewById(R.id.button3);
        TextView    helpMessage   = (TextView) rootView.findViewById(R.id.textView);
        LoginButton loginButton   = (LoginButton) rootView.findViewById(R.id.login_button);
        Animation darkenAnim = AnimationUtils.loadAnimation(this,R.anim.back_alpha_lower);
        Animation lightenAnim = AnimationUtils.loadAnimation(this,R.anim.back_alpha_higher);
        Animation fadeinAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Animation fadeoutAnim = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        darkenAnim.setFillAfter(true);
        lightenAnim.setFillAfter(true);
        fadeoutAnim.setFillAfter(true);
        switch (sState) {
            case STATE_TOUR_WELCOME:
                ImageView lgog = (ImageView) rootView.findViewById(R.id.dot_logo);
                lgog.startAnimation(fadeoutAnim);
                backgroundPicture.startAnimation(darkenAnim);
                mFragmentManager = getSupportFragmentManager();
                toggleFragment(INDEX_SIMPLE_LOGIN);
                //set imageview alpha lower
                sState = STATE_TOUR_1;
                break;
            case STATE_TOUR_1:
                helpMessage.startAnimation(fadeinAnim);
                helpMessage.setText(getString(R.string.welcome_tour_1));
                loginButton.startAnimation(darkenAnim);
                whatsHot.startAnimation(darkenAnim);
                chatWithLocal.startAnimation(darkenAnim);
                sState = STATE_TOUR_2;
                break;
            case STATE_TOUR_2:
                helpMessage.setText(getString(R.string.welcome_tour_2));
                nearbyPlace.startAnimation(darkenAnim);
                whatsHot.startAnimation(lightenAnim);
                sState = STATE_TOUR_3;
                break;
            case STATE_TOUR_3:
                helpMessage.setText(getString(R.string.welcome_tour_3));
                whatsHot.startAnimation(darkenAnim);
                chatWithLocal.startAnimation(lightenAnim);
                sState = STATE_TOUR_COMPLETE;
                break;
            case STATE_TOUR_COMPLETE:
                helpMessage.startAnimation(fadeoutAnim);
                nearbyPlace.startAnimation(lightenAnim);
                whatsHot.startAnimation(lightenAnim);
                backgroundPicture.startAnimation(lightenAnim);
                loginButton.startAnimation(lightenAnim);
                sState = STATE_TOUR_DISABLE;
                break;
        }
    }

    @Override
    public void onFacebookLogin() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Profile profile = Profile.getCurrentProfile();
        String userName = getUserFromFb(profile);
        String id       = getIdFromFb  (profile);
        if (userName == null) {
            userName = prefs.getString("username", "traveller");
        }
        if (id == null) {
            id = prefs.getString("userid", null);
        }
        prefs.edit().putString("username", userName).commit();
        prefs.edit().putString("userid", id).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void nearbyBtn(View view) {
        Intent intent = new Intent(this, NearbyActivity.class);
        startActivity(intent);
    }

    public void localTopicBtn(View view) {
        Intent intent = new Intent(this, BoardActivity.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    public void chatBtn(View view) {
        Intent intent = new Intent(this, ChatListActivity.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    private void toggleFragment(int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (index) {
            case INDEX_SIMPLE_LOGIN:
                transaction.replace(android.R.id.content, new MainFragment(), FRAGMENT_TAG);
                break;
        }
        transaction.commit();
    }

    private String getUserFromFb(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append(profile.getName());
        }
        return stringBuffer.toString();
    }

    private String getIdFromFb(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append(profile.getId());
        }
        return stringBuffer.toString();
    }
}
