package com.odogwudev.stopsmoking.about;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.odogwudev.stopsmoking.MainActivity;
import com.odogwudev.stopsmoking.R;


public class About_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_about);


        Toolbar toolbar=findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);
        getSupportFragmentManager ().beginTransaction ()
                .replace (R.id.container, About_fragment.newInstance (new About_fragment ()))
                .commit ();

        setTitle (R.string.about_title);

        androidx.appcompat.app.ActionBar actionBar=getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled (true);
        }

        //display banner Ad in viewPager
        AdRequest adRequest=new AdRequest.Builder ()
                // TODO: Replace with your own Device ID.
                .addTestDevice ("33BE2250B43518CCDA7DE426D04EE231")
                .build ();
        AdView adView=findViewById (R.id.adView);
        adView.loadAd (adRequest);
//end banner Ad in viewPager

        MainActivity mainActivity=new MainActivity ();
        MainActivity.showInterstitial ();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId ();

        if (id == android.R.id.home) {
            finish ();
        }
        return super.onOptionsItemSelected (item);
    }

}
