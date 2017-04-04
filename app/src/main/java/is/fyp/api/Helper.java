package is.fyp.api;

import android.content.SharedPreferences;

import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Formatter;

import is.fyp.api.requests.BaseRequest;
import is.fyp.api.responses.BaseResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jason on 4/4/2017.
 */

public class Helper {

    private OkHttpClient client;

    private Gson gson;

    protected String endpoint;

    protected String publicKey;

    protected String privateKey;

    public Helper() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
        this.endpoint = "https://mint1.coms.hk/";
        this.publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxf17kB0kRznB/NH9/bokZ635UrIsO7q8NekNLUqxJDxCrCoesvqSz0Wln9tCPtfLGpwK9AXXlOtuSjxlx+yxsbIm0eNoV6TBvBnVAHHB2kehJmq/s1LYjVCbw9zQcfJBDw1K+BXirG6ExHwixxV6I8nM/JStDjqM8jUVeX3HkFqmXMKrwqloeKQ/USRHC4l11uZ8WEUQTyFloKpafGv1c2PRbLDt5UGpIxos/9hfHmpvbDA/13/IVTf0oeYLURP5+tYIVdx2tHnyKypNnZgdqYfHIrMv2bRECAsZquBOEyTZKombtIjMunafoxXn7tPAIUrG02uOnJB9UDCIxA3eZQIDAQAB";

        this.privateKey = "";
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void sign(BaseRequest request, PrivateKey privateKey) throws NoSuchProviderException, SignatureException {
        String json = this.gson.toJson(request);
        byte[] data = json.getBytes();
        byte[] sign;
        Formatter formatter = new Formatter();
        String hex;

        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            sign = signature.sign();

            for (byte b : sign) {
                formatter.format("%02x", b);
            }
            hex = formatter.toString();

            request.setSign(hex);
            //return hex;
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {

        }

        //return null;
    }

    public BaseResponse request(String json) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(this.endpoint + "api")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        BaseResponse responseBody = this.gson.fromJson(response.body().string(), BaseResponse.class);

        return responseBody;
    }

}
