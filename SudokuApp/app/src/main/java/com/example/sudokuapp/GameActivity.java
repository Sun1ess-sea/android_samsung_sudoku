package com.example.sudokuapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private CountDownTimer timer;

    private GridLayout sudokuGrid;
    private TextView timerTextView;
    private Button backButton;
    private Button clearButton;

    private int[][] solutionBoard; // Полностью заполненная доска
    private int[][] playerBoard;   // Игровая доска игрока

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

        // Получаем уровень сложности, переданный из DifficultyActivity
        String difficulty = getIntent().getStringExtra("difficulty");

        // Обработчик кнопки "Назад"
        backButton.setOnClickListener(v -> showExitConfirmationDialog());

        // Обработчик кнопки "Очистить"
        clearButton.setOnClickListener(v -> toggleClearMode());

        // Таймер
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
            case "easy":
                filledCells = 78;
                break;
            case "medium":
                filledCells = 45;
                break;
            case "hard":
                filledCells = 35;
                break;
            default:
                filledCells = 50;
                break;
        }
        return filledCells;
    }

    private void createSudokuGrid() {
        // Получаем размеры экрана
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        // Учитываем отступы и делаем поле квадратным
        int gridSize = Math.min(screenWidth, screenHeight) - 64;
        int cellSize = gridSize / 9; // Размер одной клетки

        sudokuGrid.setRowCount(9);
        sudokuGrid.setColumnCount(9);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button cellButton = new Button(this);
                buttons[row][col] = cellButton;

                final int currentRow = row;
                final int currentCol = col;

                // Обработка кликов для установки числа
                cellButton.setOnClickListener(v -> {
                    if (!gameOver && selectedNumber != 0) {
                        setCellNumber(currentRow, currentCol, selectedNumber);
                    }

                    // Если активен режим очистки, очищаем клетку
                    if (isClearMode && playerBoard[currentRow][currentCol] != 0) {
                        clearCell(currentRow, currentCol);
                    }
                });

                // Настройка параметров GridLayout
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);

                params.width = cellSize;  // Квадратная ширина
                params.height = cellSize; // Квадратная высота
                params.setMargins(2, 2, 2, 2); // Границы между клетками

                cellButton.setLayoutParams(params);
                cellButton.setBackgroundResource(R.drawable.cell_border);

                // Настройка внешнего вида клеток
                int cellValue = playerBoard[row][col];
                if (cellValue != 0) {
                    // Установка текста для предзаполненной клетки
                    cellButton.setText(String.valueOf(cellValue));
                    cellButton.setEnabled(false); // Блокируем неизменяемую клетку

                    // Для предзаполненных клеток ставим лавандовый фон
                    cellButton.setBackgroundColor(Color.parseColor("#E6E6FA")); // Лавандовый фон
                } else {
                    // Для пустых клеток оставляем белый фон с серой границей
                    cellButton.setBackgroundColor(Color.WHITE);
                }

                sudokuGrid.addView(cellButton);
            }
        }
    }

    private void setSelectedNumber(int number) {
        selectedNumber = number;
        // После выбора числа, выключаем режим очистки
        isClearMode = false;
        clearButton.setBackgroundColor(Color.GRAY); // Возвращаем цвет кнопки очистки
    }

    private void setCellNumber(int row, int col, int number) {
        if (playerBoard[row][col] == 0) {
            playerBoard[row][col] = number;
            buttons[row][col].setText(String.valueOf(number));
            if (isGameWon()) {
                showWinDialog();
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
