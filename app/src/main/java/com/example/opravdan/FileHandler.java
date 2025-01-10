package com.example.opravdan;


import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.util.Log;

public class FileHandler {

    private static final String TAG = "FileHandler";

    public static void saveApologyToFile(Context context, String prompt, String apology) {
        String filename = prompt + ".txt";
        File file = new File(context.getFilesDir(), filename);

        try {
            FileOutputStream outputStream;
            if (file.exists()) {
                outputStream = new FileOutputStream(file, true);
                outputStream.write(("\n" + apology).getBytes());
            }
            else {
                outputStream = new FileOutputStream(file);
                outputStream.write(apology.getBytes());
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Ошибка при сохранении извинения в файл: " + e.getMessage(), e);
        }
    }
    public static String readFileContents(Context context, String prompt) {
        String filename = prompt + ".txt";
        File file = new File(context.getFilesDir(), filename);
        StringBuilder fileContents = new StringBuilder();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContents.append(line).append("\n");
                }
            } catch (IOException e) {
                Log.e(TAG, "Ошибка при чтении файла: " + e.getMessage(), e);
            }
        } else {
            Log.w(TAG, "Файл не найден: " + filename);
            return "Файл не найден.";
        }

        return fileContents.toString().trim();
    }

    public static boolean deleteFile(Context context, String prompt) {
        String filename = prompt + ".txt";
        File file = new File(context.getFilesDir(), filename);

        if (file.exists()) {
            return file.delete();
        } else {
            Log.w(TAG, "Попытка удалить несуществующий файл: " + filename);
            return false; // Файл не найден
        }
    }

    public static File[] listFilesInDirectory(Context context) {
        File directory = context.getFilesDir();

        return directory.listFiles();
    }
}
