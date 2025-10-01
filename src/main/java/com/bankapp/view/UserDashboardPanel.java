package main.java.com.bankapp.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class UserDashboardPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color CARD_COLOR = Color.white;
    private static final Color TEXT_LIGHT = new Color(108, 117, 125);
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    private int mouseX = 0, mouseY = 0;
    private boolean showTooltip = false;
    private String tooltipText = "";
    
    // BURAYA VERİ TABANI VERİLERİ GELECEK.
    private double moneyPercentageL = 25000.0;
    private double debtPercentageL = 15000.0;
    private double creditPercentageL = 10000.0;
    private int circleX, circleY, circleSize = 120;

    public UserDashboardPanel() {
        setupPanel();
        setupMouseListeners();
    }

    private void setupPanel() {
        setPreferredSize(new Dimension(200, 100));
        setBackground(null);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private void setupMouseListeners() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                checkTooltip();
                repaint();
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                showTooltip = false;
                repaint();
            }
        });
    }

    private void checkTooltip() {
        if (circleX > 0 && circleY > 0) {
            int centerX = circleX + circleSize / 2;
            int centerY = circleY + circleSize / 2;
            int innerRadius = (circleSize - 30) / 2;
            int outerRadius = circleSize / 2;
            
            double distance = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
            
            if (distance >= innerRadius && distance <= outerRadius) {
                double angle = Math.atan2(mouseY - centerY, mouseX - centerX);
                angle = Math.toDegrees(angle);
                if (angle < 0) angle += 360;

                angle = (angle + 90) % 360;
                
                double total = moneyPercentageL + debtPercentageL + creditPercentageL;
                double moneyPercentage = (moneyPercentageL / total) * 100;
                double debtPercentage = (debtPercentageL / total) * 100;
                
                if (angle <= moneyPercentage * 360 / 100) {
                    tooltipText = String.format("Para: %.0f TL (%.1f%%)", moneyPercentageL, moneyPercentage);
                    showTooltip = true;
                } else if (angle <= (moneyPercentage + debtPercentage) * 360 / 100) {
                    tooltipText = String.format("Kredi Borcu: %.0f TL (%.1f%%)", debtPercentageL, debtPercentage);
                    showTooltip = true;
                } else {
                    double creditPercentage = (creditPercentageL / total ) * 100;
                    tooltipText = String.format("Kredi: %.0f TL (%.1f%%)", creditPercentageL, creditPercentage);
                    showTooltip = true;
                }
            } else {
                showTooltip = false;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawDashboardCard(g2d);
        
        if (showTooltip) {
            drawTooltip(g2d);
        }
        
        g2d.dispose();
    }

    private void drawTooltip(Graphics2D g2d) {
        g2d.setFont(new Font("Calibri", Font.BOLD, 11));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(tooltipText);
        int textHeight = fm.getHeight();
        
        int tooltipX = mouseX + 10;
        int tooltipY = mouseY - 5;
        
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fill(new RoundRectangle2D.Float(tooltipX, tooltipY - textHeight, 
                                          textWidth + 10, textHeight + 5, 5, 5));

        g2d.setColor(Color.WHITE);
        g2d.drawString(tooltipText, tooltipX + 5, tooltipY - 5);
    }

    private void drawDashboardCard(Graphics2D g2d) {
        int cardWidth = getWidth() - 100;
        int cardHeight = getHeight() - 80;

        int cardX = 70;
        int cardY = 30;

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
        String title = "DASHBOARD";
        int titleX = cardX + (cardWidth - titleMetrics.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, cardY + 35);

        g2d.setColor(PRIMARY_COLOR);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(titleX, cardY + 42, titleX + titleMetrics.stringWidth(title), cardY + 42);
        
        g2d.setColor(PRIMARY_COLOR);
        g2d.setFont(new Font("Calibri", Font.BOLD, 12));
        g2d.drawString("KREDİ", cardX + 60, cardY + 70);

        g2d.setColor(SUCCESS_COLOR);
        g2d.setFont(new Font("Calibri", Font.BOLD, 12));
        g2d.drawString("BANKA", cardX + 130, cardY + 70);
        
        g2d.setColor(new Color(220, 53, 69));
        g2d.setFont(new Font("Calibri", Font.BOLD, 12));
        g2d.drawString("BORÇ", cardX + 200, cardY + 70);
        
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawLine(cardX + 20, cardY + 80, cardX + cardWidth - 20, cardY + 80);
        
        g2d.setColor(TEXT_LIGHT);
        g2d.setFont(new Font("Calibri", Font.ITALIC, 10));
        String updateTime = "Son güncelleme: " + java.time.LocalTime.now().toString().substring(0, 5);
        FontMetrics updateMetrics = g2d.getFontMetrics();
        g2d.drawString(updateTime, cardX + cardWidth - updateMetrics.stringWidth(updateTime) - 20, cardY + 210);

        drawFinancialChart(g2d, cardX, cardY, cardWidth);
    }

    private void drawFinancialChart(Graphics2D g2d, int cardX, int cardY, int cardWidth) {
        double worth = moneyPercentageL + debtPercentageL + creditPercentageL;
        
        double moneyPercentage = (moneyPercentageL / worth) * 100;
        double debtPercentage = (debtPercentageL / worth) * 100;
        double creditPercentage = (creditPercentageL / worth) * 100; 
        
        circleX = cardX + (cardWidth - circleSize) / 2;
        circleY = cardY + 90;
        
        Color moneyColor = SUCCESS_COLOR;
        Color debtColor = PRIMARY_COLOR;
        Color creditColor = new Color(220, 53, 69);
        
        double[] percentages = {moneyPercentage, debtPercentage, creditPercentage};
        Color[] colors = {moneyColor , debtColor, creditColor};
        
        int startAngle = -90;
        
        for (int i = 0; i < percentages.length; i++) {
            int arcAngle = (int) (percentages[i] * 360 / 100);
            
            g2d.setColor(colors[i]);
            g2d.fillArc(circleX, circleY, circleSize, circleSize, startAngle, arcAngle);
            
            startAngle += arcAngle;
        }
        
        int circle2XInside = circleX + 25;
        int circle2YInside = circleY + 25;
        int circle2SizeInside = circleSize - 50;
        
        g2d.setColor(CARD_COLOR);
        g2d.fillOval(circle2XInside, circle2YInside, circle2SizeInside, circle2SizeInside);
        
        g2d.setColor(TEXT_DARK);
        g2d.setFont(new Font("Calibri", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        
        String line1 = "Net Para";
        String line2 = String.format("%.0f TL", worth - debtPercentageL);

        int centerX = circleX + circleSize / 2;
        int centerY = circleY + circleSize / 2;
        
        int line1X = centerX - fm.stringWidth(line1) / 2;
        int line2X = centerX - fm.stringWidth(line2) / 2;

        g2d.drawString(line1, line1X, centerY - 5);
        g2d.drawString(line2, line2X, centerY + 10);
        
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(circleX, circleY, circleSize, circleSize);
    }
}