package com.example.vladbirukov.player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;



public class ActivityThree extends AppCompatActivity {
    String compos_id;
    String music_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_three);


        Intent intent = getIntent();
        compos_id = intent.getStringExtra("compos_id");
        music_id = intent.getStringExtra("music_id");


        Fragment frag1 = new Fragment1();

        Bundle bundle = new Bundle();
        bundle.putString("composition_id", compos_id);
        bundle.putString("music_id", music_id);

        frag1.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment1, frag1);
        ft.commit();

    }

}

