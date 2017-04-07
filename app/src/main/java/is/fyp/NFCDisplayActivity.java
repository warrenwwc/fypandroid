package is.fyp;

import android.app.Activity;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.fyp.api.Coin;
import is.fyp.api.Helper;
import is.fyp.api.requests.TransactionRequest;
import is.fyp.api.responses.BaseResponse;
import is.fyp.api.tasks.EndorseTask;
import is.fyp.api.tasks.TransactionTask;

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
                final Helper helper = Helper.getInstance();
                TransactionRequest request = new TransactionRequest();
                final String publicKey = sharedPreferences.getString("publicKey", "");
                //final String randomMerchant = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwnqEM1PNOF++GUR2oElEmkIQh0ZbXu7Fr7Fjouv36JTWaXA4EjHFBnZKghKq7flFDJ+UVEIHJr/BL30AT3e1AimMb3xmUbm2q3BThHXZr7gfPIeXmuGNRaaY9YPC9tX7xCAwhy9ChNpxr6Hlf226+Yc4Fq7be/QxYyka7DTu5cyNijk09pCCL6jA4aMm59yhfM9j8ESjxOxgYhV+qL8Mz9okHT0IvetUZsHdDtWbkarfEpZAoyZZNXguod0XCgWZwVCK9+tpJzk4S/XSWu12UZyQ8PKYIvw5Tgoo6cJOJ/vhvikaFvuzvfoG5sTIExrqa+Utrol2t0Hq5Vu2gOTR3wIDAQAB";
                request.setFaddr(publicKey);
                //request.setType("MT");
                helper.sign(request);

                new TransactionTask(request) {
                    protected void onPostExecute(List<Coin> result) {

                        ArrayList<String> endorsed = new ArrayList<String>();
                        for (Coin coin : result) {
                            if(coin.getType().equals("TX")) {
                                endorsed.add(coin.getSn());
                                //endorsed.add(coin.getHsign());
                                Log.d("used coin", coin.getHsign());
                            }
                        }

                        int amt = Integer.parseInt(amountText.getText().toString());
                        int count = 0;
                        List<Coin> pay = new ArrayList<>();
                        outerloop:
                        for (Coin coin : result) {
                            if(!coin.getType().equals("MT")) {
                                continue ;
                            }
                            Log.d("count", String.valueOf(count));
                            if(amt == count) {
                                break ;
                            }
                            for (String endorsedHash : endorsed) {
                                if(endorsedHash.equals(coin.getHsign())) {
                                    endorsed.remove(endorsedHash);
                                    continue outerloop;
                                }
                            }
                            Coin temp = new Coin();
                            temp.setFaddr(publicKey);
                            temp.setTaddr(pkText.getText().toString().trim());
                            temp.setType("TX");
                            temp.setSn(coin.getHsign());
                            Log.d("hsign", coin.getHsign());
                            pay.add(temp);
                            count++;
                        }

                        new EndorseTask(pay) {
                            protected void onPostExecute(BaseResponse result) {
                                if (!result.hasError()) {
                                    Log.d("Endorse", result.message);
                                }else{
                                    Log.d("Endorse Error", result.error);
                                }
                            }
                        }.execute();
                    }
                }.execute();
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