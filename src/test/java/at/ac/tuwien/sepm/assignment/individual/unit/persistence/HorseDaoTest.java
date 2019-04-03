package at.ac.tuwien.sepm.assignment.individual.unit.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IHorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.impl.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class HorseDaoTest {

    @Autowired
    IHorseDao horseDao;
    @Autowired
    DBConnectionManager dbConnectionManager;

    /**
     * It is important to close the database connection after each test in order to clean the in-memory database
     */
    @After
    public void afterEachTest() throws PersistenceException {
        dbConnectionManager.closeConnection();
    }

    @Test(expected = NotFoundException.class)
    public void givenNothing_whenFindHorseByIdWhichNotExists_thenNotFoundException()
        throws PersistenceException, NotFoundException {
        horseDao.findOneById(1);
    }

    @Test(expected = PersistenceException.class)
    public void saveNewHorse_missingName() throws PersistenceException {
        Horse horse = new Horse();
        horse.setBreed("tst_breed");
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        horse.setMaxSpeed(0.0);
        horse.setMinSpeed(0.0);
        horse = horseDao.saveNewHorse(horse);
    }

    @Test
    public void saveNewHorse() throws PersistenceException {
        Horse horse = new Horse();
        horse.setName("testn");
        horse.setBreed("d");
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        horse = horseDao.saveNewHorse(horse);
        assertTrue(horse.getId() != null);
       /* Horse horse = new Horse();
        horse.setName("test_name");
        horse.setBreed("tst_breed");
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        horse.setMaxSpeed(0.0);
        horse.setMinSpeed(0.0);
        horse = horseDao.saveNewHorse(horse);

        assertTrue(horse.getId() != null );
*/
    }

    @Test
    public void updateHorse() throws PersistenceException, NotFoundException {

        //CREATE
        Horse horse = new Horse();
        horse.setName("test_name");
        horse.setBreed("tst_breed");
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        horse.setMaxSpeed(0.0);
        horse.setMinSpeed(0.0);
        horseDao.saveNewHorse(horse);

        // UPDATE
        Horse horseUpdate = null;
        horseUpdate = horseDao.findOneById(1);
        String oldName = horseUpdate.getName();
        String newName = "NewName";
        horseUpdate.setName(newName);
        horseDao.update(horseUpdate);
        horseUpdate = horseDao.findOneById(1);

        assertTrue(newName.equals(horseUpdate.getName()));
        assertTrue(!newName.equals(oldName));


    }

    @Test
    public void findAllHorse() throws PersistenceException, NotFoundException {

        //CREATE
        Horse horse = new Horse();
        horse.setName("test_name");
        horse.setBreed("tst_breed");
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        horse.setMaxSpeed(0.0);
        horse.setMinSpeed(0.0);
        horseDao.saveNewHorse(horse);

        //CREATE
        Horse horse2 = new Horse();
        horse2.setName("test_name");
        horse2.setBreed("tst_breed");
        horse2.setCreated(LocalDateTime.now());
        horse2.setUpdated(LocalDateTime.now());
        horse2.setMaxSpeed(0.0);
        horse2.setMinSpeed(0.0);
        horseDao.saveNewHorse(horse2);

        List<Horse> list = horseDao.findAll();

        assertTrue(list.size() == 2);

    }


    @Test
    public void testSearch() throws PersistenceException{

        //CREATE
        Horse horse = new Horse();
        horse.setName("test_name");
        horse.setBreed("test_breed");
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        horse.setMaxSpeed(6.0);
        horse.setMinSpeed(3.0);
        horse = horseDao.saveNewHorse(horse);

        //search

        List<Horse> result = horseDao.filter("test_name", "test_breed", 2.0, 6.0);
        assertTrue(result.size() > 0);
    }
}

