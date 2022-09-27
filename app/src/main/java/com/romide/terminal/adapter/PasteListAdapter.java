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

package com.romide.terminal.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.romide.terminal.R;
import com.romide.terminal.activity.PasteHistory;
import com.romide.terminal.common.WriteScriptInterface;
import com.romide.terminal.emulatorview.UpdateCallback;
import com.romide.terminal.emulatorview.compat.ClipboardManagerCompat;
import com.romide.terminal.emulatorview.compat.ClipboardManagerCompatFactory;
import java.util.ArrayList;
import java.util.Map;


public class PasteListAdapter extends BaseAdapter implements UpdateCallback {
    private Context ctx;
    private ArrayList<History> mHistory;
    private WriteScriptInterface writeScriptInstance;
    private Dialog mDialog;

    private class History {
        String time;
        String script;
    }

    public PasteListAdapter(Context ctx, Map<String, ?> history, WriteScriptInterface writeScriptInstance, Dialog dialog) {
        this.ctx = ctx;
        this.writeScriptInstance = writeScriptInstance;
        this.mDialog = dialog;
        setSessions(history);
    }

    public void setSessions(final Map<String, ?> history) {
        ArrayList<History> histories = new ArrayList<History>();
        for (final String key : history.keySet()) {
            History item = new History();
            item.time = key;
            item.script = (String) history.get(key);
            histories.add(item);
        }
        mHistory = histories;
        onUpdate();
    }

    @Override
    public int getCount() {
        return mHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, ViewGroup parent) {
        View child = LayoutInflater.from(ctx).inflate(R.layout.paste_list_item, parent, false);
        View close = child.findViewById(R.id.memo_list_delete);
        View copy = child.findViewById(R.id.memo_list_copy);

        TextView script = child.findViewById(R.id.memo_list_script);
        TextView time = child.findViewById(R.id.memo_list_time);

        History item = (History) getItem(position);
        script.setText(item.script);
        time.setText(item.time);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                History item = (History) getItem(position);
                new PasteHistory(view.getContext()).removeHistory(item.time);
                mHistory.remove(position);
                notifyDataSetChanged();
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                History item = (History) getItem(position);
                Context context = view.getContext();
                new PasteHistory(context).removeHistory(item.time);
                ClipboardManagerCompat clip = ClipboardManagerCompatFactory.getManager(view.getContext().getApplicationContext());
                clip.setText(item.script);
                Toast.makeText(context, "OK!", Toast.LENGTH_SHORT).show();
            }
        });

        script.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                History item = (History) getItem(position);
                writeScript(item.script);
            }
        });

        return child;
    }

    private void writeScript(String script) {
        writeScriptInstance.writeScript(script);
        mDialog.dismiss();
    }

    @Override
    public void onUpdate() {
        notifyDataSetChanged();
    }
}
