package smssecure.briyan.id.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class NewMessageActivity extends ActionBarActivity {

    static final int PICK_CONTACT_REQUEST = 1;
    static final int PICK_CONTACT_REQUEST2 = 2;



    EditText etContact;
    EditText etMsg;
    EditText etKey;
    Button btnEncrypt;
    Button btnCari;

    String name="";
    String key="";
    String phonenumber="";

    String msg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        etContact = (EditText) findViewById(R.id.etContact);
        etKey = (EditText) findViewById(R.id.etKey);
        btnEncrypt= (Button) findViewById(R.id.btnEncrypt);
        btnCari= (Button) findViewById(R.id.btnCari);
        etMsg= (EditText) findViewById(R.id.etMsg);
        etContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
            }
        });




        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((!etMsg.getText().toString().matches(""))&&(!etKey.getText().toString().matches(""))&&(!etContact.getText().toString().matches(""))/*&&(etContact.getText().toString().length()>=3)*/)
                {
                    /*
                    Toast.makeText(NewMessageActivity.this,etMsg.getText().toString()+", "+etKey.getText().toString()+", "+etContact.getText().toString(),Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), SecureSmsActivity.class);
                    i.putExtra("Value1",   etContact.getText().toString());
                    i.putExtra("Value2", name);
                    i.putExtra("Value3", etMsg.getText().toString());
                    i.putExtra("Value4", etKey.getText().toString());

                    startActivity(i);
                    */
                    sendSMSIntent(etContact.getText().toString(), etMsg.getText().toString(), etKey.getText().toString());
                }else
                {
                    Toast.makeText(NewMessageActivity.this,"Isi semua form terlebih dahulu.",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void sendSMSIntent(String number, String txt, String key)
    {
        String enc="";
        try {
            AESCrypt ac= null;
            ac = new AESCrypt(key);
            enc=ac.encrypt(txt).trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(txt, number, null)));
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("address", number);
        sendIntent.putExtra("sms_body", enc);
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivityForResult(sendIntent, PICK_CONTACT_REQUEST2);
        //startActivityForResult(sendIntent, PICK_CONTACT_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER ,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int column2= cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                phonenumber = cursor.getString(column);
                name = cursor.getString(column2);
                //etContact.setText(name+" ("+phonenumber+")");
                etContact.setText(phonenumber);
                // Do something with the phone number...
            }

        }
        else if (requestCode == PICK_CONTACT_REQUEST2) {
            // Make sure the request was successful
            Intent i= new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_new_message, menu);
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
