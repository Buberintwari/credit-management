package com.flame.bgfcreditapp.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flame.bgfcreditapp.Model.Analyst;
import com.flame.bgfcreditapp.R;

import java.util.HashMap;
import java.util.Map;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;


public class AnalystAdapter extends FirebaseRecyclerAdapter<
        Analyst, AnalystAdapter.myviewholder> {

    public AnalystAdapter(
            @NonNull FirebaseRecyclerOptions<Analyst> options) {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Analyst model) {

        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.name.setText(model.getName());

        // Add lastname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.phone.setText(model.getPhonenumber());

        // Add age from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.email.setText(model.getEmail());
        Glide.with(holder.img.getContext()).load(model.getProfilepictureurl()).into(holder.img);

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialog_content))
                        .setExpanded(true,1000)
                        .create();

                View myview = dialogPlus.getHolderView();
                TextInputEditText purl = myview.findViewById(R.id.upPhoto);
                TextInputEditText name = myview.findViewById(R.id.upName);
                TextInputEditText email = myview.findViewById(R.id.upMail);
                TextInputEditText contact = myview.findViewById(R.id.upContact);
                Button submit  = myview.findViewById(R.id.upBtn);

                purl.setText(model.getProfilepictureurl());
                name.setText(model.getName());
                email.setText(model.getEmail());
                contact.setText(model.getPhonenumber());

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("profilepictureurl",purl.getText().toString());
                        map.put("name",name.getText().toString());
                        map.put("email",email.getText().toString());
                        map.put("phonenumber",contact.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("analysts")
                                .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialogPlus.dismiss();

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });


                    }
                });


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Delete panel");
                builder.setTitle("Delete....");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance().getReference().child("analysts")
                                .child(getRef(position).getKey()).removeValue();


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }
        });
    }


    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analyst_dispaly, parent, false);
        return new myviewholder(view);

    }


    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class myviewholder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView name, phone, email;
        ImageButton update,delete;

        public myviewholder(@NonNull View itemView) {
            super(itemView);


            img = itemView.findViewById(R.id.userProfileImage);
            name = itemView.findViewById(R.id.uName);
            phone = itemView.findViewById(R.id.uContact);
            email = itemView.findViewById(R.id.uEmail);
            update = itemView.findViewById(R.id.updateBtn);
            delete = itemView.findViewById(R.id.deleleBtn);
        }


    }
}

