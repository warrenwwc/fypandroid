package is.fyp.api;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;

import is.fyp.api.requests.RegisterRequest;
import is.fyp.api.responses.BaseResponse;

/**
 * Created by Jason on 4/4/2017.
 */

public class RegisterTask extends AsyncTask<Void, Void, BaseResponse> {

    private RegisterRequest request;

    public RegisterTask(RegisterRequest request) {
        this.request = request;
    }

    @Override
    protected BaseResponse doInBackground(Void ...param) {
        Helper helper = new Helper();
        Gson gson = new Gson();
        String json;
        BaseResponse response;

        json = gson.toJson(this.request);
        try {
            response = helper.request(json);
        } catch (IOException e) {
            return new BaseResponse();
        }

        return response;
    }

}
