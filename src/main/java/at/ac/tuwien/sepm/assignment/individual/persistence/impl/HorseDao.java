package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IHorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import org.h2.command.dml.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.w3c.dom.html.HTMLObjectElement;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Repository
public class HorseDao implements IHorseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseDao.class);
    private final DBConnectionManager dbConnectionManager;

    @Autowired
    public HorseDao(DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static Horse dbResultToHorseDto(ResultSet result) throws SQLException {
        return new Horse(
            result.getInt("id"),
            result.getString("name"),
            result.getString("breed"),
            result.getDouble("min_speed"),
            result.getDouble("max_speed"),
            result.getTimestamp("created").toLocalDateTime(),
            result.getTimestamp("updated").toLocalDateTime(),
            result.getBoolean("deleted")
        );
    }

    @Override
    public Horse findOneById(Integer id) throws PersistenceException, NotFoundException {
        LOGGER.info("Get horse with id " + id);
        String sql = "SELECT * FROM Horse WHERE id=? and deleted = false";
        Horse horse = null;
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                horse = dbResultToHorseDto(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL select statement for reading horse with id " + id, e);
            throw new PersistenceException("Could not read horses with id " + id, e);
        }
        if (horse != null) {
            return horse;
        } else {
            throw new NotFoundException("Could not find horse with id " + id);
        }
    }

    @Override
    public Horse saveNewHorse(Horse horse) throws PersistenceException {

        LOGGER.debug("Input in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName()
            + " with Parameter: " + (horse == null ? "null" : horse));


        if (horse == null) {
            LOGGER.error("Error in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + ", ILLEGAL ARGUMENT!");
            throw new IllegalArgumentException("Horse is null and can not be saved");
        }
        try {
            PreparedStatement ps = dbConnectionManager.getConnection().prepareStatement(
                "INSERT INTO horse (NAME, BREED, MIN_SPEED, MAX_SPEED, CREATED, UPDATED) " +
                    "Values(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, horse.getName());
            ps.setString(2, horse.getBreed());
            ps.setDouble(3, horse.getMinSpeed() == null ? 0 : horse.getMinSpeed());
            ps.setDouble(4, horse.getMaxSpeed() == null ? 0 : horse.getMaxSpeed());
            ps.setTimestamp(5, Timestamp.valueOf(horse.getCreated()));
            ps.setTimestamp(6, Timestamp.valueOf(horse.getUpdated()));
            int status = ps.executeUpdate();

            if (status > 0) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    horse.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException exception) {
            LOGGER.debug("Datenaccesserror in Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName());
            throw new PersistenceException(exception.getMessage());
        }
        LOGGER.debug("Output aus Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName()
            + " with Parameter: " + horse);
        return horse;
    }

    @Override
    public Horse update(Horse horse) throws PersistenceException {
        if (horse == null) {
            LOGGER.error("");
            throw new IllegalArgumentException("horses ist null");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = dbConnectionManager.getConnection().prepareStatement("SELECT * FROM HORSE WHERE ID =?",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setInt(1, horse.getId().intValue());
            rs = ps.executeQuery();
            rs.first();

            rs.updateObject("NAME", horse.getName());
            rs.updateObject("BREED", horse.getBreed());
            rs.updateObject("MIN_SPEED", horse.getMinSpeed());
            rs.updateObject("MAX_SPEED", horse.getMaxSpeed() );
            rs.updateObject("UPDATED", horse.getUpdated() );
            rs.updateObject("DELETED", horse.getDeleted() );

            //fuehre die updates im aktuellen Datensatz (Zeile in der Tabelle) durch und beende das resultset
            rs.updateRow();
            rs.close();

        } catch (SQLException e) {
            LOGGER.error("Datenaccesserror in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
            throw new PersistenceException("d");
        }
        return horse;
    }

    @Override
    public Horse removeHorseById (Integer id) throws PersistenceException, NotFoundException {

        LOGGER.debug("input in removeProductById Methode with such a Parameter: " + id);
        Horse horse = findOneById(id);
        if (horse == null) {
            throw new IllegalArgumentException("No horse found!");
        }
        try {
            horse.setDeleted(true);  // boolean bir deger atamis produktun silinip silinmedigini kontrol ediyor
            // bu sayede true dÃ¶nderiyor
            update(horse);
        } catch (PersistenceException e) {
            throw new PersistenceException("Could not delete horse");
        }

        return horse;

    }

    @Override
    public List<Horse> findAll() throws PersistenceException {
        List<Horse> result = new ArrayList<Horse>();

        ResultSet rs;
        PreparedStatement pst;

        try{
            String s="SELECT * FROM HORSE WHERE 1=1 AND DELETED=false";

            pst =  dbConnectionManager.getConnection().prepareStatement(s);
            rs = pst.executeQuery();

            Horse tmp = null;
            while(rs.next()){

                tmp = new Horse();
                tmp.setId( rs.getInt("id"));
                tmp.setName(rs.getString ("name"));
                tmp.setBreed(rs.getString ("breed"));
                tmp.setMinSpeed(rs.getDouble("min_Speed"));
                tmp.setMaxSpeed(rs.getDouble("min_Speed"));
                tmp.setCreated(rs.getTimestamp("created").toLocalDateTime());
                tmp.setUpdated(rs.getTimestamp("updated").toLocalDateTime());
                tmp.setDeleted(rs.getBoolean("deleted"));
                result.add(tmp);
            }

        } catch (SQLException e) {
            throw new PersistenceException("There is no horse,that we can use, we couldnt use in next one : " + e.getMessage());
        }

        LOGGER.debug("Output from method aus der find Horse WithFilter");
        return result;
    }


    @Override
    public List<Horse> filter(String name, String breed, Double minSpeed, Double maxSpeed) throws PersistenceException{

        List<Horse> result = new ArrayList<Horse>();

        ResultSet rs = null;
        PreparedStatement pst = null;

        try{
            String s="SELECT * FROM HORSE WHERE 1=1";
            int help=0;
            if(name!=null){ s+=" AND lower(NAME) LIKE ?"; help++;}
            if(breed!=null){ s += " AND BREED LIKE ? "; help++;}
            if(minSpeed!=null){ s += " AND MIN_SPEED >= ? " ; help++;}
            if(maxSpeed!=null){ s += " AND MAX_SPEED <= ? " ; help++;}


            s+=" AND DELETED=false";
            int filterOptionsSize = help+1;

            pst = dbConnectionManager.getConnection().prepareStatement(s);
            if (help!=0){
                if(name!=null){pst.setString(filterOptionsSize-help, '%' + name.toLowerCase() + '%'); help--;}
                if(breed!=null){pst.setString(filterOptionsSize-help, breed); help--;}
                if(minSpeed!=null){pst.setDouble (filterOptionsSize-help, minSpeed); help--;}
                if(maxSpeed!=null){pst.setDouble (filterOptionsSize-help, maxSpeed); help--;}

            }

            rs = pst.executeQuery();

            Horse tmp = null;
            while (rs.next()) {
                tmp = dbResultToHorseDto(rs);
                result.add(tmp);
            }

        } catch (SQLException e) {
            LOGGER.error("Output from filter Horse method: " + e.getMessage());
            throw new PersistenceException("There is no Horse, that we can use, we couldn't use in next one :  " + e.getMessage());
        }

        LOGGER.debug("Output from the find Horse with Methode");
        return result;

    }

}