package com.agilesoftware.misforuniversity;

public class StudentDetails {
    private String Name;
    private String EMail;
    private String Address;
    private int Age;
    private long ContactNo;
    private String Gender;
    private String Department;
    private String RollNo;
    private int Batch;
    private String Password;

    public StudentDetails(String Name, String EMail, String Address, int Age, long ContactNo, String Gender, String Department, String RollNo, int Batch, String Password) {
        setName(Name);
        setEMail(EMail);
        setAddress(Address);
        setAge(Age);
        setContactNo(ContactNo);
        setGender(Gender);
        setDepartment(Department);
        setRollNo(RollNo);
        setBatch(Batch);
        setPassword(Password);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public long getContactNo() {
        return ContactNo;
    }

    public void setContactNo(long contactNo) {
        ContactNo = contactNo;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String rollNo) {
        RollNo = rollNo;
    }

    public int getBatch() {
        return Batch;
    }

    public void setBatch(int batch) {
        Batch = batch;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
