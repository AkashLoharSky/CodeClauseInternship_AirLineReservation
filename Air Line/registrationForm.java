import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class registrationForm {
    public JPanel registrationPanel;
    public JTextField name;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JRadioButton othersRadioButton;
    private JTextField pass;
    private JTextField email;
    private JTextField conpass;
    private JTextArea add;
    private JComboBox month;
    private JComboBox year;
    private JButton REGISTERButton;
    private JButton BACKButton;
    private JButton EXITButton;
    private JTextField mobile;
    private JComboBox day;
    private JTextArea textArea1;
    private ButtonGroup bG=new ButtonGroup();
    private String dayT;
    public String monthT;
    private String yearT;

//connection for Registration
    static void databaseConnectionR (String query){

        try{
            String url= "jdbc:mysql://127.0.0.1:3306/airline";
            String user ="root";
            String pass="";
            Connection conn= DriverManager.getConnection(url,user,pass);
            Statement s = conn.createStatement();
            s.executeUpdate(query);
            System.out.println("Connection Done");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    registrationForm(){
        name.requestFocus();
        name.grabFocus();
        maleRadioButton.setActionCommand("M");
        femaleRadioButton.setActionCommand("F");
        othersRadioButton.setActionCommand("O");
        bG.add(maleRadioButton);
        bG.add(femaleRadioButton);
        bG.add(othersRadioButton);

        EXITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginForm l =new loginForm();
                AirLineReservation a= new AirLineReservation();
                a.airLineReservationM(l.loginPanel);
                JFrame frame = (JFrame) SwingUtilities.getRoot(registrationPanel);
                frame.dispose();
                a.menuBar(false);
                a.myAccount.setSelected(true);
            }
        });
        REGISTERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nmT = name.getText();
                dayT= Objects.requireNonNull(day.getSelectedItem()).toString();
                monthT= Integer.valueOf(month.getSelectedIndex()).toString();
                yearT= Objects.requireNonNull(year.getSelectedItem()).toString();
                String mbT = mobile.getText();
                String emlT = email.getText();
                String addT = add.getText();
                String passT= pass.getText();
                String cPassT= conpass.getText();


                if(nmT.isEmpty()||emlT.isEmpty()||addT.isEmpty()|| mbT.isEmpty()|| cPassT.isEmpty() ||dayT.equals("Day")||monthT.equals("Month")||yearT.equals("Year")|| bG.getSelection()==null){
                    JOptionPane.showMessageDialog(registrationPanel,"Blank Not allowed","Blank Entry",JOptionPane.WARNING_MESSAGE);
                } else if (!nmT.matches("[a-zA-Z]+.*\\s.*")){
                    JOptionPane.showMessageDialog(name,"Enter Name Properly!\nOnly Letters are Allowed","Not in Proper Format",JOptionPane.WARNING_MESSAGE);
                } else if (!mbT.matches("[0-9]+")) {
                    JOptionPane.showMessageDialog(mobile,"Enter Mobile Number Properly\nOnly Numbers are Allowed","Not in Proper Format",JOptionPane.WARNING_MESSAGE);
                } else if (!emlT.contains("@")) {
                    JOptionPane.showMessageDialog(email,"Enter Email Address Properly","Not in Proper Format",JOptionPane.WARNING_MESSAGE);
                } else if (!cPassT.equals(passT)) {
                    JOptionPane.showMessageDialog(email,"Confirm Password Doesn't Match","Confirm Password Not Matched",JOptionPane.WARNING_MESSAGE);
                } else {
                    String genT=bG.getSelection().getActionCommand();
                    System.out.println(genT);
                    databaseConnectionR("INSERT INTO `users` (`id`, `name`, `gender`, `dob`, `mobile`, `mail`, `password`, `address`) VALUES (NULL, '"+nmT+"', '"+genT+"', '"+yearT+"-"+monthT+"-"+dayT+"', '"+mbT+"', '"+emlT+"', '"+passT+"','"+addT+"');");
                    JOptionPane.showMessageDialog(registrationPanel,"Sign Up Successful..\nPlease Login to Continue","User Added",JOptionPane.INFORMATION_MESSAGE);
                    loginForm l =new loginForm();
                    AirLineReservation a= new AirLineReservation();
                    a.airLineReservationM(l.loginPanel);
                    JFrame frame = (JFrame) SwingUtilities.getRoot(registrationPanel);
                    frame.dispose();
                }
            }
        });
        //tab on address
        add.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_TAB){
                    e.consume();
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                }

            }
        });
    }


}
