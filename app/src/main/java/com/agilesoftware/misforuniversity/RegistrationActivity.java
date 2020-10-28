package com.agilesoftware.misforuniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistrationActivity extends AppCompatActivity {
    ScrollView s1;
    Button ib1;
    TextView tname,temail,taddress,tage,tcontact,tpass,tconfirmpass,tgender,tmain;
    EditText name,email,address,age,contact,pass,confirmpass;
    RadioButton ge1,ge2;
    CheckBox ch1;
    DBHelper dbHelper;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Email = "emailKey";
    public static final String Theme = "themeKey";

    public static String md5(String input) {
        String md5 = null;
        if(null == input) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes(), 0, input.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        s1=findViewById(R.id.scroller);
        ib1=findViewById(R.id.submit);
        ge1=findViewById(R.id.rb_male);
        ge2=findViewById(R.id.rb_female);
        ch1=findViewById(R.id.chk_terms);
        tmain=findViewById(R.id.main_txt);
        registerForContextMenu(tmain);
        tname=findViewById(R.id.txt_name);
        temail=findViewById(R.id.txt_email);
        taddress=findViewById(R.id.txt_address);
        tage=findViewById(R.id.txt_age);
        tcontact=findViewById(R.id.txt_phone);
        tpass=findViewById(R.id.txt_pass);
        tconfirmpass=findViewById(R.id.txt_confirm_pass);
        tgender=findViewById(R.id.txt_gender);
        name=findViewById(R.id.edt_name);
        email=findViewById(R.id.edt_email);
        address=findViewById(R.id.edt_address);
        age=findViewById(R.id.edt_age);
        contact=findViewById(R.id.edt_phone);
        pass=findViewById(R.id.edt_pass);
        confirmpass=findViewById(R.id.edt_confirm_pass);
        dbHelper=new DBHelper(this);
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

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch1.isChecked() && (confirmpass.getText().toString().matches(pass.getText().toString())))
                {
                    if (name.getText().toString().matches("") || email.getText().toString().matches("") ||
                            address.getText().toString().matches("") || age.getText().toString().matches("") ||
                            contact.getText().toString().matches("") || confirmpass.getText().toString().matches("") ||
                            pass.getText().toString().matches("")){
                        Toast.makeText(RegistrationActivity.this, "Some Text Field is Empty...", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent int1=new Intent(RegistrationActivity.this,RelativeLoginActivity.class);
                        LayoutInflater li1=getLayoutInflater();
                        View layout=li1.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView txt= (TextView) layout.findViewById(R.id.text_toast);
                        txt.setText("Registered Successfully as Admin!");

                        String Name = name.getText().toString();
                        String EMail = email.getText().toString();
                        String Address = address.getText().toString();
                        int Age = Integer.parseInt(age.getText().toString());
                        long ContactNo = Long.parseLong(contact.getText().toString());
                        String Gender = "None";
                        if(ge1.isChecked()){
                            Gender = "Male";
                        }
                        else if (ge2.isChecked()){
                            Gender = "Female";
                        }
                        String Password = pass.getText().toString();

                        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                        if (Age < 17){
                            Toast.makeText(getApplicationContext(), "You Are Not Eligible!", Toast.LENGTH_LONG).show();
                        }else if (!EMail.matches(emailPattern)){
                            Toast.makeText(getApplicationContext(), "Invalid E-Mail Address...", Toast.LENGTH_LONG).show();
                        }else if (String.valueOf(ContactNo).length()!=10){
                            Toast.makeText(getApplicationContext(), "Mobile Number Should have 10 Digits...", Toast.LENGTH_LONG).show();
                        }else {
                            AdminDetails adminDetails = new AdminDetails(Name,EMail,Address,Age,ContactNo,Gender,md5(Password));
                            DatabaseHelper.addNewAdmin(adminDetails);
                            Toast toast=new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            startActivity(int1);
                            finish();
                        }
                    }
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Checkbox Not Checked/Password Doesn't Match with Confirm Password", Toast.LENGTH_LONG).show();
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
}
