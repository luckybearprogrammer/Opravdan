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

import java.util.HashMap;
import java.util.Map;

public class Screen2 extends AppCompatActivity {
    TextView text1, text2;
    String[] results;

    String user_text_prompt;

    // НУЖНО ТОЛЬКО ПРИ РАЗРАБОТКЕ
    boolean disable_ai = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen2);

        user_text_prompt = getIntent().getStringExtra("user_text_prompt");

        if (!disable_ai) {
            getText gt = new getText(user_text_prompt);
            gt.execute();
            try {
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
        }
    }


    public void open_history_OnClick(View v) {
        Intent intent = new Intent(Screen2.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void back(View v) {
        Intent intent = new Intent(Screen2.this, MainActivity.class);
        startActivity(intent);
    }

    public void OnClickHeart(View v) {
        String heart_id = "" + v.getId();
        Map<String, String> apology_dict = new HashMap<>();
        apology_dict.put("2131230932", ((TextView) findViewById(R.id.text1)).getText().toString());
        apology_dict.put("2131230933", ((TextView) findViewById(R.id.text2)).getText().toString());

        Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();

        String prompt = user_text_prompt;  // Название файла
        String apology = apology_dict.get(heart_id);  // Текст для записи
        File_work(prompt, apology);
    }

    public void File_work(String prompt, String apology) {
        FileHandler.saveApologyToFile(getApplicationContext(), prompt, apology);
        String res = FileHandler.readFileContents(getApplicationContext(), prompt);
        if (res.equals("Файл не найден.")) {
            Toast.makeText(this, "Почему то нет файла ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
        }
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