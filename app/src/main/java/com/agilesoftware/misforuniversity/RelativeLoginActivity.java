package com.agilesoftware.misforuniversity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class RelativeLoginActivity extends AppCompatActivity {
    Button b1,b2;
    EditText e1,e2;
    TextView forgotPassword;
    ScrollView mainLayout;
    DBHelper dbHelper;
    Drawable drawable;
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
        setContentView(R.layout.activity_relative_login);

        b1=findViewById(R.id.login);
        b2=findViewById(R.id.register);
        e1=findViewById(R.id.edt_name);
        e2=findViewById(R.id.edt_pwd);
        forgotPassword=findViewById(R.id.forgot_password);
        dbHelper = new DBHelper(this);
        mainLayout=findViewById(R.id.main_layout);
        drawable = mainLayout.getBackground();
        sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
        if(sharedpreferences.contains(Email))
        {
            e1.setText(sharedpreferences.getString(Email, ""));
        }
        if (sharedpreferences.contains(Theme)){
            if (sharedpreferences.getString(Theme,"").matches("Light")){
                mainLayout.setBackgroundResource(R.drawable.navy);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_yellow)));
                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getSupportActionBar().getTitle() + "</font>")));
                drawable = mainLayout.getBackground();
            }else if (sharedpreferences.getString(Theme,"").matches("Dark")){
                mainLayout.setBackgroundResource(R.drawable.blackcar);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_black)));
                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#0000FF\">" + getSupportActionBar().getTitle() + "</font>")));
                drawable = mainLayout.getBackground();
            }
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (e1.getText().toString().matches("") || e2.getText().toString().matches("")){
                    Toast.makeText(RelativeLoginActivity.this, "Please Enter All Credentials...", Toast.LENGTH_SHORT).show();
                }
                else {
                    final String userEmailID = e1.getText().toString();
                    final String userPwd = e2.getText().toString();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RelativeLoginActivity.this);
                    dialog.setTitle("Role of User");
                    dialog.setMessage("How do you want to login as?");
                    dialog.setPositiveButton("Faculty", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Faculty");
                            dbRef.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int result = DatabaseHelper.fetchLoginDetails(snapshot,userEmailID,userPwd);
                                    if (result==0){
                                        Snackbar snackbar = Snackbar.make(b1,"Successfully Logged in as Faculty",Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        if(userPwd.matches("changeme")){
                                            Intent facultyIntent = new Intent(RelativeLoginActivity.this, ResetPasswordActivity.class);
                                            Bundle bundle = new Bundle();
                                            ArrayList<FacultyDetails> facultyDetails = DatabaseHelper.getFacultyDetailsEmail(snapshot,userEmailID);
                                            String name = facultyDetails.get(0).getName();
                                            String department = facultyDetails.get(0).getDepartment();
                                            bundle.putString("Position","Faculty");
                                            bundle.putString("Email",userEmailID);
                                            bundle.putString("Password",userPwd);
                                            bundle.putString("Name",name);
                                            bundle.putString("Department",department);
                                            facultyIntent.putExtras(bundle);
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString(Email, userEmailID);
                                            editor.commit();
                                            startActivity(facultyIntent);
                                            finish();
                                        }else {
                                            Intent facultyIntent = new Intent(RelativeLoginActivity.this, FacultyHomePage.class);
                                            Bundle bundle = new Bundle();
                                            ArrayList<FacultyDetails> facultyDetails = DatabaseHelper.getFacultyDetailsEmail(snapshot,userEmailID);
                                            String name = facultyDetails.get(0).getName();
                                            String department = facultyDetails.get(0).getDepartment();
                                            bundle.putString("Position","Faculty");
                                            bundle.putString("Email",userEmailID);
                                            bundle.putString("Password",userPwd);
                                            bundle.putString("Name",name);
                                            bundle.putString("Department",department);
                                            facultyIntent.putExtras(bundle);
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString(Email, userEmailID);
                                            editor.commit();
                                            startActivity(facultyIntent);
                                            finish();
                                        }
                                    }else if (result==1){
                                        Snackbar snackbar = Snackbar.make(b1,"Invalid Credentials for Faculty",Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    alertFirebaseFailure(error);
                                    error.toException();
                                }
                            });
                            onResume();
                        }
                    });

                    dialog.setNegativeButton("Student", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Student");
                            dbRef.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int result = DatabaseHelper.fetchLoginDetails(snapshot,userEmailID,userPwd);
                                    if (result==0){
                                        Snackbar snackbar = Snackbar.make(b1,"Successfully Logged in as Student",Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        if(userPwd.matches("changeme")){
                                            Intent studentIntent = new Intent(RelativeLoginActivity.this, ResetPasswordActivity.class);
                                            Bundle bundle = new Bundle();
                                            ArrayList<StudentDetails> studentDetails = DatabaseHelper.getStudentDetailsEmail(snapshot,userEmailID);
                                            String name=studentDetails.get(0).getName();
                                            String department=studentDetails.get(0).getDepartment();
                                            String rollno=studentDetails.get(0).getRollNo();
                                            int yearofpass=studentDetails.get(0).getBatch();
                                            bundle.putString("Name",name);
                                            bundle.putString("Department",department);
                                            bundle.putString("RollNo",rollno);
                                            bundle.putInt("YearOfPass",yearofpass);
                                            bundle.putString("Position","Student");
                                            bundle.putString("Email",userEmailID);
                                            bundle.putString("Password",userPwd);
                                            studentIntent.putExtras(bundle);
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString(Email, userEmailID);
                                            editor.commit();
                                            startActivity(studentIntent);
                                            finish();
                                        }else {
                                            Intent studentIntent = new Intent(RelativeLoginActivity.this, StudentHomePage.class);
                                            Bundle bundle = new Bundle();
                                            ArrayList<StudentDetails> studentDetails = DatabaseHelper.getStudentDetailsEmail(snapshot,userEmailID);
                                            String name=studentDetails.get(0).getName();
                                            String department=studentDetails.get(0).getDepartment();
                                            String rollno=studentDetails.get(0).getRollNo();
                                            int yearofpass=studentDetails.get(0).getBatch();
                                            bundle.putString("Name",name);
                                            bundle.putString("Department",department);
                                            bundle.putString("RollNo",rollno);
                                            bundle.putInt("YearOfPass",yearofpass);
                                            bundle.putString("Position","Student");
                                            bundle.putString("Email",userEmailID);
                                            bundle.putString("Password",userPwd);
                                            studentIntent.putExtras(bundle);
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString(Email, userEmailID);
                                            editor.commit();
                                            startActivity(studentIntent);
                                            finish();
                                        }
                                    }else if (result==1){
                                        Snackbar snackbar = Snackbar.make(b1,"Invalid Credentials for Student",Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    alertFirebaseFailure(error);
                                    error.toException();
                                }
                            });
                            onResume();
                        }
                    });

                    dialog.setNeutralButton("Admin", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/Admin");
                            dbRef.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int result = DatabaseHelper.fetchLoginDetails(snapshot,userEmailID,userPwd);
                                    if (result==0){
                                        Snackbar snackbar = Snackbar.make(b1,"Successfully Logged in as Admin",Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        Intent adminIntent = new Intent(RelativeLoginActivity.this, AdminHomePage.class);
                                        ArrayList<AdminDetails> adminDetails = DatabaseHelper.getAdminDetailsEmail(snapshot,userEmailID);
                                        String name=adminDetails.get(0).getName();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("Position","Admin");
                                        bundle.putString("Email",userEmailID);
                                        bundle.putString("Password",userPwd);
                                        bundle.putString("Name",name);
                                        adminIntent.putExtras(bundle);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Email, userEmailID);
                                        editor.commit();
                                        startActivity(adminIntent);
                                        finish();
                                    }else if (result==1){
                                        Snackbar snackbar = Snackbar.make(b1,"Invalid Credentials for Admin",Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    alertFirebaseFailure(error);
                                    error.toException();
                                }
                            });
                            onResume();
                        }
                    });
                    dialog.show();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RelativeLoginActivity.this, RegistrationActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(RelativeLoginActivity.this);
                dialog.setTitle("Role");
                dialog.setMessage("What is your role in Management Information System?");
                dialog.setPositiveButton("Faculty", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(RelativeLoginActivity.this,ForgotPassword.class);
                        intent.putExtra("Role","Faculty");
                        startActivity(intent);
                        onResume();
                    }
                });

                dialog.setNegativeButton("Student", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(RelativeLoginActivity.this,ForgotPassword.class);
                        intent.putExtra("Role","Student");
                        startActivity(intent);
                        onResume();
                    }
                });

                dialog.setNeutralButton("Admin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(RelativeLoginActivity.this,ForgotPassword.class);
                        intent.putExtra("Role","Admin");
                        startActivity(intent);
                        onResume();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mainLayout.getBackground().getConstantState()==getResources().getDrawable(R.drawable.navy).getConstantState()
        || mainLayout.getBackground().getConstantState()==getResources().getDrawable(R.drawable.blackcar).getConstantState()){
            if (sharedpreferences.contains(Theme)){
                if (sharedpreferences.getString(Theme,"").matches("Light")){
                    mainLayout.setBackgroundResource(R.drawable.navy);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_yellow)));
                    getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getSupportActionBar().getTitle() + "</font>")));
                }else if (sharedpreferences.getString(Theme,"").matches("Dark")){
                    mainLayout.setBackgroundResource(R.drawable.blackcar);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_black)));
                    getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#0000FF\">" + getSupportActionBar().getTitle() + "</font>")));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit From App");
        dialog.setMessage("Do You really want to exit from the application?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onResume();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.camera:
                if (isPermissionGranted(1)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.gallery:
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                break;
            case R.id.open_calc:
                Intent intent1 = new Intent(getApplicationContext(),CalculatorActivity.class);
                startActivity(intent1);
                break;
            case R.id.change_theme:
                if (drawable.getConstantState()==getResources().getDrawable(R.drawable.navy).getConstantState()){
                    mainLayout.setBackgroundResource(R.drawable.blackcar);
                    drawable = mainLayout.getBackground();
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_black)));
                    getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#0000FF\">" + getSupportActionBar().getTitle() + "</font>")));
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Theme, "Dark");
                    editor.commit();
                } else {
                    mainLayout.setBackgroundResource(R.drawable.navy);
                    drawable = mainLayout.getBackground();
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.simple_yellow)));
                    getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getSupportActionBar().getTitle() + "</font>")));
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Theme, "Light");
                    editor.commit();
                }
                break;
            case R.id.exit:
                System.exit(0);
                break;
        }
        return true;
    }

    public boolean isPermissionGranted(int no){
        if(Build.VERSION.SDK_INT>22)
        {
            if(no==1)
            {
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
                    return false;
                }
                else {
                    return true;
                }
            }
        }
        else {
            return true;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                mainLayout.setBackground(new BitmapDrawable(getResources(),photo));
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bmp = null;
                try {
                    bmp = getBitmapFromUri(selectedImage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mainLayout.setBackground(new BitmapDrawable(getResources(),bmp));
            }
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
