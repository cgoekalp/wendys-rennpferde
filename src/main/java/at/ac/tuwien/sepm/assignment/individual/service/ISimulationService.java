package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyCombination;
import at.ac.tuwien.sepm.assignment.individual.entity.Simulation;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationParticipant;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseJockeyCombinationDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationParticipantDto;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

import java.util.List;

public interface ISimulationService {

    public Simulation saveNewSimulation(SimulationDto simulationDto) throws ServiceException;

    public List<SimulationParticipant> saveParticipants(Integer simulationId, List<SimulationParticipantDto> simulationParticipants) throws ServiceException;

    public List<HorseJockeyCombination> runSimulation(Integer simulationId) throws ServiceException;

    public List<Simulation> findAll() throws ServiceException;

    public List<HorseJockeyCombination> getHorseJockeyCombinationById(Integer id) throws ServiceException;

    public Simulation findOnebyId(Integer id) throws ServiceException;

    public List<Simulation> filter(String name) throws ServiceException;
}
