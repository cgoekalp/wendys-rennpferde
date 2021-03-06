package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IHorseDao {

    /**
     * @param id of the horse to find.
     * @return the horse with the specified id.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException    will be thrown if the horse could not be found in the database.
     */
    Horse findOneById(Integer id) throws PersistenceException, NotFoundException;

    Horse saveNewHorse(Horse horse) throws PersistenceException;

    Horse update(Horse horse) throws PersistenceException, NotFoundException;

    Horse removeHorseById(Integer id) throws PersistenceException, NotFoundException;

    List<Horse> findAll() throws PersistenceException;

    List<Horse> filter(String name, String breed, Double minSpeed, Double maxSpeed) throws PersistenceException;

}


