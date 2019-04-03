package at.ac.tuwien.sepm.assignment.individual.unit.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IHorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.IJockeyDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class JockeyDaoTest {

    @Autowired
    IJockeyDao jockeyDao;
    @Autowired
    DBConnectionManager dbConnectionManager;

    @After
    public void afterEachTest() throws PersistenceException {
        dbConnectionManager.closeConnection();
    }

    @Test
    public void saveNewHorse() throws PersistenceException {
        Jockey jockey = new Jockey();
        jockey.setName("test_name");
        jockey.setCreated(LocalDateTime.now());
        jockey.setUpdated(LocalDateTime.now());
        jockey.setSkill(0.0);
        jockey = jockeyDao.saveNewJockey(jockey);

        assertTrue(jockey.getId() != null );

    }

    @Test
    public void updateJockey() throws PersistenceException, NotFoundException {

        //CREATE
        Jockey jockey = new Jockey();
        jockey.setName("test_name");
        jockey.setSkill(3.);
        jockey.setCreated(LocalDateTime.now());
        jockey.setUpdated(LocalDateTime.now());
        jockeyDao.saveNewJockey(jockey);

        // UPDATE
        Jockey jockeyUpdate = null;
        jockeyUpdate = jockeyDao.findOneById(1);
        String oldName = jockeyUpdate.getName();
        String newName = "NewName";
        jockeyUpdate.setName(newName);
        jockeyDao.update(jockeyUpdate);
        jockeyUpdate = jockeyDao.findOneById(1);

        assertTrue(newName.equals(jockeyUpdate.getName()));
        assertTrue(!newName.equals(oldName));

    }


    @Test
    public void testSearch() throws PersistenceException{

        //CREATE
        Jockey jockey = new Jockey();
        jockey.setName("test_name");
        jockey.setSkill(2.3);
        jockey.setCreated(LocalDateTime.now());
        jockey.setUpdated(LocalDateTime.now());
        jockey = jockeyDao.saveNewJockey(jockey);

        //search

        List<Jockey> result = jockeyDao.filter("test_name",  2.3);
        assertTrue(result.size() > 0);
    }

    @Test
    public void findAllJockey() throws PersistenceException, NotFoundException {

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

        List<Jockey> list = jockeyDao.findAll();

        assertTrue(list.size() == 2);

    }
}
