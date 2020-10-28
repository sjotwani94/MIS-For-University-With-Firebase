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

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.MyViewHolder> {
    private List<AttendanceListData> functionsList;
    String RollNo;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courseDesc, courseAttendance,coursePenalty;
        LinearLayout attendanceContainer;
        MyViewHolder(View view) {
            super(view);
            courseDesc = view.findViewById(R.id.course_desc);
            courseAttendance = view.findViewById(R.id.course_attendance);
            coursePenalty = view.findViewById(R.id.course_attend_category);
            attendanceContainer = view.findViewById(R.id.attendance_container);
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
    public AttendanceListAdapter(List<AttendanceListData> functionsList, Context context, String RollNo) {
        this.functionsList = functionsList;
        this.context = context;
        this.RollNo = RollNo;
    }
    @NonNull
    @Override
    public AttendanceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.attendance_file,parent,false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceListAdapter.MyViewHolder holder, int position) {
        final AttendanceListData listData = functionsList.get(position);
        holder.courseDesc.setText(listData.getCourseDesc());
        holder.courseAttendance.setText(listData.getCourseAttendance());
        holder.coursePenalty.setText(listData.getCoursePenaltyCategory());
        holder.attendanceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/StudentEnrollments");
                dbRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<StudentEnrollments> studentEnrollments = DatabaseHelper.getStudentEnrollmentDetails(snapshot,RollNo,listData.getCourseDesc().substring(0,5));
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle(listData.getCourseDesc());
                        dialog.setMessage("Total Presence: "+studentEnrollments.get(0).getTotalPresence()+"/"+studentEnrollments.get(0).getTotalLectures()+" Lectures\n"+
                                "Total Absence: "+(studentEnrollments.get(0).getTotalLectures() - studentEnrollments.get(0).getTotalPresence())+"/"+studentEnrollments.get(0).getTotalLectures()+" Lectures");
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
