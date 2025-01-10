package com.example.opravdan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import android.util.Log;

// TODO: Почему-то есть проблема с null. То файл имеет имя null, то оправдания

public class HistoryActivity extends Activity {
    private static final String TAG = "HistoryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Сделать получение имен из имен файлов промтов
        File[] files = FileHandler.listFilesInDirectory(getApplicationContext());

        if (files.length == 0) {
            Toast.makeText(this, "Нет файлов ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
        }
        ArrayList<String> prompts = new ArrayList<>();
        for (File file : files) {
            if (file.getName().equals("profileInstalled"))
                continue;
            String prompt = file.getName().replace(".txt", "");
            Log.d(TAG, file.getName());
            prompts.add(prompt);
        }

        ListView lv_prompts = findViewById(R.id.prompt_list_view);
        ArrayAdapter<String> prompts_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, prompts);

        lv_prompts.setAdapter(prompts_adapter);

        ListView lv_apologies = findViewById(R.id.apologies_list_view);

        lv_prompts.setOnItemClickListener((parent, view, position, id) -> {
            // Получаем выбранный элемент
            String selectedItem = (String) parent.getItemAtPosition(position);

            String[] apologies = FileHandler.readFileContents(getApplicationContext(), selectedItem).split("/n");

//            if (apologies.length == 1) {
//                Toast.makeText(this, "Нет оправданий ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
//                apologies[0] = "Нет оправданий ¯\\_(ツ)_/¯";
//            }

            ArrayAdapter<String> apologies_adapter = new ArrayAdapter<>(HistoryActivity.this,
                    android.R.layout.simple_list_item_1, apologies);

            lv_apologies.setAdapter(apologies_adapter);

//                // Выполняем действие при нажатии
//                Toast.makeText(HistoryActivity.this, "Вы выбрали: " + selectedItem, Toast.LENGTH_SHORT).show();
        });
    }
    public void Prompt_OnClick(View vwk) {
        Intent intent = new Intent(HistoryActivity.this, Screen2.class);
        startActivity(intent);
    }
}

