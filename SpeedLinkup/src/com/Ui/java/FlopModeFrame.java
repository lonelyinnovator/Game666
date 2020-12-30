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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
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
    private int time;
    private int hour;
    private int minute;
    private int second;
    private String countDown;
    private boolean stop_flag = false;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 750;
    private int promptnumber;
    private int timeincreasenumber;
    
    NewPanel jPanel_east;
    NewPanel jPanel_timeremain;
    JPanel jPanel_continue_stop;
    JPanel jPanel_restart;
    JPanel jPanel_time_increase;
    JPanel jPanel_prompt;
    JButton jButton_continue_stop;
    JButton jButton_restart;
    JButton jButton_time_increase;
    JButton jButton_prompt;
    JLabel jLabel_timeremain;
     

    public FlopModeFrame(FlopArchive flopArchive) {
    	super();
        this.flopArchive = flopArchive;
        this.theme = flopArchive.getTheme();
        this.hour = flopArchive.getHour();
        this.minute = flopArchive.getMinute();
        this.second = flopArchive.getSecond();
        this.model = flopArchive.getMapModel();
        this.promptnumber = 10000;
        this.timeincreasenumber = 10000;
        initFrame();
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

    private void initFrame() {
    	this.setBackground(Color.WHITE);
    	this.setTitle("连连看");
    	this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    	this.setLocationRelativeTo(getOwner());
    	this.setLayout(null);
    	this.setResizable(true);

    	//主面板
    	flopModePanel = new FlopModePanel(this, flopArchive);

    	//东边面板
        jPanel_east = new NewPanel("res/ui/east_panel.png");
        jPanel_east.setLayout(new FlowLayout(FlowLayout.LEADING, 57, 40));
        jPanel_east.setPreferredSize(new Dimension(250, 50));
        jPanel_east.setBackground(Color.PINK);
        
      //设置字体
        Font f = new Font("等线", Font.BOLD, 18);

        //暂停面板
        jPanel_continue_stop = new JPanel(new BorderLayout());
        jPanel_continue_stop.setPreferredSize(new Dimension(165, 80));
        jPanel_continue_stop.setBackground(Color.gray);
        jButton_continue_stop = new JButton();
        ImageIcon imageIcon_continue_stop = new ImageIcon("res/ui/stop_label.png");
        jButton_continue_stop.setIcon(imageIcon_continue_stop);
        jButton_continue_stop.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				stop_flag = true;
                showStopDialog();
                stop_flag = false;
			}
		});
        jPanel_continue_stop.add(jButton_continue_stop, BorderLayout.CENTER);
        jPanel_east.add(jPanel_continue_stop);

        //重新开始面板
        jPanel_restart = new JPanel(new BorderLayout());
        jPanel_restart.setPreferredSize(new Dimension(165, 80));
        jButton_restart = new JButton();
        ImageIcon imageIcon_restart = new ImageIcon("res/ui/restart_label.png");
        jButton_restart.setIcon(imageIcon_restart);
        jButton_restart.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
                flopArchive = FlopArchive.newArchive();
                flopModePanel.reDrawPanel(flopArchive);
                flopModePanel.clickedNum = 0;
                time = hour * 60 * 60 + minute * 60 + second;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        jPanel_restart.add(jButton_restart, BorderLayout.CENTER);
        jPanel_east.add(jPanel_restart);

        //增加时间面板
        jPanel_time_increase = new JPanel(new BorderLayout());
        jPanel_time_increase.setPreferredSize(new Dimension(165, 80));
        jButton_time_increase = new JButton();
        ImageIcon imageIcon_time_imcrease = new ImageIcon("res/ui/time_increase_label.png");
        jButton_time_increase.setIcon(imageIcon_time_imcrease);
        jButton_time_increase.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (timeincreasenumber > 0) {
					flopModePanel.time += 10;
					timeincreasenumber--;
				} else {
					jButton_time_increase.setEnabled(false);
				}
			}
		});
        jPanel_time_increase.add(jButton_time_increase, BorderLayout.CENTER);
        jPanel_east.add(jPanel_time_increase);

        //提示面板
        jPanel_prompt = new JPanel(new BorderLayout());
        jPanel_prompt.setPreferredSize(new Dimension(165, 80));
        jButton_prompt = new JButton();
        ImageIcon imageIcon_prompt = new ImageIcon("res/ui/hint_label.png");
        jButton_prompt.setIcon(imageIcon_prompt);
        jButton_prompt.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
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
//        jButton_prompt.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				if (promptnumber > 0) {
//					promptnumber--;
//					List<Lattice.Point> availableList = flopArchive.getMap().getAvailableLattices();
//					Lattice.Point point1 = availableList.get(0);
//					Lattice.Point point2 = availableList.get(1);
//					Graphics graphics = getGraphics();
//					graphics.setColor(Color.green);
//
//					graphics.drawRect(flopModePanel.changeLocation(point1)[0] + 7, flopModePanel.changeLocation(point1)[1] + 30, model.getPictureWidth(), model.getPictureHeight());
//					graphics.drawRect(flopModePanel.changeLocation(point2)[0] + 7, flopModePanel.changeLocation(point2)[1] + 30, model.getPictureWidth(), model.getPictureHeight());
//				} else {
//					jButton_prompt.setEnabled(false);
//				}
//			}
//		});
        jPanel_prompt.add(jButton_prompt, BorderLayout.CENTER);
        jPanel_east.add(jPanel_prompt);
        
        //倒计时面板
        jPanel_timeremain = new NewPanel("res/ui/time_remain_label.png");
        jPanel_timeremain.setPreferredSize(new Dimension(165, 60));
        jPanel_timeremain.setLayout(new FlowLayout(FlowLayout.LEADING, 110, 21));
        jLabel_timeremain = new JLabel(minute + ":" + second);
        jLabel_timeremain.setFont(f);
        jLabel_timeremain.setForeground(Color.RED);
        showCountDown();
        if (flopModePanel.time == 0) {
			showFailedDialog(this, this);
		}
        jPanel_timeremain.add(jLabel_timeremain);
        jPanel_east.add(jPanel_timeremain);

        //主界面
        this.setLayout(new BorderLayout());
        this.add(jPanel_east, BorderLayout.EAST);
        this.add(flopModePanel, BorderLayout.CENTER);
    }


//	/**
//     * 游戏成功通关后弹出胜利对话框
//     *
//     * @param owner
//     * @param parentComponent
//     * @return void
//     */
//    public void showSuccessDialog(Frame owner, Component parentComponent) {
//        // 对话框
//        final JDialog dialog = new JDialog(owner, "Success", true);
//        dialog.setSize(500, 500);
//        dialog.setResizable(false);
//        dialog.setLocationRelativeTo(parentComponent);
//
//        //对话框面板
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 100, 70));
//        panel.setBackground(Color.CYAN);
//
//        //设置字体
//        Font f = new Font("等线", Font.BOLD, 24);
//
//        //游戏成功通关面板
//        JPanel success_panel = new JPanel(new BorderLayout());
//        success_panel.setPreferredSize(new Dimension(300, 50));
//        success_panel.setBackground(Color.PINK);
//        JLabel success_label = new JLabel("Game Success, 666666!", JLabel.CENTER);
//        success_label.setFont(f);
//        success_panel.add(success_label, BorderLayout.CENTER);
//
//        //返回开始界面面板
//        JPanel return_panel = new JPanel(new BorderLayout());
//        return_panel.setPreferredSize(new Dimension(300, 50));
//        JButton return_button = new JButton("返回开始界面");
//        return_button.setFocusPainted(false);
//        return_button.setBackground(Color.RED);
//        return_button.setFont(f);
//        return_button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dialog.dispose();
//            }
//        });
//        return_panel.add(return_button, BorderLayout.CENTER);
//
//        //新游戏界面面板
//        JPanel newGame_panel = new JPanel(new BorderLayout());
//        newGame_panel.setPreferredSize(new Dimension(300, 50));
//        JButton newGame_button = new JButton("开始新游戏");
//        newGame_button.setFocusPainted(false);
//        newGame_button.setBackground(Color.ORANGE);
//        newGame_button.setFont(f);
//        newGame_button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                flopArchive = FlopArchive.newArchive();
//                flopModePanel.removeAll();
//                flopModePanel.initMainPanel(flopModePanel.flopModeFrame, flopArchive);
//                flopModePanel.repaint();
//                flopModePanel.revalidate();
//                dialog.dispose();
//            }
//        });
//        newGame_panel.add(newGame_button, BorderLayout.CENTER);
//        panel.add(success_panel);
//        panel.add(return_panel);
//        panel.add(newGame_panel);
//        dialog.setContentPane(panel);
//        dialog.setVisible(true);
//    }
//
//    /**
//     * 游戏失败后弹出失败对话框
//     *
//     * @param owner
//     * @param parentComponent
//     * @return void
//     */
//    public void showFailedDialog(FlopModeFrame owner, FlopModeFrame parentComponent) {
//        // 对话框
//        final JDialog dialog = new JDialog(owner, "Failed", true);
//        dialog.setSize(500, 500);
//        dialog.setResizable(false);
//        dialog.setLocationRelativeTo(parentComponent);
//
//        //对话框面板
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 100, 70));
//        panel.setBackground(Color.RED);
//
//        //设置字体
//        Font f = new Font("等线", Font.BOLD, 24);
//
//        //游戏成功通关面板
//        JPanel success_panel = new JPanel(new BorderLayout());
//        success_panel.setPreferredSize(new Dimension(300, 50));
//        success_panel.setBackground(Color.ORANGE);
//        JLabel success_label = new JLabel("Game Failed, hhhhhhh!", JLabel.CENTER);
//        success_label.setFont(f);
//        success_panel.add(success_label, BorderLayout.CENTER);
//
//        //返回开始界面面板
//        JPanel return_panel = new JPanel(new BorderLayout());
//        return_panel.setPreferredSize(new Dimension(300, 50));
//        JButton return_button = new JButton("返回开始界面");
//        return_button.setFocusPainted(false);
//        return_button.setBackground(Color.ORANGE);
//        return_button.setFont(f);
//        return_button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dialog.dispose();
//            }
//        });
//        return_panel.add(return_button, BorderLayout.CENTER);
//
//        //新游戏界面面板
//        JPanel newGame_panel = new JPanel(new BorderLayout());
//        newGame_panel.setPreferredSize(new Dimension(300, 50));
//        JButton newGame_button = new JButton("开始新游戏");
//        newGame_button.setFocusPainted(false);
//        newGame_button.setBackground(Color.ORANGE);
//        newGame_button.setFont(f);
//        newGame_button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dialog.dispose();
//            }
//        });
//        newGame_panel.add(newGame_button, BorderLayout.CENTER);
//        panel.add(success_panel);
//        panel.add(return_panel);
//        panel.add(newGame_panel);
//        dialog.setContentPane(panel);
//        dialog.setVisible(true);
//    }
    
    /**
     * 显示倒计时
     *
     * @param
     * @return String
     */
    private void showCountDown() {
        Font f = new Font("等线", Font.BOLD, 18);
        new Thread(() -> {
            while (flopModePanel.time > 0) {
                if (!stop_flag) {
                    flopModePanel.time--;
                }
                try {
                    int mm = flopModePanel.time / 60 % 60;
                    int ss = flopModePanel.time % 60;
                    countDown = mm + ":" + ss;
                    Thread.sleep(1000);
                    jLabel_timeremain.setFont(f);
                    jLabel_timeremain.setText(countDown);
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
    /**
     * 游戏成功通关后弹出胜利对话框
     *
     * @param owner
     * @param parentComponent
     * @return void
     */
    public void showSuccessDialog(Frame owner, Component parentComponent) {
        Graphics g = getGraphics();
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
                flopModePanel.reDrawPanel(flopArchive);
                flopModePanel.clickedNum = 0;
                time = hour * 60 * 60 + minute * 60 + second;
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
     * @param flopModeFrame2
     * @param flopModeFrame3
     * @return void
     */
    public void showFailedDialog(FlopModeFrame flopModeFrame2, FlopModeFrame flopModeFrame3) {
        // 对话框
        final JDialog dialog = new JDialog(flopModeFrame2, "Failed", true);
        dialog.setSize(500, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(flopModeFrame3);

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
