import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by Poojan on 6/28/2018.
 */
public class Reservation {

    private static final Logger log = Logger.getLogger(Reservation.class.getName());
    private static long rid =1;
    private static final Reservation instance = new Reservation();
    public static final String SELECT_CARIDS = "SELECT CARID FROM CAR where CARTYPE=?";
    public static final String SELECT_TIME_FOR_CARID = "SELECT SATRTDATETIME, ENDDATETIME FROM RESERVATION WHERE CARID=?";
    public static final String INSERT_INTO_RESERVATION = "INSERT INTO RESERVATION VALUES (?,?,?,?,?,?)";


    private Reservation() {
    }

    public static Reservation getInstance() {
        return instance;
    }

    public void createNew() {

        BookingRequest r = new BookingRequest();
        r.initializeBookingRequestFromUser();
        List<String> carIds;

        try (Connection con = Database.getInstance().createConnection()) {

            //list of unique cars available for this type
            carIds = getUniqueCarIdsForType(r.getCarType().name(), con);

            //now find reservation times of ids to find and available car
            for (String id : carIds) {
                List<String> rsrvtimes = getReservationTimesForId(id, con);
                if (carAvailable(rsrvtimes, r.getBookingStartDateTime(), r.getBookingEndDateTime())) {
                    log.info("Creating Connection");
                    createReservation(r, con, id);
                    return;
                }
            }

            log.info("Unable to Create reservation as required car type not available for requested time");
            System.out.println("Unable to Create reservation as required car type not available for requested time");

        } catch (SQLException e) {
            log.severe("Unable to get list of Unique car ids for listed Car Type");
            e.printStackTrace();
            return;
        }


    }

    private void createReservation(BookingRequest r, Connection con, String carid) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(INSERT_INTO_RESERVATION);
        preparedStatement.setString(1, ""+(rid++));
        preparedStatement.setString(2, carid);
        preparedStatement.setString(3, r.getBookingStartDateTime());
        preparedStatement.setString(4, r.getBookingEndDateTime());
//        long cost = LocalDateTime.parse(r.getBookingStartDateTime(),BookingRequest.DATE_FORMAT).m;
//        preparedStatement.setString(5, ""+cost);

    }

    private boolean carAvailable(List<String> rsrvtimes, String bookingStartDateTime, String bookingEndDateTime) {

        for (String t : rsrvtimes) {
            String st = t.split("XX")[0];
            String en = t.split("XX")[1];
            LocalDateTime start = LocalDateTime.parse(st, BookingRequest.DATE_FORMAT);
            LocalDateTime end = LocalDateTime.parse(en, BookingRequest.DATE_FORMAT);

            LocalDateTime reqStart = LocalDateTime.parse(bookingStartDateTime, BookingRequest.DATE_FORMAT);
            LocalDateTime reqEnd = LocalDateTime.parse(bookingEndDateTime, BookingRequest.DATE_FORMAT);


            //find overlapping time
            if (reqStart.isAfter(start) && reqStart.isBefore(end))
                return false;
            else if (reqEnd.isAfter(start) && reqEnd.isBefore(end))
                return false;
            else if (reqStart.isEqual(start) ||
                    reqStart.isEqual(end) ||
                    reqEnd.isEqual(reqStart) ||
                    reqEnd.isEqual(reqEnd))
                return false;

        }
        return true;
    }

    private List<String> getReservationTimesForId(String id, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TIME_FOR_CARID);
        preparedStatement.setString(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        List<String> time = new ArrayList<>();
        while (rs.next()) {
            time.add((rs.getString(1) + "XX" + rs.getString(2)));
        }

        return time;
    }

    private List<String> getUniqueCarIdsForType(String name, Connection connection) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CARIDS);
        preparedStatement.setString(1, name);
        ResultSet rs = preparedStatement.executeQuery();
        List<String> ids = new ArrayList<>();
        while (rs.next()) {
            ids.add(rs.getString(1));
        }
        return ids;
    }

    private void getBookingRequest() {
        Boolean flag = true;
        Scanner sc = new Scanner(System.in);
        while (flag) {

            System.out.println("Enter 1 to CREATE NEW RESERVATION");
            System.out.println("Enter 2 to MODIFY EXISTING RESERVATION");
            System.out.println("Enter 3 to CANCEL RESERVATION");
            System.out.println("Enter 4 to EXIT CarRental");
            System.out.print("Your Entry : ");
            int option = sc.nextInt();

        }
    }

    public void modify() {

    }

    public void delete() {

    }
}
