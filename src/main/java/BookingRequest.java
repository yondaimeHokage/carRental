import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;


/**
 * Created by Poojan on 6/29/2018.
 */
public class BookingRequest {


    private static final Logger log = Logger.getLogger(BookingRequest.class.getName());
    private static final Pattern datetimePattern = Pattern.compile("(?<YEAR>\\d{4})-(?<MONTH>\\d{2})-(?<DAY>\\d{2}) (?<HOUR>\\d{2}):(?<MIN>\\d{2})");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final Scanner sc = new Scanner(System.in);
    private CarTypes carType;
    private int numberOfPassengers;
    private String bookingStartDateTime;
    private String bookingEndDateTime;

    public BookingRequest() {
    }

    public BookingRequest(CarTypes carType, String bookingStartDate, String bookingStartTime, String bookingEndDate, String bookingEndTime) {
        this.carType = carType;
        this.bookingStartDateTime = bookingStartDate;
        this.bookingEndDateTime = bookingEndDate;
    }

    public CarTypes getCarType() {
        return carType;
    }

    public void setCarType(CarTypes carType) {
        this.carType = carType;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getBookingStartDateTime() {
        return bookingStartDateTime;
    }

    public void setBookingStartDateTime(String bookingStartDateTime) {
        this.bookingStartDateTime = bookingStartDateTime;
    }

    public String getBookingEndDateTime() {
        return bookingEndDateTime;
    }

    public void setBookingEndDateTime(String bookingEndDateTime) {
        this.bookingEndDateTime = bookingEndDateTime;
    }

    public boolean validatCarType(String carType) {
        for (CarTypes ct : CarTypes.values()) {
            if (ct.name().equalsIgnoreCase(carType))
                return true;
        }
        return false;
    }

    public boolean validateStartAndEndDateTime(String startDateTime, String endDateTime) {
        LocalDateTime strtdt, enddt;
        try {
             strtdt = LocalDateTime.parse(startDateTime, DATE_FORMAT);
             enddt = LocalDateTime.parse(endDateTime, DATE_FORMAT);
        }catch (DateTimeParseException e){
            log.severe("Parse exception for date time . Invalid datetime");
            return false;
        }

        //ensure end date time is after start
        if (!enddt.isAfter(strtdt)) {
            log.info("Invalid start and end date time");
            return false;
        }

        //ensure booking is in future and not past
        LocalDateTime now = LocalDateTime.now().minusMinutes(2L);
        if (strtdt.isAfter(now)) {
            return true;
        } else {
            log.info("Start date time is in the past the current time. INVALID !");
            return false;
        }
    }

    public void initializeBookingRequestFromUser() {
        this.setCarType(getRequestCarType());
        getRequestDateTime();
    }

    private void getRequestDateTime() {
        String strt, end;
        do {
            System.out.println("Enter reservation Start Date time in format yyyy-MM-dd HH:mm ");
            strt = sc.next();
            System.out.println("Enter reservation Start Date time in format yyyy-MM-dd HH:mm ");
            end = sc.next();
        } while (!datetimePattern.matcher(strt).matches() &&
                !datetimePattern.matcher(end).matches() &&
                !validateStartAndEndDateTime(strt, end));

        this.setBookingStartDateTime(strt);
        this.setBookingEndDateTime(end);
    }

    private CarTypes getRequestCarType() {
        String val;
        do {
            System.out.println("Enter Car Type to reserve: ");
            val = sc.next();
        } while (!validatCarType(val));


        return CarTypes.valueOf(val);

    }

}
