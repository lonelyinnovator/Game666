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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.configuration.java.FlopModel;
import com.configuration.java.MapModel;
import com.configuration.java.ModelFactory;
import com.configuration.java.Theme;
import com.control.java.FlopArchive;
import com.control.java.TypicalArchive;
import com.map.java.Lattice;
import com.map.java.Map;
import com.map.java.Lattice.Point;

/**
 *
 * @author DELL
 * map_size_x 方块列数
 * map_size_y 
 * picture_width
 * picture_height
 * picture_init_x
 * picture_init_y
 * pciture_number
 * map
 * clickedNum
 * flopModeFrame
 * countDown
 * point1
 * point2
 * musicflag
 * musicvalue
 * musics
 * flopArchive
 * model
 *
 */
public class FlopModePanel extends JPanel implements MouseListener, MouseMotionListener {
    private int map_size_x;
    private int map_size_y;
    private int picture_width;
    private int picture_height;
    private int picture_init_x;
    private int picture_init_y;
    private int picture_number;
    private Map map;
    public int clickedNum = 0;
    public FlopModeFrame flopModeFrame;
    private String countDown;
    private Point point1 = new Point(0, 0);
    private Point point2 = new Point(0, 0);
    private static boolean musicFlag = true;
    private static double musicValue = 1.0;
    private String[] musics = new String[]{};
    private FlopArchive flopArchive;
    private FlopModel model;
    private Theme theme;
    private int hour;
    private int minute;
    private int second;
    private int timeincreasenumber = 100000;
    private Image offScreenImage;

    public int time;
    public Thread thread1;

    public FlopModePanel(FlopModeFrame flopModeFrame, FlopArchive flopArchive) {
        super();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        initFlopModelPanel(flopModeFrame, flopArchive);
    }

    public void initFlopModelPanel(FlopModeFrame flopModeFrame, FlopArchive flopArchive) {
        this.flopModeFrame = flopModeFrame;
        this.model = flopArchive.getMapModel();
        this.setBackground(Color.BLACK);
        this.map_size_x = model.getColumnNum();
        this.map_size_y = model.getRowNum();
        this.picture_width = model.getPictureWidth();
        this.picture_height = model.getPictureHeight();
        this.picture_init_x = model.getPictureWidth();
        this.picture_init_y = model.getPictureHeight();
        this.picture_number = model.getTypeNum();
        this.flopArchive = flopArchive;
        this.theme = flopArchive.getTheme();
        this.hour = flopArchive.getHour();
        this.minute = flopArchive.getMinute();
        this.second = flopArchive.getSecond();
        this.map = flopArchive.getMap();
        this.timeincreasenumber = 3;
        this.time = hour * 60 * 60 + minute * 60 + second;
        this.thread1 = new Thread();
        new Thread(() -> {
            while (true) {
                playMusic(musics);
            }
        }).start();
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
    }

    /**
     * 图片双缓冲，用来刷新绘图界面
     *
     * @param g
     * @return void
     */
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(model.getPanelWidth(), model.getPanelHeight());
        }
        Graphics gImage = offScreenImage.getGraphics();
        gImage.clearRect(0, 0, model.getPanelWidth(), model.getPanelHeight());
        paint(gImage);
        g.drawImage(offScreenImage, 0, 0, null);
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

    /**
     * 绘制所有图片
     */
    public void drawAllImage(Graphics g) {
        drawbackground(g);
        drawImage(g);
    }

    /**
     * 绘制背景图片
     */
    public void drawbackground(Graphics g) {
        try {
            BufferedImage image = ImageIO.read(new File("res/ui/starBackground.png"));
            g.drawImage(image, 0, 0, model.getPanelWidth(), model.getPanelHeight(), null);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 绘制图片
     */
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        for (int i = 1; i <= map_size_x; i++) {
            for (int j = 1; j <= map_size_y; j++) {
                try {
                    Point new_point = new Point(i, j);
                    if (map.getType(new_point) != 0) {
                        BufferedImage image = ImageIO.read(new File("res/material0/picture1.jpg"));
                        g2d.drawImage(image, picture_init_x + (i - 1) * picture_width, picture_init_y + (j - 1) * picture_height,
                                picture_width, picture_height, this);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }

    /**
     * 将界面中的坐标转换成地图中的索引。
     *
     * @param x
     * @param y
     * @return Lattice.Point
     */
    public Point changeLocation(int x, int y) {
        return new Point((x - picture_init_x) / picture_width + 1, (y - picture_init_y) / picture_height + 1);
    }

    /**
     * 将地图中的索引转换成界面中的坐标。
     *
     * @param new_point
     * @return int[]
     */
    public int[] changeLocation(Point new_point) {
        return new int[]{
                picture_init_x + (new_point.x - 1) * picture_width, picture_init_y + (new_point.y - 1) * picture_height
        };
    }

    /**
     * 删除两个可以消除的格子上的图片，同时画线。
     *
     * @param
     * @return void
     */
    private void removeLatticeImg(Point pointOne, Point pointTwo) {
        try {
            Graphics g = getGraphics();
            g.setColor(Color.BLACK);
            if (pointOne.x < 0 || pointOne.y < 0 || pointTwo.x < 0 || pointTwo.y < 0
                    || pointOne.x > map_size_x || pointOne.y > map_size_y
                    || pointTwo.x > map_size_x || pointTwo.y > map_size_y) {
                return;
            }
            if (map.judge(pointOne, pointTwo)) {
                drawLine(Color.WHITE);
                Thread.sleep(50);
                drawLine(Color.BLACK);
                map.removeLattice(pointOne);
                map.removeLattice(pointTwo);
                FlopArchive.saveArchiveInfo(map, model, theme, hour, minute, second);
                System.out.println(true);
//                drawAllImage(g);
                update(g);
//                g.drawImage(null, changeLocation(pointOne)[0], changeLocation(pointOne)[1],
//                        picture_width, picture_height, this);
//                g.drawImage(null, changeLocation(pointTwo)[0], changeLocation(pointTwo)[1],
//                        picture_width, picture_height, this);
                if (map.getAvailableLattices() == null) {
                    FlopArchive.deleteArchive();
                    if (map.getRestLatticeAmount() == 0) {
                        flopModeFrame.showSuccessDialog(flopModeFrame, flopModeFrame);
                    } else {
                        flopModeFrame.showFailedDialog(flopModeFrame, flopModeFrame);
                    }
                }
            } else {
                System.out.println(false);
                g.drawRect(changeLocation(pointOne)[0], changeLocation(pointOne)[1], picture_width, picture_height);
                g.drawRect(changeLocation(pointTwo)[0], changeLocation(pointTwo)[1], picture_width, picture_height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消除格子中的图片时不画线
     *
     * @param flag
     * @return void
     */
    public void removeLatticeImg(Point pointOne, Point pointTwo, boolean flag) {
        if (!flag) {
            try {
                Graphics g = getGraphics();
                g.setColor(Color.BLACK);
                if (pointOne.x < 0 || pointOne.y < 0 || pointTwo.x < 0 || pointTwo.y < 0
                        || pointOne.x > map_size_x || pointOne.y > map_size_y
                        || pointTwo.x > map_size_x || pointTwo.y > map_size_y) {
                    return;
                }
                if (map.judge(pointOne, pointTwo)) {
//                    drawLine(Color.WHITE);
//                    Thread.sleep(50);
//                    drawLine(Color.BLACK);
                    map.removeLattice(pointOne);
                    map.removeLattice(pointTwo);
                    TypicalArchive.saveArchiveInfo(map, model, theme, time);
                    System.out.println(true);
                    g.fillRect(changeLocation(pointOne)[0], changeLocation(pointOne)[1],
                            picture_width + 1, picture_height + 1);
                    g.fillRect(changeLocation(pointTwo)[0], changeLocation(pointTwo)[1],
                            picture_width + 1, picture_height + 1);
                    if (map.getAvailableLattices() == null) {
                        TypicalArchive.deleteArchive();
                        if (map.getRestLatticeAmount() == 0) {
                            flopModeFrame.showSuccessDialog(flopModeFrame, flopModeFrame);
                        } else {
                            flopModeFrame.showFailedDialog(flopModeFrame, flopModeFrame);
                        }
                    }
                } else {
                    System.out.println(false);
                    g.drawRect(changeLocation(pointOne)[0], changeLocation(pointOne)[1], picture_width, picture_height);
                    g.drawRect(changeLocation(pointTwo)[0], changeLocation(pointTwo)[1], picture_width, picture_height);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 画框
     *
     * @param
     * @return void
     */
    public void drawBox(int x, int y) {
        System.out.println(clickedNum);
        Graphics g1 = getGraphics();
        Graphics2D g2d = (Graphics2D) g1.create();
        Point new_point = changeLocation(x, y);
        if (!(x < picture_init_x || x > picture_init_x + map_size_x * picture_width ||
                y < picture_init_y || y > picture_init_y + map_size_y * picture_height || map.isNULL(new_point))) {
            g1.setColor(Color.WHITE);
            if (clickedNum == 0) {
                point1.x = new_point.x;
                point1.y = new_point.y;
                try {
                    g1.setColor(Color.black);
                    g1.fillRect(changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height);
                    BufferedImage image = ImageIO.read(new File(map.getLattice(new_point).getImgPath()));
                    g2d.drawImage(image, changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height, this);
                    clickedNum++;
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (clickedNum == 1) {
                point2.x = new_point.x;
                point2.y = new_point.y;
                try {
                    g1.setColor(Color.black);
                    g1.fillRect(changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height);
                    BufferedImage image = ImageIO.read(new File(map.getLattice(new_point).getImgPath()));
                    g2d.drawImage(image, changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height, this);
                    Thread.sleep(500);
                    if (!map.judge(point1, point2)) {
                        image = ImageIO.read(new File("res/material0/picture1.jpg"));
                        g2d.drawImage(image, changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height, this);
                        g2d.drawImage(image, changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height, this);
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                removeLatticeImg(point1, point2);
                clickedNum = 0;
            }
        }
    }

    /**
     * 不画框
     *
     * @param
     * @return void
     */
    public void drawBox(int x, int y, boolean flag) {
        if (!flag) {
            Graphics g = getGraphics();
            Point new_point = changeLocation(x, y);
            if (!(x < picture_init_x || x > picture_init_x + map_size_x * picture_width ||
                    y < picture_init_y || y > picture_init_y + map_size_y * picture_height || map.isNULL(new_point))) {
                g.setColor(Color.WHITE);
                if (clickedNum == 0) {
                    point1.x = new_point.x;
                    point1.y = new_point.y;
//                    g.drawRect(changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height);
                    clickedNum++;
                } else if (clickedNum == 1) {
                    point2.x = new_point.x;
                    point2.y = new_point.y;
//                    g.drawRect(changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height);
                    removeLatticeImg(point1, point2);
                    clickedNum = 0;
                }
            }
        }
    }

    /**
     * 移除框
     *
     * @param pointOne
     * @return void
     */
    public void removeBox(Point pointOne) {
        if (clickedNum == 1) {
            Graphics g = getGraphics();
            g.setColor(Color.BLACK);
            g.drawRect(changeLocation(pointOne)[0], changeLocation(pointOne)[1], picture_width, picture_height);
            clickedNum = 0;
        }
    }


    /**
     * 绘制消去两个图片时的线。传入黑色可以消去白色的线。
     *
     * @param color
     * @return void
     */
    public void drawLine(Color color) {
        Graphics g = getGraphics();
        g.setColor(color);
        int i;
        int width_setOff = picture_width / 2;
        int height_setOff = picture_height / 2;
        Point new_point = new Point(0, 0);
        ArrayList<Point> pointArrayList = (ArrayList<Point>) map.getPath();
        if (pointArrayList.size() != 0) {
            if ((Math.abs(point2.x - pointArrayList.get(0).x) == 1 && point2.y == pointArrayList.get(0).y) ||
                    (Math.abs(point2.y - pointArrayList.get(0).y) == 1 && point2.x == pointArrayList.get(0).x)) {
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
            g.drawLine(changeLocation(point1)[0] + width_setOff, changeLocation(point1)[1] + height_setOff,
                    changeLocation(pointArrayList.get(0))[0] + picture_width / 2,
                    changeLocation(pointArrayList.get(0))[1] + picture_height / 2);
            g.drawLine(changeLocation(point1)[0] + width_setOff, changeLocation(point1)[1] + height_setOff,
                    changeLocation(pointArrayList.get(0))[0] + picture_width / 2,
                    changeLocation(pointArrayList.get(0))[1] + picture_height / 2);
            for (i = 0; i < pointArrayList.size() - 1; i++) {
                g.drawLine(changeLocation(pointArrayList.get(i))[0] + picture_width / 2,
                        changeLocation(pointArrayList.get(i))[1] + picture_height / 2,
                        changeLocation(pointArrayList.get(i + 1))[0] + picture_width / 2,
                        changeLocation(pointArrayList.get(i + 1))[1] + picture_height / 2);
                g.drawLine(changeLocation(pointArrayList.get(i))[0] + picture_width / 2,
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
            g.drawLine(changeLocation(pointArrayList.get(i))[0] + picture_width / 2,
                    changeLocation(pointArrayList.get(i))[1] + picture_height / 2,
                    changeLocation(point2)[0] + width_setOff, changeLocation(point2)[1] + height_setOff);
            g.drawLine(changeLocation(pointArrayList.get(i))[0] + picture_width / 2,
                    changeLocation(pointArrayList.get(i))[1] + picture_height / 2,
                    changeLocation(point2)[0] + width_setOff, changeLocation(point2)[1] + height_setOff);
        }
    }

    /**
     * 显示倒计时
     *
     * @param
     * @return String
     */
    private void showCountDown() {
        Graphics g = getGraphics();
        g.setFont(new Font("Consolas", Font.PLAIN, 25));
//        thread1.start();
//        while (time > 0) {
//        	time--;
//        	try {
//				int mm = time / 60 % 60;
//				int ss = time % 60;
//				countDown = mm + ":" + ss;
//				g.setColor(Color.WHITE);
//				g.drawString(countDown, 700, 20);
//				Thread.sleep(1000);
//				g.setColor(Color.BLACK);
//				g.fillRect(680, 0, 100, 100);
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//        }
        new Thread(() -> {
            while (time > 0) {
                time--;
                try {
                    int mm = time / 60 % 60;
                    int ss = time % 60;
                    countDown = mm + ":" + ss;
                    g.setColor(Color.WHITE);
                    g.drawString(countDown, 700, 20);
                    Thread.sleep(1000);
                    g.setColor(Color.BLACK);
                    g.fillRect(680, 0, 100, 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void reDrawPanel(FlopArchive flopArchive) {
        Graphics g = getGraphics();
        initFlopModelPanel(flopModeFrame, flopArchive);
        update(g);
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

//		Graphics g1 = getGraphics();
//		Graphics g2 = getGraphics();
//		Graphics2D g2d = (Graphics2D)g1.create();
//		List<Point>randommove;
//		int type;
//		int x = e.getX();
//		int y = e.getY();
//		Point new_point = changeLocation(x, y);
//
//		//如果鼠标按下的是左键则开始响应
//		if (e.getButton() == MouseEvent.BUTTON1) {
//			//如果鼠标按下的位置在连连看的图形范围内，则执行翻牌
//			if (!(x < picture_init_x || x > picture_init_x + map_size_x * picture_width ||
//                    y < picture_init_y || y > picture_init_y + map_size_y * picture_height ||
//                    map.isNULL(new_point))) {
//				if (clickedNum == 0) {
//					point1.x = new_point.x;
//					point1.y = new_point.y;
//					try {
//						g1.setColor(Color.black);
//						g1.fillRect(changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height);
//						BufferedImage image = ImageIO.read(new File(map.getLattice(new_point).getImgPath()));
//						g2d.drawImage(image, changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height, this);
//						clickedNum++;
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				} else if (clickedNum == 1) {
//					point2.x = new_point.x;
//					point2.y = new_point.y;
//					try {
//						g1.setColor(Color.black);
//						g1.fillRect(changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height);
//						BufferedImage image = ImageIO.read(new File(map.getLattice(new_point).getImgPath()));
//						g2d.drawImage(image, changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height, this);
//						Thread.sleep(500);
//						image = ImageIO.read(new File("res/material0/picture1.jpg"));
//						g2d.drawImage(image, changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height, this);
//						g2d.drawImage(image, changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height, this);
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//
//					//当玩家消除了可触发随机消除功能的方块时，则随机选择一组方块消去
//					if (map.judge(point1, point2)) {
//						type = map.getLattice(new_point).getType();
//						g2.setColor(Color.black);
//						if (type == 2 || type == 5 || type == 9) {
//							randommove = map.getSameLatticePoints();
//							Point removemarkPoint1 = randommove.get(0);
//							Point removemarkPoint2 = randommove.get(1);
////							removeLatticeImg(removemarkPoint1, removemarkPoint2);
//							g2.fillRect(changeLocation(removemarkPoint1)[0], changeLocation(removemarkPoint1)[1], picture_width, picture_height);
//							map.removeLattice(removemarkPoint1);
//							g2.fillRect(changeLocation(removemarkPoint2)[0], changeLocation(removemarkPoint2)[1], picture_width, picture_height);
//							map.removeLattice(removemarkPoint2);
////							System.out.println("随机消除成功");
////							System.out.println("随机消除方块的坐标");
//							System.out.println(removemarkPoint1.x + "  " + removemarkPoint1.y);
//							System.out.println(removemarkPoint2.x + "  " + removemarkPoint2.y);
//						}
//					}
//					removeLatticeImg(point1, point2);
//					clickedNum = 0;
//				}
//			}
//		}
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            drawBox(x, y);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            removeBox(point1);
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
