package main.java.com.bankapp.view;
import main.java.com.bankapp.database.ConnectionDB;
import main.java.com.bankapp.database.Person;
import main.java.com.bankapp.utils.DashboardFunc;
import main.java.com.bankapp.utils.SideMenuFunc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;


public class DashboardPanel extends JPanel {
	
    private static final long serialVersionUID = 1L;
    private JLayeredPane layeredPane;

    // Using but not directly just for reference
	private MainFrame parentFrame;
 	private JPanel menuPanel;
    private AtomicBoolean isMenuVisible = new AtomicBoolean(false);
    private JButton menuButton;
    private Person person = ConnectionDB.getPerson();

    public DashboardPanel(MainFrame parentFrame) {
        setBackground(new Color(255, 255, 255));
        setMinimumSize(new Dimension(800, 600));
        this.parentFrame = parentFrame;
        initializeComponents();
    }

    private void initializeComponents() {
    	setLayout(new BorderLayout());
    	
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.setOpaque(true);
        layeredPane.setVisible(true);
        
        add(layeredPane, BorderLayout.CENTER);
    	
        final int sliderSize = parentFrame.getHeight() / 4;
        final int sliderHeight = parentFrame.getHeight();
     
        JLabel displayName = new JLabel("Welcome, "+ person);
        displayName.setFont(new Font("Calibri", Font.PLAIN, 14));
        displayName.setBounds(570, 10, 230, 20);
        layeredPane.add(displayName, JLayeredPane.DEFAULT_LAYER);

        menuButton = new JButton();
        menuButton.putClientProperty("JButton.buttonType", "roundRect");
        menuButton.putClientProperty("JButton.arc", 8);
        menuButton.setBounds(5, 0, 30, 30);
        menuButton.setFont(new Font("Calibri", Font.BOLD, 30));
        menuButton.setForeground(Color.BLACK);
        menuButton.setFocusPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        menuButton.setOpaque(false);
        menuButton.setText("...");
        menuButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuButton.setForeground(Color.GRAY);
                menuButton.setBackground(new Color(240, 240, 240));
                menuButton.setContentAreaFilled(true);
                menuButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuButton.setForeground(Color.BLACK);
                menuButton.setContentAreaFilled(false);
                menuButton.repaint();
            }
        });

        SwingUtilities.invokeLater(() -> {
            menuButton.revalidate();
            menuButton.repaint();
        });

        menuPanel = DashboardFunc.createMenuPanel(menuButton, isMenuVisible, sliderSize, sliderHeight, parentFrame, new Color(45, 52, 54), new Color(57, 62, 70));

        if (menuPanel != null) {
            menuButton.addActionListener(e -> {
                SideMenuFunc.toggleMenu(menuButton, menuPanel, isMenuVisible);
            });

            layeredPane.add(menuButton, JLayeredPane.PALETTE_LAYER);
            layeredPane.add(menuPanel, JLayeredPane.MODAL_LAYER);
        } else {
            System.err.println("menuPanel hasn't been initialized properly.");
        }
        
        UserDashboardPanel testPanel = new UserDashboardPanel();
        testPanel.setBounds(70, 50, 400, 300);
        layeredPane.add(testPanel, JLayeredPane.DEFAULT_LAYER);
        
        ExchangeGraphPanel graphPanel = new ExchangeGraphPanel(null);
        graphPanel.setBounds(400, 50, 400, 300);
        layeredPane.add(graphPanel, JLayeredPane.DEFAULT_LAYER);
        
        UserLatestTransactionPanel userTransactionPanel = new UserLatestTransactionPanel();
        userTransactionPanel.setBounds(70, 350, 730, 260);
        layeredPane.add(userTransactionPanel, JLayeredPane.DEFAULT_LAYER);
    }
}




