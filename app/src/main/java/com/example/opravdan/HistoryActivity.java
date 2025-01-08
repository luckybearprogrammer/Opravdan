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

public class HistoryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Сделать получение имен из имен файлов промтов
        File[] files = FileHandler.listFilesInDirectory(getApplicationContext());
        if (files == null) {
            Toast.makeText(this, "Нет файлов ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
        }
        ArrayList<String> prompts = new ArrayList<String>();
        for (File file : files) {
            if (file.getName().equals("profileInstalled"))
                continue;
            prompts.add(file.getName().replace(".txt", ""));
        }

        ListView lv_history = (ListView) findViewById(R.id.prompt_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, prompts);
        lv_history.setAdapter(adapter);
    }

    public void Prompt_OnClick(View v) {
        Intent intent = new Intent(HistoryActivity.this, Screen2.class);
        startActivity(intent);
    }
}