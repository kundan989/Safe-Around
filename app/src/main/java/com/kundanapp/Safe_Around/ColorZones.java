package com.kundanapp.Safe_Around;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;



public class ColorZones extends AppCompatActivity {

    private static DecimalFormat df = new DecimalFormat("0.00");

    private RequestQueue queue;
    ExpandableListView expandableListView;
    ArrayList<colordata_parent> arrayList;
    customadaptercolor adapter;
    TreeMap<String, List<colordata_child>> listItem;
    TextView reddistric,orangedistric,greendistric;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_zones);

        setTitle("Color Wise Zone Information");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);
        expandableListView = (ExpandableListView) findViewById(R.id.mycolorListView);
        arrayList = new ArrayList<>();
        listItem = new TreeMap<>();

        reddistric = findViewById(R.id.redzoneid);
        orangedistric = findViewById(R.id.orangezoneid);
        greendistric = findViewById(R.id.greenzoneid);
//
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("colorzones");
//
//// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String redinf= String.valueOf(dataSnapshot.child("Red").getValue());
                String orangeinf= String.valueOf(dataSnapshot.child("Orange").getValue());
                String greeninf= String.valueOf(dataSnapshot.child("Green").getValue());
                reddistric.setText(redinf);
                orangedistric.setText(orangeinf);
                greendistric.setText(greeninf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        String url = "https://api.covid19india.org/data.json";
        pDialog = ProgressDialog.show(this, "Loading",
                "Please wait while we fetch latest information for you", false);
        pDialog.setCancelable(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
//                        pDialog.hide();
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray result = new JSONObject(response).getJSONArray("statewise");
                            JSONArray jsonArray = result;
                            for (int i = 1; i < jsonArray.length(); i++) {
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                String location = json_data.getString("state");
                                float totalcase = json_data.getInt("confirmed");
                                float recovered = json_data.getInt("recovered");
                                float deaths = json_data.getInt("deaths");

                                colordata_parent model = new colordata_parent();
                                model.setMstate(location);
                                if(totalcase==0)
                                {
                                    model.setMrecover("0");
                                    model.setMdeath("0");
                                }
                                else
                                {
                                    model.setMrecover(String.valueOf(df.format(recovered/totalcase*100.0))+"%");
                                    model.setMdeath(String.valueOf(df.format(deaths/totalcase*100))+"%");
                                }
                                if(!location.equals("State Unassigned"))
                                    arrayList.add(model);
                                Collections.sort(arrayList, new Comparator<colordata_parent>() {

                                    /* This comparator will sort objects alphabetically. */

                                    @Override
                                    public int compare(colordata_parent a1, colordata_parent a2) {

                                        // String implements Comparable
                                        return (a1.getMstate()).compareTo(a2.getMstate());
                                    }
                                });

                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ColorZones.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ColorZones.this, "Please Check Your Internet Connection or try Restarting the App", Toast.LENGTH_LONG).show();
            }
        });

        String url2 = "https://api.covid19india.org/zones.json";
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject result2 = new JSONObject(response);
                            JSONArray json_data = result2.getJSONArray("zones");
                            for (int i = 0; i < json_data.length(); i++) {
                                    JSONObject json_data_dist = json_data.getJSONObject(i);
                                    String location = json_data_dist.getString("state");
                                    String district = json_data_dist.getString("district");
                                    String zone = json_data_dist.getString("zone");

                                    colordata_child model = new colordata_child();
                                    model.setDistrict(district);
                                    model.setColor(zone);
                                if(listItem.containsKey(location))
                                {
                                    List<colordata_child> temp=listItem.get(location);
                                    temp.add(model);
                                    listItem.put(location,temp);
                                }
                                else {
//                                    arrayList.add(location);
                                    List<colordata_child> temp=new ArrayList<>();
                                    temp.add(model);
                                    listItem.put(location, temp);
                                }
//                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ColorZones.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ColorZones.this, "Please Check Your Internet Connection or try Restarting the App", Toast.LENGTH_LONG).show();
            }
        });

        adapter = new customadaptercolor(this,arrayList,listItem);
        expandableListView.setAdapter(adapter);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.add(stringRequest2);
    }

    //
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.help, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                "mailto","kundank102@gmail.com", null));
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion/Feedback");
////        intent.putExtra(Intent.EXTRA_TEXT, message);
//        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
//            finish();
//            return true;
//    }

}