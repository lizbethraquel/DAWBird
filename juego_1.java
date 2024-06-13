package dawbird;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

public class juego_1 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    protected Image fondo = new ImageIcon("./img/fondoXP.png").getImage();
    protected Image birdOrange = new ImageIcon("./img/bird1.png").getImage();

    private JPanel[] lblColumup;
    private JPanel[] lblColumdown;

    private int birdOrangeY;
    private int[] columupX;
    private int[] columdownX;
    private boolean isOrangeJumping;
    private int orangeJumpSpeed;
    private int columGap;
    private Random random;

    private TwoPlayers parent;

    private PrintStream out;
    private BufferedReader in;

    private static final int COLUMN_WIDTH = 100;
    private static final int MIN_GAP = 400; // Mínimo espacio entre columnas
    private static final int MAX_GAP = 600; // Máximo espacio entre columnas
    private int numColumns;

    private boolean isPlayer1;

    private JLabel messageLabel;

    public juego_1(TwoPlayers parent, PrintStream out, BufferedReader in, boolean isPlayer1) {
        this.parent = parent;
        this.isPlayer1 = isPlayer1;

        this.parent = parent;
        this.in = in;
        this.out = out;
        setTitle("FlappyBird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600); // Establece las dimensiones iniciales de la ventana

        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibuja la imagen de fondo
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

                // Dibuja las columnas como rectángulos
                for (int i = 0; i < numColumns; i++) {
                    g.setColor(Color.GREEN); // Color de las columnas
                    g.fillRect(columupX[i], 0, COLUMN_WIDTH, lblColumup[i].getHeight());
                    g.fillRect(columdownX[i], lblColumdown[i].getY(), COLUMN_WIDTH, getHeight() - lblColumdown[i].getY());
                }
            }
        };
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Inicializa el número de columnas según el tamaño de la pantalla
        numColumns = getWidth() / (COLUMN_WIDTH + MIN_GAP); // +3 para asegurar que siempre hay columnas extra
        lblColumdown = new JPanel[numColumns];
        lblColumup = new JPanel[numColumns];
        columupX = new int[numColumns];
        columdownX = new int[numColumns];

        for (int i = 0; i < numColumns; i++) {
            lblColumup[i] = new JPanel();
            lblColumup[i].setBackground(Color.GREEN); // Color de la columna
            contentPane.add(lblColumup[i]);

            lblColumdown[i] = new JPanel();
            lblColumdown[i].setBackground(Color.GREEN); // Color de la columna
            contentPane.add(lblColumdown[i]);
        }

        birdOrange = birdOrange.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel lblBirdOrange = new JLabel(new ImageIcon(birdOrange));
        lblBirdOrange.setBounds(157, 200, 60, 60);
        contentPane.add(lblBirdOrange);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jump();
                    out.println("jump");
                }
            }
        });

        birdOrangeY = 200;
        isOrangeJumping = false;
        orangeJumpSpeed = 0;
        random = new Random();

        // Crear la etiqueta de mensaje
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 32));
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(0, getHeight() / 2 - 50, getWidth(), 100);
        contentPane.add(messageLabel);

        resetColumns();

        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; i < numColumns; i++) {
                        columupX[i] -= 4;
                        columdownX[i] -= 4;

                        if (columupX[i] + COLUMN_WIDTH < 0) {
                            int lastIndex = (i == 0) ? numColumns - 1 : i - 1;
                            columupX[i] = columupX[lastIndex] + COLUMN_WIDTH + MIN_GAP + random.nextInt(MAX_GAP - MIN_GAP);
                            columdownX[i] = columupX[i];

                            int columupY = -26 - random.nextInt(300);
                            int gapHeight = 200;
                            lblColumup[i].setBounds(columupX[i], columupY, COLUMN_WIDTH, random.nextInt(200) + 100);
                            lblColumdown[i].setBounds(columdownX[i], columupY + lblColumup[i].getHeight() + gapHeight, COLUMN_WIDTH, getHeight() - (columupY + lblColumup[i].getHeight() + gapHeight));
                        }

                        lblColumup[i].setBounds(columupX[i], lblColumup[i].getY(), COLUMN_WIDTH, lblColumup[i].getHeight());
                        lblColumdown[i].setBounds(columdownX[i], lblColumdown[i].getY(), COLUMN_WIDTH, getHeight() - lblColumdown[i].getY());
                    }

                    try {
                        if (in.ready()) {
                            String request = in.readLine();
                            if (request.equals("jump")) {
                                jump();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isOrangeJumping) {
                        birdOrangeY -= orangeJumpSpeed;
                        orangeJumpSpeed -= 1;
                    } else {
                        birdOrangeY += 1; // Reduce la velocidad de caída
                    }

                    lblBirdOrange.setBounds(157, birdOrangeY, 60, 60);

                    // Manejo de colisiones
                    for (int i = 0; i < numColumns; i++) {
                        Rectangle birdRect = new Rectangle(lblBirdOrange.getX(), lblBirdOrange.getY(), lblBirdOrange.getWidth(), lblBirdOrange.getHeight());
                        Rectangle columnUpRect = new Rectangle(lblColumup[i].getX(), lblColumup[i].getY(), lblColumup[i].getWidth(), lblColumup[i].getHeight());
                        Rectangle columnDownRect = new Rectangle(lblColumdown[i].getX(), lblColumdown[i].getY(), lblColumdown[i].getWidth(), lblColumdown[i].getHeight());

                        if (birdRect.intersects(columnUpRect) || birdRect.intersects(columnDownRect)) {
                            handleCollision();
                        }
                    }

                    if (birdOrangeY < 0 || birdOrangeY > getHeight() - 60) {
                        handleCollision();
                    }

                    // Vuelve a pintar el contenido del panel
                    contentPane.repaint();
                }
            },
            0,
            16 // 16ms = 60fps
        );
    }

    // Método para manejar el salto del pájaro
    public void jump() {
        isOrangeJumping = true;
        orangeJumpSpeed = 10;
    }



    private void handleCollision() {
        if (parent != null) {
            if (out != null) {
                try {
                    // Si este es el servidor (Player 1) y ha perdido, envía "lose"
                    // Si este es el cliente (Player 2) y ha perdido, envía "win"
                    if (parent.isServer) {
                        out.println("lose");
                        showMessage("¡Has perdido!");
                    } else {
                        out.println("win");
                        showMessage("¡Has ganado!");
                    }
                    out.flush(); // Asegurar que el mensaje se envíe inmediatamente
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Cierra la ventana del juego usando la referencia del contenedor principal
            if (SwingUtilities.getWindowAncestor(parent) != null) {
            SwingUtilities.getWindowAncestor(parent).dispose();
            }
        }
    }

    private void showMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            messageLabel.setText(message);
            messageLabel.setVisible(true);
        });
    }

    // Método para reiniciar las columnas
    private void resetColumns() {
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        for (int i = 0; i < numColumns; i++) {
            columupX[i] = screenWidth + i * (COLUMN_WIDTH + MIN_GAP + random.nextInt(MAX_GAP - MIN_GAP)); // Añadir variabilidad en la separación
            columdownX[i] = columupX[i];
            int gapHeight = 200;

            int columupY = -20 - random.nextInt(100); // Variabilidad de la posición vertical de la columna superior

            lblColumup[i].setBounds(columupX[i], columupY, COLUMN_WIDTH, random.nextInt(200) + 100);
            lblColumdown[i].setBounds(columdownX[i], columupY + lblColumup[i].getHeight() + gapHeight, COLUMN_WIDTH, screenHeight - (columupY + lblColumup[i].getHeight() + gapHeight));
        }
    }
}