package com.kundanapp.Safe_Around;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class volunteer extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    SharedPreferences pref;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        setTitle("Volunteers");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.volunteerlistview);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        context = getApplicationContext();
        new FirebaseVolunteerhelper().readcases(new FirebaseVolunteerhelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<volunteerinfo> govtcases, List<String> keys) {
                new Recyclerview_config().setConfig(mRecyclerView,volunteer.this,govtcases,keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.volunteermenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.registermenu) {
            if(pref.getBoolean("volunteer",false))
            {
                Toast.makeText(context, "To Prevent any False Information We allow One Volunteer From One Device. Register from another Device!", Toast.LENGTH_LONG).show();
            }
            else {
                Intent i = new Intent(getApplicationContext(), registervolunteer.class);
                startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
