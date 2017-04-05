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
import java.util.List;

import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.requests.TransactionRequest;
import is.fyp.api.tasks.RegisterTask;
import is.fyp.api.tasks.TransactionTask;

import static org.ow2.util.base64.Base64.encode;

public class MainActivity extends AppCompatActivity{
    TextView sign_up;
    Button sign_in;
    SharedPreferences sharedPreferences;
    EditText id;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("data" , MODE_PRIVATE);
        //Log.d("fcm:", FirebaseInstanceId.getInstance().getToken()); null point wtf

        Helper helper = Helper.getInstance();
        helper.setPrivateKey(("MIIEowIBAAKCAQEAw3ioYc1r61s2O/8fZ++JdPkY9VtCGk7VUealTpvBOnnkQkKz\n" +
                "/j99tJEFDAbWVyyh8AtHJQxK/Vq1xtVZsSNEMglijhAl3YkbahuDq2j1AjBMPqcw\n" +
                "URzi7flfNCov2HNAnE8FFeYVIabTL3FKnb0u2lNSCE228dPwTAYV6b9cNg2hrFZc\n" +
                "Rqc8UY0+WBDQ0tjHAlSPMi6VrAAecpZtBeTeEqI/YsX8b1C5WEWDL7GvOSkDU5kq\n" +
                "vbKuQ/NZKSkxJs8fmM6XbtIs3mPRnJuJAgipEi6DOhDIRmH2TCvmHmGZMFljR5OI\n" +
                "P1y2b9pCANmwNE3hTqdgHGdNe4egOL3p4jlL9wIDAQABAoIBADT7q4h3Cd2Np7Gz\n" +
                "gWvGvdd6/Yzj45MF80Rz5DLefQ3ApGJhejBcPVKITHnqEIcojexm4i3kmx75S3RN\n" +
                "uDYPfRii+pfPQLQ/ybKFKrd3h5HETaZd4vbZgZlHksqs9LMS6lxz/sjkyK2KbovZ\n" +
                "hYQkF2MOWpntlr0N2YCB1ca6G+B6b8WwUkbZSz+mX756KRQy0/Kdvu5/NsnLhkQx\n" +
                "pWjYRHev1YIXIXyqbOl7fFcgwBKoqcAGaOnb5aXytm9YsAHyWxPxC+LiQLSY37ID\n" +
                "HbR57j496ou4ZMR2Z4Mkq8q4oM2vTSiXRIZVm84htBWhwzoEI67d2Qd3e3CIecHQ\n" +
                "BjguAqECgYEA8iLyKqVxFuP8LR3QwGf3qnyUOdcfhkxG4fOmyROpxUyJqxVRhaQx\n" +
                "bYnrBOFnly5O5GvWoAPqAeq9Ng/TTOKBqW09sk07Md+zo5ebqdSyOy4CFVY3dk7/\n" +
                "D1hm0S+r7iGdJkB+h4o8a7+B5HgMTvkigj21x5JftSPtP2DpPTCyMusCgYEAzqm6\n" +
                "i8xHPIeoBMOXDmxv5SmrGQ5Nh0KjKFMDGeix/7L0IXJWnDUwkRim2KeuO/aqcVPI\n" +
                "o4JRaduKdzsDPQ8tgv3o3QEa4Jw1VbHzXfGQufrTW1zNfvl5NgS2+WMWk2PRPNs1\n" +
                "U0oMmqVQNJ3CIHETiiQeCplbFJD18VNHrN0L0CUCgYEAsqENPmDm2RhABZilVAxf\n" +
                "LarSPwlw/EZxVGfHdzfGWwNn3IrRpWHIBSNl+ie/oExNbz4PC+VXUSq3g5aRL3s6\n" +
                "ZJ9ukIdhUB3UDK/f6p47DmWWq97685C7obp5v4EHuZmasYmKzrswb9zMGpxlmC07\n" +
                "RBH6dMyLRrrGX6dC6h1umRkCgYBQQbPTVJqZlCkY6IhCLrRpr3vluBs9mxBXuZ0r\n" +
                "s6Vkoq+SSZ/++90HjCZHXx8X5FwetKXncdWCIaMtWHqSfNF03HxRT3uLnL5NsFTN\n" +
                "t+E3iIQKKAkZ9XIwGzaftO5wgMQiORMFbG1mpSp3tIhOJvuqmwobnaC0ZPNOK8Rb\n" +
                "CmDrDQKBgA07Kp4WwH3U0yu9OkSPAK3eaiXtG29NPDEyOtZXl8CXEpkqHcSLMNq/\n" +
                "AHScbOZ4PRFn2WhPxPTBB29yYZwrhjbJ4rgxJo8joJXshJHUIMBmO8T4Qn+YlG57\n" +
                "bcz6Q8jVz2NjI8CQW3B2FeEvoWdNfTy75gjg14smJJaRmelfhkGI").replace("\n", ""));

        TransactionRequest request = new TransactionRequest();
        request.setFaddr("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw3ioYc1r61s2O/8fZ++JdPkY9VtCGk7VUealTpvBOnnkQkKz/j99tJEFDAbWVyyh8AtHJQxK/Vq1xtVZsSNEMglijhAl3YkbahuDq2j1AjBMPqcwURzi7flfNCov2HNAnE8FFeYVIabTL3FKnb0u2lNSCE228dPwTAYV6b9cNg2hrFZcRqc8UY0+WBDQ0tjHAlSPMi6VrAAecpZtBeTeEqI/YsX8b1C5WEWDL7GvOSkDU5kqvbKuQ/NZKSkxJs8fmM6XbtIs3mPRnJuJAgipEi6DOhDIRmH2TCvmHmGZMFljR5OIP1y2b9pCANmwNE3hTqdgHGdNe4egOL3p4jlL9wIDAQAB");
        request.setType("MT");

        Log.d("faddr", request.getFaddr());
        Log.d("type", request.getType());
        Log.d("limit", String.valueOf(request.getLimit()));
        Log.i("offset", String.valueOf(request.getOffset()));

        helper.sign(request);
        Log.d("privatekey", helper.getPrivateKey());
        Log.d("sign", request.getSign());

        new TransactionTask(request) {
            protected void onPostExecute(List<Coin> result) {
                for (Coin coin : result) {
                    Log.d("sn:", coin.getSn());
                }
            }
        }.execute();

        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
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
