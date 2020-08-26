package com.kundanapp.Safe_Around;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
  private FirebaseDatabase mDatabase;
  private DatabaseReference mreference;
  private List<govtcase> cases = new ArrayList<>();

  public interface DataStatus
  {
    void DataIsLoaded(List<govtcase> govtcases, List<String> keys);
    void DataIsInserted();
    void DataIsUpdated();
    void DataIsDeleted();
  }

  public FirebaseDatabaseHelper() {
    mDatabase = FirebaseDatabase.getInstance();
    mreference = mDatabase.getReference("govtcases");
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
          govtcase govtcases = keyNode.getValue(govtcase.class);
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