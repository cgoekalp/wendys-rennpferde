package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;

import java.util.List;

public interface IJockeyDao {

    Jockey findOneById(Integer id) throws PersistenceException, NotFoundException;

    Jockey saveNewJockey(Jockey jockey) throws PersistenceException;

    Jockey update(Jockey jockey) throws PersistenceException;

    Jockey removeJockeyById(Integer id) throws PersistenceException, NotFoundException;

    List<Jockey> findAll() throws PersistenceException;

    List<Jockey> filter(String name, Double minSkill) throws PersistenceException;

}

