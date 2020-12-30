package com.Ui.java;

import com.configuration.java.EndlessModel;
import com.configuration.java.Theme;
import com.control.java.EndlessArchive;
import com.control.java.TypicalArchive;
import com.map.java.Lattice;
import com.map.java.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main panel of the game.
 *
 * @author 沈佳军
 * @date 2020/12/22
 */
public class EndlessModePanel extends JPanel implements MouseMotionListener, MouseListener {
    private int map_size_x;
    private int map_size_y;
    private int picture_width;
    private int picture_height;
    private int picture_init_x;
    private int picture_init_y;
    private int picture_number;
    private int panel_width;
    private int panel_height;
    public Map map;
    private int clickedNum = 0;
    public EndlessModeFrame endlessModeFrame;
    private String countDown;
    private Lattice.Point point1 = new Lattice.Point(0, 0);
    private Lattice.Point point2 = new Lattice.Point(0, 0);
    private static boolean musicFlag = true;
    private static double musicValue = 1.0;
    private String[] musics = new String[]{};
    private EndlessArchive endlessArchive;
    public EndlessModel model;
    public Theme theme;
    public int hour;
    public int minute;
    public int second;
    public int time;
    private Image offScreenImage;
    private boolean first = true;

    public EndlessModePanel(EndlessModeFrame endlessModeFrame, EndlessArchive endlessArchive) {
        super();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        initEndlessModePanel(endlessModeFrame, endlessArchive);
    }

    /**
     * 初始化游戏面板
     *
     * @param
     * @param endlessArchive
     * @return void
     */
    public void initEndlessModePanel(EndlessModeFrame endlessModeFrame, EndlessArchive endlessArchive) {
        this.endlessModeFrame = endlessModeFrame;
        this.model = endlessArchive.getMapModel();
        this.setBackground(Color.BLACK);
        this.map_size_x = model.getColumnNum();
        this.map_size_y = model.getRowNum();
        this.picture_width = model.getPictureWidth();
        this.picture_height = model.getPictureHeight();
        this.picture_init_x = model.getPictureWidth();
        this.picture_init_y = model.getPictureHeight();
        this.picture_number = model.getTypeNum();
        this.panel_width = model.getPanelWidth();
        this.panel_height = model.getPanelHeight();
        this.endlessArchive = endlessArchive;
        this.theme = endlessArchive.getTheme();
        this.hour = endlessArchive.getHour();
        this.minute = endlessArchive.getMinute();
        this.second = endlessArchive.getSecond();
        this.map = endlessArchive.getMap();
        if (first) {
            this.time = hour * 60 * 60 + minute * 60 + second;
            first = false;
        }
        System.out.println(time);


        new Thread(() -> {
            while (true) {
                playMusic(musics);
            }
        }).start();
    }

	/**
	 * 将界面中的坐标转换成地图中的索引。
	 *
	 * @param x
	 * @param y
	 * @return Lattice.Point
	 */
	public Lattice.Point changeLocation(int x, int y) {
		return new Lattice.Point((x - picture_init_x) / picture_width + 1, (y - picture_init_y) / picture_height + 1);
	}

	/**
	 * 将地图中的索引转换成界面中的坐标。
	 *
	 * @param new_point
	 * @return int[]
	 */
	public int[] changeLocation(Lattice.Point new_point) {
		return new int[]{picture_init_x + (new_point.x - 1) * picture_width,
				picture_init_y + (new_point.y - 1) * picture_height};
	}

    /**
     * 绘画主函数
     *
     * @param g
     * @return void
     */
    @Override
    public void paint(Graphics g) {

        super.paint(g);
		drawAllImage(g);
//        showCountDown();
    }

    /**
     * 图片双缓冲，用来刷新绘图界面
     *
     * @param g
     * @return void
     */
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(panel_width, panel_height);
        }
        Graphics gImage = offScreenImage.getGraphics();
        gImage.clearRect(0, 0, panel_width, panel_height);
        paint(gImage);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void drawAllImage(Graphics g){
    	drawBackground(g);
    	drawLatticeImage(g);
	}

    public void drawBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            BufferedImage bufImage1 = ImageIO.read(new File("res/material1/Background.png"));
            g2d.drawImage(bufImage1, 0, 0, panel_width, panel_height, null);
        } catch (Exception e) {
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
    public void drawLatticeImage(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        for (int i = 1; i <= map_size_x; i++) {
            for (int j = 1; j <= map_size_y; j++) {
                try {
                    Lattice.Point new_point = new Lattice.Point(i, j);
                    if (map.getType(new_point) != 0) {
                        BufferedImage bufImage = ImageIO.read(new File(map.getLattice(new_point).getImgPath()));
                        g2d.drawImage(bufImage, picture_init_x + (i - 1) * picture_width,
                                picture_init_y + (j - 1) * picture_height, picture_width, picture_height, this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        g2d.dispose();
    }

    /**
     * 删除两个可以消除的格子上的图片。
     *
     * @param
     * @return void
     */
    private void removeLatticeImg() {
        try {
            Graphics g = getGraphics();
            g.setColor(Color.BLACK);
            if (point1.x < 0 || point1.y < 0 || point2.x < 0 || point2.y < 0 || point1.x > map_size_x
                    || point1.y > map_size_y || point2.x > map_size_x || point2.y > map_size_y) {
                return;
            }
            if (map.judge(point1, point2)) {
                //增加时间和分数
//                this.time += 2;
                endlessModeFrame.integral += 10;
                endlessModeFrame.jLabel_integral.setText("当前积分：" + endlessModeFrame.integral);
                drawLine();
				Thread.sleep(100);
                map.removeLattice(point1);
                map.removeLattice(point2);
                System.out.println(true);
				update(g);
//				g.fillRect(changeLocation(point1)[0], changeLocation(point1)[1], picture_width + 1,
//						picture_height + 1);
//                g.fillRect(changeLocation(point2)[0], changeLocation(point2)[1], picture_width + 1,
//						picture_height + 1);
                if (map.getAvailableLattices() == null) {
                    EndlessArchive.deleteArchive();
                    endlessArchive = EndlessArchive.newArchive();
                    initEndlessModePanel(endlessModeFrame, endlessArchive);
                    this.time += 10;
                    endlessModeFrame.integral += 10;
                    endlessModeFrame.number += 1;
                    endlessModeFrame.jLabel_number.setText("当前轮数：" + endlessModeFrame.number);
                    EndlessArchive.saveArchiveInfo(map, model, theme, hour, minute, second,
                            endlessModeFrame.integral, endlessModeFrame.number);
                    update(g);
                }
            } else {
                System.out.println(false);
				update(g);
//                g.drawRect(changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height);
//                g.drawRect(changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制消去两个图片时的线。传入黑色可以消去白色的线。
     *
     * @param
     * @return void
     */
    public void drawLine() {
        Graphics g = getGraphics();
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(3.0f);
        g2d.setStroke(stroke);
        int i;
        int width_setOff = picture_width / 2;
        int height_setOff = picture_height / 2;
        Lattice.Point new_point = new Lattice.Point(0, 0);
        ArrayList<Lattice.Point> pointArrayList = (ArrayList<Lattice.Point>) map.getPath();
        if (pointArrayList.size() != 0) {
            if ((Math.abs(point2.x - pointArrayList.get(0).x) == 1 && point2.y == pointArrayList.get(0).y)
                    || (Math.abs(point2.y - pointArrayList.get(0).y) == 1 && point2.x == pointArrayList.get(0).x)) {
                new_point.x = point1.x;
                new_point.y = point1.y;
                point1.x = point2.x;
                point1.y = point2.y;
                point2.x = new_point.x;
                point2.y = new_point.y;
            }
            if (point1.x < pointArrayList.get(0).x) {
                width_setOff = picture_width;
            } else if (point1.x > pointArrayList.get(0).x) {
                width_setOff = 0;
            } else if (point1.y < pointArrayList.get(0).y) {
                height_setOff = picture_height;
            } else if (point1.y > pointArrayList.get(0).y) {
                height_setOff = 0;
            }
            g2d.drawLine(changeLocation(point1)[0] + width_setOff, changeLocation(point1)[1] + height_setOff,
                    changeLocation(pointArrayList.get(0))[0] + picture_width / 2,
                    changeLocation(pointArrayList.get(0))[1] + picture_height / 2);
            g2d.drawLine(changeLocation(point1)[0] + width_setOff, changeLocation(point1)[1] + height_setOff,
                    changeLocation(pointArrayList.get(0))[0] + picture_width / 2,
                    changeLocation(pointArrayList.get(0))[1] + picture_height / 2);
            for (i = 0; i < pointArrayList.size() - 1; i++) {
                g2d.drawLine(changeLocation(pointArrayList.get(i))[0] + picture_width / 2,
                        changeLocation(pointArrayList.get(i))[1] + picture_height / 2,
                        changeLocation(pointArrayList.get(i + 1))[0] + picture_width / 2,
                        changeLocation(pointArrayList.get(i + 1))[1] + picture_height / 2);
                g2d.drawLine(changeLocation(pointArrayList.get(i))[0] + picture_width / 2,
                        changeLocation(pointArrayList.get(i))[1] + picture_height / 2,
                        changeLocation(pointArrayList.get(i + 1))[0] + picture_width / 2,
                        changeLocation(pointArrayList.get(i + 1))[1] + picture_height / 2);
            }
            if (point2.x < pointArrayList.get(i).x) {
                width_setOff = picture_width;
                height_setOff = picture_height / 2;
            } else if (point2.x > pointArrayList.get(i).x) {
                width_setOff = 0;
                height_setOff = picture_height / 2;
            } else if (point2.y < pointArrayList.get(i).y) {
                width_setOff = picture_width / 2;
                height_setOff = picture_height;
            } else if (point2.y > pointArrayList.get(i).y) {
                width_setOff = picture_width / 2;
                height_setOff = 0;
            }
            g2d.drawLine(changeLocation(pointArrayList.get(i))[0] + picture_width / 2,
                    changeLocation(pointArrayList.get(i))[1] + picture_height / 2,
                    changeLocation(point2)[0] + width_setOff, changeLocation(point2)[1] + height_setOff);
            g2d.drawLine(changeLocation(pointArrayList.get(i))[0] + picture_width / 2,
                    changeLocation(pointArrayList.get(i))[1] + picture_height / 2,
                    changeLocation(point2)[0] + width_setOff, changeLocation(point2)[1] + height_setOff);
        }
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

    public void reDrawPanel(EndlessArchive endlessArchive) {
        Graphics g = getGraphics();
        initEndlessModePanel(endlessModeFrame, endlessArchive);
        update(g);
    }

    // --------------------------鼠标监听器-----------------------------------//

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * 鼠标左键和右键释放时监控事件
     *
     * @param e
     * @return void
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Graphics g = getGraphics();
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(3.0f);
        g2d.setStroke(stroke);
        int x = e.getX();
        int y = e.getY();
        Lattice.Point new_point = changeLocation(x, y);
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!(x < picture_init_x || x > picture_init_x + map_size_x * picture_width || y < picture_init_y
                    || y > picture_init_y + map_size_y * picture_height || map.isNULL(new_point))) {
                if (clickedNum == 0) {
                    point1.x = new_point.x;
                    point1.y = new_point.y;
                    g2d.drawRect(changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height);
                    clickedNum++;
                } else if (clickedNum == 1) {
                    point2.x = new_point.x;
                    point2.y = new_point.y;
                    g2d.drawRect(changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height);
                    removeLatticeImg();
                    clickedNum = 0;
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (clickedNum == 1) {
                update(g);
                clickedNum = 0;
            }
        }
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