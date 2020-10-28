package com.agilesoftware.misforuniversity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageFacultyAdapter extends RecyclerView.Adapter<ManageFacultyAdapter.MyViewHolder> {
    private List<ManageFacultyData> functionsList;
    Context context;
    ArrayList<FacultyDetails> facultyDetails = new ArrayList<FacultyDetails>();
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView facultyName, facultyEmail, facultyDepartment;
        LinearLayout facultyContainer;
        MyViewHolder(View view) {
            super(view);
            facultyName = view.findViewById(R.id.faculty_name);
            facultyEmail = view.findViewById(R.id.email_faculty);
            facultyDepartment = view.findViewById(R.id.dept_desg);
            facultyContainer = view.findViewById(R.id.faculty_container);
        }
    }

    public void alertFirebaseFailure(DatabaseError error) {

        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context)
                .setTitle("An error occurred while connecting to Firebase!")
                .setMessage(error.toString())
                .setPositiveButton("Dismiss", null)
                .setIcon(android.R.drawable.presence_busy)
                .show();
    }

    public ManageFacultyAdapter(List<ManageFacultyData> functionsList, Context context) {
        this.functionsList = functionsList;
        this.context = context;
    }
    @NonNull
    @Override
    public ManageFacultyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.faculty_file,parent,false);
        return new ManageFacultyAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageFacultyAdapter.MyViewHolder holder, final int position) {
        final ManageFacultyData listData = functionsList.get(position);
        holder.facultyName.setText(listData.getFacultyName());
        holder.facultyEmail.setText(listData.getFacultyEmail());
        holder.facultyDepartment.setText(listData.getFacultyDept()+" ("+listData.getFacultyDesg()+")");
        holder.facultyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Faculty");
                dbRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        facultyDetails = DatabaseHelper.getFacultyDetailsEmail(snapshot,listData.getFacultyEmail());
                        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme));
                        dialog.setTitle(listData.getFacultyName());
                        dialog.setMessage("Name: "+facultyDetails.get(0).getName()+"\n"+
                                "E-Mail: "+facultyDetails.get(0).getEMail()+"\n"+
                                "Contact: "+facultyDetails.get(0).getContactNo()+"\n"+
                                "Department: "+facultyDetails.get(0).getDepartment()+"\n"+
                                "Designation: "+facultyDetails.get(0).getPosition());
                        dialog.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Faculty");
                                dbRef.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userkey = DatabaseHelper.retrieveKey(snapshot,listData.getFacultyEmail());
                                        DatabaseHelper.deleteFaculty(userkey);
                                        dialog.dismiss();
                                        Toast.makeText(context, "Entry Deleted", Toast.LENGTH_LONG).show();
                                        ((ManageFaculties)context).finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        alertFirebaseFailure(error);
                                        error.toException();
                                    }
                                });
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        dialog.setNeutralButton("Update Details", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context,UpdateFaculty.class);
                                final Bundle bundle = new Bundle();
                                bundle.putString("Email",listData.getFacultyEmail());
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Faculty");
                                dbRef.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userkey = DatabaseHelper.retrieveKey(snapshot,listData.getFacultyEmail());
                                        bundle.putString("Index",userkey);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        alertFirebaseFailure(error);
                                        error.toException();
                                    }
                                });
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                ((ManageFaculties)context).finish();
                                dialog.dismiss();
                            }
                        });
                        try {
                            dialog.show();
                        }
                        catch (WindowManager.BadTokenException e) {
                            Log.d("Error", "Sorry");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        alertFirebaseFailure(error);
                        error.toException();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return functionsList.size();
    }
}
