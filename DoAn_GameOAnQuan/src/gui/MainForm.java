/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.sun.source.tree.BreakTree;
import pojo.Music;
import dao.MusicDAO;
import dao.NguoiDungDAO;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Kim Phuong
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    NguoiDungDAO nddao = new NguoiDungDAO();
    frmdangnhap dn;
    boolean raiDaFinish = false;
    private Clip currentClip;
    ArrayList<Music> danhSachNhac;
    int soquan1 = 0;
    int soquan2 = 0;
    static int soquan1temp = 0;
    Object[] option = {"<-", "->", "Hủy"};
    boolean newgame, endgame, happening = false;
    boolean luot1 = false;
    boolean luot2 = true;
    boolean gameIsOver = false;
    private int[] gameBoard;
    int currentImageIndex = 0;
    String[] imagePaths = {"/gui/1.png", "/gui/2.png", "/gui/3.png"};

    public MainForm() {
        initComponents();
        setLocationRelativeTo(null);
        hienThiDanhSachNhac();
        btnEndGame.setEnabled(false);
        lblSoQuan1.setText("Số quân: " + soquan1);
        lblSoQuan2.setText("Số quân: " + soquan2);
        lblLuotNguoi1.setVisible(false);
        lblLuotNguoi2.setVisible(false);
        lblSoQuan1.setVisible(false);
        lblSoQuan2.setVisible(false);
        lblTen1.setVisible(false);
        lblTen2.setVisible(false);
        initializeGameBoard();
        initButton();
        initImage();

    }

    private void initImage() {
        URL imageUrl = getClass().getResource(imagePaths[currentImageIndex]);
        if (imageUrl == null) {
            System.out.println("Không tìm thấy: " + imagePaths[currentImageIndex]);
        } else {
            ImageIcon icon = new ImageIcon(imageUrl);
            lblImage.setIcon(icon);
        }
        lblImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentImageIndex = (currentImageIndex + 1) % imagePaths.length;
                lblImage.setIcon(new ImageIcon(getClass().getResource(imagePaths[currentImageIndex])));
            }
        });
    }

    private void initButton() {
        if (happening) {
            btnO1.setEnabled(true);
            btnO2.setEnabled(true);
            btnO3.setEnabled(true);
            btnO4.setEnabled(true);
            btnO5.setEnabled(true);
            btnO6.setEnabled(true);
            btnO7.setEnabled(true);
            btnO8.setEnabled(true);
            btnO9.setEnabled(true);
            btnO10.setEnabled(true);
            btnQuan1.setEnabled(true);
            btnQuan2.setEnabled(true);
        } else {
            btnO1.setEnabled(false);
            btnO2.setEnabled(false);
            btnO3.setEnabled(false);
            btnO4.setEnabled(false);
            btnO5.setEnabled(false);
            btnO6.setEnabled(false);
            btnO7.setEnabled(false);
            btnO8.setEnabled(false);
            btnO9.setEnabled(false);
            btnO10.setEnabled(false);
            btnQuan1.setEnabled(false);
            btnQuan2.setEnabled(false);
        }
    }

    private void initializeGameBoard() {
        gameBoard = new int[12];
        Arrays.fill(gameBoard, 5);
        gameBoard[0] = gameBoard[6] = 1;
        updateBoardDisplay();
    }

    private int getDa(int index) {
        return gameBoard[index];
    }

    private void setDa(int index, int stones) {
        gameBoard[index] = stones;
        JButton button = getButtonAtIndex(index);
        button.setText(String.valueOf(stones));
    }

    private JButton getButtonAtIndex(int index) {
        return switch (index) {
            case 0 ->
                btnQuan1;
            case 1 ->
                btnO1;
            case 2 ->
                btnO2;
            case 3 ->
                btnO3;
            case 4 ->
                btnO4;
            case 5 ->
                btnO5;
            case 6 ->
                btnQuan2;
            case 7 ->
                btnO6;
            case 8 ->
                btnO7;
            case 9 ->
                btnO8;
            case 10 ->
                btnO9;
            case 11 ->
                btnO10;
            default ->
                null;
        };
    }

    private void updateBoardDisplay() {

        for (int i = 0; i < gameBoard.length; i++) {

            JButton button = getButtonAtIndex(i);
            if (button != null) {
                button.setText(String.valueOf(gameBoard[i]));
            }

        }
        if (newgame == true) {
            if (radNguoivsMay.isSelected()) {
                soquan1 += soquan1temp;
            }

            if (luot1 == true) {
                lblLuotNguoi1.setVisible(true);
                lblLuotNguoi2.setVisible(false);
            } else if (luot2 == true) {
                lblLuotNguoi1.setVisible(false);
                lblLuotNguoi2.setVisible(true);
            }

            lblSoQuan1.setText("Số quân: " + soquan1);
            lblSoQuan2.setText("Số quân: " + soquan2);
        }
        initButton();

    }

    private void anLienTuc(int vt, String huongDi) {

        while (getDa(vt) == 0) {
            int vtTiepTheoNua = viTriTiepTheo(vt, huongDi);

            if (getDa(vtTiepTheoNua) > 0) {
                if (luot2) {
                    soquan2 += getDa(vtTiepTheoNua);
                } else {
                    soquan1 += getDa(vtTiepTheoNua);
                }
                setDa(vtTiepTheoNua, 0);
            } else {

                break;
            }
            vt = viTriTiepTheo(vtTiepTheoNua, huongDi);
        }

    }

    private void raiDa(int vtBatDau, int stones, String huongDi) {
        if (happening) {
            System.out.println("Human move: " + vtBatDau + " huongDi: " + huongDi);
            setDa(vtBatDau, 0);
            int vtHienTai = vtBatDau;

            while (stones > 0) {
                vtHienTai = viTriTiepTheo(vtHienTai, huongDi);
                setDa(vtHienTai, getDa(vtHienTai) + 1);
                stones--;

                if (stones == 0) {
                    vtHienTai = viTriTiepTheo(vtHienTai, huongDi);
                    if (getDa(vtHienTai) == 0) {
                        anLienTuc(vtHienTai, huongDi);
                        if (luot2) {
                            luot2 = false;
                            luot1 = true;
                        } else {
                            luot2 = true;
                            luot1 = false;
                        }

                    } else if (vtHienTai == 0 || vtHienTai == 6) {
                        if (luot2) {
                            luot2 = false;
                            luot1 = true;
                        } else {
                            luot2 = true;
                            luot1 = false;
                        }
                        break;
                    } else {
                        stones = getDa(vtHienTai);
                        setDa(vtHienTai, 0);
                    }
                    anQuanHet();
                }
            }
            raiDaFinish = true;
            if (raiDaFinish == true && radNguoivsMay.isSelected()) {
                playGame();
            }
            updateBoardDisplay();
            checkWin();
        }
    }

    private void anQuanHet() {
        if (getDa(0) == 0 && getDa(6) == 0) {
            for (int i = 1; i <= 5; i++) {
                soquan1 += getDa(i);
                setDa(i, 0);
            }
            for (int i = 7; i <= 11; i++) {
                soquan2 += getDa(i);
                setDa(i, 0);
            }
        }
    }

    private int viTriTiepTheo(int vtHienTai, String huongDi) {

        if (huongDi.equals("left")) {
            if (vtHienTai == 11) {
                return 0;

            } else {
                return vtHienTai + 1;
            }

        }
        if (huongDi.equals("right")) {
            if (vtHienTai == 0) {
                return 11;
            } else {
                return vtHienTai - 1;
            }
        }

        return vtHienTai;

    }

    private void checkWin() {
        if (radNguoivsNguoi.isSelected()) {
            int sum1 = 0;
            int sum2 = 0;
            for (int i = 1; i <= 5; i++) {
                sum1 += gameBoard[i];
            }

            for (int j = 7; j <= 11; j++) {
                sum2 += gameBoard[j];
            }

            if (sum1 == 0 || sum2 == 0 || gameBoard[0] == 0 && gameBoard[6] == 0) {
                if (soquan1 > soquan2) {
                    JOptionPane.showMessageDialog(this, "NGƯỜI CHƠI 1 THẮNG\nTỉ số quân: " + soquan1 + ":" + soquan2);
                } else if (soquan1 < soquan2) {
                    JOptionPane.showMessageDialog(this, "NGƯỜI CHƠI 2 THẮNG\nTỉ số quân: " + soquan1 + ":" + soquan2);
                    String username = dn.username;
                    if (username != null) {
                        nddao.capNhatDiem(username, 1);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "HÒA\nTỉ số quân: " + soquan1 + ":" + soquan2);
                }
                happening = false;
            } else {
                happening = true;
            }
        } else if (radNguoivsMay.isSelected()) {
            int sum1 = 0;
            int sum2 = 0;
            for (int i = 1; i <= 5; i++) {
                sum1 += gameBoard[i];
            }

            for (int j = 7; j <= 11; j++) {
                sum2 += gameBoard[j];
            }

            if (sum1 == 0 || sum2 == 0 || gameBoard[0] == 0 && gameBoard[6] == 0) {
                if (soquan1 > soquan2) {
                    JOptionPane.showMessageDialog(this, "MÁY THẮNG\nTỉ số quân: " + soquan1 + ":" + soquan2);
                } else if (soquan1 < soquan2) {
                    JOptionPane.showMessageDialog(this, "NGƯỜI CHƠI THẮNG\nTỉ số quân: " + soquan1 + ":" + soquan2);

                    String username = dn.username;
                    if (username != null) {

                        nddao.capNhatDiem(username, 1);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "HÒA\nTỉ số quân: " + soquan1 + ":" + soquan2);
                }
                happening = false;
                gameIsOver = true;
            } else {
                happening = true;
            }
        }
    }

    private boolean gameIsOver() {
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 1; i <= 5; i++) {
            sum1 += gameBoard[i];
        }

        for (int j = 7; j <= 11; j++) {
            sum2 += gameBoard[j];
        }
        return (sum1 == 0 || sum2 == 0 || gameBoard[0] == 0 && gameBoard[6] == 0);
    }

    private int evaluateBoard(int[] gameBoard) {
        int scorePlayer1 = 0;
        int scorePlayer2 = 0;

        for (int i = 0; i <= 5; i++) {
            scorePlayer1 += gameBoard[i];
        }

        for (int i = 6; i <= 11; i++) {
            scorePlayer2 += gameBoard[i];
        }

        return scorePlayer1 - scorePlayer2;
    }

    private boolean isValidMove(int vtBatDau) {
        return (vtBatDau > 0 && vtBatDau < 6 && gameBoard[vtBatDau] > 0 && vtBatDau != 0 && vtBatDau != 6);
    }

    private int viTriTiepTheoAI(int vtHienTai, String huongDi) {
        if (huongDi.equals("right")) {
            vtHienTai = (vtHienTai + 1) % gameBoard.length;
        } else {
            vtHienTai = (vtHienTai - 1 + gameBoard.length) % gameBoard.length;
        }
        return vtHienTai;
    }

    private void makeMove(int[] gameBoard, int vtBatDau, String huongDi) {
        int stones = gameBoard[vtBatDau];
        gameBoard[vtBatDau] = 0;
        int vtHienTai = vtBatDau;
        int tongQuan = 0;
        while (stones > 0) {
            vtHienTai = viTriTiepTheoAI(vtHienTai, huongDi);
            gameBoard[vtHienTai]++;
            stones--;
            if (stones == 0) {
                int vtTiepTheo = viTriTiepTheoAI(vtHienTai, huongDi);
                if (vtTiepTheo != 0 && vtTiepTheo != 6) {
                    int vtTiepTheoNua = viTriTiepTheoAI(vtTiepTheo, huongDi);
                    if (gameBoard[vtTiepTheo] == 0) {
                        tongQuan += gameBoard[vtTiepTheoNua];
                        gameBoard[vtTiepTheoNua] = 0;
                    } else if (gameBoard[vtTiepTheo] > 0) {
                        vtHienTai = vtTiepTheo;
                        stones = gameBoard[vtHienTai];
                        gameBoard[vtHienTai] = 0;
                    }
                } else {
                    break;
                }
            }

        }
        soquan1temp = tongQuan;

    }

    private int minimax(int[] gameBoard, int depth, boolean maximizingPlayer, int alpha, int beta, String huongDi) {
        if (depth == 0 || gameIsOver()) {
            return evaluateBoard(gameBoard);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i <= 11; i++) {
                if (isValidMove(i)) {
                    for (String direction : new String[]{"left", "right"}) {
                        int[] tempBoard = Arrays.copyOf(gameBoard, gameBoard.length);
                        makeMove(tempBoard, i, direction);
                        int eval = minimax(tempBoard, depth - 1, false, alpha, beta, direction);
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i <= 11; i++) {
                if (isValidMove(i)) {
                    for (String direction : new String[]{"left", "right"}) {
                        int[] tempBoard = Arrays.copyOf(gameBoard, gameBoard.length);
                        makeMove(tempBoard, i, direction);
                        int eval = minimax(tempBoard, depth - 1, true, alpha, beta, direction);
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return minEval;
        }
    }

    private int[] findBestMove() {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        String bestHuongDi = "right";
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int depth = 3;

        for (int i = 0; i <= 11; i++) {
            if (isValidMove(i)) {
                for (String huongDi : new String[]{"left", "right"}) {
                    int[] tempBoard = Arrays.copyOf(gameBoard, gameBoard.length);
                    makeMove(tempBoard, i, huongDi);
                    int score = minimax(tempBoard, depth, false, alpha, beta, huongDi);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = i;
                        bestHuongDi = huongDi;
                    }
                }
            }
        }
        return new int[]{bestMove, bestHuongDi.equals("right") ? 1 : 0};
    }

    public void playGame() {
        while (!gameIsOver()) {
            if (luot1) {
                int[] aiMoveDirection = findBestMove();
                int aiMove = aiMoveDirection[0];
                String aiDirection = aiMoveDirection[1] == 1 ? "right" : "left";
                System.out.println("AI: " + aiMove + " huongDi: " + aiDirection);
                makeMove(gameBoard, aiMove, aiDirection);

                luot1 = false;
                luot2 = true;

                updateBoardDisplay();

                if (gameIsOver()) {
                    break;
                }

            } else if (luot1 == false) {
                setupButtonListeners();
                return;
            }
        }

        System.out.println("Game over!");
        if (gameIsOver()) {
            System.out.println("Game over!");
        } else {
            setupButtonListeners();
        }
    }

    private void setupButtonListeners() {
        for (JButton button : new JButton[]{btnO6, btnO7, btnO8, btnO9, btnO10}) {
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }
        }

        if (luot2) {
            btnO6.addActionListener(evt -> handleButtonAction(7, btnO6));
            btnO7.addActionListener(evt -> handleButtonAction(8, btnO7));
            btnO8.addActionListener(evt -> handleButtonAction(9, btnO8));
            btnO9.addActionListener(evt -> handleButtonAction(10, btnO9));
            btnO10.addActionListener(evt -> handleButtonAction(11, btnO10));
        }
    }

    private void handleButtonAction(int vtBatDau, JButton button) {
        if (luot2) {
            int stones = Integer.parseInt(button.getText());
            if (stones > 0) {
                String huongDi = "";
                int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
                switch (userChoice) {
                    case JOptionPane.YES_OPTION ->
                        huongDi = "left";
                    case JOptionPane.NO_OPTION ->
                        huongDi = "right";
                    case JOptionPane.CANCEL_OPTION -> {
                    }
                }
                raiDa(vtBatDau, stones, huongDi);
                updateBoardDisplay();
                checkWin();
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
        }
    }

    private void hienThiDanhSachNhac() {
        danhSachNhac = MusicDAO.layDanhSachNhac();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-------------------");
        for (Music music : danhSachNhac) {
            model.addElement(music.getTenBaiHat());
        }
        cbMusic.setModel(model);
    }

    private void stopCurrentMusic() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        pnMenu = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        pnComponent = new javax.swing.JPanel();
        btnEndGame = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cbMusic = new javax.swing.JComboBox<>();
        btnDangXuat = new javax.swing.JButton();
        btnProfile = new javax.swing.JButton();
        btnNewGame2 = new javax.swing.JButton();
        pnHead = new javax.swing.JPanel();
        pnCheDo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        radNguoivsNguoi = new javax.swing.JRadioButton();
        radNguoivsMay = new javax.swing.JRadioButton();
        pnTitle = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pnMain = new javax.swing.JPanel();
        btnQuan1 = new javax.swing.JButton();
        btnO1 = new javax.swing.JButton();
        btnO6 = new javax.swing.JButton();
        btnO2 = new javax.swing.JButton();
        btnO7 = new javax.swing.JButton();
        btnO3 = new javax.swing.JButton();
        btnO8 = new javax.swing.JButton();
        btnO4 = new javax.swing.JButton();
        btnO10 = new javax.swing.JButton();
        btnO5 = new javax.swing.JButton();
        btnO9 = new javax.swing.JButton();
        btnQuan2 = new javax.swing.JButton();
        pnNguoi2 = new javax.swing.JPanel();
        lblTen1 = new javax.swing.JLabel();
        lblSoQuan1 = new javax.swing.JLabel();
        lblLuotNguoi1 = new javax.swing.JLabel();
        pnNguoi3 = new javax.swing.JPanel();
        lblTen2 = new javax.swing.JLabel();
        lblLuotNguoi2 = new javax.swing.JLabel();
        lblSoQuan2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ô ĂN QUAN");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnMenu.setBackground(new java.awt.Color(239, 239, 255));

        lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/1.png"))); // NOI18N

        pnComponent.setBackground(new java.awt.Color(239, 239, 255));
        pnComponent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEndGame.setBackground(new java.awt.Color(51, 204, 0));
        btnEndGame.setFont(new java.awt.Font("Cascadia Code", 1, 14)); // NOI18N
        btnEndGame.setForeground(new java.awt.Color(255, 255, 255));
        btnEndGame.setText("END GAME");
        btnEndGame.setBorder(null);
        btnEndGame.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEndGame.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnEndGame.setPreferredSize(new java.awt.Dimension(140, 26));
        btnEndGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndGameActionPerformed(evt);
            }
        });
        pnComponent.add(btnEndGame, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 150, 30));

        jLabel3.setFont(new java.awt.Font("Cascadia Code", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Music");
        pnComponent.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 110, 20));

        cbMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMusicActionPerformed(evt);
            }
        });
        pnComponent.add(cbMusic, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 110, -1));

        btnDangXuat.setBackground(new java.awt.Color(255, 71, 71));
        btnDangXuat.setFont(new java.awt.Font("Cascadia Code", 1, 14)); // NOI18N
        btnDangXuat.setForeground(new java.awt.Color(255, 255, 255));
        btnDangXuat.setText("ĐĂNG XUẤT");
        btnDangXuat.setBorder(null);
        btnDangXuat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDangXuat.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnDangXuat.setPreferredSize(new java.awt.Dimension(140, 26));
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });
        pnComponent.add(btnDangXuat, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 150, 30));

        btnProfile.setBackground(new java.awt.Color(153, 153, 255));
        btnProfile.setFont(new java.awt.Font("Cascadia Code", 1, 14)); // NOI18N
        btnProfile.setForeground(new java.awt.Color(255, 255, 255));
        btnProfile.setText("PROFILE");
        btnProfile.setBorder(null);
        btnProfile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnProfile.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnProfile.setPreferredSize(new java.awt.Dimension(140, 26));
        btnProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfileActionPerformed(evt);
            }
        });
        pnComponent.add(btnProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 150, 30));

        btnNewGame2.setBackground(new java.awt.Color(102, 153, 255));
        btnNewGame2.setFont(new java.awt.Font("Cascadia Code", 1, 14)); // NOI18N
        btnNewGame2.setForeground(new java.awt.Color(255, 255, 255));
        btnNewGame2.setText("NEW GAME");
        btnNewGame2.setBorder(null);
        btnNewGame2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewGame2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnNewGame2.setPreferredSize(new java.awt.Dimension(140, 26));
        btnNewGame2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGame2ActionPerformed(evt);
            }
        });
        pnComponent.add(btnNewGame2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 150, 30));

        javax.swing.GroupLayout pnMenuLayout = new javax.swing.GroupLayout(pnMenu);
        pnMenu.setLayout(pnMenuLayout);
        pnMenuLayout.setHorizontalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(pnComponent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnMenuLayout.setVerticalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnComponent, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(pnMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 520));

        pnHead.setLayout(new java.awt.BorderLayout());

        pnCheDo.setBackground(new java.awt.Color(239, 239, 255));
        pnCheDo.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Cascadia Code", 0, 14)); // NOI18N
        jLabel1.setText("Chọn chế độ:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 60);
        pnCheDo.add(jLabel1, gridBagConstraints);

        buttonGroup1.add(radNguoivsNguoi);
        radNguoivsNguoi.setFont(new java.awt.Font("Cascadia Code", 0, 14)); // NOI18N
        radNguoivsNguoi.setText("Người vs Người");
        radNguoivsNguoi.setMargin(new java.awt.Insets(2, 10, 3, 2));
        radNguoivsNguoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                radNguoivsNguoiMousePressed(evt);
            }
        });
        radNguoivsNguoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radNguoivsNguoiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, -50, 0, 10);
        pnCheDo.add(radNguoivsNguoi, gridBagConstraints);

        buttonGroup1.add(radNguoivsMay);
        radNguoivsMay.setFont(new java.awt.Font("Cascadia Code", 0, 14)); // NOI18N
        radNguoivsMay.setText("Người vs Máy");
        radNguoivsMay.setMargin(new java.awt.Insets(2, 10, 3, 2));
        radNguoivsMay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                radNguoivsMayMousePressed(evt);
            }
        });
        radNguoivsMay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radNguoivsMayActionPerformed(evt);
            }
        });
        pnCheDo.add(radNguoivsMay, new java.awt.GridBagConstraints());

        pnHead.add(pnCheDo, java.awt.BorderLayout.CENTER);

        pnTitle.setBackground(new java.awt.Color(239, 239, 255));

        jLabel2.setFont(new java.awt.Font("Courier New", 1, 36)); // NOI18N
        jLabel2.setText("Ô ĂN QUAN");
        pnTitle.add(jLabel2);

        pnHead.add(pnTitle, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(pnHead, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 650, 110));

        pnMain.setBackground(new java.awt.Color(239, 239, 255));
        pnMain.setLayout(new java.awt.GridBagLayout());

        btnQuan1.setBackground(new java.awt.Color(204, 255, 204));
        btnQuan1.setFont(new java.awt.Font("Cascadia Code", 0, 24)); // NOI18N
        btnQuan1.setText("1");
        btnQuan1.setActionCommand("2");
        btnQuan1.setBorder(null);
        btnQuan1.setPreferredSize(new java.awt.Dimension(80, 160));
        btnQuan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuan1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        pnMain.add(btnQuan1, gridBagConstraints);

        btnO1.setBackground(new java.awt.Color(204, 255, 204));
        btnO1.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO1.setText("5");
        btnO1.setActionCommand("2");
        btnO1.setBorder(null);
        btnO1.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        pnMain.add(btnO1, gridBagConstraints);

        btnO6.setBackground(new java.awt.Color(204, 255, 204));
        btnO6.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO6.setText("5");
        btnO6.setActionCommand("2");
        btnO6.setBorder(null);
        btnO6.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        pnMain.add(btnO6, gridBagConstraints);

        btnO2.setBackground(new java.awt.Color(204, 255, 204));
        btnO2.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO2.setText("5");
        btnO2.setActionCommand("2");
        btnO2.setBorder(null);
        btnO2.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        pnMain.add(btnO2, gridBagConstraints);

        btnO7.setBackground(new java.awt.Color(204, 255, 204));
        btnO7.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO7.setText("5");
        btnO7.setActionCommand("2");
        btnO7.setBorder(null);
        btnO7.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        pnMain.add(btnO7, gridBagConstraints);

        btnO3.setBackground(new java.awt.Color(204, 255, 204));
        btnO3.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO3.setText("5");
        btnO3.setActionCommand("2");
        btnO3.setBorder(null);
        btnO3.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        pnMain.add(btnO3, gridBagConstraints);

        btnO8.setBackground(new java.awt.Color(204, 255, 204));
        btnO8.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO8.setText("5");
        btnO8.setActionCommand("2");
        btnO8.setBorder(null);
        btnO8.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        pnMain.add(btnO8, gridBagConstraints);

        btnO4.setBackground(new java.awt.Color(204, 255, 204));
        btnO4.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO4.setText("5");
        btnO4.setActionCommand("2");
        btnO4.setBorder(null);
        btnO4.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        pnMain.add(btnO4, gridBagConstraints);

        btnO10.setBackground(new java.awt.Color(204, 255, 204));
        btnO10.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO10.setText("5");
        btnO10.setActionCommand("2");
        btnO10.setBorder(null);
        btnO10.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO10ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        pnMain.add(btnO10, gridBagConstraints);

        btnO5.setBackground(new java.awt.Color(204, 255, 204));
        btnO5.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO5.setText("5");
        btnO5.setActionCommand("2");
        btnO5.setBorder(null);
        btnO5.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        pnMain.add(btnO5, gridBagConstraints);

        btnO9.setBackground(new java.awt.Color(204, 255, 204));
        btnO9.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        btnO9.setText("5");
        btnO9.setActionCommand("2");
        btnO9.setBorder(null);
        btnO9.setPreferredSize(new java.awt.Dimension(80, 80));
        btnO9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnO9ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        pnMain.add(btnO9, gridBagConstraints);

        btnQuan2.setBackground(new java.awt.Color(204, 255, 204));
        btnQuan2.setFont(new java.awt.Font("Cascadia Code", 0, 24)); // NOI18N
        btnQuan2.setText("1");
        btnQuan2.setActionCommand("2");
        btnQuan2.setBorder(null);
        btnQuan2.setPreferredSize(new java.awt.Dimension(80, 160));
        btnQuan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuan2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        pnMain.add(btnQuan2, gridBagConstraints);

        pnNguoi2.setBackground(new java.awt.Color(239, 239, 255));
        pnNguoi2.setPreferredSize(new java.awt.Dimension(560, 80));

        lblTen1.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        lblTen1.setText("NGƯỜI");

        lblSoQuan1.setFont(new java.awt.Font("Cascadia Code", 1, 18)); // NOI18N
        lblSoQuan1.setText("70");

        lblLuotNguoi1.setFont(new java.awt.Font("Cascadia Code", 2, 18)); // NOI18N
        lblLuotNguoi1.setText("LƯỢT CỦA BẠN");

        javax.swing.GroupLayout pnNguoi2Layout = new javax.swing.GroupLayout(pnNguoi2);
        pnNguoi2.setLayout(pnNguoi2Layout);
        pnNguoi2Layout.setHorizontalGroup(
            pnNguoi2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNguoi2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTen1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSoQuan1)
                .addGap(20, 20, 20))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnNguoi2Layout.createSequentialGroup()
                .addContainerGap(199, Short.MAX_VALUE)
                .addComponent(lblLuotNguoi1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(162, 162, 162))
        );
        pnNguoi2Layout.setVerticalGroup(
            pnNguoi2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNguoi2Layout.createSequentialGroup()
                .addGap(0, 18, Short.MAX_VALUE)
                .addComponent(lblLuotNguoi1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnNguoi2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTen1)
                    .addComponent(lblSoQuan1))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 4;
        pnMain.add(pnNguoi2, gridBagConstraints);

        pnNguoi3.setBackground(new java.awt.Color(239, 239, 255));
        pnNguoi3.setPreferredSize(new java.awt.Dimension(560, 80));

        lblTen2.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        lblTen2.setText("NGƯỜI");

        lblLuotNguoi2.setFont(new java.awt.Font("Cascadia Code", 2, 18)); // NOI18N
        lblLuotNguoi2.setText("LƯỢT CỦA BẠN");

        lblSoQuan2.setFont(new java.awt.Font("Cascadia Code", 1, 18)); // NOI18N
        lblSoQuan2.setText("70");

        javax.swing.GroupLayout pnNguoi3Layout = new javax.swing.GroupLayout(pnNguoi3);
        pnNguoi3.setLayout(pnNguoi3Layout);
        pnNguoi3Layout.setHorizontalGroup(
            pnNguoi3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNguoi3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTen2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSoQuan2)
                .addGap(15, 15, 15))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnNguoi3Layout.createSequentialGroup()
                .addContainerGap(198, Short.MAX_VALUE)
                .addComponent(lblLuotNguoi2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(187, 187, 187))
        );
        pnNguoi3Layout.setVerticalGroup(
            pnNguoi3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNguoi3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pnNguoi3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTen2)
                    .addComponent(lblSoQuan2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLuotNguoi2)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 4;
        pnMain.add(pnNguoi3, gridBagConstraints);

        getContentPane().add(pnMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 650, 410));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMusicActionPerformed
        // TODO add your handling code here:
        stopCurrentMusic();
        String selectedSong = (String) cbMusic.getSelectedItem();
        Music music = danhSachNhac.stream()
                .filter(m -> m.getTenBaiHat().equals(selectedSong))
                .findFirst().orElse(null);
        if (music != null) {
            try {
                File musicFile = new File(music.getDuongDanNhac());
                if (musicFile.exists()) {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    currentClip = clip;
                } else {
                    System.out.println("Không tìm thấy");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }//GEN-LAST:event_cbMusicActionPerformed

    private void btnEndGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndGameActionPerformed
        // TODO add your handling code here:
        endgame = true;
        newgame = false;
        happening = true;
        radNguoivsMay.setSelected(false);
        radNguoivsNguoi.setSelected(false);
        btnEndGame.setEnabled(false);
        btnProfile.setEnabled(true);
        radNguoivsMay.setEnabled(true);
        radNguoivsNguoi.setEnabled(true);
        lblLuotNguoi1.setVisible(false);
        lblLuotNguoi2.setVisible(false);
        lblSoQuan1.setVisible(false);
        lblSoQuan2.setVisible(false);
        lblTen1.setVisible(false);
        lblTen2.setVisible(false);

        btnO1.setEnabled(false);
        btnO2.setEnabled(false);
        btnO3.setEnabled(false);
        btnO4.setEnabled(false);
        btnO5.setEnabled(false);
        btnO6.setEnabled(false);
        btnO7.setEnabled(false);
        btnO8.setEnabled(false);
        btnO9.setEnabled(false);
        btnO10.setEnabled(false);
        btnQuan1.setEnabled(false);
        btnQuan2.setEnabled(false);
    }//GEN-LAST:event_btnEndGameActionPerformed

    private void btnO9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO9ActionPerformed
        // TODO add your handling code here:
        if (luot2 == true) {
            int stones = Integer.parseInt(btnO9.getText());
            int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
            String huongDi = "";
            if (userChoice == JOptionPane.YES_OPTION) {
                huongDi = "left";
                raiDa(10, stones, huongDi);
            } else if (userChoice == JOptionPane.NO_OPTION) {
                huongDi = "right";
                raiDa(10, stones, huongDi);
            } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
        }

    }//GEN-LAST:event_btnO9ActionPerformed

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        frmdangnhap dn = new frmdangnhap();
        dn.setVisible(true);
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void radNguoivsMayMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radNguoivsMayMousePressed
        // TODO add your handling code here:
        lblLuotNguoi1.setText("LƯỢT CỦA MÁY");
        lblLuotNguoi2.setText("LƯỢT CỦA BẠN"); //Mốt có đăng nhập sẽ đổi BẠN thành tên
        lblTen1.setText("MÁY");
        lblTen2.setText("NGƯỜI");

    }//GEN-LAST:event_radNguoivsMayMousePressed

    private void btnO1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO1ActionPerformed
        // TODO add your handling code here:
        if (radNguoivsNguoi.isSelected()) {
            if (luot2 == false) {
                int stones = Integer.parseInt(btnO1.getText());

                int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
                String huongDi = "";
                if (userChoice == JOptionPane.YES_OPTION) {
                    huongDi = "right";
                    raiDa(1, stones, huongDi);
                } else if (userChoice == JOptionPane.NO_OPTION) {
                    huongDi = "left";
                    raiDa(1, stones, huongDi);
                } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
            }
        }


    }//GEN-LAST:event_btnO1ActionPerformed

    private void btnO2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO2ActionPerformed
        // TODO add your handling code here:
        if (radNguoivsNguoi.isSelected()) {
            if (luot2 == false) {
                int stones = Integer.parseInt(btnO2.getText());

                int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
                String huongDi = "";
                if (userChoice == JOptionPane.YES_OPTION) {
                    huongDi = "right";
                    raiDa(2, stones, huongDi);
                } else if (userChoice == JOptionPane.NO_OPTION) {
                    huongDi = "left";
                    raiDa(2, stones, huongDi);
                } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
            }
        }
    }//GEN-LAST:event_btnO2ActionPerformed

    private void btnO3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO3ActionPerformed
        // TODO add your handling code here:
        if (radNguoivsNguoi.isSelected()) {
            if (luot2 == false) {
                int stones = Integer.parseInt(btnO3.getText());

                int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
                String huongDi = "";
                if (userChoice == JOptionPane.YES_OPTION) {
                    huongDi = "right";
                    raiDa(3, stones, huongDi);
                } else if (userChoice == JOptionPane.NO_OPTION) {
                    huongDi = "left";
                    raiDa(3, stones, huongDi);
                } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
            }
        }
    }//GEN-LAST:event_btnO3ActionPerformed

    private void btnO4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO4ActionPerformed
        // TODO add your handling code here:
        if (radNguoivsNguoi.isSelected()) {
            if (luot2 == false) {
                int stones = Integer.parseInt(btnO4.getText());

                int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
                String huongDi = "";
                if (userChoice == JOptionPane.YES_OPTION) {
                    huongDi = "right";
                    raiDa(4, stones, huongDi);
                } else if (userChoice == JOptionPane.NO_OPTION) {
                    huongDi = "left";
                    raiDa(4, stones, huongDi);
                } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
            }
        }
    }//GEN-LAST:event_btnO4ActionPerformed

    private void btnO5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO5ActionPerformed
        // TODO add your handling code here:
        if (radNguoivsNguoi.isSelected()) {
            if (luot2 == false) {
                int stones = Integer.parseInt(btnO5.getText());

                int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
                String huongDi = "";
                if (userChoice == JOptionPane.YES_OPTION) {
                    huongDi = "right";
                    raiDa(5, stones, huongDi);
                } else if (userChoice == JOptionPane.NO_OPTION) {
                    huongDi = "left";
                    raiDa(5, stones, huongDi);
                } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
            }
        }

    }//GEN-LAST:event_btnO5ActionPerformed

    private void btnO6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO6ActionPerformed
        // TODO add your handling code here:

        if (luot2 == true) {
            int stones = Integer.parseInt(btnO6.getText());
            int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
            String huongDi = "";
            if (userChoice == JOptionPane.YES_OPTION) {
                huongDi = "left";
                raiDa(7, stones, huongDi);
            } else if (userChoice == JOptionPane.NO_OPTION) {
                huongDi = "right";
                raiDa(7, stones, huongDi);
            } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");

        }


    }//GEN-LAST:event_btnO6ActionPerformed

    private void btnO7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO7ActionPerformed
        // TODO add your handling code here:
        if (luot2 == true) {
            int stones = Integer.parseInt(btnO7.getText());
            int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
            String huongDi = "";
            if (userChoice == JOptionPane.YES_OPTION) {
                huongDi = "left";
                raiDa(8, stones, huongDi);
            } else if (userChoice == JOptionPane.NO_OPTION) {
                huongDi = "right";
                raiDa(8, stones, huongDi);
            } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
        }
    }//GEN-LAST:event_btnO7ActionPerformed

    private void btnO8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO8ActionPerformed
        // TODO add your handling code here:
        if (luot2 == true) {
            int stones = Integer.parseInt(btnO8.getText());
            int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
            String huongDi = "";
            if (userChoice == JOptionPane.YES_OPTION) {
                huongDi = "left";
                raiDa(9, stones, huongDi);
            } else if (userChoice == JOptionPane.NO_OPTION) {
                huongDi = "right";
                raiDa(9, stones, huongDi);
            } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
        }
    }//GEN-LAST:event_btnO8ActionPerformed

    private void btnO10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnO10ActionPerformed
        // TODO add your handling code here:
        if (luot2 == true) {

            int stones = Integer.parseInt(btnO10.getText());
            int userChoice = JOptionPane.showOptionDialog(this, "Bạn muốn đi hướng nào?", "Chọn hướng đi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[2]);
            String huongDi = "";
            if (userChoice == JOptionPane.YES_OPTION) {
                huongDi = "left";
                raiDa(11, stones, huongDi);
            } else if (userChoice == JOptionPane.NO_OPTION) {
                huongDi = "right";
                raiDa(11, stones, huongDi);
            } else if (userChoice == JOptionPane.CANCEL_OPTION) {
                return;
            }

        } else {
            JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
        }

    }//GEN-LAST:event_btnO10ActionPerformed

    private void radNguoivsMayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radNguoivsMayActionPerformed
        // TODO add your handling code here:
        lblLuotNguoi1.setText("LƯỢT CỦA MÁY");
        lblLuotNguoi2.setText("LƯỢT CỦA BẠN");
        lblTen1.setText("MÁY");
        lblTen2.setText("NGƯỜI 2");

    }//GEN-LAST:event_radNguoivsMayActionPerformed

    private void radNguoivsNguoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radNguoivsNguoiActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_radNguoivsNguoiActionPerformed

    private void radNguoivsNguoiMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radNguoivsNguoiMousePressed
        // TODO add your handling code here:
        lblLuotNguoi1.setText("LƯỢT CỦA BẠN");
        lblLuotNguoi2.setText("LƯỢT CỦA BẠN");
        lblTen1.setText("NGƯỜI 1");
        lblTen2.setText("NGƯỜI 2");
    }//GEN-LAST:event_radNguoivsNguoiMousePressed

    private void btnProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfileActionPerformed
        // TODO add your handling code here:
        Profile pf = new Profile();
        pf.setVisible(true);
        this.setVisible(false);


    }//GEN-LAST:event_btnProfileActionPerformed

    private void btnQuan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuan1ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
    }//GEN-LAST:event_btnQuan1ActionPerformed

    private void btnQuan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuan2ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "Bạn không được chọn ô này");
    }//GEN-LAST:event_btnQuan2ActionPerformed

    private void btnNewGame2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGame2ActionPerformed
        // TODO add your handling code here:
        soquan1 = soquan2 = 0;
        if (radNguoivsNguoi.isSelected()) {
            lblLuotNguoi1.setVisible(false);
            lblLuotNguoi2.setVisible(true);
            newgame = true;
            endgame = false;
            soquan1 = soquan2 = 0;
            btnEndGame.setEnabled(true);
            btnProfile.setEnabled(false);
            radNguoivsMay.setEnabled(false);
            radNguoivsNguoi.setEnabled(false);
            lblTen1.setVisible(true);
            lblTen2.setVisible(true);
            lblSoQuan1.setVisible(true);
            lblSoQuan2.setVisible(true);
            happening = true;
            luot2 = true;
            luot1 = false;
        } else if (radNguoivsMay.isSelected()) {
            lblLuotNguoi1.setVisible(false);
            lblLuotNguoi2.setVisible(true);
            newgame = true;
            endgame = false;
            btnEndGame.setEnabled(true);
            btnProfile.setEnabled(false);
            radNguoivsMay.setEnabled(false);
            radNguoivsNguoi.setEnabled(false);
            lblTen1.setVisible(true);
            lblTen2.setVisible(true);
            lblSoQuan1.setVisible(true);
            lblSoQuan2.setVisible(true);
            happening = true;
            luot2 = true;
            luot1 = false;
        } else {
            JOptionPane.showMessageDialog(this, "Bạn phải chọn chế độ chơi", "Error", JOptionPane.OK_OPTION);
            newgame = false;
            return;
        }

        initializeGameBoard();
    }//GEN-LAST:event_btnNewGame2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnEndGame;
    private javax.swing.JButton btnNewGame2;
    private javax.swing.JButton btnO1;
    private javax.swing.JButton btnO10;
    private javax.swing.JButton btnO2;
    private javax.swing.JButton btnO3;
    private javax.swing.JButton btnO4;
    private javax.swing.JButton btnO5;
    private javax.swing.JButton btnO6;
    private javax.swing.JButton btnO7;
    private javax.swing.JButton btnO8;
    private javax.swing.JButton btnO9;
    private javax.swing.JButton btnProfile;
    private javax.swing.JButton btnQuan1;
    private javax.swing.JButton btnQuan2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbMusic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblLuotNguoi1;
    private javax.swing.JLabel lblLuotNguoi2;
    private javax.swing.JLabel lblSoQuan1;
    private javax.swing.JLabel lblSoQuan2;
    private javax.swing.JLabel lblTen1;
    private javax.swing.JLabel lblTen2;
    private javax.swing.JPanel pnCheDo;
    private javax.swing.JPanel pnComponent;
    private javax.swing.JPanel pnHead;
    private javax.swing.JPanel pnMain;
    private javax.swing.JPanel pnMenu;
    private javax.swing.JPanel pnNguoi2;
    private javax.swing.JPanel pnNguoi3;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JRadioButton radNguoivsMay;
    private javax.swing.JRadioButton radNguoivsNguoi;
    // End of variables declaration//GEN-END:variables
}
