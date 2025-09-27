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
    public JLabel label1;
    public JLabel label2;
    public JLabel label3;
    private MediaPlayer player;
    public Color curr_color = null;
    public Color next_color = null;
    private boolean is_playing = false;

    OcelotPanel()
    {
        add_graphics_and_widgets();
        new JFXPanel();
        Platform.runLater(()->
        {
            try
            {
                String filePath = "E:\\Radiohead - KID A MNESIA (2021) Mp3 320kbps [PMEDIA] ⭐️\\CD1\\01. Everything In Its Right Place.mp3";
                Media media = new Media(new File(filePath).toURI().toString());
                player = new MediaPlayer(media);

                Mp3File load_song = new Mp3File(filePath);
                String title = "Unknown";
                String artist = "Unknown";
                BufferedImage artworkImage = null;
                if(load_song.hasId3v2Tag())
                {
                    ID3v2 tag = load_song.getId3v2Tag();
                    if(tag.getTitle() != null)title = tag.getTitle();
                    if(tag.getArtist() != null)artist = tag.getArtist();
                    byte[] imageData = tag.getAlbumImage();
                    if(imageData != null)
                    {
                        InputStream in = new ByteArrayInputStream(imageData);
                        artworkImage = ImageIO.read(in);
                    }
                }
                
                final String finalTitle = title;
                final String finalArtist = artist;
                final BufferedImage finalImage = artworkImage;

                SwingUtilities.invokeLater(()->
                {
                    label1.setText(finalTitle);
                    label2.setText(finalArtist);
                    if(finalImage != null)
                    {
                        ImageIcon icon = new ImageIcon(finalImage.getScaledInstance(400, 400, Image.SCALE_SMOOTH));
                        label3.setIcon(icon);
                    }
                });
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        });

        Timer timer = new Timer(50, e->
        {
            time = time + 0.05;
            curr_color = getRainbowColor(0, 0);
            next_color = getRainbowColor(100, 100);
            label1.setForeground(curr_color);
            label2.setForeground(curr_color);
            setBackground(Color.BLACK);
        });
        timer.start();
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_LENGTH, WINDOW_HEIGHT));
    }

    public void add_graphics_and_widgets()
    {
        button1 = new JButton("Prev");
        button2 = new JButton("Play");
        button3 = new JButton("Stop");
        button4 = new JButton("Next");

        button1.setBounds(44,  25, BUTTON_LENGTH, BUTTON_HEIGHT);
        button2.setBounds(208, 25, BUTTON_LENGTH, BUTTON_HEIGHT);
        button3.setBounds(372, 25, BUTTON_LENGTH, BUTTON_HEIGHT);
        button4.setBounds(536, 25, BUTTON_LENGTH, BUTTON_HEIGHT);

        button1.setBackground(Color.RED);
        button2.setBackground(Color.RED);
        button3.setBackground(Color.RED);
        button4.setBackground(Color.RED);

        button1.setBorderPainted(false);
        button2.setBorderPainted(false);
        button3.setBorderPainted(false);
        button4.setBorderPainted(false);

        button1.setFocusPainted(false);
        button2.setFocusPainted(false);
        button3.setFocusPainted(false);
        button4.setFocusPainted(false);

        button1.setFont(new Font("Arial", Font.BOLD, 16)); 
        button2.setFont(new Font("Arial", Font.BOLD, 16)); 
        button3.setFont(new Font("Arial", Font.BOLD, 16));
        button4.setFont(new Font("Arial", Font.BOLD, 16)); 

        button2.addActionListener(this);
        button3.addActionListener(this);

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

        label1.setFont(new Font("Arial", Font.BOLD, 30));
        label2.setFont(new Font("Arial", Font.BOLD, 30));

        this.add(button1);
        this.add(button2);
        this.add(button3);
        this.add(button4);
        this.add(label1);
        this.add(label2);
        this.add(label3);
    }

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();
        if(source == button2)
        {
            Platform.runLater(()->
            {
               if(!is_playing)
               {
                   player.play();
                   is_playing = true;
               }
            });
        }
        if(source == button3)
        {
            Platform.runLater(()->
            {
               if(is_playing)
               {
                   player.pause();
                   is_playing = false;
               }
            });
        }
    }

    public Color getRainbowColor(int x, int y)
    {
        double pix_val = Math.sin(time + (x + y) * 0.05);
        float hue_val = (float)((pix_val + 1.0f) / 2.0f);
        return Color.getHSBColor(hue_val, 1.0f, 1.0f);
    }
}
