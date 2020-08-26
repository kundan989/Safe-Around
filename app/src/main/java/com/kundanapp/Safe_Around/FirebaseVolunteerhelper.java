package com.kundanapp.Safe_Around;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseVolunteerhelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mreference;
    private List<volunteerinfo> cases = new ArrayList<>();

    public interface DataStatus
    {
        void DataIsLoaded(List<volunteerinfo> govtcases, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseVolunteerhelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mreference = mDatabase.getReference("Volunteer");
    }

    public void readcases(final DataStatus dataStatus)
    {
        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cases.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    keys.add(keyNode.getKey());
                    volunteerinfo govtcases = keyNode.getValue(volunteerinfo.class);
                    cases.add(govtcases);
                }
                dataStatus.DataIsLoaded(cases,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}