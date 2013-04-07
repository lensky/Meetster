package com.russia.meetster;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowPopUp extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set up main content view
        setContentView(R.layout.new_event);
        //this button will show the dialog
        Button button1main = (Button) findViewById(R.id.timedatebutton);
 
        button1main.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
                //set up dialog
                Dialog dialog = new Dialog(ShowPopUp.this);
                dialog.setContentView(R.layout.maindialog);
                dialog.setTitle("Select Start and End Time");
                dialog.setCancelable(true);
                //there are a lot of settings, for dialog, check them all out!
 
                //set up text
                TextView text = (TextView) dialog.findViewById(R.id.TextView01);
                text.setText(R.string.app_name);
 
                //set up image view
                ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
                img.setImageResource(R.drawable.ic_launcher);
 
                //set up button
                Button button = (Button) dialog.findViewById(R.id.Button01);
                button.setOnClickListener(new OnClickListener() {
                @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                //now that the dialog is set up, it's time to show it    
                dialog.show();
            }
        });
    }
 }
