package is.fyp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TransferActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(TransferActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Sorry this device does not have NFC.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);

    }

    /**
     * Ndef Record that will be sent over via NFC
     * @param nfcEvent
     * @return
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        Intent myIntent = getIntent(); // gets the previously created intent
        String amt = myIntent.getStringExtra("amount");
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", transData(amt));
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    private byte[] transData(String amount) {
        SharedPreferences sharedPreferences = getSharedPreferences("data" , MODE_PRIVATE);
        String publicKey = String.valueOf(sharedPreferences.getString("publicKey", ""));
        String rtnMeg = publicKey + "," + amount;
        return rtnMeg.getBytes();
    }

    private void openSuccessAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TransferActivity.this);

        alertDialogBuilder.setTitle("Registration Success");
        alertDialogBuilder.setMessage("Click OK to continue");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                Intent i = new Intent(TransferActivity.this, MenuActivity.class);
                TransferActivity.this.finish();
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }
}

