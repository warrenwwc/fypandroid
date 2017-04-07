package is.fyp.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.responses.BaseResponse;

/**
 * Created by Jason on 5/4/2017.
 */

public class EndorseTask extends AsyncTask<Void, Void, BaseResponse> {

    private List<Coin> coinList;

    public EndorseTask(List<Coin> conList) {
        this.coinList = conList;
    }

    @Override
    protected BaseResponse doInBackground(Void ...params) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        Helper helper = Helper.getInstance();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json;
        BaseResponse response;

        for (Coin coin : this.coinList) {
            helper.sign(coin);
        }

        Type coinListType = new TypeToken<ArrayList<Coin>>(){}.getType();
        json = gson.toJson(coinList, coinListType);

        Log.d("SignedJson", json);

        try {
            response = helper.transform(helper.request(json));
        } catch (IOException e) {
            return new BaseResponse();
        }

        return response;
    }

}
