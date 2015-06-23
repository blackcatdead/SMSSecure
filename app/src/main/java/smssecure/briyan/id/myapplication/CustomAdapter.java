package smssecure.briyan.id.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ikhsanudin on 5/1/2015.
 */
public class CustomAdapter extends BaseAdapter {
    private Activity activity;

    List<SMSData> data = new ArrayList<SMSData>();
    private static LayoutInflater inflater = null;

    public CustomAdapter(Activity a, List<SMSData> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup arg2) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.item_sms, null);

        TextView nama = (TextView) vi.findViewById(R.id.sms_name);
        TextView number = (TextView) vi.findViewById(R.id.sms_number);
        TextView date = (TextView) vi.findViewById(R.id.sms_date);
        TextView txt = (TextView) vi.findViewById(R.id.sms_text);



        final String f_id=data.get(position).getId().toString();
        final String f_body=data.get(position).getBody().toString();
        final String f_date=data.get(position).getDate().toString();
        final String f_number=data.get(position).getNumber().toString();

        long datz=Long.parseLong(f_date);
       // Date datex = new Date(datz);
        //String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(datex);

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datz);
        //Log.i("time", "time"+formatter.format(calendar.getTime()));

        nama.setText(f_id);
        txt.setText(f_body);
        date.setText(formatter.format(calendar.getTime()));
        number.setText(f_number);
        //String id=data.get(position).get().toString();

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.setTitle("Kode Rahasia");
                dialog.setContentView(R.layout.open_sms);

                final EditText etKodeRahasia= (EditText) dialog.findViewById(R.id.etKodeRahasia);

                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(activity);

                String strUserName = SP.getString("example_key", "");
                etKodeRahasia.setText(strUserName);
                Button dialogButton = (Button) dialog.findViewById(R.id.button);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(activity, OpenMessageActivity.class);
                        i.putExtra("Value1", f_id);
                        i.putExtra("Value2", etKodeRahasia.getText().toString());

                        activity.startActivity(i);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return vi;
    }
}
