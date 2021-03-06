package com.agilesoftware.misforuniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceDisplayActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView department, rollNo, studentName, semesterNo;
    private List<AttendanceListData> functionsList = new ArrayList<>();
    private AttendanceListAdapter adapter;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay, studyYear;
    List<String> courseDesc = new ArrayList<String>();
    List<String> courseAttendance = new ArrayList<String>();
    List<String> coursePenalty = new ArrayList<String>();
    RelativeLayout s1;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Email = "emailKey";
    public static final String Theme = "themeKey";

    public void alertFirebaseFailure(DatabaseError error) {

        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getApplicationContext())
                .setTitle("An error occurred while connecting to Firebase!")
                .setMessage(error.toString())
                .setPositiveButton("Dismiss", null)
                .setIcon(android.R.drawable.presence_busy)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_display);
        s1 = findViewById(R.id.scroller);
        department=findViewById(R.id.degree_name);
        rollNo=findViewById(R.id.edt_roll_num);
        studentName=findViewById(R.id.edt_student_name);
        semesterNo=findViewById(R.id.edt_semester_no);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Theme)){
            if (sharedpreferences.getString(Theme,"").matches("Light")){
                s1.setBackgroundResource(R.drawable.navy);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_yellow)));
                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getSupportActionBar().getTitle() + "</font>")));
            }else if (sharedpreferences.getString(Theme,"").matches("Dark")){
                s1.setBackgroundResource(R.drawable.blackcar);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_black)));
                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#0000FF\">" + getSupportActionBar().getTitle() + "</font>")));
            }
        }
        String Name = getIntent().getExtras().getString("Name");
        String Department = getIntent().getExtras().getString("Department");
        final String RollNo = getIntent().getExtras().getString("RollNo");
        int YearOfPass = getIntent().getExtras().getInt("YearOfPass");
        department.setText(Department);
        studentName.setText(Name);
        rollNo.setText(RollNo);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);
        studyYear = mYear - YearOfPass;
        if (mMonth>6){
            semesterNo.setText(String.valueOf((studyYear*2)+1));
        }else {
            semesterNo.setText(String.valueOf(2*studyYear));
        }

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/StudentEnrollments");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<StudentEnrollments> studentEnrollments = DatabaseHelper.getStudentEnrollmentRollNo(snapshot,RollNo);
                for (int i=0;i<studentEnrollments.size();i++){
                    courseDesc.add(studentEnrollments.get(i).getCourseCode()+" ("+studentEnrollments.get(i).getCourseName()+")");
                    float total;
                    Log.d("Total Lectures", "Total Lectures: "+studentEnrollments.get(i).getTotalLectures());
                    if (studentEnrollments.get(i).getTotalLectures()!=0){
                        total = (Float.valueOf(studentEnrollments.get(i).getTotalPresence())/Float.valueOf(studentEnrollments.get(i).getTotalLectures()))*100;
                        Log.d("Total", "Attendance Percent: "+total);
                    }
                    else {
                        total = 0;
                    }
                    courseAttendance.add(String.valueOf(total));
                    if (total>=85){
                        coursePenalty.add("None");
                    }else if (total>75 && total<85){
                        coursePenalty.add("A");
                    }else if (total>65 && total<=75){
                        coursePenalty.add("B");
                    }else {
                        coursePenalty.add("C");
                    }
                }

                recyclerView = findViewById(R.id.recycler_functions);
                for (int len=0;len<courseDesc.size();len++){
                    functionsList.add(new AttendanceListData(courseDesc.get(len),courseAttendance.get(len),coursePenalty.get(len)));
                }
                adapter = new AttendanceListAdapter(functionsList,AttendanceDisplayActivity.this,RollNo);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertFirebaseFailure(error);
                error.toException();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedpreferences.contains(Theme)){
            if (sharedpreferences.getString(Theme,"").matches("Light")){
                s1.setBackgroundResource(R.drawable.navy);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_yellow)));
                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getSupportActionBar().getTitle() + "</font>")));
            }else if (sharedpreferences.getString(Theme,"").matches("Dark")){
                s1.setBackgroundResource(R.drawable.blackcar);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_black)));
                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#0000FF\">" + getSupportActionBar().getTitle() + "</font>")));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_items1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dark_theme:
                s1.setBackgroundResource(R.drawable.blackcar);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_black)));
                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#0000FF\">" + getSupportActionBar().getTitle() + "</font>")));
                editor.putString(Theme, "Dark");
                editor.commit();
                break;
            case R.id.light_theme:
                s1.setBackgroundResource(R.drawable.navy);
                SharedPreferences.Editor editor1 = sharedpreferences.edit();
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_yellow)));
                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getSupportActionBar().getTitle() + "</font>")));
                editor1.putString(Theme, "Light");
                editor1.commit();
                break;
            case R.id.exit:
                System.exit(0);
                break;
        }
        return true;
    }
}
