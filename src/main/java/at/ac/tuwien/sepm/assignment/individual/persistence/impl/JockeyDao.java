package at.ac.tuwien.sepm.assignment.individual.persistence.impl;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IJockeyDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JockeyDao implements IJockeyDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JockeyDao.class);
    private final DBConnectionManager dbConnectionManager;

    @Autowired
    public JockeyDao(DBConnectionManager dbConnectionManager){
        this.dbConnectionManager = dbConnectionManager;
    }

    private static Jockey dbResultToJockeyDto(ResultSet result) throws SQLException {
        return new Jockey(
            result.getInt("id"),
            result.getString("name"),
            result.getDouble("skill"),
            result.getTimestamp("created").toLocalDateTime(),
            result.getTimestamp("updated").toLocalDateTime(),
            result.getBoolean("deleted")
        );
    }

    @Override
    public Jockey findOneById(Integer id) throws PersistenceException, NotFoundException {
        LOGGER.info("Get jockey with id " + id);
        String sql = "SELECT * FROM jockey WHERE id=? and deleted = false";
        Jockey jockey = null;
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                jockey = dbResultToJockeyDto(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL select statement for reading jockey with id " + id, e);
            throw new PersistenceException("Could not read jockey with id " + id, e);
        }
        if (jockey != null) {
            return jockey;
        } else {
            throw new NotFoundException("Could not find jockey with id " + id);
        }
    }

    @Override
    public Jockey saveNewJockey(Jockey jockey) throws PersistenceException {
        LOGGER.debug("Input in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName()
            + " with Parameter: " + (jockey == null ? "null" : jockey));


        if (jockey == null) {
            LOGGER.error("Error in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + ", ILLEGAL ARGUMENT!");
            throw new IllegalArgumentException("Horse is null and can not be saved");
        }
        try {
            PreparedStatement ps = dbConnectionManager.getConnection().prepareStatement(
                "INSERT INTO jockey (NAME, SKILL, CREATED, UPDATED) " +
                    "Values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, jockey.getName());
            ps.setDouble(2, jockey.getSkill());
            ps.setTimestamp(3, Timestamp.valueOf(jockey.getCreated()));
            ps.setTimestamp(4, Timestamp.valueOf(jockey.getUpdated()));
            int status = ps.executeUpdate();

            if (status > 0) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    jockey.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException exception) {
            LOGGER.debug("Datenaccesserror in Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName());
            throw new PersistenceException(exception.getMessage());
        }
        LOGGER.debug("Output aus Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName()
            + " with Parameter: " + jockey);
        return jockey;
    }

    @Override
    public Jockey update(Jockey jockey) throws PersistenceException {
        if (jockey == null) {
            LOGGER.error("");
            throw new IllegalArgumentException("jockey is null");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = dbConnectionManager.getConnection().prepareStatement("SELECT * FROM jockey WHERE ID =?",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setInt(1, jockey.getId().intValue());
            rs = ps.executeQuery();
            rs.first();

            rs.updateObject("NAME", jockey.getName());
            rs.updateObject("SKILL", jockey.getSkill());
            rs.updateObject("UPDATED", jockey.getUpdated() );
            rs.updateObject("DELETED", jockey.getDeleted() );

            //fuehre die updates im aktuellen Datensatz (Zeile in der Tabelle) durch und beende das resultset
            rs.updateRow();
            rs.close();


        } catch (SQLException e) {
            LOGGER.error("Datenaccesserror in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
            throw new PersistenceException("d");
        }
        return jockey;
    }

    @Override
    public Jockey removeJockeyById(Integer id) throws PersistenceException, NotFoundException {
        LOGGER.debug("input in removeProductById Methode with such a Parameter: " + id);
        Jockey jockey = findOneById(id);
        if (jockey == null) {
            throw new IllegalArgumentException("No horse found!");
        }
        try {
            jockey.setDeleted(true);  // boolean bir deger atamis produktun silinip silinmedigini kontrol ediyor
            // bu sayede true dderiyor
            update(jockey);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return jockey;
    }

    @Override
    public List<Jockey> findAll() throws PersistenceException {
        List<Jockey> result = new ArrayList<Jockey>();

        ResultSet rs;
        PreparedStatement pst;

        try{
            String s="SELECT * FROM jockey WHERE 1=1 AND DELETED=false";

            pst =  dbConnectionManager.getConnection().prepareStatement(s);
            rs = pst.executeQuery();

            Jockey tmp = null;
            while(rs.next()){

                tmp = new Jockey();
                tmp.setId( rs.getInt("id"));
                tmp.setName(rs.getString ("name"));
                tmp.setSkill(rs.getDouble("skill"));
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
    public List<Jockey> filter(String name, Double minSkill) throws PersistenceException {
        List<Jockey> result = new ArrayList<Jockey>();

        ResultSet rs = null;
        PreparedStatement pst = null;

        try{
            String s="SELECT * FROM jockey WHERE 1=1";

            int help=0;

            if(name!=null){ s+=" AND lower(NAME) LIKE ?"; help++;}
            if(minSkill!=null){ s += " AND SKILL >= ? " ; help++;}


            s+=" AND DELETED=false";
            int filterOptionsSize = help+1;

            pst = dbConnectionManager.getConnection().prepareStatement(s);

            if (help!=0){
                if(name!=null){
                    pst.setString(filterOptionsSize-help, '%' + name.toLowerCase() + '%');
                    help--;
                }
                if(minSkill!=null){
                    pst.setDouble (filterOptionsSize-help, minSkill);
                    help--;}
            }

            rs = pst.executeQuery();

            Jockey tmp = null;
            while (rs.next()) {
                tmp = dbResultToJockeyDto(rs);
                result.add(tmp);
            }

        } catch (SQLException e) {
            LOGGER.error("Output from filter Jockey method: " + e.getMessage());
            throw new PersistenceException("There is no Jockey, that we can use, we couldn't use in next one :  " + e.getMessage());
        }

        LOGGER.debug("Output from the find Jockey with Methode");
        return result;
    }
}

