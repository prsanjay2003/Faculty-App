package com.example.staffapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class WorkshopAdapter extends FirebaseRecyclerAdapter<workmain,WorkshopAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public WorkshopAdapter(@NonNull FirebaseRecyclerOptions<workmain> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull workmain model) {
        holder.title.setText(model.getEdTitle());
        holder.year.setText(model.getEdYear());
        holder.month.setText(model.getEdMonth());
        holder.place.setText(model.getEdPlace());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.title.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_workshop))
                        .setExpanded(true,1600)
                        .create();

                //dialogPlus.show();
                View v = dialogPlus.getHolderView();
                EditText title = v.findViewById(R.id.title);
                EditText year = v.findViewById(R.id.year);
                EditText month = v.findViewById(R.id.month);
                EditText place = v.findViewById(R.id.place);

                Button btnupdate = v.findViewById((R.id.btnupdate));

                title.setText(model.getEdTitle());
                year.setText(model.getEdYear());
                month.setText(model.getEdMonth());
                place.setText(model.getEdPlace());

                dialogPlus.show();

                btnupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("edTitle",title.getText().toString());
                        map.put("edYear",year.getText().toString());
                        map.put("edMonth",month.getText().toString());
                        map.put("edPlace",place.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Workshop")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.title.getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.title.getContext(), "Error Updating", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });
        holder.pdfclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pdfUrl = model.getPdfUrl(); // Assuming you have a method to get the PDF URL from your model class
                if (pdfUrl != null && !pdfUrl.isEmpty()) {
                    // Open PDF URL in browser or download using Intent
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(pdfUrl));
                    holder.title.getContext().startActivity(intent);
                } else {
                    Toast.makeText(holder.title.getContext(), "PDF URL not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.title.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data cannot be Undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Workshop")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.title.getContext(), "Cancelled",Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workshop_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView pdfclick;
        TextView title,year,month,place;
        Button update,delete;

        public myViewHolder(@NonNull View itemView) {


            super(itemView);

            title =(TextView)itemView.findViewById(R.id.title);
            year = (TextView)itemView.findViewById(R.id.year);
            month=(TextView)itemView.findViewById(R.id.month);
            place=(TextView)itemView.findViewById(R.id.place);
            update = (Button)itemView.findViewById(R.id.update);
            delete=(Button)itemView.findViewById(R.id.delete);
            pdfclick = (ImageView)itemView.findViewById(R.id.pdfclick);

        }
    }

}
