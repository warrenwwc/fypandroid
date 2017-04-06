package is.fyp.uiFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import is.fyp.MenuActivity;
import is.fyp.R;
import is.fyp.ResideMenu;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jason on 1/1/2017.
 */

public class PayFragment extends Fragment {

    private View parentView;
    private MenuActivity parentActivity;
    private ResideMenu resideMenu;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQ);
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

    // TODO:  make a  camera activity for furture usage
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Activity activity = getActivity();
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(activity, "Image saved successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, "Image saved successfully in: " + data.getData(), Toast.LENGTH_LONG).show();
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