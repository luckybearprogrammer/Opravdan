package com.example.opravdan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button buttonStart;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = findViewById(R.id.buttonStart);
        editText = findViewById(R.id.editText);
    }

    public String returnText() {
        return editText.getText().toString();
    }

    public void clickStart(View view) {
        // Сделал удаление лишних пробелов в конце и начале промпта
        // Сделал удаление переносов строк в промпте
        String user_text_prompt = returnText().strip();
        if (user_text_prompt.contains("\n")) {
            user_text_prompt = user_text_prompt.replaceAll("\n", ". ");
        }
        if (!(returnText().isEmpty())){
            Intent intent = new Intent(MainActivity.this, Screen2.class);
            intent.putExtra("user_text_prompt", user_text_prompt);
            intent.putExtra("is_not_generating_apologies", false);
            startActivity(intent);
            Toast.makeText(this, user_text_prompt, Toast.LENGTH_SHORT).show();
        }
    }
}
