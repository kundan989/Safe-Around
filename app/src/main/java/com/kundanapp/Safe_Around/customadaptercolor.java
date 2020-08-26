package com.kundanapp.Safe_Around;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.TreeMap;

public class customadaptercolor extends BaseExpandableListAdapter {
    Context context;
//    List<String> states;
    List<colordata_parent> arrayList;
    TreeMap<String , List<colordata_child>> finaldata;

    public customadaptercolor(Context context, List<colordata_parent> arrayList, TreeMap<String, List<colordata_child>> finaldata) {
        this.context = context;
//        this.states=states;
        this.arrayList=arrayList;
        this.finaldata = finaldata;
    }

    @Override
    public int getGroupCount() {
        return arrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return finaldata.get(arrayList.get(groupPosition).getMstate()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return finaldata.get(arrayList.get(groupPosition).getMstate()).get(childPosition);
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
        colordata_parent pos = (colordata_parent) getGroup(groupPosition);
        if (convertView ==  null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.color_parent,null);
        }

        TextView state,recover,death;

        state = (TextView) convertView.findViewById(R.id.statena);
        recover = (TextView) convertView.findViewById(R.id.recoverrate);
        death = (TextView) convertView.findViewById(R.id.deceasedrate);
        state.setText(pos.getMstate());
        recover.setText(pos.getMrecover());
        death.setText(pos.getMdeath());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        colordata_child pos2 = (colordata_child) getChild(groupPosition,childPosition);
        if (convertView ==  null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.color_child,null);        }

        TextView location, code;

        location = (TextView) convertView.findViewById(R.id.location);
        code = (TextView) convertView.findViewById(R.id.color);
        location.setText(pos2.getDistrict());
//        code.setText(pos2.getColor());
        if (pos2.getColor().equals("Red")) {
            code.setTextColor(Color.parseColor("#FF1744"));
            code.setText(pos2.getColor());
        }
        if (pos2.getColor().equals("Orange")) {
            code.setTextColor(Color.parseColor("#FFA500"));
            code.setText(pos2.getColor());
        }
        if (pos2.getColor().equals("Green")) {
            code.setTextColor(Color.parseColor("#388E3C"));
            code.setText(pos2.getColor());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
