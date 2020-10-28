package com.agilesoftware.misforuniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateFaculty extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ScrollView s1;
    Button ib1;
    TextView tmain;
    EditText name,email,address,age,contact,joindate;
    Spinner department,position;
    RadioButton ge1,ge2;
    String[] departments,positions;
    String EmailID, Gender, password, userKey;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay;
    private String index;
    private String mDate;
    ArrayList<FacultyDetails> facultyDetails = new ArrayList<FacultyDetails>();
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
        setContentView(R.layout.activity_update_faculty);
        s1=findViewById(R.id.scroller);
        tmain=findViewById(R.id.main_txt);
        registerForContextMenu(tmain);
        ib1=findViewById(R.id.submit);
        name=findViewById(R.id.edt_name);
        email=findViewById(R.id.edt_email);
        address=findViewById(R.id.edt_address);
        age=findViewById(R.id.edt_age);
        contact=findViewById(R.id.edt_phone);
        joindate=findViewById(R.id.edt_date);
        department=findViewById(R.id.edt_department);
        position=findViewById(R.id.edt_position);
        ge1=findViewById(R.id.rb_male);
        ge2=findViewById(R.id.rb_female);
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
        departments=getResources().getStringArray(R.array.department);
        positions=getResources().getStringArray(R.array.position);

        EmailID=getIntent().getExtras().getString("Email");
        index=getIntent().getExtras().getString("Index");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Faculty");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                facultyDetails = DatabaseHelper.getFacultyDetailsEmail(snapshot,EmailID);
                name.setText(facultyDetails.get(0).getName());
                email.setText(facultyDetails.get(0).getEMail());
                address.setText(facultyDetails.get(0).getAddress());
                age.setText(String.valueOf(facultyDetails.get(0).getAge()));
                contact.setText(String.valueOf(facultyDetails.get(0).getContactNo()));
                if (facultyDetails.get(0).getGender().matches("Male")){
                    ge1.setChecked(true);
                }else if (facultyDetails.get(0).getGender().matches("Female")){
                    ge2.setChecked(true);
                }
                String dept = facultyDetails.get(0).getDepartment();
                String pos = facultyDetails.get(0).getPosition();
                joindate.setText(facultyDetails.get(0).getJoinDate());
                password = facultyDetails.get(0).getPassword();

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateFaculty.this, android.R.layout.simple_spinner_item, departments);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                department.setAdapter(adapter);
                int spinner1Position = 0;
                spinner1Position = adapter.getPosition(dept);
                department.setSelection(spinner1Position);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(UpdateFaculty.this, android.R.layout.simple_spinner_item, positions);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                position.setAdapter(adapter1);
                int spinner2Position = 0;
                spinner2Position = adapter1.getPosition(pos);
                position.setSelection(spinner2Position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertFirebaseFailure(error);
                error.toException();
            }
        });

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().matches("") || email.getText().toString().matches("") ||
                        address.getText().toString().matches("") || age.getText().toString().matches("") ||
                        contact.getText().toString().matches("") || joindate.getText().toString().matches("") ||
                        department.getSelectedItem().toString().matches("") || position.getSelectedItem().toString().matches("")){
                    Toast.makeText(UpdateFaculty.this, "Some Text Field is Empty...", Toast.LENGTH_SHORT).show();
                }
                else {
                    final Intent int1=new Intent(UpdateFaculty.this,ManageFaculties.class);
                    LayoutInflater li1=getLayoutInflater();
                    final View layout=li1.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
                    TextView txt= (TextView) layout.findViewById(R.id.text_toast);
                    txt.setText("Faculty Updated Successfully!");

                    final String Name = name.getText().toString();
                    final String EMail = email.getText().toString();
                    final String Address = address.getText().toString();
                    final int Age = Integer.parseInt(age.getText().toString());
                    final long ContactNo = Long.parseLong(contact.getText().toString());
                    if(ge1.isChecked()){
                        Gender = "Male";
                    }
                    else if (ge2.isChecked()){
                        Gender = "Female";
                    }
                    final String JoinDate = joindate.getText().toString();
                    final String Department = department.getSelectedItem().toString();
                    final String Position = position.getSelectedItem().toString();

                    String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                    //String emailPatternTwo = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
                    if (Age < 17){
                        Toast.makeText(getApplicationContext(), "You Are Not Eligible!", Toast.LENGTH_LONG).show();
                    }else if (!EMail.matches(emailPattern)){
                        Toast.makeText(getApplicationContext(), "Invalid E-Mail Address...", Toast.LENGTH_LONG).show();
                    }else if (String.valueOf(ContactNo).length()!=10){
                        Toast.makeText(getApplicationContext(), "Mobile Number Should have 10 Digits...", Toast.LENGTH_LONG).show();
                    }else {
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Faculty");
                        dbRef.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userKey = DatabaseHelper.retrieveKey(snapshot,EMail);
                                Log.d("Faculty Key", userKey);
                                FacultyDetails facultyDetails = new FacultyDetails(Name,EMail,Address,Age,ContactNo,Gender,Department,Position,JoinDate,password);
                                DatabaseHelper.updateFaculty(userKey,facultyDetails);
                                Toast toast=new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                startActivity(int1);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                alertFirebaseFailure(error);
                                error.toException();
                            }
                        });
                    }
                }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater cmi = getMenuInflater();
        cmi.inflate(R.menu.menu_items1,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
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

    public void setDate(View v){
        Calendar mcurrentDate = Calendar.getInstance();
        int mmYear = mcurrentDate.get(Calendar.YEAR);
        int mmMonth = mcurrentDate.get(Calendar.MONTH);
        int mmDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(UpdateFaculty.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear ++;
                mDay = dayOfMonth;
                mMonth = monthOfYear;
                mYear = year;
                mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
                joindate.setText(mDate);
            }
        }, mmYear, mmMonth, mmDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month ++;
        mDay = dayOfMonth;
        mMonth = month;
        mYear = year;
        mDate = dayOfMonth + "/" + month + "/" + year;
        joindate.setText(mDate);
    }
}
