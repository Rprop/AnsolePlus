/*
 * Copyright (C) 2011 Steven Luo
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

package com.romide.terminal.compat;

import android.app.ActionBar;
import android.widget.SpinnerAdapter;

/**
 * Provides ActionBar APIs.
 */
public abstract class ActionBarCompat {
    public static final int NAVIGATION_MODE_STANDARD = 0;
    public static final int NAVIGATION_MODE_LIST = 1;
    public static final int NAVIGATION_MODE_TABS = 2;
    public static final int DISPLAY_USE_LOGO = 1;
    public static final int DISPLAY_SHOW_HOME = 2;
    public static final int DISPLAY_HOME_AS_UP = 4;
    public static final int DISPLAY_SHOW_TITLE = 8;
    public static final int DISPLAY_SHOW_CUSTOM = 16;

    // Provides android.R.id.home from API 11 and up
    public static final int ID_HOME = 0x0102002c;

    public static ActionBarCompat wrap(Object actionBar) {
        if (actionBar != null) {
            if (AndroidCompat.SDK >= 11) {
                return new ActionBarApi11OrLater(actionBar);
            }
        }
        return null;
    }

    public abstract int getDisplayOptions();

    public abstract void setDisplayOptions(int options);

    public abstract int getHeight();

    public abstract int getNavigationItemCount();

    public abstract int getNavigationMode();

    public abstract void setNavigationMode(int mode);

    public abstract int getSelectedNavigationIndex();

    public abstract CharSequence getTitle();

    public abstract void setTitle(int resId);

    public abstract void setTitle(CharSequence title);

    public abstract void hide();

    public abstract boolean isShowing();

    public abstract void setDisplayOptions(int options, int mask);

    public abstract void setListNavigationCallbacks(SpinnerAdapter adapter, OnNavigationListener callback);

    public abstract void setSelectedNavigationItem(int position);

    public abstract void show();

    public interface OnNavigationListener {
        public abstract boolean onNavigationItemSelected(int position, long id);
    }
}

class ActionBarApi11OrLater extends ActionBarCompat {
    private ActionBar bar;

    ActionBarApi11OrLater(Object bar) {
        this.bar = (ActionBar) bar;
    }

    @SuppressWarnings("deprecation")
    private ActionBar.OnNavigationListener wrapOnNavigationCallback(OnNavigationListener callback) {
        final OnNavigationListener cb = callback;
        return new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int position, long id) {
                return cb.onNavigationItemSelected(position, id);
            }
        };
    }

    @Override
    public int getDisplayOptions() {
        return bar.getDisplayOptions();
    }

    @Override
    public void setDisplayOptions(int options) {
        bar.setDisplayOptions(options);
    }

    @Override
    public int getHeight() {
        return bar.getHeight();
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getNavigationItemCount() {
        return bar.getNavigationItemCount();
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getNavigationMode() {
        return bar.getNavigationMode();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setNavigationMode(int mode) {
        bar.setNavigationMode(mode);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getSelectedNavigationIndex() {
        return bar.getSelectedNavigationIndex();
    }

    @Override
    public CharSequence getTitle() {
        return bar.getTitle();
    }

    @Override
    public void setTitle(int resId) {
        bar.setTitle(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        bar.setTitle(title);
    }

    @Override
    public void hide() {
        bar.hide();
    }

    @Override
    public boolean isShowing() {
        return bar.isShowing();
    }

    @Override
    public void setDisplayOptions(int options, int mask) {
        bar.setDisplayOptions(options, mask);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setListNavigationCallbacks(SpinnerAdapter adapter, OnNavigationListener callback) {
        bar.setListNavigationCallbacks(adapter, wrapOnNavigationCallback(callback));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setSelectedNavigationItem(int position) {
        bar.setSelectedNavigationItem(position);
    }

    @Override
    public void show() {
        bar.show();
    }
}
