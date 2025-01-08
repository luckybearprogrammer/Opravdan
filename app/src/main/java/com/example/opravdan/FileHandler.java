package com.example.opravdan;


import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHandler {

    public static void saveApologyToFile(Context context, String prompt, String apology) {
        String filename = prompt + ".txt";
        File file = new File(context.getFilesDir(), filename);

        try {
            if (file.exists()) {
                FileOutputStream outputStream = new FileOutputStream(file, true);
                outputStream.write(("\n" + apology).getBytes());
                outputStream.close();
            }
            else {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(apology.getBytes());
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        } else {
            return "Файл не найден.";
        }

        return "Название файла: " + filename + "\nСодержимое:\n" + fileContents.toString().trim();
    }

    public static boolean deleteFile(Context context, String prompt) {
        String filename = prompt + ".txt";
        File file = new File(context.getFilesDir(), filename);

        if (file.exists()) {
            return file.delete();
        } else {
            return false; // Файл не найден
        }
    }

    public static File[] listFilesInDirectory(Context context) {
        File directory = context.getFilesDir();

        // Получаем массив файлов в этой директории
        File[] files = directory.listFiles();

        return files;
    }
}
