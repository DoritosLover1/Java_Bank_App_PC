package main.java.com.bankapp.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import main.java.com.bankapp.database.ConnectionDB;
import main.java.com.bankapp.database.Transactions;

public class UserLatestTransactionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color CARD_COLOR = Color.white;
    private static final Color TEXT_LIGHT = new Color(108, 117, 125);
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);
    
    private JList<String> transactionList;
    private DefaultListModel<String> listModel;
    private ScheduledExecutorService scheduler;

    public UserLatestTransactionPanel() {
        setupPanel();
        createTransactionList();
        loadTransactions();
        startAutoRefresh();
    }

    private void setupPanel() {
        setBackground(null);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);
    }
    
    private void createTransactionList() {
    	try {
            listModel = new DefaultListModel<>();
            
            transactionList = new JList<>(listModel);
            transactionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            transactionList.setFont(new Font("Calibri", Font.PLAIN, 14));

            transactionList.setCellRenderer(new TransactionCellRenderer());

            JScrollPane scrollPane = new JScrollPane(transactionList);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            
            add(scrollPane);
    	} catch (Exception e) {
    		System.out.print(e.getMessage());
    	}
    }
    
    private void loadTransactions() {
        ArrayList<Transactions> transactions = ConnectionDB.getTransaction();
        
        listModel.clear();
        
        if (transactions != null && !transactions.isEmpty()) {
            for(Transactions transaction : transactions) {
                listModel.addElement(transaction.transaction_type() + " - " + 
                                   transaction.amount() + " - " + 
                                   transaction.timestamp());
            }
            repaint();
        } else {
            listModel.addElement("Wait a second! Your transactions've been coming");
        }
    }
    
    private void startAutoRefresh() {
        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {
                refreshTransactions();
                System.out.println("Transactions refreshed: " + new java.util.Date());
            });
        }, 0, 30, TimeUnit.SECONDS);
    }
    
    public void refreshTransactions() {
            loadTransactions();
            repaint();

    }

    public void stopAutoRefresh() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
    
    @Override
    public void doLayout() {
        super.doLayout();
        Component scrollPane = getComponent(0);
        if (scrollPane != null) {
            int cardWidth = getWidth() - 100;
            int cardHeight = getHeight() - 60;
            int cardX = 70;
            int cardY = 0;
            
            scrollPane.setBounds(cardX + 20, cardY + 60, cardWidth - 40, cardHeight - 80);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawUserLatestTransactionCard(g2d);

        g2d.dispose();
    }

    private void drawUserLatestTransactionCard(Graphics2D g2d) {
        int cardWidth = getWidth() - 100;
        int cardHeight = getHeight() - 60;
        int cardX = 70;
        int cardY = 0;

        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fill(new RoundRectangle2D.Float(cardX + 2, cardY + 2, cardWidth, cardHeight, 12, 12));

        g2d.setColor(CARD_COLOR);
        g2d.fill(new RoundRectangle2D.Float(cardX, cardY, cardWidth, cardHeight, 12, 12));

        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1f));
        g2d.draw(new RoundRectangle2D.Float(cardX, cardY, cardWidth, cardHeight, 12, 12));

        drawContent(g2d, cardX, cardY, cardWidth);
    }

    private void drawContent(Graphics2D g2d, int cardX, int cardY, int cardWidth) {
        g2d.setColor(TEXT_DARK);
        g2d.setFont(new Font("Calibri", Font.BOLD, 18));
        FontMetrics titleMetrics = g2d.getFontMetrics();
        String title = "LATEST TRANSACTIONS";
        int titleX = cardX + (cardWidth - titleMetrics.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, cardY + 30);

        g2d.setColor(PRIMARY_COLOR);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(titleX, cardY + 35, titleX + titleMetrics.stringWidth(title), cardY + 35);
    }

    private class TransactionCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 3567724742944543808L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (isSelected) {
                label.setBackground(PRIMARY_COLOR);
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(Color.WHITE);
                label.setForeground(TEXT_DARK);
            }
            
            label.setFont(new Font("Calibri", Font.PLAIN, 13));
            label.setBorder(new EmptyBorder(8, 12, 8, 12));
            
            return label;
        }
    }

    public void addTransaction(String transaction) {
        listModel.addElement(transaction);
    }

    public void clearTransactions() {
        listModel.clear();
    }

    public String getSelectedTransaction() {
        return transactionList.getSelectedValue();
    }
}