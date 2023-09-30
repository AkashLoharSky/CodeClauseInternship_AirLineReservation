import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

 class DatabaseConnection {
    String id;
    String name;
    String add;
    String gen;
    String dob;
    String mail;
    String mobile;
    String pass;
     Statement s;
    public ResultSet resultSet;

        public String databaseConnectionM(String query) {
        String url = "jdbc:mysql://127.0.0.1:3306/airline";
        String userName = "root";
        String password = "";
        try {
            Connection   conn = DriverManager.getConnection(url, userName, password);
             s = conn.createStatement();
            resultSet = s.executeQuery(query);

            while (resultSet.next()) {
                id = resultSet.getString("id");
                name = resultSet.getString("name");
                gen = resultSet.getString("gender");
                pass = resultSet.getString("password");
                dob = resultSet.getString("dob");
                mobile = resultSet.getString("mobile");
                mail = resultSet.getString("mail");
                add = resultSet.getString("address");
            }
            System.out.println("Connected to SQL");
           s.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection Problem: " + e.getMessage());
        }
        return id;
    }
}

public class loginForm {
    public JPanel loginPanel;
    private JButton signUpBtn;
    public JTextField userNameTextField;
    private JPasswordField passwordField;
    public JButton loginBtn;
    private JLabel passwordWL;
    private JLabel uNameWL;
    private JButton adminButton;
    private String inputUN;
    private boolean tC=false;
    private String pass;
    public static String uId;
    DatabaseConnection dC = new DatabaseConnection();
    registrationForm R= new registrationForm();

    public loginForm() {
        userNameTextField.requestFocusInWindow();
        userNameTextField.grabFocus();
        uNameWL.setVisible(false);
        passwordWL.setVisible(false);
        R.registrationPanel.setVisible(false);
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //taking Inputs
                inputUN = userNameTextField.getText();
                //noinspection deprecation
                pass = passwordField.getText();
                //checking inputs
                if(inputUN.equals("")||pass.equals("")){
                    JOptionPane.showMessageDialog(userNameTextField,"Blank Not allowed","Blank",JOptionPane.WARNING_MESSAGE);
                }
                else{
                //connection with sql with given Email or Mobile
                if(inputUN.contains("@")){
                dC.databaseConnectionM("select * from users where mail ='"+inputUN+"'");
                tC=true;
                }
                else if(inputUN.matches("[0-9]+")){
                dC.databaseConnectionM("select * from users where mobile ="+inputUN);
                tC= true;
                }
                else{
                    JOptionPane.showMessageDialog(userNameTextField,"Enter Mobile Number or Email Correctly","Wrong Format",JOptionPane.WARNING_MESSAGE);
                }
                //checking the user is available or not
                if(tC){
                if (!Objects.equals(dC.name, null)) {
                //logging in into the account
                    if ((inputUN.equals(dC.mobile) || inputUN.equals(dC.mail))&& pass.equals(dC.pass)) {
                        uId =dC.id;
                        AirLineReservation a = new AirLineReservation();
                        booking b = new booking();
                        loginPanel.setVisible(false);
                        a.menuBar(true);
                        a.bookF.setEnabled(false);
                        a.airLineReservationM(b.bookingPanel);
                        b.bookingPanel.setVisible(true);
                        b.receiveUserDetails(uId);
                        new viewEditUser().getUserId(uId);
                        JFrame frame = (JFrame) SwingUtilities.getRoot(loginPanel);
                        frame.dispose();
                    }
                    else{
                        passwordWL.setVisible(true);
                        uNameWL.setVisible(false);
                    }
                }
                else{
                    uNameWL.setVisible(true);
                }}
            }
            }
        });
        signUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AirLineReservation a = new AirLineReservation();
                a.menuBar(false);
                a.airLineReservationM(R.registrationPanel);
                R.registrationPanel.setVisible(true);
                JFrame frame = (JFrame) SwingUtilities.getRoot(loginPanel);
                frame.dispose();
            }
        });

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                passwordWL.setVisible(false);
            }
        });
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AirLineReservation a = new AirLineReservation();
                AdminLogin aL =new AdminLogin();
                a.airLineReservationM(aL.adminloginPanel);
                JFrame frame = (JFrame) SwingUtilities.getRoot(loginPanel);
                frame.dispose();
            }
        });

    }

}