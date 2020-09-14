package bsl.co.ke.fundsmanagementapi.ui.activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bsl.co.ke.fundsmanagementapi.PERSONALAPI;
import bsl.co.ke.fundsmanagementapi.R;
import bsl.co.ke.fundsmanagementapi.database.DataBaseAdapter;
import bsl.co.ke.fundsmanagementapi.model.User;
import bsl.co.ke.fundsmanagementapi.qrcode.barcodeScanner.BarcodeScanner;
import bsl.co.ke.fundsmanagementapi.ui.common.fragment.CenteredTextFragment;
import bsl.co.ke.fundsmanagementapi.ui.common.menu.DrawerAdapter;
import bsl.co.ke.fundsmanagementapi.ui.common.menu.DrawerItem;
import bsl.co.ke.fundsmanagementapi.ui.common.menu.SimpleItem;
import bsl.co.ke.fundsmanagementapi.ui.common.menu.SpaceItem;
import bsl.co.ke.fundsmanagementapi.ui.common.menu.slidingrootnav.SlidingRootNav;
import bsl.co.ke.fundsmanagementapi.ui.common.menu.slidingrootnav.SlidingRootNavBuilder;
import bsl.co.ke.fundsmanagementapi.ui.views.activities.personalaccount.BankTransferActivity;
import bsl.co.ke.fundsmanagementapi.ui.views.activities.personalaccount.CreditPurchaseStatementActivity;
import bsl.co.ke.fundsmanagementapi.ui.views.activities.personalaccount.DepositActivity;
import bsl.co.ke.fundsmanagementapi.ui.views.activities.personalaccount.SendFundsPersonalActivity;
import bsl.co.ke.fundsmanagementapi.ui.views.activities.personalaccount.WithdrawFundsActivity;
import bsl.co.ke.fundsmanagementapi.ui.views.adapters.MyAdapter;
import bsl.co.ke.fundsmanagementapi.ui.views.adapters.SliderAdapterExample;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class PersonalDashboardActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final int POS_DASHBOARD = 0;
    private static final int POS_QR_GENERATOR = 1;
    private static final int POS_QR_SCANNER = 2;
    private static final int POS_ACCOUNT = 3;
    private static final int POS_MESSAGES = 4;
    private static final int POS_LOGOUT = 5;
    private static final int POS_CART = 6;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    //declare constants used with shared preferences
    public static final String MY_BALANCE = "My_Balance";
    public static final String CHECKING_KEY = "checking_key";
    public static final String SAVINGS_KEY = "savings_key";
    //declare variables for message, checking and savings balance
    String receivedString;
    public String chkBalance, savBalance;
    private SlidingRootNav slidingRootNav;

    //mpesa
    SliderView sliderView;
    private SliderAdapterExample adapter;

    PERSONALAPI app;
    DataBaseAdapter db;
    public User user;
    @BindView(R.id.cardViewDeposits)
    CardView cardViewDeposits;
    @BindView(R.id.cardViewSendFunds)
    CardView cardViewSendFunds;
    @BindView(R.id.cardViewWithdrawFunds)
    CardView cardViewWithdrawFunds;
    @BindView(R.id.cardViewBankFundsTransfer)
    CardView cardViewBankFundsTransfer;
    @BindView(R.id.cardViewCreditPurchase)
    CardView cardViewCreditPurchase;

    //TextView txtvwdate;
    protected List<User> userList;

    User userb = new User();
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] img = {R.drawable.one, R.drawable.two, R.drawable.three};
    private ArrayList<Integer> ImgArray = new ArrayList<Integer>();

    String selectionfunction, typeofaccount, phoneNumber, amount, pin, recipient, paymentmode;

    private static final int accvisibility = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_personal_account);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DataBaseAdapter(getApplicationContext()).open();
        init();
        initViews();
        initObjects();

      /*  txtvwdate = (TextView) findViewById(R.id.textviewbsDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = sdf.format(new Date());
        txtvwdate.setText(currentDateandTime);*/
        Bundle extras = getIntent().getExtras();
        //receive welcome msg from MainActivity
        if (extras != null) {
            receivedString = extras.getString("stringReference");
            Toast.makeText(PersonalDashboardActivity.this, receivedString, Toast.LENGTH_LONG).show();
        }//end if

        //read checking and savings balances from shared preferences file
        getPrefs();


        cardViewDeposits.setOnClickListener(v -> {
            Intent depositsintent = new Intent(getApplicationContext(), DepositActivity.class);
            depositsintent.putExtra("balance", chkBalance); //savings balance
            depositsintent.putExtra("key", CHECKING_KEY); //key used to store savings balance
            startActivity(depositsintent);

        });
        cardViewSendFunds.setOnClickListener(v -> {
            Intent sendfundsIntent = new Intent(getApplicationContext(), SendFundsPersonalActivity.class);
            sendfundsIntent.putExtra("balanceC", chkBalance); //checking balance
            sendfundsIntent.putExtra("balanceS", savBalance); //savings balance
            startActivity(sendfundsIntent);
        });
        cardViewWithdrawFunds.setOnClickListener(v -> {
            //user wants to access savings account
            Intent sendfundsIntent = new Intent(getApplicationContext(), WithdrawFundsActivity.class);
            //send only data related to savings account
            sendfundsIntent.putExtra("balance", savBalance); //savings balance
            sendfundsIntent.putExtra("key", SAVINGS_KEY); //key used to store savings balance
            //sendfundsIntent.putExtra("key", SAVINGS_KEY);
            startActivity(sendfundsIntent);
        });
        cardViewBankFundsTransfer.setOnClickListener(v -> {
            Intent transferIntent = new Intent(getApplicationContext(), BankTransferActivity.class);
            //send both balances
            transferIntent.putExtra("balanceC", chkBalance); //checking balance
            transferIntent.putExtra("balanceS", savBalance); //savings balance
            startActivity(transferIntent);
        });

        cardViewCreditPurchase.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CreditPurchaseStatementActivity.class);
            intent.putExtra("balance", chkBalance); //savings balance
            intent.putExtra("key", CHECKING_KEY); //key used to store s
            startActivity(intent);

//            Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
        });


        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_QR_GENERATOR),
                createItemFor(POS_QR_SCANNER),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_LOGOUT),
                new SpaceItem(10),
                createItemFor(POS_CART)));

        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);


    }

    private void init() {
        for (int i = 0; i < img.length; i++)
            ImgArray.add(img[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(PersonalDashboardActivity.this, ImgArray));
       /* CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);*/

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == img.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        //Auto start
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);
    }


   /* public void removeLastItem(View view) {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void addNewItem(View view) {
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("Slider Item Added Manually");
        sliderItem.setImageUrl("https://drive.google.com/file/d/1B6vKpmNCmiEl_1DYqEUOwUev8pkSD7K1/view?usp=sharing");
        adapter.addItem(sliderItem);
    }*/

    private void initViews() {


    }

    /**
     * This method is to initialize listeners
     */

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        db = new DataBaseAdapter(this);

    }

    boolean validateSpinner(Spinner spinner, String error) {

        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("-select function-")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    boolean validateSpinnerpaymentmode(Spinner spinner, String error) {

        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("-select payment mode-")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    boolean validateSpinnerRecipient(Spinner spinner, String error) {

        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("-select recipient-")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    boolean validateSpinneracc(Spinner spinner, String error) {

        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("-select top up account-")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            new FetchLogout().execute();
        }
        if (position == POS_QR_GENERATOR) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://m.apkure.com/qr-code-generator/com.ykart.tool.qrcodegen/download?from=details"));
            startActivity(intent);
        }
        if (position == POS_QR_SCANNER) {
            Intent intent = new Intent(this, BarcodeScanner.class);
            startActivity(intent);

        }
        slidingRootNav.closeMenu();
        Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        showFragment(selectedScreen);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton:
                postDataToSQLite();

                break;
        }
    }


    private void postDataToSQLite() {



          /*  user.setSelectionfunction(ssselectfunction.getSelectedItem().toString().trim());
            user.setTypeofaccount(sstypeofaccount.getSelectedItem().toString().trim());
            user.setPhoneNumber(editTextPhoneNumber.getText().toString().trim());
            user.setRecipient(ssRecipient.getSelectedItem().toString().trim());
            user.setPaymentmode(ssPaymentMode.getSelectedItem().toString().trim());
            user.setAmount(editTextAmount.getText().toString().trim());
            user.setPin(editTextpin.getText().toString().trim());

            db.updateUser(user);*/

        new submit().execute();
        // finish();
    }

    private class FetchLogout extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mprogress = new ProgressDialog(PersonalDashboardActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.show();
            mprogress.setTitle("Authenticating...");
            mprogress.setMessage("Logging Out...");
            mprogress.setCancelable(false);
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

            if (mprogress.isShowing()) {
                mprogress.dismiss();
            }
            runOnUiThread(new Runnable() {
                public void run() {


                    // Start your app main activity


                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                    finish();


                    // close this activity
//                    finish();

                }
            });
        }
    }

    private class submit extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mprogress = new ProgressDialog(PersonalDashboardActivity.this);
        boolean islogin = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.show();
            mprogress.setTitle("Authenticating...");
            mprogress.setMessage("Submitting Request...");
            mprogress.setCancelable(false);
            mprogress.setIndeterminate(false);

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
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


                    // Start your app main activity


                    Intent intent = new Intent(getApplicationContext(), PersonalDashboardActivity.class);
                    startActivity(intent);

                    finish();

                }
            });
        }
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.colorPrimary))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.black))
                .withSelectedTextTint(color(R.color.black));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }


    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_Aboutus) {
            Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_gallery) {
            return true;
        }
        if (id == R.id.action_logout) {
            new FetchLogout().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().show();
        getPrefs();
    }

    //function to open shared preferences and retrieve current balances
    public void getPrefs() {
        //open shared preferences xml file
        SharedPreferences BalancePref = getSharedPreferences(PersonalDashboardActivity.MY_BALANCE, MODE_PRIVATE);
        //retrieve checking and savings balances if they are not null
        //or set balances to default value if they are null
        chkBalance = BalancePref.getString(CHECKING_KEY, "10000.00");
        savBalance = BalancePref.getString(SAVINGS_KEY, "80000.00");
    }//end getPrefs

    @Override
    public boolean onSupportNavigateUp() {
        return true;
    }


}
