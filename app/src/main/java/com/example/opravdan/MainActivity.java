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
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        buttonStart = findViewById(R.id.buttonStart);
        editText = findViewById(R.id.editText);
    }

    public String returnText() {
        return editText.getText().toString();
    }

    public void clickStart(View view) {
        String user_text_prompt = returnText();
        if (!(editText.getText().toString().isEmpty())){
            Intent intent = new Intent(MainActivity.this, Screen2.class);
            intent.putExtra("user_text_prompt", user_text_prompt);
            startActivity(intent);
            Toast.makeText(this, user_text_prompt, Toast.LENGTH_SHORT).show();
        }
    }
}
