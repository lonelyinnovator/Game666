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
public class ChoiceUI extends JPanel implements MouseMotionListener, MouseListener {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 400;
    private MainFrame mainFrame;
    private MainFrame oldMainFrame;
    int a;
    public ChoiceUI(MainFrame mainFrame,MainFrame oldMainFrame,int a) {
        super();
        this.mainFrame = mainFrame;
        this.oldMainFrame=oldMainFrame;
        this.a=a;
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
        BufferedImage bufImage = ImageIO.read(new File(String.format("res/mainUi/choice.jpg")));
        g2d.drawImage(bufImage,0,0,WINDOW_WIDTH,WINDOW_HEIGHT,this);
        g2d.dispose();
    }

    
    public void entryNewTypicalMode() {
    	EventQueue.invokeLater(new Runnable() {
            @Override
                 public void run() {
                     // 创建窗口对象
                      TypicalArchive typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(2));
                      TypicalModeFrame typicalModeFrame = new TypicalModeFrame(typicalArchive);
                      typicalModeFrame.setTitle("经典游戏");
                      typicalModeFrame.setVisible(true);
                 }
             });
    }
    public void entryGoOnTypicalMode() {
    	EventQueue.invokeLater(new Runnable() {
            @Override
                 public void run() {
                     // 创建窗口对象
                 	 TypicalArchive typicalArchive = TypicalArchive.readArchiveInfo();
                      if (typicalArchive == null){
                          typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(2));
                      }
                      TypicalModeFrame typicalModeFrame = new TypicalModeFrame(typicalArchive);
                      typicalModeFrame.setTitle("经典游戏");
                      typicalModeFrame.setVisible(true);
                 }
             });
    }
    public void entryNewEndlessMode() {

    }
    public void entryGoOnEndlessMode() {

    }
    public void entryNewPuzzleMode() {

    }
    public void entryGoOnPuzzleMode() {

    }
    //--------------------------鼠标监听器-----------------------------------

    @Override
    public void mouseClicked(MouseEvent e) {
    	int x = e.getX();
        int y = e.getY();
        System.out.println(x + "   " +y);
        if (x>=191&&x<=327&&y>=114&&y<=143)
        {
        	//System.out.println("进入继续游戏");
        	if (a==1)
        	{
        		System.out.println("进入经典模式的继续游戏");
        		entryGoOnTypicalMode();
            	oldMainFrame.dispose();
            	mainFrame.dispose();
        	}
        	else if (a==2)
        	{
        		System.out.println("进入翻牌的继续游戏");
        	}
        	else if (a==3)
        	{
        		System.out.println("进入无尽模式的继续游戏");
        	}
        	
        }
        else if (x>=191&&x<=327&&y>=218&&y<=244)
        {

        	if (a==1)
        	{
        		System.out.println("进入经典模式的重新游戏");
            	entryNewTypicalMode();
            	oldMainFrame.dispose();
            	mainFrame.dispose();
        		
        	}
        	else if (a==2)
        	{
        		System.out.println("进入翻牌的重新游戏");
        	}
        	else if (a==3)
        	{
        		System.out.println("进入无尽模式的重新游戏");
        	}
        	
        }
        else if (x>=378&&x<=3443&&y>=322&&y<=350)
        {
        	System.out.println("返回");
        	mainFrame.dispose();
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