import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

class DatabaseConnectionAdmin {
    String id;
    String name;
    String pass;
public void databaseConnectionA(String query) {
        String url = "jdbc:mysql://127.0.0.1:3306/airline";
        String userName = "root";
        String password = "";
        try {
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement   s = conn.createStatement();
        ResultSet  resultSet = s.executeQuery(query);

        while (resultSet.next()) {
        id = resultSet.getString("id");
        name = resultSet.getString("name");
        pass = resultSet.getString("password");
        }
        System.out.println("Connected to SQL");
        s.close();
        } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Connection Problem");
        }
        }
        }
public class AdminLogin {
    private JButton LOGINButton;
    private JButton BACKButton;
    private JTextField userNameTextField;
    private JTextField passwordField;
    public JPanel adminloginPanel;
    private String inputUId="";
    private boolean tC= false;
    private String pass="";

    DatabaseConnectionAdmin dC= new DatabaseConnectionAdmin();

    public AdminLogin (){
        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        //taking Inputs
        inputUId = userNameTextField.getText();

        pass = passwordField.getText();
        //checking inputs
        if(inputUId.equals("")||pass.equals("")){
            JOptionPane.showMessageDialog(userNameTextField,"Blank Not allowed","Blank",JOptionPane.WARNING_MESSAGE);
            userNameTextField.requestFocusInWindow();
        }
        else {
            if(!inputUId.matches("[0-9]+")){
                JOptionPane.showMessageDialog(userNameTextField,"Enter Id Correctly","Wrong Format",JOptionPane.WARNING_MESSAGE);
            }
            else{
                dC.databaseConnectionA("select * from admins where id ="+inputUId);

            //checking the user is available or not
                if (!Objects.equals(dC.name, null)) {
                    //logging in into the account
                    if (inputUId.equals(dC.id) && pass.equals(dC.pass)) {
                        AirLineReservation a = new AirLineReservation();
                        addFlight b = new addFlight();
                        adminloginPanel.setVisible(false);
//                        a.add();
                        a.airLineReservationM(b.addFlightPanel);
                        b.addFlightPanel.setVisible(true);
                        a.menuBar(true);
                        JFrame frame = (JFrame) SwingUtilities.getRoot(adminloginPanel);
                        frame.dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(adminloginPanel,"Wrong Password");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(adminloginPanel,"Admin not found");
                }

        }}
            }
        });

        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginForm l =new loginForm();
                AirLineReservation a= new AirLineReservation();
                a.airLineReservationM(l.loginPanel);
                JFrame frame = (JFrame) SwingUtilities.getRoot(adminloginPanel);
                frame.dispose();
                a.menuBar(false);
                a.myAccount.setSelected(true);
            }
        });
    }
}
