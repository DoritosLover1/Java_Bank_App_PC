package main.java.com.bankapp.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import main.java.com.bankapp.database.ConnectionDB;
import main.java.com.bankapp.database.Transactions;

public final class DashboardFunc {
	
	//Compact and reusable menu button creation function
	final public static JLabel createMenuButton(String text, int sliderSize) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Calibri", Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(new EmptyBorder(sliderSize/50, sliderSize/10, sliderSize/50, sliderSize/10));
        label.setOpaque(true);
        label.setBackground(new Color(45, 52, 54));
        return label;
    }
   
	
	final public static JPanel createMenuPanel(JButton menuButton, AtomicBoolean isMenuVisible, int sliderSize, int sliderHeight, JFrame parentFrame, Color MENU_BG_COLOR, Color HOVER_COLOR ) {

	    Color MENU_BG_COLOR1 = MENU_BG_COLOR;
	    Color HOVER_COLOR1 = HOVER_COLOR;
	
	    final JPanel menuPanel = new JPanel();
	    menuPanel.setBackground(MENU_BG_COLOR1);
	    menuPanel.setBounds(-sliderSize, 0, sliderSize, sliderHeight);
	    menuPanel.setLayout(new java.awt.GridLayout(6, 1,0, 10));

	    MouseAdapter createHoverEffect = new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            JLabel source = (JLabel) e.getSource();
	            source.setBackground(HOVER_COLOR1);
	            source.setOpaque(true);
	        }
	        
	        @Override
	        public void mouseExited(MouseEvent e) {
	            JLabel source = (JLabel) e.getSource();
	            source.setBackground(MENU_BG_COLOR1);
	            source.setOpaque(true);
	        }
	    };
	    
	    JLabel backButton = DashboardFunc.createMenuButton("Back", sliderSize);
	    backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    backButton.setIcon(new ImageIcon(DashboardFunc.class.getResource("/icons/SlideMenuIcons/back.png")));
	    backButton.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            SideMenuFunc.toggleMenu(menuButton, menuPanel, isMenuVisible);
	        }
	        
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            createHoverEffect.mouseEntered(e);
	        }
	        
	        @Override
	        public void mouseExited(MouseEvent e) {
	            createHoverEffect.mouseExited(e);
	        }
	    });

	    JLabel informationButton = DashboardFunc.createMenuButton("Information", sliderSize);
	    informationButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    informationButton.setIcon(new ImageIcon(DashboardFunc.class.getResource("/icons/SlideMenuIcons/person.png")));
	    informationButton.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            String infoMessage = "Banking System Application\n" +
	                               "Version 1.0\n" +
	                               "Developed by DoritosLover1";
	            JOptionPane.showMessageDialog(parentFrame, infoMessage, 
	                                        "Application Information", 
	                                        JOptionPane.INFORMATION_MESSAGE);
	        }
	        
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            createHoverEffect.mouseEntered(e);
	        }
	        
	        @Override
	        public void mouseExited(MouseEvent e) {
	            createHoverEffect.mouseExited(e);
	        }
	    });

	    JLabel settingsButton = DashboardFunc.createMenuButton("Settings", sliderSize);
	    settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    settingsButton.setIcon(new ImageIcon(DashboardFunc.class.getResource("/icons/SlideMenuIcons/setting.png")));
	    settingsButton.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            // Settings functionality burada implement edilecek
	            JOptionPane.showMessageDialog(parentFrame, 
	                                        "Settings panel coming soon!", 
	                                        "Settings", 
	                                        JOptionPane.INFORMATION_MESSAGE);
	        }
	        
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            createHoverEffect.mouseEntered(e);
	        }
	        
	        @Override
	        public void mouseExited(MouseEvent e) {
	            createHoverEffect.mouseExited(e);
	        }
	    });

	    JLabel helpButton = DashboardFunc.createMenuButton("Help", sliderSize);
	    helpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    helpButton.setIcon(new ImageIcon(DashboardFunc.class.getResource("/icons/SlideMenuIcons/help.png")));
	    helpButton.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            String helpMessage = "Customer Support Number: +XXXXXXXXXXXXXXXX\n" +
	                                "Customer Support Email: customer.support@doritoslover1.com\n" +
	                                "Working Hours: 09:00 - 18:00 (Mon-Fri)";
	            JOptionPane.showMessageDialog(parentFrame, helpMessage, 
	                                        "Customer Support", 
	                                        JOptionPane.QUESTION_MESSAGE);
	        }
	        
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            createHoverEffect.mouseEntered(e);
	        }
	        
	        @Override
	        public void mouseExited(MouseEvent e) {
	            createHoverEffect.mouseExited(e);
	        }
	    });

	    JLabel logoutButton = DashboardFunc.createMenuButton("Logout", sliderSize);
	    logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    logoutButton.setIcon(new ImageIcon(DashboardFunc.class.getResource("/icons/SlideMenuIcons/logout.png")));
	    logoutButton.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            try {
					SideMenuFunc.handleMenuItemClick(parentFrame, menuButton, menuPanel, isMenuVisible);
					
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	        }
	        
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            createHoverEffect.mouseEntered(e);
	        }
	        
	        @Override
	        public void mouseExited(MouseEvent e) {
	            createHoverEffect.mouseExited(e);
	        }
	    });

	    menuPanel.add(backButton);
	    menuPanel.add(informationButton);
	    menuPanel.add(settingsButton);
	    menuPanel.add(helpButton);
	    menuPanel.add(logoutButton);
	    
	    return menuPanel;
	}
	
   // DEPRICATED - Use createMenuButton and add listeners where needed
   /*
   final public static JLabel createClickableMenuButton(String text) {
        JLabel label = createMenuButton(text);
        label.setIcon(null);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(new Color(60, 70, 75));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(new Color(45, 52, 54));
            }
        });
        
        return label;
    }
    */
}
