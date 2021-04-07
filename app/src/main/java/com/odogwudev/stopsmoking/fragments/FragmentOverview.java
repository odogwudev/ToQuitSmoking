package com.odogwudev.stopsmoking.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.odogwudev.stopsmoking.Constants;
import com.odogwudev.stopsmoking.CustomItemClickListener;
import com.odogwudev.stopsmoking.ItemOffsetDecoration;
import com.odogwudev.stopsmoking.MainActivity;
import com.odogwudev.stopsmoking.R;
import com.odogwudev.stopsmoking.TipsActivity;
import com.odogwudev.stopsmoking.TitleAdapter;
import com.odogwudev.stopsmoking.WebActivity;
import com.odogwudev.stopsmoking.helper.Activity_EditNote;
import com.odogwudev.stopsmoking.helper.helper_main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

;


public class FragmentOverview extends Fragment {

    ArrayList<String> quotes;
    TextView textView;
    ArrayList<String> titleArrayList;
    private TextView textView_time2;
    private TextView textView_time3;
    private TextView textView_time4;
    private TextView textView_cig2;
    private TextView textView_cig2_cost;
    private TextView textView_duration;
    private TextView textView_date2;
    private TextView textView_date3;
    private String currency;
    private String dateFormat;
    private String dateQuit;
    private String timeQuit;
    private SharedPreferences SP;
    private Button Button;
    private Context mContext;
    private CardView cardView;
    private ArrayList<Constants> imageModelArrayList;

    private int[] myImageList=new int[]{
            R.drawable.can_you_quit,
            R.drawable.sagging_arms,
            R.drawable.lips,
            R.drawable.damaged_teeth,
            R.drawable.stained_fingers,
            R.drawable.hair_loss,
            R.drawable.cataracts,
            R.drawable.psoriasis,
            R.drawable.eye_wrinkles,
            R.drawable.brittle_bones,
            R.drawable.heart_disease,
            R.drawable.reproductive_problems,
            R.drawable.early_menopause,
            R.drawable.oral_cancer,
            R.drawable.lung_cancer,
            R.drawable.age_spots
    };

    public FragmentOverview() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView=inflater.inflate (R.layout.fragment_main, container, false);
        imageModelArrayList=imageMode ();
        mContext=FragmentOverview.this.getActivity ();
        titleArrayList=new ArrayList<> ();
        titleArrayList.add (Constants.affects1);
        titleArrayList.add (Constants.affects2);
        titleArrayList.add (Constants.affects3);
        titleArrayList.add (Constants.affects4);
        titleArrayList.add (Constants.affects5);
        titleArrayList.add (Constants.affects6);
        titleArrayList.add (Constants.affects7);
        titleArrayList.add (Constants.affects8);
        titleArrayList.add (Constants.affects9);
        titleArrayList.add (Constants.affects10);
        titleArrayList.add (Constants.affects11);
        titleArrayList.add (Constants.affects12);
        titleArrayList.add (Constants.affects13);
        titleArrayList.add (Constants.affects14);
        titleArrayList.add (Constants.affects15);
        titleArrayList.add (Constants.affects16);

        RecyclerView mRecyclerView=rootView.findViewById (R.id.RecyclerView);
        mRecyclerView.setHasFixedSize (true);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (mContext.getApplicationContext (), LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration=new ItemOffsetDecoration (mContext, R.dimen.item_offset);
        mRecyclerView.addItemDecoration (itemDecoration);
        TitleAdapter adapter=new TitleAdapter (mContext, titleArrayList, imageModelArrayList, new CustomItemClickListener () {

            @Override
            public void onItemClick(View v, int position) {
                Intent webIntent=new Intent (FragmentOverview.this.getActivity (), WebActivity.class);
                webIntent.putExtra ("titles", titleArrayList.get (position));
                startActivity (webIntent);


            }
        });

        mRecyclerView.setAdapter (adapter);
        MainActivity mainActivity=new MainActivity ();
        MainActivity.showInterstitial ();
        PreferenceManager.setDefaultValues (Objects.requireNonNull (getActivity ()), R.xml.user_settings, false);
        SP=PreferenceManager.getDefaultSharedPreferences (getActivity ());

        textView_cig2_cost=rootView.findViewById (R.id.text_cigs2_cost);
        textView_cig2=rootView.findViewById (R.id.text_cigs2);
        textView_duration=rootView.findViewById (R.id.text_duration);
        textView_date2=rootView.findViewById (R.id.text_date2);
        textView_date3=rootView.findViewById (R.id.text_date3);

        textView_time2=rootView.findViewById (R.id.text_time2);
        textView_time3=rootView.findViewById (R.id.text_time3);
        textView_time4=rootView.findViewById (R.id.text_time4);
        cardView=rootView.findViewById (R.id.tips_card);
        cardView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (FragmentOverview.this.getActivity (), TipsActivity.class);
                startActivity (intent);

            }
        });

        textView=rootView.findViewById (R.id.motivationquote);
        quotes=new ArrayList<> ();
        quotes.add (getString (R.string.quote1));
        quotes.add (getString (R.string.quote2));
        quotes.add (getString (R.string.quote3));
        quotes.add (getString (R.string.quote4));
        quotes.add (getString (R.string.quote5));
        quotes.add (getString (R.string.quote6));
        quotes.add (getString (R.string.quote7));
        quotes.add (getString (R.string.quote8));
        quotes.add (getString (R.string.quote9));
        quotes.add (getString (R.string.quote10));
        quotes.add (getString (R.string.quote11));
        quotes.add (getString (R.string.quote12));
        quotes.add (getString (R.string.quote13));
        quotes.add (getString (R.string.quote14));
        quotes.add (getString (R.string.quote15));
        quotes.add (getString (R.string.quote16));
        quotes.add (getString (R.string.quote17));
        quotes.add (getString (R.string.quote18));
        quotes.add (getString (R.string.quote19));
        quotes.add (getString (R.string.quote20));
        Collections.shuffle (quotes);
        textView.setText (quotes.get (0));


        assert textView_date2 != null;
        assert textView_date3 != null;

        currency=SP.getString ("currency", "1");
        dateFormat=SP.getString ("dateFormat", "1");
        dateQuit=SP.getString ("date", "");
        timeQuit=SP.getString ("time", "");

        switch (dateFormat) {
            case "1":
                SimpleDateFormat format=new SimpleDateFormat ("yyyy-MM-dd HH:mm", Locale.getDefault ());
                setText (format);
                break;

            case "2":
                SimpleDateFormat format2=new SimpleDateFormat ("dd/MM/yyyy HH:mm", Locale.getDefault ());
                setText (format2);
                break;

            case "3":
                SimpleDateFormat format3=new SimpleDateFormat ("dd.MM.yyyy HH:mm", Locale.getDefault ());
                setText (format3);
                break;
        }

        setHasOptionsMenu (true);
        return rootView;


    }

    private ArrayList<Constants> imageMode() {

        ArrayList<Constants> list=new ArrayList<> ();

        for (int i=0; i < 16; i++) {
            Constants fruitModel=new Constants ();
            fruitModel.setImage_drawable (myImageList[i]);
            list.add (fruitModel);
        }

        return list;
    }


    private void setText(SimpleDateFormat format) {

        String dateStart=format.format (SP.getLong ("startTime", 0));
        dateQuit=dateStart.substring (0, 10);
        timeQuit=dateStart.substring (11, 16);

        try {

            helper_main.calculate (getActivity ());

            textView_time2.setText (SP.getString ("SPtimeDiffDays", "0") + " " + getString (R.string.time_days));
            textView_time3.setText (SP.getString ("SPtimeDiffHours", "0") + " " + getString (R.string.time_hours));
            textView_time4.setText (SP.getString ("SPtimeDiffMinutes", "0") + " " + getString (R.string.time_minutes));

            textView_date2.setText (String.valueOf (dateQuit));
            textView_date3.setText (String.valueOf (timeQuit));

            //Number of Cigarettes
            textView_cig2.setText (SP.getString ("SPcigSavedString", "0"));

            //Saved Money

            switch (currency) {
                case "1":
                    textView_cig2_cost.setText (SP.getString ("SPmoneySavedString", "0") + " " + getString (R.string.money_euro));
                    break;
                case "2":
                    textView_cig2_cost.setText (SP.getString ("SPmoneySavedString", "0") + " " + getString (R.string.money_dollar));
                    break;
                case "3":
                    textView_cig2_cost.setText (SP.getString ("SPmoneySavedString", "0") + " " + getString (R.string.money_pound));
                    break;
                case "4":
                    textView_cig2_cost.setText (SP.getString ("SPmoneySavedString", "0") + " " + getString (R.string.money_yen));
                    break;

                case "5":
                    textView_cig2_cost.setText (SP.getString ("SPmoneySavedString", "0") + " " + getString (R.string.money_Rupee));
                    break;
                case "6":
                    textView_cig2_cost.setText (SP.getString ("SPmoneySavedString", "0") + " " + getString (R.string.money_BRL));
                    break;
                case "7":
                    textView_cig2_cost.setText (SP.getString ("SPmoneySavedString", "0") + " " + getString (R.string.money_RUB));
                    break;
            }

            //Saved Time
            textView_duration.setText (SP.getString ("SPtimeSavedString", "0") + " " + getString (R.string.stat_h));

        } catch (Exception e) {
            e.printStackTrace ();
        }


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu (menu);
        menu.findItem (R.id.action_backup).setVisible (false);
        menu.findItem (R.id.action_image).setVisible (false);
        menu.findItem (R.id.action_filter).setVisible (false);
        menu.findItem (R.id.action_sort).setVisible (false);
        menu.findItem (R.id.action_info).setVisible (false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        final String days=textView_time2.getText ().toString ();
        final String hours=textView_time3.getText ().toString ();
        final String minutes=textView_time4.getText ().toString ();

        final String saved_cigarettes=textView_cig2.getText ().toString ();
        final String saved_money=textView_cig2_cost.getText ().toString ();
        final String saved_time=textView_duration.getText ().toString ();

        if (currency != null && currency.length () > 0 &&
                dateFormat != null && dateFormat.length () > 0 &&
                dateQuit != null && dateQuit.length () > 0 &&
                timeQuit != null && timeQuit.length () > 0) {

            switch (item.getItemId ()) {

                case R.id.action_share:

                    Intent sharingIntent=new Intent (Intent.ACTION_SEND);
                    sharingIntent.setType ("text/plain");
                    sharingIntent.putExtra (Intent.EXTRA_SUBJECT, getString (R.string.share_subject));

                    sharingIntent.putExtra (Intent.EXTRA_TEXT, getString (R.string.share_text) + " " +
                            days + " " + hours + " " + getString (R.string.share_text2) + " " + minutes + ". " +
                            getString (R.string.share_text3) + " " + saved_cigarettes + " " + getString (R.string.share_text4) + ", " +
                            saved_money + " " + getString (R.string.share_text5) + " " +
                            saved_time + " " + "\n " + "\n " + getString (R.string.share_message));

                    startActivity (Intent.createChooser (sharingIntent, "Share using"));
                    return true;

                case R.id.action_reset:
                    Snackbar snackbar=Snackbar
                            .make (textView_time2, R.string.reset_confirm, Snackbar.LENGTH_LONG)
                            .setAction (R.string.yes, new View.OnClickListener () {
                                @SuppressWarnings("ConstantConditions")
                                @Override
                                public void onClick(View view) {

                                    String title=days + " " + hours + " " + getString (R.string.share_text2) + " " + minutes;

                                    String text=getString (R.string.share_text_fail) + " " +
                                            days + " " + hours + " " + getString (R.string.share_text2) + " " + minutes + ". " +
                                            getString (R.string.share_text3) + " " + saved_cigarettes + " " + getString (R.string.share_text4) + ", " +
                                            saved_money + " " + getString (R.string.share_text5) + " " +
                                            saved_time + " " + getString (R.string.share_text6);

                                    SP.edit ()
                                            .putString ("handleTextTitle", title)
                                            .putString ("handleTextText", text)
                                            .putString ("handleTextCreate", helper_main.createDate ())
                                            .apply ();
                                    Intent intent=new Intent (getActivity (), Activity_EditNote.class);
                                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    getActivity ().startActivity (intent);

                                    SP.edit ().putLong ("startTime", Calendar.getInstance ().getTimeInMillis ()).apply ();
                                    getActivity ().finish ();
                                }
                            });
                    snackbar.show ();
                    return true;
            }
        }

        return super.onOptionsItemSelected (item);
    }
}
