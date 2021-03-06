package is.fyp.uiFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.fyp.MenuActivity;
import is.fyp.R;
import is.fyp.ResideMenu;

/**
 * Created by eie3333 on 12/24/2016.
 */

public class LoginFragment extends Fragment {
    private View parentView;
    private ResideMenu resideMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.login, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
    }
}
