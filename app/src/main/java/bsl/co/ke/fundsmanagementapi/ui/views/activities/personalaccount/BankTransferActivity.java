package bsl.co.ke.fundsmanagementapi.ui.views.activities.personalaccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;
import java.util.Objects;

import bsl.co.ke.fundsmanagementapi.R;
import bsl.co.ke.fundsmanagementapi.database.DataBaseAdapter;
import bsl.co.ke.fundsmanagementapi.marikiti.Deposit;
import bsl.co.ke.fundsmanagementapi.model.Withdraw;
import bsl.co.ke.fundsmanagementapi.ui.activities.PersonalDashboardActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BankTransferActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //declare constants used with shared preferences
    public static final String MY_BALANCE = "My_Balance";
    public static final String CHECKING_KEY = "checking_key";
    public static final String SAVINGS_KEY = "savings_key";
    //declare variables
    public String receivedBalanceC, receivedBalanceS; //data received from menu activity
    public DecimalFormat currency = new DecimalFormat("Ksh: ###,##0.00"); //decimal formatting
    TextView cBalanceTV, sBalanceTV;
    public double cBalanceD, sBalanceD, cNewBalance, sNewBalance;
    public double TransferEntered;
    int transferChoice; //spinner index

    ImageView imgdepo;
    private DataBaseAdapter db;
    EditText Transferpin;
    EditText TransferEtt;
    EditText TransferEA;
    EditText TransferET;

    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_transfer_activity);

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
        imgdepo = (ImageView) findViewById(R.id.imgdepo);
        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //set current balance of checking account
        cBalanceTV = (TextView) findViewById(R.id.cBalanceTextView);
        cBalanceD = Double.parseDouble(String.valueOf(receivedBalanceC));
        cBalanceTV.setText(String.valueOf(currency.format(cBalanceD)));

        //set current balance of savings account
        sBalanceTV = (TextView) findViewById(R.id.sBalanceTextView);
        sBalanceD = Double.parseDouble(String.valueOf(receivedBalanceS));
        sBalanceTV.setText(String.valueOf(currency.format(sBalanceD)));


        //declare transfer input amount, Spinner, and Button
        TransferEtt = (EditText) findViewById(R.id.etbanknumber);
        TransferEA = (EditText) findViewById(R.id.etAccountnumber);
        TransferET = (EditText) findViewById(R.id.etAmount);
        Transferpin = (EditText) findViewById(R.id.etmarikitipin);
        final Spinner TransferS = (Spinner) findViewById(R.id.TransferSpinner);
        Button transferB = (Button) findViewById(R.id.sendButton);

        //register transfer button with Event Listener class, and Event handler method
        transferB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if transfer amount was entered
                if (!TextUtils.isEmpty(TransferEtt.getText()) || !TextUtils.isEmpty(TransferEA.getText()) ||
                        !TextUtils.isEmpty(TransferET.getText()) || !TextUtils.isEmpty(Transferpin.getText()))
                {
                   /* if (!db.checkpin(Transferpin.getText().toString().trim()))
                    {
                        new sentbtn().execute();
                    }
                    else

                        {

                        Toast.makeText(TransferActivity.this, "Wrong User Pin Entered!", Toast.LENGTH_LONG).show();
                    }*/

                    TransferEntered = Double.parseDouble(String.valueOf(TransferET.getText()));
                    //get index of spinner string array
                    transferChoice = TransferS.getSelectedItemPosition();
                    //choose between two available transfer options
                    switch (transferChoice) {
                        //transfer funds from checking to savings
                        case 1:

                            //check if transfer amount is valid
                            if (cBalanceD >= TransferEntered) {

                                //withdraw from checking
                                Withdraw wd = new Withdraw();
                                wd.setBalance(cBalanceD);
                                wd.setWithdraw(TransferEntered);
                                //set new checking balance
                                cNewBalance = wd.getNewBalance();

                                cBalanceTV.setText(String.valueOf(currency.format(cNewBalance)));
                                cBalanceD = cNewBalance;

                                //deposit to savings
                                Deposit dp = new Deposit();
                                dp.setBalance(sBalanceD);
                                dp.setDeposit(TransferEntered);

                                //set new savings balance
                                sNewBalance = dp.getNewBalance();

                                sBalanceTV.setText(String.valueOf(currency.format(sNewBalance)));
                                sBalanceD = sNewBalance;

                                //reset transfer amount
                                TransferEntered = 0;
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
                            if (sBalanceD >= TransferEntered) {

                                //withdraw from savings
                                Withdraw wd = new Withdraw();
                                wd.setBalance(sBalanceD);
                                wd.setWithdraw(TransferEntered);
                                //set new savings balance
                                sNewBalance = wd.getNewBalance();

                                sBalanceTV.setText(String.valueOf(currency.format(sNewBalance)));
                                sBalanceD = sNewBalance;

                                //deposit to checking
                                Deposit dp = new Deposit();
                                dp.setBalance(cBalanceD);
                                dp.setDeposit(TransferEntered);

                                //set new checking balance
                                cNewBalance = dp.getNewBalance();

                                cBalanceTV.setText(String.valueOf(currency.format(cNewBalance)));
                                cBalanceD = cNewBalance;

                                //reset transfer amount
                                TransferEntered = 0;
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
    //function to open and edit shared preferences file
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
    //function to display insufficient funds message
    public void noFundsMsg() {
        Toast.makeText(BankTransferActivity.this, "Insufficient funds! Please enter a valid transfer amount and try again!", Toast.LENGTH_LONG).show();
    }//end noFundsMsg

    //function to prompt user for input
    public void noAmountMsg() {
        Toast.makeText(BankTransferActivity.this, "Nothing entered! Please enter transfer amount and try again!", Toast.LENGTH_LONG).show();
    }//noAmountMsg

    public void pinMsg() {
        final EditText Transferpin = (EditText) findViewById(R.id.etmarikitipin);
    }//noAmountMsg

    //end TransferActivity
    private class sentbtn extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mprogress = new ProgressDialog(BankTransferActivity.this);

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
                   // Toast.makeText(getApplicationContext(), "Successfully Sent", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BankTransferActivity.this, PersonalDashboardActivity.class);
                    startActivity(intent);

                }
            });
        }
    }
    //function to open and edit shared preferences file

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}//end TransferActivity
