package com.Ui.java;

import com.configuration.java.MapModel;
import com.configuration.java.ModelFactory;
import com.configuration.java.Theme;
import com.control.java.EndlessArchive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	public static final int WINDOW_HEIGHT = 800;
	public int integral = 0;
	public int number = 1;
	public String countDown;
	JLabel jLabel_time_remain = new JLabel();
	private boolean stop_time = false;

	public EndlessModeFrame(EndlessArchive endlessArchive) {
		super();
		this.endlessArchive = endlessArchive;
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
		JPanel jPanel_east = new JPanel(new FlowLayout(FlowLayout.LEADING, 50, 90));
		jPanel_east.setPreferredSize(new Dimension(300, 50));
		jPanel_east.setBackground(Color.PINK);

		// 轮数面板
		JPanel jPanel_number = new JPanel(new BorderLayout());
		jPanel_number.setPreferredSize(new Dimension(200, 30));
		JButton jButton_bumber = new JButton("当前轮数：\n" + this.number);
		jButton_bumber.setFont(f);
		jPanel_number.add(jButton_bumber, BorderLayout.CENTER);
		jPanel_east.add(jPanel_number);

		// 积分面板
		JPanel jPanel_integral = new JPanel(new BorderLayout());
		jPanel_integral.setPreferredSize(new Dimension(200, 30));
		JButton jButton_integral = new JButton("当前积分：\n" + this.integral);
		jButton_integral.setFont(f);
		jPanel_integral.add(jButton_integral, BorderLayout.CENTER);
		jPanel_east.add(jPanel_integral);

		// 剩余时间面板
		JPanel jPanel_time_remain = new JPanel(new BorderLayout());
		jPanel_time_remain.setPreferredSize(new Dimension(200, 50));
		jLabel_time_remain.setFont(f);
		jPanel_time_remain.add(jLabel_time_remain, BorderLayout.CENTER);
		jPanel_east.add(jPanel_time_remain);

		// 暂停游戏面板
		JPanel jPanel_continue_stop = new JPanel(new BorderLayout());
		jPanel_continue_stop.setPreferredSize(new Dimension(200, 50));
		JButton jButton_continue_stop = new JButton("暂停");
		jButton_continue_stop.setFont(f);
		jPanel_continue_stop.add(jButton_continue_stop, BorderLayout.CENTER);
		jPanel_east.add(jPanel_continue_stop);
		// 增加监听器
		jButton_continue_stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stop_time = true;
				stop();
				stop_time = false;
			}
		});

		// 重新开始面板
		JPanel jPanel_restart = new JPanel(new BorderLayout());
		jPanel_restart.setPreferredSize(new Dimension(200, 50));
		JButton jButton_restart = new JButton("重新开始");
		jButton_restart.setFont(f);
		jPanel_restart.add(jButton_restart, BorderLayout.CENTER);
		jPanel_east.add(jPanel_restart);
		jButton_restart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stop_time = true;
				restart();
				stop_time = false;
			}

		});

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
            	if(!stop_time) {
            		endlessModePanel.time--;
            	}
                try {
                    int mm = endlessModePanel.time / 60 % 60;
                    int ss = endlessModePanel.time % 60;
                    countDown = mm + ":" + ss;
                    Thread.sleep(1000);
                    jLabel_time_remain.setFont(f);
                    jLabel_time_remain.setText("剩余时间： " + countDown);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }


	private void stop() {
		// 对话框
		final JDialog dialog = new JDialog(this, "Stop", true);
		dialog.setSize(500, 500);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(this);

		// 对话框面板
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 100, 70));
		panel.setBackground(Color.CYAN);

		// 设置字体
		Font f = new Font("等线", Font.BOLD, 24);

		// 返回开始界面面板
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

		// 继续游戏界面面板
		JPanel continueGame_panel = new JPanel(new BorderLayout());
		continueGame_panel.setPreferredSize(new Dimension(300, 50));
		JButton continueGame_button = new JButton("继续游戏");
		continueGame_button.setFocusPainted(false);
		continueGame_button.setBackground(Color.ORANGE);
		continueGame_button.setFont(f);
		continueGame_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		continueGame_panel.add(continueGame_button, BorderLayout.CENTER);

		panel.add(return_panel);
		panel.add(continueGame_panel);
		dialog.setContentPane(panel);
		dialog.setVisible(true);

	}

	private void restart() {
		// 对话框
		final JDialog dialog = new JDialog(this, "Restart", true);
		dialog.setSize(500, 500);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(this);

		// 对话框面板
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 100, 70));
		panel.setBackground(Color.CYAN);

		// 设置字体
		Font f = new Font("等线", Font.BOLD, 24);

		// 判断 界面面板
		JPanel judge_panel = new JPanel(new BorderLayout());
		judge_panel.setPreferredSize(new Dimension(300, 50));
		JButton judge_button = new JButton("是否重新开始");
		judge_button.setBackground(Color.white);
		judge_button.setFont(f);
		judge_panel.add(judge_button, BorderLayout.CENTER);

		// “是” 界面面板
		JPanel yes_panel = new JPanel(new BorderLayout());
		yes_panel.setPreferredSize(new Dimension(300, 50));
		JButton yes_button = new JButton("是");
		yes_button.setFocusPainted(false);
		yes_button.setBackground(Color.RED);
		yes_button.setFont(f);
		yes_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				re();
				dialog.dispose();

			}
		});
		yes_panel.add(yes_button, BorderLayout.CENTER);

		// “否” 界面面板
		JPanel noGame_panel = new JPanel(new BorderLayout());
		noGame_panel.setPreferredSize(new Dimension(300, 50));
		JButton noGame_button = new JButton("否");
		noGame_button.setFocusPainted(false);
		noGame_button.setBackground(Color.ORANGE);
		noGame_button.setFont(f);
		noGame_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		noGame_panel.add(noGame_button, BorderLayout.CENTER);

		panel.add(judge_panel);
		panel.add(yes_panel);
		panel.add(noGame_panel);
		dialog.setContentPane(panel);
		dialog.setVisible(true);
	}

	private void re() {
		this.dispose();
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                EndlessArchive endlessArchive = EndlessArchive.readArchiveInfo();
                if (endlessArchive == null){
                    endlessArchive = EndlessArchive.newArchive();
                }
                EndlessModeFrame endlessModeFrame = new EndlessModeFrame(endlessArchive);
                endlessModeFrame.setVisible(true);
            }
        });
	}

}