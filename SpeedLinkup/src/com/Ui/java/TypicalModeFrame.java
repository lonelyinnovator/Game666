package com.Ui.java;

import com.configuration.java.MapModel;
import com.configuration.java.ModelFactory;
import com.configuration.java.Theme;
import com.configuration.java.ThemeFactory;
import com.control.java.TypicalArchive;
import com.map.java.Lattice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * The main frame of the game.
 *
 * @author 沈佳军
 * @date 2020/12/22
 */
public class TypicalModeFrame extends JFrame {
    private TypicalModePanel typicalModePanel;
    private TypicalArchive typicalArchive;
    private MapModel model;
    private Theme theme;
    private int time;
    private int hour;
    private int minute;
    private int second;
    private String countDown;
    private boolean change_difficulty_flag = false;
    private boolean stop_flag = false;
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 800;

    NewPanel jPanel_north;
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
        this.typicalArchive = typicalArchive;
        this.model = typicalArchive.getMapModel();
        this.theme = typicalArchive.getTheme();
        this.hour = typicalArchive.getHour();
        this.minute = typicalArchive.getMinute();
        this.second = typicalArchive.getSecond();
        this.time = hour * 60 * 60 + minute * 60 + second;
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

    private void initTypicalModeFrame() {
        this.setBackground(Color.white);
        this.setTitle("连连看");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(getOwner());
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //主面板
        typicalModePanel = new TypicalModePanel(this, typicalArchive);

        //北边面板
        jPanel_north = new NewPanel("res/ui/north_panel.png");
        jPanel_north.setLayout(new FlowLayout(FlowLayout.LEADING, 80, 0));
        jPanel_north.setPreferredSize(new Dimension(50, 60));

        //东边面板
        jPanel_east = new NewPanel("res/ui/east_panel.png");
        jPanel_east.setLayout(new FlowLayout(FlowLayout.LEADING, 57, 40));
        jPanel_east.setPreferredSize(new Dimension(280, 50));
        jPanel_east.setBackground(Color.PINK);


        //设置字体
        Font f = new Font("等线", Font.BOLD, 18);

        //简单难度
        jPanel_easy = new JPanel(new BorderLayout());
        jPanel_easy.setPreferredSize(new Dimension(180, 60));
        jLabel_easy = new JLabel();
        if (model.getModelType() == 1) {
            jLabel_easy.setText("当前难度");
        } else {
            jLabel_easy.setText("简单难度");
        }
        ImageIcon imageIcon_easy_label = new ImageIcon("res/ui/easy_label.png");
        jLabel_easy.setIcon(imageIcon_easy_label);
        TypicalModeFrame typicalModeFrame = this;
        jLabel_easy.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showChangeDifficultyDialog(typicalModeFrame, typicalModeFrame);
                if (change_difficulty_flag) {
                    TypicalArchive.deleteArchive();
                    typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(1));
                    typicalModePanel.reDrawPanel(typicalArchive);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ImageIcon imageIcon_easy_label_selected = new ImageIcon("res/ui/easy_label_selected.png");
                jLabel_easy.setIcon(imageIcon_easy_label_selected);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ImageIcon imageIcon_easy_label = new ImageIcon("res/ui/easy_label.png");
                jLabel_easy.setIcon(imageIcon_easy_label);
            }
        });
        jPanel_easy.add(jLabel_easy, BorderLayout.CENTER);
        jPanel_north.add(jPanel_easy);

        //中等难度
        jPanel_middle = new JPanel(new BorderLayout());
        jPanel_middle.setPreferredSize(new Dimension(183, 60));
        jLabel_middle = new JLabel();
        ImageIcon imageIcon_middle_label = new ImageIcon("res/ui/middle_label.png");
        jLabel_middle.setIcon(imageIcon_middle_label);
        if (model.getModelType() == 2) {
            jLabel_middle.setText("当前难度");
        } else {
            jLabel_middle.setText("中等难度");
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
                showChangeDifficultyDialog(typicalModeFrame, typicalModeFrame);
                if (change_difficulty_flag) {
                    TypicalArchive.deleteArchive();
                    typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(2));
                    typicalModePanel.reDrawPanel(typicalArchive);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ImageIcon imageIcon_middle_label_selected = new ImageIcon("res/ui/middle_label_selected.png");
                jLabel_middle.setIcon(imageIcon_middle_label_selected);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ImageIcon imageIcon_middle_label = new ImageIcon("res/ui/middle_label.png");
                jLabel_middle.setIcon(imageIcon_middle_label);
            }
        });
        jPanel_middle.add(jLabel_middle, BorderLayout.CENTER);
        jPanel_north.add(jPanel_middle);

        //困难难度
        jPanel_hard = new JPanel(new BorderLayout());
        jPanel_hard.setPreferredSize(new Dimension(183, 60));
        jLabel_hard = new JLabel();
        if (model.getModelType() == 3) {
            jLabel_hard.setText("当前难度");
        } else {
            jLabel_hard.setText("困难难度");
            jPanel_hard.add(jLabel_hard, BorderLayout.CENTER);
        }
        ImageIcon imageIcon_hard_label = new ImageIcon("res/ui/hard_label.png");
        jLabel_hard.setIcon(imageIcon_hard_label);
        jLabel_hard.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showChangeDifficultyDialog(typicalModeFrame, typicalModeFrame);
                if (change_difficulty_flag) {
                    TypicalArchive.deleteArchive();
                    typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(3));
                    typicalModePanel.reDrawPanel(typicalArchive);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ImageIcon imageIcon_hard_label_selected = new ImageIcon("res/ui/hard_label_selected.png");
                jLabel_hard.setIcon(imageIcon_hard_label_selected);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ImageIcon imageIcon_hard_label = new ImageIcon("res/ui/hard_label.png");
                jLabel_hard.setIcon(imageIcon_hard_label);
            }
        });
        jPanel_hard.add(jLabel_hard, BorderLayout.CENTER);
        jPanel_north.add(jPanel_hard);

        //暂停游戏面板
        jPanel_stop = new JPanel(new BorderLayout());
        jPanel_stop.setPreferredSize(new Dimension(165, 79));
        jLabel_stop = new JLabel();
        ImageIcon imageIcon_stop_label = new ImageIcon("res/ui/stop_label.png");
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
        jPanel_restart.setPreferredSize(new Dimension(165, 80));
        jLabel_restart = new JLabel();
        ImageIcon imageIcon_restart_label = new ImageIcon("res/ui/restart_label.png");
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
        jPanel_time_increase.setPreferredSize(new Dimension(165, 80));
        jLabel_time_increase = new JLabel();
        ImageIcon imageIcon_time_increase_label = new ImageIcon("res/ui/time_increase_label.png");
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
                typicalModePanel.time += 10;
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
        jPanel_hint.setPreferredSize(new Dimension(165, 80));
        jLabel_hint = new JLabel();
        ImageIcon imageIcon_hint_label = new ImageIcon("res/ui/hint_label.png");
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
                ImageIcon imageIcon_hint_label_selected = new ImageIcon("res/ui/hint_label_selected.png");
                jLabel_hint.setIcon(imageIcon_hint_label_selected);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ImageIcon imageIcon_hint_label = new ImageIcon("res/ui/hint_label.png");
                jLabel_hint.setIcon(imageIcon_hint_label);
            }
        });
        jPanel_hint.add(jLabel_hint, BorderLayout.CENTER);
        jPanel_east.add(jPanel_hint);

        //倒计时面板
        jPanel_time_remain = new NewPanel("res/ui/time_remain_label.png");
        jPanel_time_remain.setPreferredSize(new Dimension(165, 60));
        jPanel_time_remain.setLayout(new FlowLayout(FlowLayout.LEADING, 110, 21));
        jLabel_time_remain = new JLabel(minute + ":" + second);
        jLabel_time_remain.setForeground(Color.RED);
        jLabel_time_remain.setFont(f);
        showCountDown();
        if (typicalModePanel.time == 0){
            typicalModePanel.showFailedDialog(this, this);
        }
        jPanel_time_remain.add(jLabel_time_remain);
        jPanel_east.add(jPanel_time_remain);

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
     * 显示倒计时
     *
     * @param
     * @return String
     */
    private void showCountDown() {
        Font f = new Font("等线", Font.BOLD, 18);
        new Thread(() -> {
            while (typicalModePanel.time > 0) {
                if (!stop_flag) {
                    typicalModePanel.time--;
                }
                try {
                    int mm = typicalModePanel.time / 60 % 60;
                    int ss = typicalModePanel.time % 60;
                    countDown = mm + ":" + ss;
                    Thread.sleep(1000);
                    jLabel_time_remain.setFont(f);
                    jLabel_time_remain.setText(countDown);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 按下暂停键弹出暂停对话框
     *
     * @param
     * @return void
     */
    public void showStopDialog() {
        Graphics g = getGraphics();
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
}
