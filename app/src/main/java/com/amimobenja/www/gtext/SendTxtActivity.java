package com.amimobenja.www.gtext;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by User on 12 July 2015.
 */
public class SendTxtActivity extends ActionBarActivity {
    Spinner spinner, spinnerTwo;
    String[] namesarry;
    Button btnSendSndTxt, btnSendClear;
    EditText edtGreetings, edtSendMessage;

    String SENT = "SMS_SENT", DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending_text_activity);

        final Context context = this;


        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.messages_icon_3);

        actionBar.getDisplayOptions();

        String title = getIntent().getStringExtra("title");
        TextView titleTextView = (TextView) findViewById(R.id.txt_sendTxt);
        titleTextView.setText(title);

        edtGreetings = (EditText) findViewById(R.id.edt_Greetings);
        edtSendMessage = (EditText) findViewById(R.id.edtInput);

        spinner = (Spinner) findViewById(R.id.spinner);
        System.out.println("Spinner set.");
        final DBAdapter db = new DBAdapter(this);
        try {
            db.open();
            loadSpinnerData(db);
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        namesarry = getResources().getStringArray(R.array.name_array);
        spinnerTwo = (Spinner) findViewById(R.id.spinnerThree);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namesarry);
        spinnerTwo.setAdapter(adapter);

        final DBAdapterTwo dbTwo = new DBAdapterTwo(this);
        btnSendSndTxt = (Button) findViewById(R.id.btn_send_snd);
        btnSendSndTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtGreetings.getText().toString().equals("") || edtSendMessage.getText().toString().equals("")) {
                    if (edtGreetings.getText().toString().equals("")) { DisplayToast("Enter the Greetings.");}
                    else {DisplayToast("The Message is missing.");}

                } else {
                    int a = 0;
                    try {
                        dbTwo.open();
                        System.out.println("Spinner 1 - "+spinner.getSelectedItem().toString());
                        Cursor c = dbTwo.getAllRegPeople(spinner.getSelectedItem().toString());

                        dialogShow(context, Integer.valueOf(c.getCount()+"000"));
                        if (c.moveToFirst()) {

                            do {
                                Cursor d = dbTwo.getPerson(c.getString(0), spinner.getSelectedItem().toString());
                                System.out.println("Phone Number - "+c.getString(0));
                                if (d.moveToFirst()) {
                                    String msg_to_send = "Hi";
                                    System.out.println("d - Phone Number - " + "0" + d.getString(0));
                                    if (spinnerTwo.getSelectedItem().toString().equals("First Name")) {
                                        msg_to_send = edtGreetings.getText().toString()+" "+d.getString(1)+", "+edtSendMessage.getText();

                                    } else if (spinnerTwo.getSelectedItem().toString().equals("Second Name")) {
                                        msg_to_send = edtGreetings.getText().toString()+" "+d.getString(2)+", "+edtSendMessage.getText();

                                    } else if (spinnerTwo.getSelectedItem().toString().equals("Nick Name")){
                                        msg_to_send = edtGreetings.getText().toString()+" "+d.getString(3)+", "+edtSendMessage.getText();
                                    } else {
                                        msg_to_send = edtSendMessage.getText().toString();
                                    }

                                    System.out.println("Message - "+msg_to_send);
                                    a++;
                                    String b = String.valueOf(a);
                                    if ((b.substring(b.length()-1, b.length())).equals("0")) {
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    sendSMS("0" + d.getString(0), msg_to_send);

                                } else {
                                    DisplayToast("No such record exists.");
                                }

                            } while (c.moveToNext());


                        } else {
                            DisplayToast("No records found.");
                        }

                        DisplayToast(a+" - SMS Created.");

                        dbTwo.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        btnSendClear = (Button) findViewById(R.id.btn_send_clear);
        btnSendClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        //---create the BroadcastReceiver when the SMS is sent---
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        DisplayShortToast("SMS Sent");
                        break;
                    case  SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        DisplayShortToast("Generic failure");
                        break;
                    case  SmsManager.RESULT_ERROR_NO_SERVICE:
                        DisplayShortToast("No Service");
                        break;
                    case  SmsManager.RESULT_ERROR_NULL_PDU:
                        DisplayShortToast("Null PDU");
                        break;
                    case  SmsManager.RESULT_ERROR_RADIO_OFF:
                        DisplayShortToast("Radio Off");
                        break;

                }

            }
        };

        //---create the BroadcastReceiver when the SMS is delivered ---
        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        DisplayShortToast("SMS Delivered");
                        break;
                    case Activity.RESULT_CANCELED:
                        DisplayShortToast("SMS not Delivered");
                        break;
                }
            }
        };

        // --- register the two BroadcastReceivers---
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
    }

    @Override
    public void onPause() {
        super.onPause();

        //---unregister the two BroadcastReceivers---
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    private void dialogShow(final Context cxt, final int slp) {
        final ProgressDialog dialog = ProgressDialog.show(cxt, "Sending SMS", "Please wait...", true);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(slp);
                    dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendSMS(final String phoneNumber, final String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

    }

    private void clear() {
        edtGreetings.setText("");
        edtSendMessage.setText("");
    }

    private void DisplayToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void DisplayShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData(DBAdapter db) {
        // database handler

        // Spinner Drop down elements
        List<String> lables = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


}
