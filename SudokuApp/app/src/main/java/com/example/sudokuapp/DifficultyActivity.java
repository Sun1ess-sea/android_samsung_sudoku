package com.example.sudokuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficults);  // Убедитесь, что у вас есть правильный layout-файл

        // Кнопка для легкого уровня
        Button easyButton = findViewById(R.id.easyButton);
        easyButton.setOnClickListener(v -> startGame("easy"));  // Легкий уровень

        // Кнопка для среднего уровня
        Button mediumButton = findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(v -> startGame("medium"));  // Средний уровень

        // Кнопка для сложного уровня
        Button hardButton = findViewById(R.id.hardButton);
        hardButton.setOnClickListener(v -> startGame("hard"));  // Сложный уровень

        // Кнопка "Назад" для возврата в главное меню
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());  // Закрывает текущую активность и возвращает в предыдущее
    }

    // Метод для запуска игры с передачей уровня сложности
    private void startGame(String level) {
        Intent intent = new Intent(DifficultyActivity.this, GameActivity.class);
        intent.putExtra("difficulty", level);  // Передаем уровень сложности
        startActivity(intent);  // Переходим в игровую активность
    }
}
