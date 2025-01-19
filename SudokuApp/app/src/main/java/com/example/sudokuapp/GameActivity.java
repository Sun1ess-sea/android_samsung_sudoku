package com.example.sudokuapp;

import android.util.Log;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private CountDownTimer timer;

    private GridLayout sudokuGrid;
    private TextView timerTextView;
    private Button backButton;
    private Button clearButton;

    private int[][] solutionBoard;
    private int[][] playerBoard;

    private Button[][] buttons;    // Ссылки на кнопки на игровом поле
    private int selectedNumber = 0; // Выбранное игроком число

    private boolean gameOver = false;
    private int secondsElapsed = 0;

    private boolean isClearMode = false; // Флаг для режима очистки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sudokuGrid = findViewById(R.id.sudokuGrid);
        timerTextView = findViewById(R.id.timerTextView);
        backButton = findViewById(R.id.returnButton);
        clearButton = findViewById(R.id.clearButton);

        String difficulty = Session.getInstance().getLevel();

        backButton.setOnClickListener(v -> showExitConfirmationDialog());

        clearButton.setOnClickListener(v -> toggleClearMode());

        startTimer();

        // Инициализация игры с уровнем сложности
        initGame(difficulty);

        // Обработчики для кнопок выбора чисел
        findViewById(R.id.button_1).setOnClickListener(v -> setSelectedNumber(1));
        findViewById(R.id.button_2).setOnClickListener(v -> setSelectedNumber(2));
        findViewById(R.id.button_3).setOnClickListener(v -> setSelectedNumber(3));
        findViewById(R.id.button_4).setOnClickListener(v -> setSelectedNumber(4));
        findViewById(R.id.button_5).setOnClickListener(v -> setSelectedNumber(5));
        findViewById(R.id.button_6).setOnClickListener(v -> setSelectedNumber(6));
        findViewById(R.id.button_7).setOnClickListener(v -> setSelectedNumber(7));
        findViewById(R.id.button_8).setOnClickListener(v -> setSelectedNumber(8));
        findViewById(R.id.button_9).setOnClickListener(v -> setSelectedNumber(9));
    }

    private void initGame(String difficulty) {
        buttons = new Button[9][9];
        playerBoard = new int[9][9];

        // Генерация решения с помощью SudokuSolver
        SudokuSolver solver = new SudokuSolver();
        solver.generateSolutionBoard(); // Генерируем полное решение
        solutionBoard = solver.getBoard(); // Получаем решение

        // Определение количества предзаполненных клеток в зависимости от сложности
        int filledCells = getFilledCellsForDifficulty(difficulty);

        // Копирование решения в игровое поле
        generatePlayerBoard(filledCells);

        // Создаем игровое поле (кнопки)
        createSudokuGrid();
    }

    private void generatePlayerBoard(int preFilledCount) {
        // Копируем решение в игровое поле
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                playerBoard[row][col] = solutionBoard[row][col];
            }
        }

        // Определяем количество клеток для удаления в зависимости от уровня сложности
        int cellsToRemove = 81 - preFilledCount;

        // Удаляем клетки на игровом поле
        removeCells(cellsToRemove);
    }

    private void removeCells(int cellsToRemove) {
        Random rand = new Random();
        int removed = 0;

        while (removed < cellsToRemove) {
            int row = rand.nextInt(9);  // Случайная строка
            int col = rand.nextInt(9);  // Случайный столбец

            // Убираем цифру, если она еще не была удалена
            if (playerBoard[row][col] != 0) {
                playerBoard[row][col] = 0;  // Очищаем клетку
                removed++;
            }
        }
    }

    private int getFilledCellsForDifficulty(String difficulty) {
        int filledCells = 0;
        switch (difficulty) {
            case "Легкий":
                filledCells = 78;
                break;
            case "Средний":
                filledCells = 60;
                break;
            case "Сложный":
                filledCells = 40;
                break;
            default:
                filledCells = 55;
                break;
        }
        return filledCells;
    }

    private void createSudokuGrid() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        int gridSize = Math.min(screenWidth, screenHeight) - 64;
        int cellSize = gridSize / 9;

        sudokuGrid.setRowCount(9);
        sudokuGrid.setColumnCount(9);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button cellButton = new Button(this);
                buttons[row][col] = cellButton;

                final int currentRow = row;
                final int currentCol = col;

                cellButton.setOnClickListener(v -> {
                    if (!gameOver && selectedNumber != 0) {
                        setCellNumber(currentRow, currentCol, selectedNumber);
                    }
                    if (isClearMode && playerBoard[currentRow][currentCol] != 0) {
                        clearCell(currentRow, currentCol);
                    }
                });

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = cellSize;
                params.height = cellSize;

                // Определяем толщину границ
                int thickBorder = 6;  // Толстая граница
                int thinBorder = 2;   // Обычная граница

                int left = (col % 3 == 0) ? thickBorder : thinBorder;
                int top = (row % 3 == 0) ? thickBorder : thinBorder;
                int right = (col == 8) ? thickBorder : thinBorder;
                int bottom = (row == 8) ? thickBorder : thinBorder;

                params.setMargins(left, top, right, bottom);
                cellButton.setLayoutParams(params);

                cellButton.setBackgroundColor(Color.WHITE);

                // Устанавливаем число, если оно есть в начальном поле
                int cellValue = playerBoard[row][col];
                if (cellValue != 0) {
                    cellButton.setText(String.valueOf(cellValue));
                    cellButton.setEnabled(false);
                    cellButton.setBackgroundColor(Color.parseColor("#E6E6FA"));
                }

                sudokuGrid.addView(cellButton);
            }
        }
    }

    private void setSelectedNumber(int number) {
        selectedNumber = number;
        // После выбора числа, выключаем режим очистки
        isClearMode = false;
        clearButton.setBackgroundColor(Color.GRAY);
    }

    private void setCellNumber(int row, int col, int number) {
        if (playerBoard[row][col] == 0) {
            playerBoard[row][col] = number;
            buttons[row][col].setText(String.valueOf(number));

            // Проверяем, правильно ли установлено число
            if (number == solutionBoard[row][col]) {
                buttons[row][col].setBackgroundColor(Color.WHITE);
            } else {
                buttons[row][col].setBackgroundColor(Color.RED);
            }

            // Проверяем, выиграл ли игрок
            if (isGameWon()) {
                showWinDialog();

                // Сохраняем финальное время перед форматированием
                int finalSecondsElapsed = secondsElapsed;

                // Переводим в формат "MM:SS"
                int minutes = finalSecondsElapsed / 60;
                int seconds = finalSecondsElapsed % 60;
                String formattedTime = String.format("%02d:%02d", minutes, seconds);

                // Получаем данные о пользователе и уровне
                String username = Session.getInstance().getUsername();
                String level = Session.getInstance().getLevel();

                // Сохраняем данные в singleton
                Session.getInstance().setTime(formattedTime);

                // Логируем результаты
                Log.d("GameActivity", "=== Победа ===");
                Log.d("GameActivity", "Игрок: " + username);
                Log.d("GameActivity", "Уровень: " + level);
                Log.d("GameActivity", "Время: " + formattedTime);

                DbRecordsHelper dbHelper = new DbRecordsHelper(this);
                boolean isRecordSaved = dbHelper.addRecord(username, level, formattedTime);

                // Проверка, был ли успешно сохранен рекорд
                if (isRecordSaved) {
                    Toast.makeText(this, "Рекорд сохранен!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Ошибка при сохранении рекорда", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean isGameWon() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (playerBoard[row][col] != solutionBoard[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWinDialog() {
        stopTimer();
        gameOver = true;
        new AlertDialog.Builder(this)
                .setTitle("Поздравляем!")
                .setMessage("Вы успешно решили судоку!")
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();
    }

    private void startTimer() {
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = secondsElapsed / 60;
                int seconds = secondsElapsed % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
                secondsElapsed++;
            }

            @Override
            public void onFinish() {
                // Таймер не закончится
            }
        };
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> finish())
                .setNegativeButton("Нет", null)
                .show();
    }

    // Метод для включения/выключения режима очистки
    private void toggleClearMode() {
        isClearMode = !isClearMode; // Переключаем состояние режима очистки
        if (isClearMode) {
            clearButton.setBackgroundColor(Color.RED); // Изменяем цвет кнопки на красный, когда режим активирован
        } else {
            clearButton.setBackgroundColor(Color.GRAY); // Возвращаем обычный цвет кнопки, когда режим отключен
        }
    }

    // Логика для очистки клетки
    private void clearCell(int row, int col) {
        if (playerBoard[row][col] != 0) {
            playerBoard[row][col] = 0;     // Сбрасываем значение в массиве
            buttons[row][col].setText(""); // Очищаем текст на кнопке
            buttons[row][col].setBackgroundColor(Color.WHITE); // Белый фон для очищенной клетки
        }
    }
}
