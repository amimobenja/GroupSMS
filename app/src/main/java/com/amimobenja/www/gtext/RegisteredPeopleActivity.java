package com.amimobenja.www.gtext;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by User on 21 July 2015.
 */
public class RegisteredPeopleActivity extends ActionBarActivity {
    private Spinner spinner;
    private TextView txtRegisteredPeople;
    private LinearLayout regPeopleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_people_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.messages_icon_3);

        actionBar.getDisplayOptions();

        String title = getIntent().getStringExtra("title");
        TextView titleTextView = (TextView) findViewById(R.id.txt_regTxt);
        titleTextView.setText(title);

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

        final DBAdapterTwo dbTwo = new DBAdapterTwo(this);
        regPeopleLayout = (LinearLayout) findViewById(R.id.id_ll_3);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DisplayToast("Group - "+spinner.getSelectedItem());
                try {
                    dbTwo.open();
                    System.out.println("Spinner - "+spinner.getSelectedItem().toString());
                    Cursor c = dbTwo.getAllRegPeople(spinner.getSelectedItem().toString());
                    int a = 0;
                    regPeopleLayout.removeAllViews();
                    if (c.moveToFirst()) {
                        do {
                            a++;
                            TextView tv=new TextView(getApplicationContext());
                            tv.setText("   "+a+". 0"+c.getString(0)+"  -  "+c.getString(1)+" "+c.getString(2));
                            tv.setTextColor(getResources().getColor(R.color.greyish));
                            regPeopleLayout.addView(tv);
                        } while (c.moveToNext());

                    } else {
                        DisplayToast("No record found.");
                    }

                    dbTwo.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DisplayToast("Nothing is Selected.");

            }
        });


    }

    private void DisplayToast(String msg) {
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
