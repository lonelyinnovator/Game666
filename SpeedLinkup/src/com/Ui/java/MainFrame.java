package com.Ui.java;

import javax.swing.*;
import java.awt.*;

/**
 * The main frame of the game.
 *
 * @author 李云鹏
 * @date 2020/12/29
 * 学号：2019302835
 */
public class MainFrame extends JFrame {
	//窗口宽和高
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private static final int BOUNDS_X = 500;
    private static final int BOUNDS_Y = 150;
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    //获取屏幕尺寸
    private static final int WINDOW_WIDTH_1 = 500;
    private static final int WINDOW_HEIGHT_1 = 400;
    //主页面的初始化构造函数
    public MainFrame() {
        super();
        initFrame();
    }
  //选择继续还是开始新游戏页面的初始化构造函数
    public MainFrame(int x,MainFrame oldMainFrame,int a) {
        super();
        initFrame(x,oldMainFrame,a);
    }
  //选择继续还是开始新游戏页面的初始化函数
    private void initFrame(int x,MainFrame oldMainFrame,int a){
    	if(x==0)
    	{
            Container p = getContentPane();
            p.setBackground(Color.white);
            this.setTitle("连连看");
            this.setBounds((d.width-WINDOW_WIDTH_1)/2,(d.height-WINDOW_HEIGHT_1)/2, WINDOW_WIDTH_1, WINDOW_HEIGHT_1);
            this.setLayout(null);//绝对布局
            this.setResizable(false);
            //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           
            ChoiceUI choiceUI = new ChoiceUI(this,oldMainFrame,a);//把自己窗口进行关联。
            this.setContentPane(choiceUI);
            
    	}
    	if(x==1)
    	{
            Container p = getContentPane();
            p.setBackground(Color.white);
            this.setTitle("连连看");
            this.setBounds((d.width-WINDOW_WIDTH)/2,(d.height-WINDOW_HEIGHT)/2, WINDOW_WIDTH, WINDOW_HEIGHT);
            this.setLayout(null);//绝对布局
            this.setResizable(false);
            //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           
            SetUI setUI = new SetUI(this,oldMainFrame,a);//把自己窗口进行关联。
            this.setContentPane(setUI);
            
    	}

    }
    
    //主页面的初始化函数
    private void initFrame(){
        Container p = getContentPane();
        p.setBackground(Color.white);
        this.setTitle("连连看");
        this.setBounds((d.width-WINDOW_WIDTH)/2,(d.height-WINDOW_HEIGHT)/2, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLayout(null);//绝对布局
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainUI mainUI = new MainUI(this);//把自己窗口进行关联。
        this.setContentPane(mainUI);
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 创建窗口对象
                MainFrame mainFrame = new MainFrame();
                // 显示窗口
                mainFrame.setVisible(true);
            }
        });
    }
}
