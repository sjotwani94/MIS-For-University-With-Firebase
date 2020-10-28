package com.agilesoftware.misforuniversity;

public class AdminDetails {
    private String Name;
    private String EMail;
    private String Address;
    private int Age;
    private long ContactNo;
    private String Gender;
    private String Password;

    public AdminDetails(String Name, String EMail, String Address, int Age, long ContactNo, String Gender, String Password) {
        setName(Name);
        setEMail(EMail);
        setAddress(Address);
        setAge(Age);
        setContactNo(ContactNo);
        setGender(Gender);
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
