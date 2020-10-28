package com.agilesoftware.misforuniversity;

public class CourseDetails {
    private String CourseCode;
    private String CourseName;
    private String CourseDescription;
    private int Semester;
    private String CoursePrerequisites;

    public CourseDetails(String CourseCode, String CourseName, String CourseDescription, int Semester, String CoursePrerequisites) {
        setCourseCode(CourseCode);
        setCourseName(CourseName);
        setCourseDescription(CourseDescription);
        setSemester(Semester);
        setCoursePrerequisites(CoursePrerequisites);
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

    public String getCourseDescription() {
        return CourseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        CourseDescription = courseDescription;
    }

    public int getSemester() {
        return Semester;
    }

    public void setSemester(int semester) {
        Semester = semester;
    }

    public String getCoursePrerequisites() {
        return CoursePrerequisites;
    }

    public void setCoursePrerequisites(String coursePrerequisites) {
        CoursePrerequisites = coursePrerequisites;
    }
}
