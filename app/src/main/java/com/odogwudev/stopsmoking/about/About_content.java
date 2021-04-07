package com.odogwudev.stopsmoking.about;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.odogwudev.stopsmoking.R;
import com.odogwudev.stopsmoking.helper.Activity_intro;


class About_content {

    static MaterialAboutList createMaterialAboutList(final Context c) {
        MaterialAboutCard.Builder appCardBuilder=new MaterialAboutCard.Builder ();

        // Add items to card

        appCardBuilder.addItem (new MaterialAboutTitleItem.Builder ()
                .text (R.string.app_name)
                .icon (R.mipmap.ic_launcher)
                .build ());

        try {

            appCardBuilder.addItem (ConvenienceBuilder.createVersionActionItem (c,
                    ContextCompat.getDrawable (c, R.drawable.earth2),
                    "Version",
                    false));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }

        appCardBuilder.addItem (new MaterialAboutActionItem.Builder ()
                .text (R.string.more_app)
                .subText (R.string.about_changelog_summary)
                .icon (R.drawable.format_list_bulleted)
                // TODO: Replace with your own Store link.
                .setOnClickListener (ConvenienceBuilder.createWebsiteOnClickAction (c, Uri.parse ("http://slideme.org/store/apps/dev?id=xxxxxxxx")))
                .build ());

        appCardBuilder.addItem (new MaterialAboutActionItem.Builder ()
                .text (R.string.privacy_policy)
                .subText (R.string.about_privacy_policy_summary)
                .icon (R.drawable.ic_security)
                .setOnClickListener (new MaterialAboutActionItem.OnClickListener () {
                    @Override
                    public void onClick() {
                        SpannableString s;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            // TODO: Replace with your own privacy policy URL in strings.xml .
                            s=new SpannableString (Html.fromHtml (c.getString (R.string.about_text), Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            //noinspection deprecation
                            s=new SpannableString (Html.fromHtml (c.getString (R.string.about_text)));
                        }

                        Linkify.addLinks (s, Linkify.WEB_URLS);

                        final AlertDialog d=new AlertDialog.Builder (c, R.style.CustomAlertInfoDialogTheme)
                                .setTitle (R.string.about_title)
                                .setMessage (s)
                                .setPositiveButton (c.getString (R.string.yes),
                                        new DialogInterface.OnClickListener () {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel ();
                                            }
                                        }).show ();
                        d.show ();
                        ((TextView) d.findViewById (android.R.id.message)).setMovementMethod (LinkMovementMethod.getInstance ());
                    }
                })
                .build ());

        appCardBuilder.addItem (new MaterialAboutActionItem.Builder ()
                .text (R.string.about_intro)
                .subText (R.string.about_intro_summary)
                .icon (R.drawable.information_outline_dark)
                .setOnClickListener (new MaterialAboutActionItem.OnClickListener () {
                    @Override
                    public void onClick() {
                        Intent intent=new Intent (c, Activity_intro.class);
                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        c.startActivity (intent);
                    }
                })
                .build ());


        MaterialAboutCard.Builder authorCardBuilder=new MaterialAboutCard.Builder ();
        authorCardBuilder.title (R.string.about_title_rate);

        authorCardBuilder.addItem (new MaterialAboutActionItem.Builder ()
                .text (R.string.rate_us)
                .subText (R.string.rate_us_summary)
                .icon (R.drawable.ic_smoking_rooms)
                .setOnClickListener (ConvenienceBuilder.createWebsiteOnClickAction (c, Uri.parse ("market://details?id=" + c.getPackageName ())))
                .build ());


        return new MaterialAboutList (appCardBuilder.build (), authorCardBuilder.build ());
    }

}
