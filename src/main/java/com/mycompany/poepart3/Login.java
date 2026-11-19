/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepart3;

/**
 * Part 1 registration and login validation used by the Part 3 console app.
 */
public class Login {
    private String username = " ";
    private String password = " ";
    private String cellPhoneNumber = " ";
    private String firstName = " ";
    private String lastName = " ";
    
    public Login(){ }
    
    public Login(String username, String password, String cellPhoneNumber, String firstName, String lastName){
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username == null ? "" : username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password == null ? "" : password; }
    public String getCellPhoneNumber() { return cellPhoneNumber; }
    public void setCellPhoneNumber(String value) { cellPhoneNumber = value == null ? "" : value; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName == null ? "" : firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName == null ? "" : lastName; }

    public boolean checkUserName() { return checkUserName(username); }
    public boolean checkUserName(String value) {
        return value != null && value.contains("_") && value.length() <= 5;
    }
    
     public boolean checkPasswordComplexity() { return checkPasswordComplexity(password); }
    public boolean checkPasswordComplexity(String value) {
        if (value == null || value.length() < 8) return false;
        boolean uppercase = false, digit = false, special = false;
        for (char ch : value.toCharArray()) {
            if (Character.isUpperCase(ch)) uppercase = true;
            else if (Character.isDigit(ch)) digit = true;
            else if (!Character.isLetterOrDigit(ch)) special = true;
        }
        return uppercase && digit && special;
    }
    
    public boolean checkCellPhoneNumber() { return checkCellPhoneNumber(cellPhoneNumber); }
    public boolean checkCellPhoneNumber(String value) {
        return value != null && value.matches("\\+27\\d{9}");
    }

    public String registerUser() {
        if (!checkUserName()) return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.";
        if (!checkPasswordComplexity()) return "Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.";
        if (!checkCellPhoneNumber()) return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        return "Username and password successfully captured.";
    }
    
    public String registerrUser() { return registerUser(); }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return enteredUsername != null && enteredPassword != null
                && enteredUsername.equals(username) && enteredPassword.equals(password);
    }

    public String returnLoginStatus(boolean successful) {
        return successful
                ? "Welcome " + firstName + ", " + lastName + " it is great to see you again."
                : "Username or password incorrect, please try again.";
    }
}
