package com.flame.bgfcreditapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.flame.bgfcreditapp.Adapter.ClientAdapter;
import com.flame.bgfcreditapp.Adapter.CustomerAdapter;
import com.flame.bgfcreditapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnalystMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    DatabaseReference mbase;
    private ClientAdapter userAdapter;
    private Toolbar toolbar;
   // private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyst_main);

       // firebaseAuth = FirebaseAuth.getInstance();
      //  checkUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("eIdeni BGF");




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

  /*  private void checkUser() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
            ref.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            String userType= ""+snapshot.child("search").getValue();
                            if (userType.equals("client")){

                                Intent i = new Intent(AnalystMainActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });



    }

   */


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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //adding a click listner for option selected on below line.
        int id = item.getItemId();
        switch (id) {
            case R.id.leave:
                //displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
                //on below line we are signing out our user.
               // mAuth.signOut();
                //on below line we are opening our login activity.
                Intent i = new Intent(AnalystMainActivity.this, LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            case R.id.read:
                //Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                //startActivity(intent);
                Intent intent1 = new Intent(AnalystMainActivity.this, ReadDocActivity.class);
                startActivity(intent1);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
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