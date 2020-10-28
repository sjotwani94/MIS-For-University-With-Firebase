package com.agilesoftware.misforuniversity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ManageStudentAdapter extends RecyclerView.Adapter<ManageStudentAdapter.MyViewHolder> {
    private List<ManageStudentData> functionsList;
    Context context;
    ArrayList<StudentDetails> studentDetails = new ArrayList<StudentDetails>();
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, studentEmail, studentDepartment;
        LinearLayout studentContainer;
        MyViewHolder(View view) {
            super(view);
            studentName = view.findViewById(R.id.student_name);
            studentEmail = view.findViewById(R.id.email_student);
            studentDepartment = view.findViewById(R.id.dept_and_yoa);
            studentContainer = view.findViewById(R.id.student_container);
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

    public ManageStudentAdapter(List<ManageStudentData> functionsList, Context context) {
        this.functionsList = functionsList;
        this.context = context;
    }
    @NonNull
    @Override
    public ManageStudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.student_file,parent,false);
        return new ManageStudentAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageStudentAdapter.MyViewHolder holder, final int position) {
        final ManageStudentData listData = functionsList.get(position);
        holder.studentName.setText(listData.getStudentName());
        holder.studentEmail.setText(listData.getStudentEmail());
        holder.studentDepartment.setText(listData.getStudentDept()+" ("+listData.getStudentBatch()+")");
        holder.studentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Student");
                dbRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        studentDetails = DatabaseHelper.getStudentDetailsEmail(snapshot,listData.getStudentEmail());
                        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme));
                        dialog.setTitle(listData.getStudentName()+" ("+studentDetails.get(0).getRollNo()+")");
                        dialog.setMessage("Name: "+studentDetails.get(0).getName()+"\n"+
                                "E-Mail: "+studentDetails.get(0).getEMail()+"\n"+
                                "Contact: "+studentDetails.get(0).getContactNo()+"\n"+
                                "Department: "+studentDetails.get(0).getDepartment()+"\n"+
                                "Year of Admission: "+studentDetails.get(0).getBatch());
                        dialog.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Student");
                                dbRef.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userkey = DatabaseHelper.retrieveKey(snapshot,listData.getStudentEmail());
                                        DatabaseHelper.deleteStudent(userkey);
                                        dialog.dismiss();
                                        Toast.makeText(context, "Entry Deleted", Toast.LENGTH_LONG).show();
                                        ((ManageStudents)context).finish();
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
                                Intent intent = new Intent(context,UpdateStudent.class);
                                final Bundle bundle = new Bundle();
                                bundle.putString("Email",listData.getStudentEmail());
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Student");
                                dbRef.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userkey = DatabaseHelper.retrieveKey(snapshot,listData.getStudentEmail());
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
                                ((ManageStudents)context).finish();
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
