package com.example.sudokuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button registerButton;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Инициализация элементов
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (dbHelper.checkUser(username, password)) {
                    Toast.makeText(RegistrationActivity.this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                    // Переход на экран главного меню
                    startActivity(new Intent(RegistrationActivity.this, MainMenuActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Обработчик клика по кнопке "Регистрация"
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, NewUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
