package com.flame.bgfcreditapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {


    private CircleImageView regPhoto;
    private TextInputEditText regName, regId, regContact, regEmail, regPassword;
    private Button regBtn;
    private Uri resultUri;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    private TextView logUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        regPhoto = findViewById(R.id.regPhoto);
        regName = findViewById(R.id.regName);
        regId = findViewById(R.id.regId);
        regContact = findViewById(R.id.regContact);
        regEmail = findViewById(R.id.regMail);
        regPassword = findViewById(R.id.regPassword);
        regBtn = findViewById(R.id.regBtn);
         loader = new ProgressDialog(this);
        logUser = findViewById(R.id.logUser);
        logUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        mAuth = FirebaseAuth.getInstance();


        //image
        regPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);


            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = regEmail.getText().toString().trim();
                final String password = regPassword.getText().toString().trim();
                final String contact = regContact.getText().toString().trim();
                final String id = regId.getText().toString().trim();
                final String name = regName.getText().toString().trim();

                if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    regEmail.setError("Invalid email pattern!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    regPassword.setError("Password is required!");
                    return;
                }
                if (TextUtils.isEmpty(contact)) {
                    regContact.setError("Phone number is required!");
                    return;
                }
                if (TextUtils.isEmpty(id)) {
                    regId.setError("Identity number is required!");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    regName.setError("Name is required!");
                    return;
                } else {
                    loader.setMessage("Registering you...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                String error = task.getException().toString();
                                Toast.makeText(RegistrationActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            } else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", name);
                                userInfo.put("email", email);
                                userInfo.put("idnumber", id);
                                userInfo.put("phonenumber", contact);
                                userInfo.put("search", "client");

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegistrationActivity.this, "Data set Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                        //loader.dismiss();
                                    }
                                });

                                if (resultUri != null) {
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile images").child(currentUserId);
                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistrationActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepictureurl", imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(RegistrationActivity.this, "Image url added to database successfully", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        finish();
                                                    }
                                                });
                                            }

                                        }
                                    });

                                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }
                            }

                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
            regPhoto.setImageURI(resultUri);

        }

    }
}

