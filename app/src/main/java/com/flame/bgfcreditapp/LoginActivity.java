package com.flame.bgfcreditapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextView TVNewUser;
    private TextInputEditText logGmail, logPassword, userCode;

    private ProgressBar progressbar;
    private ProgressDialog loader;


    private FirebaseAuth mAuth;
    private Button logBtn;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        //initialisation variables
        TVNewUser = findViewById(R.id.idTVNewUser);

        logGmail = findViewById(R.id.logGmail);
        logPassword = findViewById(R.id.logPassword);
        // progressbar = findViewById(R.id.progressBar);

        logBtn = findViewById(R.id.logBtn);

        //userCode= findViewById(R.id.userCode);


        // taking instance of FirebaseAuth

        loader = new ProgressDialog(this);

        TVNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);

            }
        });


        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserAccount();
            }
        });
    }

    private void loginUserAccount() {

        // show the visibility of progress bar to show loading
        // progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password, user;
        email = logGmail.getText().toString().trim();
        password = logPassword.getText().toString().trim();

        // user= userCode.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            logGmail.setError("Gmail is required!");
            return;

        }

        if (TextUtils.isEmpty(password)) {
            logPassword.setError("Password is required!");
            return;
        }

        //if (user.equals("Select your role")) {
        //   Toast.makeText(LoginActivity.this, "Select your role", Toast.LENGTH_SHORT).show();
        //  return;
        // }

        if (email.equals("admin@gmail.com") && password.equals("admin")) {

            Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
            startActivity(intent);

            logGmail.setText("");
            logPassword.setText("");
            return;


        }

        if (email.equals("analyst@gmail.com") && password.equals("analyst")) {

            Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(LoginActivity.this, AnalystMainActivity.class);
            startActivity(intent);
            logGmail.setText("");
            logPassword.setText("");
            return;


        }




        // signin existing user
        loader.setMessage("Registering you...");
        loader.setCanceledOnTouchOutside(false);
        loader.show();
         mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

                                    // hide the progress bar
                                    //progressbar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();

                                    // hide the progress bar
                                    //   progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
        loader.dismiss();


/*
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                ref.child(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                String userType= ""+snapshot.child("search").getValue();
                                if (userType.equals("client")){

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else if (userType.equals("analyst")){

                                    Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(LoginActivity.this, AnalystMainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });

                loader.dismiss();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loader.dismiss();
                        Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

 */

    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}