package com.example.opravdan;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
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

    boolean disable_ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        disable_ai = getIntent().getBooleanExtra("is_not_generating_apologies", true);

        // НУЖНО ТОЛЬКО ПРИ РАЗРАБОТКЕ ДЛЯ ТОГО ЧТОБЫ НЕ ИСПОЛЬЗОВАТЬ ПОПУСТУ ИИ
        disable_ai = true;

        user_text_prompt = getIntent().getStringExtra("user_text_prompt");

        if (!disable_ai) {
            SharedPreferences sharedPreferences = getSharedPreferences("apologies", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
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
            editor.putString("text1", results[0]);
            editor.putString("text2", results[1]);
            editor.apply();
            text1 = findViewById(R.id.text1);
            text2 = findViewById(R.id.text2);
            text1.setText(results[0]);
            text2.setText(results[1]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("apologies", MODE_PRIVATE);
        String apology1 = sharedPreferences.getString("text1", getString(R.string.apology1));
        String apology2 = sharedPreferences.getString("text2", getString(R.string.apology2));
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text1.setText(apology1);
        text2.setText(apology2);
    }


    public void open_history_OnClick(View v) {
        Intent intent = new Intent(Screen2.this, HistoryActivity.class);
        intent.putExtra("user_text_prompt", user_text_prompt);
        startActivity(intent);
    }

    public void back(View v) {
        Intent intent = new Intent(Screen2.this, MainActivity.class);
        startActivity(intent);
    }

    public void OnClickHeart(View v) {
        int heart_id = v.getId();
        String heart_name = getResources().getResourceEntryName(heart_id);

        Map<String, String> apology_dict = new HashMap<>();
        apology_dict.put("heart1", ((TextView) findViewById(R.id.text1)).getText().toString());
        apology_dict.put("heart2", ((TextView) findViewById(R.id.text2)).getText().toString());

        Toast.makeText(this, getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();

        String prompt = user_text_prompt;  // Название файла
        String apology = apology_dict.get(heart_name);  // Текст для записи
        File_work(prompt, apology);
    }

    public void File_work(String prompt, String apology) {
        FileHandler.saveApologyToFile(getApplicationContext(), prompt, apology);
        String res = FileHandler.readFileContents(getApplicationContext(), prompt);
        if (res.equals("Файл не найден.")) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
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

    public void clickText1(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        // Создаем объект ClipData с текстом из TextView
        ClipData clip = ClipData.newPlainText("label", text1.getText());
        // Устанавливаем созданный ClipData в буфер обмена
        clipboard.setPrimaryClip(clip);
        // Показываем уведомление о том, что текст скопирован
        Toast.makeText(getApplicationContext(), "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show();
    }

    public void clickText2(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        // Создаем объект ClipData с текстом из TextView
        ClipData clip = ClipData.newPlainText("label", text2.getText());
        // Устанавливаем созданный ClipData в буфер обмена
        clipboard.setPrimaryClip(clip);
        // Показываем уведомление о том, что текст скопирован
        Toast.makeText(getApplicationContext(), "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show();
    }

}