package com.developer.pollingmanagementsystem.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.pollingmanagementsystem.PollingDataModel;
import com.developer.pollingmanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.makeText;

public class PrecidingOfficer extends Fragment {

    private PrecidingOfficerViewModel mViewModel;
    public EditText timeEditText,boothNumberEditText,maleEditText,femaleEditText,zonalIdEditText,totalVotersEditText;
    public TextView totalTextView,percentageTextView;
    public Button submitButton;
    public float voterPercentage;
    public String time,boothNumber,zonalId,totalVoters;
    public int maleCount = 0;
    public int femaleCount = 0;
    public int totalCount = 0;

    //Firebase reference to upload data
    DatabaseReference reference ;


    public static PrecidingOfficer newInstance() {
        return new PrecidingOfficer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preciding_officer_fragment, container, false);

        //GRAB VIEWS

        timeEditText = view.findViewById(R.id.timeEditText);
        boothNumberEditText = view.findViewById(R.id.bootNumberEditText);
        maleEditText = view.findViewById(R.id.maleCountEditText);
        femaleEditText = view.findViewById(R.id.femaleCountEditText);
        totalTextView = view.findViewById(R.id.totalCountTextView);
        submitButton = view.findViewById(R.id.detailSubmitButton);
        zonalIdEditText = view.findViewById(R.id.zonalIdEditText);
        totalVotersEditText = view.findViewById(R.id.totalVotersEditText);
        percentageTextView = view.findViewById(R.id.percentageTextView);

        //GRAB TEXT FROM VIEWS

        time = timeEditText.getText().toString();
        boothNumber = boothNumberEditText.getText().toString();
        maleCount = Integer.parseInt(String.valueOf(maleEditText.getText()));
        femaleCount = Integer.parseInt(String.valueOf(femaleEditText.getText()));
        totalVoters = String.valueOf(totalVotersEditText.getText());

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

        return view;
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
                 Toast.makeText(getActivity(), "Data Submitted", Toast.LENGTH_SHORT).show();
             }
         });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PrecidingOfficerViewModel.class);
        // TODO: Use the ViewModel
    }

}

