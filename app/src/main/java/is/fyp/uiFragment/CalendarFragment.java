package is.fyp.uiFragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.fyp.R;
import is.fyp.Utils;
import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.requests.RegisterRequest;
import is.fyp.api.requests.TransactionRequest;
import is.fyp.api.tasks.RegisterTask;
import is.fyp.api.tasks.TransactionTask;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午3:26
 * Mail: specialcyci@gmail.com
 */
public class CalendarFragment extends Fragment {

    private View parentView;
    private ListView listView;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.calendar, container, false);
        listView = (ListView) parentView.findViewById(R.id.listView);

        initView();
        return parentView;
    }

    private void initView() {
        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getCalendarData());*/
        final String ID_TITLE = "TITLE", ID_SUBTITLE = "SUBTITLE";

        sharedPreferences = this.getActivity().getSharedPreferences("data", this.getActivity().MODE_PRIVATE);

        final Helper helper = Helper.getInstance();
        TransactionRequest request = new TransactionRequest();
        request.setFaddr(sharedPreferences.getString("publicKey", ""));
        request.setGroupby(true);
        request.setLimit(10000);
        //request.setType("MT");
        helper.sign(request);
        //final Dialog dialog = Utils.getLoader(CalendarFragment.this.getContext());


        new TransactionTask(request) {

            private Dialog dialog;

            protected void onPreExecute () {
                this.dialog = Utils.getLoader(CalendarFragment.this.getContext());
                this.dialog.show();
            }

            protected void onPostExecute(List<Coin> result) {
                Log.d("coin.length", String.valueOf(result.size()));
                ArrayList<HashMap<String, String>> myListData = new ArrayList<HashMap<String, String>>();
                Map<String, String> keyMap = new HashMap<String, String>();
                keyMap.put("MT", "Bought");
                keyMap.put("TX", "Paid");
                keyMap.put("ED", "Received");
                keyMap.put("RR", "Revoked");
                for (Coin coin : result) {
                    //Log.d("sn:", coin.getSn());
                    if (coin.getType().equals("TX") && coin.getTaddr().equals(sharedPreferences.getString("publicKey", ""))) {
                        continue;
                    }
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put(ID_TITLE, keyMap.get(coin.getType()) + ": " + coin.getAmount());
                    item.put(ID_SUBTITLE, coin.getTime());
                    myListData.add(item);
                }
                listView.setAdapter(
                        new SimpleAdapter(
                                CalendarFragment.this.getContext(),
                                myListData,
                                android.R.layout.simple_list_item_2,
                                new String[]{"TITLE", "SUBTITLE"},
                                new int[]{android.R.id.text1, android.R.id.text2}
                        )
                );
                this.dialog.dismiss();
            }
        }.execute();


//        String[] titles = new String[]{"$ 10", "$ 20", "$ 30"};
//        String[] subtitles = new String[]{"1 day ago", "1 day ago", "1 day ago"};
//
//        for (int i = 0; i < titles.length; ++i) {
//            HashMap<String, String> item = new HashMap<String, String>();
//            item.put(ID_TITLE, titles[i]);
//            item.put(ID_SUBTITLE, subtitles[i]);
//            myListData.add(item);
//        }

//        listView.setAdapter(
//                new SimpleAdapter(
//                        this.getContext(),
//                        myListData,
//                        android.R.layout.simple_list_item_2,
//                        new String[]{"TITLE", "SUBTITLE"},
//                        new int[]{android.R.id.text1, android.R.id.text2}
//                )
//        );
        /*listView.setAdapter(arrayAdapter);*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();*/
            }
        });
    }
}
