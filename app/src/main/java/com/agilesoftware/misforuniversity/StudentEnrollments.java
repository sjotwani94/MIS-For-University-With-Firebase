package com.agilesoftware.misforuniversity;

public class StudentEnrollments {
    private String RollNo;
    private String CourseCode;
    private String CourseName;
    private int ClassTest;
    private int MidSem;
    private int Assignments;
    private int LabPracticals;
    private int FinalExam;
    private int TotalPresence;
    private int TotalLectures;

    public StudentEnrollments(String rollNo, String courseCode, String courseName, int classTest, int midSem, int assignments, int labPracticals, int finalExam, int totalPresence, int totalLectures) {
        setRollNo(rollNo);
        setCourseCode(courseCode);
        setCourseName(courseName);
        setClassTest(classTest);
        setMidSem(midSem);
        setAssignments(assignments);
        setLabPracticals(labPracticals);
        setFinalExam(finalExam);
        setTotalPresence(totalPresence);
        setTotalLectures(totalLectures);
    }

    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String rollNo) {
        RollNo = rollNo;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public int getClassTest() {
        return ClassTest;
    }

    public void setClassTest(int classTest) {
        ClassTest = classTest;
    }

    public int getMidSem() {
        return MidSem;
    }

    public void setMidSem(int midSem) {
        MidSem = midSem;
    }

    public int getAssignments() {
        return Assignments;
    }

    public void setAssignments(int assignments) {
        Assignments = assignments;
    }

    public int getLabPracticals() {
        return LabPracticals;
    }

    public void setLabPracticals(int labPracticals) {
        LabPracticals = labPracticals;
    }

    public int getFinalExam() {
        return FinalExam;
    }

    public void setFinalExam(int finalExam) {
        FinalExam = finalExam;
    }

    public int getTotalPresence() {
        return TotalPresence;
    }

    public void setTotalPresence(int totalPresence) {
        TotalPresence = totalPresence;
    }

    public int getTotalLectures() {
        return TotalLectures;
    }

    public void setTotalLectures(int totalLectures) {
        TotalLectures = totalLectures;
    }
}
