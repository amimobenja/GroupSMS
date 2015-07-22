package com.amimobenja.www.gtext;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by User on 13 July 2015.
 */
public class RegistrationActivity extends ActionBarActivity {
    private Spinner spinnerGrp;
    private Button btnRegSave, btnRegView, btnRegClear, btnRegSearch, btnRegUpdate, btnRegDelete;

    private EditText edtFName, edtSName, edtNName;
    private AutoCompleteTextView edtPNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.messages_icon_3);

        actionBar.getDisplayOptions();

        String title = getIntent().getStringExtra("title");
        TextView titleTextView = (TextView) findViewById(R.id.txt_RegistrationTxt);
        titleTextView.setText(title);

        spinnerGrp = (Spinner) findViewById(R.id.spinnerGroup);
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

        edtPNumber = (AutoCompleteTextView) findViewById(R.id.edtPhoneNumber);
        edtFName = (EditText) findViewById(R.id.edtFirstName);
        edtSName = (EditText) findViewById(R.id.edtSecondName);
        edtNName = (EditText) findViewById(R.id.edtNickName);


        edtPNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbTwo.open();
                    edtPNumber.setThreshold(3);
                    System.out.println("ENTERED Threshold");
                    edtPNumber.setAdapter(returnAdapterTwo(dbTwo, spinnerGrp.getSelectedItem().toString()));
                    dbTwo.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });



        btnRegSave = (Button) findViewById(R.id.btn_RegSave);
        btnRegSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbTwo.open();
                    System.out.println("Phone - "+R.string.edt_PhoneNumber);
                    if (edtPNumber.getText().toString().equals("") || edtFName.getText().toString().equals("") ||
                    edtSName.getText().toString().equals("") || edtNName.getText().toString().equals("")) {
                        DisplayToast("Empty Values Noted!");

                    } else {
                        Cursor c = dbTwo.getPerson(edtPNumber.getText().toString().trim(), spinnerGrp.getSelectedItem().toString());
                        System.out.println("Phone Number - "+edtPNumber.getText().toString().trim());
                        if (c.moveToFirst()) {
                            DisplayToast(edtPNumber.getText().toString().trim()+" - Record Exist.");
                        } else {

                            long idTwo = dbTwo.insertContact(edtPNumber.getText().toString().trim(),
                                    capitalize(edtFName.getText().toString().trim()),
                                    capitalize(edtSName.getText().toString().trim()),
                                    capitalize(edtNName.getText().toString().trim()),
                                    spinnerGrp.getSelectedItem().toString());
                            DisplayToast("RC"+idTwo+":- "+edtPNumber.getText().toString()+" is Saved!");
                            clear();

                        }
                    }
                    dbTwo.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        btnRegView = (Button) findViewById(R.id.btn_RegView);
        btnRegView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbTwo.open();
                    System.out.println("Spinner - "+spinnerGrp.getSelectedItem().toString());
                    Cursor c = dbTwo.getAllRegPeople(spinnerGrp.getSelectedItem().toString());
                    if (c.moveToFirst()) {
                        do {
                            DisplayToast("Phone No. - 0"+c.getString(0)+"\nName - "+c.getString(1)+" "+c.getString(2)+
                                "\nNick Name - "+c.getString(3)+
                                "\nGroup Assigned - "+c.getString(4));
                        } while (c.moveToNext());

                    } else {
                        DisplayToast("No record found.");
                    }

                    dbTwo.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        btnRegClear = (Button) findViewById(R.id.btn_RegClear);
        btnRegClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        btnRegSearch = (Button) findViewById(R.id.btn_RegSearch);
        btnRegSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbTwo.open();
                    System.out.println("Phone - "+R.string.edt_PhoneNumber);
                    if (edtPNumber.getText().toString().equals("")) {
                        DisplayToast("Enter Phone Number");

                    } else {
                        Cursor c = dbTwo.getPerson(edtPNumber.getText().toString().trim(), spinnerGrp.getSelectedItem().toString());
                        System.out.println("Phone Number - "+edtPNumber.getText().toString().trim());
                        if (c.moveToFirst()) {
                            edtFName.setText(c.getString(1));
                            edtSName.setText(c.getString(2));
                            edtNName.setText(c.getString(3));

                            db.open();
                            int spinnerPosition = returnAdapter(db).getPosition(c.getString(4));
                            db.close();
                            spinnerGrp.setSelection(spinnerPosition);

                            DisplayToast("Record "+edtPNumber.getText().toString()+" is Opened");

                        } else {
                            DisplayToast("Record "+edtPNumber.getText().toString().trim()+" Not found!");

                        }
                    }
                    dbTwo.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        btnRegUpdate = (Button) findViewById(R.id.btn_RegEdit);
        btnRegUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbTwo.open();
                    System.out.println("Phone No: "+edtPNumber.getText().toString().trim());


                    if (edtPNumber.getText().toString().equals("") || edtFName.getText().toString().equals("") ||
                            edtSName.getText().toString().equals("") || edtNName.getText().toString().equals("")) {
                        DisplayToast("Empty Values Noted!");

                    } else {
                        if (dbTwo.updateContact(edtPNumber.getText().toString().trim(), capitalize(edtFName.getText().toString().trim()),
                                capitalize(edtSName.getText().toString().trim()), capitalize(edtNName.getText().toString().trim()),
                                spinnerGrp.getSelectedItem().toString())) {
                            DisplayToast("Record - "+edtPNumber.getText().toString().trim()+" Updated Successfully.");
                            clear();

                        } else {
                            DisplayToast("Update not Successful.");
                        }
                    }


                    dbTwo.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });


        btnRegDelete = (Button) findViewById(R.id.btn_RegDelete);
        btnRegDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    dbTwo.open();
                    if (dbTwo.deleteValue(edtPNumber.getText().toString().trim(), spinnerGrp.getSelectedItem().toString())) {
                        DisplayToast("Record - "+edtPNumber.getText().toString().trim()+" Deleted Successfully.");
                        clear();
                    } else {
                        DisplayToast("Delete failed for Record - "+edtPNumber.getText().toString().trim());
                    }
                    dbTwo.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });



    }

    private void clear() {
        edtPNumber.setText("");
        edtFName.setText("");
        edtSName.setText("");
        edtNName.setText("");
    }

    private String capitalize(String line) {
        line = line.toLowerCase();
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
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
        spinnerGrp.setAdapter(dataAdapter);
    }

    private ArrayAdapter returnAdapter(DBAdapter db) {
        List<String> lables = db.getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        return dataAdapter;
    }

    private ArrayAdapter returnAdapterTwo(DBAdapterTwo dbArgs, String group) {
        List<String> lables = dbArgs.getPhoneNumbers(group);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, lables);
        return dataAdapter;
    }

    private void DisplayToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


}