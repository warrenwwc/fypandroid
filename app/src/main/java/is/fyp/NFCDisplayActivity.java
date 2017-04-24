package is.fyp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.requests.TransactionRequest;
import is.fyp.api.responses.BaseResponse;
import is.fyp.api.tasks.EndorseTask;
import is.fyp.api.tasks.GetCoinsTask;
import is.fyp.api.tasks.TransactionTask;
import is.fyp.uiFragment.CalendarFragment;

import static android.R.id.list;

public class NFCDisplayActivity extends Activity {

    private TextView pkText;
    private TextView amountText;
    SharedPreferences sharedPreferences;
    int amount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_display);
        pkText = (TextView) findViewById(R.id.pkText);
        amountText = (TextView) findViewById(R.id.amountText);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        Button pay = (Button) findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String publicKey = sharedPreferences.getString("publicKey", "");
                final String targetPublickey = pkText.getText().toString().trim();
                final int amount = Integer.parseInt(amountText.getText().toString());

                new GetCoinsTask(){
                    private Dialog dialog;

                    protected void onPreExecute () {
                        this.dialog = Utils.getLoader(NFCDisplayActivity.this);
                        this.dialog.show();
                    }


                    protected void onPostExecute(ArrayList<String> result) {
                        List<Coin> pay = new ArrayList<>();

                        if (amount != result.size()) {
                            Toast.makeText(NFCDisplayActivity.this, "Endorse Error: not enough coin",  Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            return;
                        }

                        for (String coin : result) {
                            Coin temp = new Coin();
                            temp.setFaddr(publicKey);
                            temp.setTaddr(targetPublickey);
                            temp.setType("TX");
                            temp.setSn(coin);
                            pay.add(temp);
                        }

                        new EndorseTask(pay) {
                            protected void onPostExecute(BaseResponse result) {
                                dialog.dismiss();
                                if (!result.hasError()) {
                                    Log.d("Endorse", result.message);
                                    Toast.makeText(NFCDisplayActivity.this, "Payment Successful",  Toast.LENGTH_LONG).show();
                                }else{
                                    Log.d("Endorse Error", result.error);
                                    Toast.makeText(NFCDisplayActivity.this, "Endorse Error: " + result.error,  Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute();
                    }
                }.execute(publicKey, amount);

                Intent i = new Intent(NFCDisplayActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            String strArr[] = new String(message.getRecords()[0].getPayload()).split(",");
            pkText.setText(strArr[0]);
            amountText.setText(strArr[1]);
            amount = Integer.parseInt(strArr[1]);
        }

    }
}