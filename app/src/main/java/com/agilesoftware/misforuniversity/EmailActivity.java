package com.agilesoftware.misforuniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmailActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    Spinner callList,roleList;
    EditText selectedContact,composedSubject,composedMessage;
    Button emailPerson;
    String[] roles;
    List<String> contactNames = new ArrayList<String>();
    List<String> emailIds = new ArrayList<String>();
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
        setContentView(R.layout.activity_email);
        s1 = findViewById(R.id.scroller);
        callList=findViewById(R.id.call_list);
        roleList=findViewById(R.id.role_selector);
        selectedContact=findViewById(R.id.selected_number);
        composedSubject=findViewById(R.id.composed_subject);
        composedMessage=findViewById(R.id.composed_message);
        emailPerson=findViewById(R.id.submit);
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
        roles=getResources().getStringArray(R.array.roles);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleList.setAdapter(adapter);
        roleList.setOnItemSelectedListener(this);
        emailPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int2=new Intent(Intent.ACTION_SEND);
                String[] clients = {selectedContact.getText().toString()};
                int2.putExtra(Intent.EXTRA_EMAIL, clients);
                int2.putExtra(Intent.EXTRA_SUBJECT, composedSubject.getText().toString());
                int2.putExtra(Intent.EXTRA_TEXT, composedMessage.getText().toString());
                int2.setType("message/rfc822");
                startActivity(int2);
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
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        ((TextView) parent.getChildAt(0)).setTextSize(25);
        ((TextView) parent.getChildAt(0)).setTextAppearance(EmailActivity.this, R.style.fontForNotificationLandingPage);
        switch (parent.getId()){
            case R.id.role_selector:
                switch (position){
                    case 0:
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Admin");
                        dbRef.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Map<String,List<String>> map = DatabaseHelper.getContactDetails(snapshot);
                                ArrayList<AdminDetails> adminDetails = DatabaseHelper.getAdminDetails(snapshot);
                                contactNames.clear();
                                emailIds.clear();
                                for (int i=0;i<adminDetails.size();i++){
                                    contactNames.add(adminDetails.get(i).getName());
                                    emailIds.add(adminDetails.get(i).getEMail());
                                }
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, contactNames);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                callList.setAdapter(adapter1);
                                callList.setOnItemSelectedListener(EmailActivity.this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                alertFirebaseFailure(error);
                                error.toException();
                            }
                        });
                        break;
                    case 1:
                        DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference("/Faculty");
                        dbRef1.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Map<String,List<String>> map = DatabaseHelper.getContactDetails(snapshot);
                                contactNames.clear();
                                emailIds.clear();
                                ArrayList<FacultyDetails> facultyDetails = DatabaseHelper.getFacultyDetails(snapshot);
                                for (int i=0;i<facultyDetails.size();i++){
                                    contactNames.add(facultyDetails.get(i).getName());
                                    emailIds.add(facultyDetails.get(i).getEMail());
                                }
                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, contactNames);
                                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                callList.setAdapter(adapter2);
                                callList.setOnItemSelectedListener(EmailActivity.this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                alertFirebaseFailure(error);
                                error.toException();
                            }
                        });
                        break;
                    case 2:
                        DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference("/Student");
                        dbRef2.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                contactNames.clear();
                                emailIds.clear();
                                ArrayList<StudentDetails> studentDetails = DatabaseHelper.getStudentDetails(snapshot);
                                for (int i=0;i<studentDetails.size();i++){
                                    contactNames.add(studentDetails.get(i).getName());
                                    emailIds.add(studentDetails.get(i).getEMail());
                                }
                                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, contactNames);
                                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                callList.setAdapter(adapter3);
                                callList.setOnItemSelectedListener(EmailActivity.this);
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
            case R.id.call_list:
                switch (position){
                    default:
                        selectedContact.setText(emailIds.get(position));
                }
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
