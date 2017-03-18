package is.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import is.fyp.ResideMenu;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:33
 * Mail: specialcyci@gmail.com
 */
public class HomeFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        final MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

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
