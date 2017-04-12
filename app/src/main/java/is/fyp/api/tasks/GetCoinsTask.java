package is.fyp.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.requests.TransactionRequest;
import is.fyp.api.responses.BaseResponse;

/**
 * Created by Jason on 12/4/2017.
 */

public class GetCoinsTask extends AsyncTask<Object, Void, ArrayList<String>> {

    @Override
    protected ArrayList<String> doInBackground(Object ...params) {
        ArrayList<String> result = new ArrayList<>();
        Helper helper = Helper.getInstance();
        Gson gson = new Gson();
        String publicKey = (String) params[0];
        Integer amount = (Integer) params[1];
        String json;
        String response;
        int offset = 0;
        int size = 1000;
        List<Coin> coinList;
        ArrayList<String> endorsed = new ArrayList<String>();
        Type coinListType = new TypeToken<ArrayList<Coin>>(){}.getType();
        TransactionRequest request = new TransactionRequest();
        request.setFaddr(publicKey);
        request.setLimit(size);

        do {
            Log.d("trying offset", String.valueOf(offset));

            try {

                helper.sign(request);
                json = gson.toJson(request);

                Log.d("signedJson", json);

                response = helper.request(json, "record", "post");

                try {
                    BaseResponse baseResponse = helper.transform(response);
                    if(baseResponse.hasError()) {
                        Log.d("error", baseResponse.error);
                    }
                    return null;
                } catch (JsonParseException ex) {

                }

                coinList = gson.fromJson(response, coinListType);

                if (coinList.isEmpty()) {
                    break;
                }

                for (Coin coin : coinList) {
                    if(coin.getType().equals("TX") || coin.getType().equals("RR")) {
                        endorsed.add(coin.getSn());
                        Log.d("used coin", coin.getHsign());
                    }
                }

                for (Coin coin : coinList) {
                    if (!coin.getType().equals("MT") && !coin.getType().equals("ED")) {
                        continue;
                    }

                    if(endorsed.contains(coin.getHsign())) {
                        endorsed.remove(coin.getHsign());
                        continue;
                    }

                    result.add(coin.getHsign());

                    if (amount == result.size()) {
                        break;
                    }
                }

            } catch (IOException e) {

            }

            offset += size;
            request.setOffset(offset);
            Log.d("changed offset", String.valueOf(offset));

        } while (amount != result.size());


        return result;
    }

}
