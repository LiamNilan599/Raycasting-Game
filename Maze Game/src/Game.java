import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Graphics;

public class Game extends JFrame implements Runnable
{
    private static final long serialVersionUID = 1L;
    public int map_width = 15;
    public int map_height = 15;
    public int lvl;
    public boolean Win = false;
    public boolean on_square = false;
    public String NormText = "Find the red Wall";
    public String WinText = "you win";
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    public int[] pixels;
    public ArrayList<Texture> textures;
    public Camera camera;
    public Screen screen;
    public Timer_display timer;
    private Graphics g;
    public static int[][] mapL1 =
            {
                    {1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
                    {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
                    {1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
                    {1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
                    {1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
                    {1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
                    {1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
                    {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
                    {1,1,1,0,1,1,1,1,4,4,4,0,4,4,4},
                    {1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
                    {1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
                    {1,0,0,4,0,0,1,4,0,3,3,5,3,0,4},
                    {1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                    {1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
            };
    public static int[][] mapL2 =
            {
                    {3,3,3,3,3,3,3,3,2,2,2,2,2,2,2},
                    {3,0,0,0,3,0,0,0,2,0,0,0,0,0,2},
                    {3,0,3,3,3,3,3,0,0,0,2,2,2,0,2},
                    {1,0,0,0,0,0,3,0,2,0,0,0,2,0,2},
                    {1,0,3,3,3,0,3,0,2,2,2,0,2,2,2},
                    {1,0,3,0,0,0,0,0,1,0,0,0,1,0,1},
                    {1,0,3,3,0,3,3,0,0,0,1,1,1,0,1},
                    {1,0,0,0,0,0,1,0,4,0,0,0,0,0,1},
                    {1,1,1,1,1,0,1,1,4,4,4,0,4,4,4},
                    {1,0,0,0,1,0,0,0,0,4,4,4,0,4,4},
                    {1,0,1,0,0,0,1,1,0,0,0,0,0,0,3},
                    {1,0,1,1,1,1,1,1,4,0,3,0,3,0,3},
                    {1,0,1,1,1,0,0,0,4,0,3,3,3,0,3},
                    {1,0,0,0,0,0,4,0,4,0,0,0,0,0,3},
                    {1,1,1,1,1,1,4,5,4,3,3,3,3,3,3}
            };
    public Game(int lvl)
    {
        this.lvl = lvl;
        if (lvl == 1)
        {
            thread = new Thread(this);
            image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
            pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
            textures = new ArrayList<Texture>();
            textures.add(Texture.wood);
            textures.add(Texture.brick);
            textures.add(Texture.bluestone);
            textures.add(Texture.stone);
            textures.add(Texture.maze_end);
            camera = new Camera(13.5, 2.5, 1, 0, 0, -.66);
            screen = new Screen(mapL1, map_width, map_height, textures, 640, 480);
            timer = new Timer_display();
            addKeyListener(camera);
            setSize(640, 480);
            setResizable(false);
            setTitle("Ray Casting Demo Level 1");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBackground(Color.black);
            setLocationRelativeTo(null);
            setVisible(true);
            start();
        }
        if(lvl == 2)
        {
            thread = new Thread(this);
            image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
            pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
            textures = new ArrayList<Texture>();
            textures.add(Texture.wood);
            textures.add(Texture.brick);
            textures.add(Texture.bluestone);
            textures.add(Texture.stone);
            textures.add(Texture.maze_end);
            camera = new Camera(1.5, 1.5, 1, 0, 0, -.66);
            screen = new Screen(mapL2, map_width, map_height, textures, 640, 480);
            timer = new Timer_display();
            addKeyListener(camera);
            setSize(640, 480);
            setResizable(false);
            setTitle("Ray Casting Demo Level 2");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBackground(Color.black);
            setLocationRelativeTo(null);
            setVisible(true);
            start();
        }
    }
    private synchronized void start()
    {
        running = true;
        thread.start();
    }
    public synchronized void stop()
    {
        running = false;
        try
        {
            thread.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 200, 60);
        g.fillRect(0, 440, 150, 40);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial Black", Font.BOLD, 20));
        g.drawString(timer.toString(),55,465);
        if(Win)
        {
            g.drawString(WinText, 50, 50);
        }
        else
        {
            g.drawString(NormText,1, 50);
        }
        g.dispose();
        bs.show();
    }
    public void run()
    {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;//60 times per second
        double delta = 0;
        requestFocus();
        while(running) {
            long now = System.nanoTime();
            delta = delta + ((now-lastTime) / ns);
            lastTime = now;
            while (delta >= 1)//Make sure update is only happening 60 times a second
            {
                //handles all of the logic restricted time
                screen.update(camera, pixels);
                if(lvl == 1)
                {
                    camera.update(mapL1);
                }
                else if(lvl == 2)
                {
                    camera.update(mapL2);
                }
                delta--;
            }
            if( lvl == 1 && camera.xPos >= 10 && camera.xPos < 11 && camera.yPos >= 11 && camera.yPos < 12)
            {
                Win = true;
                on_square = true;
                timer.Stop();

            }
            else if( lvl == 2 && camera.xPos >= 13 && camera.xPos < 14 && camera.yPos >= 7 && camera.yPos < 8)
            {
                Win = true;
                on_square = true;
                timer.Stop();
            }
            else
            {
                on_square = false;
            }
            if(Win == true && on_square == false)
            {
                Menu.frame.setVisible(true);
                setVisible(false);
                stop();
            }
            render();//displays to the screen unrestricted time
        }
    }
}