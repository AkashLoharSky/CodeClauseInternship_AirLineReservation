import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AirLineReservation extends JFrame {
    public Container container;
    public JMenuBar menuBar;
    public JMenu myAccount;
    public JMenu bookF = new JMenu();
    public JMenu fList = new JMenu();
    public JMenuItem i1 = new JMenuItem("Login");
    public JMenuItem i2 = new JMenuItem("Register");
    public JMenuItem i3 = new JMenuItem("View");
    public JMenuItem i4 = new JMenuItem("Logout");
    Component aC = new Component() {
    };
    loginForm l = new loginForm();
    registrationForm r = new registrationForm();

    public static JFrame frame;

    public void airLineReservationM(Component a) {
        aC = a;
        frame = this;
        setTitle("Sky Line");
        setSize(1000, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setIconImage(new ImageIcon("plane-png-flights-airlines-msp-airport-1.png").getImage());
        getContentPane();
        add(a);
        l.loginPanel.setVisible(true);
    }

    public void menuBar(boolean sM) {
        //menu bar
        menuBar = new JMenuBar();
        bookF = new JMenu("Book a Flight");
        fList = new JMenu("Flight Lists");

//      menuBar.add(myAccount);
        myAccount = new JMenu("My Account");
        myAccount.add(i1);
        myAccount.add(i2);
        myAccount.add(i3);
        myAccount.add(i4);
        i1.setVisible(!sM);
        i2.setVisible(!sM);
        i3.setVisible(sM);
        i4.setVisible(sM);
        bookF.setVisible(sM);
        menuBar.add(bookF);
        menuBar.add(fList);
        menuBar.add(myAccount);
        setJMenuBar(menuBar);
        fList.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                getContentPane().removeAll();
                getContentPane().add(new FlightList().flightlistpnl);
                revalidate();
                repaint();
                fList.setFocusable(false);
                UIManager.put("Button.disabledText", Color.GRAY);
                fList.setEnabled(false);
                bookF.setEnabled(true);
                myAccount.setEnabled(true);
                i1.setEnabled(true);
                i2.setEnabled(true);
                i3.setEnabled(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                fList.setSelected(false);
            }
            @Override
            public void menuCanceled(MenuEvent e) {

            }

        });

        //booking listener

        bookF.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                getContentPane().removeAll();
                getContentPane().add(new booking().bookingPanel);
                revalidate();
                repaint();
                fList.setFocusable(false);
                fList.setFocusPainted(false);
                bookF.setEnabled(false);
                fList.setEnabled(true);
                myAccount.setEnabled(true);
                i3.setEnabled(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        i2.addActionListener(e -> {
            getContentPane().removeAll();
            getContentPane().add(new registrationForm().registrationPanel);
            i1.setEnabled(true);
            i2.setEnabled(false);
            fList.setEnabled(true);
            revalidate();
            repaint();
        });
        i1.addActionListener(e -> {
            getContentPane().removeAll();
            getContentPane().add(new loginForm().loginPanel);
            l.userNameTextField.requestFocus();
            l.userNameTextField.grabFocus();
            i2.setEnabled(true);
            fList.setEnabled(true);
            i1.setEnabled(false);
            revalidate();
            repaint();
        });
        i3.addActionListener(e -> {
            getContentPane().removeAll();
            viewEditUser vE = new viewEditUser();
            getContentPane().add(vE.viewEditPnl);
            i4.setEnabled(true);
            i3.setEnabled(false);
            fList.setEnabled(true);
            bookF.setEnabled(true);
            revalidate();
            repaint();
        });

        i4.addActionListener(e -> {
            getContentPane().removeAll();
            loginForm lg = new loginForm();
            getContentPane().add(lg.loginPanel);
            lg.userNameTextField.requestFocus();
            lg.userNameTextField.grabFocus();
            i4.setEnabled(true);
            i3.setEnabled(false);
            fList.setEnabled(true);
            bookF.setEnabled(true);
            menuBar(false);
            revalidate();
            repaint();
        });


    }

    public static void main(String[] args) {
        AirLineReservation air = new AirLineReservation();
        loginForm l = new loginForm();
        l.userNameTextField.requestFocusInWindow();
        air.menuBar(false);
        air.airLineReservationM(l.loginPanel);
        air.i1.setEnabled(false);
        frame.revalidate();
        frame.repaint();
    }
}