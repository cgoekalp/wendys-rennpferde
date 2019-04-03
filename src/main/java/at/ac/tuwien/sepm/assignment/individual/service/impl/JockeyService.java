package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IJockeyDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;
import at.ac.tuwien.sepm.assignment.individual.rest.validator.JockeyDtoValidator;
import at.ac.tuwien.sepm.assignment.individual.rest.validator.SimulationDtoValidator;
import at.ac.tuwien.sepm.assignment.individual.service.IJockeyService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JockeyService implements IJockeyService {

    private final IJockeyDao jockeyDao;

    @Autowired
    public JockeyService(IJockeyDao jockeyDao){
        this.jockeyDao = jockeyDao;
    }

    @Override
    public Jockey findOneById(Integer id) throws ServiceException, NotFoundException {

        try {
           return jockeyDao.findOneById(id);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Jockey saveNewJockey(JockeyDto parameter) throws ServiceException {

        String error = JockeyDtoValidator.isValid(parameter);

        if(!error.isEmpty()){
            throw new ServiceException(error);
        }

        Jockey jockey = new Jockey();
        jockey.setName(parameter.getName());
        jockey.setCreated(LocalDateTime.now());
        jockey.setUpdated(LocalDateTime.now());
        jockey.setSkill(parameter.getSkill());
        jockey.setDeleted(false);

        try {
            return jockeyDao.saveNewJockey(jockey);
        } catch (PersistenceException e) {
            throw new ServiceException("Could not save jockey", e);
        }
    }

    @Override
    public Jockey updateJockey(JockeyDto jockeyDto) throws ServiceException, NotFoundException {
        Jockey jockey =null;

        String error = JockeyDtoValidator.isValid(jockeyDto);

        if(!error.isEmpty()){
            throw new ServiceException(error);
        }

        try {
            jockey = jockeyDao.findOneById(jockeyDto.getId());
            jockey.setSkill(jockeyDto.getSkill());
            jockey.setName(jockeyDto.getName());
            jockey.setUpdated(LocalDateTime.now());

            jockeyDao.update(jockey);
        } catch (PersistenceException e) {
            throw new ServiceException("Could not update jockey", e);
        }

        return jockey;
    }

    @Override
    public Jockey deleteById(Integer id) throws ServiceException, NotFoundException {
        try {
            return jockeyDao.removeJockeyById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Jockey> findAll() throws ServiceException {

        try {
            return jockeyDao.findAll();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Jockey> filterJockey(String name, Double skill) throws ServiceException {

        try {
            return jockeyDao.filter(name, skill);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return null;
    }
}
