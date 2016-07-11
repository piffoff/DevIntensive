package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.softdesign.devintensive.R;

/**
 * Created by Admin on 06.07.2016.
 */
public class TestClass extends AppCompatActivity{

   public void sendIntent(){

       TextView Message = (TextView) findViewById(R.id.user_email);
       String textMessage = Message.getText().toString();

       Intent intent = new Intent();
       intent.setAction(Intent.ACTION_SEND);
       intent.putExtra(Intent.EXTRA_TEXT, textMessage);
       intent.setType("text/plain");

       if (intent.resolveActivity(getPackageManager()) != null) {
           startActivity(intent);
       }
       /*принудительный выбор чем открывать интент*/
       Intent chooser = Intent.createChooser(intent, "Meke youre choose!");

       if (intent.resolveActivity(getPackageManager()) != null) {
           startActivity(chooser);
       }
   }

}
