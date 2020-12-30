package com.Ui.java;

import com.configuration.java.MapModel;
import com.configuration.java.ModelFactory;
import com.configuration.java.Theme;
import com.control.java.EndlessArchive;
import com.control.java.TypicalArchive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The main frame of the game.
 *
 * @author 沈佳军
 * @date 2020/12/22
 */
public class EndlessModeFrame extends JFrame {
    private EndlessModePanel endlessModePanel;
    private EndlessArchive endlessArchive;
    private MapModel model;
    private Theme theme;
    private int hour;
    private int minute;
    private int second;
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 750;
    public int integral = 0;
    public int number = 1;
    public String countDown;
    private boolean stop_flag = false;
    private EndlessModeFrame endlessModeFrame = this;

    JPanel jPanel_east;
    JPanel jPanel_number;
    JPanel jPanel_integral;
    JPanel jPanel_time_remain;
    JPanel jPanel_continue_stop;
    JPanel jPanel_restart;
    JLabel jLabel_time_remain;
    JLabel jLabel_number;
    JLabel jLabel_integral;
    JLabel jLabel_continue_stop;
    JLabel jLabel_restart;


    public EndlessModeFrame(EndlessArchive endlessArchive) {
        super();
        this.endlessArchive = endlessArchive;
        this.number = endlessArchive.getRoundNum();
        this.integral = endlessArchive.getScore();
        this.model = endlessArchive.getMapModel();
        this.theme = endlessArchive.getTheme();
        this.hour = endlessArchive.getHour();
        this.minute = endlessArchive.getMinute();
        this.second = endlessArchive.getSecond();
        initFrame();
    }

    private void initFrame() {
        this.setBackground(Color.white);
        this.setTitle("连连看");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(getOwner());
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endlessModePanel = new EndlessModePanel(this, endlessArchive);

        // 主面板
        // 设置字体
        Font f = new Font("等线", Font.BOLD, 18);

        // 东边面板
        // 对齐方式：从开始位置 水平间隙 垂直间隙
        jPanel_east = new JPanel(new FlowLayout(FlowLayout.LEADING, 57, 40));
        jPanel_east.setPreferredSize(new Dimension(244, 50));
        jPanel_east.setBackground(Color.PINK);

        // 轮数面板
        jPanel_number = new JPanel(new BorderLayout());
        jPanel_number.setPreferredSize(new Dimension(165, 80));
        jLabel_number = new JLabel("当前轮数：" + this.number);
        jLabel_number.setFont(f);
        jPanel_number.add(jLabel_number, BorderLayout.CENTER);
        jPanel_east.add(jPanel_number);

        // 积分面板
        jPanel_integral = new JPanel(new BorderLayout());
        jPanel_integral.setPreferredSize(new Dimension(165, 80));
        jLabel_integral = new JLabel("当前积分：" + this.integral);
        jLabel_integral.setFont(f);
        jPanel_integral.add(jLabel_integral, BorderLayout.CENTER);
        jPanel_east.add(jPanel_integral);

        // 剩余时间面板
        jPanel_time_remain = new JPanel(new BorderLayout());
        jPanel_time_remain.setPreferredSize(new Dimension(165, 80));
        jLabel_time_remain = new JLabel("剩余时间： " + endlessModePanel.minute + ":" + endlessModePanel.second);
        jLabel_time_remain.setFont(f);
        jPanel_time_remain.add(jLabel_time_remain, BorderLayout.CENTER);
        jPanel_east.add(jPanel_time_remain);

        // 暂停游戏面板
        jPanel_continue_stop = new JPanel(new BorderLayout());
        jPanel_continue_stop.setPreferredSize(new Dimension(165, 80));
        jLabel_continue_stop = new JLabel("暂停");
        jLabel_continue_stop.setFont(f);
        jLabel_continue_stop.addMouseListener(new MouseListener() {
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
        jPanel_continue_stop.add(jLabel_continue_stop, BorderLayout.CENTER);
        jPanel_east.add(jPanel_continue_stop);


        // 重新开始面板
        jPanel_restart = new JPanel(new BorderLayout());
        jPanel_restart.setPreferredSize(new Dimension(165, 80));
        jLabel_restart = new JLabel("重新开始");
        jLabel_restart.setFont(f);
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
                showRestartGameDialog();
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
        this.setLayout(new BorderLayout());
        this.add(jPanel_east, BorderLayout.EAST);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.add(endlessModePanel, BorderLayout.CENTER);
        showCountDown();
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
            while (endlessModePanel.time > 0) {
                if (!stop_flag) {
                    endlessModePanel.time--;
                }
                try {
                    endlessModePanel.minute = endlessModePanel.time / 60 % 60;
                    endlessModePanel.second = endlessModePanel.time % 60;
                    countDown = endlessModePanel.minute + ":" + endlessModePanel.second;
                    Thread.sleep(1000);
                    jLabel_time_remain.setFont(f);
                    jLabel_time_remain.setText("剩余时间： " + countDown);
                    EndlessArchive.saveArchiveInfo(endlessModePanel.map, endlessModePanel.model, endlessModePanel.theme,
                            endlessModePanel.hour, endlessModePanel.minute, endlessModePanel.second,
                            this.integral, this.number);
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
     * 显示重新开始的对话框
     *
     * @param
     * @param
     * @return int
     */
    public void showRestartGameDialog() {
//        return JOptionPane.showConfirmDialog(null, "是否放弃当前游戏，进入新游戏？",
//                "ChangeDifficulty", JOptionPane.YES_NO_OPTION);
        // 对话框
        final JDialog dialog = new JDialog(this, "Restart Game", true);
        dialog.setSize(500, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

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
                EndlessArchive.deleteArchive();
                endlessArchive = EndlessArchive.newArchive();
                endlessModePanel.initEndlessModePanel(endlessModeFrame, endlessArchive);
                endlessModePanel.hour = endlessArchive.getHour();
                endlessModePanel.minute = endlessArchive.getMinute();
                endlessModePanel.second = endlessArchive.getSecond();
                endlessModePanel.time = endlessModePanel.hour * 60 * 60 + endlessModePanel.minute * 60 +
                        endlessModePanel.second;
                number = endlessArchive.getRoundNum();
                integral = endlessArchive.getScore();
                jLabel_number.setText("当前轮数：" + number);
                jLabel_integral.setText("当前积分：" + "");
                jLabel_time_remain.setText("剩余时间：" + endlessModePanel.minute + ":" + endlessModePanel.second);
                endlessModePanel.reDrawPanel(endlessArchive);
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
}