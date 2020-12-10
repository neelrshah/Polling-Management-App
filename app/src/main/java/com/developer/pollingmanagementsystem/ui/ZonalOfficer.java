package com.developer.pollingmanagementsystem.ui;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.pollingmanagementsystem.AddData;
import com.developer.pollingmanagementsystem.PollingDataAdapter;
import com.developer.pollingmanagementsystem.PollingDataModel;
import com.developer.pollingmanagementsystem.R;
import com.developer.pollingmanagementsystem.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ZonalOfficer extends Fragment {

    private ZonalOfficerViewModel mViewModel;
    public RecyclerView recyclerView;
    List<PollingDataModel> pollingData = new ArrayList<>();
    PollingDataAdapter pollingDataAdapter ;
    public String zonalId;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid;
    FloatingActionButton floatingActionButton;



    public static ZonalOfficer newInstance() {
        return new ZonalOfficer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.zonal_officer_fragment, container, false);
        recyclerView = view.findViewById(R.id.zonalRecyclerView);

        uid = user.getUid();
        pollingDataAdapter = new PollingDataAdapter(pollingData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pollingDataAdapter);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), AddData.class);
                startActivity(intent);
            }
        });

        // FETCH DATA FROM DATABASE

        DatabaseReference reference = FirebaseDatabase.getInstance("https://pollingmanagementsystem-default-rtdb.firebaseio.com/")
                .getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                zonalId = dataSnapshot.child("Users").child(uid).child("zonalOfficierId").getValue(String.class);
                Log.d("ZONAL",zonalId);
                DatabaseReference pollingDataReference = reference.child("PollingData");
                DatabaseReference dataReference = (DatabaseReference) pollingDataReference.child(zonalId);
                dataReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pollingData.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            pollingData.add((PollingDataModel) snapshot.getValue(PollingDataModel.class));
                        }
                        pollingDataAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ZonalOfficerViewModel.class);
        // TODO: Use the ViewModel
    }

}