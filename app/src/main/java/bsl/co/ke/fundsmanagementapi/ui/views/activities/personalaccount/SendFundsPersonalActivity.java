package bsl.co.ke.fundsmanagementapi.ui.views.activities.personalaccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;
import java.util.Objects;

import bsl.co.ke.fundsmanagementapi.R;
import bsl.co.ke.fundsmanagementapi.database.DataBaseAdapter;
import bsl.co.ke.fundsmanagementapi.marikiti.Deposit;
import bsl.co.ke.fundsmanagementapi.marikiti.Withdraw;
import bsl.co.ke.fundsmanagementapi.model.BSend;
import bsl.co.ke.fundsmanagementapi.ui.activities.PersonalDashboardActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SendFundsPersonalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //declare constants used with shared preferences
    public static final String MY_BALANCE = "My_Balance";
    public static final String CHECKING_KEY = "checking_key";
    public static final String SAVINGS_KEY = "savings_key";
    //declare variables
    public String receivedBalanceC, receivedBalanceS; //data received from menu activity
    public DecimalFormat currency = new DecimalFormat("Ksh: ###,##0.00"); //decimal formatting
    TextView cBalanceTV, sBalanceTV;
    public double cBalanceD, sBalanceD, cNewBalance, sNewBalance;
    public double SendEntered;
    int transferChoice; //spinner inde
    SharedPreferences.Editor myEditor;
    //declare variables
    public String receivedBalance, receivedKey, receivedTitle; //data received from menu activity
    public double BalanceD;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ssRecipientNumber)
    Spinner ssRecipientNumber;
    @BindView(R.id.etamount)
    EditText etamount;
    @BindView(R.id.etmarikitipin)
    EditText etmarikitipin;
    @BindView(R.id.etRecipientNumber)
    EditText etRecipientNumber;
    @BindView(R.id.sendButton)
    AppCompatButton sendButton;
    DataBaseAdapter db;
    String agentno;
    AppCompatButton btnAgentQrsScan;

    String[] deposit_type = {"-Select Type-", "Marikiti APP Account", "Other Networks"};
    ImageView imgdepo;
    String sentviaType, amount, pin, recipientNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_funds);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //receive checking and savings balance from menu activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            receivedBalanceC = extras.getString("balanceC");
            receivedBalanceS = extras.getString("balanceS");
        }

        db = new DataBaseAdapter(getApplicationContext());
        //set current balance of checking account
        cBalanceTV = (TextView) findViewById(R.id.cBalanceTextView);
        cBalanceD = Double.parseDouble(String.valueOf(receivedBalanceC));
        cBalanceTV.setText(String.valueOf(currency.format(cBalanceD)));

        //set current balance of savings account
        sBalanceTV = (TextView) findViewById(R.id.sBalanceTextView);
        sBalanceD = Double.parseDouble(String.valueOf(receivedBalanceS));
        sBalanceTV.setText(String.valueOf(currency.format(sBalanceD)));

        imgdepo = (ImageView) findViewById(R.id.imgdepo);
        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //declare transfer input amount, Spinner, and Button
        final EditText amountET = (EditText) findViewById(R.id.etamount);
        final Spinner TransferS = (Spinner) findViewById(R.id.TransferSpinner);
        Button sendB = (Button) findViewById(R.id.sendButton);

        Spinner sentviaType = (Spinner) findViewById(R.id.ssRecipientNumber);

        //Creating the ArrayAdapter instance having  list
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, deposit_type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner

        sentviaType.setAdapter(arrayAdapter);

        //register deposit button with Event Listener class, and Event handler method
        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentviaType = Objects.requireNonNull(ssRecipientNumber.getSelectedItem()).toString().trim();
                String recipientNumber = Objects.requireNonNull(etRecipientNumber.getText()).toString().trim();
                String pin = Objects.requireNonNull(etmarikitipin.getText()).toString().trim();

                if (TextUtils.isEmpty(recipientNumber)) {
                    etRecipientNumber.setError("Amount Required");
                    return;
                }

                if (TextUtils.isEmpty(pin)) {
                    etmarikitipin.setError("Pin Required");
                    return;
                }
                //check if transfer amount was entered
                else if (!TextUtils.isEmpty(amountET.getText())) {

                    SendEntered = Double.parseDouble(String.valueOf(amountET.getText()));
                    //get index of spinner string array
                    transferChoice = TransferS.getSelectedItemPosition();
                    //choose between two available transfer options
                    switch (transferChoice) {
                        //transfer funds from checking to savings
                        case 1:

                            //check if transfer amount is valid
                            if (cBalanceD >= SendEntered) {

                                //withdraw from checking
                                Withdraw wd = new Withdraw();
                                wd.setBalance(cBalanceD);
                                wd.setWithdraw(SendEntered);
                                //set new checking balance
                                cNewBalance = wd.getNewBalance();

                                cBalanceTV.setText(String.valueOf(currency.format(cNewBalance)));
                                cBalanceD = cNewBalance;

                                //deposit to savings
                                Deposit dp = new Deposit();
                                dp.setBalance(sBalanceD);
                                dp.setDeposit(SendEntered);

                                //set new savings balance
                                sNewBalance = dp.getNewBalance();

                                sBalanceTV.setText(String.valueOf(currency.format(sNewBalance)));
                                sBalanceD = sNewBalance;

                                //reset transfer amount
                                SendEntered = 0;
                                new sentbtn().execute();
                            }//end checking if transfer is valid
                            //transfer amount is not valid
                            else {
                                //send msg insufficient funds
                                noFundsMsg();
                            }//end transfer is not valid msg
                            return;

                        //transfer funds from savings  to checking
                        case 2:

                            //check if transfer amount is valid
                            if (sBalanceD >= SendEntered) {

                                //withdraw from savings
                                Withdraw wd = new Withdraw();
                                wd.setBalance(sBalanceD);
                                wd.setWithdraw(SendEntered);
                                //set new savings balance
                                sNewBalance = wd.getNewBalance();

                                sBalanceTV.setText(String.valueOf(currency.format(sNewBalance)));
                                sBalanceD = sNewBalance;

                                //deposit to checking
                                Deposit dp = new Deposit();
                                dp.setBalance(cBalanceD);
                                dp.setDeposit(SendEntered);

                                //set new checking balance
                                cNewBalance = dp.getNewBalance();

                                cBalanceTV.setText(String.valueOf(currency.format(cNewBalance)));
                                cBalanceD = cNewBalance;

                                //reset transfer amount
                                SendEntered = 0;
                                new sentbtn().execute();
                            }////end checking if transfer is valid
                            //transfer amount is not valid
                            else {

                                //send msg insufficient funds
                                noFundsMsg();
                            }//end transfer is not valid msg

                            return;
                    }//end switch transferChoice
                }//end check if transfer amount was entered
                //user didn't enter transfer amount
                else {

                    //send msg no amount entered
                    noAmountMsg();
                }//end transfer amount was not entered msg
            }//end if
        });//end onClickListener TransferButton
    }//end onCreate

    public void noFundsMsg() {

        Toast.makeText(SendFundsPersonalActivity.this, "Insufficient funds! Please enter a valid transfer amount and try again!", Toast.LENGTH_LONG).show();
    }//end noFundsMsg

    //function to prompt user for input
    public void noAmountMsg() {

        Toast.makeText(SendFundsPersonalActivity.this, "Nothing entered! Please enter transfer amount and try again!", Toast.LENGTH_LONG).show();
    }//noAmountMsg

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //function to open and edit shared preferences file
    protected void onPause() {

        super.onPause();
        //open shared preferences xml file
        myEditor = getSharedPreferences(MY_BALANCE, MODE_PRIVATE).edit();

        //save new checking and savings balance
        myEditor.putString(CHECKING_KEY, String.valueOf(cBalanceD));
        myEditor.putString(SAVINGS_KEY, String.valueOf(sBalanceD));
        myEditor.apply();

    }//end onPause

    boolean validateSpinneracc(Spinner spinner, String error) {

        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("-Select Type-")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void sentrequest() {


        BSend bSend = new BSend();

        bSend.setPayment_type(sentviaType);
        bSend.setRecipient_number(recipientNumber);
        bSend.setAmount(amount);
        bSend.setMarikiti_pin(pin);

        db.insertpersonalsents(bSend);
        new sentbtn().execute();
       /* boolean valid = validateSpinneracc(ssRecipientNumber, sentviaType);
        if (valid) {
            if (!db.checkpin(etmarikitipin.getText().toString().trim())) {
                db.insertpersonalsents(bSend);
                new sentbtn().execute();
            } else {
                Toast.makeText(SendFundsPersonalActivity.this, "Wrong User Pin Entered!", Toast.LENGTH_LONG).show();
            }
        }*/
    }

    private class sentbtn extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mprogress = new ProgressDialog(SendFundsPersonalActivity.this);

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
                 //   Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SendFundsPersonalActivity.this, PersonalDashboardActivity.class);
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}//end  TransactionActivity


