package com.example.a2fapplication;

import static com.example.a2fapplication.LogIn.generateCode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CodeVerificationPage extends AppCompatActivity {

    private EditText codeField;
    private Button verifyButton, resendCodeButton, goToLogInButton;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code_verification_page);

        codeField = findViewById(R.id.editTextCode);
        verifyButton = findViewById(R.id.btnVerify);
        resendCodeButton = findViewById(R.id.btnResendCode);
        goToLogInButton = findViewById(R.id.btnGoToLogIn);

        verifyButton.setOnClickListener(v->{
            String enteredCode = codeField.getText().toString();
            if(enteredCode.equals(UserSession.getGeneratedCode()) && UserSession.getTimeIsValid()){
                Toast.makeText(this, "You logged in successfully !", Toast.LENGTH_SHORT).show();
                UserSession.clearGeneratedCode();
                Intent intent = new Intent(CodeVerificationPage.this, HomePage.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Code is not valid or is expired !", Toast.LENGTH_SHORT).show();
                UserSession.clearGeneratedCode();
            }
        });

        resendCodeButton.setOnClickListener(v->{
            String code = generateCode();
            UserSession.storeGeneratedCode(code);  // Store the generated code temporarily

            // Send the code to the user's email
            EmailActivity2FA.sendVerificationCode(UserSession.getEmaili(), code, "Your 2FA code", "If you didn't  try to log in, just ignore this email ! \n Your 2FA code: ");
            UserSession.setTimeValidity(true);
            Toast.makeText(this, "Code sent !", Toast.LENGTH_SHORT).show();
        });

        goToLogInButton.setOnClickListener(v->{
            Intent intent = new Intent(CodeVerificationPage.this, LogIn.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}