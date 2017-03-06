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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
