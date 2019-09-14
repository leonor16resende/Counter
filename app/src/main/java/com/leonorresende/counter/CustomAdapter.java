package com.leonorresende.counter;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<AppCounter> {
    private ArrayList<AppCounter> countersData = new ArrayList<>();
    Context thisContext;
    public CustomAdapter(Context context, ArrayList<AppCounter> list) {
        super(context, R.layout.row_counter, list);
        thisContext = context;
        countersData = list;
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(thisContext).inflate(R.layout.row_counter, parent,false);


        final AppCounter currentCounter = countersData.get(position);
        TextView titleTextView = listItem.findViewById(R.id.titleTextView);
        titleTextView.setText(currentCounter.getTitle());

        Button numberButton = listItem.findViewById(R.id.numberButton);
        numberButton.setText(String.valueOf(currentCounter.getNumber()));

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.goToCounter(currentCounter.getTitle(), currentCounter.getNumber());
            }
        });


        return listItem;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        super.registerDataSetObserver(dataSetObserver);

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        super.unregisterDataSetObserver(dataSetObserver);
    }
}
