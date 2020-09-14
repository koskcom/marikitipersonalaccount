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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import bsl.co.ke.fundsmanagementapi.database.DataBaseAdapter;
import bsl.co.ke.fundsmanagementapi.marikiti.Deposit;
import bsl.co.ke.fundsmanagementapi.model.DePo;
import bsl.co.ke.fundsmanagementapi.ui.activities.PersonalDashboardActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DepositActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    private DataBaseAdapter db;

    @BindView(R.id.ssDepositType)
    Spinner ssDepositType;
    @BindView(R.id.etAgentNumber)
    EditText etAgentNumber;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.DepositButton)
    AppCompatButton DepositButton;

    String deposittype, agentno, amount;
    AppCompatButton depositButton;
    AppCompatButton btnAgentQrsScan;
    String[] deposit_type = {"-Select Type-", "Agent Deposit", "Debit/Credit Card"};
    String depositType;

    EditText agentNumber;

    ImageView imgdepo;
    //declare constant of shared preferences file
    public static final String MY_BALANCE = "My_Balance";

    //declare variables
    public String receivedBalance, receivedKey, receivedTitle; //data received from menu activity
    public double BalanceD;
    public double DepositEntered, NewBalance, WithdrawEntered;
    TextView BalanceTV, TitleTV;
    public DecimalFormat currency = new DecimalFormat("Ksh: ###,##0.00"); //decimal formatting
    SharedPreferences.Editor myEditor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DataBaseAdapter(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        imgdepo = (ImageView) findViewById(R.id.imgdepo);
        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (extras != null) {
            receivedBalance = extras.getString("balance");
            receivedKey = extras.getString("key");

        }
        BalanceTV = (TextView) findViewById(R.id.BalanceTextView);
        BalanceD = Double.parseDouble(String.valueOf(receivedBalance));
        BalanceTV.setText(String.valueOf(currency.format(BalanceD)));
        //scanner
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);

        //set visibility to gone

        surfaceView.setVisibility(View.GONE);
        etAgentNumber.setEnabled(false);
        etAgentNumber.setFocusable(false);

        Spinner sdepositType = (Spinner) findViewById(R.id.ssDepositType);
        sdepositType.setOnItemSelectedListener(this);
        //Setting the ArrayAdapter data on the Spinner
        //Creating the ArrayAdapter instance having  list
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, deposit_type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sdepositType.setAdapter(arrayAdapter);

        //receive balance, key and title from menu activity

        //set current balance of checking or savings account
        btnAgentQrsScan = (AppCompatButton) findViewById(R.id.btnAgentQrsScan);

        //declare deposit button
        Button DepositB = (Button) findViewById(R.id.DepositButton);
        //declare user deposit input amount
        agentNumber = (EditText) findViewById(R.id.etAgentNumber);
        final EditText DepositET = (EditText) findViewById(R.id.etAmount);

        //register deposit button with Event Listener class, and Event handler method
        DepositB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if deposit field is not empty, get deposit amount
                String agentNumber = Objects.requireNonNull(etAgentNumber.getText()).toString().trim();
               /* if (TextUtils.isEmpty(agentNumber)) {
                    etAgentNumber.setError("Agent Number Required!");
                    return;
                }*/
                if (!TextUtils.isEmpty(DepositET.getText())) {
                    DepositEntered = Double.parseDouble(String.valueOf(DepositET.getText()));
                    //create deposit object
                    Deposit dp = new Deposit();
                    dp.setBalance(BalanceD);
                    dp.setDeposit(DepositEntered);

                    //calculate new balance
                    NewBalance = dp.getNewBalance();

                    BalanceTV.setText(String.valueOf(currency.format(NewBalance)));
                    BalanceD = NewBalance;
                    //reset user deposit amount to zero
                    DepositEntered = 0;
                    sentrequest();

                }//end if
                //deposit filed is empty, prompt user to enter deposit amount
                else {

                    Toast.makeText(DepositActivity.this, "Please enter amount and try again!", Toast.LENGTH_LONG).show();
                }//end else
                //clear deposit field
                etAgentNumber.setText(null);
                DepositET.setText(null);

            }
        });//end DepositB OnClick

        btnAgentQrsScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                surfaceView.setVisibility(View.VISIBLE);
                initialiseDetectorsources();

                // surfaceView.setVisibility(View.GONE);

            }
        });

    }//end onCreate

    //function to open and edit shared preferences file

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);
        if (selection.equals("-Select Deposit Type-")) {
            surfaceView.setVisibility(View.GONE);
            etAgentNumber.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Function Required", Toast.LENGTH_SHORT);
        } else if (selection.equals("Agent Deposit")) {
            etAgentNumber.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
            initialiseDetectorsources();
        } else if (selection.equals("Debit/Credit Card")) {
            surfaceView.setVisibility(View.GONE);
            etAgentNumber.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "Directed to Third Party Bank API to\n" +
                    "complete transaction", Toast.LENGTH_SHORT).show();
          /*  Intent intent = new Intent(DepositAcitivity.this, ActivityFundsDasboard.class);
            startActivity(intent);*/
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    boolean validateSpinneracc(Spinner spinner, String error) {

        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("Select Deposit Type")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void sentrequest() {
        depositType = Objects.requireNonNull(ssDepositType.getSelectedItem()).toString().trim();
        agentno = Objects.requireNonNull(etAgentNumber.getText()).toString().trim();
        amount = Objects.requireNonNull(etAmount.getText()).toString().trim();
        DePo depo = new  DePo();
        depo.setAgentDeposittype(depositType);
        depo.setAgentNumber(agentno);
        depo.setAmount(amount);

       // db.insertpersonaldeposits(depo);
        new Topupbtn().execute();
    }

    @Override
    public void onClick(View v) {

    }

    private class Topupbtn extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mprogress = new ProgressDialog(DepositActivity.this);

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
                   // Toast.makeText(getApplicationContext(), "Successfully Sent", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DepositActivity.this, PersonalDashboardActivity.class);
                    startActivity(intent);

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
                .setAutoFocusEnabled(true)//you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(DepositActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(DepositActivity.this, new
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


                    etAgentNumber.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                etAgentNumber.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                etAgentNumber.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                etAgentNumber.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            }
                        }
                    });

                }
            }
        });

    }

    //function to open and edit shared preferences file
    protected void onPause() {

        super.onPause();
        cameraSource.release();
        //open shared preferences xml file
        myEditor = getSharedPreferences(MY_BALANCE, MODE_PRIVATE).edit();

        //save new checking or savings balance
        myEditor.putString(receivedKey, String.valueOf(BalanceD));
        myEditor.apply();

    }//end onPause

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsources();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}//end  TransactionActivity

