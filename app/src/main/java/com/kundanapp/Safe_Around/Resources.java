package com.kundanapp.Safe_Around;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ExpandableListView;
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

public class Resources extends AppCompatActivity {

    private RequestQueue queue;
    ExpandableListView expandableListView;
    ArrayList<String> arrayList;
    customadapterresource adapter;
    TreeMap<String, List<resource_data>> listItem;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        setTitle("");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(this);
        expandableListView = (ExpandableListView) findViewById(R.id.myresourceListView);
        arrayList = new ArrayList<>();
        listItem = new TreeMap<>();

        String url = "https://api.covid19india.org/resources/resources.json";
        pDialog = ProgressDialog.show(this, "Loading",
                "Please wait while we fetch latest information for you", true);
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray result = new JSONObject(response).getJSONArray("resources");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json_data = result.getJSONObject(i);
                                String category = json_data.getString("category");
                                String State = json_data.getString("state");
                                String City = json_data.getString("city");
                                String Nameorg = json_data.getString("nameoftheorganisation");
                                String phone = json_data.getString("phonenumber");
                                String contact = json_data.getString("contact");
                                String desc = json_data.getString("descriptionandorserviceprovided");

                                resource_data model = new resource_data();
                                model.setState(State);
                                model.setCity(City);
                                model.setNameorg(Nameorg);
                                model.setPhone(phone);
                                model.setContact(contact);
                                model.setDescrip(desc);
                                if(listItem.containsKey(category))
                                {
                                    List<resource_data> temp=listItem.get(category);
                                    temp.add(model);
                                    listItem.put(category,temp);
                                }
                                else {
                                    arrayList.add(category);
                                    List<resource_data> temp=new ArrayList<>();
                                    temp.add(model);
                                    listItem.put(category, temp);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Resources.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Resources.this, "Server is not responding (Covid-19 Tracker)", Toast.LENGTH_LONG).show();
            }
        });

        adapter = new customadapterresource(this,arrayList,listItem);
        expandableListView.setAdapter(adapter);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
