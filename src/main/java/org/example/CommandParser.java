package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommandParser {
    public int getCode(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Не введен нужный режим");
        }
        int code = Integer.parseInt(args[0]);
        if (code > 5 || code < 0) {
            throw new RuntimeException("Введен неверный режим: " + code + ". Должен быть >=1 | <=5.");
        }
        return code;
    }

    public Person getPerson(String[] args) {
        if (args.length != 4) {
            throw new RuntimeException("Введены некорректные параметры");
        }
        String name = args[1];
        LocalDate date = LocalDate.parse(args[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Person.Gender gender = Person.Gender.byStringValue(args[3]);
        return new Person(name, gender, date);
    }
}
