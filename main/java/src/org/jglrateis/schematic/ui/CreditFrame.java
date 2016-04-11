package org.jglrateis.schematic.ui;

import static org.jglrateis.schematic.util.Translation.LANG;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CreditFrame extends JFrame
{
    private static final long serialVersionUID = 1L;
    
    public CreditFrame()
    {
        this.setTitle(LANG.getTranslation("credits"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(400, 200);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        
        URL iconURL = ClassLoader.getSystemResource("resources/img/icon.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image icon = kit.createImage(iconURL);
        this.setIconImage(icon);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JButton sponsorButton = new JButton("Minecraft Forge France");
        sponsorButton.setAlignmentX(CENTER_ALIGNMENT);
        sponsorButton.setAlignmentY(CENTER_ALIGNMENT);
        sponsorButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("http://www.minecraftforgefrance.fr"));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(CreditFrame.this, String.format(LANG.getTranslation("errorOpeningURL"), "http://www.minecraftforgefrance.fr"), LANG.getTranslation("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton xavTwitterButton = new JButton("@jglrxavpok");
        try
        {
            Image img = ImageIO.read(getClass().getResourceAsStream("/resources/img/favicon.png"));
            xavTwitterButton.setIcon(new ImageIcon(img));
        }
        catch(IOException ex)
        {}
        
        xavTwitterButton.setAlignmentX(CENTER_ALIGNMENT);
        xavTwitterButton.setAlignmentY(CENTER_ALIGNMENT);
        xavTwitterButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("http://www.twitter.com/jglrxavpok"));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(CreditFrame.this, String.format(LANG.getTranslation("errorOpeningURL"), "http://www.twitter.com/jglrxavpok"), LANG.getTranslation("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton amaTwitterButton = new JButton("@ZeAmateis");
        try
        {
            Image img = ImageIO.read(getClass().getResourceAsStream("/resources/img/favicon.png"));
            amaTwitterButton.setIcon(new ImageIcon(img));
        }
        catch(IOException ex)
        {}
        
        amaTwitterButton.setAlignmentX(CENTER_ALIGNMENT);
        amaTwitterButton.setAlignmentY(CENTER_ALIGNMENT);
        amaTwitterButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("http://www.twitter.com/ZeAmateis"));
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(CreditFrame.this, String.format(LANG.getTranslation("errorOpeningURL"), "http://www.twitter.com/ZeAmateis"), LANG.getTranslation("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JPanel sponsorPanel = new JPanel();
        sponsorPanel.setLayout(new BoxLayout(sponsorPanel, BoxLayout.X_AXIS));
        sponsorPanel.setAlignmentX(CENTER_ALIGNMENT);
        sponsorPanel.setAlignmentY(CENTER_ALIGNMENT);
        sponsorPanel.add(sponsorButton);
        sponsorPanel.add(xavTwitterButton);
        sponsorPanel.add(amaTwitterButton);
        
        JPanel twitterPanel = new JPanel();
        twitterPanel.setLayout(new BoxLayout(twitterPanel, BoxLayout.X_AXIS));
        twitterPanel.setAlignmentX(CENTER_ALIGNMENT);
        twitterPanel.setAlignmentY(CENTER_ALIGNMENT);
        twitterPanel.add(xavTwitterButton);
        twitterPanel.add(amaTwitterButton);
        
        JLabel text = new JLabel();
        String creditText = LANG.getTranslation("creditsText");
        text.setText(creditText);
        text.setAlignmentX(CENTER_ALIGNMENT);
        text.setAlignmentY(CENTER_ALIGNMENT);
        
        panel.add(text);
        panel.add(sponsorPanel);
        panel.add(twitterPanel);
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }
}