package com.example.a2fapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private EditText firstNameField, lastNameField, phoneField, emailField, passwordField;
    DB DB;
    private Button signUpButton;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        firstNameField = findViewById(R.id.editTextFirstName);
        lastNameField = findViewById(R.id.editTextLastName);
        phoneField = findViewById(R.id.editTextPhone);
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        signUpButton = findViewById(R.id.btnSignUpS);
        logInButton = findViewById(R.id.btnLogInS);

        DB = new DB(this);

        //set on click listener for sign up button
        signUpButton.setOnClickListener(v->{
            if(validateFiels()){
                String firstName =firstNameField.getText().toString().trim();
                String lastName = lastNameField.getText().toString().trim();
                String phone = phoneField.getText().toString().trim();
                String  email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if(DB.checkEmail(email)){
                    Toast.makeText(this, "This user already exists", Toast.LENGTH_SHORT).show();
                } else if(DB.insertUser(email, firstName, lastName, phone, password)){
                    Toast.makeText(this, "User inserted successfully", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(this, "Failed to insert new user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logInButton.setOnClickListener(v->{
            Intent intent = new Intent(SignUp.this, LogIn.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

//    public void validateFieldsForSignUp(String firstName, String lastName, String phone, String email, String password){
//
//        if(validateFiels()){
//
//            Boolean checkUser =DB.checkEmail(email);
//
//            if(!checkUser){
//
//                Toast.makeText(this, "User with this email, already exists !", Toast.LENGTH_SHORT).show();
//
//
//            } else {
//
//                String code = generateCode();
//                UserSession.storeGeneratedCode(code);  // Store the generated code temporarily
//                UserSession.setEmail(email);
//                // Send the code to the user's email
//                EmailActivity2FA.sendVerificationCode(email, code, "Your 2FA code", "If you didn't  try to sign in, just ignore this email ! \n Your 2FA code: ");
//
//                Timer timer = new Timer(this, 15000);
//                timer.startTimer();
//
//                Toast.makeText(this, "Log In successfully !", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(LogIn.this, CodeVerificationPage.class);
//                startActivity(intent);
//            }
//        }
//    }

    private boolean validateFiels(){
        String firstName =firstNameField.getText().toString().trim();
        String lastName = lastNameField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String  email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        //validate name (only alphsbets, not empty)
        if(TextUtils.isEmpty(firstName) || !firstName.matches("[a-zA-Z]+")){
            firstNameField.setError("First name must contain only letters and not be empty");
            return false;
        }

        //validate lastname (same)
        if(TextUtils.isEmpty(lastName) || !lastName.matches("[a-zA-Z]+")){
            firstNameField.setError("Last name must contain only letters and not be empty");
            return false;
        }

        //validate email (not empty and email format)
        if(TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailField.setError("Enter a valid email address");
            return false;
        }

        //validate phone (not empty, valid format)
        if(TextUtils.isEmpty(phone) || !phone.matches("\\d{10,15}")){
            phoneField.setError("Enter a valid phone number");
            return false;
        }

        //validate password
        if(TextUtils.isEmpty(password) || !Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{6,20}$").matcher(password).matches()  ){
            passwordField.setError("Password must contain 1 lowercase, 1 uppercase, 1 digit, 1 special character, and be 6-20 characters long.");
            return false;
        }

        return true;
    }

//    public static String generateCode() {
//        Random rand = new Random();
//        int code = rand.nextInt(999999);
//        return String.format("%06d", code);  // Ensure it's always 6 digits
//    }
}