package org.example;

import java.time.LocalDate;
import java.time.Period;

public class Person {
    public enum Gender {
        MALE("Male"),
        FEMALE("Female");
        final String stringValue;

        Gender(String stringValue) {
            this.stringValue = stringValue;
        }
        static Gender byStringValue(String value){
            if(value.equals(MALE.stringValue)){
                return MALE;
            }else if(value.equals(FEMALE.stringValue)){
                return FEMALE;
            }else{
                throw new IllegalArgumentException("Неверно задан пол: "+value);
            }
        }
    }
    String name;
    Gender gender;

    LocalDate birthday;

    public Person(String name, Gender gender, LocalDate birthday) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthday, currentDate);
        return period.getYears();
    }

    public void save(Database database) {
        database.addPerson(this);
    }

    @Override
    public String toString() {
       return name+" "+birthday+" "+gender.stringValue+" "+getAge();
    }
}
