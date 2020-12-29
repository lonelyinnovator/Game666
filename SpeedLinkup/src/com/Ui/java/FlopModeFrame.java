/**
 *
 */
package com.Ui.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.configuration.java.MapModel;
import com.configuration.java.ModelFactory;
import com.configuration.java.Theme;
import com.control.java.FlopArchive;
import com.map.java.Lattice;

import java.util.List;

/**
 * @author DELL
 *
 */
public class FlopModeFrame extends JFrame{
	private FlopModePanel flopModePanel;
    private FlopArchive flopArchive;
    private MapModel model;
    private Theme theme;
    private int hour;
    private int minute;
    private int second;
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private int promptnumber;
    private int timeincreasenumber;

    public FlopModeFrame(FlopArchive flopArchive) {
    	super();
        this.flopArchive = flopArchive;
        this.theme = flopArchive.getTheme();
        this.hour = flopArchive.getHour();
        this.minute = flopArchive.getMinute();
        this.second = flopArchive.getSecond();
        this.model = flopArchive.getMapModel();
        this.promptnumber = 3;
        this.timeincreasenumber = 3;
        initFrame();
    }

    private void initFrame() {
    	this.setBackground(Color.WHITE);
    	this.setTitle("连连看");
    	this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    	this.setLocationRelativeTo(getOwner());
    	this.setLayout(null);
    	this.setResizable(false);

    	//主面板
    	flopModePanel = new FlopModePanel(this, flopArchive);

    	//东边面板
        JPanel jPanel_east = new JPanel(new FlowLayout(FlowLayout.LEADING, 50, 70));
        jPanel_east.setPreferredSize(new Dimension(300, 50));
        jPanel_east.setBackground(Color.PINK);

        //暂停面板
        JPanel jPanel_continue_stop = new JPanel(new BorderLayout());
        jPanel_continue_stop.setPreferredSize(new Dimension(200, 50));
        jPanel_continue_stop.setBackground(Color.gray);
        JButton jButton_continue_stop = new JButton("暂停");
        Font f = new Font("等线", Font.BOLD, 18);
        jButton_continue_stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				flopModePanel.thread1.interrupt();
			}
		});
        jButton_continue_stop.setFont(f);
        jPanel_continue_stop.add(jButton_continue_stop, BorderLayout.CENTER);
        jPanel_east.add(jPanel_continue_stop);

        //重新开始面板
        JPanel jPanel_restart = new JPanel(new BorderLayout());
        jPanel_restart.setPreferredSize(new Dimension(200, 50));
        jPanel_restart.setBackground(Color.gray);
        JButton jButton_restart = new JButton("重新开始");
        jButton_restart.setFont(f);
        jButton_restart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
        jPanel_restart.add(jButton_restart, BorderLayout.CENTER);
        jPanel_east.add(jPanel_restart);

        //增加时间面板
        JPanel jPanel_time_increase = new JPanel(new BorderLayout());
        jPanel_time_increase.setPreferredSize(new Dimension(200, 50));
        JButton jButton_time_increase = new JButton("增加时间");
        jButton_time_increase.setFont(new Font("等线", Font.BOLD, 18));
        jButton_time_increase.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				flopModePanel.time += 10;
				if (timeincreasenumber <= 0) {
					jButton_time_increase.setEnabled(false);
				}
				timeincreasenumber--;
			}
		});
        jButton_time_increase.setFont(f);
        jPanel_time_increase.add(jButton_time_increase, BorderLayout.CENTER);
        jPanel_east.add(jPanel_time_increase);

        //提示面板
        JPanel jPanel_prompt = new JPanel(new BorderLayout());
        jPanel_prompt.setPreferredSize(new Dimension(200, 50));
        jPanel_prompt.setBackground(Color.gray);
        JButton jButton_prompt = new JButton("提示");
        jButton_prompt.setFont(f);
        jButton_prompt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (promptnumber > 0) {
					promptnumber--;
					List<Lattice.Point> availableList = flopArchive.getMap().getAvailableLattices();
					Lattice.Point point1 = availableList.get(0);
					Lattice.Point point2 = availableList.get(1);
					Graphics graphics = getGraphics();
					graphics.setColor(Color.green);

					graphics.drawRect(flopModePanel.changeLocation(point1)[0] + 7, flopModePanel.changeLocation(point1)[1] + 30, model.getPictureWidth(), model.getPictureHeight());
					graphics.drawRect(flopModePanel.changeLocation(point2)[0] + 7, flopModePanel.changeLocation(point2)[1] + 30, model.getPictureWidth(), model.getPictureHeight());
				} else {
					jButton_prompt.setEnabled(false);
				}

			}
		});
        jPanel_prompt.add(jButton_prompt, BorderLayout.CENTER);
        jPanel_east.add(jPanel_prompt);



        //主界面
        this.setLayout(new BorderLayout());
        this.add(jPanel_east, BorderLayout.EAST);
        this.add(flopModePanel, BorderLayout.CENTER);
    }


	/**
     * 游戏成功通关后弹出胜利对话框
     *
     * @param owner
     * @param parentComponent
     * @return void
     */
    public void showSuccessDialog(Frame owner, Component parentComponent) {
        // 对话框
        final JDialog dialog = new JDialog(owner, "Success", true);
        dialog.setSize(500, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentComponent);

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
                flopArchive = FlopArchive.newArchive();
                flopModePanel.removeAll();
                flopModePanel.initMainPanel(flopModePanel.flopModeFrame, flopArchive);
                flopModePanel.repaint();
                flopModePanel.revalidate();
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
     * @param owner
     * @param parentComponent
     * @return void
     */
    public void showFailedDialog(FlopModeFrame owner, FlopModeFrame parentComponent) {
        // 对话框
        final JDialog dialog = new JDialog(owner, "Failed", true);
        dialog.setSize(500, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentComponent);

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



}
