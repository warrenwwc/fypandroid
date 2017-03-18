package is.fyp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Jason on 1/1/2017.
 */

public class TransferFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.transfer, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        Toolbar toolbar = (Toolbar) parentView.findViewById(R.id.toolbar);
        toolbar.setTitle("Transfer");

        //((TextView)parentView.findViewById(R.id.amount)).setText(Html.fromHtml("$ 50<sup><small>20</small></sup>"));

        final TextView mEditText = (TextView) parentView.findViewById(R.id.amount);

        Button send = (Button) parentView.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //parentActivity.changeFragment(new PayFragment());
                Intent i = new Intent(getActivity(), TransferActivity.class);
                i.putExtra("amount", mEditText.getText().toString());
                startActivity(i);
            }
        });

        Button num1 = (Button) parentView.findViewById(R.id.num1);
        num1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            mEditText.append("1");
            }
        });

        Button num2 = (Button) parentView.findViewById(R.id.num2);
        num2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditText.append("2");
            }
        });

        Button num3 = (Button) parentView.findViewById(R.id.num3);
        num3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditText.append("3");
            }
        });

        Button num4 = (Button) parentView.findViewById(R.id.num4);
        num4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditText.append("4");
            }
        });

        Button num5 = (Button) parentView.findViewById(R.id.num5);
        num5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditText.append("5");
            }
        });

        Button num6 = (Button) parentView.findViewById(R.id.num6);
        num6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditText.append("6");
            }
        });

        Button num7 = (Button) parentView.findViewById(R.id.num7);
        num7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditText.append("7");
            }
        });

        Button num8 = (Button) parentView.findViewById(R.id.num8);
        num8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditText.append("8");
            }
        });

        Button num9 = (Button) parentView.findViewById(R.id.num9);
        num9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditText.append("9");
            }
        });

        Button num0 = (Button) parentView.findViewById(R.id.num0);
        num0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditText.append("0");
            }
        });

        ImageButton del = (ImageButton) parentView.findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = mEditText.getText().toString();
                str = str.substring ( 0, str.length() - 1 );
                mEditText.setText(str);
            }
        });
    }

}