import org.hsqldb.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

}
