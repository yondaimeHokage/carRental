import org.hsqldb.Server;

import java.sql.*;
import java.util.logging.Logger;

/**
 * Created by Poojan on 6/28/2018.
 */
public class Database {

    private static final Logger log = Logger.getLogger(Database.class.getName());
    public static final String CLASS_NAME = "org.hsqldb.jdbc.JDBCDriver";
    public static final String URL = "jdbc:hsqldb:hsql://localhost/testdb";
    public static final String USER = "SA";
    public static final String PASSWORD = "";
    private static final Database instance = new Database();
    public static final String CREATE_CAR_TABLE = "CREATE TABLE CAR (\n" +
            "   id VARCHAR(10) NOT NULL,\n" +
            "   type VARCHAR(50) NOT NULL,\n" +
            "   model VARCHAR(20),\n" +
            "   noOfPass INT,\n" +
            "   PRIMARY KEY (id) \n" +
            ");";
    public static final String CREATE_RESERVATION_TABLE = "CREATE TABLE RESERVATION (\n" +
            "   rid VARCHAR(10) NOT NULL,\n" +
            "   cid VARCHAR(10) NOT NULL,\n" +
            "   startDateTime VARCHAR(25) NOT NULL,\n" +
            "   endDateTime VARCHAR(25) NOT NULL,\n" +
            "   costperday VARCHAR(4),\n" +
            "   status VARCHAR(10) NOT NULL,\n" +
            "   PRIMARY KEY (rid) \n" +
            ");";
    public static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE CUSTOMER (\n" +
            "   pid VARCHAR(10) NOT NULL,\n" +
            "   age VARCHAR(3) NOT NULL,\n" +
            "   licenseState VARCHAR(20),\n" +
            "   licensceNumber VARCHAR(20) NOT NULL,\n" +
            "   PRIMARY KEY (pid) \n" +
            ");";

    private static Server hsqlServer;

    private Database(){}

    public static Database getInstance(){
        return instance;
    }

    private void createDbServe() {
        log.info("Creating new server");
        hsqlServer = new Server();
        hsqlServer.setLogWriter(null);
        hsqlServer.setSilent(true);
        log.info("Initializing database name and Path");
        hsqlServer.setDatabaseName(0, "testdb");
        hsqlServer.setDatabasePath(0, "file:testdb");
    }

    public void startDatabaseServer() {
        if (hsqlServer == null) {
            createDbServe();
        }
        hsqlServer.start();
    }

    public void stopDatabaseServer() {
        if (hsqlServer == null) {
            return;
        }
        hsqlServer.stop();
    }

    public Connection createConnection() {
        Connection connection = null;
        try {
            //Registering the HSQLDB JDBC driver
            Class.forName(CLASS_NAME);
            //Creating the connection with HSQLDB
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                log.info("Connection created successfully");
            } else {
                log.severe("Problem with creating connection");
            }
        } catch (ClassNotFoundException | SQLException e) {
            log.severe("Exception Creating Connection");
            e.printStackTrace();
        }
        return connection;
    }

    public void setupDb() throws SQLException {
        setUpCarTable();
        SetUpReservationTable();
        setUpCustomerTable();
    }

    private void setUpCustomerTable() throws SQLException {
        try(Connection connection = instance.createConnection()){

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet r =  metaData.getTables(null,null,"CUSTOMER", null);
            if(r.next())
                return;
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_CUSTOMER_TABLE);

        } catch (SQLException e){
            log.severe("Unable to setup Customer table");
            e.printStackTrace();
            throw e;
        }
    }

    private void SetUpReservationTable() throws SQLException {
        try(Connection connection = instance.createConnection()){

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet r =  metaData.getTables(null,null,"RESERVATION", null);
            if(r.next())
                return;
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_RESERVATION_TABLE);

        } catch (SQLException e){
            log.severe("Unable to setup Reservation table");
            e.printStackTrace();
            throw e;
        }
    }

    private void setUpCarTable() throws SQLException {
        try(Connection connection = instance.createConnection()){

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet r =  metaData.getTables(null,null,"CAR", null);
            if(r.next())
                return;
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_CAR_TABLE);

        } catch (SQLException e){
            log.severe("Unable to setup Car table");
            e.printStackTrace();
            throw e;
        }
    }
}
