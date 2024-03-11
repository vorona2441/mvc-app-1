package org.example.dao;

import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Person show(int id) {
        // Второй параметр query - список аргументов для prepared statement
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id}, new PersonMapper())
                .stream().findAny().orElse(null); //query возвращает нам список, поэтому нужно его преобразовать к 1 объекту или вернуть null
    }       // Возвращает объект optional

    public void save(Person person){
        jdbcTemplate.update("INSERT INTO person (name, age, email) VALUES (?,?,?)", new Object[]{person.getName(), person.getAge(), person.getEmail()});
    }

    public void update(int id, Person person){
        String sql = "UPDATE public.person SET " +
                "name = ?::character varying," +
                "age = ?::integer," +
                "email = ?::character varying " +
                "WHERE id = ?;";
        jdbcTemplate.update(sql,person.getName(),person.getAge(),person.getEmail(),id);
    }

    public void delete(int id){
        String sql = "DELETE FROM public.person WHERE id IN (?)";
        jdbcTemplate.update(sql, id);
    }

    public List<Person> index(){
        // BeanPropertyRowMapper - переводит строки из нашей таблицы к нашему классу
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
   }
}
