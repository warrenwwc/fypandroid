package is.fyp.uiFragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import is.fyp.CameraActivity;
import is.fyp.MenuActivity;
import is.fyp.NFCDisplayActivity;
import is.fyp.R;
import is.fyp.ResideMenu;
import is.fyp.Utils;
import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.requests.TransactionRequest;
import is.fyp.api.responses.BaseResponse;
import is.fyp.api.tasks.EndorseTask;
import is.fyp.api.tasks.GetCoinsTask;
import is.fyp.api.tasks.TransactionTask;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jason on 1/1/2017.
 */

public class PayFragment extends Fragment {

    private View parentView;
    private MenuActivity parentActivity;
    private ResideMenu resideMenu;
    SharedPreferences sharedPreferences;

    private ZBarScannerView mScannerView;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
    private static final int ZBAR_CAMERA_PERMISSION = 1;

    private String targetPublickey;
    private String targetAmount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("data", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(getActivity(), CameraActivity.class);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQ);
        }

        parentView = inflater.inflate(R.layout.pay, container, false);
        setUpViews();
        parentView.setVisibility(View.INVISIBLE);
        return parentView;
    }

    private void setUpViews() {
        parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        Toolbar toolbar = (Toolbar) parentView.findViewById(R.id.toolbar);
        toolbar.setTitle("Pay To");

        final Button payButton = (Button) parentView.findViewById(R.id.button6);
        payButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //final Helper helper = Helper.getInstance();
                //TransactionRequest request = new TransactionRequest();
                final String publicKey = sharedPreferences.getString("publicKey", "");
                final int amount = Integer.parseInt(targetAmount);

                new GetCoinsTask(){

                    private Dialog dialog;

                    protected void onPreExecute () {
                        this.dialog = Utils.getLoader(PayFragment.this.getContext());
                        this.dialog.show();
                    }

                    protected void onPostExecute(ArrayList<String> result) {
                        List<Coin> pay = new ArrayList<>();

                        if (amount != result.size()) {
                            Toast.makeText(PayFragment.this.getContext(), "Endorse Error: not enough coin",  Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            return;
                        }

                        for (String coin : result) {
                            Coin temp = new Coin();
                            temp.setFaddr(publicKey);
                            temp.setTaddr(targetPublickey);
                            temp.setType("TX");
                            temp.setSn(coin);
                            pay.add(temp);
                        }

                        new EndorseTask(pay) {

                            protected void onPostExecute(BaseResponse result) {
                                dialog.dismiss();
                                if (!result.hasError()) {
                                    Log.d("Endorse", result.message);
                                    Toast.makeText(PayFragment.this.getContext(), "Payment Successful",  Toast.LENGTH_LONG).show();
                                }else{
                                    Log.d("Endorse Error", result.error);
                                    Toast.makeText(PayFragment.this.getContext(), "Endorse Error: " + result.error,  Toast.LENGTH_LONG).show();
                                }

                                parentActivity.changeFragment(new HomeFragment());
                            }
                        }.execute();
                    }
                }.execute(publicKey, amount);


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(CameraActivity.class != null) {
                        Intent intent = new Intent(getActivity(), CameraActivity.class);
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQ);
                    }
                } else {
                    Toast.makeText(getContext(), "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    // TODO:  make a  camera activity for furture usage
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Activity activity = getActivity();
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == RESULT_OK) {
                TextView amountView = (TextView) parentView.findViewById(R.id.textView5);
                amountView.setText(data.getStringExtra("amount") + " Coin");

                targetPublickey = data.getStringExtra("publicKey");
                targetAmount = data.getStringExtra("amount");

                parentView.setVisibility(View.VISIBLE);
                // showPhoto(photoUri);
           /* } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show();
                MenuActivity.getcon*/
            } else {
                Toast.makeText(activity, "Cant Scan any image!", Toast.LENGTH_LONG).show();
                parentActivity.changeFragment(new HomeFragment());
            }
        }
    }

}