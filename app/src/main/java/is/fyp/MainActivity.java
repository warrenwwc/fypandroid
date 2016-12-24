package is.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    TextView sign_up;
    Button sign_in;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("loginData" , MODE_PRIVATE);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.sign_up: {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
            }

            case R.id.sign_in: {
                SharedPreferences sharedPreferences = getSharedPreferences("loginData" , MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLogin", true);
                editor.apply();
                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                MainActivity.this.finish();
                startActivity(i);
                break;
            }
        }
    }
}
