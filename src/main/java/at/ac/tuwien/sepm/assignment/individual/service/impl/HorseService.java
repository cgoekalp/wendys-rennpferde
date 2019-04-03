package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IHorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.rest.validator.HorseDtoValidator;
import at.ac.tuwien.sepm.assignment.individual.service.IHorseService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HorseService implements IHorseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseService.class);
    private final IHorseDao horseDao;

    @Autowired
    public HorseService(IHorseDao horseDao) {
        this.horseDao = horseDao;
    }

    @Override
    public Horse findOneById(Integer id) throws ServiceException, NotFoundException {
        LOGGER.info("Get horse with id " + id);
        try {
            return horseDao.findOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Horse saveNewHorse(HorseDto horseDto) throws ServiceException {

        String error = HorseDtoValidator.isValid(horseDto);

        if(!error.isEmpty()){
            throw new ServiceException(error);
        }

        Horse horse = new Horse();
        horse.setName(horseDto.getName());
        horse.setBreed(horseDto.getBreed());
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        horse.setMaxSpeed(horseDto.getMaxSpeed());
        horse.setMinSpeed(horseDto.getMinSpeed());

        try {
            return horseDao.saveNewHorse(horse);
        } catch (PersistenceException e) {
            throw new ServiceException("Could not save horse", e);
        }
    }

    @Override
    public Horse deleteById(Integer id) throws ServiceException, NotFoundException {
        LOGGER.info("Delete horse with id " + id);
        try {
            return horseDao.removeHorseById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Horse> findAll() throws ServiceException {

        List<Horse> listHorse = new ArrayList<>();

        try {
            listHorse.addAll(horseDao.findAll());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return listHorse;
    }

    @Override
    public List<Horse> filterHorse(String name, String breed, Double minSpeed, Double maxSpeed) throws ServiceException {
        List<Horse> listHorse = new ArrayList<>();

        try {
            listHorse.addAll(horseDao.filter(name, breed, minSpeed, maxSpeed));
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return listHorse;
    }

    @Override
    public Horse updateHorse(HorseDto horseDto) throws ServiceException, NotFoundException {
        Horse horse = null;

        String error = HorseDtoValidator.isValid(horseDto);

        if(!error.isEmpty()){
            throw new ServiceException(error);
        }

        try {

            horse = horseDao.findOneById(horseDto.getId());

            horse.setMinSpeed(horseDto.getMinSpeed());
            horse.setMaxSpeed(horseDto.getMaxSpeed());
            horse.setName(horseDto.getName());
            horse.setUpdated(LocalDateTime.now());

            horse = horseDao.update(horse);

        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return horse;
    }
}
