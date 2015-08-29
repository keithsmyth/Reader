package com.keithsmyth.reader.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keithsmyth.reader.R;
import com.keithsmyth.reader.ui.fragment.RssFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, Fragment.instantiate(this, RssFragment.class.getName()))
                .commit();
        }
    }
}
