package in.galaxyofandroid.spinerdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import in.galaxyofandroid.spinnerdialog.R;

public class SpinnerDialog {

    private final Activity context;

    private final ArrayList<String> itemIds = new ArrayList<>();
    private final ArrayList<String> itemValues = new ArrayList<>();
    private final String dTitle;
    private final int windowAnimationsStyle;

    private OnSpinnerItemClickListener onSpinnerItemClickListener;
    private AlertDialog alertDialog;
    private int pos;

    public SpinnerDialog(Activity activity, ArrayList<SpinnerDialogItem> items, String dialogTitle) {
        populateDataSet(items);
        this.context = activity;
        this.dTitle = dialogTitle;
        this.windowAnimationsStyle = 0;
    }

    public SpinnerDialog(Activity activity, ArrayList<SpinnerDialogItem> items, String dialogTitle, int windowAnimationsStyle) {
        populateDataSet(items);
        this.context = activity;
        this.dTitle = dialogTitle;
        this.windowAnimationsStyle = windowAnimationsStyle;
    }

    private void populateDataSet(ArrayList<SpinnerDialogItem> items) {
        for (SpinnerDialogItem item : items) {
            itemIds.add(item.getId());
            itemValues.add(item.getValue());
        }
    }

    public void bindOnSpinnerListener(OnSpinnerItemClickListener listener) {
        this.onSpinnerItemClickListener = listener;
    }

    public void showSpinnerDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        View v = context.getLayoutInflater().inflate(R.layout.dialog_layout, null);
        TextView rippleViewClose = (TextView) v.findViewById(R.id.close);
        TextView title = (TextView) v.findViewById(R.id.spinnerTitle);
        title.setText(dTitle);
        final ListView listView = (ListView) v.findViewById(R.id.list);
        final EditText searchBox = (EditText) v.findViewById(R.id.searchBox);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.items_view, itemValues);
        listView.setAdapter(adapter);
        adb.setView(v);
        alertDialog = adb.create();
        alertDialog.getWindow().getAttributes().windowAnimations = windowAnimationsStyle;
        alertDialog.setCancelable(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t = (TextView) view.findViewById(R.id.text1);
                for (int j = 0; j < itemValues.size(); j++) {
                    if (t.getText().toString().equalsIgnoreCase(itemValues.get(j))) {
                        pos = j;
                    }
                }
                onSpinnerItemClickListener.onClick(itemIds.get(pos), t.getText().toString(), pos);
                alertDialog.dismiss();
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(searchBox.getText().toString());
            }
        });

        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}