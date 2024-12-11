package com.example.a2fapplication;

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

public class newPassword extends AppCompatActivity {

    private EditText newPasswordField, confirmNewPasswordField, codeField;
    private Button changePasswordButton, goToLoginButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_password);

        newPasswordField = findViewById(R.id.editTextNewPassword);
        confirmNewPasswordField = findViewById(R.id.editTextConfirmNewPassword);
        codeField = findViewById(R.id.editText2FACode);
        changePasswordButton = findViewById(R.id.btnChangePassword);
        goToLoginButton = findViewById(R.id.btnLogInC);

        DB DB = new DB(this);
        changePasswordButton.setOnClickListener(v->{
            String newPassword = newPasswordField.getText().toString();
            String confirmNewPassword = confirmNewPasswordField.getText().toString();
            String enteredCode = codeField.getText().toString();

            if(!newPassword.equals(confirmNewPassword)){
                Toast.makeText(this, "Password and confirm password are not matching !", Toast.LENGTH_SHORT).show();
            } else{
                if(!enteredCode.equals(UserSession.getGeneratedCode()) || UserSession.getTimeIsValid()){
                    Toast.makeText(this, "Code is not valid or is expired !", Toast.LENGTH_SHORT).show();
                } else if(DB.updatePassword(UserSession.getEmaili(), newPassword)){
                    Toast.makeText(this, "Password changed successfully !", Toast.LENGTH_SHORT).show();

                    UserSession.clearEmail();
                    UserSession.clearEmail();
                    Intent intent = new Intent(newPassword.this, LogIn.class);
                    startActivity(intent);
                }
            }
        });

        goToLoginButton.setOnClickListener(v->{
            Intent intent = new Intent(newPassword.this, LogIn.class);
            startActivity(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}