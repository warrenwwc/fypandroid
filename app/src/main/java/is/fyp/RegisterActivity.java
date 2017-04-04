package is.fyp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import is.fyp.api.Helper;
import is.fyp.api.RegisterTask;
import is.fyp.api.requests.RegisterRequest;
import is.fyp.api.responses.BaseResponse;

import static org.ow2.util.base64.Base64.encode;

public class RegisterActivity extends AppCompatActivity {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private GoogleApiClient client;
    EditText name;
    EditText id;
    EditText email;
    EditText pass1;
    EditText pass2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        name = (EditText) findViewById(R.id.your_full_name);
        id = (EditText) findViewById(R.id.id);
        email = (EditText) findViewById(R.id.your_email_address);
        pass1 = (EditText) findViewById(R.id.create_new_password);
        pass2 = (EditText) findViewById(R.id.create_new_password2);
    }

    public void register(View view) throws Exception {
//        if (isValidName(name.getText().toString()) && isValidID(id.getText().toString()) && isValidEmail(email.getText().toString()) && isValidPassword(pass1.getText().toString(), pass2.getText().toString())) {
//            execute();
//        }
        execute();
    }

    // validating name
    private boolean isValidName(String name) {
        if ((name != null) && (name.length() > 6)) {
            return true;
        }
        Toast.makeText(this, "Invalid name", Toast.LENGTH_LONG).show();
        return false;
    }

    // validating id
    private boolean isValidID(String id) {
        if ((id != null) && (id.length() > 6)) {
            return true;
        }
        Toast.makeText(this, "Invalid ID", Toast.LENGTH_LONG).show();
        return false;
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
        {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // validating password with retype password
    private boolean isValidPassword(String pass1, String pass2) {
        if ((pass1 != null) && (pass1.length() > 6) && (pass1.trim().contentEquals(pass2.trim()))) {
            return true;
        }
        Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show();
        return false;
    }

    public void execute() throws Exception {
        GenRSA();

        Helper helper = new Helper();
        //String android_id = Settings.Secure.getString(getContentResolver(),
        //        Settings.Secure.ANDROID_ID);
        String android_id = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token: ",  android_id);

        String mintPK = helper.getPublicKey();
        final String publicKeyString = String.valueOf(encode(publicKey.getEncoded()));
        final String privateKeyKeyString = String.valueOf(encode(privateKey.getEncoded()));

        RegisterRequest request = new RegisterRequest();
        request.setFaddr(publicKeyString);
        request.setTaddr(mintPK);
        request.setDevice_id(android_id);
        helper.sign(request, privateKey);

        new RegisterTask(request) {
            protected void onPostExecute(BaseResponse result) {
                if(!result.hasError() && result.message.equals("SUCCESS_CREATE_TICKET")) {
                    SharedPreferences sharedPreferences = getSharedPreferences("data" , MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("privateKey", privateKeyKeyString);
                    editor.putString("publicKey", publicKeyString);
                    editor.putBoolean("isLogin", true);
                    editor.putString("id", id.getText().toString());
                    editor.putString("name", name.getText().toString());
                    editor.putString("email", email.getText().toString());

                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(pass1.getText().toString().getBytes("UTF-8")); // or UTF-16 if needed
                        String passwordHash = String.valueOf(encode(md.digest()));
                        editor.putString("password", passwordHash);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    editor.apply();
                    openSuccessAlert();
                    Log.d("result", result.message);
                } else {
                    openFailAlert();
                    Log.d("result", result.error);
                }

            }
        }.execute();
    }

    /*public void execute() throws Exception {
        String mintPK = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxf17kB0kRznB/NH9/bokZ635UrIsO7q8NekNLUqxJDxCrCoesvqSz0Wln9tCPtfLGpwK9AXXlOtuSjxlx+yxsbIm0eNoV6TBvBnVAHHB2kehJmq/s1LYjVCbw9zQcfJBDw1K+BXirG6ExHwixxV6I8nM/JStDjqM8jUVeX3HkFqmXMKrwqloeKQ/USRHC4l11uZ8WEUQTyFloKpafGv1c2PRbLDt5UGpIxos/9hfHmpvbDA/13/IVTf0oeYLURP5+tYIVdx2tHnyKypNnZgdqYfHIrMv2bRECAsZquBOEyTZKombtIjMunafoxXn7tPAIUrG02uOnJB9UDCIxA3eZQIDAQAB";

        Gson gson = new Gson();
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        HttpResponse response;
        BaseResponse apiResponse;
        GenRSA();
        String publicKeyString = String.valueOf(encode(publicKey.getEncoded()));
        String privateKeyKeyString = String.valueOf(encode(privateKey.getEncoded()));
        Map<String, String> reqJSON = new HashMap<String, String>();

        reqJSON.put("faddr", publicKeyString);
        reqJSON.put("taddr", mintPK);
        reqJSON.put("type", "RU");
        reqJSON.put("device_id", android_id);
        String json = new GsonBuilder().create().toJson(reqJSON, Map.class);
        String sign = sign(json);
        reqJSON.put("sign", sign);
        Log.d("sign", sign);
        Log.d("public", String.valueOf(publicKeyString));
        Log.d("private", String.valueOf(privateKeyKeyString));
        Log.d("Device ID", Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID));
        Log.d("json1", json);
        json = new GsonBuilder().create().toJson(reqJSON, Map.class);
        Log.d("json2", json);
        response = makeRequest("https://mint1.coms.hk/api", json);
        HttpEntity resEntity = response.getEntity();
        apiResponse = gson.fromJson(EntityUtils.toString(resEntity), BaseResponse.class);
        if (apiResponse.message == null) {
            apiResponse.message = "";
        }
        Log.d("Result", apiResponse.message);
        if (apiResponse.message.equals("SUCCESS_CREATE_TICKET")){
            SharedPreferences sharedPreferences = getSharedPreferences("data" , MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //editor.putString("privateKey", String.valueOf(privateKeyKeyString));
            String encryptedKey = encrypt(pass1.getText().toString(), id.getText().toString(), String.valueOf(privateKeyKeyString));
            //editor.putString("privateKey", encryptedKey);
            editor.putString("privateKey", privateKeyKeyString);
            editor.putString("publicKey", publicKeyString);
            Log.d("BeforeAES", String.valueOf(privateKeyKeyString));
            Log.d("AES", encryptedKey);
            Log.d("DecryptAES", decrypt(pass1.getText().toString(), id.getText().toString(),encryptedKey));
            editor.putBoolean("isLogin", true);
            editor.putString("id", id.getText().toString());
            editor.putString("name", name.getText().toString());
            editor.putString("email", email.getText().toString());
            //editor.putString("password", pass1.getText().toString());

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pass1.getText().toString().getBytes("UTF-8")); // or UTF-16 if needed
            String passwordHash = String.valueOf(encode(md.digest()));
            editor.putString("password", passwordHash);

            editor.apply();
            openSuccessAlert();
        }
        else {
            openFailAlert();
        }
        if( response.getEntity() != null ) {
            response.getEntity().consumeContent();
        }
    }*/

    private void openSuccessAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);

        alertDialogBuilder.setTitle("Registration Success");
        alertDialogBuilder.setMessage("Click OK to continue");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                Intent i = new Intent(RegisterActivity.this, MenuActivity.class);
                RegisterActivity.this.finish();
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }

    private void openFailAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);

        alertDialogBuilder.setTitle("Registration Failed");
        alertDialogBuilder.setMessage("Click Continue to register again");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Continue",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                try {
                    execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
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
