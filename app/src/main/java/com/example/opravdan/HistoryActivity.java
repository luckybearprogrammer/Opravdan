package com.example.opravdan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import android.util.Log;

// TODO: Почему-то есть проблема с null. То файл имеет имя null, то оправдания
// TODO: Прятать опровдания после удаления промпта

public class HistoryActivity extends Activity {
    private static final String TAG = "HistoryActivity";

    ArrayAdapter<String> prompts_adapter;
    ArrayList<String> prompts;

    ListView lv_apologies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Сделать получение имен из имен файлов промтов
        File[] files = FileHandler.listFilesInDirectory(getApplicationContext());

//        if (files.length == 0) {
//            Toast.makeText(this, getString(R.string.no_files), Toast.LENGTH_SHORT).show();
//        }
        prompts = new ArrayList<>();
        for (File file : files) {
            if (file.getName().equals("profileInstalled"))
                continue;
            String prompt = file.getName().replace(".txt", "");
            Log.d(TAG, file.getName());
            prompts.add(prompt);
        }

        ListView lv_prompts = findViewById(R.id.prompt_list_view);

        // Регистрируем контекстное меню
        registerForContextMenu(lv_prompts);

        prompts_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, prompts);

        lv_prompts.setAdapter(prompts_adapter);

        lv_apologies = findViewById(R.id.apologies_list_view);

        lv_prompts.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);

            String[] apologies = FileHandler.readFileContents(getApplicationContext(), selectedItem).split("/n");

            ArrayAdapter<String> apologies_adapter = new ArrayAdapter<>(HistoryActivity.this,
                    android.R.layout.simple_list_item_1, apologies);

            lv_apologies.setAdapter(apologies_adapter);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, getString(R.string.delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Удалить")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String fileName = prompts.get(info.position);
            // Удаляем файл через FileHandler
            boolean isDeleted = FileHandler.deleteFile(getApplicationContext(), fileName);

            if (isDeleted) {
                // Убираем элемент из списка и обновляем адаптер
                prompts.remove(info.position);
                prompts_adapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.file_deleted) + " " + fileName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.file_delete_failed) + " " + fileName, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onContextItemSelected(item);
    }

    public void Prompt_OnClick(View vwk) {
        Intent intent = new Intent(HistoryActivity.this, Screen2.class);
        startActivity(intent);
    }
}

