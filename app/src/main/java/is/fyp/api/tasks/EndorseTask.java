package is.fyp.api.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.responses.BaseResponse;

/**
 * Created by Jason on 5/4/2017.
 */

public class EndorseTask extends AsyncTask<Coin, Void, BaseResponse> {

    @Override
    protected BaseResponse doInBackground(Coin ...coins) {
        Helper helper = Helper.getInstance();
        Gson gson = new Gson();
        String json;
        BaseResponse response;

        List<Coin> coinList = new ArrayList<>();
        for (Coin coin : coins) {
            helper.sign(coin);
            coinList.add(coin);
        }

        json = gson.toJson(coinList);
        try {
            response = helper.transform(helper.request(json));
        } catch (IOException e) {
            return new BaseResponse();
        }

        return response;
    }

}
