import com.mpatric.mp3agic.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Queue;
import java.util.LinkedList;

public class Ocelot extends JFrame
{
    public static void main(String args[])
    {
        JFrame ocelot = new JFrame();
        ocelot.setTitle("Ocelot");
        ocelot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ocelot.add(new OcelotPanel());
        ocelot.pack();
        ocelot.setResizable(false);
        ocelot.setLocationRelativeTo(null);
        ocelot.setVisible(true);
    }
}

class OcelotPanel extends JPanel implements ActionListener
{
    private final int WINDOW_LENGTH = 700;
    private final int WINDOW_HEIGHT = 600;
    private final int BUTTON_LENGTH = 120;
    private final int BUTTON_HEIGHT = 50;
    private double time = 0.0d;

    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;

    public JLabel label1;
    public JLabel label2;
    public JLabel label3;

    private MediaPlayer player;
    private boolean is_playing = false;
    private Queue<String> queue;

    OcelotPanel()
    {
        queue = new LinkedList();
        add_graphics_and_widgets();
        new JFXPanel(); 
        
        Timer timer = new Timer(50, e->
        {
            time += 0.05;
            Color curr_color = getRainbowColor(0, 0);
            label1.setForeground(curr_color);
            label2.setForeground(curr_color);
            button5.setForeground(curr_color);
            button1.setForeground(curr_color);
            button2.setForeground(curr_color);
            button3.setForeground(curr_color);
            button4.setForeground(curr_color);
            setBackground(Color.BLACK);
        });
        timer.start();

        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_LENGTH, WINDOW_HEIGHT));
    }

    public void add_graphics_and_widgets()
    {
        button1 = new JButton("Restart");
        button2 = new JButton("Play");
        button3 = new JButton("Stop");
        button4 = new JButton("Next");
        button5 = new JButton("Add Song");

        button1.setBounds(44,  25, BUTTON_LENGTH, 20);
        button2.setBounds(208, 25, BUTTON_LENGTH, 20);
        button3.setBounds(372, 25, BUTTON_LENGTH, 20);
        button4.setBounds(536, 25, BUTTON_LENGTH, 20);
        button5.setBounds(290, 570, BUTTON_LENGTH, 20);

        button1.setBackground(Color.BLACK);
        button2.setBackground(Color.BLACK);
        button3.setBackground(Color.BLACK);
        button4.setBackground(Color.BLACK);
        button5.setBackground(Color.BLACK);

        button1.setBorderPainted(false);
        button2.setBorderPainted(false);
        button3.setBorderPainted(false);
        button4.setBorderPainted(false);
        button5.setBorderPainted(false);

        button1.setFocusPainted(false);
        button2.setFocusPainted(false);
        button3.setFocusPainted(false);
        button4.setFocusPainted(false);
        button5.setFocusPainted(false);

        button1.setFont(new Font("Arial", Font.ITALIC, 16));
        button2.setFont(new Font("Arial", Font.ITALIC, 16));
        button3.setFont(new Font("Arial", Font.ITALIC, 16));
        button4.setFont(new Font("Arial", Font.ITALIC, 16));
        button5.setFont(new Font("Arial", Font.ITALIC, 16));

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);

        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();

        label1.setVerticalAlignment(SwingConstants.CENTER);
        label2.setVerticalAlignment(SwingConstants.CENTER);
        label3.setVerticalAlignment(SwingConstants.CENTER);

        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label3.setHorizontalAlignment(SwingConstants.CENTER);

        label1.setBounds(0, 43, 700, 100);
        label2.setBounds(0, 70, 700, 100);
        label3.setBounds(0, 150, 700, 400);

        label1.setFont(new Font("Arial", Font.ITALIC, 30));
        label2.setFont(new Font("Arial", Font.ITALIC, 30));

        this.add(button1);
        this.add(button2);
        this.add(button3);
        this.add(button4);
        this.add(button5);
        this.add(label1);
        this.add(label2);
        this.add(label3);
    }

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if(source == button2)
        {
            if(!is_playing && player != null)
            {
                Platform.runLater(()->
                {
                    player.play();
                    is_playing = true;
                });
            }
            else if(!queue.isEmpty() && player == null)
            {
                String next = queue.poll();
                load_song(next);
            }
        }

        if(source == button3)
        {
            if(is_playing && player != null)
            {
                Platform.runLater(()->
                {
                    player.pause();
                    is_playing = false;
                });
            }
        }

        if(source == button4)
        {
            if(!queue.isEmpty())
            {
                String next = queue.poll();
                load_song(next);
            }
        }

        if(source == button1)
        {
            if(player != null)
            {
                Platform.runLater(()->
                {
                    player.stop();
                    player.play();
                    is_playing = true;
                });
            }
        }

        if(source == button5)
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("MP3 Files", "mp3"));
            int result = chooser.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                File[] files = chooser.getSelectedFiles();
                for(File f : files) queue.add(f.getAbsolutePath());

                if(player == null && !queue.isEmpty())
                {
                    String next = queue.poll();
                    load_song(next);
                }
            }
        }
    }

    public void load_song(String path)
    {
        Platform.runLater(()->
        {
            try
            {
                if(player != null) player.stop();

                Media media = new Media(new File(path).toURI().toString());
                player = new MediaPlayer(media);

                String title = "Unknown";
                String artist = "Unknown";
                BufferedImage artwork = null;

                Mp3File mp3file = new Mp3File(path);
                if(mp3file.hasId3v2Tag())
                {
                    ID3v2 tag = mp3file.getId3v2Tag();
                    if(tag.getTitle() != null) title = tag.getTitle();
                    if(tag.getArtist() != null) artist = tag.getArtist();
                    byte[] imgData = tag.getAlbumImage();
                    if(imgData != null) artwork = ImageIO.read(new ByteArrayInputStream(imgData));
                }

                final String fTitle = title;
                final String fArtist = artist;
                final BufferedImage fArtwork = artwork;

                SwingUtilities.invokeLater(()->
                {
                    label1.setText(fTitle);
                    label2.setText(fArtist);
                    if(fArtwork != null) label3.setIcon(new ImageIcon(fArtwork.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
                    else label3.setIcon(null);
                });

                player.play();
                is_playing = true;

                player.setOnEndOfMedia(()->
                {
                    if(!queue.isEmpty())
                    {
                        String next = queue.poll();
                        load_song(next);
                    }
                    else
                    {
                        is_playing = false;
                    }
                });
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public Color getRainbowColor(int x, int y)
    {
        double pix_val = Math.sin(time + (x + y) * 0.05);
        float hue_val = (float)((pix_val + 1.0f) / 2.0f);
        return Color.getHSBColor(hue_val, 1.0f, 1.0f);
    }
}
