package com.example.opravdan;


import static com.example.opravdan.MainActivity.returnText;

import static com.example.opravdan.Private.API.API;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class getText extends AsyncTask<Void, Void, String> {
    StringBuilder response = new StringBuilder();
    String answer;

    @Override
    protected String doInBackground(Void... params) {
        try {

            String requestUrl = "https://api.together.xyz/v1/chat/completions";
            String situatoin = returnText();
            System.out.println(situatoin);
            String userMessage =
                    "Мне нужно придумать 2 коротких оправдания для ситуации (далее написана ситуация на английском)"+situatoin+", но что бы оно было максимально забавным, нелепым и смешным в начале оправдания унжно написать <opstart>, в конце оправдания нужно написать <opend>" +
                            "Например для ситуации я опозздал на урок хорошим вариантом забавных оправданий были бы " +
                            "<opstart>Извините, но мой будильник решил брать выходной, и я только что понял, что он всё-таки работает по пятницам<opend>" +
                            "<opstart>На улице был огромный голубь, и я не мог пройти, пока он не дал мне разрешение<opend>" ;

            String jsonData = "{ \"model\": \"meta-llama/Meta-Llama-3.1-70B-Instruct-Turbo\", "
                    + "\"messages\": [{\"role\": \"user\", \"content\": \"" + userMessage + "\"}] }";


            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", API);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
                writer.write(jsonData);
                writer.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String content = "jib";
        String jsonResponse = response.toString();
        if (jsonResponse.isEmpty()) {
            System.out.println("Ответ от сервера пуст.");
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray choices = jsonObject.getJSONArray("choices");

            if (choices.length() > 0) {
                JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                content = message.getString("content");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        answer = content;
        System.out.println(answer);
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(answer);
    }
}