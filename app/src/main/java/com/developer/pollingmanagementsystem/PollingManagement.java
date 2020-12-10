package com.developer.pollingmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PollingManagement extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    // User INFORMATION
    public FirebaseUser user;
    public DatabaseReference reference;
    public String userId, userName, email, contact, userPosition, stationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling_management);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_about, R.id.nav_zonal, R.id.nav_preciding)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // GRAB USER INFO FROM FIREBASE :

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://pollingmanagementsystem-default-rtdb.firebaseio.com/").getReference("Users");
        userId = user.getUid();



        //GRAB NAV USERS COMPONENTS

        View headerView = navigationView.getHeaderView(0);
        final TextView navUser = headerView.findViewById(R.id.navUserName);
        final TextView navEmail = headerView.findViewById(R.id.navUserEmail);



        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);
                if(userProfile != null){
                    String fullName = userProfile.getName();
                    String email = userProfile.getEmail();
                    String contact = userProfile.getContact();
                    String stationId = userProfile.getStationId();
                    String userPosition = userProfile.getOfficerPosition();


                    //SET USER DATA IN NAV BAR
                    if (userPosition.equals("Zonal Officer")){

                        NavigationView navigationView= findViewById(R.id.nav_view);
                        Menu menuNav=navigationView.getMenu();
                        MenuItem nav_preciding = menuNav.findItem(R.id.nav_preciding);
                        nav_preciding.setEnabled(false);
                        MenuItem nav_zonal = menuNav.findItem(R.id.nav_zonal);
                        nav_zonal.setEnabled(true);
                    }

                    if (userPosition.equals("Preciding Officer")){

                        NavigationView navigationView= findViewById(R.id.nav_view);
                        Menu menuNav=navigationView.getMenu();
                        MenuItem nav_zonal = menuNav.findItem(R.id.nav_zonal);
                        nav_zonal.setEnabled(false);
                        MenuItem nav_preciding = menuNav.findItem(R.id.nav_preciding);
                        nav_preciding.setEnabled(true);
                    }
                    navUser.setText(fullName);
                    navEmail.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.polling_management, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            final TextView navUser = headerView.findViewById(R.id.navUserName);
            final TextView navEmail = headerView.findViewById(R.id.navUserEmail);
            navUser.setText("");
            navEmail.setText("");
            startActivity(new Intent(PollingManagement.this,Login.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}