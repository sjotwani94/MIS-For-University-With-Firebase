package com.agilesoftware.misforuniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class SpinnerCourseActivity extends AppCompatActivity {
    Spinner coursesList, rollNosList;
    Button submit;
    List<String> listOfCourses = new ArrayList<String>();
    List<String> listOfRollNos = new ArrayList<String>();
    List<String> listOfCourseCodes = new ArrayList<String>();
    List<String> listOfCourseNames = new ArrayList<String>();
    ArrayList<StudentDetails> studentDetails = new ArrayList<StudentDetails>();
    ArrayList<CourseDetails> courseDetails = new ArrayList<CourseDetails>();
    String selectedCourse,selectedCourseName,selectedRollNo;
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
        setContentView(R.layout.activity_spinner_course);
        s1 = findViewById(R.id.scroller);
        coursesList=findViewById(R.id.list_courses);
        rollNosList=findViewById(R.id.list_roll_nos);
        submit=findViewById(R.id.submit_course);
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
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Student");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentDetails = DatabaseHelper.getStudentDetails(snapshot);
                for (int i =0; i<studentDetails.size();i++)
                {
                    listOfRollNos.add(studentDetails.get(i).getRollNo());
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SpinnerCourseActivity.this, android.R.layout.simple_spinner_item, listOfRollNos);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                rollNosList.setAdapter(adapter1);
                rollNosList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (parent.getId()){
                            case R.id.list_roll_nos:
                                switch (position){
                                    default:
                                        selectedRollNo = listOfRollNos.get(position);
                                        break;
                                }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertFirebaseFailure(error);
                error.toException();
            }
        });
        DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference("/Courses");
        dbRef1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseDetails = DatabaseHelper.getCourseDetails(snapshot);
                for (int i =0; i<courseDetails.size();i++)
                {
                    listOfCourseCodes.add(courseDetails.get(i).getCourseCode());
                    listOfCourseNames.add(courseDetails.get(i).getCourseName());
                    listOfCourses.add(courseDetails.get(i).getCourseCode()+" ("+courseDetails.get(i).getCourseName()+")");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SpinnerCourseActivity.this, android.R.layout.simple_spinner_item, listOfCourses);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                coursesList.setAdapter(adapter);
                coursesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (parent.getId()){
                            case R.id.list_courses:
                                switch (position){
                                    default:
                                        selectedCourse = listOfCourseCodes.get(position);
                                        selectedCourseName = listOfCourseNames.get(position);
                                        break;
                                }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
                StudentEnrollments studentEnrollments = new StudentEnrollments(selectedRollNo,selectedCourse,selectedCourseName,0,0,0,0,0,0,0);
                DatabaseHelper.addStudentEnrollment(studentEnrollments);
                Toast.makeText(getApplicationContext(), "Enrollment Successful", Toast.LENGTH_LONG).show();
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
