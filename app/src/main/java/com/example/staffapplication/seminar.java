package com.example.staffapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class seminar extends AppCompatActivity {
    EditText edTitle;
    EditText edYear;
    EditText edMonth;
    EditText edPlace;
    String radio ,title,month,year,place;
    RadioGroup edRadio;

    RadioButton r1;
    RadioButton r2,r3;
    Button btn;
    FloatingActionButton floatingActionButton;
    TextView pdf , add;
    String downloadUrl;
    private final int REQ=1;
    public Uri pdfData;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog pd;
    private String pdfName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seminar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);


        edTitle = findViewById(R.id.editTextRegUserName);
        edYear = findViewById(R.id.editTextRegEmail);
        edMonth = findViewById(R.id.editTextRegPassword);
        pdf=findViewById(R.id.textpdf);
        edPlace = findViewById(R.id.editTextRegConfirmPassword);
        edRadio = findViewById(R.id.radiogrp);
        r1 = findViewById(R.id.participate);
        r2= findViewById(R.id.organize);
        r3=findViewById(R.id.resource);
        btn = findViewById(R.id.btn);
        add = findViewById(R.id.textView3);
        floatingActionButton = findViewById(R.id.float_btn);
        pdf.setVisibility(View.GONE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                title = edTitle.getText().toString();
                year = edYear.getText().toString();
                month = edMonth.getText().toString();
                place = edPlace.getText().toString();

                if (r1.isChecked()) {
                    radio = "participate";
                } else if (r2.isChecked()) {
                    radio = "organize";
                } else if (r3.isChecked()) {
                    radio = "resource";
                }

                if (title.length() == 0 ||year.length() == 0 || month.length() == 0 || place.length() == 0||radio.length()==0) {

                    Toast.makeText(getApplicationContext(), "Please fill the details", Toast.LENGTH_SHORT).show();
                } else if (pdfData == null) {
                    Toast.makeText(seminar.this, "Please Upload Certificate", Toast.LENGTH_SHORT).show();
                } else {
                    uploadPdf();

                }

            }
        });
    }

    private void uploadData(String downloadUrl){
        String uniqueKey = databaseReference.child("Seminar").push().getKey();

        HashMap data = new HashMap();

        data.put("edTitle",title);
        data.put("edYear",year);
        data.put("edMonth",month);
        data.put("edPlace",place);
        data.put("edRadio",radio);
        data.put("pdfUrl",downloadUrl);

        databaseReference.child("Seminar").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(seminar.this, "seminar added Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(seminar.this, "Failed to add Seminar", Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfData = data.getData();


            pdf.setText(pdfName);


            pdf.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
        }


    }
    private void uploadPdf(){
        pd.setTitle("Please wait.....");
        pd.setMessage("Adding Seminar");
        pd.show();
        StorageReference reference = storageReference.child("Seminar/"+ pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        uploadData(String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(seminar.this, "Something went Wrong !!", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
