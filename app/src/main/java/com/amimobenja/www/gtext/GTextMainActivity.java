package com.amimobenja.www.gtext;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


public class GTextMainActivity extends ActionBarActivity {
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gtext_main);

        gridView = (GridView) findViewById(R.id.gridView);



        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);


                System.out.println("HERE - "+item.getTitle());

                TypedArray imgs_name = getResources().obtainTypedArray(R.array.image_name);
                //Create intent
                if (item.getTitle().equals(imgs_name.getString(0))) {
                    Intent intent = new Intent(GTextMainActivity.this, AddGroupActivity.class);
                    intent.putExtra("title", item.getTitle());
                    //Start details activity
                    startActivity(intent);
                } else if (item.getTitle().equals(imgs_name.getString(1))) {
                    Intent intent = new Intent(GTextMainActivity.this, DetailsActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("image", item.getImage());
                    //Start details activity
                    startActivity(intent);

                } else if (item.getTitle().equals(imgs_name.getString(2))) {
                    Intent intent = new Intent(GTextMainActivity.this, DetailsActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("image", item.getImage());
                    //Start details activity
                    startActivity(intent);

                } else if (item.getTitle().equals(imgs_name.getString(3))) {
                    Intent intent = new Intent(GTextMainActivity.this, DetailsActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("image", item.getImage());
                    //Start details activity
                    startActivity(intent);

                } else if (item.getTitle().equals(imgs_name.getString(4))) {
                    Intent intent = new Intent(GTextMainActivity.this, DetailsActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("image", item.getImage());
                    //Start details activity
                    startActivity(intent);

                } else if (item.getTitle().equals(imgs_name.getString(5))) {
                    Intent intent = new Intent(GTextMainActivity.this, DetailsActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("image", item.getImage());
                    //Start details activity
                    startActivity(intent);

                } else  {
                    Intent intent = new Intent(GTextMainActivity.this, DetailsActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("image", item.getImage());
                    //Start details activity
                    startActivity(intent);

                }
                System.out.println("THIS - "+item.getTitle());






            }
        });
    }




    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        TypedArray imgs_name = getResources().obtainTypedArray(R.array.image_name);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
                imageItems.add(new ImageItem(bitmap, imgs_name.getString(i)));
        }
        return imageItems;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gtext_main, menu);
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
