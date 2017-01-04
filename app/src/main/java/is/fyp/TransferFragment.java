package is.fyp;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        ((TextView)parentView.findViewById(R.id.textView7)).setText(Html.fromHtml("$ 50<sup><small>20</small></sup>"));


    }

}