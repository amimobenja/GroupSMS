package com.amimobenja.www.gtext;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by User on 20 May 2015.
 */
public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.messages_icon_3);

        String title = getIntent().getStringExtra("title");
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        Bitmap bitmap = getIntent().getParcelableExtra("image");
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
    }
}
