import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;
import java.util.Objects;

public class Payment {

    private JTextField nameIn;
    private JTextField cardIn;
    private JButton MAKEPAYMENTButton;
    private JButton CANCELButton;
    private JPasswordField cvvIn;
    public JPanel payment;
    private JLabel details;
    private JLabel tno;
    private JLabel price;
    public static double p;
    public static String from;
    public static String to;
    public static String date;
    public static String time;
    public static long tId;
    public static String ticket;
    private static String rfId;
    private static String ruId;
    private static String rRdate;
    private static String rSeat;
    private static String totDseat;
    private static String totRseat;
    private static String returnFlightId;
    private boolean sQ;


    public void amount(double amnt, String fP, String tP, String d, String t, String uId, String fId, String rdate, String rSeats, String tdSeats, String trseats, String rRfId) {
        p = amnt;
        from = fP;
        to = tP;
        date = d;
        time = t;
        ruId = uId;
        rfId = fId;
        rRdate = rdate;
        rSeat = rSeats;
        totDseat = tdSeats;
        totRseat = trseats;
        returnFlightId = rRfId;
        ticket = from.substring(0, 2).toUpperCase() + to.substring(0, 2).toUpperCase() + uId + date.replace("-", "");
    }

    public void connectionToDB(String query) {
        String url = "jdbc:mysql://127.0.0.1:3306/airline";
        String userName = "root";
        String password = "";
        try {
            Connection conn = DriverManager.getConnection(url, userName, password);

            Statement s = conn.createStatement();
            s.executeUpdate(query);
            System.out.println("Connected to SQL");
            sQ = true;
        } catch (Exception e2) {
            e2.printStackTrace();
            sQ = false;
        }
    }

    loginForm l = new loginForm();
    AirLineReservation a = new AirLineReservation();

    public Payment() {

        price.setText("Amount: " + String.valueOf(p));
        tId = new Date().getTime();
        details.setText("From: " + from + ", To: " + to + ", On: " + date + ", At: " + time);
        tno.setText("Ticket No: " + ticket);

        CANCELButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getRoot(payment);
                frame.dispose();
            }
        });
        MAKEPAYMENTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(cardIn.getText(), "") || Objects.equals(nameIn.getText(), "") || cvvIn.getText().toString().equals("")) {
                    JOptionPane.showMessageDialog(payment, "Please Enter the Card Details");
                } else if (!(cardIn.getText().length() == 16)) {
                    JOptionPane.showMessageDialog(cardIn, "Please Enter the Valid Card Number");
                } else if (!(cvvIn.getText().length() == 3)) {
                    JOptionPane.showMessageDialog(cvvIn, "Please Enter the Valid CVV");
                } else {
                    int opt = JOptionPane.showConfirmDialog(payment, "Total Rs. " + p + " /-\nSure to Pay? ", "Confirm Payment", JOptionPane.YES_NO_OPTION);
                    if (opt == 0) {
                        connectionToDB("INSERT INTO `bookedtickets` (`ticketNo`, `flightId`, `userid`, `fromPlace`, `toPlace`, `seatnumbers`, `departureDate`, `returnDate`) VALUES ('" + ticket + "', '" + rfId + "', '" + ruId + "', '" + from + "', '" + to + "', '" + rSeat + "', '" + date + "', '" + rRdate + "');");
                        if (sQ) {
                            connectionToDB("INSERT INTO `payment` (`ticketno`, `transid`, `cardno`, `name`, `datetime`,`price`) VALUES ('" + ticket + "', '" + tId + "', '" + cardIn.getText() + "', '" + nameIn.getText() + "', CURRENT_TIMESTAMP,'" + p + "');");

                            //DECREASE SEAT NO
                            connectionToDB("UPDATE `flightdetails` SET `seats` = '" + (Integer.parseInt(totDseat) - Integer.parseInt(rSeat)) + "' WHERE `flightdetails`.`flightId` ='" + rfId + "'; ");
                            if (!(returnFlightId.isEmpty())) {
                                connectionToDB("UPDATE `flightdetails` SET `seats` = '" + (Integer.parseInt(totRseat) - Integer.parseInt(rSeat)) + "' WHERE `flightdetails`.`flightId` ='" + returnFlightId + "'; ");
                                JOptionPane.showMessageDialog(payment, "   TICKET BOOKED\n   Thank You!! \nYour Ticket No Is: " + ticket, "Success", JOptionPane.INFORMATION_MESSAGE);
                            }

                        } else {
                            JOptionPane.showMessageDialog(payment, "Ticket Already Booked", "Failed", JOptionPane.INFORMATION_MESSAGE);
                        }
                        JFrame frame = (JFrame) SwingUtilities.getRoot(payment);
                        frame.dispose();
                    }
                }
            }
        });
    }

}
