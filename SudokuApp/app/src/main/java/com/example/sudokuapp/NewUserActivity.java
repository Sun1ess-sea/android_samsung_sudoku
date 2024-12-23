package com.example.sudokuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NewUserActivity extends AppCompatActivity {

    private EditText newUsernameEditText, newPasswordEditText;
    private TextView errorTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Button backButton = findViewById(R.id.backButton);
        newUsernameEditText = findViewById(R.id.newUsernameEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        errorTextView = findViewById(R.id.errorTextView);
        Button confirmButton = findViewById(R.id.confirmButton);

        dbHelper = new DatabaseHelper(this);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = newUsernameEditText.getText().toString().trim();
                String password = newPasswordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    errorTextView.setText("Все поля должны быть заполнены");
                    errorTextView.setVisibility(View.VISIBLE);
                } else if (!isValidInput(username) || !isValidInput(password)) {
                    errorTextView.setText("Логин и пароль должны содержать только английские буквы и цифры");
                    errorTextView.setVisibility(View.VISIBLE);
                } else {
                    if (dbHelper.addUser(username, password)) {
                        Toast.makeText(NewUserActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                        // Переход на экран главного меню
                        startActivity(new Intent(NewUserActivity.this, MainMenuActivity.class));
                        finish(); // Закрыть активность
                    } else {
                        errorTextView.setText("Пользователь уже существует");
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // Обработчик для кнопки "Вернуться назад"
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewUserActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private boolean isValidInput(String input) {
        return input.matches("^[a-zA-Z0-9]+$");
    }
}
