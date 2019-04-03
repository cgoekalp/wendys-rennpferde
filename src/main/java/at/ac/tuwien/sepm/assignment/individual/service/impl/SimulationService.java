package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyCombination;
import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyJoined;
import at.ac.tuwien.sepm.assignment.individual.entity.Simulation;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationParticipant;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.ISimulationDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationParticipantDto;
import at.ac.tuwien.sepm.assignment.individual.rest.validator.JockeyDtoValidator;
import at.ac.tuwien.sepm.assignment.individual.rest.validator.SimulationDtoValidator;
import at.ac.tuwien.sepm.assignment.individual.service.ISimulationService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.SimulationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SimulationService implements ISimulationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseService.class);
    private final ISimulationDao simulationDao;
    private final SimulationMapper simulationMapper;


    @Autowired
    public SimulationService(ISimulationDao simulationDao, SimulationMapper simulationMapper) {
        this.simulationDao = simulationDao;
        this.simulationMapper = simulationMapper;
    }


    @Override
    public Simulation saveNewSimulation(SimulationDto simulationDto) throws ServiceException {

        String error = SimulationDtoValidator.isValid(simulationDto);

        if(!error.isEmpty()){
            throw new ServiceException(error);
        }

        Simulation simulation = new Simulation(simulationDto.getName(), LocalDateTime.now(), false);
        List<SimulationParticipant> participantList = null;

        try {

            simulation = simulationDao.saveNewSimulation(simulation);

        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return simulation;
    }

    @Override
    public List<SimulationParticipant> saveParticipants(Integer simulationId, List<SimulationParticipantDto> simulationParticipants)
            throws ServiceException {

        String error = "";

        for(SimulationParticipantDto simulationParticipant : simulationParticipants){
            error = SimulationDtoValidator.isValid(simulationParticipant);
            break;
        }

        if(!error.isEmpty()){
            throw new ServiceException(error);
        }

        List<SimulationParticipant> simulationParticipantList = new ArrayList<>();

        for(SimulationParticipantDto simulationParticipantDto : simulationParticipants){
            simulationParticipantList.add(
                    new SimulationParticipant(simulationParticipantDto.getHorseId(), simulationParticipantDto.getJockeyId(),
                            simulationParticipantDto.getLuckFactor())
            );
        }

        try {
            return simulationDao.saveParticpants(simulationId, simulationParticipantList);

        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<HorseJockeyCombination> runSimulation(Integer simulationId) throws ServiceException {

        List<HorseJockeyJoined> horseJockeyJoinedList = null;
        List<HorseJockeyCombination> horseJockeyCombinationList = new ArrayList<>();

        try {

            horseJockeyJoinedList = simulationDao.getHorseJockeyCombinationByJoin(simulationId);

            Double horseSpeed = 0.0;
            Double jockeySkill = 0.0;
            Double averageSpeed = 0.0;

            for(HorseJockeyJoined horseJockeyJoined : horseJockeyJoinedList){

                horseSpeed = calculateHorseSpeed(horseJockeyJoined.getHorseMin(), horseJockeyJoined.getHorseMax(),
                        horseJockeyJoined.getLuckFactor());
                jockeySkill = calculateJockeySkill(horseJockeyJoined.getSkill());
                averageSpeed = calculateAverageSpeed(horseSpeed, jockeySkill, horseJockeyJoined.getLuckFactor());

                horseJockeyCombinationList.add(
                        new HorseJockeyCombination(
                                0,
                                horseJockeyJoined.getHorseName(),
                                horseJockeyJoined.getJockeyName(),
                                averageSpeed,
                                horseSpeed,
                                jockeySkill,
                                horseJockeyJoined.getLuckFactor())
                );
            }

            // sort, sorted by avg speed, descending
            Collections.sort(horseJockeyCombinationList);

            // add ranks
            for(int i =0; i<horseJockeyCombinationList.size();i++){
                horseJockeyCombinationList.get(i).setRank(i+1);
            }

            return simulationDao.saveResults(simulationId, horseJockeyCombinationList);

        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Simulation> findAll() throws ServiceException {
        try {
            return simulationDao.findAll();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<HorseJockeyCombination> getHorseJockeyCombinationById(Integer id) throws ServiceException {
        try {
            return simulationDao.getHorseJockeyCombinationById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Simulation findOnebyId(Integer id) throws ServiceException {
        try {
            return simulationDao.findOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Simulation> filter(String name) throws ServiceException {
        try {
            return simulationDao.filter(name);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private Double calculateAverageSpeed(Double horseSpeed, Double jockeySkill, Double luckFactor){

        Double result = horseSpeed * jockeySkill * luckFactor;

        return Math.round(result * 10000d) / 10000d;
    }

    private Double calculateJockeySkill(Double skill){

        Double result =  1 + (0.15 * (1/Math.PI) * Math.atan(0.2 * skill));

        return Math.round(result * 10000d) / 10000d;

    }

    private Double calculateHorseSpeed(Double minSpeed, Double maxSpeed, Double luckFactor){

        Double result = (luckFactor - 0.95) * (maxSpeed - minSpeed) / (1.05 - 0.95) + minSpeed;

        return Math.round(result * 10000d) / 10000d;
    }


}
