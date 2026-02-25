package MainMenu;
import API.CallGames;
import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;


/* ================= BACKGROUND PANEL ================= */
class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    public BackgroundPanel(java.net.URL imgURL) {
        try {
            if (imgURL != null) {
                backgroundImage = ImageIO.read(imgURL);
            } else {
                System.out.println("หาไฟล์ภาพไม่เจอ!");
                setBackground(Color.LIGHT_GRAY);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setBackground(Color.LIGHT_GRAY);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

class GameButton extends JButton {

    private Color normalColor;
    private Color hoverColor;
    private boolean isHover = false;

    public GameButton(String text, Color normal, Color hover) {
        super(text);
        
        normalColor = normal;
        hoverColor = hover;

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 22));
        setPreferredSize(new Dimension(250, 60));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isHover = true;
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                isHover = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //เงาปุ่ม
        g2.setColor(new Color(0,0,0,80));
        g2.fillRoundRect(3,6,getWidth()-3,getHeight()-3,30,30);

        // ===== สีปุ่ม =====
        g2.setColor(isHover ? hoverColor : normalColor);

        g2.fillRoundRect(
            0,
            0,
            getWidth()-3,
            getHeight()-6,
            30,
            30
        );

        super.paintComponent(g);
    }
}

/* ================= MAIN APP ================= */
public class App {

    private static CardLayout cardLayout;
    private static JPanel cardPanel;

    private static int volume = 50;
    private static int sfxVolume = 50;
    private static boolean muted = false;

    private static Clip backgroundClip;
    private static Clip clickClip;
    private static FloatControl volumeControl;
    private static FloatControl sfxVolumeControl;


    private static final String CONFIG_FILE = "config.properties";

    public static void main(String[] args) {

        loadConfig();
        initSound();
        initClickSound();

        JFrame frame = new JFrame("Suntana");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setSize(1452, 854);
        frame.setLocationRelativeTo(null);
        // GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        // gd.setFullScreenWindow(frame);
        frame.setVisible(true);

        java.net.URL bgURL = App.class.getResource("01.jpg");
        BackgroundPanel bgPanel = new BackgroundPanel(bgURL);
        bgPanel.setLayout(new CardLayout());

        cardLayout = (CardLayout) bgPanel.getLayout();
        cardPanel = bgPanel;

        cardPanel.add(createMainMenu(frame), "MAIN");
        cardPanel.add(createOptionMenu(), "OPTION");
        frame.setContentPane(cardPanel);
        frame.setVisible(true);
    }

    /* ================= MAIN MENU ================= */
    private static JPanel createMainMenu(JFrame frame) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setOpaque(false);

        String[] menuItems = {"Start Game", "Option", "Exit"};

        for (String text : menuItems) {
            GameButton btn;

        switch(text){

            case "Start Game" ->
                btn = new GameButton(
                    text,
                    new Color(46, 125, 50),   // เขียว
                    new Color(76, 175, 80)    // เขียวอ่อน
                );

            case "Option" ->
                btn = new GameButton(
                    text,
                    new Color(33, 150, 243),  // ฟ้า
                    new Color(100, 181, 246)
                );

            case "Credit" ->
                btn = new GameButton(
                    text,
                    new Color(156, 39, 176), // ม่วง
                    new Color(186, 104, 200)
                );

            default -> // Exit
                btn = new GameButton(
                    text,
                    new Color(198, 40, 40), // แดง
                    new Color(239, 83, 80)
                );
        }

            btn.addActionListener(e -> {

                playSoundClickButton();

                switch (text) {
                    case "Start Game" -> {
                        CallGames.StartGamePanel(); 
                            frame.dispose();
                    }
                    case "Option" -> {
                        cardLayout.show(cardPanel, "OPTION");
                    }
                    case "Exit" -> {
                        System.exit(0);
                    }
                }

            });

            buttonPanel.add(btn);
        }

        panel.add(buttonPanel);
        return panel;
    }

    /* ================= OPTION MENU ================= */
    private static JPanel createOptionMenu() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        // ===== กระดานไม้กลางจอ =====
        JPanel board = new JPanel();
        board.setLayout(new BoxLayout(board, BoxLayout.Y_AXIS));
        board.setBackground(new Color(222, 158, 107));
        board.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(101,44,28), 8),
                BorderFactory.createEmptyBorder(40, 120, 40, 120)
        ));

        JLabel title = new JLabel("OPTIONS");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(new Color(150, 60, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel volumeLabel = new JLabel("Volume: " + volume + "%");
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        volumeLabel.setForeground(new Color(120, 50, 30));
        volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider volumeSlider = new JSlider(0, 100, volume);
        volumeSlider.setOpaque(false);
        volumeSlider.setFocusable(false);
        volumeSlider.setMaximumSize(new Dimension(400, 60));
        volumeSlider.setUI(new ModernSliderUI(volumeSlider));

        volumeSlider.addChangeListener(e -> {
            volume = volumeSlider.getValue();
            volumeLabel.setText("Volume: " + volume + "%");
            updateVolume();
            saveConfig();
            volumeSlider.repaint();
        });

        GameButton soundBtn = new GameButton(
            muted ? "Sound: OFF" : "Sound: ON",
            new Color(120,80,40),
            new Color(170,120,80)
        );
        soundBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        soundBtn.addActionListener(e -> {
            muted = !muted;
            soundBtn.setText(muted ? "Sound: OFF" : "Sound: ON");
            updateVolume();
            saveConfig();
        });

        // ===== แถว Volume =====
        JPanel volumeRow = new JPanel();
        volumeRow.setLayout(new BoxLayout(volumeRow, BoxLayout.X_AXIS));
        volumeRow.setOpaque(false);
        volumeRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        volumeSlider.setMaximumSize(new Dimension(500, 100));
        volumeLabel.setPreferredSize(new Dimension(140, 50));

        volumeRow.add(volumeLabel);
        volumeRow.add(Box.createRigidArea(new Dimension(20, 0)));
        volumeRow.add(volumeSlider);

        // ===== แถว SFX  =====

        JLabel sfxLabel = new JLabel("Sound Effect: " + sfxVolume + "%");
        sfxLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sfxLabel.setForeground(new Color(120, 50, 30));
        sfxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider sfxSlider = new JSlider(0, 100, sfxVolume);
        sfxSlider.setOpaque(false);
        sfxSlider.setFocusable(false);
        sfxSlider.setMaximumSize(new Dimension(500, 50));
        sfxLabel.setPreferredSize(new Dimension(140, 50));
        sfxSlider.setUI(new ModernSliderUI(sfxSlider));

        sfxSlider.addChangeListener(e -> {
            sfxVolume = sfxSlider.getValue();
            sfxLabel.setText("Sound Effect: " + sfxVolume + "%");
            updateSFXVolume();
            saveConfig();
            sfxSlider.repaint();
        });

        JPanel sfxRow = new JPanel();
        sfxRow.setLayout(new BoxLayout(sfxRow, BoxLayout.X_AXIS));
        sfxRow.setOpaque(false);
        sfxRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        sfxRow.add(sfxLabel);
        sfxRow.add(Box.createRigidArea(new Dimension(20, 0)));
        sfxRow.add(sfxSlider);

        board.add(title);
        board.add(Box.createRigidArea(new Dimension(0, 30)));

        board.add(volumeRow);

        board.add(Box.createRigidArea(new Dimension(0, 30)));
        board.add(sfxRow);

        board.add(Box.createRigidArea(new Dimension(0, 30)));
        board.add(soundBtn);


        // วางกระดานไว้กลาง
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(board, gbc);

        // ===== ปุ่ม BACK ชิดขวาล่าง =====
        WoodBackButton backBtn = new WoodBackButton("BACK");
        backBtn.addActionListener(e -> {
            playSoundClickButton();
            cardLayout.show(cardPanel, "MAIN");
        });

        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(0, 0, 40, 40);

        panel.add(backBtn, gbc);

        return panel;
    }


    /*Playsound Mainmanu */
    private static void initSound() {
        try {

            InputStream input = App.class.getClassLoader().getResourceAsStream("MainMenu/Rock.wav");
            if (input == null) {
                System.out.println("ไม่พบไฟล์เสียง Rock.wav");
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(input);

            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);

            volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);

            updateVolume();// ตั้งค่าระดับเสียงตอนเริ่ม

            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();

        } catch (Exception e) {
            System.out.println("โหลดเสียงไม่สำเร็จ");
            e.printStackTrace();
        }
    }

    private static void updateVolume() {
        if (volumeControl == null) return;

        if (muted) {
            volumeControl.setValue(volumeControl.getMinimum());
        } else {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float gain = min + (max - min) * (volume / 100f);
            volumeControl.setValue(gain);
        }
    }

    /* ================= CONFIG ================= */
    private static void saveConfig() {
        try {
            Properties prop = new Properties();
            prop.setProperty("volume", String.valueOf(volume));
            prop.setProperty("sfxVolume", String.valueOf(sfxVolume));
            prop.setProperty("muted", String.valueOf(muted));
            prop.store(new FileOutputStream(CONFIG_FILE), "Game Config");

        } catch (IOException ignored) {}
    }

    private static void loadConfig() {
        try {
            File file = new File(CONFIG_FILE);
            if (!file.exists()) return;

            Properties prop = new Properties();
            prop.load(new FileInputStream(file));

            volume = Integer.parseInt(prop.getProperty("volume", "50"));
            sfxVolume = Integer.parseInt(prop.getProperty("sfxVolume", "10"));
            muted = Boolean.parseBoolean(prop.getProperty("muted", "false"));


        } catch (IOException ignored) {}
    }

    public static class ModernSliderUI extends BasicSliderUI {
        private static final int TRACK_HEIGHT = 8;
        private static final int THUMB_SIZE = 20;
        private static final Color TRACK_COLOR = new Color(200, 170, 120);
        private static final Color ACTIVE_COLOR = new Color(150, 80, 40);

        public ModernSliderUI(JSlider b) {
            super(b);
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Rectangle trackBounds = trackRect;
            
            // วาดพื้นหลัง (สีเทา)
            g2.setColor(TRACK_COLOR);
            g2.fill(new RoundRectangle2D.Double(trackBounds.x, trackBounds.y + (trackBounds.height - TRACK_HEIGHT) / 2.0, 
                    trackBounds.width, TRACK_HEIGHT, TRACK_HEIGHT, TRACK_HEIGHT));

            // วาดส่วนที่เลือกแล้ว (สีฟ้า)
            int thumbPos = thumbRect.x + thumbRect.width / 2;
            int trackLeft = trackBounds.x;
            
            g2.setColor(ACTIVE_COLOR);
            g2.fill(new RoundRectangle2D.Double(trackLeft, trackBounds.y + (trackBounds.height - TRACK_HEIGHT) / 2.0, 
                    thumbPos - trackLeft, TRACK_HEIGHT, TRACK_HEIGHT, TRACK_HEIGHT));
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = thumbRect.x;
            int y = thumbRect.y + (thumbRect.height - THUMB_SIZE) / 2;

            // วาดเงาเล็กน้อย
            g2.setColor(new Color(0,0,0, 30));
            g2.fillOval(x + 1, y + 2, THUMB_SIZE, THUMB_SIZE);

            // วาดปุ่มวงกลมสีขาว
            g2.setColor(Color.WHITE);
            g2.fillOval(x, y, THUMB_SIZE, THUMB_SIZE);
            
            // วาดขอบปุ่มสีฟ้า
            g2.setColor(ACTIVE_COLOR);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(x, y, THUMB_SIZE, THUMB_SIZE);
        }

        @Override
        protected Dimension getThumbSize() {
            return new Dimension(THUMB_SIZE, THUMB_SIZE); // กำหนดขนาดปุ่ม
        }
    }

    //Sound กด
    private static void initClickSound() {
        try {
            InputStream input = App.class.getClassLoader().getResourceAsStream("MainMenu/Click_Captainsounds.wav");

            if (input == null) {
                System.out.println("ไม่พบไฟล์เสียง Click_Captainsounds.wav");
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(input);

            clickClip = AudioSystem.getClip();
            clickClip.open(audioStream);

             sfxVolumeControl = (FloatControl) clickClip.getControl(FloatControl.Type.MASTER_GAIN);

            updateSFXVolume();

        } catch (Exception e) {
            System.out.println("โหลดเสียงปุ่มไม่สำเร็จ");
            e.printStackTrace();
        }
    }
    
    public static void playSoundClickButton() {
        
        if (clickClip == null || muted) return;

        if (clickClip.isRunning()) {
            clickClip.stop();
        }

        clickClip.setFramePosition(0);
        clickClip.start();
    }

    private static void updateSFXVolume() {
        if (sfxVolumeControl == null) return;

        if (muted || sfxVolume == 0) {
            sfxVolumeControl.setValue(sfxVolumeControl.getMinimum());
            return;
        }

        float min = sfxVolumeControl.getMinimum();
        float max = sfxVolumeControl.getMaximum();

        float gain = (float) (Math.log10(sfxVolume / 100.0) * 20.0);
        gain = Math.max(min, Math.min(max, gain));

        sfxVolumeControl.setValue(gain);
    }

    /* ================= CUSTOM WOOD BACK BUTTON ================= */
    private static class WoodBackButton extends JButton {
        public WoodBackButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setForeground(new Color(101, 44, 28)); // สีน้ำตาลเข้ม
            setFont(new Font("Arial", Font.BOLD, 20));
            setPreferredSize(new Dimension(140, 50));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // 1. วาดรูปทรงปุ่มไม้ (สี่เหลี่ยม + ปลายแหลมด้านขวา)
            int[] xPoints = {0, w - 20, w, w - 20, 0};
            int[] yPoints = {0, 0, h / 2, h, h};
            
            // เงาด้านล่าง
            g2.setColor(new Color(60, 30, 10));
            g2.fillPolygon(xPoints, yPoints, 5);
            
            // ตัวปุ่ม (สีเนื้อไม้)
            g2.setColor(new Color(222, 158, 107));
            g2.fillPolygon(new int[]{2, w - 22, w - 5, w - 22, 2}, 
                        new int[]{2, 2, h / 2, h - 2, h - 2}, 5);

            // เส้นขอบไม้
            g2.setColor(new Color(101, 44, 28));
            g2.setStroke(new BasicStroke(3));
            g2.drawPolygon(new int[]{2, w - 22, w - 5, w - 22, 2}, 
                        new int[]{2, 2, h / 2, h - 2, h - 2}, 5);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}