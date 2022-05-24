package com.flame.bgfcreditapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class AdminDashboardActivity extends AppCompatActivity {

    private  TextView cData,aData,addAnalyst;
   private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        cData = findViewById(R.id.cData);
        aData = findViewById(R.id.aData);
        addAnalyst = findViewById(R.id.addAnalyst);

       toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("eIdeni BGF");

       cData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminDashboardActivity.this, CustomerAdminActivity.class);
                startActivity(i);

            }
        });

        aData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminDashboardActivity.this, AnalystAdminActivity.class);
                startActivity(i);

            }
        });
        addAnalyst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminDashboardActivity.this, AddAnalystActivity.class);
                startActivity(i);

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //adding a click listner for option selected on below line.
        int id = item.getItemId();
        switch (id) {
            case R.id.leave:
                //displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
                //on below line we are signing out our user.
             //   mAuth.signOut();
                //on below line we are opening our login activity.
                Intent i = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;

            case R.id.client:
                //displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "Clients list", Toast.LENGTH_LONG).show();
                //on below line we are signing out our user.
                //   mAuth.signOut();
                //on below line we are opening our login activity.
                Intent i1 = new Intent(AdminDashboardActivity.this, CustomerAdminActivity.class);
                startActivity(i1);
                this.finish();
                return true;

            case R.id.analyst:
                //displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "Analyst list", Toast.LENGTH_LONG).show();
                //on below line we are signing out our user.
                //   mAuth.signOut();
                //on below line we are opening our login activity.
                Intent i2 = new Intent(AdminDashboardActivity.this, AnalystAdminActivity.class);
                startActivity(i2);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //on below line we are inflating our menu file for displaying our menu options.
        getMenuInflater().inflate(R.menu.adminmenu, menu);
        return true;
    }
}