package com.kundanapp.Safe_Around;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.TreeMap;

public class customadapterresource extends BaseExpandableListAdapter {
    Context context;
    List<String> states;
    TreeMap<String , List<resource_data>> finaldata;

    public customadapterresource(Context context, List<String> states, TreeMap<String, List<resource_data>> finaldata) {
        this.context = context;
        this.states = states;
        this.finaldata = finaldata;
    }

    @Override
    public int getGroupCount() {
        return states.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return finaldata.get(states.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return states.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return finaldata.get(states.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String pos = (String) getGroup(groupPosition);
        if (convertView ==  null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.resource_parent,null);
        }

        TextView name;

        name = (TextView) convertView.findViewById(R.id.resource_parent);
        name.setText(pos);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final resource_data pos2 = (resource_data) getChild(groupPosition,childPosition);
        if (convertView ==  null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.resource_child,null);        }

        TextView states,name,city,phone,contact;

        states = (TextView) convertView.findViewById(R.id.state_child);
        city = (TextView) convertView.findViewById(R.id.city_child);
        name = (TextView) convertView.findViewById(R.id.name_child);
        phone = (TextView) convertView.findViewById(R.id.phone_child);
        contact = (TextView) convertView.findViewById(R.id.contact_child);
        states.setText(pos2.getState());
        city.setText(pos2.getCity());
        name.setText(pos2.getNameorg());
        phone.setText(pos2.getPhone());
//        contact.setText(pos2.getContact());
        final LinearLayout childlayout= convertView.findViewById(R.id.resource_child_layout);
        childlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(pos2.getContact()));
                context.startActivity(i);
            }
        });

        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
