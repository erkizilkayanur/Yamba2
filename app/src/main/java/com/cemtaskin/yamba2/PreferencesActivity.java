package com.cemtaskin.yamba2;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by ctaskin on 25/11/15.
 */
public class PreferencesActivity  extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
