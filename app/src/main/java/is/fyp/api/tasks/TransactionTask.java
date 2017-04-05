package is.fyp.api.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.requests.TransactionRequest;

/**
 * Created by Jason on 5/4/2017.
 */

public class TransactionTask extends AsyncTask<Void, Void, List<Coin>> {

    private TransactionRequest request;

    public TransactionTask(TransactionRequest request) {
        this.request = request;
    }

    @Override
    protected List<Coin> doInBackground(Void ...param) {
        Helper helper = new Helper();
        Gson gson = new Gson();
        String json;
        String response;
        List<Coin> coinList = new ArrayList<>();

        json = gson.toJson(this.request);

        try {
            response = helper.request(json);
            Type coinListType = new TypeToken<ArrayList<Coin>>(){}.getType();

            coinList = gson.fromJson(response, coinListType);
        } catch (IOException e) {
            return coinList;
        }

        return coinList;
    }
}
