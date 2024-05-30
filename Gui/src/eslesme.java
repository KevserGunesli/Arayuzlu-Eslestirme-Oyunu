import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class eslesme extends javax.swing.JFrame {
     private int satir;
    private int sutun;
    private char[][] tahta;
    private ArrayList<JButton> butonlar;
    private int dogruTahmin;
    private int yanlisTahmin;
    private long baslangic;
    private JButton firstButton;
    private JButton secondButton;
    private JButton thirdButton;
    private boolean isChecking;
    private Timer gameTimer;
    private long elapsedTime;
    private JLabel timerLabel;

    public eslesme(int satir, int sutun) {
         this.satir = satir;
        while (sutun % 3 != 0) {
            String input = JOptionPane.showInputDialog(this, "Sütun sayısı 3 ve 3'ün katı olmalıdır. Lütfen tekrar giriniz:");
            if (input == null) {
                System.exit(0); 
            }
            try {
                sutun = Integer.parseInt(input);
            } catch (NumberFormatException e) {
            }
        }
        this.sutun = sutun;
        butonlar = new ArrayList<>();
        dogruTahmin = 0;
        yanlisTahmin = 0;
        isChecking = false;

        initComponents();
        ElemanYerlestirme();
        createButtons();

        baslangic = System.currentTimeMillis();
        elapsedTime = 0;
        gameTimer = new Timer(1000, new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                elapsedTime = (System.currentTimeMillis() - baslangic) / 1000;
                timerLabel.setText("Geçen Süre: " + elapsedTime + " saniye");
            }
        });
        gameTimer.start();
    }

    private void initComponents() {
         setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Timer label
        timerLabel = new JLabel("Geçen Süre: 0 saniye");
        timerLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 24));
        timerLabel.setBackground(new Color(0, 102, 102));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Timer panel
        JPanel timerPanel = new JPanel(new GridBagLayout());
        timerPanel.setBackground(Color.WHITE);
        timerPanel.setForeground(new Color(0, 102, 102));
        timerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        timerPanel.add(timerLabel, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(satir, sutun, 5, 5));
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        getContentPane().add(buttonPanel, BorderLayout.CENTER);
        getContentPane().add(timerPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

     private void createButtons() {
          JPanel buttonPanel = (JPanel) getContentPane().getComponent(0);
        for (int i = 0; i < satir; i++) {
            for (int j = 0; j < sutun; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Showcard Gothic", Font.PLAIN, 50));
                button.setBackground(new Color(0, 102, 102));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                button.addActionListener(new ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        if (!isChecking) {
                            handleButtonClick(button);
                        }
                    }
                });
                butonlar.add(button);
                buttonPanel.add(button);
            }
        }
        pack();
    }


    private void handleButtonClick(JButton button) {
        if (firstButton == null) {
            firstButton = button;
            revealButton(button);
        } else if (secondButton == null && button != firstButton) {
            secondButton = button;
            revealButton(button);
        } else if (thirdButton == null && button != firstButton && button != secondButton) {
            thirdButton = button;
            revealButton(button);
            checkMatch();
        }
    }

    private void revealButton(JButton button) {
        int index = butonlar.indexOf(button);
        int row = index / sutun;
        int col = index % sutun;
        button.setText(String.valueOf(tahta[row][col]));
    }

    private void checkMatch() {
        isChecking = true;

        int index1 = butonlar.indexOf(firstButton);
        int index2 = butonlar.indexOf(secondButton);
        int index3 = butonlar.indexOf(thirdButton);

        int row1 = index1 / sutun;
        int col1 = index1 % sutun;
        int row2 = index2 / sutun;
        int col2 = index2 % sutun;
        int row3 = index3 / sutun;
        int col3 = index3 % sutun;

        if (tahta[row1][col1] == tahta[row2][col2] && tahta[row2][col2] == tahta[row3][col3]) {
            dogruTahmin += 3;
            firstButton.setEnabled(false);
            secondButton.setEnabled(false);
            thirdButton.setEnabled(false);
            firstButton = null;
            secondButton = null;
            thirdButton = null;
            isChecking = false;
            checkGameEnd();
        } else {
            yanlisTahmin++;
            Timer timer = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hideButtons();
                    firstButton = null;
                    secondButton = null;
                    thirdButton = null;
                    isChecking = false;
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void hideButtons() {
        firstButton.setText("");
        secondButton.setText("");
        thirdButton.setText("");
    }

    private void checkGameEnd() {
        if (dogruTahmin == satir * sutun) {
            gameTimer.stop(); 
            long totalSeconds = elapsedTime;
            int totalScore = Skor();
            JOptionPane.showMessageDialog(this, "Oyun bitti! Toplam puanınız: " + totalScore + "\nGeçen süre: " + totalSeconds + " saniye.");
         }
    }

    private void ElemanYerlestirme() {
        ArrayList<Character> elemanlar = new ArrayList<>();
        for (char c = '0'; c <= '9'; c++) {
            elemanlar.add(c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            elemanlar.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            elemanlar.add(c);
        }
        Collections.shuffle(elemanlar);
        tahta = new char[satir][sutun];
        ArrayList<Character> tempSymbols = new ArrayList<>(elemanlar.subList(0, satir * sutun / 3));

        int bolum = sutun / 3;
        for (char symbol : tempSymbols) {
            for (int i = 0; i < 3; i++) {
                int row, col;
                do {
                    row = (int) (Math.random() * satir);
                    col = (int) (switch (i) {
                        case 0 -> Math.random() * bolum;
                        case 1 -> bolum + (int) (Math.random() * bolum);
                        default -> 2 * bolum + (int) (Math.random() * bolum);
                    });
                } while (tahta[row][col] != '\u0000' || !SutunBosMu(col, symbol));

                tahta[row][col] = symbol;
            }
        }
    }

    private boolean SutunBosMu(int col, char symbol) {
        for (int row = 0; row < satir; row++) {
            if (tahta[row][col] == symbol) {
                return false;
            }
        }
        return true;
    }

    public int Skor() {
        
        int correctGuessScore = dogruTahmin * 10;
        int wrongGuessScore = yanlisTahmin * 5;
        

        return correctGuessScore - wrongGuessScore ;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new eslesme(3, 3).setVisible(true);
        });
    }
}
