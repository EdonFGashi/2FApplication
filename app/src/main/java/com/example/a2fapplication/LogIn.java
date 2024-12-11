package com.example.a2fapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.usb.UsbRequest;
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

public class LogIn extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button logInButton, signUpButton, forgotPasswordButton;
    DB DB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        emailField = findViewById(R.id.editTextEmailL);
        passwordField = findViewById(R.id.editTextPasswordL);
        logInButton = findViewById(R.id.btnLogInL);
        signUpButton = findViewById(R.id.btnSignUpL);
        forgotPasswordButton = findViewById(R.id.btnForgotPassword);

        DB = new DB(this);

        logInButton.setOnClickListener(v->{
            validateFields();
        });

        signUpButton.setOnClickListener(v->{
            Intent intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
        });

        forgotPasswordButton.setOnClickListener(v->{
            validateUserForChanginfPassword();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void validateFields(){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        //validojme fushat
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();
        } else{

            Boolean validateUser =DB.validateUser(email, password);

            if(validateUser){


                String code = generateCode();
                UserSession.storeGeneratedCode(code);  // Store the generated code temporarily
                UserSession.setEmail(email);
                // Send the code to the user's email
                EmailActivity2FA.sendVerificationCode(email, code, "Your 2FA code", "If you didn't  try to log in, just ignore this email ! \n Your 2FA code: ");

                Timer timer = new Timer(this, 15000);
                timer.startTimer();

                Toast.makeText(this, "Log In successfully !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LogIn.this, CodeVerificationPage.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Wrong credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void validateUserForChanginfPassword(){
        String email = emailField.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email field is empty !", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter a valid email address !", Toast.LENGTH_SHORT).show();
        } else {
            Boolean validEmail = DB.checkEmail(email);

            if(validEmail){
                String code = generateCode();
                UserSession.storeGeneratedCode(code);
                UserSession.setEmail(email);
                EmailActivity2FA.sendVerificationCode(emailField.getText().toString(), code, "Your 2FA code", "If you didn't  try to change password, just ignore this email ! \n Your 2FA code: ");

                Timer time = new Timer(this, 35000);
                time.startTimer();

                Toast.makeText(this, "Code verification has sent !", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LogIn.this, newPassword.class);
                startActivity(intent);
            }
        }
    }

        public static String generateCode() {
            Random rand = new Random();
            int code = rand.nextInt(999999);
            return String.format("%06d", code);  // Ensure it's always 6 digits
        }
}