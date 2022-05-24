package com.flame.bgfcreditapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.flame.bgfcreditapp.Model.PdfHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SendDocActivity extends AppCompatActivity {

    TextInputEditText pdfName;
    Button btn_upload;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    private ImageButton backButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_doc);

        pdfName = findViewById(R.id.txt_pdfname);
        btn_upload =  findViewById(R.id.btn_upload);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(SendDocActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userName=user.getEmail();
        pdfName.setText(userName);
        pdfName.setEnabled(false);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDFfile();

            }
        });

    }

    private void selectPDFfile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF file"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && data !=null && data.getData()!=null){

            uploadPDFFile(data.getData());

        }
    }

    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userName=user.getEmail();

        StorageReference reference = storageReference.child("uploads/"+userName+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url = uri.getResult();

                        PdfHelper pdfHelper = new PdfHelper(pdfName.getText().toString(),url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(pdfHelper);
                        Toast.makeText(SendDocActivity.this, "File Uploaded..", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                progressDialog.setMessage("Uploaded: "+(int)progress+"%");

            }
        });
    }
}