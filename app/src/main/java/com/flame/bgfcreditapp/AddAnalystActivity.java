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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class AddAnalystActivity extends AppCompatActivity {
    private ImageButton bBtn;

    private CircleImageView regPhoto;
    private TextInputEditText regName, regContact, regEmail, regPassword;
    private Button regBtn;
    private Uri resultUri;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_analyst);

        regPhoto = findViewById(R.id.regPhoto);
        regName = findViewById(R.id.regName);

        regContact = findViewById(R.id.regContact);
        regEmail = findViewById(R.id.regMail);
        regPassword = findViewById(R.id.regPassword);
        regBtn = findViewById(R.id.regBtn);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        //back button function
        bBtn = findViewById(R.id.backButton);
        bBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddAnalystActivity.this, AdminDashboardActivity.class);
                startActivity(i);

            }
        });

        // get image from gallery

        regPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);


            }
        });



     //register function

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = regEmail.getText().toString().trim();
                final String password = regPassword.getText().toString().trim();
                final String contact = regContact.getText().toString().trim();

                final String name = regName.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    regEmail.setError("Email is required!");
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
                                Toast.makeText(AddAnalystActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            } else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("analysts").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", name);
                                userInfo.put("email", email);

                                userInfo.put("phonenumber", contact);
                                userInfo.put("search", "analyst");


                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddAnalystActivity.this, "Data set Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AddAnalystActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                        //loader.dismiss();
                                    }
                                });

                                if (resultUri != null) {
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile images for analysts").child(currentUserId);
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
                                            Toast.makeText(AddAnalystActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
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
                                                                    Toast.makeText(AddAnalystActivity.this, "Image url added to database successfully", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(AddAnalystActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        finish();
                                                    }
                                                });
                                            }

                                        }
                                    });

                                    Intent intent = new Intent(AddAnalystActivity.this, AdminDashboardActivity.class);
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