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
        alertDialogBuilder.setMessage("Click OK to login again");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }

    public void onClick(View v) {
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
                if (id.getText().toString().trim().contentEquals(sharedPreferences.getString("id", "")) && (password.getText().toString().trim().contentEquals(sharedPreferences.getString("password", "")))) {
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
