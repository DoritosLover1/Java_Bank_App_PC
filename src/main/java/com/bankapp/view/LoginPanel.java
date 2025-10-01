package main.java.com.bankapp.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import main.java.com.bankapp.database.ConnectionDB;
import main.java.com.bankapp.encryptionfunc.PasswordEncyrption;
import main.java.com.bankapp.computerinfos.ComputerInformations;

public class LoginPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private JPasswordField passwordField;
    private JTextField tcInput;
    private MainFrame parentFrame;
    
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(44, 62, 80);
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color ACCENT_COLOR = new Color(26, 188, 156);
    
    public LoginPanel(MainFrame parentFrame) {
        setMinimumSize(new Dimension(800, 600));
        this.parentFrame = parentFrame;
        setBackground(BACKGROUND_COLOR);
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BACKGROUND_COLOR);
        mainContainer.setBorder(new EmptyBorder(40, 40, 120, 40));
        
        JPanel infoPanel = createInfoPanel();
        
        JPanel loginPanel = createLoginPanel();
        
        mainContainer.add(infoPanel, BorderLayout.NORTH);
        mainContainer.add(loginPanel, BorderLayout.CENTER);
        
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1, true),
            "Computer Informations",
            0, 0, new Font("Calibri", Font.BOLD, 14), SECONDARY_COLOR
        ));
        
        String[] infos = {
            "Computer Name: " + ComputerInformations.getComputerName(),
            "MAC Address: " + ComputerInformations.getComputerMAC(),
            "Local IP: " + ComputerInformations.getComputerIP(),
            "Real IP: " + ComputerInformations.getExternalIP()
        };
        
        for (String info : infos) {
            JLabel label = new JLabel(info);
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            label.setForeground(TEXT_COLOR);
            panel.add(label);
        }
        
        return panel;
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel titleLabel = new JLabel("Login Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        titleLabel.setForeground(SECONDARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel tcLabel = new JLabel("TC:");
        tcLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
        tcLabel.setForeground(TEXT_COLOR);
        panel.add(tcLabel, gbc);
        
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 0);
        tcInput = createStyledTextField();
        panel.add(tcInput, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(10, 0, 5, 0);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        panel.add(passwordLabel, gbc);
        
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 15, 0);
        passwordField = createStyledPasswordField();
        panel.add(passwordField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton loginButton = createStyledButton("Sign In", PRIMARY_COLOR);
        loginButton.addActionListener(e -> handleLogin());
        
        JButton signupButton = createStyledButton("Sign Up", ACCENT_COLOR);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(250, 35));
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(250, 35));
        return field;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Calibri", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void handleLogin() {
        String tc = tcInput.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (tc.isEmpty() || password.isEmpty()) {
            showStyledMessage("Please, type TC and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String hashedPassword = PasswordEncyrption.hashFunction(password);
        System.out.println("Attempting login with Username: " + tc + " and Hashed Password: " + hashedPassword);
        
        try {
            boolean loginSuccess = ConnectionDB.login(tc, password);
            
            if (loginSuccess) {
                showStyledMessage("Successfully accessed", "Successful", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Login successful, switching to dashboard...");

                parentFrame.showPanel("DASHBOARD");

                tcInput.setText("");
                passwordField.setText("");
                
            } else {
                showStyledMessage("Either invalid TC or password.", "Failed Log In", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.err.println("Error during login: " + ex.getMessage());
            ex.printStackTrace();
            showStyledMessage("An error occurred during login. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.messageFont", new Font("Calibri", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}