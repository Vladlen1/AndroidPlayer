package com.example.vladbirukov.player;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    ArrayList<Category> category_music = new ArrayList<Category>();
    ArrayList<Category> popular_music = new ArrayList<Category>();
    ArrayList<Category> top_5_music = new ArrayList<Category>();
    Adapter Adapter;
    Adapter Adapter_1;
    String[] category = {"Jazz", "Pop", "Rock", "Metall", "National"};
    int[] image = {R.mipmap.jazz, R.mipmap.pop, R.mipmap.ic_launcher, R.mipmap.metal, R.mipmap.national, R.mipmap.ic_launcher, R.mipmap.metal, R.mipmap.national};
    DBHelper dbHelper;

    private ViewFlipper flipper = null;
    private float fromPosition;
    TextView textView;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menus) {
            textView.setText("Top 5 music");
            top_5_music.clear();
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            String orderBy = "repeat";
            Cursor cursor = database.query(DBHelper.TABLE_NAME2, null, null, null, null, null, orderBy);

            if (cursor.moveToNext()) {
                int category_index = cursor.getColumnIndex("name");
                int image_index = cursor.getColumnIndex("image");

                do {

//                    popular_music.add(new Category(cursor.getString(category_index), image[cursor.getInt(image_index)]));
                    popular_music.add(new Category(cursor.getString(category_index), R.mipmap.disk));
                } while (cursor.moveToNext());


            }
            cursor.close();
            dbHelper.close();

            for (int i = 0; i < 5; i++) {
                Category compos = popular_music.get(popular_music.size() - 1 - i);
                top_5_music.add(compos);
            }


            Adapter_1.notifyDataSetChanged();

//

        }
        if (item.getItemId() == R.id.menus_2) {
            textView.setText("Old 5 music");
            top_5_music.clear();
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            Cursor cursor = database.rawQuery(" SELECT * FROM music ORDER BY repeat ", null);

            if (cursor.moveToNext()) {
                int category_index = cursor.getColumnIndex("name");
//                int image_index = cursor.getColumnIndex("image");

                do {

                    popular_music.add(new Category(cursor.getString(category_index), R.mipmap.disk));
                } while (cursor.moveToNext());


            }
            cursor.close();
            dbHelper.close();

            for (int i = 0; i < 5; i++) {
                Category compos = popular_music.get(i);
                top_5_music.add(compos);
            }


            Adapter_1.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activite_two, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout main_layout = (LinearLayout) findViewById(R.id.main_layout);
        main_layout.setOnTouchListener(this);

        flipper = (ViewFlipper) findViewById(R.id.flipper);

        //swap
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layouts[] = new int[]{R.layout.about_me};
        for (int layout : layouts)
            flipper.addView(inflater.inflate(layout, null));

        dbHelper = new DBHelper(this);

        // create adapter
        fillData();
        Adapter = new Adapter(this, category_music);
        Adapter_1 = new Adapter(this, top_5_music);
        // setting list
        final ListView popular_songs = (ListView) findViewById(R.id.popular_songs);
        final ListView lvMain = (ListView) findViewById(R.id.music_list);
        textView = (TextView) findViewById(R.id.textView2);
        lvMain.setAdapter(Adapter);

        popular_songs.setAdapter(Adapter_1);

        lvMain.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String res = String.valueOf(position);
                Intent intent = new Intent(MainActivity.this, ActiviteTwo.class);
                intent.putExtra("id", res);
                startActivity(intent);


            }
        });


    }


    void fillData() {
        int n = 0;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        for (int i = 0; i <= 4; i++) {
//            contentValues.put(DBHelper.KEY_NAME, category[i]);
//            contentValues.put(DBHelper.KEY_IMAGE, image[i]);
//            database.insert(DBHelper.TABLE_NAME, null, contentValues);
//        }
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int category_index = cursor.getColumnIndex("name");
            int image_index = cursor.getColumnIndex("image");

            do {
                category_music.add(new Category(cursor.getString(category_index), cursor.getInt(image_index)));

                n += 1;

            } while (cursor.moveToNext() && n < 5);


        }

        cursor.close();
        dbHelper.close();


    }

    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fromPosition = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float toPosition = event.getX();
                if (fromPosition > toPosition) {
                    flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.go_next_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.go_next_out));
                    flipper.showNext();
                } else if (fromPosition < toPosition) {
                    flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.go_prev_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.go_prev_out));
                    flipper.showPrevious();
                }
            default:
                break;
        }
        return true;
    }
}
