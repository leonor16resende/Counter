package com.leonorresende.counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    private void saveCounterNumber(int newNumber) {
        String sqlStatement = "UPDATE countersData SET number = ? WHERE title = ? AND number = ?";
        SQLiteStatement statement = myDatabase.compileStatement(sqlStatement);
        statement.bindLong(1, newNumber);
        statement.bindString(2, title);
        statement.bindLong(3, number);
        statement.execute();

        number = newNumber;
    }

    private int getCounterId(String title, int number) {
        Cursor c = myDatabase.rawQuery("SELECT * FROM countersData WHERE title = ? AND number = ? LIMIT 1", new String[] {title, String.valueOf(number)});
        int idIndex = c.getColumnIndex("id");

        if (c.moveToFirst()) {
            return c.getInt(idIndex);
        }
        return -1;
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

}
