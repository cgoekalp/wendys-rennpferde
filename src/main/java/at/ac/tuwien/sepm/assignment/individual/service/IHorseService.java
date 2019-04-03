package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

import java.util.List;

public interface IHorseService {

    /**
     * @param id of the horse to find.
     * @return the horse with the specified id.
     * @throws ServiceException  will be thrown if something goes wrong during data processing.
     * @throws NotFoundException will be thrown if the horse could not be found in the system.
     */
    Horse findOneById(Integer id) throws ServiceException, NotFoundException;

    Horse saveNewHorse(HorseDto horseDto) throws ServiceException;

    Horse deleteById(Integer id) throws ServiceException, NotFoundException;

    List<Horse> findAll() throws ServiceException;

    List<Horse> filterHorse(String name, String breed, Double minSpeed, Double maxSpeed) throws ServiceException;

    Horse updateHorse(HorseDto horseDto) throws ServiceException, NotFoundException;
}
