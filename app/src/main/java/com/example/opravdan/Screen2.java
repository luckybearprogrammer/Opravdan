package com.example.opravdan;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

public class Screen2 extends AppCompatActivity {
    int index = 1;
    TextView text1, text2;
    String[] results;

    getText gt = new getText();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen2);
        gt.execute();
        try {
            // Задержка на 3 секунды
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // Обработка исключения
            System.out.println("Поток был прерван: " + e.getMessage());
            // Восстановление статуса прерывания
            Thread.currentThread().interrupt();
        }
        results = extractTexts(gt.answer);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text1.setText(results[0]);
        text2.setText(results[1]);


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }


    public void open_history_OnClick(View v) {
        Intent intent = new Intent(Screen2.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void back(View v) {
        Intent intent = new Intent(Screen2.this, MainActivity.class);
        startActivity(intent);
    }

    public void File_work() {
        String prompt = "Помогите1";  // Название файла
        String apology = "This is my apology.";  // Текст для записи

        FileHandler.saveApologyToFile(getApplicationContext(), prompt, apology);
        String res = FileHandler.readFileContents(getApplicationContext(), prompt);
        if (res.equals("Файл не найден.")) {
            Toast.makeText(this, "Почему то нет файла ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
        }
//        if (!FileHandler.deleteFile(getApplicationContext(), prompt)) {
//            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
//        }
    }

    public static String[] extractTexts(String input) {
        List<String> extractedTexts = new ArrayList<>();
        System.out.println(input);
        input = input.replace("[opstart]", "<opstart>");

        Pattern pattern = Pattern.compile("<opstart>(.*?)<opend>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            extractedTexts.add(matcher.group(1).trim());
        }

        return extractedTexts.toArray(new String[0]);
    }
}