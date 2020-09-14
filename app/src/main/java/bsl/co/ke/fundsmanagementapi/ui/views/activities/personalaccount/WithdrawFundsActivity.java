package bsl.co.ke.fundsmanagementapi.ui.views.activities.personalaccount;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import bsl.co.ke.fundsmanagementapi.R;
import bsl.co.ke.fundsmanagementapi.marikiti.Withdraw;
import bsl.co.ke.fundsmanagementapi.ui.activities.PersonalDashboardActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WithdrawFundsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //declare constant of shared preferences file
    public static final String MY_BALANCE = "My_Balance";

    //declare variables
    public String receivedBalance, receivedKey, receivedTitle; //data received from menu activity
    public double BalanceD;
    public double DepositEntered, NewBalance, WithdrawEntered;
    TextView BalanceTV, TitleTV;
    public DecimalFormat currency = new DecimalFormat("Ksh: ###,##0.00"); //decimal formatting
    SharedPreferences.Editor myEditor;
    public static final String CHECKING_KEY = "checking_key";
    public static final String SAVINGS_KEY = "savings_key";
    //declare variables
    public String receivedBalanceC, receivedBalanceS; //data received from menu activity
    TextView cBalanceTV, sBalanceTV;
    public double cBalanceD, sBalanceD, cNewBalance, sNewBalance;
    public double SendEntered;
    int transferChoice; //spinner inde

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ettraderNumber)
    EditText ettraderNumber;
    @BindView(R.id.etAgentNumber)
    EditText etAgentNumber;
    @BindView(R.id.etmarikitipin)
    EditText etmarikitipin;
    @BindView(R.id.WithdrawButton)
    AppCompatButton WithdrawButton;
    @BindView(R.id.btnAgentQrsScan)
    AppCompatButton btnAgentQrsScan;

    EditText traderno;
    EditText agentNumber;
    EditText withdrawtET;
    EditText marikitipin;
    ImageView imgdepo;
    String depositType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //scanner
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        //set visibility to gone
        surfaceView.setVisibility(View.GONE);

        //receive balance, key and title from menu activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            receivedBalance = extras.getString("balance");
            receivedKey = extras.getString("key");

        }
        //set current balance of checking or savings account
        BalanceTV = (TextView) findViewById(R.id.cBalanceTextView);
        BalanceD = Double.parseDouble(String.valueOf(receivedBalance));
        BalanceTV.setText(String.valueOf(currency.format(BalanceD)));


        imgdepo = (ImageView) findViewById(R.id.imgdepo);
        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set current balance of checking or savings account
        btnAgentQrsScan = (AppCompatButton) findViewById(R.id.btnAgentQrsScan);

        //declare deposit button
        Button withdrawbtn = (Button) findViewById(R.id.WithdrawButton);
        //declare user deposit input amount
        final Spinner ssDepositType = (Spinner) findViewById(R.id.ssDepositType);
        traderno = (EditText) findViewById(R.id.ettraderNumber);
        agentNumber = (EditText) findViewById(R.id.etAgentNumber);
        withdrawtET = (EditText) findViewById(R.id.etAmount);
        marikitipin = (EditText) findViewById(R.id.etmarikitipin);

        btnAgentQrsScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surfaceView.setVisibility(View.VISIBLE);
                initialiseDetectorsources();
            }
        });
        //register withdraw button with Event Listener class, and Event handler method
        withdrawbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if withdraw field is not empty, get withdraw amount
                String traderno = Objects.requireNonNull(ettraderNumber.getText()).toString().trim();
                String agentNumber = Objects.requireNonNull(etAgentNumber.getText()).toString().trim();
                String marikitipin = Objects.requireNonNull(etmarikitipin.getText()).toString().trim();

                if (TextUtils.isEmpty(traderno)) {
                    ettraderNumber.setError("Field Required");
                    return;
                }
                if (TextUtils.isEmpty(agentNumber)) {
                    etAgentNumber.setError("Field Required");
                    return;
                }
                if (TextUtils.isEmpty(marikitipin)) {
                    etmarikitipin.setError("Field Required");
                    return;
                } else if (!TextUtils.isEmpty(withdrawtET.getText())) {

                    WithdrawEntered = Double.parseDouble(String.valueOf(withdrawtET.getText()));
                    //check if there's enough money to withdraw in the acoount
                    if (BalanceD >= WithdrawEntered) {
                        //create withdraw object
                        Withdraw wd = new Withdraw();
                        wd.setBalance(BalanceD);
                        wd.setWithdraw(WithdrawEntered);

                        //calculate new balance
                        NewBalance = wd.getNewBalance();

                        BalanceTV.setText(String.valueOf(currency.format(NewBalance)));
                        BalanceD = NewBalance;
                        //reset user withdraw amount to zero
                        WithdrawEntered = 0;
                        sentrequest();

                    }//end 2nd if
                    //there's not enough money in the account, prompt user for valid input
                    else
                        Toast.makeText(WithdrawFundsActivity.this, "Insufficient funds! Please enter a valid withdraw amount and try again!", Toast.LENGTH_LONG).show();
                }//end 1st if
                //withdraw filed is empty, prompt user to enter withdraw amount
                else {
                    Toast.makeText(WithdrawFundsActivity.this, "Nothing entered! Please enter withdraw amount and try again!", Toast.LENGTH_LONG).show();
                }

                //clear withdraw field
                ettraderNumber.setText(null);
                etAgentNumber.setText(null);
                withdrawtET.setText(null);
                etmarikitipin.setText(null);
            }
        });//end Withdraw onClick

    }

    protected void onPause() {

        super.onPause();
        //open shared preferences xml file
        myEditor = getSharedPreferences(MY_BALANCE, MODE_PRIVATE).edit();

        //save new checking or savings balance
        myEditor.putString(receivedKey, String.valueOf(BalanceD));
        myEditor.apply();

    }//end onPause
    //function to open and edit shared preferences file


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);
        if (selection.equals("Agent Deposit")) {
            surfaceView.setVisibility(View.GONE);
            initialiseDetectorsources();
            etAgentNumber.setEnabled(false);
            etAgentNumber.setFocusable(false);

        } else if (selection.equals("-Select Type-")) {
            surfaceView.setVisibility(View.GONE);
            etAgentNumber.setEnabled(false);
            etAgentNumber.setFocusable(false);
            Toast.makeText(this, "Function Required", Toast.LENGTH_SHORT);
        } else if (selection.equals("Debit/Credit Card")) {
            surfaceView.setVisibility(View.GONE);
            etAgentNumber.setEnabled(false);
            etAgentNumber.setFocusable(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    boolean validateSpinneracc(Spinner spinner, String error) {

        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("-Select Type-")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Field Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void sentrequest() {
        // DepositEntered = Double.parseDouble(String.valueOf(DepositET.getText()));
        //create deposit object
      /*  if (depositType.equals("Debit/Credit Card")) {
            Toast.makeText(getApplicationContext(), "Directed to Third Party Bank API to\n" +
                    "complete transaction", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(WithdrawFundsActivity.this, PersonalAcountActivity.class);
            startActivity(intent);
        }*/

        new withdrawbtn().execute();

    }

    @Override
    public void onClick(View v) {

    }

    private class withdrawbtn extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mprogress = new ProgressDialog(WithdrawFundsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.show();
            mprogress.setMessage("Sending...");
            mprogress.setCancelable(true);
            mprogress.setIndeterminate(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(4000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            if (mprogress.isShowing()) {
                mprogress.dismiss();
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    Intent intent = new Intent(WithdrawFundsActivity.this, PersonalDashboardActivity.class);
                    startActivity(intent);
                    finish();
                    finishAffinity();

                }
            });
        }
    }

    //scanner
    private void initialiseDetectorsources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(WithdrawFundsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(WithdrawFundsActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    ettraderNumber.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                ettraderNumber.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                ettraderNumber.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {

                                barcodeData = barcodes.valueAt(0).displayValue;
                                ettraderNumber.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            }
                        }
                    });

                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}//end  TransactionActivity

