package com.romide.terminal.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.romide.terminal.R;
import com.romide.terminal.adapter.PasteListAdapter;
import com.romide.terminal.common.WriteScriptInterface;

public class PasteHistoryDialog extends DialogFragment {
    private WriteScriptInterface writeScriptInstance;
    private Context mContext;

    public PasteHistoryDialog() {
        super();
    }

    public static PasteHistoryDialog getInstance(WriteScriptInterface writeScript) {
        PasteHistoryDialog dialog = new PasteHistoryDialog();
        dialog.writeScriptInstance = writeScript;

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(mContext, R.style.FullScreenDialog);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.dialog_paste_hisotry, null);
        dialog.setContentView(view);

        PasteListAdapter pasteListAdapter = new PasteListAdapter(this.mContext, new PasteHistory(mContext).listHistory(), writeScriptInstance, dialog);
        ListView listView = view.findViewById(R.id.memo_items);
        listView.setAdapter(pasteListAdapter);

        view.findViewById(R.id.memo_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PasteHistory(mContext).clearHistory();
                dialog.dismiss();
            }
        });
        View listViewHeader = layoutInflater.inflate(R.layout.paste_list_header, null);
        listView.addHeaderView(listViewHeader);

        return dialog;
    }

    // R.layout.dialog_paste_hisotry
}
