package smssecure.briyan.id.myapplication;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class InboxActivity extends ActionBarActivity {

    ListView list;
    CustomAdapter adapter;


    List<SMSData> smsList = new ArrayList<SMSData>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        Uri uri = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[] { "_id", "address", "body", "person", "date","read" };
        Cursor c= getContentResolver().query(uri, reqCols, null ,null,null);
        startManagingCursor(c);

        // Read the sms data and store it in the list
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
                SMSData sms = new SMSData();
                sms.setId(c.getString(c.getColumnIndexOrThrow("_id")).toString());
                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());


                //sms.setName(getContactName(getApplicationContext(),c.getString(c.getColumnIndexOrThrow("address")).toString()));
                sms.setDate(c.getString(c.getColumnIndexOrThrow("date")).toString());
                //sms.setDate_sent(c.getString(c.getColumnIndexOrThrow("date_sent")).toString());
                //sms.setCreator(c.getString(c.getColumnIndexOrThrow("creator")).toString());
                sms.setRead(c.getString(c.getColumnIndexOrThrow("read")).toString());
                smsList.add(sms);
                //Log.i("sms", c.getString(c.getColumnIndexOrThrow("address")).toString() + ", " + c.getString(c.getColumnIndexOrThrow("body")).toString());

                c.moveToNext();
            }
        }
        //c.close();

        list= ( ListView )findViewById( R.id.list_inbox );
        adapter = new CustomAdapter(InboxActivity.this, smsList);
        list.setAdapter( adapter );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_inbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
