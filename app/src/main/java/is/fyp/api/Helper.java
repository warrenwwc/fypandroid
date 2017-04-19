package is.fyp.api;

import android.content.SharedPreferences;

import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import is.fyp.api.contracts.Signable;
import is.fyp.api.requests.BaseRequest;
import is.fyp.api.responses.BaseResponse;
import is.fyp.api.responses.QueueResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jason on 4/4/2017.
 */

public class Helper {

    private volatile static Helper instance;

    private OkHttpClient client;

    private Gson gson;

    protected String endpoint;

    protected String publicKey;

    protected String privateKey;

    private Helper() {
        this.client = new OkHttpClient();
        this.gson = new GsonBuilder()
                .disableHtmlEscaping()
                .registerTypeAdapter(
                        new TypeToken<TreeMap<String, Object>>(){}.getType(),
                        new JsonDeserializer<TreeMap<String, Object>>() {
                            @Override
                            public TreeMap<String, Object> deserialize(
                                    JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
                                TreeMap<String, Object> treeMap = new TreeMap<>();
                                JsonObject jsonObject = json.getAsJsonObject();
                                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                for (Map.Entry<String, JsonElement> entry : entrySet) {
                                    treeMap.put(entry.getKey(), entry.getValue());
                                }
                                return treeMap;
                            }
                        }
                ).create();

        this.endpoint = "https://mint1.coms.hk/";
        this.publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxf17kB0kRznB/NH9/bokZ635UrIsO7q8NekNLUqxJDxCrCoesvqSz0Wln9tCPtfLGpwK9AXXlOtuSjxlx+yxsbIm0eNoV6TBvBnVAHHB2kehJmq/s1LYjVCbw9zQcfJBDw1K+BXirG6ExHwixxV6I8nM/JStDjqM8jUVeX3HkFqmXMKrwqloeKQ/USRHC4l11uZ8WEUQTyFloKpafGv1c2PRbLDt5UGpIxos/9hfHmpvbDA/13/IVTf0oeYLURP5+tYIVdx2tHnyKypNnZgdqYfHIrMv2bRECAsZquBOEyTZKombtIjMunafoxXn7tPAIUrG02uOnJB9UDCIxA3eZQIDAQAB";

        this.privateKey = "";
    }

    public static Helper getInstance() {
        if(instance == null) {
            synchronized (Helper.class) {
                if(instance == null) {
                    instance = new Helper();
                }
            }
        }
        return instance;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void sign(Signable request) {
        try {
            byte[] keyBytes = Base64.decode(this.getPrivateKey(), Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = fact.generatePrivate(keySpec);
            this.sign(request, privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException | SignatureException e) {
            e.printStackTrace();
        }
    }

    public void sign(Signable request, PrivateKey privateKey) throws NoSuchProviderException, SignatureException {
        request.setSign(null);

        String json = this.gson.toJson(request);
        // sometimes it wont sort ...
        Type typeToken = new TypeToken<TreeMap<String, Object>>(){}.getType();
        TreeMap<String, Object> map = this.gson.fromJson(json, typeToken);
        json = this.gson.toJson(map);
        byte[] data = json.getBytes();

        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            byte[] sign = signature.sign();

            Formatter formatter = new Formatter();
            for (byte b : sign) {
                formatter.format("%02x", b);
            }

            request.setSign(formatter.toString());
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {

        }
    }

    public String getEndpoint() {
        ArrayList<String> mints = new ArrayList<>(Arrays.asList("https://mint1.coms.hk/", "https://mint2.coms.hk/", "https://mint3.coms.hk/"));
        String[] mintPublicKey = {
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxf17kB0kRznB/NH9/bokZ635UrIsO7q8NekNLUqxJDxCrCoesvqSz0Wln9tCPtfLGpwK9AXXlOtuSjxlx+yxsbIm0eNoV6TBvBnVAHHB2kehJmq/s1LYjVCbw9zQcfJBDw1K+BXirG6ExHwixxV6I8nM/JStDjqM8jUVeX3HkFqmXMKrwqloeKQ/USRHC4l11uZ8WEUQTyFloKpafGv1c2PRbLDt5UGpIxos/9hfHmpvbDA/13/IVTf0oeYLURP5+tYIVdx2tHnyKypNnZgdqYfHIrMv2bRECAsZquBOEyTZKombtIjMunafoxXn7tPAIUrG02uOnJB9UDCIxA3eZQIDAQAB",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxDOi4PlBmE7AL9lMmNQB1vyi21OS50Savqe8RsijBdA55tglfwA7U5HtdBQn50UXDBTLAIRklP0WFxjPHNBjTUTiL4oN3SHH2E/DI+0WfBjS9ODrBhbKgJdRJ9eKb2rJSxZdGcKq2X6fEPWCyLsqaYQSjTosdZUAEM+aRQrWRfG4KtRRgDQ4HBh2pDjAVdjDTLcOEX2BjKpSAg9j/L3uBcnEZkxHBf6tYO+5smeT5posR4lV5hprM5W4J3bBOgcvZ6accr25sdGIgnnbHKwomCH/voy7DHGk0bmoruSM3jju4wtJKsNysVsA/PwsF7siKm3LLRk4OK9Ez36uo21ajQIDAQAB",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0HAJVXE4/1Y9FB0nxf5FGTUhNg3HCX6Sds9NvfMv5xIIDYM+xDJ9lzhxYohWij5zZmSI3ch3giPhz/T/l0cX08/UBc81yobikBhYOtXj7Hfj/Rm4gylSF9dRK2ngGSOKLyRKHOYC/+xtJUB/qki0FgG5sRdSmdV2T9tB08WZq4dfjkxtUfmJfPvTR1Wt2Bq1gZTe4rofcLyhtQFwyRQ/wo+4i89j1HB4FaLw3VvBOFIm6AJYrizv9lgv4hZ5JDdLjIZUU7S4Y+GpkH2hjyN5Q3Tbjl3xUF9OTTon+b9rSeUPg4GDniGk5H4DOK1dTxYwsSB85ifUo6DkQqnUIiic/QIDAQAB"
        };
        ArrayList<Integer> mintStatus = new ArrayList<>();

        Request.Builder requestBuilder = new Request.Builder().addHeader("Content-Type", "application/json").get();

        int i = 0;
        for (String mint : mints) {
            try {
                Response response = client.newCall(requestBuilder.url(mint + "queue").build()).execute();

                String responseBody = response.body().string();

                QueueResponse mintQueue = gson.fromJson(responseBody, QueueResponse.class);

                mintStatus.add(i, mintQueue.queue);

                if (mintQueue.queue == 0) {
                    return mint;
                }

            } catch (IOException e) {
                continue;
            }
            i++;
        }

        if (mintStatus.isEmpty()) {
            return null;
        }

        Integer bestMint = mintStatus.indexOf(Collections.min(mintStatus));

        if (!mints.get(bestMint).isEmpty()) {
            this.setPublicKey(mintPublicKey[bestMint]);
            return mints.get(bestMint);
        }

        this.setPublicKey(mintPublicKey[0]);
        return mints.get(0);
    }

    public String request(String json) throws IOException {
        return this.request(json, "api", "post");
    }

    public String request(String json, String service, String method) throws IOException {

        String mintEndpoint = this.getEndpoint();

        if (mintEndpoint == null) {
            BaseResponse response = new BaseResponse();
            response.setError("No mint server is available.");
            return this.gson.toJson(response);
        }

        Log.d("Current mint:", mintEndpoint);

        Request.Builder requestBuilder = new Request.Builder()
                .url(mintEndpoint + service)
                .addHeader("Content-Type", "application/json");

        Request request;

        if (method.equals("post")) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            request = requestBuilder.post(body).build();
        } else {
            request = requestBuilder.get().build();
        }

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public BaseResponse transform(String json) {
        return this.gson.fromJson(json, BaseResponse.class);
    }

}
