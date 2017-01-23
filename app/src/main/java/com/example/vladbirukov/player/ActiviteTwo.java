package com.example.vladbirukov.player;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.content.ContentValues;
import android.widget.Toast;



import java.util.ArrayList;

public class ActiviteTwo extends AppCompatActivity {
    String id;
    Button add_song;
    Button inner_join;
    Button ndk;
    RelativeLayout view;
    EditText composion_name;
    EditText image_name;
    final int DIALOG = 1;
    int btn;
    private static final int CM_DELETE_ID = 1;
    String composition_id;

    int[] image = {R.mipmap.jazz, R.mipmap.pop, R.mipmap.ic_launcher, R.mipmap.metal, R.mipmap.national, R.mipmap.ic_launcher, R.mipmap.metal, R.mipmap.national, R.mipmap.metal, R.mipmap.national, R.mipmap.ic_launcher, R.mipmap.national, R.mipmap.national, R.mipmap.national, R.mipmap.ic_launcher};
    DBHelper dbHelper;

    ArrayList<Category> compositions_song = new ArrayList<Category>();
    Adapter Adapter;


    private ImageView imageView;
    private float scale = 1f;
    private ScaleGestureDetector detector;
                                                                                                                                    int music_compostion [] = {R.raw.jazz_1,R.raw.jazz_2, R.raw.jazz_4, R.raw.pop_1,R.raw.pop_3,R.raw.pop_4, R.raw.rock_2,R.raw.rock_3,R.raw.rock_4, R.raw.metal_1,R.raw.metal_2,R.raw.metal_3, R.raw.national_1, R.raw.national_2,R.raw.national_4};
    MediaPlayer player;
    double duration_mas[] = new double[15];
    static {
        System.loadLibrary("native-lib");
    }

    public native String cackduration(double[] mas);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_two);


        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        composition_id = id;

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(image[Integer.valueOf(composition_id)]);
        detector = new ScaleGestureDetector(this, new ScaleListener());


        fillData(id);
        Adapter = new Adapter(this, compositions_song);
        final ListView lvtwo = (ListView) findViewById(R.id.sing_song);
        lvtwo.setAdapter(Adapter);
        registerForContextMenu(lvtwo);
        lvtwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String music_id = String.valueOf(id);
                Intent intent = new Intent(ActiviteTwo.this, ActivityThree.class);
                intent.putExtra("compos_id", composition_id);
                intent.putExtra("music_id", music_id);
                startActivity(intent);


            }
        });

        add_song = (Button) findViewById(R.id.add_song);
        add_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn = view.getId();
                showDialog(DIALOG);
            }
        });


        inner_join = (Button) findViewById(R.id.inner_join);
        inner_join.setOnTouchListener(new com.example.vladbirukov.player.OnClickListener() {
            @Override
            public void onClick() {
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                String sqlQuery = " select PL.name as Category, PS.name as Name"
                        + " from category as PL "
                        + " inner join music as PS "
                        + " on PL.id - 1  = PS.category ";
                Cursor cursor = database.rawQuery(sqlQuery, null);
                logCursor(cursor);
                cursor.close();
                dbHelper.close();
                Log.d("inner join", "test");
            }
        });

        ndk = (Button) findViewById(R.id.ndk);
        ndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calck_time();
                long startTime = System.nanoTime();
                calc_duration_java(duration_mas);
                long totalTime = System.nanoTime() - startTime;
                Toast toast = Toast.makeText(getApplicationContext(),"Speed Java " + String.valueOf(totalTime), Toast.LENGTH_SHORT);
                toast.show();
                Log.d("Speed java", String.valueOf(totalTime));


                startTime = System.nanoTime();
                cackduration(duration_mas);
                totalTime = System.nanoTime() - startTime;
                toast = Toast.makeText(getApplicationContext(),"Speed C " + String.valueOf(totalTime), Toast.LENGTH_SHORT);
                toast.show();
                Log.d("Speed C", String.valueOf(totalTime));

            }
        });




    }

    public double calc_duration_java(double[] mas){
        double res= 0;
        for(int i = 0; i < 15; i++){
            res += mas[i];
        }
        return res;
    }

    public void calck_time(){
        for(int i=0; i<15;i++){
            player = MediaPlayer.create(ActiviteTwo.this, music_compostion[i]);
            duration_mas[i] = player.getDuration()/6e+4;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        float onScaleBegin = 0;
        float onScaleEnd = 0;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            onScaleBegin = scale;

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            onScaleEnd = scale;

            if (onScaleEnd > onScaleBegin) {
            }

            if (onScaleEnd < onScaleBegin) {
            }

            super.onScaleEnd(detector);
        }
    }


    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d("prime", str);
                } while (c.moveToNext());
            }
        } else
            Log.d("prime", "Cursor is null");
    }

    void fillData(String id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] columns = new String[]{"name", "image"};
        String selection = "category = ?";
        String[] selectionArgs = {id};
        Cursor cursor = database.query(DBHelper.TABLE_NAME2, columns, selection, selectionArgs, null, null, null);
        int i = 0;

        if (cursor.moveToNext()) {
            int category_index = cursor.getColumnIndex("name");
            int image_index = cursor.getColumnIndex("image");

            do {

//                compositions_song.add(new Category(cursor.getString(category_index), image[cursor.getInt(image_index)]));
                compositions_song.add(new Category(cursor.getString(category_index), R.mipmap.disk));

                i += 1;

            } while (cursor.moveToNext());


        }
        cursor.close();
        dbHelper.close();


    }

    protected Dialog onCreateDialog(int id) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        // заголовок
        view = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog, null);
        adb.setView(view);
        composion_name = (EditText) view.findViewById(R.id.composition);
        image_name = (EditText) view.findViewById(R.id.image_name);
        adb.setNeutralButton("Cancel", myClickListener);
        adb.setPositiveButton("Add", myClickListener);


        return adb.create();
    }

    OnClickListener myClickListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // add
                case Dialog.BUTTON_POSITIVE:
                    String compos = composion_name.getText().toString();
                    String image_load = image_name.getText().toString();
                    Integer image_int = Integer.valueOf(image_load);

                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_NAME, compos);
                    contentValues.put(DBHelper.KEY_IMAGE, image_load);
                    contentValues.put(DBHelper.KEY_CATEGORY, id);
                    contentValues.put(DBHelper.KEY_REPEAT, "0");
                    database.insert(DBHelper.TABLE_NAME2, null, contentValues);

                    dbHelper.close();

//                    compositions_song.add(new Category(compos, image[image_int]));
                    compositions_song.add(new Category(compos, R.mipmap.disk));

                    Adapter.notifyDataSetChanged();


                    composion_name.setText("");
                    image_name.setText("");

                    break;
                // cancel
                case Dialog.BUTTON_NEUTRAL:

                    break;
            }
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Удалить ");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();

            Category prime = compositions_song.get(acmi.position);
            String name_del = prime.name;
            Log.d("prime", name_del);
            database.delete(DBHelper.TABLE_NAME2, "name = ?", new String[]{name_del});
            compositions_song.remove(acmi.position);
            Adapter.notifyDataSetChanged();
            return true;
        }
        return super.onContextItemSelected(item);
    }

}



