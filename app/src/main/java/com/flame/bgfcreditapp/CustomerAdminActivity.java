package com.flame.bgfcreditapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.flame.bgfcreditapp.Adapter.AnalystAdapter;
import com.flame.bgfcreditapp.Adapter.ClientAdapter;
import com.flame.bgfcreditapp.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerAdminActivity extends AppCompatActivity {
    private  ImageButton bBtn;
    private Button cuBackButton;
    private RecyclerView recyclerView;

    DatabaseReference mbase;
    private ClientAdapter userAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_admin);
        bBtn = findViewById(R.id.bBtn);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("eIdeni BGF");
        bBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerAdminActivity.this, AdminDashboardActivity.class);
                startActivity(i);

            }
        });



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mbase = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseRecyclerOptions<User> options
                = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(mbase, User.class)
                .build();


        userAdapter = new ClientAdapter(options);

        recyclerView.setAdapter(userAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mysearch,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s) {

        FirebaseRecyclerOptions<User> options
                = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("name").startAt(s).endAt(s+"\ufaff "), User.class)
                .build();
        userAdapter = new ClientAdapter(options);
        userAdapter.startListening();
        recyclerView.setAdapter(userAdapter);

    }
}