import com.sun.org.apache.xpath.internal.SourceTree;

import java.sql.Connection;
import java.util.Scanner;

/**
 * Created by Poojan on 6/28/2018.
 */
public class Main {

    public static void main(String[] args) {

        Database instance = Database.getInstance();
        instance.startDatabaseServer();
//        Connection connection = Database.getInstance().createConnection();
        Scanner sc = new Scanner(System.in);
        Reservation reserve = Reservation.getInstance();
        Boolean flag = true;
        System.out.println("Welcome to CarRental Service");


        while(flag){
            System.out.println("Enter 1 to CREATE NEW RESERVATION");
            System.out.println("Enter 2 to MODIFY EXISTING RESERVATION");
            System.out.println("Enter 3 to CANCEL RESERVATION");
            System.out.println("Enter 4 to EXIT CarRental");
            System.out.print("Your Entry : ");
            int option = sc.nextInt();
            switch (option) {
                case 1:
                    reserve.createNew();
                    break;
                case 2:
                    reserve.modify();
                    break;
                case 3:
                    reserve.delete();
                    break;
                case 4:
                    flag =false;
                    break;
                default:
                    System.out.println("INVALID ENTRY PLEASE TRY AGAIN !");
                    break;
            }
        }

    }
}
