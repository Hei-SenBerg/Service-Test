package com.example.service_test.project.Models;


import android.graphics.Bitmap;

public class Employee {

    private String id ;
    private String employee_name ;
    private String salary ;
    private String age ;
    private byte[] image ;


    public Employee ( String id, String employee_name , String salary , String age, byte[] image ){
        this.id = id ;
        this.employee_name = employee_name ;
        this.salary = salary ;
        this.age = age ;
        this.image = image ;
    }

    public Employee ( String id, String employee_name , String salary , String age ){
        this.id = id ;
        this.employee_name = employee_name ;
        this.salary = salary ;
        this.age = age ;

    }

    public Employee (){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
