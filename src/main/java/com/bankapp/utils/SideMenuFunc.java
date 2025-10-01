package main.java.com.bankapp.utils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.com.bankapp.database.ConnectionDB;
import main.java.com.bankapp.view.MainFrame;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SideMenuFunc {
    
	private static void showMenu(JButton menuButton, JPanel menuPanel, AtomicBoolean isMenuVisible) {

        menuButton.setVisible(false);

        new Thread(() -> {
            for (int x = -200; x <= 0; x += 5) {
                menuPanel.setLocation(x, 0);
                try { Thread.sleep(5); } catch (InterruptedException ignored) {}
            }
            isMenuVisible.set(true);
        }).start();
    }

    private static void hideMenu(JButton menuButton, JPanel menuPanel, AtomicBoolean isMenuVisible) {
        new Thread(() -> {
            for (int x = 0; x >= -200; x -= 5) {
                menuPanel.setLocation(x, 0);
                try { Thread.sleep(5); } catch (InterruptedException ignored) {}
            }
            isMenuVisible.set(false);

            menuButton.setVisible(true);
        }).start();
    }
    
    final public static void toggleMenu(JButton menuButton, JPanel menuPanel, AtomicBoolean isMenuVisible) {
        if (!isMenuVisible.get()) {
            showMenu(menuButton, menuPanel, isMenuVisible);
        } else {
            hideMenu(menuButton, menuPanel, isMenuVisible);
        }
    }
    
	final public static void handleMenuItemClick(JFrame parentFrame, JButton menuButton, JPanel menuPanel, AtomicBoolean isMenuVisible) throws ClassNotFoundException, SQLException {
		int result = JOptionPane.showConfirmDialog(
	            parentFrame,
	            "Are you sure you want to logout?",
	            "Logout Confirmation",
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.QUESTION_MESSAGE
	            );
		if (result == JOptionPane.YES_OPTION) {
			try {
			    ConnectionDB.reConnect();
			    hideMenu(menuButton, menuPanel, isMenuVisible);
			    ((MainFrame) parentFrame).showPanel("LOGIN");
			} catch (Exception e) {
			    JOptionPane.showMessageDialog(parentFrame, 
			        "Unexpected error: " + e.getMessage(),
			        "Error", JOptionPane.ERROR_MESSAGE);
			    e.printStackTrace();
			}
	    }
	}
}