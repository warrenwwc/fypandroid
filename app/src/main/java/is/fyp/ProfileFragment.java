package is.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:31
 * Mail: specialcyci@gmail.com
 */
public class ProfileFragment extends Fragment {
    TextView name;
    TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);
        name = (TextView) rootView.findViewById(R.id.user_name);
        email = (TextView) rootView.findViewById(R.id.user_email);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data" , MODE_PRIVATE);
        Log.d("name pref", sharedPreferences.getString("name", ""));
        name.setText(sharedPreferences.getString("name", ""));
        email.setText(sharedPreferences.getString("email", ""));
        return rootView;
    }
}
