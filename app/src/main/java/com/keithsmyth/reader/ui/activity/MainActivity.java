package com.keithsmyth.reader.ui.activity;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keithsmyth.reader.R;
import com.keithsmyth.reader.ui.fragment.ListFeedsFragment;

public class MainActivity extends AppCompatActivity implements Navigatable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ListFeedsFragment())
                .commit();
        }
    }

    @Override
    public void startFragment(Fragment fragment) {
        final String name = fragment.getClass().getSimpleName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, name)
                .addToBackStack(name)
                .commit();
    }

    @Override
    public void showDialog(DialogFragment dialogFragment, String tag) {
        dialogFragment.show(getSupportFragmentManager(), tag);
    }
}
