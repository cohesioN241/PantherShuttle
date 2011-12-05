package org.cs4880.panthershuttle;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class SplashScreen extends Activity {
	/** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.splash);
      Thread splashThread = new Thread() {
         @Override
         public void run() {
            try {
            	MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.panther);
            	player.start();
               int waited = 0;
               while (waited < 3000) {
                  sleep(100);
                  waited += 100;
               }
            } catch (InterruptedException e) {
               // do nothing
            } finally {
               finish();
               Intent i = new Intent();
               i.setClassName("org.cs4880.panthershuttle",
                              "org.cs4880.panthershuttle.UniMap");
               startActivity(i);
            }
         }
      };
      splashThread.start();
   }
}