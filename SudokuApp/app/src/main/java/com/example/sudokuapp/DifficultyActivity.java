package com.example.sudokuapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficults);  // Убедитесь, что у вас есть правильный layout-файл

        // Кнопка для легкого уровня
        Button easyButton = findViewById(R.id.easyButton);
        easyButton.setOnClickListener(v -> startGame("Легкий"));  // Легкий уровень

        // Кнопка для среднего уровня
        Button mediumButton = findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(v -> startGame("Средний"));  // Средний уровень

        // Кнопка для сложного уровня
        Button hardButton = findViewById(R.id.hardButton);
        hardButton.setOnClickListener(v -> startGame("Сложный"));  // Сложный уровень

        // Кнопка "Назад" для возврата в главное меню
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());  // Закрывает текущую активность и возвращает в предыдущее
    }

    // Метод для запуска игры с передачей уровня сложности
    private void startGame(String level) {
        Session.getInstance().setLevel(level);
        Session.getInstance().logCurrentState();
        Intent intent = new Intent(DifficultyActivity.this, GameActivity.class);
        startActivity(intent);  // Переходим в игровую активность
    }
}
