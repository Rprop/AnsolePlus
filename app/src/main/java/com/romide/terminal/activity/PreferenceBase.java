/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.romide.terminal.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.romide.terminal.R;

public class PreferenceBase extends ActivityBase {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preference_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        }
    }

    protected void setPreference(int resId) {
        InnerPreference fragment = new InnerPreference();
        Bundle args = new Bundle();
        args.putInt(InnerPreference.ARG_RES, resId);

        fragment.setArguments(args);

        getFragmentManager().beginTransaction()
                .add(R.id.preference_holder, fragment)
                .commit();
    }

    public static class InnerPreference extends PreferenceFragment {
        public static final String ARG_RES = "resId";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();
            int resId = args.getInt(ARG_RES);

            addPreferencesFromResource(resId);
        }
    }
}
