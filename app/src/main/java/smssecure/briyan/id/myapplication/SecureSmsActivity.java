package smssecure.briyan.id.myapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class SecureSmsActivity extends ActionBarActivity {

    static final int MY_REQUEST_CODE = 1;

    TextView etNama;
    TextView etNotlp;
    TextView encryptText;
    TextView tvKey;
    TextView etPesan;
    TextView etChar;
    Button btnsend;

    String nama="";
    String notlp="";
    String msg="";
    String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_sms);


        etNama= (TextView) findViewById(R.id.etNama);
        etNotlp= (TextView) findViewById(R.id.etNotlp);
        encryptText= (TextView) findViewById(R.id.etEncryp);
        btnsend= (Button) findViewById(R.id.btnSend);
        tvKey=(TextView) findViewById(R.id.etKodeRahasia);
        etPesan=(TextView) findViewById(R.id.etPesan);
        etChar=(TextView) findViewById(R.id.etChar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.i("extras","extras tidak null");
            notlp = extras.getString("Value1");
            nama = extras.getString("Value2");
            msg = extras.getString("Value3");
            key = extras.getString("Value4");
            Log.i("v1",nama);
            Log.i("v2",notlp);
            Log.i("v3",msg);
            Log.i("v4",key);

            try {
                Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(notlp));
                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getContentResolver()
                        .query(uri, projection, null, null, null);
                cursor.moveToFirst();
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column= cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                nama = cursor.getString(column);
            }catch (Exception e){
                nama = "Tidak diketahui";
            }



            etNama.setText(nama);
            etNotlp.setText(notlp);
            tvKey.setText(key);
            etPesan.setText(msg);



            /*
            AES.setKey(key);
            AES.encrypt(msg);
            encryptText.setText(AES.getEncryptedString().toString());
*/
            //new EncryptTask().execute(msg);


            try {
                AESCrypt ac= null;
                ac = new AESCrypt(key);
                encryptText.setText(ac.encrypt(msg).trim());

            } catch (Exception e) {
                e.printStackTrace();
            }
            int txt_count=encryptText.getText().toString().length();

            int page=txt_count/160;

            int sisa=txt_count%160;

            etChar.setText((page != 0) ? page + " " : "" + "(" + sisa + "/160)");

        }

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(notlp, null, encryptText.getText().toString(), null, null);
                    Toast.makeText(getApplicationContext(),
                            "SMS terkirim",
                            Toast.LENGTH_LONG).show();
                    Intent i= new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),
                            "SMS tidak terkirim",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }*/


                String phoneNo = notlp;
                String message = encryptText.getText().toString();

                sendSMS(phoneNo, message);


                //sendSMSIntent(notlp,message);
            }
        });

        sendSMSIntent(notlp, encryptText.getText().toString());
    }

    public void sendSMSIntent(String number, String txt)
    {
        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(txt, number, null)));
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("address", number);
        sendIntent.putExtra("sms_body", txt);
        sendIntent.setType("vnd.android-dir/mms-sms");
        //startActivity(sendIntent);
        startActivityForResult(sendIntent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Intent i= new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Terkirim",
                                Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_secure_sms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
