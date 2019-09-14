package com.leonorresende.counter;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView countersListView;
    ArrayList<AppCounter> counters;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.your_counters);

        countersListView = (ListView) findViewById(R.id.list);
        counters = new ArrayList<AppCounter>();
        counters.add(new AppCounter("first counter", 0));

        adapter = new CustomAdapter(this, counters);
        countersListView.setAdapter(adapter);

        //countersListView.setOnItemClickListener(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    private int makeNewCounter(String counterTitle, int counterNumber) {
        AppCounter newCounter = new AppCounter(counterTitle, counterNumber);
        counters.add(newCounter);
        adapter.notifyDataSetChanged();
        return counters.size() - 1;
    }

    public void goToCounter(int index) {
        String title = counters.get(index).getTitle();
        int number = counters.get(index).getNumber();
        Toast.makeText(this, "Title: " + title + ", Number: " + number, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            final EditText titleInput = new EditText(MainActivity.this);

            new AlertDialog.Builder(this)
                    .setTitle("New Counter")
                    .setMessage("Give it a title")
                    .setView(titleInput)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int index = makeNewCounter(titleInput.getText().toString(), 0);
                            goToCounter(index);

                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    /*@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("Item clicked", "NO. " + i);
        goToCounter(i);
    }*/

}
