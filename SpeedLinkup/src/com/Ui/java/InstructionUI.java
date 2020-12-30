package com.Ui.java;

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
public class InstructionUI extends JPanel implements MouseMotionListener, MouseListener {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private MainFrame mainFrame;

    public InstructionUI(MainFrame mainFrame) {
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
        BufferedImage bufImage = ImageIO.read(new File(String.format("res/mainUi/游戏说明.png")));
        g2d.drawImage(bufImage,0,0,WINDOW_WIDTH,WINDOW_HEIGHT,this);
        g2d.dispose();
    }

    
    private void zhuyemian() {
		// TODO Auto-generated method stub
    	EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 创建窗口对象
                 mainFrame.setTitle("连连看");
                 //this.setBounds((d.width-WINDOW_WIDTH)/2,(d.height-WINDOW_HEIGHT)/2, WINDOW_WIDTH, WINDOW_HEIGHT);
                 mainFrame.setLayout(null);//绝对布局
                 mainFrame.setResizable(false);
                 mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                 MainUI mainUI = new MainUI(mainFrame);//把自己窗口进行关联。
                 mainFrame.setContentPane(mainUI);
                //主页面的构造函数
                // 显示窗口
                mainFrame.setVisible(true);
            }
        });
	}

    //--------------------------鼠标监听器-----------------------------------

    @Override
    public void mouseClicked(MouseEvent e) {
    	int x = e.getX();
        int y = e.getY();
        System.out.println(x + "   " +y);
        if (x>=876&&x<=965&&y>=689&&y<=731)
        {
        	mainFrame.dispose();
        	zhuyemian();
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