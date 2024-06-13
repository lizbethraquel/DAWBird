package dawbird;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JCheckBox;

public class Inicio extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtName;
    private JPasswordField passwordField;
    private Image fondo;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Inicio frame = new Inicio();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Inicio() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage("./img/Player1.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(8000, 10000, 2000, 1200);
        
        fondo = new ImageIcon("./img/fondo_XP.png").getImage();
        
        contentPane = new JPanel() {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibuja la imagen de fondo
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    };
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
        panel.setBackground(new Color(218, 233, 255));
        panel.setBounds(576, 217, 752, 564);
        contentPane.add(panel);
                panel.setLayout(null);
                
                        JButton btnRegistrar = new JButton("REGISTER");
                        btnRegistrar.setBackground(UIManager.getColor("MenuItem.background"));
                        btnRegistrar.setFont(new Font("Dialog", Font.BOLD, 14));
                        btnRegistrar.setBounds(324, 448, 117, 25);
                        panel.add(btnRegistrar);
                        
                                JButton btnIniciar = new JButton("LOGIN");
                                btnIniciar.setFont(new Font("Dialog", Font.BOLD, 16));
                                btnIniciar.setBounds(324, 335, 117, 25);
                                panel.add(btnIniciar);
                                
                                JPanel panel_1 = new JPanel();
                                panel_1.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
                                panel_1.setBounds(0, 0, 752, 72);
                                panel.add(panel_1);
                                        panel_1.setLayout(null);
                                
                                        JLabel lblIntroduceTuUsuario = new JLabel("LOGIN");
                                        lblIntroduceTuUsuario.setForeground(new Color(119, 118, 123));
                                        lblIntroduceTuUsuario.setBounds(195, 0, 348, 72);
                                        panel_1.add(lblIntroduceTuUsuario);
                                        lblIntroduceTuUsuario.setHorizontalAlignment(SwingConstants.CENTER);
                                        lblIntroduceTuUsuario.setFont(new Font("TeX Gyre Bonum", Font.BOLD, 41));
                                
                                        JLabel lblUsuario = new JLabel("NAME ");
                                        lblUsuario.setFont(new Font("Dialog", Font.BOLD, 22));
                                        lblUsuario.setBounds(228, 109, 138, 25);
                                        panel.add(lblUsuario);
                                        
                                                txtName = new JTextField();
                                                txtName.setBounds(228, 161, 324, 34);
                                                panel.add(txtName);
                                                txtName.setColumns(10);
                                                
                                                        JLabel lblContrasea = new JLabel("PASSWORD");
                                                        lblContrasea.setFont(new Font("Dialog", Font.BOLD, 22));
                                                        lblContrasea.setBounds(228, 224, 232, 25);
                                                        panel.add(lblContrasea);
                                                        
                                                                passwordField = new JPasswordField();
                                                                passwordField.setBounds(228, 269, 324, 34);
                                                                panel.add(passwordField);
                                                                
                                                                JLabel lblYouDoNot = new JLabel("You do not have an account yet?");
                                                                lblYouDoNot.setFont(new Font("Dialog", Font.PLAIN, 14));
                                                                lblYouDoNot.setBounds(271, 409, 281, 15);
                                                                panel.add(lblYouDoNot);
                                btnIniciar.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        String name = txtName.getText();
                                        String password = new String(passwordField.getPassword());

                                        String nick = DBConnectionUser.authenticateUser(name, password);
                                        if (nick != null) {
                                            try {
                                                Juego frame = new Juego(nick); // 'nick' es el apodo del usuario obtenido de la base de datos
                                                frame.setVisible(true);
                                                dispose(); // Cerrar la ventana de inicio de sesión
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                });
                        btnRegistrar.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent e) {
                        		try {
                                    Register frame = new Register();
                                    frame.setVisible(true);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                        	}
                        });
                        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}