package com.agilesoftware.misforuniversity;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.MyViewHolder> {
    private List<RecyclerListData> functionsList;
    String RollNo;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courseDesc, courseMarks,courseGrade;
        LinearLayout resultContainer;
        MyViewHolder(View view) {
            super(view);
            courseDesc = view.findViewById(R.id.course_desc);
            courseMarks = view.findViewById(R.id.course_marks);
            courseGrade = view.findViewById(R.id.course_grade);
            resultContainer = view.findViewById(R.id.result_container);
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
    public RecyclerListAdapter(List<RecyclerListData> functionsList, Context context, String RollNo) {
        this.functionsList = functionsList;
        this.context = context;
        this.RollNo = RollNo;
    }
    @NonNull
    @Override
    public RecyclerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_file,parent,false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerListAdapter.MyViewHolder holder, int position) {
        final RecyclerListData listData = functionsList.get(position);
        holder.courseDesc.setText(listData.getCourseDesc());
        holder.courseMarks.setText(listData.getCourseMarks());
        holder.courseGrade.setText(listData.getCourseGrade());
        holder.resultContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/StudentEnrollments");
                dbRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<StudentEnrollments> studentEnrollments = DatabaseHelper.getStudentEnrollmentDetails(snapshot,RollNo,listData.getCourseDesc().substring(0,5));
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle(listData.getCourseDesc());
                        dialog.setMessage("Class Test: "+studentEnrollments.get(0).getClassTest()+"/30\n"+
                                "Mid Sem Exam: "+studentEnrollments.get(0).getMidSem()+"/40\n"+
                                "Assignment Marks: "+studentEnrollments.get(0).getAssignments()+"/30\n"+
                                "Laboratory Practicals' Marks: "+studentEnrollments.get(0).getLabPracticals()+"/100\n"+
                                "Final Exam Marks: "+studentEnrollments.get(0).getFinalExam()+"/100");
                        dialog.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        dialog.show();
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
