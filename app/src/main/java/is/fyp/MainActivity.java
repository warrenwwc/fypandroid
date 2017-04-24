package is.fyp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.requests.TransactionRequest;
import is.fyp.api.responses.BaseResponse;
import is.fyp.api.tasks.EndorseTask;
import is.fyp.api.tasks.RegisterTask;
import is.fyp.api.tasks.TransactionTask;

import static org.ow2.util.base64.Base64.encode;

public class MainActivity extends AppCompatActivity{
    TextView sign_up;
    Button sign_in;
    SharedPreferences sharedPreferences;
    EditText id;
    EditText password;
    public ArrayList<HashMap<String, String>> myListData = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("data" , MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);

        /*sharedPreferences.edit()
                .putString("privateKey", "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCyoE9tI2/XPbpBTgBXmeXmqedIRZzm/L3V2rATNVdsvwP2LPIPxbn8cnLhms9RGtYuGKm505h7z5FrxQa8J/60eYdCgRnHZjor4epkuQxAAdVllkqLD1e5WAIoFJ+Htj6MQFlY2FDdLQ277FZTZvcARAryWZNNFVjJOhGlAeoMhaNGe8YCIPQojfuy29e+F6IyKaRgScWCYNGeqttLpkyiDVozXOEMxSBAxl60a3Nibkc1oLAB1ENA9DB6nvWvbHcssrCZgB04dxWCLVHJ0PLeczCScFR7HfS8YTf56ci5N9JszltiMb5X8Xp/rKw8LU4qtlrPbwPynvNzfYkFasHtAgMBAAECggEATjzbLSFlXjBiQTJKAhSdVu+6JrSe6alv+JhFoNQvS6lKseuzxmbDdKgoW1p0TcDkaOJSjNr6IdijiEGPFqCp9iB4tw9W3DiRaCEWijFnBS4IautuEQSSVtMOdsG5sjaHpOSWfcyZdpbCOjplATjLHSmwZcAIPa4I6nJtwglZm0u9K88OKoSq145ICSTaQrvA2hpIIkSxea1c2WLCMgQ/P0+CFHuV12bWxT6L6aLy76aWo8Cv/MwPhS105zMNshRlb2xim1QU0gRNGElIr+dQB1BQYfSwB7hvvKVib3EjxrMns2jSjWKw9y1e9VxCUl6jb6y2gZJkDgBV+rb8s+uG3QKBgQDrad1dxg0vyYwPcw8ACM14LAMdbpCdRmekiNnGqElQOVDWleYNtHv4ibpY68K/NbcTjXH0sQPS/kKJhxuZkCTItmpYrl1uW7/SsP6+SQkWJSJQu9cqWKfnDbHKVV4eFveVMQmanj++JeUt7IkwXWjzQSE0QTcxcfdHNQQgY8aZ8wKBgQDCPyn/KbmOIMP63M7WMhw2Ob3gqpnvu8YtMcm8VMDZz17VLphB5XTcuqo7YZdjZW5vSmdUYFKdwCy7pQ8ldfA5VusjT9yTLcqGB3bb+v9H+pFvy5DTew90AqHYUPCzOTrEyWVBj86FRpDpwycgGF7m9nWWTKSFnUU0urcqDW9MnwKBgE7jmSU1m3GqLt92hU5TpNdimGCijp/F2jGPEru55WeW7XobnY52Lx0oumPWDdsZ2xIYUlnXgBqSziVcO2mInmsMhjLVq8WxxWSLn7KQsxTSuN8pM3+jWhDZy/ysAvA+bhV262r/IQlNlbGAFLE6fFMYPlJOkHpnYKBiKdHY3dL1AoGAYlcOEPNz0IPAFFITmRdXdxB2k7ZrJpae9yR8qI8T73Fv5menh7z+E/2gz11SAm1Ioqk9dNcrHFg9jWY8K8/wpWbetkrpNc35+S7wOuBwu2Ucmkep85catLXccCJSKbowiY4YaD/A43JdTSuStEoCSdW+pC/7HvYHInHg5zZUAasCgYAw+GKTfF3UmZMSGz8g4I1udD9WzgO1QLZ33Fq+MbwnFlr+lb1sw8/syMvZjrejr1N43SVNkeQ4AE0Me+l84x90sICkw8vmGUIw+aDw0EPWgHzroi7vaXTrMujoabAzSwKG8MhwTC/svsc6t1Racbb2dM0jvpASXT29QdUFBhmbmg==")
                .putString("publicKey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsqBPbSNv1z26QU4AV5nl5qnnSEWc5vy91dqwEzVXbL8D9izyD8W5/HJy4ZrPURrWLhipudOYe8+Ra8UGvCf+tHmHQoEZx2Y6K+HqZLkMQAHVZZZKiw9XuVgCKBSfh7Y+jEBZWNhQ3S0Nu+xWU2b3AEQK8lmTTRVYyToRpQHqDIWjRnvGAiD0KI37stvXvheiMimkYEnFgmDRnqrbS6ZMog1aM1zhDMUgQMZetGtzYm5HNaCwAdRDQPQwep71r2x3LLKwmYAdOHcVgi1RydDy3nMwknBUex30vGE3+enIuTfSbM5bYjG+V/F6f6ysPC1OKrZaz28D8p7zc32JBWrB7QIDAQAB")
                .apply();
*/

        final Helper helper = Helper.getInstance();
        if (sharedPreferences.contains("privateKey") && !sharedPreferences.getString("privateKey", "").isEmpty()) {
            Log.d("publicKey", sharedPreferences.getString("publicKey", ""));
            Log.d("privateKey", sharedPreferences.getString("privateKey", ""));
            helper.setPrivateKey(sharedPreferences.getString("privateKey", ""));
        }

        if (isLogin) {
            Intent i = new Intent(MainActivity.this, MenuActivity.class);
            MainActivity.this.finish();
            startActivity(i);
        }
        else {
            setContentView(R.layout.login);
            sign_up = (TextView) findViewById(R.id.sign_up);
            sign_in = (Button) findViewById(R.id.sign_in);
        }



    }

    private void openFailAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        alertDialogBuilder.setTitle("Login Failed");
        alertDialogBuilder.setMessage("Invalid ID or password");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }

    public void onClick(View v) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        switch (v.getId()) {
            case  R.id.sign_up: {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
            }

            case R.id.sign_in: {
                SharedPreferences sharedPreferences = getSharedPreferences("data" , MODE_PRIVATE);
                id = (EditText) findViewById(R.id.signin_id);
                password = (EditText) findViewById(R.id.signin_pw);

                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getText().toString().trim().getBytes("UTF-8")); // or UTF-16 if needed
                String passwordHash = String.valueOf(encode(md.digest()));

                if ((id.getText().toString().trim().contentEquals(sharedPreferences.getString("id", "")) && (passwordHash.contentEquals(sharedPreferences.getString("password", "")))) || (true)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin", true);
                    editor.apply();
                    Intent i = new Intent(MainActivity.this, MenuActivity.class);
                    MainActivity.this.finish();
                    startActivity(i);
                }
                else {
                    openFailAlert();
                }
                break;
            }
        }
    }
}
