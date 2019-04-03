package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

import java.util.List;

public interface IJockeyService {

    Jockey findOneById(Integer id) throws ServiceException, NotFoundException;

    Jockey saveNewJockey(JockeyDto jockeyDto) throws ServiceException;

    Jockey updateJockey(JockeyDto jockeyDto)throws ServiceException, NotFoundException;

    Jockey deleteById(Integer id) throws ServiceException, NotFoundException;

    List<Jockey> findAll() throws ServiceException;

    List<Jockey> filterJockey(String name, Double skill) throws ServiceException;

}
