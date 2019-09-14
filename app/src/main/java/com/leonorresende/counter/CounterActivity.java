package com.leonorresende.counter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class CounterActivity extends AppCompatActivity {

    String title;
    int number;

    TextView counterTitleTextView;
    TextView numberTextView;

    SQLiteDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        myDatabase = this.openOrCreateDatabase("Counters", MODE_PRIVATE, null);

        counterTitleTextView = findViewById(R.id.counterTitleTextView);
        numberTextView = findViewById(R.id.numberTextView);


        Intent intent = getIntent();
        number = intent.getIntExtra("number", -1);
        title = intent.getStringExtra("title");
        if (number != -1 && title != null && title != "") {
            counterTitleTextView.setText(title);
            numberTextView.setText(String.valueOf(number));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_counter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.edit_title) {
            editTitle();
            return true;
        }
        else if (id == R.id.delete_counter) {
            deleteCounter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveCounterNumber(int newNumber) {
        String sqlStatement = "UPDATE countersData SET number = ? WHERE title = ? AND number = ?";
        SQLiteStatement statement = myDatabase.compileStatement(sqlStatement);
        statement.bindLong(1, newNumber);
        statement.bindString(2, title);
        statement.bindLong(3, number);
        statement.execute();

        number = newNumber;
    }

    public void incrementCounter(View view) {
        int newNumber = number + 1;
        numberTextView.setText(String.valueOf(newNumber));
        saveCounterNumber(newNumber);
    }

    public void decrementCounter(View view) {
        int newNumber = number - 1;
        numberTextView.setText(String.valueOf(newNumber));
        saveCounterNumber(newNumber);
    }

    public void resetCounter(View view) {
        int newNumber = 0;
        numberTextView.setText(String.valueOf(newNumber));
        saveCounterNumber(newNumber);
    }

    private void deleteCounter() {
        String sqlStatement = "DELETE FROM countersData WHERE title = ? AND number = ?";
        SQLiteStatement statement = myDatabase.compileStatement(sqlStatement);
        statement.bindString(1, title);
        statement.bindLong(2, number);
        statement.execute();

        finish();
    }

    private void editTitle() {
        final EditText titleInput = new EditText(CounterActivity.this);
        titleInput.setText(title);
        TextInputLayout textInputLayout = new TextInputLayout(CounterActivity.this);

        FrameLayout container = new FrameLayout(CounterActivity.this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int left_margin = dpToPx(20, getResources());
        int top_margin = dpToPx(0, getResources());
        int right_margin = dpToPx(20, getResources());
        int bottom_margin = dpToPx(4, getResources());
        params.setMargins(left_margin, top_margin, right_margin, bottom_margin);

        textInputLayout.setLayoutParams(params);

        textInputLayout.addView(titleInput);
        container.addView(textInputLayout);



        new AlertDialog.Builder(this)
                .setTitle("Edit title!")
                .setView(container)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newTitle = titleInput.getText().toString();

                        String sqlStatement = "UPDATE countersData SET title = ? WHERE title = ? AND number = ?";
                        SQLiteStatement statement = myDatabase.compileStatement(sqlStatement);
                        statement.bindString(1, newTitle);
                        statement.bindString(2, title);
                        statement.bindLong(3, number);
                        statement.execute();

                        title = newTitle;
                        counterTitleTextView.setText(title);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

}
