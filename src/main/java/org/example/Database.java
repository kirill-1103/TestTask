package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class Database {
    private static final String TABLE_NAME = "person";

    private final static String URL = "jdbc:postgresql://localhost/testdb";
    private final static String USER = "kirill";
    private final static String PASSWORD = "123";

    private Connection connection;

    public Database(){
        Properties props = new Properties();
        props.setProperty("user",USER);
        props.setProperty("password", PASSWORD);

        try{
            this.connection = DriverManager.getConnection(URL,props);
            System.out.println("Подключение к базе данных установлено");
        }catch (SQLException e){
            throw new RuntimeException("Не удалось подключиться к базе данных. "+e.getMessage(),e);
        }
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(" +
                "id serial PRIMARY KEY, " +
                "name VARCHAR(200) NOT NULL, " +
                "gender VARCHAR(6) NOT NULL, " +
                "birthday DATE NOT NULL);";
        try(Statement st = connection.createStatement()){
            st.execute(sql);
            System.out.println("Таблица "+TABLE_NAME+" создана.");
        }catch (SQLException e){
            throw new RuntimeException("Не удалось создать таблицу. "+e.getMessage(),e);
        }
    }

    public void addPerson(Person person){
        try(Statement st = connection.createStatement()){
            st.execute(getStringForInsertPerson(person));
            System.out.println("Пользователь Person{"+person+"}"+ "добавлен");
        }catch (SQLException e){
            throw new RuntimeException("Не удалось добавить пользователя. "+e.getMessage(),e);
        }
    }

    public List<Person> getAllUnique(){
        String sql = "SELECT name,MAX(gender),birthday FROM "+TABLE_NAME +
                " GROUP BY name,birthday" +
                " ORDER BY name;";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            ResultSet rs = st.executeQuery();
            return getPeopleFromResultSet(rs);
        }catch (SQLException e){
            throw new RuntimeException("Не удалось выполнить запрос. "+e.getMessage(),e);
        }
    }

    public void closeConnection(){
        if(!Objects.isNull(this.connection)){
            try {
                this.connection.close();
                System.out.println("Соедениение с базой данных закрыто.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void fillTable(List<Person> people){
        try(Statement st = connection.createStatement()){
            for(Person p: people){
                st.addBatch(getStringForInsertPerson(p));
            }
            st.executeBatch();
            System.out.println("База данных заполнена случайными данными");
        }catch (SQLException e){
            throw new RuntimeException("Не удалось добавить данные в таблицу. "+e.getMessage(),e);
        }
    }

    public List<Person> queryWithF(){
        String sql ="SELECT name,gender,birthday FROM "+TABLE_NAME +" WHERE " +
                "name LIKE 'F%'"+ " AND "+" gender = '" + Person.Gender.MALE.stringValue+"';";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            ResultSet rs = st.executeQuery();
            return getPeopleFromResultSet(rs);
        }catch (SQLException e){
            throw new RuntimeException("Не удалось выполнить запрос."+e.getMessage(),e);
        }
    }

    public void removeData(){
        String sql = "DELETE FROM "+TABLE_NAME+";";
        try(Statement st = connection.createStatement()){
            st.execute(sql);
        }catch (SQLException e){
            throw new RuntimeException("Не удалось сбросить данные. "+e.getMessage(),e);
        }
    }

    private String getStringForInsertPerson(Person person){
        return "INSERT INTO "+TABLE_NAME+" (name,gender,birthday) " + "VALUES(" +
                "'"+person.name + "', '" +
                person.gender.stringValue + "', '"+
                person.birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"');";
    }

    private List<Person> getPeopleFromResultSet(ResultSet rs) throws SQLException {
        List<Person> people = new ArrayList<>();
        while(rs.next()){
            String name = rs.getString(1);
            String gender = rs.getString(2);
            LocalDate birthday = rs.getDate(3).toLocalDate();
            people.add(new Person(name,Person.Gender.byStringValue(gender),birthday));
        }
        return people;
    }
}
