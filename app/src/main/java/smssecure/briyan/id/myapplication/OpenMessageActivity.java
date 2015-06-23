package smssecure.briyan.id.myapplication;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class OpenMessageActivity extends ActionBarActivity {

    TextView om_number;
    TextView om_nama;
    TextView om_enc_msg;
    TextView om_key;
    TextView om_dec_msg;

    String kodeRahasia="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_message);

        om_number= (TextView) findViewById(R.id.om_notlp);
        om_nama= (TextView) findViewById(R.id.om_nama);
        om_enc_msg= (TextView) findViewById(R.id.om_Encryp_msg);
        om_key= (TextView) findViewById(R.id.om_key);
        om_dec_msg= (TextView) findViewById(R.id.om_message);

        String smsID="";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.i("extras", "extras tidak null");
            smsID = extras.getString("Value1");
            kodeRahasia= extras.getString("Value2");

        }



        Uri myMessage = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(myMessage, new String[] { "_id", "address", "date", "body","read" },"_id = "+smsID, null, null);

        c.moveToFirst();

        String number = c.getString(c.getColumnIndexOrThrow("address")).toString();
        String date = c.getString(c.getColumnIndexOrThrow("date")).toString();
        String status = c.getString(c.getColumnIndex("read"));
        String message = c.getString(c.getColumnIndexOrThrow("body")).toString();

        c.close();

        String nama;

        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
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



        om_nama.setText(nama);


        om_number.setText(number);
        om_enc_msg.setText(message);
        om_key.setText(kodeRahasia);
        //new DecryptTask().execute(message);

        try {
            AESCrypt ac= null;
            ac = new AESCrypt(kodeRahasia);
            om_dec_msg.setText(ac.decrypt(message));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_open_message, menu);
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
