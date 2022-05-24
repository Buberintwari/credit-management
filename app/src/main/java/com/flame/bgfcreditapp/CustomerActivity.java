package com.flame.bgfcreditapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.flame.bgfcreditapp.Adapter.AnalystAdapter;
import com.flame.bgfcreditapp.Adapter.ClientAdapter;
import com.flame.bgfcreditapp.Adapter.CustomerAdapter;
import com.flame.bgfcreditapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CustomerActivity extends AppCompatActivity {
    private ImageButton cuBackButton;
    private RecyclerView recyclerView;
    private ImageButton d,u;

    DatabaseReference mbase;
    private CustomerAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        cuBackButton = findViewById(R.id.cuBackButton);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mbase = FirebaseDatabase.getInstance().getReference().child("users");



    FirebaseRecyclerOptions<User> options
                = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(mbase, User.class)
                .build();


        userAdapter = new CustomerAdapter(options);

        recyclerView.setAdapter(userAdapter);




        cuBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this, MainActivity.class);
               // Intent intent  = new Intent(CustomerActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
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
}
