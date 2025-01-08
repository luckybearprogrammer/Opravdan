package com.example.opravdan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetArray {

    /**
     * Извлекает все тексты, которые заключены между <opstart> и <opend>,
     * и возвращает их в виде массива строк.
     *
     * @param input исходная строка с несколькими блоками <opstart>...<opend>
     * @return массив строк, извлечённых из тегов
     */
    public static String[] extractTexts(String input) {
        // Список, в который будем складывать найденные тексты
        List<String> extractedTexts = new ArrayList<>();
        input = input.replace("[opstart]", "<opstart>");

        // Регулярное выражение для поиска всего, что находится между <opstart> и <opend>
        // DOTALL нужен, чтобы символы перевода строки тоже учитывались
        Pattern pattern = Pattern.compile("<opstart>(.*?)<opend>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        // Проходим по всем совпадениям и сохраняем содержимое в список
        while (matcher.find()) {
            extractedTexts.add(matcher.group(1).trim());
        }

        // Преобразуем список в массив и возвращаем
        return extractedTexts.toArray(new String[0]);
    }

    public static void main(String[] args) {
        String text = "Здесь несколько вариантов забавных оправданий:\n\n"
                + "[opstart]\"Я был занят поиском идеальной позиции для сушки волос, и это заняло больше времени, чем я рассчитывал.\"<opend>\n\n"
                + "[opstart]\"Мой холодильник решил организовать свой внутренний мир, и я не мог войти в него, пока он не закончил.\"<opend>\n\n"
                + "[opstart]\"Я пытался научить своего кошку играть на пианино, и это заняло больше времени, чем я рассчитывал.\"<opend>\n\n"
                + "[opstart]\"Мой компьютер решил сделать паузу в работе и посмотреть фильм, и я не мог его остановить.\"<opend>\n\n"
                + "[opstart]\"Я был занят поездкой на Луну, но мой самолёт задержался из-за сильного ветра на Марсе.\"<opend>";

        String[] results = extractTexts(text);

        // Вывод результата
        for (String result : results) {
            System.out.println(result);
        }
    }
}
