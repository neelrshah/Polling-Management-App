package com.developer.pollingmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.pollingmanagementsystem.ui.ZonalOfficer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddData extends AppCompatActivity {

    public EditText boothNumberEditText,maleEditText,femaleEditText,totalVotersEditText;
    public TextView totalTextView,percentageTextView,timeEditText,zonalIdEditText;
    public Button submitButton;
    public float voterPercentage;
    public String time,boothNumber,zonalId,totalVoters;
    public int maleCount = 0;
    public int femaleCount = 0;
    public int totalCount = 0;

    //Firebase reference to upload data
    DatabaseReference reference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);



        //GRAB VIEWS

        timeEditText = findViewById(R.id.timeEditText);
        boothNumberEditText =findViewById(R.id.bootNumberEditText);
        maleEditText = findViewById(R.id.maleCountEditText);
        femaleEditText =findViewById(R.id.femaleCountEditText);
        totalTextView =findViewById(R.id.totalCountTextView);
        submitButton = findViewById(R.id.detailSubmitButton);
        zonalIdEditText = findViewById(R.id.zonalIdEditText);
        totalVotersEditText = findViewById(R.id.totalVotersEditText);
        percentageTextView = findViewById(R.id.percentageTextView);

        //SET TIME FROM SYSTEM
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        String currentDateandTime = sdf.format(calender.getTime());
        Log.d("DATE",currentDateandTime);
        timeEditText.setText(currentDateandTime);


        //GRAB TEXT FROM VIEWS

        time = timeEditText.getText().toString();
        boothNumber = boothNumberEditText.getText().toString();
        maleCount = Integer.parseInt(String.valueOf(maleEditText.getText()));
        femaleCount = Integer.parseInt(String.valueOf(femaleEditText.getText()));
        totalVoters = String.valueOf(totalVotersEditText.getText());


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://pollingmanagementsystem-default-rtdb.firebaseio.com/")
                .getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                zonalId = dataSnapshot.child("Users").child(uid).child("zonalOfficierId").getValue(String.class);
                zonalIdEditText.setText(zonalId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Used to automatic change addition when numbers are updated.

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!maleEditText.getText().toString().equals("") && !femaleEditText.getText().toString().equals("")){
                    int temp1 = Integer.parseInt(maleEditText.getText().toString());
                    int temp2 = Integer.parseInt(femaleEditText.getText().toString());
                    totalCount = temp1 + temp2;
                    totalTextView.setText(String.valueOf(totalCount));
                    percentageTextView.setText("0");
                }

                if (!totalVotersEditText.getText().toString().equals("")){
                    int temp1 = Integer.parseInt(maleEditText.getText().toString());
                    int temp2 = Integer.parseInt(femaleEditText.getText().toString());
                    totalCount = temp1 + temp2;
                    totalTextView.setText(String.valueOf(totalCount));
                    float temp3 = Float.parseFloat(totalVotersEditText.getText().toString());
                    float percentage = (totalCount/ temp3)*100;
                    percentageTextView.setText(String.valueOf(percentage));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        maleEditText.addTextChangedListener(textWatcher);
        femaleEditText.addTextChangedListener(textWatcher);
        totalVotersEditText.addTextChangedListener(textWatcher);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

    }

    private void submitData() {
        time = timeEditText.getText().toString();
        boothNumber = boothNumberEditText.getText().toString();
        maleCount = Integer.parseInt(String.valueOf(maleEditText.getText()));
        femaleCount = Integer.parseInt(String.valueOf(femaleEditText.getText()));
        totalCount = Integer.parseInt(String.valueOf(totalTextView.getText()));
        zonalId = zonalIdEditText.getText().toString();
        totalVoters = String.valueOf(totalVotersEditText.getText());
        voterPercentage = Float.parseFloat(String.valueOf(percentageTextView.getText()));

        if(time.isEmpty()){
            timeEditText.setError("Enter Time");
            timeEditText.requestFocus();
            return;
        }

        if(boothNumber.isEmpty()){
            boothNumberEditText.setError("Enter Booth Number");
            boothNumberEditText.requestFocus();
            return;
        }

        if(maleCount == 0){
            maleEditText.setError("Enter Male Count");
            maleEditText.requestFocus();
            return;
        }

        if(femaleCount == 0){
            femaleEditText.setError("Enter Female Count");
            femaleEditText.requestFocus();
            return;
        }

        if(totalVoters.isEmpty()){
            totalVotersEditText.setError("Enter Total Number of Voters");
            totalVotersEditText.requestFocus();
            return;
        }


        reference = FirebaseDatabase.getInstance("https://pollingmanagementsystem-default-rtdb.firebaseio.com/")
                .getReference().child("PollingData");

        DatabaseReference zonalReference;
        zonalReference = reference.child(zonalId);

        PollingDataModel pollingDataModel = new PollingDataModel
                (time,boothNumber,maleCount,femaleCount,totalCount,voterPercentage);
        zonalReference.push().setValue(pollingDataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Data Submitted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddData.this, ZonalOfficer.class));
                finish();

            }
        });
    }

}