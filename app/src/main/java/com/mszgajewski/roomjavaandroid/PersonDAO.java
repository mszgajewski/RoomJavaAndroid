package com.mszgajewski.roomjavaandroid;

import androidx.room.Dao;
import androidx.room.Insert;

import java.util.List;

@Dao
public interface PersonDAO {

    @Insert
    public void addPerson(Person person);

    public void updatePerson(Person person);

    public void deletePerson(Person person);

    public List<Person> getAllPerson();

    public Person getPerson(int person_id);

}
