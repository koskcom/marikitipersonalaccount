package bsl.co.ke.fundsmanagementapi.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import bsl.co.ke.fundsmanagementapi.PERSONALAPI;
import bsl.co.ke.fundsmanagementapi.R;
import bsl.co.ke.fundsmanagementapi.database.DataBaseAdapter;
import bsl.co.ke.fundsmanagementapi.database.DatabaseHelper;
import bsl.co.ke.fundsmanagementapi.ui.views.widgets.InputValidation;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bsl.co.ke.fundsmanagementapi.settings.Settings.hasPermissions;


public class LoginActivity extends AppCompatActivity {


    private NestedScrollView nestedScrollView;
    int loginCntr = 3;

    private AppCompatButton appCompatButtonLogin;

    EditText textInputEditTextphoneNumber;
    EditText textInputEditTextpassword;
    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;


    @BindView(R.id.textInputEditTextPhoneNumber)
    EditText textInputEditTextPhoneNumber;
    @BindView(R.id.textInputEditTextPassword)
    EditText textInputEditTextPassword;

    int PERMISSION_ALL = 1;
    public ProgressDialog mprogress;
    private static int SPLASH_TIME_OUT = 3000;

    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    //static LoginRequest login;

    static String userName;

    PERSONALAPI app;
    DataBaseAdapter db;
    private Intent serviceIntent;
    Snackbar snackbar;
    private ScrollView scrollView;


    String phonenumber, pin;

    private static final int DATA_SETUP_SERVICE_ID = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkPermissions();
        //  app = (FMAPI) getApplication();
        db = new DataBaseAdapter(getApplicationContext());
        inputValidation = new InputValidation(this);


        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);


        textInputEditTextphoneNumber = (EditText) findViewById(R.id.textInputEditTextPhoneNumber);
        textInputEditTextpassword = (EditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);


        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyFromSQLite();
            }
        });
        textViewLinkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
            }
        });
    }
    private void checkPermissions() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void verifyFromSQLite() {

        phonenumber = Objects.requireNonNull(textInputEditTextPhoneNumber.getText()).toString().trim();
        pin = Objects.requireNonNull(textInputEditTextPassword.getText()).toString().trim();


        if (TextUtils.isEmpty(phonenumber)) {
            textInputEditTextPhoneNumber.setError("Field Required");
            return;
        }
        if (TextUtils.isEmpty(pin)) {
            textInputEditTextPassword.setError("Pin Required");
            return;
        } else if (db.checkUser(textInputEditTextPhoneNumber.getText().toString().trim(), textInputEditTextPassword.getText().toString().trim())) {
            new FetchLogin().execute();
            // accountsIntent.putExtra("EMAIL", textInputEditTextIdNo.getText().toString().trim());
            emptyInputEditText();

        } else if (loginCntr != 1) {
            //unregistered user, display unregistered user msg and decrease login counter
            loginCntr = loginCntr - 1;

            Toast.makeText(getApplicationContext(), "Access Denied! Please try again.You have " + loginCntr + " attempt(s) remaining", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Username or password", Toast.LENGTH_SHORT).show();
            // Snack Bar to show success message that record is wrong
            //    Snackbar.make(nestedScrollView, getString(R.string.error_valid_no_password), Snackbar.LENGTH_LONG).show();
            // Toast.makeText(this, "Invalid trader number or password", Toast.LENGTH_SHORT);
        }
    }

    private void emptyInputEditText() {
        textInputEditTextPhoneNumber.setText(null);
        textInputEditTextPassword.setText(null);
    }

    private boolean validatePassword(String pass) {
        if (pass.length() < 3 || pass.length() > 20) {
            textInputEditTextPassword.setError("Password Must consist of 3 to 20 characters");
            return false;
        }
        return true;
    }

    private boolean validateUsername(String email) {

        /* if (email.length() < 4 *//*|| email.length() > 30*//*) {
            textInputEditTextPhoneNumber.setError("Email Must consist of at least characters");
            return false;
        } else if (!email.matches("^[A-za-z0-9.@]+")) {
            textInputEditTextPhoneNumber.setError("Only . and @ characters allowed");
            return false;
        } else if (!email.contains("@") || !email.contains(".")) {
            textInputEditTextPhoneNumber.setError("Email must contain @ and .");
            return false;
        }*/
        return true;
    }

    public class FetchLogin extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mprogress = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.show();
            mprogress.setMessage("Authenticating...Please wait!!!");
            mprogress.setCancelable(true);
            mprogress.setIndeterminate(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(Void result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    // Start your app main activity
                    Intent intent = new Intent(getApplicationContext(), PersonalDashboardActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
        }
    }

}
