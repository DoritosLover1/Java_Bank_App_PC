// view/LoadingDialog.java
package main.java.com.bankapp.view;

import javax.swing.*;
import java.awt.*;

public class LoadingDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
    private JLabel statusLabel;
    private JProgressBar progressBar;

    public LoadingDialog(JFrame parent) {
        super(parent, "Connecting to Database", true);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(350, 120);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statusLabel = new JLabel("Initializing connection...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(statusLabel);

        panel.add(Box.createVerticalStrut(10));

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Connecting...");
        panel.add(progressBar);

        add(panel);
    }

    public void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
            progressBar.setString(status);
        });
    }

    public void showLoading() {
        setVisible(true);
    }

    public void hideLoading() {
        setVisible(false);
        dispose();
    }
}