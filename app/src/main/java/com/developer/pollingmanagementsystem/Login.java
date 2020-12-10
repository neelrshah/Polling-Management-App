package com.developer.pollingmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{
    public TextView register,forgotPassword;
    public Button login;
    public ProgressBar loginProgressBar;

    private FirebaseAuth mAuth;
    public EditText loginEmailEditText, loginPasswordEditText;




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerTextView:
                startActivity(new Intent(this,RegisterUser.class));
                break;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.registerTextView);
        forgotPassword = findViewById(R.id.forgotPassTextView);
        login =  findViewById(R.id.loginBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        loginEmailEditText = findViewById(R.id.loginEditTextEmailAddress);
        loginPasswordEditText = findViewById(R.id.editTextPassword);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        register.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });


    }




    private void resetPassword() {

        startActivity(new Intent(Login.this,ForgotPassword.class));

    }


    private void userLogin() {
        String loginEmail = loginEmailEditText.getText().toString().trim();
        String loginPassword = loginPasswordEditText.getText().toString().trim();

        if(loginEmail.isEmpty()){
            loginEmailEditText.setError("Enter E-mail");
            loginEmailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()){
            loginEmailEditText.setError("Enter valid E-mail");
            loginEmailEditText.requestFocus();
            return;
        }

        if(loginPassword.isEmpty()){
            loginPasswordEditText.setError("Enter Password");
            loginPasswordEditText.requestFocus();
            return;
        }

        if (loginPassword.length() < 6){
            loginPasswordEditText.setError("Minimum password length should be 6");
            loginPasswordEditText.requestFocus();
            return;
        }


        loginProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        loginProgressBar.setVisibility(View.GONE);
                        startActivity(new Intent(Login.this,PollingManagement.class));
                    }
                    else{

                        user.sendEmailVerification();
                        loginProgressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Verify your E-mail", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    loginProgressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Login Failed.Please check your credential", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}