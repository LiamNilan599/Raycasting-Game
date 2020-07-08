import com.sun.deploy.panel.RuleSetViewerDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu implements ActionListener
{
    public static JFrame frame;
    private JPanel panel;
    private JButton buttonLevel1;
    private JButton buttonLevel2;
    public int Level;
    private Game game;

    public Menu()
    {
        frame = new JFrame();
        frame.setSize(100,300);frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buttonLevel1 = new JButton("Level 1");
        buttonLevel1.addActionListener(this);
        buttonLevel2 = new JButton("Level 2");
        buttonLevel2.addActionListener(this);
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,1));
        panel.add(buttonLevel1);
        panel.add(buttonLevel2);
        frame.add(panel,BorderLayout.CENTER);
        frame.setVisible(true);
    }
    public static void main(String [] args)
    {
        new Menu();
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        frame.setVisible(false);
        if (e.getSource() == buttonLevel1)
        {
            Level = 1;
            game = new Game(Level);
        }
        if (e.getSource() == buttonLevel2)
        {
            Level = 2;
            game = new Game(Level);
        }
    }
}
