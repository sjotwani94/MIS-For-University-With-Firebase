package com.agilesoftware.misforuniversity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetEmailFragment extends Fragment {
    TextInputEditText UserEmailID;
    Button sendEmailOTP;
    OnNameSetListener onNameSetListener;
    //public ForgotPassword forgotPassword;
    int min = 100000;
    int max = 999999;
    int randomInt;
    public GetEmailFragment() {
        // Required empty public constructor
    }

    public void alertFirebaseFailure(DatabaseError error) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext())
                .setTitle("An error occurred while connecting to Firebase!")
                .setMessage(error.toString())
                .setPositiveButton("Dismiss", null)
                .setIcon(android.R.drawable.presence_busy)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_email, container, false);
        //forgotPassword = (ForgotPassword) getActivity();
        final String userRole = getActivity().getIntent().getStringExtra("Role");
        UserEmailID = view.findViewById(R.id.get_user_email);
        sendEmailOTP = view.findViewById(R.id.submit);

        sendEmailOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbRef = null;
                Log.d("User Role", userRole);
                if (userRole.matches("Admin")){
                    dbRef = FirebaseDatabase.getInstance().getReference("/Admin");
                }else if (userRole.matches("Faculty")){
                    dbRef = FirebaseDatabase.getInstance().getReference("/Faculty");
                }else if (userRole.matches("Student")){
                    dbRef = FirebaseDatabase.getInstance().getReference("/Student");
                }
                final String userEmailID = UserEmailID.getText().toString();
                dbRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean result = DatabaseHelper.verifyEmailID(snapshot,userEmailID);
                        if (result){
                            randomInt = (int)(Math.random() * (max - min + 1) + min);
                            sendEmailforOTP(userEmailID,randomInt);
                            Snackbar snackbar = Snackbar.make(sendEmailOTP,"OTP Sent on your registered Email-ID.",Snackbar.LENGTH_LONG);
                            snackbar.show();
                            onNameSetListener.setGeneralDetails(userEmailID,randomInt);
                        }else{
                            Snackbar snackbar = Snackbar.make(sendEmailOTP,"You are not registered.",Snackbar.LENGTH_LONG);
                            snackbar.show();
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
        return view;
    }

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        try {
            onNameSetListener = (OnNameSetListener) activity;
        }
        catch(Exception e){
        }
    }

    public void sendEmailforOTP(String recipient,int otp){
        //Creating SendMail object
        String message = "Dear User,"
                + "\n\t Your OTP for resetting your password is "+otp+"." +"\n\nThanks and Regards,\nAdministrator - EVS";
        SendMail sm = new SendMail(getActivity(), recipient, "Reset Password for Management Information System", message);
        sm.execute();
    }

    public interface OnNameSetListener {
        public void setGeneralDetails(String emailID, int otp);
    }
}