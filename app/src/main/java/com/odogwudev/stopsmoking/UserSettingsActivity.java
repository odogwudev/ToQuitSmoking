package com.odogwudev.stopsmoking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.odogwudev.stopsmoking.about.About_activity;
import com.odogwudev.stopsmoking.helper.Activity_intro;
import com.odogwudev.stopsmoking.helper.helper_main;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Objects;


public class UserSettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        androidx.appcompat.app.ActionBar actionBar=getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled (true);
            setTitle (R.string.action_settings);
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled (true);


        PreferenceManager.setDefaultValues (UserSettingsActivity.this, R.xml.user_settings, false);
        SharedPreferences SP=PreferenceManager.getDefaultSharedPreferences (this);
        boolean show=SP.getBoolean ("intro_notShow", true);

        if (show) {
            Intent mainIntent=new Intent (UserSettingsActivity.this, Activity_intro.class);
            startActivity (mainIntent);
            overridePendingTransition (0, 0);
        }


        // Display the fragment as the fragment_main content
        getSupportFragmentManager ().beginTransaction ()
                .replace (android.R.id.content, new SettingsFragment ())
                .commit ();
//display InterstitialAd
        MainActivity mainActivity=new MainActivity ();
        MainActivity.showInterstitial ();
        //End display InterstitialAd
    }

    public void onBackPressed() {
        Intent intent_in=new Intent (UserSettingsActivity.this, MainActivity.class);
        startActivity (intent_in);
        overridePendingTransition (0, 0);
        finish ();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id=item.getItemId ();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Intent intent_in=new Intent (UserSettingsActivity.this, MainActivity.class);
            startActivity (intent_in);
            overridePendingTransition (0, 0);
            finish ();
        }

        return super.onOptionsItemSelected (item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


        private void addLicenseListener() {

            Preference reset=findPreference ("license");
            Objects.requireNonNull (reset).setOnPreferenceClickListener (pref -> {

                Intent intent_in=new Intent (getActivity (), About_activity.class);
                startActivity (intent_in);
                Objects.requireNonNull (getActivity ()).overridePendingTransition (0, 0);

                return true;
            });
        }

        private void addClearCacheListener() {

            final Activity activity=getActivity ();

            Preference reset=findPreference ("clearCache");
            Objects.requireNonNull (reset).setOnPreferenceClickListener (pref -> {

                Intent intent=new Intent ();
                intent.setAction (android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                assert activity != null;
                Uri uri=Uri.fromParts ("package", activity.getPackageName (), null);
                intent.setData (uri);
                Objects.requireNonNull (getActivity ()).startActivity (intent);

                return true;
            });
        }

        private void addDateListener() {

            Preference reset=findPreference ("time");
            Objects.requireNonNull (reset).setOnPreferenceClickListener (pref -> {

                PreferenceManager.setDefaultValues (Objects.requireNonNull (getActivity ()), R.xml.user_settings, false);
                final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences (getActivity ());
                sharedPref.edit ().putInt ("DatePicker", 0).apply ();

                Calendar now=Calendar.getInstance ();
                DatePickerDialog dpd=DatePickerDialog.newInstance (
                        SettingsFragment.this,
                        now.get (Calendar.YEAR),
                        now.get (Calendar.MONTH),
                        now.get (Calendar.DAY_OF_MONTH)
                );
                assert getFragmentManager () != null;
                dpd.show (getFragmentManager (), "DatePickerDialog");
                dpd.setThemeDark (true);

                return true;
            });
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            PreferenceManager.setDefaultValues (Objects.requireNonNull (getActivity ()), R.xml.user_settings, false);
            final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences (getActivity ());

            if (sharedPref.getInt ("DatePicker", 0) == 0) {
                sharedPref.edit ().putInt ("start_year", year).apply ();
                sharedPref.edit ().putInt ("start_month", monthOfYear).apply ();
                sharedPref.edit ().putInt ("start_day", dayOfMonth).apply ();

                Calendar now=Calendar.getInstance ();
                TimePickerDialog tpd=TimePickerDialog.newInstance (
                        SettingsFragment.this,
                        now.get (Calendar.HOUR_OF_DAY),
                        now.get (Calendar.MINUTE),
                        true
                );
                assert getFragmentManager () != null;
                tpd.show (getFragmentManager (), "DatePickerDialog");
                tpd.setThemeDark (true);
            } else {
                Calendar cal=Calendar.getInstance ();
                cal.set (Calendar.YEAR, year);
                cal.set (Calendar.MONTH, monthOfYear);
                cal.set (Calendar.DAY_OF_MONTH, dayOfMonth);
                cal.set (Calendar.HOUR_OF_DAY, 0);
                cal.set (Calendar.MINUTE, 0);

                sharedPref.edit ().putLong ("goalDate_next", cal.getTimeInMillis ()).apply ();
            }
        }

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

            PreferenceManager.setDefaultValues (Objects.requireNonNull (getActivity ()), R.xml.user_settings, false);
            final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences (getActivity ());

            Calendar cal=Calendar.getInstance ();
            cal.set (Calendar.YEAR, sharedPref.getInt ("start_year", 0));
            cal.set (Calendar.MONTH, sharedPref.getInt ("start_month", 0));
            cal.set (Calendar.DAY_OF_MONTH, sharedPref.getInt ("start_day", 0));
            cal.set (Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set (Calendar.MINUTE, minute);

            sharedPref.edit ().putLong ("startTime", cal.getTimeInMillis ()).apply ();
            sharedPref.edit ().putInt ("start_year", 0).apply ();
            sharedPref.edit ().putInt ("start_month", 0).apply ();
            sharedPref.edit ().putInt ("start_day", 0).apply ();

            sharedPref.edit ().putString ("entry_date", "").apply ();
        }

        private void addCigListener() {

            Preference reset=findPreference ("cig");
            Objects.requireNonNull (reset).setOnPreferenceClickListener (pref -> {

                PreferenceManager.setDefaultValues (Objects.requireNonNull (getActivity ()), R.xml.user_settings, false);
                final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences (getActivity ());


                AlertDialog.Builder builder=new AlertDialog.Builder (getActivity ());
                View dialogView=View.inflate (getActivity (), R.layout.dialog_cig, null);

                final EditText editNumber=dialogView.findViewById (R.id.editNumber);
                editNumber.setText (sharedPref.getString ("cig", ""));
                final EditText editTime=dialogView.findViewById (R.id.editTime);
                editTime.setText (sharedPref.getString ("duration", ""));

                builder.setView (dialogView);
                builder.setTitle (R.string.a_cig);
                builder.setPositiveButton (R.string.yes, (dialog, whichButton) -> {

                    String inputTag=editNumber.getText ().toString ().trim ();
                    sharedPref.edit ().putString ("cig", inputTag).apply ();
                    String inputTag2=editTime.getText ().toString ().trim ();
                    sharedPref.edit ().putString ("duration", inputTag2).apply ();
                });
                builder.setNegativeButton (R.string.goal_cancel, (dialog, whichButton) -> dialog.cancel ());

                final AlertDialog dialog2=builder.create ();
                // Display the custom alert dialog on interface
                dialog2.show ();

                new Handler ().postDelayed (() -> helper_main.showKeyboard (getActivity (), editNumber), 200);

                return true;
            });
        }

        private void addCigCostListener() {

            Preference reset=findPreference ("costs");
            Objects.requireNonNull (reset).setOnPreferenceClickListener (pref -> {

                PreferenceManager.setDefaultValues (Objects.requireNonNull (getActivity ()), R.xml.user_settings, false);
                final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences (getActivity ());


                AlertDialog.Builder builder=new AlertDialog.Builder (getActivity ());
                View dialogView=View.inflate (getActivity (), R.layout.dialog_cig_cost, null);

                final EditText editNumber=dialogView.findViewById (R.id.editNumber);
                editNumber.setText (sharedPref.getString ("costs", ""));

                builder.setView (dialogView);
                builder.setTitle (R.string.settings_costs);
                builder.setPositiveButton (R.string.yes, (dialog, whichButton) -> {

                    String inputTag=editNumber.getText ().toString ().trim ();
                    sharedPref.edit ().putString ("costs", inputTag).apply ();
                });
                builder.setNegativeButton (R.string.goal_cancel, (dialog, whichButton) -> dialog.cancel ());

                final AlertDialog dialog2=builder.create ();
                // Display the custom alert dialog on interface
                dialog2.show ();

                new Handler ().postDelayed (() -> helper_main.showKeyboard (getActivity (), editNumber), 200);

                return true;
            });
        }

        private void addGoalListener() {

            Preference reset=findPreference ("goalTitle");
            Objects.requireNonNull (reset).setOnPreferenceClickListener (pref -> {

                PreferenceManager.setDefaultValues (Objects.requireNonNull (getActivity ()), R.xml.user_settings, false);
                final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences (getActivity ());


                AlertDialog.Builder builder=new AlertDialog.Builder (getActivity ());
                View dialogView=View.inflate (getActivity (), R.layout.dialog_goal, null);

                final EditText editNumber=dialogView.findViewById (R.id.editTitle);
                editNumber.setText (sharedPref.getString ("goalTitle", ""));
                final EditText editTime=dialogView.findViewById (R.id.editCost);
                editTime.setText (sharedPref.getString ("goalCosts", ""));

                builder.setView (dialogView);
                builder.setTitle (R.string.a_goal);
                builder.setPositiveButton (R.string.yes, (dialog, whichButton) -> {

                    String inputTag=editNumber.getText ().toString ().trim ();
                    sharedPref.edit ().putString ("goalTitle", inputTag).apply ();
                    String inputTag2=editTime.getText ().toString ().trim ();
                    sharedPref.edit ().putString ("goalCosts", inputTag2).apply ();
                });
                builder.setNegativeButton (R.string.goal_cancel, (dialog, whichButton) -> dialog.cancel ());

                final AlertDialog dialog2=builder.create ();
                // Display the custom alert dialog on interface
                dialog2.show ();

                new Handler ().postDelayed (() -> helper_main.showKeyboard (getActivity (), editNumber), 200);

                return true;
            });
        }

        private void addGoal2Listener() {

            Preference reset=findPreference ("goalDate");
            Objects.requireNonNull (reset).setOnPreferenceClickListener (pref -> {

                PreferenceManager.setDefaultValues (Objects.requireNonNull (getActivity ()), R.xml.user_settings, false);
                final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences (getActivity ());
                sharedPref.edit ().putInt ("DatePicker", 1).apply ();

                Calendar now=Calendar.getInstance ();
                DatePickerDialog dpd=DatePickerDialog.newInstance (
                        SettingsFragment.this,
                        now.get (Calendar.YEAR),
                        now.get (Calendar.MONTH),
                        now.get (Calendar.DAY_OF_MONTH)
                );
                assert getFragmentManager () != null;
                dpd.show (getFragmentManager (), "DatePickerDialog");
                dpd.setThemeDark (true);

                return true;
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate (savedInstanceState);

            addPreferencesFromResource (R.xml.user_settings);
            addLicenseListener ();
            addClearCacheListener ();
            addDateListener ();
            addCigListener ();
            addCigCostListener ();
            addGoalListener ();
            addGoal2Listener ();
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        }
    }
}
