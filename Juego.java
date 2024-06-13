
package dawbird;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Toolkit;

public class Juego extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblBienvenido;
    private Image fondo;
    private String apodoActual; // Variable para almacenar el apodo del usuario actual

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Juego frame = new Juego("Usuario");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Juego(String apodo) {
    	
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
        panel.setBorder(new MatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        panel.setBackground(new Color(218, 233, 255));
        panel.setBounds(576, 215, 752, 564);
        contentPane.add(panel);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(0, 0, 752, 72);
        panel_1.setLayout(null);
        panel_1.setBorder(new MatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        panel.add(panel_1);

        lblBienvenido = new JLabel("Hello " + apodo);
        lblBienvenido.setForeground(new Color(119, 118, 123));
        lblBienvenido.setFont(new Font("TeX Gyre Bonum", Font.BOLD, 38));
        lblBienvenido.setBounds(12, 0, 350, 72);
        panel_1.add(lblBienvenido);
        lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnIniciar_1 = new JButton("2 PLAYERS");
        btnIniciar_1.setFont(new Font("Dialog", Font.BOLD, 18));
        btnIniciar_1.setBounds(457, 324, 177, 28);
        btnIniciar_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TwoPlayers frame = new TwoPlayers(apodo);
                frame.setVisible(true);
            }
        });
        panel.add(btnIniciar_1);

        JButton btnIniciar = new JButton("1 PLAYER");
        btnIniciar.setFont(new Font("Dialog", Font.BOLD, 18));
        btnIniciar.setBounds(135, 322, 177, 32);
        btnIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openGame(true);
            }
            
            private void openGame(boolean isPlayer1) {
                SwingUtilities.invokeLater(() -> {
                	juego_2 frame = new juego_2(isPlayer1);
                    frame.setVisible(true);
                });
            }
        });
        panel.add(btnIniciar);

        JButton btnScore = new JButton("SCORE");
        btnScore.setFont(new Font("Dialog", Font.BOLD, 22));
        btnScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Abrir la pantalla de Score con el apodo del usuario actual y datos reales de la base de datos
                try {
                    ResultSet scoreData = DBConnectionUser.getScoreByNick(apodoActual);
                    // Pasar el ResultSet completo a la clase Score
                    Score frame = new Score(apodoActual, scoreData);
                    frame.setVisible(true);

                    if (scoreData != null) {
                        scoreData.close();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnScore.setBounds(319, 430, 140, 37);
        panel.add(btnScore);

        // Otros componentes y su configuración...
        JButton btnNewButton = new JButton("");
        btnNewButton.setFont(new Font("Dialog", Font.BOLD, 22));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });

        // Dimensiones del botón
        int buttonWidth = 117;
        int buttonHeight = 101;

        // Cargar la imagen original
        ImageIcon originalIcon = new ImageIcon("./img/OnePlay.png");

        // Redimensionar la imagen
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // Establecer la imagen redimensionada como icono del botón
        btnNewButton.setIcon(resizedIcon);
        btnNewButton.setSelectedIcon(resizedIcon);

        // Posicionar el botón
        btnNewButton.setBounds(125, 169, 199, 141);
        panel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("");

        // Cargar la imagen original para el botón de 2 jugadores
        ImageIcon originalIcon2 = new ImageIcon("./img/TwoPlayers.png");

        // Redimensionar la imagen
        Image originalImage2 = originalIcon2.getImage();
        Image resizedImage2 = originalImage2.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon2 = new ImageIcon(resizedImage2);

        // Establecer la imagen redimensionada como icono del botón
        btnNewButton_1.setIcon(resizedIcon2);
        btnNewButton_1.setSelectedIcon(resizedIcon2);

        // Posicionar el botón
        btnNewButton_1.setBounds(447, 169, 194, 141);
        panel.add(btnNewButton_1);
    }
}
