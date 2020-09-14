package bsl.co.ke.fundsmanagementapi.qrcode.QRCodeGeneratorScanner.scanner;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import bsl.co.ke.fundsmanagementapi.qrcode.QRCodeGeneratorScanner.qrdb.DBManager;
import bsl.co.ke.fundsmanagementapi.R;
import butterknife.ButterKnife;

import static bsl.co.ke.fundsmanagementapi.qrcode.QRCodeGeneratorScanner.generator.QrGenerator.cropImageFromPath;
import static bsl.co.ke.fundsmanagementapi.qrcode.QRCodeGeneratorScanner.generator.QrGenerator.logger;
import static bsl.co.ke.fundsmanagementapi.qrcode.QRCodeGeneratorScanner.qrdb.DBManager.getLastDataByColumn;
import static bsl.co.ke.fundsmanagementapi.qrcode.QRCodeGeneratorScanner.qrdb.DatabaseHelper.CONTENT;
import static bsl.co.ke.fundsmanagementapi.qrcode.QRCodeGeneratorScanner.qrdb.DatabaseHelper.PATH;
import static bsl.co.ke.fundsmanagementapi.qrcode.QRCodeGeneratorScanner.qrdb.DatabaseHelper.TABLE_NAME;

public class QrScannerFragment extends AppCompatActivity {
    public static final String BARCODE_IMAGE_PATH = "barcode_image_path";
    private WebView myWebView;
    private TextView scanResult;
    private ImageView scanResultImg;
    private SharedPreferences sharedPreferences;
    private DBManager dbManager;
    private boolean sndSetting;
    private boolean vibratorSetting;
    private BeepManager beepManager;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qr_scanner);
        ButterKnife.bind(this);
        // SQLite
        dbManager = new DBManager(getApplicationContext());
        dbManager.open();

        myWebView = (WebView) findViewById(R.id.webview);
        scanResult =  (TextView) findViewById(R.id.scan_result_text);
        scanResultImg =  (ImageView) findViewById(R.id.scan_result_image);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getApplicationContext()));

        // Preferences


        boolean autoScan = sharedPreferences.getBoolean(getString(R.string.auto_scan), false);
        sndSetting = sharedPreferences.getBoolean(getString(R.string.enable_sound), false);
        vibratorSetting = sharedPreferences.getBoolean(getString(R.string.enable_vibrate), false);

      /*  FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(v -> startScan());
*/
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

      //  beepManager = new BeepManager(getApplication());

        // Start scanner on launch
        if (autoScan) {
            try {
                startScan();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
            }
        }

        dataQrPlaceholder();

    }

    private void dataQrPlaceholder() {
        String contentFromSQL = getLastDataByColumn(TABLE_NAME, CONTENT, getApplicationContext());
        String pathFromSQL = getLastDataByColumn(TABLE_NAME, PATH, getApplicationContext());
        scanResult.setText(contentFromSQL);
        scanResultImg.setImageBitmap(cropImageFromPath(pathFromSQL));
    }

    private void startScan() {
        IntentIntegrator integrator = new  IntentIntegrator (this);
    //    integrator.setCaptureActivity(AnyOrientationActivity.class);
        integrator.setPrompt(getString(R.string.scan_message));
        // Set the camera from preferences
        String cameraSource = sharedPreferences.getString(getString(R.string.camera_list), "0");
        if (cameraSource.equals("1")) {
            integrator.setCameraId(1);
        } else {
            integrator.setCameraId(0); // Use a specific camera of the device
        }
        logger(Objects.requireNonNull(getApplicationContext()), cameraSource);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
        if (sndSetting) {
            integrator.setBeepEnabled(true);
        } else {
            integrator.setBeepEnabled(false);
        }
    }


//  private void startScan() {
//    IntentIntegrator.forSupportFragment(this)
//            .setCaptureActivity(AnyOrientationActivity.class)
//            .setPrompt(getString(R.string.scan_message))
//            .setCameraId(0)  // Use a specific camera of the device
//            .setBeepEnabled(false)
//            .setBarcodeImageEnabled(true)
//            .setOrientationLocked(false)
//            .initiateScan();
//  }


    // URL Validator
    private boolean isValid(String urlString) {
        try {
            URL url = new URL(urlString);
            return URLUtil.isValidUrl(String.valueOf(url)) && Patterns.WEB_URL.matcher(String.valueOf(url)).matches();
        } catch (MalformedURLException ignored) {
        }
        return false;
    }

    // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        result.getBarcodeImagePath();
        result.getErrorCorrectionLevel();
        if (result.getContents() == null) {
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            // Put image path to sharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(BARCODE_IMAGE_PATH, result.getBarcodeImagePath());
            editor.apply();

            // Beep Manager
            if (vibratorSetting) {
                beepManager.setVibrateEnabled(true);
            }

            File file = new File(result.getBarcodeImagePath());
            Date lastModDate = new Date(file.lastModified());

            // SQLite
            final String path = result.getBarcodeImagePath();
            final String contents = result.getContents();
            final String date_created = lastModDate.toString();
            dbManager.open();
            dbManager.insert(path, contents, date_created);
            dbManager.close();
            dataQrPlaceholder();
        }
        try {
            Log.d("Scan Result", result.getBarcodeImagePath());
            Log.d("Scan Result", result.getContents());
            Log.d("Scan Result", result.getErrorCorrectionLevel());
            Log.d("Scan Result", result.getFormatName());
            Log.d("Scan Result", Arrays.toString(result.getRawBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isValid(result.getContents())) {
            myWebView.setVisibility(View.VISIBLE);
            myWebView.loadUrl(result.getContents());
        } else {
            try {
                myWebView.setVisibility(View.INVISIBLE);
                goToHistory();
            } catch (Exception e) {
                e.printStackTrace();
            }
            scanResult.setText(result.getContents());
        }
    }


    private void goToHistory() {
  //      Intent goToHistory = new
          //      Intent(getActivity(), HistoryActivity.class);
       // startActivity(goToHistory);
    }

    @Override
    public void onPause() {
        super.onPause();
    //    beepManager.setVibrateEnabled(false);
       // beepManager.setBeepEnabled(false);
    }
}
