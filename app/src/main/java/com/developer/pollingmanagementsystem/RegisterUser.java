package com.developer.pollingmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {

    public EditText nameEditText, emailEditText, contactEditText, pollingStationIdEditText, passwordEditText,zonalOfficierIdTextView;
    public Spinner officerPositionSpinner;
    public Button registerButton;
    public ProgressBar progressBar;
    public Spinner spinnerPosition;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        spinnerPosition = findViewById(R.id.spinner);
        nameEditText = findViewById(R.id.fullNameTextView);
        emailEditText = findViewById(R.id.emailTextView);
        contactEditText = findViewById(R.id.contactTextView);
        pollingStationIdEditText = findViewById(R.id.pollingStationIdTextView);
        passwordEditText = findViewById(R.id.passwordTextView);
        zonalOfficierIdTextView = findViewById(R.id.registerZonalIdEditText);
        officerPositionSpinner = findViewById(R.id.spinner);
        registerButton = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.registerProgressBar);

        //CODE FOR SPINNER

        String[] positions = getResources().getStringArray(R.array.position);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,positions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPosition.setAdapter(adapter);

        //initize database reference

        databaseReference = FirebaseDatabase.getInstance("https://pollingmanagementsystem-default-rtdb.firebaseio.com/").getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }


    private void registerUser() {

        String email = emailEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String contact  = contactEditText.getText().toString();
        String stationId = pollingStationIdEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String officerPosition = officerPositionSpinner.getSelectedItem().toString();
        String zonalId = zonalOfficierIdTextView.getText().toString();

        if(name.isEmpty()){
            nameEditText.setError("Enter Full Name");
            nameEditText.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailEditText.setError("Enter E-mail");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Enter valid E-mail");
            emailEditText.requestFocus();
            return;
        }

        if(contact.isEmpty()){
            contactEditText.setError("Enter contact no");
            contactEditText.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(contact).matches()){
            emailEditText.setError("Enter valid Contact No");
            emailEditText.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordEditText.setError("Enter Password");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6){
            passwordEditText.setError("Minimum password length should be 6");
            passwordEditText.requestFocus();
            return;
        }

        if(zonalId.isEmpty()){
            zonalOfficierIdTextView.setError("Enter Id Of Zonal Officer");
            zonalOfficierIdTextView.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, contact, email, password, stationId, officerPosition,zonalId);
                            FirebaseDatabase.getInstance("https://pollingmanagementsystem-default-rtdb.firebaseio.com/").getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterUser.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                }
                            });

                        } else {

                            Toast.makeText(RegisterUser.this, "Something went wrong! Try Again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

}