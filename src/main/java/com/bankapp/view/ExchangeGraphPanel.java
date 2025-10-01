package main.java.com.bankapp.view;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.com.bankapp.APIservices.ExchangeService;

public class ExchangeGraphPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private List<Double> usdEurList;
    
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_LIGHT = new Color(108, 117, 125);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    public ExchangeGraphPanel(Color backgroundColor) {
        this.usdEurList = ExchangeService.getUSDandEURInformations();
        setBackground(backgroundColor);
        setupPanel();
    }
    
    private void setupPanel() {
        setPreferredSize(new Dimension(200, 100));
        setBackground(null);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        drawExchangeCard(g2d);
        g2d.dispose();
    }
    
    private void drawExchangeCard(Graphics2D g2d) {
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
        String title = "DÖVİZ KURLARI";
        int titleX = cardX + (cardWidth - titleMetrics.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, cardY + 35);

        g2d.setColor(PRIMARY_COLOR);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(titleX, cardY + 42, titleX + titleMetrics.stringWidth(title), cardY + 42);

        g2d.setColor(TEXT_LIGHT);
        g2d.setFont(new Font("Calibri", Font.BOLD, 12));
        g2d.drawString("PARA BİRİMİ", cardX + 30, cardY + 70);
        g2d.drawString("ALIŞ", cardX + 150, cardY + 70);
        g2d.drawString("SATIŞ", cardX + 220, cardY + 70);

        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawLine(cardX + 20, cardY + 80, cardX + cardWidth - 20, cardY + 80);

        String[] currencyNames = {"USD", "EUR"};
        String[] currencySymbols = {"$", "€"};
        
        for (int i = 0; i < Math.min(usdEurList.size(), 2); i++) {
            int yPos = cardY + 110 + i * 35;
 
            g2d.setColor(i == 0 ? SUCCESS_COLOR : PRIMARY_COLOR);
            g2d.fillOval(cardX + 30, yPos - 12, 20, 20);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Calibri", Font.BOLD, 12));
            FontMetrics symbolMetrics = g2d.getFontMetrics();
            String symbol = currencySymbols[i];
            int symbolX = cardX + 40 - symbolMetrics.stringWidth(symbol) / 2;
            g2d.drawString(symbol, symbolX, yPos + 3);
            
            g2d.setColor(TEXT_DARK);
            g2d.setFont(new Font("Calibri", Font.BOLD, 14));
            g2d.drawString(currencyNames[i], cardX + 60, yPos + 5);
            
            double rate = usdEurList.get(i);
            double buyRate = rate * 0.998;
            double sellRate = rate * 1.002;
            
            g2d.setFont(new Font("Calibri", Font.PLAIN, 13));
            g2d.setColor(SUCCESS_COLOR);
            g2d.drawString(String.format("%.4f", buyRate), cardX + 150, yPos + 5);
            
            g2d.setColor(new Color(220, 53, 69));
            g2d.drawString(String.format("%.4f", sellRate), cardX + 220, yPos + 5);
        }
        
        g2d.setColor(TEXT_LIGHT);
        g2d.setFont(new Font("Calibri", Font.ITALIC, 10));
        String updateTime = "Son güncelleme: " + java.time.LocalTime.now().toString().substring(0, 5);
        FontMetrics updateMetrics = g2d.getFontMetrics();
        g2d.drawString(updateTime, cardX + cardWidth - updateMetrics.stringWidth(updateTime) - 20, cardY + 210);
    }
}