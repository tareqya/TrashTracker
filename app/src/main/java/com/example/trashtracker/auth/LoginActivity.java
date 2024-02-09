package com.example.trashtracker.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trashtracker.R;
import com.example.trashtracker.interfaces.AuthCallBack;
import com.example.trashtracker.main.HomeActivity;
import com.example.trashtracker.utils.Database;
import com.example.trashtracker.utils.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout login_TIL_email;
    private TextInputLayout login_TIL_password;
    private MaterialButton login_BTN_login;
    private Button login_BTN_createAccount;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initVars();
    }

    private void initVars() {
        database = new Database();

        if(database.getCurrentUser() != null){
            openHomeScreen();
        }

        database.setAuthCallBack(new AuthCallBack() {
            @Override
            public void onCreateAccountComplete(boolean status, String msg) {

            }

            @Override
            public void updateUserInfoComplete(boolean status, String msg) {

            }

            @Override
            public void onLoginComplete(boolean status, String msg) {
                if(status){
                    openHomeScreen();
                }else{
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void fetchUserInfoComplete(User user) {

            }

        });

        login_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_TIL_email.getEditText().getText().toString();
                String password = login_TIL_password.getEditText().getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email and password required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User()
                        .setEmail(email)
                        .setPassword(password);
                database.login(user);
            }
        });

        login_BTN_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void openHomeScreen() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    private void findViews() {
        login_TIL_email = findViewById(R.id.login_TIL_email);
        login_TIL_password = findViewById(R.id.login_TIL_password);
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_BTN_createAccount = findViewById(R.id.login_BTN_createAccount);
    }
}