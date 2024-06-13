package dawbird;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class juego_2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	protected Image fondo;
	protected Image birdOrange;

	private JPanel[] pnlColumdown;
	private JPanel[] pnlColumup;

	private int birdOrangeY;
	private int[] columupX;
	private int[] columdownX;
	private boolean isOrangeJumping;
	private int orangeJumpSpeed;
	private Random random;
	private boolean gameRunning;
	private JLabel messageLabel;
	private JButton btnAtrs;

	private static final int COLUMN_WIDTH = 100;
	private static final int MIN_GAP = 400; // Mínimo espacio entre columnas
	private int numColumns;

	public juego_2(boolean isPlayer1) {
		setIconImage(Toolkit.getDefaultToolkit().getImage("./img/Player1.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Obtener las dimensiones de la pantalla
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();

		// Configurar los límites de la ventana para que coincidan con el tamaño de la
		// pantalla
		setBounds(0, 0, screenWidth, screenHeight);

		fondo = new ImageIcon("./img/fondo_P.png").getImage();
		birdOrange = new ImageIcon("./img/bird1.png").getImage();
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

		// Inicializa el número de columnas según el tamaño de la pantalla
		numColumns = screenWidth / (COLUMN_WIDTH + MIN_GAP) + 3; // +3 para asegurar que siempre hay columnas extra
		pnlColumdown = new JPanel[numColumns];
		pnlColumup = new JPanel[numColumns];
		columupX = new int[numColumns];
		columdownX = new int[numColumns];

		for (int i = 0; i < numColumns; i++) {
			pnlColumup[i] = createColumnPanel();
			contentPane.add(pnlColumup[i]);

			pnlColumdown[i] = createColumnPanel();
			contentPane.add(pnlColumdown[i]);
		}

		birdOrange = birdOrange.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		JLabel lblBirdOrange = new JLabel("");
		lblBirdOrange.setIcon(new ImageIcon(birdOrange));
		lblBirdOrange.setBounds(157, 200, 60, 60);
		contentPane.add(lblBirdOrange);

		// Añadir listener de teclas según el jugador
		if (isPlayer1) {
			contentPane.setFocusable(true); // Asegura que el panel principal pueda recibir el foco para eventos de
											// teclado
			contentPane.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
						isOrangeJumping = true;
						orangeJumpSpeed = 10; // Incrementa la velocidad inicial del salto
					}
				}
			});

			contentPane.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					isOrangeJumping = true;
					orangeJumpSpeed = 10; // Incrementa la velocidad inicial del salto
				}
			});
		}

		birdOrangeY = 200;
		isOrangeJumping = false;
		orangeJumpSpeed = 0;
		random = new Random();
		gameRunning = true;

		resetColumns();

		// Crear y configurar el messageLabel
		messageLabel = new JLabel("");
		messageLabel.setBounds(300, 250, 200, 50);
		messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
		messageLabel.setForeground(Color.RED);
		contentPane.add(messageLabel);
		messageLabel.setVisible(false);

		// Configurar botón de "Atrás" pero inicialmente invisible
		btnAtrs = new JButton("BACK");
		btnAtrs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Aquí deberías crear el nuevo JFrame o realizar la acción de regresar a la
					// pantalla anterior
					// Por ejemplo, crear un nuevo JFrame Juego y hacerlo visible
					Juego frame = new Juego(""); // Asumiendo que Juego es el nombre de tu clase principal
					frame.setVisible(true);
					dispose(); // Cierra el JFrame actual
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// Calcula las coordenadas para centrar el botón en la ventana
		int btnWidth = 136;
		int btnHeight = 25;
		int btnX = (getWidth() - btnWidth) / 2;
		int btnY = (getHeight() - btnHeight) / 2;

		btnAtrs.setBounds(btnX, btnY, btnWidth, btnHeight);
		btnAtrs.setFont(new Font("Dialog", Font.BOLD, 14));
		btnAtrs.setVisible(false); // Inicialmente invisible
		contentPane.add(btnAtrs);

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				if (!gameRunning) {
					cancel(); // Detiene el temporizador si el juego ya no está en ejecución
					return;
				}

				for (int i = 0; i < numColumns; i++) {
					// Verificar colisión con la parte superior de la columna
					if (columupX[i] + COLUMN_WIDTH > 157 && columupX[i] < 157 + 60
							&& birdOrangeY < pnlColumup[i].getY() + 200 && birdOrangeY + 60 > pnlColumup[i].getY()) {
						handleCollision();
						return;
					}
					// Verificar colisión con la parte inferior de la columna
					if (columdownX[i] + COLUMN_WIDTH > 157 && columdownX[i] < 157 + 60
							&& birdOrangeY < pnlColumdown[i].getY() + getHeight()
							&& birdOrangeY + 60 > pnlColumdown[i].getY()) {
						handleCollision();
						return;
					}
				}

				// Verificar si el pájaro ha chocado con un borde superior o inferior
				if (birdOrangeY < 0 || birdOrangeY > getHeight() - 60) {
					handleCollision();
					return;
				}

				for (int i = 0; i < numColumns; i++) {
					columupX[i] -= 4; // Aumentar la velocidad de las columnas para que aparezcan más frecuentemente
					columdownX[i] -= 4;

					if (columupX[i] < -COLUMN_WIDTH) {
						columupX[i] = getWidth();
						columdownX[i] = getWidth();
						int columupY = -20 - random.nextInt(100);
						int gapHeight = 200; // Altura del espacio entre columnas

						pnlColumup[i].setBounds(columupX[i], columupY, COLUMN_WIDTH, 200);
						pnlColumdown[i].setBounds(columdownX[i], columupY + pnlColumup[i].getHeight() + gapHeight,
								COLUMN_WIDTH, getHeight() - (columupY + pnlColumup[i].getHeight() + gapHeight));
					}

					pnlColumup[i].setBounds(columupX[i], pnlColumup[i].getY(), COLUMN_WIDTH, 200);
					pnlColumdown[i].setBounds(columdownX[i], pnlColumdown[i].getY(), COLUMN_WIDTH,
							getHeight() - pnlColumdown[i].getY());
				}

				if (isOrangeJumping) {
					birdOrangeY -= orangeJumpSpeed;
					orangeJumpSpeed -= 1;
				} else {
					birdOrangeY += 1; // Reduce la velocidad de caída
				}

				lblBirdOrange.setBounds(157, birdOrangeY, 60, 60);

				for (int i = 0; i < numColumns; i++) {
					if (columupX[i] < 157 && columupX[i] + COLUMN_WIDTH > 157
							&& birdOrangeY < pnlColumup[i].getY() + 200) {
						// Handle collision for orange bird
						handleCollision();
						return;
					}
					if (columdownX[i] < 157 && columdownX[i] + COLUMN_WIDTH > 157
							&& birdOrangeY > pnlColumdown[i].getY() - 60) {
						// Handle collision for orange bird
						handleCollision();
						return;
					}
				}

				// Verifica si el pájaro ha chocado con un borde
				if (birdOrangeY < 0 || birdOrangeY > getHeight() - 60) { 
					// Maneja el impacto con el borde superior o
					handleCollision();
					return;
				}
			}
		}, 0, 16 // 16ms = 60fps
		);
	}

	private JPanel createColumnPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.GREEN);
		panel.setLayout(null);
		return panel;
	}

	private void resetColumns() {
		int screenWidth = getWidth();
		int screenHeight = getHeight(); // Obtener la altura de la pantalla
		for (int i = 0; i < numColumns; i++) {
			columupX[i] = screenWidth + i * (COLUMN_WIDTH + MIN_GAP); // Se establece para la pantalla completa
			columdownX[i] = screenWidth + i * (COLUMN_WIDTH + MIN_GAP); // Se establece para la pantalla completa
			int gapHeight = 200; // Altura del espacio entre columnas, ajusta según sea necesario

			int columupY = -20 - random.nextInt(100); // Aumenta la variabilidad de la posición vertical de la columna
														// superior

			// Ajustar la posición vertical de las columnas
			pnlColumup[i].setBounds(columupX[i], columupY, COLUMN_WIDTH, 200);
			pnlColumdown[i].setBounds(columdownX[i], columupY + pnlColumup[i].getHeight() + gapHeight, COLUMN_WIDTH,
					screenHeight - (columupY + pnlColumup[i].getHeight() + gapHeight));
		}
	}

	private void showMessage(String message) {
		SwingUtilities.invokeLater(() -> {
			messageLabel.setText(message);
			messageLabel.setSize(messageLabel.getPreferredSize()); // Ajusta el tamaño según el texto
			// Calcula las coordenadas para centrar el mensaje en la ventana
			int x = (getWidth() - messageLabel.getWidth()) / 2;
			int y = (getHeight() - messageLabel.getHeight()) / 2;
			messageLabel.setLocation(x, y);
			messageLabel.setVisible(true);
		});
	}

	private void handleCollision() {
		showMessage("¡Has perdido!");
		gameRunning = false;
		btnAtrs.setVisible(true);
		// Aquí puedes añadir más lógica según necesites al detectar la colisión y
		// detener el juego
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					juego_2 frame1 = new juego_2(true);
					frame1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
