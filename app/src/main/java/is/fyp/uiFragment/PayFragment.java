package is.fyp.uiFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import is.fyp.CameraActivity;
import is.fyp.MenuActivity;
import is.fyp.R;
import is.fyp.ResideMenu;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jason on 1/1/2017.
 */

public class PayFragment extends Fragment {

    private View parentView;
    private MenuActivity parentActivity;
    private ResideMenu resideMenu;

    private ZBarScannerView mScannerView;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
    private static final int ZBAR_CAMERA_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        /*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQ);*/
        Intent i = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ);
        //startActivity(i);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(getActivity(), CameraActivity.class);
            startActivity(intent);
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

        Button payButton = (Button) parentView.findViewById(R.id.button6);
        payButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parentActivity.changeFragment(new HomeFragment());
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
                        startActivity(intent);
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
                if (data == null) {
                    Toast.makeText(activity, "Image saved successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, "Image saved successfully in: " + data.getStringExtra("result"), Toast.LENGTH_LONG).show();
                }
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