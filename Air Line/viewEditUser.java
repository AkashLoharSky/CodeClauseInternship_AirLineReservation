import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
class DatabaseConnectionForEdit {

    public  String name;
    public  String add;
    public  String gen;
    public  String dob;
    public   String mail;
    public  String mobile;
    public  String pass;
    Statement s;
    public ResultSet resultSet;
    public boolean b;
    public void databaseConnectionM(String query) {
        String url = "jdbc:mysql://127.0.0.1:3306/airline";
        String userName = "root";
        String password = "";
        try {
            Connection conn = DriverManager.getConnection(url, userName, password);
            s = conn.createStatement();
            if(query.contains("UPDATE")){
                s.executeUpdate(query);
            }
            else{
            resultSet = s.executeQuery(query);

            while (resultSet.next()) {
                name = resultSet.getString("name");
                gen = resultSet.getString("gender");
                pass = resultSet.getString("password");
                dob = resultSet.getString("dob");
                mobile = resultSet.getString("mobile");
                mail = resultSet.getString("mail");
                add = resultSet.getString("address");
            }
            }
            System.out.println("Connected to SQL");
            s.close();
            b=true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection Problem: " + e.getMessage());
            b=false;
        }
    }

}
public class viewEditUser {
    private JTextField ADDRESSTextField;
    private JTextField PASSWORDTextField;
    private JTextField NAMETextField;
    private JTextField DOBTextField;
    private JTextField EMAILTextField;
    private JTextField MOBILETextField;
    private JButton EDITButton;
    private JButton CANCELButton;
    private JLabel edit;
    public JPanel viewEditPnl;
    public static String uId;
    DatabaseConnectionForEdit Db  = new DatabaseConnectionForEdit();
    public void getUserId(String uid){
        uId=uid;
    }
    viewEditUser() {
        Db.databaseConnectionM("select * from users where `id`='"+uId+"'");
        NAMETextField.setText(Db.name);
        DOBTextField.setText(Db.dob);
        MOBILETextField.setText(Db.mobile);
        EMAILTextField.setText(Db.mail);
        ADDRESSTextField.setText(Db.add);
        PASSWORDTextField.setText(Db.pass);

        ADDRESSTextField.setEditable(false);
        NAMETextField.setEditable(false);
        DOBTextField.setEditable(false);
        EMAILTextField.setEditable(false);
        MOBILETextField.setEditable(false);
        PASSWORDTextField.setEditable(false);

        EDITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (EDITButton.getText().equals("EDIT")) {
                    ADDRESSTextField.setEditable(true);
                    NAMETextField.setEditable(true);
                    DOBTextField.setEditable(true);
                    EMAILTextField.setEditable(true);
                    MOBILETextField.setEditable(true);
                    PASSWORDTextField.setEditable(true);
                    NAMETextField.requestFocus();
                    EDITButton.setText("UPDATE");
                } else {
                    Db.databaseConnectionM("UPDATE `users` SET `name` = '"+NAMETextField.getText()+"', `mobile` = '"+MOBILETextField.getText()+"', `password` = '"+PASSWORDTextField.getText()+"', `address` = '"+ADDRESSTextField.getText()+"' WHERE `users`.`id` = "+uId+";");
                    if (Db.b){
                        JOptionPane.showMessageDialog(viewEditPnl,"Details Updated","Successful",JOptionPane.INFORMATION_MESSAGE);
                        ADDRESSTextField.setEditable(false);
                        NAMETextField.setEditable(false);
                        DOBTextField.setEditable(false);
                        EMAILTextField.setEditable(false);
                        MOBILETextField.setEditable(false);
                        PASSWORDTextField.setEditable(false);
                        EDITButton.setText("EDIT");
                    }
                }
            }
        });
        CANCELButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ADDRESSTextField.setEditable(false);
                NAMETextField.setEditable(false);
                DOBTextField.setEditable(false);
                EMAILTextField.setEditable(false);
                MOBILETextField.setEditable(false);
                PASSWORDTextField.setEditable(false);
                NAMETextField.setText(Db.name);
                DOBTextField.setText(Db.dob);
                MOBILETextField.setText(Db.mobile);
                EMAILTextField.setText(Db.mail);
                ADDRESSTextField.setText(Db.add);
                PASSWORDTextField.setText(Db.pass);
                Db.databaseConnectionM("UPDATE `users` SET `name` = '"+NAMETextField.getText()+"', `mobile` = '"+MOBILETextField.getText()+"', `password` = '"+PASSWORDTextField.getText()+"', `address` = '"+ADDRESSTextField.getText()+"' WHERE `users`.`id` = "+uId+";");
                EDITButton.setText("EDIT");
                if (Db.b){
                    JOptionPane.showMessageDialog(viewEditPnl,"Details Update Canceled ","Successful",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}
