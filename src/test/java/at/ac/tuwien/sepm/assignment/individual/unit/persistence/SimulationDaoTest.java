package at.ac.tuwien.sepm.assignment.individual.unit.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.*;
import at.ac.tuwien.sepm.assignment.individual.persistence.IHorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.IJockeyDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.ISimulationDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.impl.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class SimulationDaoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationDaoTest.class);
    @Autowired
    DBConnectionManager dbConnectionManager;

    @Autowired
    ISimulationDao simulationDao;

    @Autowired
    IJockeyDao jockeyDao;

    @Autowired
    IHorseDao horseDao;

    /**
     * It is important to close the database connection after each test in order to clean the in-memory database
     */
    @After
    public void afterEachTest() throws PersistenceException {
        dbConnectionManager.closeConnection();
    }


    @Test
    public void saveNewSimulation() throws PersistenceException{

        //CREATE
        Horse horse = new Horse();
        horse.setName("test_name1");
        horse.setBreed("tst_breed");
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        horse.setMaxSpeed(0.0);
        horse.setMinSpeed(0.0);
        horseDao.saveNewHorse(horse);

        //CREATE
        Horse horse2 = new Horse();
        horse2.setName("test_name2");
        horse2.setBreed("tst_breed");
        horse2.setCreated(LocalDateTime.now());
        horse2.setUpdated(LocalDateTime.now());
        horse2.setMaxSpeed(0.0);
        horse2.setMinSpeed(0.0);
        horseDao.saveNewHorse(horse2);

        //CREATE
        Jockey jockey = new Jockey();
        jockey.setName("test_name");
        jockey.setCreated(LocalDateTime.now());
        jockey.setUpdated(LocalDateTime.now());
        jockey.setSkill(1.0);
        jockeyDao.saveNewJockey(jockey);

        //CREATE
        Jockey jockey2 = new Jockey();
        jockey2.setName("test_name");
        jockey2.setCreated(LocalDateTime.now());
        jockey2.setUpdated(LocalDateTime.now());
        jockey2.setSkill(2.0);
        jockeyDao.saveNewJockey(jockey2);

        Simulation simulation = new Simulation();

        simulation.setName("simulation1");
        simulation.setFinished(true);
        simulation.setCreated(LocalDateTime.now());

        SimulationParticipant simulationParticipant1 = new SimulationParticipant(1, 1, 0.95);
        SimulationParticipant simulationParticipant2 = new SimulationParticipant(2, 2, 1.0);
        List<SimulationParticipant> participantList = new ArrayList<>();
        participantList.add(simulationParticipant1);
        participantList.add(simulationParticipant2);

        simulation = simulationDao.saveNewSimulation(simulation);

        assertTrue(simulation.getId() != null );

        simulationDao.saveParticpants(simulation.getId(), participantList);

        List<HorseJockeyCombination> horseJockeyCombinationList = new ArrayList<>();

        HorseJockeyCombination horseJockeyCombination1 = new HorseJockeyCombination(1, horse.getName() , jockey.getName(), 1.0 , 1.0, 1.0, 0.95);
        HorseJockeyCombination horseJockeyCombination2 = new HorseJockeyCombination(1, horse2.getName() , jockey2.getName(), 1.0 , 1.0, 1.0, 1.0);

        horseJockeyCombinationList.add(horseJockeyCombination1);
        horseJockeyCombinationList.add(horseJockeyCombination2);

        simulationDao.saveResults(simulation.getId(), horseJockeyCombinationList);

        List<HorseJockeyCombination> combinationList = simulationDao.getHorseJockeyCombinationById(simulation.getId());

        assertTrue(combinationList.size() == 2 );

    }

}
