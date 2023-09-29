package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonGenerator {

    private Integer count = 1;

    public List<Person> generatePeople(int quantity, int quantityWithF){
        List<Person> people = new ArrayList<>();
        for(int i = 0;i<quantity;i++){
            people.add(generatePerson());
        }
        for(int i = 0 ;i<quantityWithF;i++){
            people.add(generatePersonWithF());
        }
        return people;
    }

    public Person generatePerson(){
        return new Person(generateName(false), generateGender(false), generateDate());
    }

    public Person generatePersonWithF(){
        return new Person(generateName(true),generateGender(true),generateDate());
    }

    private String generateName(boolean withF){
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        if(!withF){
            char firstLetter = (char) (Math.abs(random.nextInt())%26);
            if((char)(firstLetter+'A') == 'F'){
                firstLetter = (char) (Math.abs(random.nextInt())%4);
            }
            builder.append((char)('A'+firstLetter));
        }else{
            builder.append("F");
        }
        builder.append("surname_").append(count).append(" Name_").append(count).append(" Patronymic_").append(count);
        count++;
        return builder.toString();
    }

    private LocalDate generateDate(){
        Random random = new Random();
        return LocalDate.of(1940+Math.abs(random.nextInt())%80, Math.abs(random.nextInt())%12+1,Math.abs(random.nextInt())%28+1);
    }

    private Person.Gender generateGender(boolean withF){
        if(withF) return Person.Gender.MALE;
        Random random = new Random();
        return (random.nextInt()%2 == 0 ? Person.Gender.MALE : Person.Gender.FEMALE);
    }
}
