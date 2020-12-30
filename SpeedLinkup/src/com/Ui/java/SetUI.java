package com.Ui.java;

import com.map.java.Lattice;
import com.map.java.Map;
import com.configuration.java.Theme;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import com.configuration.java.MapModel;
import com.configuration.java.Theme;

import com.map.java.Lattice;
import com.map.java.Map;


import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The main panel of the game.
 *
 * @author 李云鹏
 * @date 2020/12/29
 * 学号：2019302835
 */
public class SetUI extends JPanel implements MouseMotionListener, MouseListener {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private MainFrame mainFrame;
    private MainFrame oldMainFrame;
    private int a;
    private static boolean musicFlag = true;
    private static double musicValue = 1.0;
    public Thread music_thread;
    
	public static String[] musics = new String[]{"res/music/Battle1.wav"};
    public SetUI(MainFrame mainFrame,MainFrame oldMainFrame,int a) {
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
        BufferedImage bufImage = ImageIO.read(new File(String.format("res/mainUi/游戏设置.png")));
        g2d.drawImage(bufImage,0,0,WINDOW_WIDTH,WINDOW_HEIGHT,this);
        g2d.dispose();
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
    /**
	 * @return the musicFlag
	 */
	public static boolean isMusicFlag() {
		return musicFlag;
	}
	/**
	 * @param musicFlag the musicFlag to set
	 */
	public static void setMusicFlag(boolean musicFlag) {
		SetUI.musicFlag = musicFlag;
	}
    //--------------------------鼠标监听器-----------------------------------

    @Override
    public void mouseClicked(MouseEvent e) {
    	int x = e.getX();
        int y = e.getY();
        System.out.println(x + "   " +y);
        //开
        if (x>=369&&x<=433&&y>=270&&y<=317)
        {
        	musicFlag = true;
            music_thread = new Thread(()->playMusic(musics));
            music_thread.start();

        	System.out.println("1");

        }
        //关
        else if (x>=725&&x<=775&&y>=278&&y<=319)
        {
        	musicFlag = false;
        	System.out.println("2");
        }
        //开
        else if (x>=371&&x<=426&&y>=524&&y<=565)
        {
        	musicFlag = true;
            music_thread = new Thread(()->playMusic(musics));
            music_thread.start();
//            new Thread(() -> {
//                while (true) {
//                    playMusic(musics);
//                }
//            }).start();
        	System.out.println("3");
        }
        //关
        else if (x>=725&&x<=772&&y>=530&&y<=571)
        {
        	musicFlag = false;
        	System.out.println("4");
        	
        }
        //返回
        else if (x>=876&&x<=965&&y>=689&&y<=731)
        {
        	//music_thread.stop();
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