package is.fyp;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static org.apache.http.protocol.HTTP.USER_AGENT;
import static org.ow2.util.base64.Base64.encode;

public class RegisterActivity extends AppCompatActivity {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void register(View view) throws Exception {
        execute();
    }

    public void execute() throws Exception {
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        GenRSA();
        String publicKeyString = String.valueOf(encode(publicKey.getEncoded()));
        String privateKeyKeyString = String.valueOf(encode(privateKey.getEncoded()));
        Map<String, String> reqJSON = new HashMap<String, String>();

        reqJSON.put("faddr", publicKeyString);
        reqJSON.put("taddr", privateKeyKeyString);
        reqJSON.put("type", "RU");
        reqJSON.put("device_id", android_id);
        String json = new GsonBuilder().create().toJson(reqJSON, Map.class);
        reqJSON.put("sign", signature(json));
        Log.d("public", String.valueOf(publicKeyString));
        Log.d("private", String.valueOf(privateKeyKeyString));
        Log.d("Device ID", Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID));
        Log.d("sign", signature(json));
        json = new GsonBuilder().create().toJson(reqJSON, Map.class);
        Log.d("json", json);
        //sendPostRequest("http://192.168.102.209/user_reg", json);
        makeRequest("http://192.168.102.209/user_reg", json);
    }

    public static HttpResponse makeRequest(String uri, String json) {
        try {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);
            return client.execute(httpPost);
            //return new DefaultHttpClient().execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendPostRequest(String url, String json) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        //String postJsonData = "{\"id\":5,\"countryName\":\"USA\",\"population\":8000}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(json);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        Log.d("http debug", "\nSending 'POST' request to URL : " + url);
        Log.d("http debug", "Post Data : " + json);
        Log.d("http debug", "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        //printing result from response
        Log.d("http debug", response.toString());
    }

    public void GenRSA() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyPairGenerator kpg;

        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    }

    public String signature(String base) throws NoSuchProviderException, SignatureException {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA", "BC");
            signature.initSign(privateKey, new SecureRandom());
            byte[] message = base.getBytes();
            signature.update(message);
            byte[] sigBytes = signature.sign();

            Signature signature1 = Signature.getInstance("SHA256withRSA", "BC");
            signature1.initVerify(publicKey);
            signature1.update(message);

            boolean result = signature1.verify(sigBytes);
            Log.d("result", String.valueOf(result));
            if (result) {
                Formatter formatter = new Formatter();
                for (byte b : sigBytes) {
                    formatter.format("%02x", b);
                }
                String hex = formatter.toString();
                return hex;
            }

        } catch (NoSuchAlgorithmException | NoSuchProviderException | SignatureException | InvalidKeyException ex) {

        }
        return "";
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
