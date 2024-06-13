package dawbird;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class TwoPlayers extends JFrame {
    private Image fondo;
    private JPanel contentPane;
    private String apodoActual;

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5005;

    private JButton btnPlayer1;
    private JButton btnPlayer2;
    
    boolean isServer = false;
    private boolean isClient = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                TwoPlayers frame = new TwoPlayers("Usuario");
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TwoPlayers(String apodo) {
        setIconImage(Toolkit.getDefaultToolkit().getImage("./img/Player1.png"));
        this.apodoActual = apodo; // Almacenar el apodo del usuario actual
        
        // Obtener las dimensiones de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        
        // Configurar los límites de la ventana para que coincidan con el tamaño de la pantalla
        setBounds(8000, 10000, 2000, 1200);

        fondo = new ImageIcon("./img/fondo_XP.png").getImage();

        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new MatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        panel.setBackground(new Color(218, 233, 255));
        panel.setBounds(628, 204, 752, 564);
        contentPane.add(panel);
        
        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new MatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        panel_1.setBounds(0, 0, 752, 72);
        panel.add(panel_1);
        
        JLabel lblPlayers = new JLabel("PLAYERS");
        lblPlayers.setHorizontalAlignment(SwingConstants.CENTER);
        lblPlayers.setForeground(new Color(119, 118, 123));
        lblPlayers.setFont(new Font("TeX Gyre Bonum", Font.BOLD, 41));
        lblPlayers.setBounds(208, 0, 348, 72);
        panel_1.add(lblPlayers);
        
        JButton btnBack = new JButton("BACK");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Juego frame = new Juego("");
                frame.setVisible(true);
            }
        });
        btnBack.setFont(new Font("Dialog", Font.BOLD, 22));
        btnBack.setBounds(319, 430, 140, 37);
        panel.add(btnBack);
        
        btnPlayer1 = new JButton("PLAYER 1");
        btnPlayer1.setBounds(134, 189, 150, 110);
        panel.add(btnPlayer1);
        
        btnPlayer2 = new JButton("PLAYER 2");
        btnPlayer2.setBounds(478, 189, 150, 110);
        panel.add(btnPlayer2);
        btnPlayer2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnPlayer2.setEnabled(false);
                isClient = true;
                checkGameStart();
            }
        });
        btnPlayer1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnPlayer1.setEnabled(false);
                isServer = true;
                checkGameStart();
            }
        });
    }

    private void checkGameStart() {
        if (isServer && isClient) {
            JOptionPane.showMessageDialog(this, "No se puede iniciar como servidor y cliente al mismo tiempo.", "Error", JOptionPane.ERROR_MESSAGE);
            btnPlayer1.setEnabled(true);
            btnPlayer2.setEnabled(true);
        } else if (isServer) {
            waitForClientConnection();
        } else if (isClient) {
            connectToServer();
        }
    }

    private void waitForClientConnection() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                Socket clientSocket = serverSocket.accept();
                PrintStream output = new PrintStream(clientSocket.getOutputStream());
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                openGame(true, output, input);
                handleGame(output, input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            PrintStream output = new PrintStream(socket.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            openGame(false, output, input);
            handleGame(output, input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openGame(boolean isPlayer1, PrintStream out, BufferedReader in) {
        SwingUtilities.invokeLater(() -> {
            juego_1 frame = new juego_1(this, out, in, isPlayer1); // Pasa el tipo de jugador
            frame.setVisible(true);
            // Cierra la ventana TwoPlayers cuando el juego se abre
            dispose();
        });
    }

    private void handleGame(PrintStream out, BufferedReader in) {
        new Thread(() -> {
            try {
                while (true) {
                    if (in.ready()) {
                        String message = in.readLine();
                        if (message.equals("lose")) {
                            //JOptionPane.showMessageDialog(this, "¡Has ganado!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        } else if (message.equals("win")) {
                          //  JOptionPane.showMessageDialog(this, "¡Has perdido!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
