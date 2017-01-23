package com.example.vladbirukov.player;


import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.ContentValues;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Fragment1 extends Fragment {


    String composition_id;
    Integer music_id;
    int[] image = {R.mipmap.jazz,R.mipmap.pop,R.mipmap.ic_launcher, R.mipmap.metal, R.mipmap.national,R.mipmap.ic_launcher, R.mipmap.metal, R.mipmap.national, R.mipmap.metal, R.mipmap.national,R.mipmap.ic_launcher, R.mipmap.national, R.mipmap.national,R.mipmap.national,R.mipmap.ic_launcher};
    DBHelper dbHelper;

    ArrayList<Category> compositions_song = new ArrayList<Category>();
                                                                                                                                                                                                        String[][] compos_url_image = {  {"http://s8.hostingkartinok.com/uploads/images/2016/11/3086927f1453bd789d017df9b6790b89.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/5e1e8006e1b87cd2b78595e23032a123.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/2be5a0b12865c0150ad708b0ca306d78.jpg"}, {"http://s8.hostingkartinok.com/uploads/images/2016/11/2b19dcf64a30d3105a85f7f3dfefaa0d.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/4459a18283109e013eeac4660dd76011.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/80b790e068e87f47366fbff06c50ca66.jpg"}, {"http://s8.hostingkartinok.com/uploads/images/2016/11/903a75a944661842e5056b4bfceb5004.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/28ee1a26ef9140118474b03db420a410.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/6ce6c50f60097be4f7c2da62dcdac20f.jpg"}, {"http://s8.hostingkartinok.com/uploads/images/2016/11/e1b03ff92e091630544e3eb037cf8fec.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/0515ac1d91f3cfdf1e14dd6ea46c1594.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/e29beb57d4e8f775d2f9ab212e39ee81.jpg"}, {"http://s8.hostingkartinok.com/uploads/images/2016/11/54e41c644bb2a65e69ba7976297ce95d.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/17976d5cc0f98ec8e8fdb10012c07ed5.jpg","http://s8.hostingkartinok.com/uploads/images/2016/11/62933d915933b60b5c91cca14b6fa595.jpg"}};


    PlayService mService;
    boolean mBound = false;
    int composition_id_servise;
    Intent intent;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = this.getArguments();
        if(bundle != null){
            composition_id = bundle.getString("composition_id");
            String music = bundle.getString("music_id");
            music_id = Integer.valueOf(music);
        }
        dbHelper = new DBHelper(getActivity());
        composition_id_servise = Integer.valueOf(composition_id);
        fillData(composition_id);


        Log.d("Fragment 1", "onCreate");



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState){


        intent = new Intent(Fragment1.this.getActivity(), PlayService.class);


        View v = inflater.inflate(R.layout.fragment1, container, false);

        ImageButton next = (ImageButton) v.findViewById(R.id.next);
        ImageButton last = (ImageButton) v.findViewById(R.id.last);

        ImageButton start = (ImageButton) v.findViewById(R.id.start_btn);
        ImageButton stop = (ImageButton) v.findViewById(R.id.stop_btn);

        next.setImageResource(R.mipmap.next);
        last.setImageResource(R.mipmap.last);
        start.setImageResource(R.mipmap.play);
        stop.setImageResource(R.mipmap.stop);

        final TextView text = (TextView) v.findViewById(R.id.music_name);
        final ImageView image_name = (ImageView) v.findViewById(R.id.image_name);


        Category categoty = compositions_song.get(music_id);


        String music_name = categoty.name;
        Integer int_image = categoty.image;
        String s = String.valueOf(int_image);

        text.setText(music_name);
//        image_name.setImageResource(int_image);
        Picasso.with(Fragment1.this.getActivity()).load(compos_url_image[composition_id_servise][music_id]).error(R.mipmap.disk).placeholder(R.mipmap.disk).into(image_name);


        repeatmusic(music_name);

        start.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

                if(!mBound) return;
                mService.get_music_composition(composition_id_servise, music_id);
                getActivity().startService(new Intent(getActivity(),PlayService.class));



            }
        });

        stop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                if(!mBound) return;
                mService.stop();
                getActivity().stopService(new Intent(getActivity(),PlayService.class));


            }
        });


        next.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

                if(!mBound) return;
                if(mService.is_Ready())
                {
                    mService.stop();
                }
                music_id +=1;
                int size_array = compositions_song.size();
                if (music_id < size_array){
                    Category category = compositions_song.get(music_id);
                    String music_name = category.name;
                    Integer int_image = category.image;
                    String s = String.valueOf(int_image);

                    text.setText(music_name);
//                    image_name.setImageResource(int_image);
                    Picasso.with(Fragment1.this.getActivity()).load(compos_url_image[composition_id_servise][music_id]).error(R.mipmap.disk).placeholder(R.mipmap.disk).into(image_name);


                    repeatmusic(music_name);
                }else{
                    music_id = music_id - size_array;
                    Category category = compositions_song.get(music_id);
                    String music_name = category.name;
                    Integer int_image = category.image;
                    String s = String.valueOf(int_image);

                    text.setText(music_name);
//                    image_name.setImageResource(int_image);
                    Picasso.with(Fragment1.this.getActivity()).load(compos_url_image[composition_id_servise][music_id]).error(R.mipmap.disk).placeholder(R.mipmap.disk).into(image_name);


                    repeatmusic(music_name);

                }
                mService.get_music_composition(composition_id_servise, music_id);
                getActivity().startService(new Intent(getActivity(),PlayService.class));

            }
        });

        last.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

                if(!mBound) return;
                if(mService.is_Ready())
                {
                    mService.stop();
                }
                music_id -=1;
                int size_array = compositions_song.size();
                if (music_id >=0){
                    Category category = compositions_song.get(music_id);
                    String music_name = category.name;
                    Integer int_image = category.image;
                    String s = String.valueOf(int_image);

                    text.setText(music_name);
//                    image_name.setImageResource(int_image);
                    Picasso.with(Fragment1.this.getActivity()).load(compos_url_image[composition_id_servise][music_id]).error(R.mipmap.disk).placeholder(R.mipmap.disk).into(image_name);


                    repeatmusic(music_name);

                }else{
                    if(music_id <0 && -music_id<size_array){
                        music_id = music_id+size_array;
                        Category category = compositions_song.get(music_id);
                        String music_name = category.name;
                        Integer int_image = category.image;
                        String s = String.valueOf(int_image);

                        text.setText(music_name);
//                        image_name.setImageResource(int_image);
                        Picasso.with(Fragment1.this.getActivity()).load(compos_url_image[composition_id_servise][music_id]).error(R.mipmap.disk).placeholder(R.mipmap.disk).into(image_name);


                        repeatmusic(music_name);

                    }
                }
                mService.get_music_composition(composition_id_servise, music_id);
                getActivity().startService(new Intent(getActivity(),PlayService.class));
            }
        });

        Log.d("Fragment 1", "onCreateView");

        return v;
    }


    void repeatmusic(String music_name){

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String[] columns=new String[]{"image","category", "repeat"};
        String selection = "name = ?";
        String[] selectionArgs = {music_name};
        Cursor cursor = database.query(DBHelper.TABLE_NAME2, columns, selection, selectionArgs, null, null, null);
        ContentValues cv=new ContentValues();

        if (cursor.moveToNext()) {
            int repeat_index = cursor.getColumnIndex("repeat");

            do {
                int repeat_music = cursor.getInt(repeat_index) + 1;
                cv.put(DBHelper.KEY_REPEAT, repeat_music);
                database.update(DBHelper.TABLE_NAME2, cv, selection, selectionArgs);

            } while (cursor.moveToNext());


        }
        cursor.close();
        dbHelper.close();

    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayService.MyBinder binder = (PlayService.MyBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    void fillData(String id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String[] columns=new String[]{"name","image"};
        String selection = "category = ?";
        String[] selectionArgs = {id};
        Cursor cursor = database.query(DBHelper.TABLE_NAME2, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToNext()) {
            int category_index = cursor.getColumnIndex("name");
            int image_index = cursor.getColumnIndex("image");

            do {

                compositions_song.add(new Category(cursor.getString(category_index), image[cursor.getInt(image_index)]));


            } while (cursor.moveToNext());


        }
        cursor.close();
        dbHelper.close();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Toast.makeText(getActivity(), "FirstFragment.onAttach()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "FirstFragment.onActivityCreated()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "FirstFragment.onStart()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "FirstFragment.onResume()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getActivity(), "FirstFragment.onPause()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getActivity(), "FirstFragment.onStop()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(), "FirstFragment.onDestroyView()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onDestroyView");
    }


    @Override
    public void onDetach() {
        super.onDetach();

        Log.d("Fragment 1", "onDetach");
    }
}
