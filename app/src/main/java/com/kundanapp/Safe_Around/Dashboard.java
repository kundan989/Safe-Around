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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;


public class Dashboard extends AppCompatActivity  {


    private RequestQueue queue;
    ExpandableListView expandableListView;
    ArrayList<rowitem> arrayList;
    customadapterdistricts adapter;
    TextView confirm, active, recover, deac;
    TreeMap<String, List<rowitem>> listItem;
    TreeSet<String> states = new TreeSet<>();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setTitle("State & District Information");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);
        expandableListView = (ExpandableListView) findViewById(R.id.myListView);
        arrayList = new ArrayList<>();

        confirm = findViewById(R.id.confirm_no);
        active = findViewById(R.id.active_no);
        recover = findViewById(R.id.recovered_no);
        deac = findViewById(R.id.deceased_no);
        listItem = new TreeMap<>();

//        findViewById(R.id.resourceslayout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), Resources.class);
//                startActivity(i);
//            }
//        });


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
                            JSONObject summ = result.getJSONObject(0);
//                            JSONObject summ = summarr.getJSONObject(0);
                            confirm.setText(summ.getString("confirmed"));
                            active.setText(summ.getString("active"));
                            recover.setText(summ.getString("recovered"));
                            deac.setText(summ.getString("deaths"));
                            JSONArray jsonArray = result;
                            for (int i = 1; i < jsonArray.length(); i++) {
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                String location = json_data.getString("state");
                                String totalcase = json_data.getString("confirmed");
                                String recovered = json_data.getString("recovered");
                                String deaths = json_data.getString("deaths");

                                rowitem model = new rowitem();
                                model.setLocation(location);
                                model.setTotalcase(totalcase);
                                model.setRecovered(recovered);
                                model.setDeaths(deaths);
                                if(!location.equals("Daman and Diu"))
                                    arrayList.add(model);
//                                Collections.sort(arrayList, new Comparator<rowitem>() {
//
//                                    /* This comparator will sort objects alphabetically. */
//
//                                    @Override
//                                    public int compare(rowitem a1, rowitem a2) {
//
//                                        // String implements Comparable
//                                        return (a1.getLocation()).compareTo(a2.getLocation());
//                                    }
//                                });
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Dashboard.this, "Please Check Your Internet Connection or try Restarting the App", Toast.LENGTH_LONG).show();
            }
        });


        String url2 = "https://api.covid19india.org/v2/state_district_wise.json";
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray result2 = new JSONArray(response);
                            for (int i = 0; i < result2.length(); i++) {
                                JSONArray json_data = result2.getJSONObject(i).getJSONArray("districtData");
                                List<rowitem> rowitems = new ArrayList<>();
                                for (int j = 0; j < json_data.length(); j++) {
                                    JSONObject json_data_dist = json_data.getJSONObject(j);
                                    String location = json_data_dist.getString("district");
                                    String totalcase = json_data_dist.getString("confirmed");
                                    String recovered = json_data_dist.getString("recovered");
                                    String deaths = json_data_dist.getString("deceased");

                                    rowitem model = new rowitem();
                                    model.setLocation(location);
                                    model.setTotalcase(totalcase);
                                    model.setRecovered(recovered);
                                    model.setDeaths(deaths);
                                    rowitems.add(model);
                                }
                                String sname=result2.getJSONObject(i).getString("state");
//                                if(sname.equals("Dadra and Nagar Haveli and Daman and Diu"))
//                                {sname="Dadar Nagar Haveli";}
//                                else if(sname.equals("Telangana"))
//                                {sname="Telengana";}
                                states.add(sname);
                                listItem.put(sname, rowitems);
//                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Dashboard.this, "Please Check Your Internet Connection or try Restarting the App", Toast.LENGTH_LONG).show();
            }
        });

        adapter = new customadapterdistricts(this,states,arrayList,listItem);
        expandableListView.setAdapter(adapter);

        // Add the request to the RequestQueue.
        queue.add(stringRequest2);
        queue.add(stringRequest);
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