package com.odogwudev.stopsmoking.helper;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;
import com.odogwudev.stopsmoking.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_intro extends OnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        List<OnboarderPage> onboarderPages=new ArrayList<> ();

        // Create your first page
        OnboarderPage onboarderPage1=new OnboarderPage (getString (R.string.intro1_title), getString (R.string.intro1_text), R.drawable.smoke_intro);
        OnboarderPage onboarderPage2=new OnboarderPage (getString (R.string.intro2_title), getString (R.string.intro2_text), R.drawable.page1);
        OnboarderPage onboarderPage3=new OnboarderPage (getString (R.string.intro3_title), getString (R.string.intro3_text), R.drawable.page2);


        // You can define title and description colors (by default white)
        onboarderPage1.setTitleColor (R.color.text_all_color);
        onboarderPage1.setBackgroundColor (R.color.colorPurple_900);
        onboarderPage2.setTitleColor (R.color.text_all_color);
        onboarderPage2.setBackgroundColor (R.color.colorAmber_900);
        onboarderPage3.setTitleColor (R.color.text_all_color);
        onboarderPage3.setBackgroundColor (R.color.colorRed_900);

        // Add your pages to the list
        onboarderPages.add (onboarderPage1);
        onboarderPages.add (onboarderPage2);
        onboarderPages.add (onboarderPage3);

        // And pass your pages to 'setOnboardPagesReady' method
        setActiveIndicatorColor (android.R.color.white);
        setInactiveIndicatorColor (R.color.colorPink_A400);
        shouldUseFloatingActionButton (true);
        setOnboardPagesReady (onboarderPages);
    }

    @Override
    public void onSkipButtonPressed() {
        // Optional: by default it skips onboarder to the end
        super.onSkipButtonPressed ();
        // Define your actions when the user press 'Skip' button
        finish ();
    }

    @Override
    public void onFinishButtonPressed() {
        // Define your actions when the user press 'Finish' button
        PreferenceManager.setDefaultValues (this, R.xml.user_settings, false);
        SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences (this);
        sharedPref.edit ().putBoolean ("intro_notShow", false).apply ();
        finish ();
    }
}
