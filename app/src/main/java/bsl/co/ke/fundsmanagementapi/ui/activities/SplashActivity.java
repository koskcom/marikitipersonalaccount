package bsl.co.ke.fundsmanagementapi.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import bsl.co.ke.fundsmanagementapi.R;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 10000;


    private static Context mContext;
    static String userName;
    static String password;


    private static final String CHANNEL_ID = "1000000";


    static String uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        TextView appname = findViewById(R.id.appname);

        appname.setTextColor(getResources().getColor(R.color.yello));
        mContext = getApplicationContext();

        YoYo.with(Techniques.Bounce)
                .duration(3000)
                .playOn(findViewById(R.id.logo));

        YoYo.with(Techniques.FadeInUp)
                .duration(3000)
                .playOn(findViewById(R.id.appname));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}

