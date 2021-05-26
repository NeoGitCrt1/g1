package ysy.game.client.v1;

import ysy.game.model.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import static ysy.game.model.BodyMeta.Direction.*;
import static ysy.game.model.Constant.CANVAS_HEIGHT;
import static ysy.game.model.Constant.CANVAS_WIDTH;

public class UIMain extends JPanel {
    public static final UIMain UI = new UIMain();
    public static final Map<String, Body> players = new HashMap<>();
    public static final Map<String, Body> mouses = new HashMap<>();
    public static final Food food = new Food();
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UIMain.class);
    //Declare menubar
    static JMenuBar menuBar;
    private GameCanvas pit;
    private ControlPanel control;
    private JLabel lblScore;

    UIMain() {
        setLayout(new BorderLayout());
        //drawing panel
        pit = new GameCanvas();
        pit.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        add(pit, BorderLayout.CENTER);

        //control panel to the bottom
        control = new ControlPanel();
        add(control, BorderLayout.SOUTH);

        //add the menu bar
        setupMenuBar();


    }

    public static void start() {
        //use the event dispatch thread to build the UI for thread-safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("G");
                // main JPanel as content pane
                frame.setContentPane(UI);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                //center the app window
                frame.setLocationRelativeTo(null);
                //show the frame
                frame.setJMenuBar(menuBar);
                frame.setVisible(true);

            }
        });

        UI.gameStart();

    }


    // Helper function to setup the menubar
    private void setupMenuBar() {
        JMenu menu; //a menu in the menu bar
        JMenuItem menuItem; // a regular menu-item in a menu

        menuBar = new JMenuBar();

        // First Menu - "Game"
        menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(menu);


        //Help Menu
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);

        menuItem = new JMenuItem("Help Contents", KeyEvent.VK_H);
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String message = "Arrow keys to change direction\n"
                        + "P to pause/resume \n"
                        + "S to toggle sound on/off \n";
                JOptionPane.showMessageDialog(UI, message,
                        "Instructions", JOptionPane.PLAIN_MESSAGE);


            }

        });

        menuItem = new JMenuItem("About", KeyEvent.VK_A);
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(UI,
                        "The snake hunting for teaching students game programming",
                        "About", JOptionPane.PLAIN_MESSAGE);

            }

        });
    }

    public void gameStart() {
        // Create a new thread
        Thread gameThread = new Thread() {
            //Override run() to provide the running behavior of this thread
            public void run() {
                while (true) {

                    // Refresh the display
                    repaint();

                    try {
                        //Provides the necessary delay and also yields control
                        //so that other thread can do work
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }

                }
            }
        };
        //Start the thread.start() calls run, which in turn calls gameLoop()
        gameThread.start();
    }

    private void gameDraw(Graphics g) {
        //draw game objects
        if (players.isEmpty() || food == null) {
            return;
        }
        players.values().forEach(b -> b.draw(g));
        mouses.values().forEach(b -> b.draw(g));
        food.draw(g);
        log.trace("draw foo:{}", food);


    }

    private void gameKeyPressed(int keyCode) {
        Man myBody = (Man) players.get(ClientEventHandle.id);
        synchronized (myBody) {
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    if (DOWN.directCode == myBody.direction()) {
                        myBody.changeDirect(HALT.directCode);
                    } else {
                        myBody.changeDirect(UP.directCode);
                    }

                    break;
                case KeyEvent.VK_DOWN:
                    if (UP.directCode == myBody.direction()) {
                        myBody.changeDirect(HALT.directCode);
                    } else {
                        myBody.changeDirect(DOWN.directCode);
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (RIGHT.directCode == myBody.direction()) {
                        myBody.changeDirect(HALT.directCode);
                    } else {
                        myBody.changeDirect(LEFT.directCode);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (LEFT.directCode == myBody.direction()) {
                        myBody.changeDirect(HALT.directCode);
                    } else {
                        myBody.changeDirect(RIGHT.directCode);
                    }
                    break;
            }
            NettyChatClient.cf.channel().writeAndFlush(myBody.gcEvent.toByteBuf());
        }

    }

    class GameCanvas extends JPanel implements KeyListener {

        private static final long serialVersionUID = 1L;

        //constructor
        public GameCanvas() {
            setFocusable(true); //so that can receive key-events
            requestFocus();
            addKeyListener(this);
        }

        //overwrite paintComponent to do custom drawing
        //called back by repaint()
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //paint background, may use an image for background
            //set background color
            setBackground(Color.decode("0x3F919E"));

            //draw the game objects
            gameDraw(g);
        }

        //KeyEvent handlers
        @Override
        public void keyPressed(KeyEvent e) {
            gameKeyPressed(e.getKeyCode());

        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        // Check if this pit contains the given(x,y) for collision detection
        @Override
        public boolean contains(int x, int y) {
            if ((x < 0) || (x >= Constant.ROWS)) return false;
            if ((y < 0) || (y >= Constant.COLUMNS)) return false;
            return true;
        }

    }

    //Game Control Panel with Start, Stop, Pause and Mute buttons, designed as an inner class
    class ControlPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private JButton btnStartPause;
        private JButton btnStop;
        private JButton btnMute;
        //import icons for buttons
        private ImageIcon iconStart = new ImageIcon(getClass().getResource("/images/start.png"), "START");
        private ImageIcon iconPause = new ImageIcon(getClass().getResource("/images/pause.png"), "PAUSE");
        private ImageIcon iconStop = new ImageIcon(getClass().getResource("/images/stop.png"), "STOP");
        private ImageIcon iconSound = new ImageIcon(getClass().getResource("/images/sound.png"), "SOUND ON");
        private ImageIcon iconMuted = new ImageIcon(getClass().getResource("/images/muted.png"), "MUTED");


        public ControlPanel() {
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

            btnStartPause = new JButton(iconPause);
            btnStartPause.setToolTipText("Pause");
            btnStartPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnStartPause.setEnabled(true);
            add(btnStartPause);

            btnStop = new JButton(iconStop);
            btnStop.setToolTipText("Stop");
            btnStop.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnStop.setEnabled(true);
            add(btnStop);

            btnMute = new JButton(iconMuted);
            btnMute.setToolTipText("Mute");
            btnMute.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnMute.setEnabled(true);
            add(btnMute);

            lblScore = new JLabel("Score: 0");
            add(lblScore);

            //handle click events on buttons
            btnStartPause.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    switch (state) {
//                        case INITIALIZED:
//                        case GAMEOVER:
//                            btnStartPause.setIcon(iconPause);
//                            btnStartPause.setToolTipText("Pause");
//                            gameStart();
//                            //To play a specific sound
//                            SoundEffect.CLICK.play();
//                            score = 0;
//                            lblScore.setText("Score: " + score);
//                            break;
//                        case PLAYING:
//                            state = GameState.PAUSED;
//                            btnStartPause.setIcon(iconStart);
//                            btnStartPause.setToolTipText("Start");
//                            //To play a specific sound
//                            SoundEffect.CLICK.play();
//                            break;
//                        case PAUSED:
//                            state = GameState.PLAYING;
//                            btnStartPause.setIcon(iconPause);
//                            btnStartPause.setToolTipText("Pause");
//                            //To play a specific sound
//                            SoundEffect.CLICK.play();
//                            break;
//                    }
                    btnStop.setEnabled(true);
                    pit.requestFocus();

                }
            });

            btnStop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    state = GameState.GAMEOVER;
                    btnStartPause.setIcon(iconStart);
                    btnStartPause.setEnabled(true);
                    btnStop.setEnabled(false);
                    //To play a specific sound
//                    SoundEffect.CLICK.play();

                }
            });

            btnMute.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    if (SoundEffect.volume == SoundEffect.Volume.MUTE) {
//                        SoundEffect.volume = SoundEffect.Volume.LOW;
//                        btnMute.setIcon(iconSound);
//                        //To play a specific sound
//                        SoundEffect.CLICK.play();
//                        pit.requestFocus();
//                    } else {
//                        SoundEffect.volume = SoundEffect.Volume.MUTE;
//                        btnMute.setIcon(iconMuted);
//                        //To play a specific sound
//                        SoundEffect.CLICK.play();
//                        pit.requestFocus();
//                    }

                }
            });

        }

        // Reset control for a new game
        public void reset() {
            btnStartPause.setIcon(iconStart);
            btnStartPause.setEnabled(true);
            btnStop.setEnabled(false);
        }
    }
}
