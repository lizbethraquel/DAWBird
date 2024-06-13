
package dawbird;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;

public class Register extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Image fondo;
    private JPanel panel;
    private JPanel panel_1;
    private JLabel lblIntroduceTuUsuario;
    private JLabel lblUsuario;
    private JTextField textNombre;
    private JLabel lblContrasea;
    private JPasswordField passwordField_1;
    private JPasswordField textVerificarContra;
    private JTextField textApodo;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Register frame = new Register();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Register() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage("./img/Player1.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Obtener las dimensiones de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        
        // Configurar los límites de la ventana para que coincidan con el tamaño de la pantalla
        setBounds(0, 0, screenWidth, screenHeight);

        fondo = new ImageIcon("./img/fondo_XP.png").getImage();

        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
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

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new MatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        panel.setBackground(new Color(218, 233, 255));
        panel.setBounds(576, 217, 752, 564);
        contentPane.add(panel);

        panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new MatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        panel_1.setBounds(0, 0, 752, 72);
        panel.add(panel_1);

        lblIntroduceTuUsuario = new JLabel("REGISTER");
        lblIntroduceTuUsuario.setHorizontalAlignment(SwingConstants.CENTER);
        lblIntroduceTuUsuario.setForeground(new Color(119, 118, 123));
        lblIntroduceTuUsuario.setFont(new Font("TeX Gyre Bonum", Font.BOLD, 41));
        lblIntroduceTuUsuario.setBounds(251, 0, 255, 72);
        panel_1.add(lblIntroduceTuUsuario);

        lblUsuario = new JLabel("NAME");
        lblUsuario.setFont(new Font("Dialog", Font.BOLD, 22));
        lblUsuario.setBounds(61, 113, 136, 23);
        panel.add(lblUsuario);

        textNombre = new JTextField();
        textNombre.setColumns(10);
        textNombre.setBounds(61, 148, 433, 34);
        panel.add(textNombre);

        lblContrasea = new JLabel("PASSWORD");
        lblContrasea.setFont(new Font("Dialog", Font.BOLD, 22));
        lblContrasea.setBounds(61, 205, 171, 25);
        panel.add(lblContrasea);

        passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(61, 252, 433, 34);
        panel.add(passwordField_1);

        JLabel lblVerifyPassword = new JLabel("VERIFY PASSWORD");
        lblVerifyPassword.setFont(new Font("Dialog", Font.BOLD, 22));
        lblVerifyPassword.setBounds(61, 309, 307, 30);
        panel.add(lblVerifyPassword);

        textVerificarContra = new JPasswordField();
        textVerificarContra.setBounds(61, 356, 433, 34);
        panel.add(textVerificarContra);

        JLabel lblNick = new JLabel("NICK");
        lblNick.setFont(new Font("Dialog", Font.BOLD, 22));
        lblNick.setBounds(61, 425, 200, 23);
        panel.add(lblNick);

        JButton btnRegistrar = new JButton("Register");
        btnRegistrar.setFont(new Font("Dialog", Font.BOLD, 16));
        btnRegistrar.setBounds(583, 244, 136, 47);
        panel.add(btnRegistrar);

        JButton btnAtrs = new JButton("BACK");
        btnAtrs.setFont(new Font("Dialog", Font.BOLD, 14));
        btnAtrs.setBounds(583, 374, 136, 25);
        panel.add(btnAtrs);

        textApodo = new JTextField();
        textApodo.setColumns(10);
        textApodo.setBounds(61, 469, 433, 34);
        panel.add(textApodo);

        btnAtrs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Inicio frame = new Inicio();
                    frame.setVisible(true);
                    dispose();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = textNombre.getText();
                String contraseña = new String(passwordField_1.getPassword());
                String verificarContraseña = new String(textVerificarContra.getPassword());
                String apodo = textApodo.getText();

                if (nombre.isEmpty() || contraseña.isEmpty() || verificarContraseña.isEmpty() || apodo.isEmpty()) {
                    JOptionPane.showMessageDialog(Register.this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!contraseña.equals(verificarContraseña)) {
                    JOptionPane.showMessageDialog(Register.this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    DBConnectionUser.insertUser(nombre, contraseña, apodo);
                    JOptionPane.showMessageDialog(Register.this, "¡Registro exitoso!", "Éxito", JOptionPane.PLAIN_MESSAGE);
                    Inicio frame = new Inicio();
                    frame.setVisible(true);
                    dispose();
                }
            }
        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
}
