package com.foreach.controller;

public class Validate {
    public final static String errorTextNotValid = "Введены некорректные данные";
    public final static String errorTextServerFall = "Сервер не смог вернуть данные";

    public static final String errorTextDriverFall = "Драйвер не подгрузился. Ошибка сервера.";
    public static final String errorTextContinue = "Запись уже была создана. Добавление не требуется";
    public static final String errorTextBDnotAccess = "База данных не отвечает.";

    public static final String ERROR_TEXT_NO_DATA_FOUND = "Данные не найдены.";

    public static final String SUCCESS_DATA_ADDED = "Данные добавлены.";

    public static boolean checkStr(String str, int desiredLength) {
        if (checkLength(str, desiredLength)
          && checkSQLwords(str)
          && checkSpesialSymbols(str)
        ) {
            return true;
        }
        return false;
    }

    private static boolean checkLength(String str, int desiredLength) {
        if (str == null  || str.length() > desiredLength) {
            return false;
        }
        return true;
    }

    private static boolean checkSQLwords(String str) {

        String[] words = str.toLowerCase().split(" ");
        for (String val : words) {
            if (val.equals("select")) {
                return false;
            }
        }
        return true;
    }
    private static boolean checkSpesialSymbols(String str) {

        char[] charArray = str.toCharArray();
        for (char val : charArray) {
            if (val == ';') {
                return false;
            }
        }
        return true;
    }
}
