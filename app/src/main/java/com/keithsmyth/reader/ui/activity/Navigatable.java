package com.keithsmyth.reader.ui.activity;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

public interface Navigatable {

    void startFragment(Fragment fragment);

    void showDialog(DialogFragment dialogFragment, String tag);
}
