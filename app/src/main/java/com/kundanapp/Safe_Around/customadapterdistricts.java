package com.kundanapp.Safe_Around;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class customadapterdistricts extends BaseExpandableListAdapter {
    Context context;
    TreeSet<String> states;
    List<rowitem> arrayList;
    TreeMap<String , List<rowitem>> finaldata;

    public customadapterdistricts(Context context, TreeSet<String> states, List<rowitem> arrayList, TreeMap<String, List<rowitem>> finaldata) {
        this.context = context;
        this.states=states;
        this.arrayList = arrayList;
        this.finaldata = finaldata;
    }

    @Override
    public int getGroupCount() {
        return arrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
            return finaldata.get(arrayList.get(groupPosition).getLocation()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return finaldata.get(arrayList.get(groupPosition).getLocation()).get(childPosition);
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
        rowitem pos = (rowitem) getGroup(groupPosition);
        if (convertView ==  null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row,null);
        }

        TextView location, totalcase, recovered, deaths;

        location = (TextView) convertView.findViewById(R.id.location);
        totalcase = (TextView) convertView.findViewById(R.id.cases);
        recovered = (TextView) convertView.findViewById(R.id.healthy);
        deaths = (TextView) convertView.findViewById(R.id.deaths);
        location.setText(pos.getLocation());
        totalcase.setText(pos.getTotalcase());
        recovered.setText(pos.getRecovered());
        deaths.setText(pos.getDeaths());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        rowitem pos2 = (rowitem) getChild(groupPosition,childPosition);
        if (convertView ==  null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.district,null);        }

        TextView location, totalcase, recovered, deaths;

        location = (TextView) convertView.findViewById(R.id.location_child);
        totalcase = (TextView) convertView.findViewById(R.id.cases_child);
        recovered = (TextView) convertView.findViewById(R.id.healthy_child);
        deaths = (TextView) convertView.findViewById(R.id.deaths_child);
        location.setText(pos2.getLocation());
        totalcase.setText(pos2.getTotalcase());
        recovered.setText(pos2.getRecovered());
        deaths.setText(pos2.getDeaths());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
