import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addFlight {
    private JTextField id;
    private JTextField name;
    private JTextField destplc;
    private JTextField srcplc;
    private JTextField aTime;
    private JTextField dTime;
    private JButton save;
    private JButton back;
    private JTextField price;
    public JPanel addFlightPanel;
    private JPanel datePanel;
    private JButton EDITFLIGHTButton;
    private JLabel flightidlabel;
    private JButton SEARCHButton;
    private JButton UPDATEButton;
    private JButton exitUpdate;
    private JButton DELETEButton;
    private JLabel heading;
    private JTextField seats;
    private JPanel date;
    private String fName = "";
    private String fId;
    private String sP = "";
    private String dP = "";
    private String fprice = "";
    private String fatime = "";
    private String fdtime = "";
    private String fdate = "";
    private String fseats = "";
    private Date d;
    private Date nD = null;
    public boolean querysts = true;
    private boolean executeSts = false;
    public ResultSet resultSet;

    AirLineReservation a = new AirLineReservation();

    public void connectionToDB(String query) {
        String url = "jdbc:mysql://127.0.0.1:3306/airline";
        String userName = "root";
        String password = "";
        try {
            Connection conn = DriverManager.getConnection(url, userName, password);

            Statement s = conn.createStatement();
            if (query.toLowerCase().contains("select")) {
                resultSet = s.executeQuery(query);
                while (resultSet.next()) {
                    fName = resultSet.getString("flightName");
                    sP = resultSet.getString("sourcePlace");
                    dP = resultSet.getString("destinationPlace");
                    d = resultSet.getDate("date");
                    fatime = resultSet.getString("arrivalTime");
                    fdtime = resultSet.getString("departureTime");
                    fprice = resultSet.getString("flightPrice");
                    fseats = resultSet.getString("seats");

                }
                System.out.println("Connected to SQL");
            } else {
                s.executeUpdate(query);
                System.out.println("Connected to SQL");
            }
            executeSts = true;
            s.close();
        } catch (Exception e2) {
            e2.printStackTrace();
            JOptionPane.showMessageDialog(addFlightPanel, "Check Inputs", "Error Occurred", JOptionPane.ERROR_MESSAGE);
            executeSts = false;
        }
    }

    private void textBoxText() {
        fName = name.getText();
        fId = id.getText();
        fprice = price.getText();
        sP = srcplc.getText();
        dP = destplc.getText();
        fatime = aTime.getText();
        fdtime = dTime.getText();
        fseats= seats.getText();
    }

    public addFlight() {
        a.menuBar(false);
        JDateChooser date = new JDateChooser();
        date.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        datePanel.add(date);
        date.setMinSelectableDate(new Date());
        date.setDateFormatString("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fdate = df.format(date.getDate()).toString();
                textBoxText();

                connectionToDB("INSERT INTO `flightdetails` (`flightId`, `flightName`, `sourcePlace`, `destinationPlace`, `date`, `arrivalTime`, `departureTime`, `flightPrice`, `seats`) VALUES ('" + fId + "', '" + fName + "', '" + sP + "', '" + dP + "','" + fdate + "', '" + fatime + "', '" + fdtime + "', '" + fprice + "', '"+fseats+"');");
                if (executeSts) {
                    JOptionPane.showMessageDialog(addFlightPanel, "Flight Added", "Done", JOptionPane.INFORMATION_MESSAGE);

                    id.setText("");
                    name.setText("");
                    destplc.setText("");
                    srcplc.setText("");
                    aTime.setText("");
                    dTime.setText("");
                    price.setText("");
                    seats.setText("");
                    date.cleanup();
                }
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        EDITFLIGHTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id.setText("");
                name.setEnabled(false);
                destplc.setEnabled(false);
                srcplc.setEnabled(false);
                aTime.setEnabled(false);
                dTime.setEnabled(false);
                price.setEnabled(false);
                seats.setEnabled(false);
                date.setEnabled(false);
                EDITFLIGHTButton.setVisible(false);
                flightidlabel.setText("Enter Flight Id");
                SEARCHButton.setVisible(true);
                save.setVisible(false);
                UPDATEButton.setVisible(true);
                back.setVisible(false);
                exitUpdate.setVisible(true);
                DELETEButton.setVisible(true);
                exitUpdate.setText("BACK");
                heading.setText("Edit Flight Details");
            }
        });
        SEARCHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                SEARCHButton.setVisible(false);
                fId = id.getText();
                if (!fId.equals("")) {
                    querysts = false;
                    connectionToDB("select * from flightdetails where flightId=" + fId);
                    if (fName.equals("")) {
                        JOptionPane.showMessageDialog(addFlightPanel, "No Flight Found");
                    } else {
                        id.setText(fId);
                        name.setText(fName);
                        destplc.setText(dP);
                        srcplc.setText(sP);
                        aTime.setText(fatime);
                        dTime.setText(fdtime);
                        price.setText(fprice);
                        seats.setText(fseats);
                        date.setDate(d);
                        name.setEnabled(true);
                        destplc.setEnabled(true);
                        srcplc.setEnabled(true);
                        aTime.setEnabled(true);
                        dTime.setEnabled(true);
                        price.setEnabled(true);
                        seats.setEnabled(true);
                        date.setEnabled(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(addFlightPanel, "Please Enter Flight Id");
                }

            }
        });
        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fdate = df.format(date.getDate()).toString();
                textBoxText();
                querysts = true;
                connectionToDB("update `flightdetails` set `flightId`= " + fId + ", flightName='" + fName + "', `sourcePlace`='" + sP + "', `destinationPlace` ='" + dP + "', `date`='" + fdate + "', `arrivalTime` = '" + fatime + "', `departureTime` ='" + fdtime + "', `flightPrice`='" + fprice +"',`seats` ='"+fseats+"' where flightId =" + fId +";");

                if (executeSts) {
                    JOptionPane.showMessageDialog(addFlightPanel, "Updated", "Done", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        exitUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id.setText("");
                name.setText("");
                destplc.setText("");
                srcplc.setText("");
                aTime.setText("");
                dTime.setText("");
                price.setText("");
                seats.setText("");
                date.setDate(nD);

                EDITFLIGHTButton.setVisible(false);
                flightidlabel.setText("Enter Flight Id");
                SEARCHButton.setVisible(false);
                save.setVisible(true);
                UPDATEButton.setVisible(false);
                back.setVisible(true);
                exitUpdate.setVisible(false);
                DELETEButton.setVisible(false);
                name.setEnabled(true);
                destplc.setEnabled(true);
                srcplc.setEnabled(true);
                aTime.setEnabled(true);
                dTime.setEnabled(true);
                price.setEnabled(true);
                seats.setEnabled(true);
                date.setEnabled(true);
                EDITFLIGHTButton.setVisible(true);
                heading.setText("Add Flight");
            }
        });
        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                querysts = true;
                int del = JOptionPane.showConfirmDialog(addFlightPanel, "Sure to Delete?", "Done", JOptionPane.YES_NO_OPTION);
                if (del == 0) {
                    connectionToDB("delete from flightdetails where flightId=" + fId);
                    id.setText("");
                    name.setText("");
                    destplc.setText("");
                    srcplc.setText("");
                    aTime.setText("");
                    dTime.setText("");
                    price.setText("");
                    seats.setText("");
                    date.setDate(nD);
                }
            }
        });
    }

//    public static void main(String[] args) {
//        Frame f = new Frame();
//        f.add(new addFlight().addFlightPanel);
//        f.setVisible(true);
//    }
}
