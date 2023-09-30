import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class dbConnection {
    public static String fId = "";
    String fName = "";
    String fDTime = "";
    String fATime = "";
    String fPrice = "";
    public static String fSeats = "";
    String url = "jdbc:mysql://127.0.0.1:3306/airline";
    String userName = "root";
    String password = "";
    public ArrayList<String> destPlaces = new ArrayList<String>();
    public ArrayList<String> sourcePlaces = new ArrayList<String>();
    List<String> filteredSuggestions = new ArrayList<>();
    int i = 0;

    void dbConnection(String query) {
        try {
            Connection conn = DriverManager.getConnection(url, userName, password);
            Statement s = conn.createStatement();
            ResultSet resultSet = s.executeQuery(query);

            while (resultSet.next()) {
                fId = resultSet.getString("flightId");
                fName = resultSet.getString("flightName");
                sourcePlaces.add(i, resultSet.getString("sourcePlace"));
                destPlaces.add(i, resultSet.getString("destinationPlace"));
                resultSet.getString("date");
                fATime = resultSet.getString("arrivalTime");
                fDTime = resultSet.getString("departureTime");
                fPrice = resultSet.getString("flightPrice");
                fSeats = resultSet.getString("seats");
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection problem: " + e.getMessage());
        }
    }
}

public class booking {
    private JRadioButton oneWay;
    private JRadioButton roundTrip;
    private JRadioButton multiCity;
    private JComboBox start;
    private JComboBox dest;
    private JComboBox setas;
    private JButton proceedToPay;
    private JButton CANCELButton;
    public JPanel bookingPanel;
    private JPanel retDate;
    private JPanel depDate;
    private JList Sc;
    DefaultListModel listModel;
    private JButton ADDButton;
    private JLabel scL;
    private JButton REMOVEButton;
    private JLabel tDetails;
    private JComboBox classC;
    private JButton CHECKButton;
    private JLabel tD2;
    private static String multiCityList;
    //    private static int seatNo;
    private List<String> startData = new ArrayList<>();
    private List<String> destData = new ArrayList<>();
    ButtonGroup bG = new ButtonGroup();
    AirLineReservation air = new AirLineReservation();
    static String selectedSource = "";
    static String selectedDest = "";
    static String uId = "";
    String sDate = "";
    String rDate = "";
    Boolean sts;
    dbConnection db = new dbConnection();

    public void receiveUserDetails(String userId) {
        uId = userId;
    }

    void flightChk(dbConnection db2, dbConnection db, String sDate, String rDate, String cL) {
        int s;
        proceedToPay.setEnabled(true);
        tDetails.setForeground(Color.green);
        tD2.setForeground(Color.green);
        double fP2 = Double.valueOf(db.fPrice);
        double fP = Double.valueOf(db2.fPrice);

        if (setas.getSelectedItem().toString().equals(">9")) {
            s = 10;
        } else {
            s = Integer.parseInt(setas.getSelectedItem().toString());
        }
        if (Integer.parseInt(db2.fSeats) < Integer.parseInt(setas.getSelectedItem().toString())) {
            tDetails.setText("Sorry! Available Seats=" + setas.getSelectedItem().toString());
        } else {
            if (classC.getSelectedItem().toString().equals("Economy")) {
                tDetails.setText("  Flight Id: " + db2.fId + ",  Flight Name: " + db2.fName + ",  Price: Rs." + (fP * s) + ",  Depature Time: " + db2.fDTime + ",  Landing Time: " + db2.fATime + " on " + sDate);
                tD2.setText("  Flight Id: " + db.fId + ",  Flight Name: " + db.fName + ",  Price: Rs." + (fP2 * s) + ",  Depature Time: " + db.fDTime + ",  Landing Time: " + db.fATime + " on " + rDate);
                rDate = "0000-00-00";
                new Payment().amount(fP * s, selectedSource, selectedDest, sDate, db2.fDTime, uId, db2.fId, rDate, setas.getSelectedItem().toString(), db2.fSeats, db.fSeats, db.fId);

            } else if (classC.getSelectedItem().toString().equals("Business")) {
                tDetails.setText("Departure: Flight Id: " + db2.fId + ",  Flight Name: " + db2.fName + ",  Price: Rs." + ((fP + (fP * 0.20)) * s) + ",  Depature Time: " + db2.fDTime + ",  Landing Time: " + db2.fATime + " on " + sDate);
                tD2.setText("Return: Flight Id: " + db.fId + ",  Flight Name: " + db.fName + ",  Price: Rs." + ((fP2 + (fP2 * 0.20)) * s) + ",  Depature Time: " + db.fDTime + ",  Landing Time: " + db.fATime + " on " + rDate);
                new Payment().amount((((fP + (fP * 0.20)) * s) + ((fP2 + (fP2 * 0.20)) * s)), selectedSource, selectedDest, sDate, rDate, uId, db2.fId, db.fDTime, setas.getSelectedItem().toString(), db2.fSeats, db.fSeats, db.fId);
            } else if (classC.getSelectedItem().toString().equals("First")) {
                tDetails.setText("Departure: Flight Id: " + db2.fId + ",  Flight Name: " + db2.fName + ",  Price: Rs." + ((fP + (fP * 0.35)) * s) + ",  Depature Time: " + db2.fDTime + ",  Landing Time: " + db2.fATime + " on " + rDate);
                tD2.setText("Return: Flight Id: " + db.fId + ",  Flight Name: " + db.fName + ",  Price: Rs." + ((fP2 + (fP2 * 0.35)) * s) + ",  Depature Time: " + db.fDTime + ",  Landing Time: " + db.fATime + " on " + rDate);
                new Payment().amount((((fP + (fP * 0.20)) * s) + ((fP2 + (fP2 * 0.20d)) * s)), selectedSource, selectedDest, sDate, rDate, uId, db2.fId, db.fDTime, setas.getSelectedItem().toString(), db2.fSeats, db.fSeats, db.fId);
            } else {
                tDetails.setText("Please fill up the form Correctly.");
                tDetails.setForeground(Color.red);
                proceedToPay.setEnabled(false);
            }
        }
    }

    public booking() {
        proceedToPay.setEnabled(false);
        Sc.setVisible(false);
        ADDButton.setVisible(false);
        scL.setVisible(false);
        REMOVEButton.setVisible(false);
        db.dbConnection("SELECT * FROM flightdetails;");
        oneWay.setSelected(true);

        for (int j = 0; db.i > j; j++) {
            startData.add(db.sourcePlaces.get(j).toString());
            destData.add(db.destPlaces.get(j).toString());
        }
        //date chooser
        JDateChooser dD = new JDateChooser();
        DateFormat dF = new SimpleDateFormat("mm-dd-yyyy");
        JDateChooser rD = new JDateChooser();
        air.menuBar(true);
        air.bookF.setEnabled(false);
        retDate.add(rD);
        depDate.add(dD);
        //buttongroup
        bG.add(oneWay);
        bG.add(multiCity);
        bG.add(roundTrip);
        rD.setEnabled(false);
        oneWay.setActionCommand("O");
        multiCity.setActionCommand("M");
        roundTrip.setActionCommand("R");
        listModel = new DefaultListModel();
        Sc.setModel(listModel);
        dD.setMinSelectableDate(new Date());
        rD.setMinSelectableDate(new Date());

        start.setModel(new DefaultComboBoxModel<>(startData.toArray(new String[0])));
        dest.setModel(new DefaultComboBoxModel<>(destData.toArray(new String[0])));

        // Add ActionListener to start for searching
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = ((JComboBox<String>) e.getSource()).getEditor().getItem().toString();

            }
        });

        dest.setEditable(true);
        oneWay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tDetails.setText("");
                tD2.setText("");
                rD.setEnabled(false);
                Sc.setVisible(false);
                ADDButton.setVisible(false);
                scL.setVisible(false);
                REMOVEButton.setVisible(false);
            }
        });

        roundTrip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tDetails.setText("");
                tD2.setText("");
                rD.setEnabled(true);
                Sc.setVisible(false);
                ADDButton.setVisible(false);
                scL.setVisible(false);
                REMOVEButton.setVisible(false);
            }
        });

        multiCity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rD.setMinSelectableDate(dD.getDate());
                tDetails.setText("");
                tD2.setText("");
                rD.setEnabled(true);
                Sc.setVisible(true);
                ADDButton.setVisible(true);
                scL.setVisible(true);
                REMOVEButton.setVisible(true);
            }
        });
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = dest.getSelectedItem().toString();
                if (!listModel.contains(selectedItem)) {
                    listModel.addElement(selectedItem);

                }
            }
        });
        REMOVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.removeElementAt(Sc.getSelectedIndex());
            }
        });

        CHECKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((oneWay.isSelected() && start.getSelectedIndex() == -1 || dest.getSelectedIndex() == -1 || dD.getDate() == null)) {
                    JOptionPane.showMessageDialog(bookingPanel, "Fill the form correctly", "Blank found", JOptionPane.ERROR_MESSAGE);
                } else if (roundTrip.isSelected() && rD.getDate() == null) {
                    JOptionPane.showMessageDialog(bookingPanel, "Fill the form correctly", "Blank found", JOptionPane.ERROR_MESSAGE);
                } else {
                    selectedSource = start.getSelectedItem().toString();
                    selectedDest = dest.getSelectedItem().toString();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    sDate = df.format(dD.getDate()).toString();

                    dbConnection db2 = new dbConnection();
                    db2.dbConnection("select * from flightdetails where `sourcePlace`=  '" + selectedSource + "' and `destinationPlace`= '" + selectedDest + "' and `date` ='" + sDate + "';");
                    if (db2.fPrice == "") {
                        tDetails.setText("No flight available. Please try an another date");
                        tDetails.setForeground(Color.red);
                        proceedToPay.setEnabled(false);
                    } else {
                        flightChk(db2, db2, sDate, null, null);
                        tD2.setText("");

                    }
                    if (rD.isEnabled()) {
                        multiCityList = listModel.toString().replace("[", "");
                        multiCityList = multiCityList.replace("]", "");

//                    System.out.println(multiCityList);
                        rDate = df.format(rD.getDate()).toString();
                        dbConnection db3 = new dbConnection();
                        db3.dbConnection("select * from flightdetails where `sourcePlace`=  '" + selectedDest + "' and `destinationPlace`= '" + selectedSource + "' and `date` ='" + rDate + "';");
                        if (db3.fPrice == "") {
                            tD2.setText("No rturn flight available. Please try an another date");
                            tD2.setForeground(Color.red);
                            proceedToPay.setEnabled(false);
                        } else {
                            if (multiCity.isSelected()) {
                                selectedDest = multiCityList;
                            }
                            flightChk(db2, db3, sDate, rDate, multiCityList);
                            tD2.setForeground(Color.green);
                        }
                    }

                }
            }
        });
        proceedToPay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AirLineReservation a = new AirLineReservation();
                a.airLineReservationM(new Payment().payment);
                a.frame.setSize(1000, 700);
                a.frame.setLocationRelativeTo(null);
                a.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
    }
}
