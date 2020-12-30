package com.Ui.java;

import com.configuration.java.MapModel;
import com.configuration.java.ModelFactory;
import com.configuration.java.Theme;
import com.control.java.TypicalArchive;
import com.map.java.Lattice;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

/**
 * The main frame of the game.
 *
 * @author 沈佳军
 * @date 2020/12/22
 */
public class TypicalModeFrame extends JFrame {
    private TypicalModePanel typicalModePanel;
    public TypicalArchive typicalArchive;
    //    private MapModel model;
//    private Theme theme;
    public int time;
    private int hour;
    private int minute;
    private int second;
    private String countDown;
    private boolean change_difficulty_flag = false;
    private boolean stop_flag = false;
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 800;
    public boolean musicFlag = true;
    private double musicValue = 1.0;
    public String[] musics = new String[]{"res/music/Battle1.wav"};

    TypicalModeFrame typicalModeFrame = this;

    JPanel jPanel_north;
    NewPanel jPanel_east;
    NewPanel jPanel_time_remain;
    JPanel jPanel_stop;
    JPanel jPanel_restart;
    JPanel jPanel_time_increase;
    JPanel jPanel_hint;
    JPanel jPanel_easy;
    JPanel jPanel_middle;
    JPanel jPanel_hard;
    JLabel jLabel_time_remain;
    JLabel jLabel_stop;
    JLabel jLabel_restart;
    JLabel jLabel_time_increase;
    JLabel jLabel_hint;
    JLabel jLabel_easy;
    JLabel jLabel_middle;
    JLabel jLabel_hard;


    public TypicalModeFrame(TypicalArchive typicalArchive) {
        super();
        new Thread(() -> {
            while (true) {
                playMusic(musics);
            }
        }).start();
        initTypicalArchive(typicalArchive);
        initTypicalModeFrame();
    }

    class NewPanel extends JPanel {
        ImageIcon icon;
        Image image;

        public NewPanel(String filePath) {
            icon = new ImageIcon(filePath);
            image = icon.getImage();
            this.setLayout(new BorderLayout());
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    public void initTypicalArchive(TypicalArchive typicalArchive) {
        this.typicalArchive = typicalArchive;
        this.hour = typicalArchive.getHour();
        this.minute = typicalArchive.getMinute();
        this.second = typicalArchive.getSecond();
        this.time = hour * 60 * 60 + minute * 60 + second;
    }

    private void initTypicalModeFrame() {
        this.setBackground(Color.white);
        this.setTitle("连连看");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(getOwner());
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //主面板
        typicalModePanel = new TypicalModePanel(this);

        //北边面板
        jPanel_north = new JPanel();
        jPanel_north.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        jPanel_north.setPreferredSize(new Dimension(50, 50));

        //东边面板
        jPanel_east = new NewPanel("res/rightUi/east_panel.png");
        jPanel_east.setLayout(new FlowLayout(FlowLayout.LEADING, 40, 80));
        jPanel_east.setPreferredSize(new Dimension(280, 50));
        jPanel_east.setBackground(Color.PINK);


        //设置字体
        Font f = new Font("等线", Font.BOLD, 18);

        //简单难度
        jPanel_easy = new JPanel(new BorderLayout());
        jPanel_easy.setPreferredSize(new Dimension(235, 50));
        jLabel_easy = new JLabel();
        if (typicalArchive.getMapModel().getModelType() == 1) {
            ImageIcon imageIcon_easy_label_selected = new ImageIcon("res/upUi/easy_label_selected.png");
            jLabel_easy.setIcon(imageIcon_easy_label_selected);
        } else {
            ImageIcon imageIcon_easy_label = new ImageIcon("res/upUi/easy_label.png");
            jLabel_easy.setIcon(imageIcon_easy_label);
        }
        jLabel_easy.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                stop_flag = true;
                showChangeDifficultyDialog(typicalModeFrame, typicalModeFrame);
                if (change_difficulty_flag) {
                    ImageIcon imageIcon_easy_label_selected = new ImageIcon("res/upUi/easy_label_selected.png");
                    jLabel_easy.setIcon(imageIcon_easy_label_selected);
                    ImageIcon imageIcon_middle_label = new ImageIcon("res/upUi/middle_label.png");
                    jLabel_middle.setIcon(imageIcon_middle_label);
                    ImageIcon imageIcon_hard_label = new ImageIcon("res/upUi/hard_label.png");
                    jLabel_hard.setIcon(imageIcon_hard_label);
                    TypicalArchive.deleteArchive();
                    typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(1));
                    System.out.println("modelType: " + typicalArchive.getMapModel().getModelType());
                    TypicalArchive.saveArchiveInfo(typicalArchive.getMap(), typicalArchive.getMapModel(),
                            typicalArchive.getTheme(), time);
                    initTypicalArchive(typicalArchive);
                    typicalModePanel.initTypicalModePanel(typicalModeFrame);
                    typicalModePanel.reDrawPanel();
                }
                stop_flag = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        jPanel_easy.add(jLabel_easy, BorderLayout.CENTER);
        jPanel_north.add(jPanel_easy);

        //中等难度
        jPanel_middle = new JPanel(new BorderLayout());
        jPanel_middle.setPreferredSize(new Dimension(238, 50));
        jLabel_middle = new JLabel();
        if (typicalArchive.getMapModel().getModelType() == 2) {
            ImageIcon imageIcon_middle_label_selected = new ImageIcon("res/upUi/middle_label_selected.png");
            jLabel_middle.setIcon(imageIcon_middle_label_selected);
        } else {
            ImageIcon imageIcon_middle_label = new ImageIcon("res/upUi/middle_label.png");
            jLabel_middle.setIcon(imageIcon_middle_label);
        }
        jLabel_middle.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                stop_flag = true;
                showChangeDifficultyDialog(typicalModeFrame, typicalModeFrame);
                if (change_difficulty_flag) {
                    ImageIcon imageIcon_easy_label = new ImageIcon("res/upUi/easy_label.png");
                    jLabel_easy.setIcon(imageIcon_easy_label);
                    ImageIcon imageIcon_middle_label_selected = new ImageIcon("res/upUi/middle_label_selected.png");
                    jLabel_middle.setIcon(imageIcon_middle_label_selected);
                    ImageIcon imageIcon_hard_label = new ImageIcon("res/upUi/hard_label.png");
                    jLabel_hard.setIcon(imageIcon_hard_label);
                    TypicalArchive.deleteArchive();
                    typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(2));
                    initTypicalArchive(typicalArchive);
                    typicalModePanel.initTypicalModePanel(typicalModeFrame);
                    typicalModePanel.reDrawPanel();
                }
                stop_flag = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        jPanel_middle.add(jLabel_middle, BorderLayout.CENTER);
        jPanel_north.add(jPanel_middle);

        //困难难度
        jPanel_hard = new JPanel(new BorderLayout());
        jPanel_hard.setPreferredSize(new Dimension(240, 50));
        jLabel_hard = new JLabel();
        if (typicalArchive.getMapModel().getModelType() == 3) {
            ImageIcon imageIcon_hard_label_selected = new ImageIcon("res/upUi/hard_label_selected.png");
            jLabel_hard.setIcon(imageIcon_hard_label_selected);
        } else {
            ImageIcon imageIcon_hard_label = new ImageIcon("res/upUi/hard_label.png");
            jLabel_hard.setIcon(imageIcon_hard_label);
        }
        jLabel_hard.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                stop_flag = true;
                showChangeDifficultyDialog(typicalModeFrame, typicalModeFrame);
                if (change_difficulty_flag) {
                    TypicalArchive.deleteArchive();
                    typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(3));
                    ImageIcon imageIcon_easy_label = new ImageIcon("res/upUi/easy_label.png");
                    jLabel_easy.setIcon(imageIcon_easy_label);
                    ImageIcon imageIcon_middle_label = new ImageIcon("res/upUi/middle_label.png");
                    jLabel_middle.setIcon(imageIcon_middle_label);
                    ImageIcon imageIcon_hard_label_selected = new ImageIcon("res/upUi/hard_label_selected.png");
                    jLabel_hard.setIcon(imageIcon_hard_label_selected);
                    initTypicalArchive(typicalArchive);
                    typicalModePanel.initTypicalModePanel(typicalModeFrame);
                    typicalModePanel.reDrawPanel();
                }
                stop_flag = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        jPanel_hard.add(jLabel_hard, BorderLayout.CENTER);
        jPanel_north.add(jPanel_hard);

        //倒计时面板
        Font countDown_f = new Font("等线", Font.BOLD, 30);
        jPanel_time_remain = new NewPanel("res/rightUi/show.png");
        jPanel_time_remain.setPreferredSize(new Dimension(278, 50));
        jPanel_time_remain.setLayout(new FlowLayout(FlowLayout.LEADING, 110, 10));
        jLabel_time_remain = new JLabel(this.minute + ":" + this.second);
        jLabel_time_remain.setForeground(Color.RED);
        jLabel_time_remain.setFont(countDown_f);
        showCountDown();
        System.out.println("------------------------");
        System.out.println(this.time);
        new Thread(() -> {
            if (this.time == 0) {
                showFailedDialog();
            }
        }).start();
        jPanel_time_remain.add(jLabel_time_remain);
        jPanel_north.add(jPanel_time_remain);

        //暂停游戏面板
        jPanel_stop = new JPanel(new BorderLayout());
        jPanel_stop.setPreferredSize(new Dimension(200, 79));
        jLabel_stop = new JLabel();
        ImageIcon imageIcon_stop_label = new ImageIcon("res/rightUi/stop_label.png");
        jLabel_stop.setIcon(imageIcon_stop_label);
        jLabel_stop.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                stop_flag = true;
                showStopDialog();
                stop_flag = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        jPanel_stop.add(jLabel_stop, BorderLayout.CENTER);
        jPanel_east.add(jPanel_stop);

        //重新开始面板
        jPanel_restart = new JPanel(new BorderLayout());
        jPanel_restart.setPreferredSize(new Dimension(200, 80));
        jLabel_restart = new JLabel();
        ImageIcon imageIcon_restart_label = new ImageIcon("res/rightUi/restart_label.png");
        jLabel_restart.setIcon(imageIcon_restart_label);
        jLabel_restart.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                stop_flag = true;
                showRestartGameDialog(typicalModeFrame, typicalModeFrame);
                stop_flag = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        jPanel_restart.add(jLabel_restart, BorderLayout.CENTER);
        jPanel_east.add(jPanel_restart);

        //增加时间面板
        jPanel_time_increase = new JPanel(new BorderLayout());
        jPanel_time_increase.setPreferredSize(new Dimension(200, 80));
        jLabel_time_increase = new JLabel();
        ImageIcon imageIcon_time_increase_label = new ImageIcon("res/rightUi/time_increase_label.png");
        jLabel_time_increase.setIcon(imageIcon_time_increase_label);
        jLabel_time_increase.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                time += 10;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        jPanel_time_increase.add(jLabel_time_increase, BorderLayout.CENTER);
        jPanel_east.add(jPanel_time_increase);


        //提示面板
        jPanel_hint = new JPanel(new BorderLayout());
        jPanel_hint.setPreferredSize(new Dimension(200, 80));
        jLabel_hint = new JLabel();
        jPanel_hint.setBackground(Color.PINK);
        ImageIcon imageIcon_hint_label = new ImageIcon("res/rightUi/hint_label.png");
        jLabel_hint.setIcon(imageIcon_hint_label);
        jLabel_hint.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    List<Lattice.Point> pointList = typicalModePanel.map.getAvailableLattices();
                    Lattice.Point point1 = pointList.get(0);
                    Lattice.Point point2 = pointList.get(1);
                    typicalModePanel.clickedNum = 0;
                    typicalModePanel.drawBox(typicalModePanel.changeLocation(point1)[0],
                            typicalModePanel.changeLocation(point1)[1]);
                    typicalModePanel.clickedNum = 0;
                    typicalModePanel.drawBox(typicalModePanel.changeLocation(point2)[0],
                            typicalModePanel.changeLocation(point2)[1]);
                    typicalModePanel.clickedNum = 0;
                    Thread.sleep(500);
                    typicalModePanel.clickedNum = 1;
                    typicalModePanel.removeBox(point1);
                    typicalModePanel.clickedNum = 1;
                    typicalModePanel.removeBox(point2);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        jPanel_hint.add(jLabel_hint, BorderLayout.CENTER);
        jPanel_east.add(jPanel_hint);



        //自动面板
//        JPanel jPanel_auto = new JPanel(new BorderLayout());
//        jPanel_auto.setPreferredSize(new Dimension(150, 50));
//        jPanel_auto.setBackground(Color.GRAY);
//        JButton jButton_auto = new JButton("autoPlay");
//        jButton_auto.setFont(f);
//        jButton_auto.setFocusPainted(false);
//        jButton_auto.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                mainPanel.autoPlay();
//            }
//        });
//        jPanel_auto.add(jButton_auto, BorderLayout.CENTER);
//        jPanel_east.add(jPanel_auto);


        //主界面
        this.setLayout(new BorderLayout());
        this.add(jPanel_north, BorderLayout.NORTH);
        this.add(jPanel_east, BorderLayout.EAST);
        this.add(typicalModePanel, BorderLayout.CENTER);
    }


    /**
     * 显示改变难度的对话框
     *
     * @param
     * @return int
     */
    public void showChangeDifficultyDialog(Frame owner, Component parentComponent) {
//        return JOptionPane.showConfirmDialog(null, "是否放弃当前游戏，进入新游戏？",
//                "ChangeDifficulty", JOptionPane.YES_NO_OPTION);
        // 对话框
        final JDialog dialog = new JDialog(owner, "Success", true);
        dialog.setSize(500, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentComponent);

        //对话框面板
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN);

        //设置字体
        Font f = new Font("等线", Font.BOLD, 24);

        //是否按钮面板
        JPanel if_not_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 120, 0));
        if_not_panel.setPreferredSize(new Dimension(500, 50));
        if_not_panel.setBackground(Color.PINK);
        JButton if_button = new JButton("是");
        if_button.setFocusPainted(false);
        if_button.setBackground(Color.CYAN);
        if_button.setFont(f);
        if_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                change_difficulty_flag = true;
                dialog.dispose();
            }
        });
        if_not_panel.add(if_button);
        JButton not_button = new JButton("否");
        not_button.setFocusPainted(false);
        not_button.setBackground(Color.GRAY);
        not_button.setFont(f);
        not_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                change_difficulty_flag = false;
                dialog.dispose();
            }
        });
        if_not_panel.add(not_button);

        //文字面板
        JPanel text_panel = new JPanel(new BorderLayout());
        text_panel.setPreferredSize(new Dimension(200, 50));
        text_panel.setBackground(Color.PINK);
        JLabel text_label = new JLabel("是否放弃当前游戏，进入新游戏？", JLabel.CENTER);
        text_label.setFont(f);
        text_panel.add(text_label, BorderLayout.CENTER);

        panel.add(if_not_panel, BorderLayout.SOUTH);
        panel.add(text_panel, BorderLayout.CENTER);
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    /**
     * 显示重新开始的对话框
     *
     * @param owner
     * @param parentComponent
     * @return int
     */
    public void showRestartGameDialog(Frame owner, Component parentComponent) {
//        return JOptionPane.showConfirmDialog(null, "是否放弃当前游戏，进入新游戏？",
//                "ChangeDifficulty", JOptionPane.YES_NO_OPTION);
        // 对话框
        final JDialog dialog = new JDialog(owner, "Restart Game", true);
        dialog.setSize(500, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentComponent);

        //对话框面板
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN);

        //设置字体
        Font f = new Font("等线", Font.BOLD, 24);

        //是否按钮面板
        JPanel if_not_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 120, 0));
        if_not_panel.setPreferredSize(new Dimension(500, 50));
        if_not_panel.setBackground(Color.PINK);
        JButton if_button = new JButton("是");
        if_button.setFocusPainted(false);
        if_button.setBackground(Color.CYAN);
        if_button.setFont(f);
        if_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TypicalArchive.deleteArchive();
                typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(3));
                TypicalArchive.saveArchiveInfo(typicalArchive.getMap(), typicalArchive.getMapModel(),
                        typicalArchive.getTheme(), time);
                initTypicalArchive(typicalArchive);
                typicalModePanel.initTypicalModePanel(typicalModeFrame);
                typicalModePanel.reDrawPanel();
                dialog.dispose();
            }
        });
        if_not_panel.add(if_button);
        JButton not_button = new JButton("否");
        not_button.setFocusPainted(false);
        not_button.setBackground(Color.GRAY);
        not_button.setFont(f);
        not_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        if_not_panel.add(not_button);

        //文字面板
        JPanel text_panel = new JPanel(new BorderLayout());
        text_panel.setPreferredSize(new Dimension(200, 50));
        text_panel.setBackground(Color.PINK);
        JLabel text_label = new JLabel("是否重新开始游戏？", JLabel.CENTER);
        text_label.setFont(f);
        text_panel.add(text_label, BorderLayout.CENTER);

        panel.add(if_not_panel, BorderLayout.SOUTH);
        panel.add(text_panel, BorderLayout.CENTER);
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }


    /**
     * 按下暂停键弹出暂停对话框
     *
     * @param
     * @return void
     */
    public void showStopDialog() {
        // 对话框
        final JDialog dialog = new JDialog(this, "StopSettings", true);
        dialog.setSize(500, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        //对话框面板
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 100, 70));
        panel.setBackground(Color.CYAN);

        //设置字体
        Font f = new Font("等线", Font.BOLD, 24);

        //继续游戏面板
        JPanel jPanel_continue = new JPanel(new BorderLayout());
        jPanel_continue.setPreferredSize(new Dimension(300, 50));
        JButton jButton_continue = new JButton("继续游戏");
        jButton_continue.setBackground(Color.PINK);
        jButton_continue.setFont(f);
        jButton_continue.setFocusPainted(false);
        jButton_continue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        jPanel_continue.add(jButton_continue, BorderLayout.CENTER);

        //返回开始界面面板
        JPanel return_panel = new JPanel(new BorderLayout());
        return_panel.setPreferredSize(new Dimension(300, 50));
        JButton return_button = new JButton("返回开始界面");
        return_button.setFocusPainted(false);
        return_button.setBackground(Color.RED);
        return_button.setFont(f);
        return_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                setVisible(false);
            }
        });
        return_panel.add(return_button, BorderLayout.CENTER);

        //退出游戏面板
        JPanel quit_panel = new JPanel(new BorderLayout());
        quit_panel.setPreferredSize(new Dimension(300, 50));
        JButton quit_button = new JButton("退出游戏");
        quit_button.setFocusPainted(false);
        quit_button.setBackground(Color.ORANGE);
        quit_button.setFont(f);
        quit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
//                setVisible(false);
            }
        });
        quit_panel.add(quit_button, BorderLayout.CENTER);
        panel.add(jPanel_continue);
        panel.add(return_panel);
        panel.add(quit_panel);
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    /**
     * 游戏成功通关后弹出胜利对话框
     *
     * @return void
     */
    public void showSuccessDialog() {
        Graphics g = getGraphics();
        // 对话框
        final JDialog dialog = new JDialog(this, "Success", true);
        dialog.setSize(500, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        //对话框面板
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 100, 70));
        panel.setBackground(Color.CYAN);

        //设置字体
        Font f = new Font("等线", Font.BOLD, 24);

        //游戏成功通关面板
        JPanel success_panel = new JPanel(new BorderLayout());
        success_panel.setPreferredSize(new Dimension(300, 50));
        success_panel.setBackground(Color.PINK);
        JLabel success_label = new JLabel("Game Success, 666666!", JLabel.CENTER);
        success_label.setFont(f);
        success_panel.add(success_label, BorderLayout.CENTER);

        //返回开始界面面板
        JPanel return_panel = new JPanel(new BorderLayout());
        return_panel.setPreferredSize(new Dimension(300, 50));
        JButton return_button = new JButton("返回开始界面");
        return_button.setFocusPainted(false);
        return_button.setBackground(Color.RED);
        return_button.setFont(f);
        return_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        return_panel.add(return_button, BorderLayout.CENTER);

        //新游戏界面面板
        JPanel newGame_panel = new JPanel(new BorderLayout());
        newGame_panel.setPreferredSize(new Dimension(300, 50));
        JButton newGame_button = new JButton("开始新游戏");
        newGame_button.setFocusPainted(false);
        newGame_button.setBackground(Color.ORANGE);
        newGame_button.setFont(f);
        newGame_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typicalArchive = TypicalArchive.newArchive(typicalArchive.getMapModel());
                TypicalArchive.saveArchiveInfo(typicalArchive.getMap(), typicalArchive.getMapModel(),
                        typicalArchive.getTheme(), typicalArchive.getHour() * 60 * 60 +
                                typicalArchive.getMinute() * 60 + typicalArchive.getSecond());
                initTypicalArchive(typicalArchive);
                typicalModePanel.initTypicalModePanel(typicalModeFrame);
                typicalModePanel.reDrawPanel();
                dialog.dispose();
            }
        });
        newGame_panel.add(newGame_button, BorderLayout.CENTER);
        panel.add(success_panel);
        panel.add(return_panel);
        panel.add(newGame_panel);
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    /**
     * 游戏失败后弹出失败对话框
     *
     * @return void
     */
    public void showFailedDialog() {
        // 对话框
        final JDialog dialog = new JDialog(this, "Failed", true);
        dialog.setSize(500, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        //对话框面板
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 100, 70));
        panel.setBackground(Color.RED);

        //设置字体
        Font f = new Font("等线", Font.BOLD, 24);

        //游戏成功通关面板
        JPanel success_panel = new JPanel(new BorderLayout());
        success_panel.setPreferredSize(new Dimension(300, 50));
        success_panel.setBackground(Color.ORANGE);
        JLabel success_label = new JLabel("Game Failed, hhhhhhh!", JLabel.CENTER);
        success_label.setFont(f);
        success_panel.add(success_label, BorderLayout.CENTER);

        //返回开始界面面板
        JPanel return_panel = new JPanel(new BorderLayout());
        return_panel.setPreferredSize(new Dimension(300, 50));
        JButton return_button = new JButton("返回开始界面");
        return_button.setFocusPainted(false);
        return_button.setBackground(Color.ORANGE);
        return_button.setFont(f);
        return_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        return_panel.add(return_button, BorderLayout.CENTER);

        //新游戏界面面板
        JPanel newGame_panel = new JPanel(new BorderLayout());
        newGame_panel.setPreferredSize(new Dimension(300, 50));
        JButton newGame_button = new JButton("开始新游戏");
        newGame_button.setFocusPainted(false);
        newGame_button.setBackground(Color.ORANGE);
        newGame_button.setFont(f);
        newGame_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typicalArchive = TypicalArchive.newArchive(typicalArchive.getMapModel());

                initTypicalArchive(typicalArchive);
                typicalModePanel.initTypicalModePanel(typicalModeFrame);
                typicalModePanel.reDrawPanel();
                dialog.dispose();
            }
        });
        newGame_panel.add(newGame_button, BorderLayout.CENTER);
        panel.add(success_panel);
        panel.add(return_panel);
        panel.add(newGame_panel);
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    /**
     * 显示倒计时
     *
     * @param
     * @return String
     */
    private void showCountDown() {
        Font f = new Font("等线", Font.BOLD, 30);
        new Thread(() -> {
            while (this.time > 0) {
                if (!stop_flag) {
                    this.time--;
                }
                try {
                    this.minute = this.time / 60 % 60;
                    this.second = this.time % 60;
                    countDown = this.minute + ":" + this.second;
                    Thread.sleep(1000);
                    jLabel_time_remain.setFont(f);
                    jLabel_time_remain.setText(countDown);
                    TypicalArchive.saveArchiveInfo(typicalArchive.getMap(), typicalArchive.getMapModel(),
                            typicalArchive.getTheme(), time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 播放背景音乐
     *
     * @param
     * @return void
     */
    public void playMusic(String[] musics) {
        try {
            for (String music : musics) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(music));
                AudioFormat aif = ais.getFormat();
                final SourceDataLine sdl;
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
                sdl = (SourceDataLine) AudioSystem.getLine(info);
                sdl.open(aif);
                sdl.start();
                FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
                // musicValue可以用来设置音量，从0-2.0
                float dB = (float) (Math.log(musicValue == 0.0 ? 0.0001 : musicValue) / Math.log(10.0) * 20.0);
                fc.setValue(dB);
                int nByte = 0;
                int writeByte = 0;
                final int SIZE = 1024 * 64;
                byte[] buffer = new byte[SIZE];
                while (nByte != -1) {// 判断 播放/暂停 状态
                    if (musicFlag) {
                        nByte = ais.read(buffer, 0, SIZE);
                        sdl.write(buffer, 0, nByte);
                    } else {
                        nByte = ais.read(buffer, 0, 0);
                    }
                }
                sdl.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
