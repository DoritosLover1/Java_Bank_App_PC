package main.java.com.bankapp.view;

import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import main.java.com.bankapp.database.ConnectionDB;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    MainFrame frame = new MainFrame();
                    frame.setLocationRelativeTo(null);
                    frame.setTitle("Banking System Application DoritosLover1");

                    LoadingDialog loadingDialog = new LoadingDialog(frame);

                    ConnectionDB.connectWithCallback(new ConnectionDB.DetailedConnectionCallback() {
                        @Override
                        public void onStatusUpdate(String status) {
                            System.out.println("Database Status: " + status);
                            loadingDialog.updateStatus(status);
                        }

                        @Override
                        public void onConnectionResult(boolean success) {
                            System.out.println("Connection callback triggered. Success: " + success);
                            loadingDialog.hideLoading();

                            if (success) {
                                System.out.println("Connection successful!");
                                frame.initUI();
                                frame.setVisible(true);
                            } else {
                                System.err.println("Connection failed!");
                                JOptionPane.showMessageDialog(frame,
                                    "Failed to connect to the database. Please check your connection settings.",
                                    "Connection Error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                        }
                    });

                    System.out.println("Starting database connection...");
                    loadingDialog.showLoading();
                    
                } catch (Exception e) {
                    System.err.println("Error in main method: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public MainFrame() {
        System.out.println("MainFrame constructor started...");
        
        setMinimumSize(new Dimension(800, 600));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        System.out.println("MainFrame constructor completed.");
    }

    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        System.out.println("Creating LoginPanel...");
        LoginPanel loginPanel = new LoginPanel(this);
        loginPanel.setMinimumSize(new Dimension(800, 600));
        mainPanel.add(loginPanel, "LOGIN");

        System.out.println("Creating DashboardPanel...");
        try {
            DashboardPanel dashboardPanel = new DashboardPanel(this);
            dashboardPanel.setMinimumSize(new Dimension(800, 600));
            mainPanel.add(dashboardPanel, "DASHBOARD");
            System.out.println("DashboardPanel created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating DashboardPanel: " + e.getMessage());
            e.printStackTrace();
        }

        showPanel("LOGIN");
        System.out.println("Showing LOGIN panel...");

        setContentPane(mainPanel);
    }

    public static void showPanel(String panelName) {
        System.out.println("Switching to panel: " + panelName);
        cardLayout.show(mainPanel, panelName);
    }

}
