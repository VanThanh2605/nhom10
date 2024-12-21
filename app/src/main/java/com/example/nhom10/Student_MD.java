package com.example.nhom10;

public class Student_MD {
    private String id;
    private String name;
    private String studentClass;

    public Student_MD() {}

    public Student_MD(String id, String name, String studentClass) {
        this.id = id;
        this.name = name;
        this.studentClass = studentClass;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
}
