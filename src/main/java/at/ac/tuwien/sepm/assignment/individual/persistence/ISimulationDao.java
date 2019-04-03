package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyCombination;
import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyJoined;
import at.ac.tuwien.sepm.assignment.individual.entity.Simulation;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationParticipant;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationParticipantDto;

import java.util.List;

public interface ISimulationDao {

    public Simulation saveNewSimulation(Simulation simulation) throws PersistenceException;

    public List<SimulationParticipant> saveParticpants(Integer simulationId, List<SimulationParticipant> simulationParticipantList) throws PersistenceException;

    // helper method to cretae combinations from join
    public List<HorseJockeyJoined> getHorseJockeyCombinationByJoin(Integer simulationId) throws PersistenceException, NotFoundException;

    List<HorseJockeyCombination> saveResults(Integer simulationId, List<HorseJockeyCombination> horseJockeyCombinationList) throws PersistenceException;

    List<Simulation> findAll() throws PersistenceException;

    List<HorseJockeyCombination> getHorseJockeyCombinationById(Integer id) throws PersistenceException;

    Simulation findOneById(Integer id) throws PersistenceException, NotFoundException;

    List<Simulation> filter(String name) throws PersistenceException;

}
