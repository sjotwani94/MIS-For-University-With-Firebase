package com.agilesoftware.misforuniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateAttendanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText totalPresent,totalLectures;
    Spinner rollNum,courseCode;
    Button submit,fetch;
    List<String> listOfCourseCodes = new ArrayList<String>();
    List<String> listOfCourseNames = new ArrayList<String>();
    List<String> listOfCourses = new ArrayList<String>();
    List<String> listOfRollNos = new ArrayList<String>();
    ArrayList<CourseDetails> courseDetails = new ArrayList<CourseDetails>();
    ArrayList<StudentDetails> studentDetails = new ArrayList<StudentDetails>();
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
        setContentView(R.layout.activity_update_attendance);
        s1 = findViewById(R.id.scroller);
        totalPresent=findViewById(R.id.edt_total_present);
        totalLectures=findViewById(R.id.edt_total_lectures);
        rollNum=findViewById(R.id.list_roll_nos);
        courseCode=findViewById(R.id.list_courses);
        submit=findViewById(R.id.submit_course);
        fetch=findViewById(R.id.fetch_course);
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
        String Department = getIntent().getStringExtra("Department");
        String[] Splitted = Department.split(" ");
        final String ShortForm = Splitted[0].substring(0,1)+Splitted[1].substring(0,1);
        Log.d("Dept", "Department: "+ShortForm);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Courses");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseDetails = DatabaseHelper.getCourseDetailsWithFilter(snapshot,ShortForm);
                for (int i =0; i<courseDetails.size();i++)
                {
                    listOfCourseCodes.add(courseDetails.get(i).getCourseCode());
                    listOfCourseNames.add(courseDetails.get(i).getCourseName());
                    listOfCourses.add(courseDetails.get(i).getCourseCode()+" ("+courseDetails.get(i).getCourseName()+")");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateAttendanceActivity.this, android.R.layout.simple_spinner_item, listOfCourses);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseCode.setAdapter(adapter);
                courseCode.setOnItemSelectedListener(UpdateAttendanceActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertFirebaseFailure(error);
                error.toException();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/StudentEnrollments");
                dbRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String rollno = rollNum.getSelectedItem().toString();
                        String coursecode = courseCode.getSelectedItem().toString().substring(0,5);
                        String enrollmentKey = DatabaseHelper.retrieveEnrollmentKey(snapshot,rollno,coursecode);
                        ArrayList<StudentEnrollments> studentEnrollments = DatabaseHelper.getStudentEnrollmentDetails(snapshot,rollno,coursecode);
                        String coursename = studentEnrollments.get(0).getCourseName();
                        int presence = Integer.parseInt(totalPresent.getText().toString());
                        int lectures = Integer.parseInt(totalLectures.getText().toString());
                        int classtest = studentEnrollments.get(0).getClassTest();
                        int midsem = studentEnrollments.get(0).getMidSem();
                        int assign = studentEnrollments.get(0).getAssignments();
                        int labprac = studentEnrollments.get(0).getLabPracticals();
                        int finalexam = studentEnrollments.get(0).getFinalExam();
                        StudentEnrollments studentEnrollments1 = new StudentEnrollments(rollno,coursecode,coursename,classtest,midsem,assign,labprac,finalexam,presence,lectures);
                        DatabaseHelper.updateStudentEnrollment(studentEnrollments1,enrollmentKey);
                        totalPresent.setText("");
                        totalLectures.setText("");
                        Toast.makeText(UpdateAttendanceActivity.this, "Attendance Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        alertFirebaseFailure(error);
                        error.toException();
                    }
                });
            }
        });
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/StudentEnrollments");
                dbRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String rollno = rollNum.getSelectedItem().toString();
                        String coursecode = courseCode.getSelectedItem().toString().substring(0,5);
                        ArrayList<StudentEnrollments> studentEnrollments = DatabaseHelper.getStudentEnrollmentDetails(snapshot,rollno,coursecode);
                        totalPresent.setText(String.valueOf(studentEnrollments.get(0).getTotalPresence()));
                        totalLectures.setText(String.valueOf(studentEnrollments.get(0).getTotalLectures()));
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.list_courses:
                switch (position){
                    default:
                        final int pos = position;
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/StudentEnrollments");
                        dbRef.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                listOfRollNos.clear();
                                ArrayList<String> studentEnrollments = DatabaseHelper.getStudentEnrollmentCourses(snapshot,listOfCourseCodes.get(pos));
                                listOfRollNos.addAll(studentEnrollments);
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(UpdateAttendanceActivity.this,android.R.layout.simple_spinner_item,listOfRollNos);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                rollNum.setAdapter(adapter1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                alertFirebaseFailure(error);
                                error.toException();
                            }
                        });
                        break;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
