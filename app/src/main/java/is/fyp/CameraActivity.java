package is.fyp;


import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import net.sourceforge.zbar.Symbol;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by Jason on 10/4/2017.
 */

public class CameraActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_zbar_scanner);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.preview);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        String data = rawResult.getContents();
        String result[];

        //if (rawResult.getBarcodeFormat().equals(Symbol.QRCODE)) {
            if (data.contains(",")) {
                result = data.split(",");
                if (result.length == 2) {
                    String publicKey = result[0];
                    String amount = result[1];

                    Intent intent = new Intent();
                    intent.putExtra("publicKey", publicKey);
                    intent.putExtra("amount", amount);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        //} else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(CameraActivity.this);
                }
            }, 2000);
        //}
    }

}
