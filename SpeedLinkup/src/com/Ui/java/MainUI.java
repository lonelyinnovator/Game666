package com.Ui.java;

import com.configuration.java.ModelFactory;
import com.configuration.java.ThemeFactory;
import com.control.java.TypicalArchive;
import com.map.java.Lattice;
import com.map.java.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The main panel of the game.
 *
 * @author 李云鹏
 * @date 2020/12/29
 * 学号：2019302835
 */
public class MainUI extends JPanel implements MouseMotionListener, MouseListener {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private MainFrame mainFrame;
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    //获取屏幕尺寸

    public MainUI(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        // this.setBackground(Color.blue);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * 绘画主函数
     *
     * @param g
     * @return void
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            drawImage(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制图片
     *
     * @param g
     * @return void
     * @throws IOException
     */
    public void drawImage(Graphics g) throws IOException {
        Graphics2D g2d = (Graphics2D) g.create();
        //BufferedImage bufImage = ImageIO.read(new File(String.format("res/mainUi/instruction.png")));    
        BufferedImage bufImage = ImageIO.read(new File(String.format("res/mainUi/mainUI.jpg")));
        g2d.drawImage(bufImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
        g2d.dispose();
    }

    //进入经典模式
    public void typicalMode() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 创建窗口对象
                TypicalArchive typicalArchive = TypicalArchive.readArchiveInfo();
                if (typicalArchive == null) {
                    typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(2));
                }
                TypicalModeFrame typicalModeFrame = new TypicalModeFrame(typicalArchive);
                typicalModeFrame.setTitle("经典游戏");
                typicalModeFrame.setVisible(true);
            }
        });
    }

    //进入选择继续游戏还是重新开始界面
    public void entry(MainFrame oldMainFrame, int a) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 创建窗口对象
                MainFrame mainFrame = new MainFrame(0, oldMainFrame, a);
                //创建新窗口
                mainFrame.setVisible(true);
            }
        });
    }

    private void instruction() {
        // TODO Auto-generated method stub
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                mainFrame.setTitle("介绍");
                //mainFrame.setBounds((d.width-WINDOW_WIDTH)/2,(d.height-WINDOW_HEIGHT)/2, WINDOW_WIDTH, WINDOW_HEIGHT);
                mainFrame.setLayout(null);//绝对布局
                mainFrame.setResizable(false);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                InstructionUI instructionUI = new InstructionUI(mainFrame);//把自己窗口进行关联。
                mainFrame.setContentPane(instructionUI);
                mainFrame.setVisible(true);
            }
        });
    }

    public void set(MainFrame oldMainFrame, int a) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 创建窗口对象
                MainFrame mainFrame = new MainFrame(1, oldMainFrame, a);
                //创建新窗口
                mainFrame.setVisible(true);
            }
        });
    }

    //--------------------------鼠标监听器-----------------------------------

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println(x + "   " + y);
        //经典模式
        int flag = 0;
        if (x >= 416 && x <= 582 && y >= 173 && y <= 208) {
            flag = 1;
            //关闭原窗口
            entry(mainFrame, flag);
            //进入游戏界面
            System.out.println("1");
        }
        //翻牌模式
        else if (x >= 415 && x <= 585 && y >= 265 && y <= 298) {
            flag = 2;
            entry(mainFrame, flag);
            System.out.println("2");
        }
        //无尽模式
        else if (x >= 415 && x <= 585 && y >= 356 && y <= 385) {
            flag = 3;
            entry(mainFrame, flag);
            System.out.println("3");
        }
        //游戏说明
        else if (x >= 415 && x <= 585 && y >= 444 && y <= 471) {
            System.out.println("4");
            instruction();
            mainFrame.dispose();
        }
        //退出游戏
        else if (x >= 415 && x <= 585 && y >= 535 && y <= 565) {
            mainFrame.dispose();
            System.out.println("5");
            // mainFrame.dispose();
        }
        //设置
        else if (x >= 896 && x <= 975 && y >= 700 && y <= 757) {
            System.out.println("6");
            flag = 4;
            set(mainFrame, flag);
            mainFrame.dispose();
            //mainFrame.dispose();
        }
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}