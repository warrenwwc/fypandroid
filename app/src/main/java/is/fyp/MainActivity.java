package is.fyp;

import android.app.AlertDialog;
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

        final Helper helper = Helper.getInstance();
        if (sharedPreferences.contains("privateKey") && !sharedPreferences.getString("privateKey", "").isEmpty()) {
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

//        TransactionRequest request = new TransactionRequest();
//        final String publicKey = sharedPreferences.getString("publicKey", "");
//        final String randomMerchant = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwnqEM1PNOF++GUR2oElEmkIQh0ZbXu7Fr7Fjouv36JTWaXA4EjHFBnZKghKq7flFDJ+UVEIHJr/BL30AT3e1AimMb3xmUbm2q3BThHXZr7gfPIeXmuGNRaaY9YPC9tX7xCAwhy9ChNpxr6Hlf226+Yc4Fq7be/QxYyka7DTu5cyNijk09pCCL6jA4aMm59yhfM9j8ESjxOxgYhV+qL8Mz9okHT0IvetUZsHdDtWbkarfEpZAoyZZNXguod0XCgWZwVCK9+tpJzk4S/XSWu12UZyQ8PKYIvw5Tgoo6cJOJ/vhvikaFvuzvfoG5sTIExrqa+Utrol2t0Hq5Vu2gOTR3wIDAQAB";
//        request.setFaddr(publicKey);
//        request.setType("MT");
//        helper.sign(request);

//        new TransactionTask(request) {
//            protected void onPostExecute(List<Coin> result) {
//                List<Coin> coins = result.subList(0, 2);
//                List<Coin> pay = new ArrayList<>();
//                for (Coin coin : coins) {
//                    Coin temp = new Coin();
//                    temp.setFaddr(publicKey);
//                    temp.setTaddr(randomMerchant);
//                    temp.setType("TX");
//                    temp.setSn(coin.getSn());
//                    pay.add(temp);
//                }
//
//                new EndorseTask(pay) {
//                    protected void onPostExecute(BaseResponse result) {
//                        if (!result.hasError()) {
//                            Log.d("Endorse", result.message);
//                        }else{
//                            Log.d("Endorse Error", result.error);
//                        }
//                    }
//                }.execute();
//            }
//        }.execute();

        Log.d("fcm:", FirebaseInstanceId.getInstance().getToken());

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
