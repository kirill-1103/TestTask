package org.example;

import java.util.List;

public class Main {

    private  static final Database database = new Database();

    public static void main(String[] args) {
        execute(args);
        database.closeConnection();
    }

    private static void execute(String[] args){
        try{
            CommandParser cp = new CommandParser();
            int code = cp.getCode(args);
            switch (code){
                case 1:
                    database.createTable();
                    break;
                case 2:
                    database.addPerson(cp.getPerson(args));
                    break;
                case 3:
                    print(database.getAllUnique());
                    break;
                case 4:
                    database.fillTable(new PersonGenerator()
                            .generatePeople(1000000,100));
                    break;
                case 5:
                    List<Person> people = calculateTimeAndGetResult();
                    print(people);
                    break;
            }
        }catch (Exception e){
            System.out.println("ERROR:"+e.getMessage());
            e.printStackTrace();
        }
    }

    private static void print(List<Person> people){
        for(Person p : people){
            System.out.println(p);
        }
    }

    private static List<Person> calculateTimeAndGetResult(){
        List<Person> personList;
        long start = System.currentTimeMillis();
        personList = database.queryWithF();
        long end = System.currentTimeMillis();
        System.out.println("TIME MS:"+(end-start));
        return personList;
    }
}
