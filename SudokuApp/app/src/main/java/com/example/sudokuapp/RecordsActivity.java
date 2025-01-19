package com.example.sudokuapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RecordsActivity extends AppCompatActivity {

    private DbRecordsHelper dbHelper;
    private ListView recordsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recordsListView = findViewById(R.id.records_list);
        dbHelper = new DbRecordsHelper(this);

        // Добавляем заголовок
        addListHeader();


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecordsActivity.this, MainMenuActivity.class);
            startActivity(intent);  // Переход в MainMenuActivity
            finish();  // Закрытие текущего activity
        });

        // Отображаем рекорды
        displayRecords();
    }

    private void addListHeader() {
        LayoutInflater inflater = getLayoutInflater();
        View headerView = inflater.inflate(R.layout.record, recordsListView, false);

        // Находим элементы заголовка
        TextView usernameHeader = headerView.findViewById(R.id.usernameTextView);
        TextView levelHeader = headerView.findViewById(R.id.levelTextView);
        TextView timeHeader = headerView.findViewById(R.id.timeTextView);

        // Устанавливаем текст заголовка
        usernameHeader.setText("Логин");
        levelHeader.setText("Уровень сложности");
        timeHeader.setText("Время");

        // Делаем заголовок жирным
        usernameHeader.setTextAppearance(android.R.style.TextAppearance_Medium);
        levelHeader.setTextAppearance(android.R.style.TextAppearance_Medium);
        timeHeader.setTextAppearance(android.R.style.TextAppearance_Medium);

        // Добавляем заголовок перед списком
        recordsListView.addHeaderView(headerView);
    }

    private void displayRecords() {
        Cursor cursor = dbHelper.getTopRecords();

        String[] fromColumns = {"username", "level", "time"};
        int[] toViews = {R.id.usernameTextView, R.id.levelTextView, R.id.timeTextView};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.record, cursor, fromColumns, toViews, 0
        );

        recordsListView.setAdapter(adapter);
    }
}
