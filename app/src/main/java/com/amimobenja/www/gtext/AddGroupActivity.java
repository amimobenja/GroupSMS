package com.amimobenja.www.gtext;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by User on 20 May 2015.
 */
public class AddGroupActivity extends ActionBarActivity {
    private Button btnSave;
    private EditText edtGroupName, edtGroupDescription;
    private TextView txtGroupName, txtGroupDescript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_groups_activity);

        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.messages_icon_3);

        String title = getIntent().getStringExtra("title");
        TextView titleTextView = (TextView) findViewById(R.id.txt_Title);
        titleTextView.setText(title);


        txtGroupName = (TextView) findViewById(R.id.txt_GroupName);
        txtGroupDescript = (TextView) findViewById(R.id.txt_DescribeGroup);
        edtGroupName = (EditText) findViewById(R.id.edit_message_edt);
        edtGroupDescription = (EditText) findViewById(R.id.edit_description);

        final DBAdapter db = new DBAdapter(this);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.open();
                    if (edtGroupName.getText().toString().equals("Group Name") || edtGroupDescription.getText().toString().equals("")) {
                        DisplayToast("Ensure that both the "+txtGroupName.getText()+" and "+txtGroupDescript.getText()+" are filled.");

                    } else {
                        long id = db.insertContact(edtGroupName.getText().toString().trim(),
                                edtGroupDescription.getText().toString().trim());
                        DisplayToast("ID - "+id+" of Group Name "+edtGroupName.getText()+" has been Inserted ");
                        edtGroupName.setText("");
                        edtGroupDescription.setText("");

                    }
                    db.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }



            }
        });




    }

    private void DisplayToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
