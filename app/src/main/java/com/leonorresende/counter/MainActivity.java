package com.leonorresende.counter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView countersListView;
    ArrayList<AppCounter> counters;
    CustomAdapter adapter;

    private static Context appContext;

    SQLiteDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.your_counters);

        appContext = this;

        myDatabase = this.openOrCreateDatabase("Counters", MODE_PRIVATE, null);

        countersListView = (ListView) findViewById(R.id.list);

        counters = new ArrayList<>();

        adapter = new CustomAdapter(this, counters);
        countersListView.setAdapter(adapter);

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS countersData (id INTEGER PRIMARY KEY, title VARCHAR, number INTEGER)");

        loadCounters();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        loadCounters();
    }

    private void loadCounters() {

        Cursor c = myDatabase.rawQuery("SELECT * FROM countersData", null);
        int titleIndex = c.getColumnIndex("title");
        int numberIndex = c.getColumnIndex("number");

        if (c.moveToFirst()) {
            counters.clear();

            do {
                AppCounter newCounter = new AppCounter(c.getString(titleIndex), c.getInt(numberIndex));
                counters.add(newCounter);
            }
            while (c.moveToNext());

            adapter.notifyDataSetChanged();
        }

    }

    private int makeNewCounter(String counterTitle, int counterNumber) {
        AppCounter newCounter = new AppCounter(counterTitle, counterNumber);
        counters.add(newCounter);
        adapter.notifyDataSetChanged();

        String sqlStatement = "INSERT INTO countersData (title, number) VALUES (?, ?)";
        SQLiteStatement statement = myDatabase.compileStatement(sqlStatement);
        statement.bindString(1, counterTitle);
        statement.bindLong(2, counterNumber);
        statement.execute();

        return counters.size() - 1;
    }

    public static void goToCounter(String title, int number) {
        Intent intent = new Intent(appContext, CounterActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("number", number);
        appContext.startActivity(intent);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            final EditText titleInput = new EditText(MainActivity.this);
            titleInput.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        String newTitle = titleInput.getText().toString();
                        if (newTitle != null && !newTitle.equals("")) {
                            makeNewCounter(newTitle, 0);
                            goToCounter(newTitle, 0);
                        }
                        return true;
                    }
                    return false;
                }
            });

            TextInputLayout textInputLayout = new TextInputLayout(MainActivity.this);

            FrameLayout container = new FrameLayout(MainActivity.this);
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
                    .setTitle("New Counter")
                    .setMessage("Give it a title!")
                    .setView(container)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String newTitle = titleInput.getText().toString();
                            if (newTitle != null && !newTitle.equals("")) {
                                makeNewCounter(newTitle, 0);
                                goToCounter(newTitle, 0);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
}
