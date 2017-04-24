package is.fyp.uiFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is.fyp.MenuActivity;
import is.fyp.R;
import is.fyp.ResideMenu;
import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.requests.TransactionRequest;
import is.fyp.api.tasks.TransactionTask;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:33
 * Mail: specialcyci@gmail.com
 */
public class HomeFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("data", this.getActivity().MODE_PRIVATE);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        final MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        final TextView balance = (TextView) parentView.findViewById(R.id.balance);
        final Helper helper = Helper.getInstance();
        TransactionRequest request = new TransactionRequest();
        request.setFaddr(sharedPreferences.getString("publicKey", ""));
        request.setLimit(10000);
        request.setGroupby(true);
        helper.sign(request);

        new TransactionTask(request) {
            protected void onPostExecute(List<Coin> result) {
                int balanceCount = 0;
                for (Coin coin : result) {
                    if (coin.getType().equals("TX") && coin.getTaddr().equals(sharedPreferences.getString("publicKey", ""))) {
                        continue;
                    }
                    if (coin.getType().equals("MT") || coin.getType().equals("ED")) {
                        balanceCount += coin.getAmount();
                    } else if (coin.getType().equals("RR") || coin.getType().equals("TX")) {
                        balanceCount -= coin.getAmount();
                    }
                }
                balance.setText(String.valueOf(balanceCount));
            }
        }.execute();

        ImageView img = (ImageView) parentView.findViewById(R.id.imageView2);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parentActivity.changeFragment(new PayFragment());
//                Intent i = new Intent(getActivity(), NFCActivity.class);
//                startActivity(i);
            }
        });

        ImageView img2 = (ImageView) parentView.findViewById(R.id.imageView3);
        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parentActivity.changeFragment(new TransferFragment());
            }
        });



//        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
//            }
//        });

        // add gesture operation's ignored views
//        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
//        resideMenu.addIgnoredView(ignored_view);
    }

}
