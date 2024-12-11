package com.example.a2fapplication;

public class UserSession {
    private static String generatedCode;  // Store the generated code
    private static Boolean timeIsValid;
    private static String emaili ;

    public static void storeGeneratedCode(String code) {
        generatedCode = code;
    }

    public static String getGeneratedCode() {
        return generatedCode;
    }

    public static void clearGeneratedCode() {
        generatedCode = null;
    }

    public static void setTimeValidity(Boolean validity){
        timeIsValid = validity;
    }

    public static Boolean getTimeIsValid(){
        return timeIsValid;
    }

    public static void setEmail(String email){
        emaili = email;
    }
    public static String getEmaili(){
        return emaili;
    }
    public static void clearEmail(){
        emaili = null;
    }
}
