import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FlightList {
    private DefaultTableModel model= new DefaultTableModel();
    public JPanel flightlistpnl;
    private JTable table1;

    FlightList() {
        AirLineReservation a= new AirLineReservation();
        flightlistpnl.grabFocus();
        a.fList.setFocusable(false);
        a.fList.setSelected(false);
        table1.setModel(model);

        flightlistpnl.add(new JScrollPane(table1), BorderLayout.CENTER);

        String url = "jdbc:mysql://127.0.0.1:3306/airline";
        String userName = "root";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, userName, password);
            Statement s = conn.createStatement();
            ResultSet resultSet = s.executeQuery("SELECT * FROM flightdetails;");

            // Set column names explicitly if needed
            model.setColumnIdentifiers(new Object[]{"Flight ID", "Flight Name", "Source Place", "Destination Place", "Date", "Arrival Time", "Departure Time", "Flight Price"});

            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getString("flightId"),
                        resultSet.getString("flightName"),
                        resultSet.getString("sourcePlace"),
                        resultSet.getString("destinationPlace"),
                        resultSet.getString("date"),
                        resultSet.getString("arrivalTime"),
                        resultSet.getString("departureTime"),
                        resultSet.getString("flightPrice")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(flightlistpnl, "Database connection problem: " + e.getMessage());
        }
    }

}
