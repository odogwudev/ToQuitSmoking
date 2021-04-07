package com.odogwudev.stopsmoking;

import android.Manifest;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.odogwudev.stopsmoking.fragments.FragmentGoal;
import com.odogwudev.stopsmoking.fragments.FragmentHealth;
import com.odogwudev.stopsmoking.fragments.FragmentNotes;
import com.odogwudev.stopsmoking.fragments.FragmentOverview;
import com.odogwudev.stopsmoking.helper.helper_main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static InterstitialAd mInterstitialAd;

    final private int REQUEST_CODE_ASK_PERMISSIONS=123;
    ConsentForm form;
    private SharedPreferences SP;

    public static void showInterstitial() {
        if (mInterstitialAd.isLoaded ()) {
            mInterstitialAd.show ();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        final Context context = this;
        RateUs.init (MainActivity.this)

                .setLaunchesDelay (7) // App Launched more than 7 times
                .setDaysDelay (0) // App is launched more than 0 days later than installation.

                .setOnCloseClickListener (new RateUs.OnCloseClick () {



                    @Override
                    public void onCloseClickListener() {
                        RateUs.resetDelay(context);

                    }

                })


                .setOnFeedbackClickListener (new RateUs.OnFeedbackClick () {

                    @Override
                    public void onFeedBackClickListener() {
                        String deviceInfo="Device Info:";
                        deviceInfo+="\n OS Version: " + System.getProperty ("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
                        deviceInfo+="\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
                        deviceInfo+="\n Device: " + android.os.Build.DEVICE;
                        deviceInfo+="\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";
                        // TODO: Replace with your own Mail.
                        Intent emailIntent=new Intent (Intent.ACTION_SENDTO, Uri.fromParts ("mailto", "mail@example.com", null));
                        emailIntent.putExtra (Intent.EXTRA_SUBJECT, "Device Info");
                        emailIntent.putExtra (Intent.EXTRA_TEXT, deviceInfo);
                        startActivity (Intent.createChooser (emailIntent, "Send email..."));

                    }

                })
                .build ();



//end rate us
        //GDPR
        ConsentInformation consentInformation=ConsentInformation.getInstance (this);
        // TODO: Replace with your publisher Id.
        String[] publisherIds={"pub-XXXXXXXXXXXXXXXX"};
        consentInformation.requestConsentInfoUpdate (publisherIds, new ConsentInfoUpdateListener () {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
            }
        });
        URL privacyUrl=null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl=new URL ("https://www.example.com/privacy-policy/");
        } catch (MalformedURLException e) {
            e.printStackTrace ();
            // Handle error.
        }
        form=new ConsentForm.Builder (this, privacyUrl)
                .withListener (new ConsentFormListener () {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        if (!MainActivity.this.isFinishing ()) form.show ();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                    }
                })
                .withPersonalizedAdsOption ()
                .withNonPersonalizedAdsOption ()
                .withAdFreeOption ()
                .build ();
        form.load ();
        //en GDPR


        MobileAds.initialize (this, getString (R.string.app_id));
//display native Ad

        AdLoader adLoader=new AdLoader.Builder (this, getString (R.string.Native))
                .forUnifiedNativeAd (unifiedNativeAd -> {
                    NativeTemplateStyle styles=new
                            NativeTemplateStyle.Builder ().build ();
                    TemplateView template=findViewById (R.id.my_template);
                    if (template != null) {

                        template.setVisibility (View.VISIBLE);
                        template.setStyles (styles);
                        template.setNativeAd (unifiedNativeAd);


                    }


                })


                .build ();
        adLoader.loadAd (new AdRequest.Builder ()
                // TODO: Replace with your own Device ID.
                .addTestDevice ("33BE2250B43518CCDA7DE426D04EE231")
                .build ());
        //end display native Ad
        showme ();


        PreferenceManager.setDefaultValues (MainActivity.this, R.xml.user_settings, false);
        SP=PreferenceManager.getDefaultSharedPreferences (this);

        Toolbar toolbar=findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        ViewPager viewPager=findViewById (R.id.viewpager);

        setupViewPager (viewPager);


        TabLayout tabLayout=findViewById (R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager (viewPager);

        if (SP.getBoolean ("first_run", true)) {
            SP.edit ().putBoolean ("first_run", false).apply ();
            Intent intent_in=new Intent (MainActivity.this, UserSettingsActivity.class);
            startActivity (intent_in);
            overridePendingTransition (0, 0);
            finish ();
        }

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int hasWRITE_EXTERNAL_STORAGE=checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale (Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder (MainActivity.this)
                            .setTitle (R.string.app_permissions_title)
                            .setMessage (helper_main.textSpannable (getString (R.string.app_permissions)))
                            .setPositiveButton (getString (R.string.yes), new DialogInterface.OnClickListener () {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions (new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            })
                            .setNegativeButton (getString (R.string.no), null)
                            .show ();

                    return;
                }
                requestPermissions (new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

        File directory=new File (Environment.getExternalStorageDirectory () + "/Android/data/toquitsmoking.backup");
        if (!directory.exists ()) {
            directory.mkdirs ();
        }

        File directory_data=new File (Environment.getExternalStorageDirectory () + "/Android/data/com.toquitsmoking.quitnow");
        if (!directory_data.exists ()) {
            directory_data.mkdirs ();
        }
//display banner Ad in viewPager
        AdRequest adRequest=new AdRequest.Builder ()
                // TODO: Replace with your own Device ID.
                .addTestDevice ("33BE2250B43518CCDA7DE426D04EE231")
                .build ();

        AdView adView=findViewById (R.id.adView);
        adView.loadAd (adRequest);
//end banner Ad in viewPager


    }

    //
    private void showme() {
        // display InterstitialAd between viewPager

        mInterstitialAd=new InterstitialAd (this);
        mInterstitialAd.setAdUnitId (getString (R.string.Interstitial));
        AdRequest adRequest=new AdRequest.Builder ()
                // TODO: Replace with your own Device ID.
                .addTestDevice ("33BE2250B43518CCDA7DE426D04EE231")
                .build ();
        mInterstitialAd.loadAd (adRequest);

        mInterstitialAd.setAdListener (new AdListener () {

            @Override
            public void onAdClosed() {
                //reload interstitial
                AdRequest adRequest=new AdRequest.Builder ()
                        // TODO: Replace with your own Device ID.
                        .addTestDevice ("33BE2250B43518CCDA7DE426D04EE231")
                        .build ();
                mInterstitialAd.loadAd (adRequest);
            }
        });
    }
    // End display InterstitialAd between viewPager

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter=new ViewPagerAdapter (getSupportFragmentManager (), 0);

        String tab_diary;
        if (SP.getString ("sortDB", "title").equals ("title")) {
            tab_diary=getString (R.string.action_diary) + " | " + getString (R.string.sort_title);
        } else if (SP.getString ("sortDB", "title").equals ("icon")) {
            tab_diary=getString (R.string.action_diary) + " | " + getString (R.string.sort_pri);
        } else {
            tab_diary=getString (R.string.action_diary) + " | " + getString (R.string.sort_date);
        }

        if (SP.getBoolean ("tab_overview", false)) {
            adapter.addFragment (new FragmentOverview (), String.valueOf (getString (R.string.action_overview)));
        }
        if (SP.getBoolean ("tab_health", false)) {
            adapter.addFragment (new FragmentHealth (), String.valueOf (getString (R.string.action_health)));
        }
        if (SP.getBoolean ("tab_goal", false)) {
            adapter.addFragment (new FragmentGoal (), String.valueOf (getString (R.string.action_goal)));
        }
        if (SP.getBoolean ("tab_diary", false)) {
            adapter.addFragment (new FragmentNotes (), tab_diary);
        }

        viewPager.setAdapter (adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id=item.getItemId ();

        if (id == R.id.action_settings) {

            Intent intent_in=new Intent (MainActivity.this, UserSettingsActivity.class);
            startActivity (intent_in);
            overridePendingTransition (0, 0);
            finish ();
            return true;
        }

        return super.onOptionsItemSelected (item);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList=new ArrayList<> ();
        private final List<String> mFragmentTitleList=new ArrayList<> ();

        private ViewPagerAdapter(@NonNull FragmentManager manager, int behavior) {
            super (manager, behavior);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get (position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size ();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add (fragment);
            mFragmentTitleList.add (title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get (position);// add return null; to display only icons
        }
    }
}