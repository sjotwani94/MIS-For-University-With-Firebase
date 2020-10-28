package com.agilesoftware.misforuniversity;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public final class DatabaseHelper {

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

    public static final int fetchLoginDetails(@NonNull DataSnapshot snapshot, String EmailID, String pwd) {
        for (DataSnapshot voter : snapshot.getChildren()) {
            String key = voter.getKey();
            String emailID = voter.child("email").getValue(String.class);
            String Password = voter.child("password").getValue(String.class);
            if (emailID.matches(EmailID) && Password.matches(md5(pwd))){
                return 0;
            }else if (emailID.matches(EmailID) && Password.matches(pwd) && pwd.matches("changeme")){
                return 0;
            }
        }
        return 1;
    }

    public static final String retrieveKey(@NonNull DataSnapshot snapshot, String EmailID) {
        for (DataSnapshot user : snapshot.getChildren()) {
            String key = user.getKey();
            String emailID = user.child("email").getValue(String.class);
            if (emailID.matches(EmailID)){
                return key;
            }
        }
        return null;
    }

    public static final String retrieveCourseKey(@NonNull DataSnapshot snapshot, String CourseCode) {
        for (DataSnapshot user : snapshot.getChildren()) {
            String key = user.getKey();
            String courseCode = user.child("courseCode").getValue(String.class);
            if (courseCode.matches(CourseCode)){
                return key;
            }
        }
        return null;
    }

    public static final String retrieveEnrollmentKey(@NonNull DataSnapshot snapshot, String RollNo, String CourseCode) {
        for (DataSnapshot user : snapshot.getChildren()) {
            String key = user.getKey();
            String courseCode = user.child("courseCode").getValue(String.class);
            String rollNo = user.child("rollNo").getValue(String.class);
            if (courseCode.matches(CourseCode) && rollNo.matches(RollNo)){
                return key;
            }
        }
        return null;
    }

    public static final ArrayList<AdminDetails> getAdminDetails(@NonNull DataSnapshot snapshot) {
        ArrayList<AdminDetails> adminDetails = new ArrayList<AdminDetails>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String name = user.child("name").getValue(String.class);
            String email = user.child("email").getValue(String.class);
            String address = user.child("address").getValue(String.class);
            int age = user.child("age").getValue(Integer.class);
            long contactno = user.child("contactNo").getValue(Long.class);
            String gender = user.child("gender").getValue(String.class);
            String password = user.child("password").getValue(String.class);
            adminDetails.add(new AdminDetails(name,email,address,age,contactno,gender,password));
        }
        return adminDetails;
    }

    public static final ArrayList<AdminDetails> getAdminDetailsEmail(@NonNull DataSnapshot snapshot, String EMail) {
        ArrayList<AdminDetails> adminDetails = new ArrayList<AdminDetails>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String name = user.child("name").getValue(String.class);
            String email = user.child("email").getValue(String.class);
            String address = user.child("address").getValue(String.class);
            int age = user.child("age").getValue(Integer.class);
            long contactno = user.child("contactNo").getValue(Long.class);
            String gender = user.child("gender").getValue(String.class);
            String password = user.child("password").getValue(String.class);
            if (email.matches(EMail)){
                adminDetails.add(new AdminDetails(name,email,address,age,contactno,gender,password));
            }
        }
        return adminDetails;
    }

    public static final ArrayList<FacultyDetails> getFacultyDetails(@NonNull DataSnapshot snapshot) {
        ArrayList<FacultyDetails> facultyDetails = new ArrayList<FacultyDetails>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String name = user.child("name").getValue(String.class);
            String email = user.child("email").getValue(String.class);
            String address = user.child("address").getValue(String.class);
            int age = user.child("age").getValue(Integer.class);
            long contactno = user.child("contactNo").getValue(Long.class);
            String gender = user.child("gender").getValue(String.class);
            String department = user.child("department").getValue(String.class);
            String position = user.child("position").getValue(String.class);
            String joindate = user.child("joinDate").getValue(String.class);
            String password = user.child("password").getValue(String.class);
            facultyDetails.add(new FacultyDetails(name,email,address,age,contactno,gender,department,position,joindate,password));
        }
        return facultyDetails;
    }

    public static final ArrayList<FacultyDetails> getFacultyDetailsEmail(@NonNull DataSnapshot snapshot, String EMail) {
        ArrayList<FacultyDetails> facultyDetails = new ArrayList<FacultyDetails>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String name = user.child("name").getValue(String.class);
            String email = user.child("email").getValue(String.class);
            String address = user.child("address").getValue(String.class);
            int age = user.child("age").getValue(Integer.class);
            long contactno = user.child("contactNo").getValue(Long.class);
            String gender = user.child("gender").getValue(String.class);
            String department = user.child("department").getValue(String.class);
            String position = user.child("position").getValue(String.class);
            String joindate = user.child("joinDate").getValue(String.class);
            String password = user.child("password").getValue(String.class);
            if (email.matches(EMail)){
                facultyDetails.add(new FacultyDetails(name,email,address,age,contactno,gender,department,position,joindate,password));
            }
        }
        return facultyDetails;
    }

    public static final ArrayList<StudentDetails> getStudentDetails(@NonNull DataSnapshot snapshot) {
        ArrayList<StudentDetails> studentDetails = new ArrayList<StudentDetails>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String name = user.child("name").getValue(String.class);
            String email = user.child("email").getValue(String.class);
            String address = user.child("address").getValue(String.class);
            int age = user.child("age").getValue(Integer.class);
            long contactno = user.child("contactNo").getValue(Long.class);
            String gender = user.child("gender").getValue(String.class);
            String department = user.child("department").getValue(String.class);
            String rollno = user.child("rollNo").getValue(String.class);
            int batch = user.child("batch").getValue(Integer.class);
            String password = user.child("password").getValue(String.class);
            studentDetails.add(new StudentDetails(name,email,address,age,contactno,gender,department,rollno,batch,password));
        }
        return studentDetails;
    }

    public static final ArrayList<StudentDetails> getStudentDetailsEmail(@NonNull DataSnapshot snapshot, String EMail) {
        ArrayList<StudentDetails> studentDetails = new ArrayList<StudentDetails>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String name = user.child("name").getValue(String.class);
            String email = user.child("email").getValue(String.class);
            String address = user.child("address").getValue(String.class);
            int age = user.child("age").getValue(Integer.class);
            long contactno = user.child("contactNo").getValue(Long.class);
            String gender = user.child("gender").getValue(String.class);
            String department = user.child("department").getValue(String.class);
            String rollno = user.child("rollNo").getValue(String.class);
            int batch = user.child("batch").getValue(Integer.class);
            String password = user.child("password").getValue(String.class);
            if (email.matches(EMail)){
                studentDetails.add(new StudentDetails(name,email,address,age,contactno,gender,department,rollno,batch,password));
            }
        }
        return studentDetails;
    }

    public static final boolean verifyEmailID(@NonNull DataSnapshot snapshot, String EmailID) {
        for (DataSnapshot voter : snapshot.getChildren()) {
            String emailID = voter.child("email").getValue(String.class);
            if (emailID.matches(EmailID)){
                return true;
            }
        }
        return false;
    }

    public static final void addNewAdmin(AdminDetails newAdmin) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/Admin");
        DatabaseReference newIssueRef = rootRef.push();
        newIssueRef.setValue(newAdmin);
    }

    public static final void addNewFaculty(FacultyDetails newFaculty) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/Faculty");
        DatabaseReference newIssueRef = rootRef.push();
        newIssueRef.setValue(newFaculty);
    }

    public static final void updateFaculty(String key,FacultyDetails newFaculty) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/Faculty/"+key);
        rootRef.setValue(newFaculty);
    }

    public static final void addNewStudent(StudentDetails newStudent) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/Student");
        DatabaseReference newIssueRef = rootRef.push();
        newIssueRef.setValue(newStudent);
    }

    public static final void updateStudent(String key,StudentDetails newStudent) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/Student/"+key);
        rootRef.setValue(newStudent);
    }

    public static final void addNewCourse(CourseDetails newCourse) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/Courses");
        DatabaseReference newIssueRef = rootRef.push();
        newIssueRef.setValue(newCourse);
    }

    public static final void addStudentEnrollment(StudentEnrollments newEnrollment) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/StudentEnrollments");
        DatabaseReference newIssueRef = rootRef.push();
        newIssueRef.setValue(newEnrollment);
    }

    public static final void updateStudentEnrollment(StudentEnrollments newEnrollment, String coursekey) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/StudentEnrollments/"+coursekey);
        rootRef.setValue(newEnrollment);
    }

    public static final ArrayList<StudentEnrollments> getStudentEnrollmentDetails(@NonNull DataSnapshot snapshot, String RollNo, String CourseCode) {
        ArrayList<StudentEnrollments> studentEnrollments = new ArrayList<StudentEnrollments>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String rollNo = user.child("rollNo").getValue(String.class);
            String courseCode = user.child("courseCode").getValue(String.class);
            String courseName = user.child("courseName").getValue(String.class);
            int classTest = user.child("classTest").getValue(Integer.class);
            int midSem = user.child("midSem").getValue(Integer.class);
            int assignments = user.child("assignments").getValue(Integer.class);
            int labPracticals = user.child("labPracticals").getValue(Integer.class);
            int finalExam = user.child("finalExam").getValue(Integer.class);
            int totalPresence = user.child("totalPresence").getValue(Integer.class);
            int totalLectures = user.child("totalLectures").getValue(Integer.class);
            if (rollNo.matches(RollNo) && courseCode.matches(CourseCode)){
                studentEnrollments.add(new StudentEnrollments(rollNo,courseCode,courseName,classTest,midSem,assignments,labPracticals,finalExam,totalPresence,totalLectures));
            }
        }
        return studentEnrollments;
    }

    public static final ArrayList<StudentEnrollments> getStudentEnrollmentRollNo(@NonNull DataSnapshot snapshot, String RollNo) {
        ArrayList<StudentEnrollments> studentEnrollments = new ArrayList<StudentEnrollments>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String rollNo = user.child("rollNo").getValue(String.class);
            String courseCode = user.child("courseCode").getValue(String.class);
            String courseName = user.child("courseName").getValue(String.class);
            int classTest = user.child("classTest").getValue(Integer.class);
            int midSem = user.child("midSem").getValue(Integer.class);
            int assignments = user.child("assignments").getValue(Integer.class);
            int labPracticals = user.child("labPracticals").getValue(Integer.class);
            int finalExam = user.child("finalExam").getValue(Integer.class);
            int totalPresence = user.child("totalPresence").getValue(Integer.class);
            int totalLectures = user.child("totalLectures").getValue(Integer.class);
            if (rollNo.matches(RollNo)){
                studentEnrollments.add(new StudentEnrollments(rollNo,courseCode,courseName,classTest,midSem,assignments,labPracticals,finalExam,totalPresence,totalLectures));
            }
        }
        return studentEnrollments;
    }

    public static final ArrayList<String> getStudentEnrollmentCourses(@NonNull DataSnapshot snapshot, String CourseCode) {
        ArrayList<String> studentEnrollments = new ArrayList<String>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String rollNo = user.child("rollNo").getValue(String.class);
            String courseCode = user.child("courseCode").getValue(String.class);
            if (courseCode.matches(CourseCode)){
                studentEnrollments.add(rollNo);
            }
        }
        return studentEnrollments;
    }

    public static final ArrayList<CourseDetails> getCourseDetails(@NonNull DataSnapshot snapshot) {
        ArrayList<CourseDetails> courseDetails = new ArrayList<CourseDetails>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String name = user.child("courseName").getValue(String.class);
            String code = user.child("courseCode").getValue(String.class);
            String description = user.child("courseDescription").getValue(String.class);
            int semester = user.child("semester").getValue(Integer.class);
            String prerequisites = user.child("coursePrerequisites").getValue(String.class);
            courseDetails.add(new CourseDetails(code,name,description,semester,prerequisites));
        }
        return courseDetails;
    }

    public static final ArrayList<CourseDetails> getCourseDetailsWithFilter(@NonNull DataSnapshot snapshot, String filter) {
        ArrayList<CourseDetails> courseDetails = new ArrayList<CourseDetails>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String name = user.child("courseName").getValue(String.class);
            String code = user.child("courseCode").getValue(String.class);
            String description = user.child("courseDescription").getValue(String.class);
            int semester = user.child("semester").getValue(Integer.class);
            String prerequisites = user.child("coursePrerequisites").getValue(String.class);
            if (code.contains(filter)){
                courseDetails.add(new CourseDetails(code,name,description,semester,prerequisites));
            }
        }
        return courseDetails;
    }

    public static final void deleteCourse(String keyToDelete) {
        DatabaseReference issueRef = FirebaseDatabase.getInstance().getReference("/Courses/" + keyToDelete);
        issueRef.removeValue();
    }

    public static final void addNewNotice(NoticeDetails newNotice) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/Notices");
        DatabaseReference newIssueRef = rootRef.push();
        newIssueRef.setValue(newNotice);
    }

    public static final ArrayList<NoticeDetails> getNoticeDetails(@NonNull DataSnapshot snapshot) {
        ArrayList<NoticeDetails> noticeDetails = new ArrayList<NoticeDetails>();
        for (DataSnapshot user : snapshot.getChildren()) {
            String NoticeSender = user.child("noticeSender").getValue(String.class);
            String NoticeSubject = user.child("noticeSubject").getValue(String.class);
            String NoticeImage = user.child("noticeImage").getValue(String.class);
            String NoticeDescription = user.child("noticeDescription").getValue(String.class);
            noticeDetails.add(new NoticeDetails(NoticeSender,NoticeSubject,NoticeImage,NoticeDescription));
        }
        return noticeDetails;
    }

    public static final void deleteAdmin(String keyToDelete) {
        DatabaseReference issueRef = FirebaseDatabase.getInstance().getReference("/Admin/" + keyToDelete);
        issueRef.removeValue();
    }

    public static final void deleteFaculty(String keyToDelete) {
        DatabaseReference issueRef = FirebaseDatabase.getInstance().getReference("/Faculty/" + keyToDelete);
        issueRef.removeValue();
    }

    public static final void deleteStudent(String keyToDelete) {
        DatabaseReference issueRef = FirebaseDatabase.getInstance().getReference("/Student/" + keyToDelete);
        issueRef.removeValue();
    }

    public static final void updateUserPassword(String userRole, String issueKey, String newPassword) {
        DatabaseReference issueRef = FirebaseDatabase.getInstance().getReference("/"+userRole+"/" + issueKey + "/password");
        issueRef.setValue(md5(newPassword));
    }
}