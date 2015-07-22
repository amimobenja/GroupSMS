package com.amimobenja.www.gtext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16 July 2015.
 */
public class SMSListener extends BroadcastReceiver {

    private SharedPreferences preferences;
    private String msg_from, msgBody, sent_from;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        final DBAdapterTwo abc = new DBAdapterTwo(context);
        final DBAdapter db = new DBAdapter(context);


        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        sent_from = "0"+msg_from.substring(4, msg_from.length());
                        msgBody = msgs[i].getMessageBody();
                    }

                    System.out.println("Message - "+msgBody);
                    String foo = msgBody.substring(0, Math.min(4, msgBody.length()));
                    System.out.println("Message FOO - "+foo);
                    System.out.println("From - "+sent_from);
                    if (foo.equals("REG:")) {
                        System.out.println("Message - "+msgBody);
                        //store into DB.

                        String fName = msgBody.split(" ")[1];
                        String sName = msgBody.split(" ")[2];
                        String nName = msgBody.split(" ")[3];
                        String gGrp = msgBody.split(" ")[4];

                        int intGrp = Integer.parseInt(gGrp);
                        db.open();
                        ArrayList<String> grp = db.myLabels();
                        String group = "NULL";
                        for (int i = 0; i < grp.size(); i++) {
                            if (i == intGrp-1) {
                                System.out.println("GROUP - "+grp.get(i));
                                group = grp.get(i);
                            }

                        }
                        db.close();

                        if (!group.equals("NULL")) {
                            abc.open();
                            Cursor c = abc.getPerson(sent_from, group);
                            if (c.moveToFirst()) {
                                System.out.println("Record - "+sent_from+" Exist.");
                                sendSMS(sent_from, "Sorry, Record - "+sent_from+" currently exist.");
                            } else {

                                System.out.println("Message - FN: "+fName+" SN: "+sName+" NN: "+nName+" GN: "+group);
                                long idTwo = abc.insertContact(sent_from,
                                        capitalize(fName.trim()),
                                        capitalize(sName.trim()),
                                        capitalize(nName.trim()),
                                        group.trim());
                                System.out.println("Message - SUCCESS");
                                sendSMS(sent_from, "WELCOME "+capitalize(fName.trim())+". \nRecord: "+sent_from+" is Registered Successfully.\n" +
                                        "FIRST NAME - "+capitalize(fName.trim())+"\n" +
                                        "SECOND NAME - "+capitalize(sName.trim())+"\n" +
                                        "NICK NAME - "+capitalize(nName.trim())+"\n" +
                                        "GROUP ASSIGNED - "+group.trim()+".\n");

                            }
                            abc.close();

                        } else {
                            sendSMS(sent_from, "Hi, Group - "+intGrp+" does not exist. Please try again.\n" +
                                    "Use the format below:\n" +
                                    "REG: First_name Second_name, Nick_name Group_number.");
                        }


                    }

                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private String capitalize(String line) {
        line = line.toLowerCase();
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }


}
