package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.*;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.ISimulationDao;
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
public class SimulationDao implements ISimulationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationDao.class);
    private final DBConnectionManager dbConnectionManager;

    @Autowired
    public SimulationDao(DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static Simulation dbResultToSimulationDto(ResultSet result) throws SQLException {
        return new Simulation(
                result.getInt("id"),
                result.getString("name"),
                result.getTimestamp("created").toLocalDateTime(),
                result.getBoolean("finished")
        );
    }

    private static HorseJockeyCombination dbResultToHorseJockeyCombinationDto(ResultSet result) throws SQLException {
        return new HorseJockeyCombination(
                result.getInt("id"),
                result.getInt("rank"),
                result.getString("horse_name"),
                result.getString("jockey_name"),
                result.getDouble("avg_speed"),
                result.getDouble("horse_speed"),
                result.getDouble("skill"),
                result.getDouble("luck_factor")
        );
    }

    @Override
    public Simulation saveNewSimulation(Simulation simulation) throws PersistenceException {
        LOGGER.debug("Input in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " with Parameter: " + (simulation == null ? "null" : simulation));

        if (simulation == null) {
            LOGGER.error("Error in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + ", ILLEGAL ARGUMENT!");
            throw new IllegalArgumentException("Simulation is null and can not be saved");
        }

        try {
            PreparedStatement ps = dbConnectionManager.getConnection().prepareStatement(
                    "INSERT INTO simulation (NAME, CREATED, FINISHED) " +
                            "Values(?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, simulation.getName());
            ps.setTimestamp(2, Timestamp.valueOf(simulation.getCreated()));
            ps.setBoolean(3, simulation.getFinished());
            int status = ps.executeUpdate();

            if (status > 0) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    simulation.setId(resultSet.getInt(1));
                }
            }

        } catch (SQLException exception) {
            LOGGER.debug("Datenaccesserror in Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName());
            throw new PersistenceException(exception.getMessage());
        }
        LOGGER.debug("Output aus Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " with Parameter: " + simulation);
        return simulation;
    }

    @Override
    public List<SimulationParticipant> saveParticpants(Integer simulationId, List<SimulationParticipant> simulationParticipantList)
            throws PersistenceException {

        LOGGER.debug("Input in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " with Parameter: " + (simulationParticipantList == null ? "null" : simulationParticipantList));

        if (simulationParticipantList == null) {
            LOGGER.error("Error in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + ", ILLEGAL ARGUMENT!");
            throw new IllegalArgumentException("Simulation is null and can not be saved");
        }
        try {
            PreparedStatement ps = dbConnectionManager.getConnection().prepareStatement(
                    "INSERT INTO simulation_participant (SIMULATION_ID, JOCKEY_ID, HORSE_ID, LUCK_FACTOR) " +
                            "Values(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            for(SimulationParticipant simulationParticipant : simulationParticipantList){

                ps.setInt(1, simulationId);
                ps.setInt(2, simulationParticipant.getJockeyId());
                ps.setInt(3, simulationParticipant.getHorseId());
                ps.setDouble(4, simulationParticipant.getLuckFactor());

                ps.addBatch();

            }

            ps.executeBatch();

        } catch (SQLException exception) {
            LOGGER.debug("Datenaccesserror in Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName());
            throw new PersistenceException(exception.getMessage());
        }
        LOGGER.debug("Output aus Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " with Parameter: " + simulationParticipantList);
        return simulationParticipantList;
    }

    @Override
    public List<HorseJockeyJoined> getHorseJockeyCombinationByJoin(Integer simulationId) throws PersistenceException, NotFoundException {

        List<HorseJockeyJoined> result = new ArrayList<>();

        String sql = "select j.name as jockey_name, h.name as horse_name, j.skill, h.min_speed, h.max_speed, s.luck_factor " +
                "from simulation_participant s " +
                "join jockey j on s.jockey_id=j.id " +
                "join horse h on s.horse_id=h.id " +
                "where s.simulation_id = ?";

        ResultSet rs;
        PreparedStatement pst;

        try{

            pst =  dbConnectionManager.getConnection().prepareStatement(sql);
            pst.setInt(1, simulationId);
            rs = pst.executeQuery();

            HorseJockeyJoined tmp = null;
            while(rs.next()){

                tmp = new HorseJockeyJoined();
                tmp.setHorseName(rs.getString ("horse_name"));
                tmp.setJockeyName(rs.getString ("jockey_name"));
                tmp.setSkill(rs.getDouble("skill"));
                tmp.setHorseMin(rs.getDouble("min_speed"));
                tmp.setHorseMax(rs.getDouble("max_speed"));
                tmp.setLuckFactor(rs.getDouble("luck_factor"));
                result.add(tmp);
            }

        } catch (SQLException e) {
            throw new PersistenceException("There is no entry,that we can use, we couldnt use in next one : " + e.getMessage());
        }

        LOGGER.debug("Output from method aus der find Entry WithFilter");
        return result;
    }

    @Override
    public List<HorseJockeyCombination> saveResults(Integer simulationId, List<HorseJockeyCombination> horseJockeyCombinationList) throws PersistenceException {
        LOGGER.debug("Input in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " with Parameter: " + (horseJockeyCombinationList == null ? "null" : horseJockeyCombinationList));

        if (horseJockeyCombinationList == null) {
            LOGGER.error("Error in Method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + ", ILLEGAL ARGUMENT!");
            throw new IllegalArgumentException("Simulation is null and can not be saved");
        }

        try {

            PreparedStatement ps = dbConnectionManager.getConnection().prepareStatement(
                    "INSERT INTO simulation_combination_result (HORSE_NAME, JOCKEY_NAME, LUCK_FACTOR, AVG_SPEED, HORSE_SPEED, SKILL, RANK, SIMULATION_ID) " +
                            "Values(?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            for(HorseJockeyCombination horseJockeyCombination : horseJockeyCombinationList){

                ps.setString(1, horseJockeyCombination.getHorseName());
                ps.setString(2, horseJockeyCombination.getJockeyName());
                ps.setDouble(3, horseJockeyCombination.getLuckFactor());
                ps.setDouble(4, horseJockeyCombination.getAvgSpeed());
                ps.setDouble(5, horseJockeyCombination.getHorseSpeed());
                ps.setDouble(6, horseJockeyCombination.getSkill());
                ps.setInt(7, horseJockeyCombination.getRank());
                ps.setInt(8, simulationId);

                ps.addBatch();

            }

            ps.executeBatch();

        } catch (SQLException exception) {
            LOGGER.debug("Datenaccesserror in Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName());
            throw new PersistenceException(exception.getMessage());
        }

        LOGGER.debug("Output aus Methode: " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " with Parameter: " + horseJockeyCombinationList);
        return horseJockeyCombinationList;
    }

    @Override
    public List<Simulation> findAll() throws PersistenceException {
        List<Simulation> result = new ArrayList<Simulation>();

        ResultSet rs;
        PreparedStatement pst;

        try{
            String s="SELECT * FROM simulation WHERE 1=1 ";

            pst =  dbConnectionManager.getConnection().prepareStatement(s);
            rs = pst.executeQuery();

            Simulation tmp = null;

            while(rs.next()){

                tmp = new Simulation();
                tmp.setId( rs.getInt("id"));
                tmp.setName(rs.getString ("name"));
                tmp.setCreated(rs.getTimestamp("created").toLocalDateTime());
                tmp.setFinished(rs.getBoolean("finished"));
                result.add(tmp);
            }

        } catch (SQLException e) {
            throw new PersistenceException("There is no simulation,that we can use, we couldnt use in next one : " + e.getMessage());
        }

        LOGGER.debug("Output from method aus der find Horse WithFilter");
        return result;
    }

    @Override
    public List<HorseJockeyCombination> getHorseJockeyCombinationById(Integer id) throws PersistenceException {
        List<HorseJockeyCombination> result = new ArrayList<HorseJockeyCombination>();

        ResultSet rs;
        PreparedStatement pst;

        try{
            String s="SELECT * FROM simulation_combination_result WHERE simulation_id=? ";

            pst =  dbConnectionManager.getConnection().prepareStatement(s);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            HorseJockeyCombination tmp = null;

            while(rs.next()){

                tmp = SimulationDao.dbResultToHorseJockeyCombinationDto(rs);
                result.add(tmp);
            }

        } catch (SQLException e) {
            throw new PersistenceException("There is no simulation,that we can use, we couldnt use in next one : " + e.getMessage());
        }

        LOGGER.debug("Output from method aus der find Horse WithFilter");
        return result;
    }

    @Override
    public Simulation findOneById(Integer id) throws PersistenceException, NotFoundException {

        ResultSet rs;
        PreparedStatement pst;
        Simulation tmp = null;

        try{
            String s="SELECT * FROM simulation WHERE id=?";

            pst =  dbConnectionManager.getConnection().prepareStatement(s);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            while(rs.next()){
                tmp = new Simulation();
                tmp.setId( rs.getInt("id"));
                tmp.setName(rs.getString ("name"));
                tmp.setCreated(rs.getTimestamp("created").toLocalDateTime());
                tmp.setFinished(rs.getBoolean("finished"));
            }

        } catch (SQLException e) {
            throw new PersistenceException("There is no simulation,that we can use, we couldnt use in next one : " + e.getMessage());
        }

        LOGGER.debug("Output from method aus der find Siomulation byid");
        return tmp;
    }

    @Override
    public List<Simulation> filter(String name) throws PersistenceException {
        List<Simulation> result = new ArrayList<Simulation>();

        ResultSet rs;
        PreparedStatement pst;

        try{
            String s="SELECT * FROM simulation WHERE lower(name) like ?";

            pst =  dbConnectionManager.getConnection().prepareStatement(s);
            pst.setString(1, '%' + name.toLowerCase() + '%');
            rs = pst.executeQuery();

            Simulation tmp = null;

            while(rs.next()){

                tmp = new Simulation();
                tmp.setId( rs.getInt("id"));
                tmp.setName(rs.getString ("name"));
                tmp.setCreated(rs.getTimestamp("created").toLocalDateTime());
                tmp.setFinished(rs.getBoolean("finished"));
                result.add(tmp);
            }

        } catch (SQLException e) {
            throw new PersistenceException("There is no simulation,that we can use, we couldnt use in next one : " + e.getMessage());
        }

        LOGGER.debug("Output from method aus der find Simulation WithFilter");
        return result;
    }
}
