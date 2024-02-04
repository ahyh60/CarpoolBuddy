package com.example.carpoolbuddy;

/**
 * This class is the template to instantiate the user object
 * @author Amanda Ho
 * @version 0.1
 */

public class User {

    //properties
    private String email;
    private String role;

    private String name;





    public User(){}; //empty constructor for firebase

    //constructor
    public User(String n, String e, String r){
        name = n;
        email = e;
        role = r;

    }


    //Accessors and Mutators
    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }
    public String getRole(){
        return role;
    }



    public void setEmail(String e){
        email = e;
    }
    public void setRole(String r){
        r = role;
    }

    public void setName(String n){
        n = name;
    }


}
