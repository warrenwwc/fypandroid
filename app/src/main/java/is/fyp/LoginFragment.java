package is.fyp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static android.content.Context.MODE_PRIVATE;

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
