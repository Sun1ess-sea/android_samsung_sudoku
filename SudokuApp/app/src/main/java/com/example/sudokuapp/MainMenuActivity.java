package com.example.sudokuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Кнопка "Играть"
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран выбора уровня сложности
                Intent intent = new Intent(MainMenuActivity.this, DifficultyActivity.class); // Замените на свой экран
                startActivity(intent);
            }
        });

        // Кнопка "Рекорды"
        Button recordsButton = findViewById(R.id.recordsButton);
        recordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран с рекордами
                Intent intent = new Intent(MainMenuActivity.this, RecordsActivity.class); // Замените на свой экран
                startActivity(intent);
            }
        });

        // Кнопка "Выйти"
        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Закрытие приложения
                finish(); // Завершаем текущую активность
                System.exit(0); // Закрытие всего приложения
            }
        });

        // Кнопка "Сменить профиль"
        Button changeProfileButton = findViewById(R.id.changeProfileButton);
        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран регистрации
                Intent intent = new Intent(MainMenuActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish(); // Закрываем MainActivity, чтобы пользователь не мог вернуться назад
            }
        });
    }
}
