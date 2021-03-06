package com.Ui.java;

import com.configuration.java.MapModel;
import com.configuration.java.Theme;
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
import java.util.ArrayList;
import java.util.List;

/**
 * The main panel of the game.
 *
 * @author 沈佳军
 * @date 2020/12/22
 */
public class TypicalModePanel extends JPanel implements MouseMotionListener, MouseListener {
    private int map_size_x;
    private int map_size_y;
    private int picture_width;
    private int picture_height;
    private int picture_init_x;
    private int picture_init_y;
    private int panel_width;
    private int panel_height;
    public Map map;
    public MapModel model;
    public int clickedNum = 0;
    public TypicalModeFrame typicalModeFrame;
    private String countDown;
    private Lattice.Point point1 = new Lattice.Point(0, 0);
    private Lattice.Point point2 = new Lattice.Point(0, 0);
    public TypicalArchive typicalArchive;
    //    public Theme theme;
//    public int hour;
//    public int minute;
//    public int second;
//    public int time;
    private Image offScreenImage;
    private boolean time_flag = true;

    public TypicalModePanel(TypicalModeFrame typicalModeFrame) {
        super();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        initTypicalModePanel(typicalModeFrame);
    }

    /**
     * 初始化游戏面板
     *
     * @param typicalModeFrame
     * @return void
     */
    public void initTypicalModePanel(TypicalModeFrame typicalModeFrame) {
        this.typicalModeFrame = typicalModeFrame;
        this.model = typicalModeFrame.typicalArchive.getMapModel();
        this.map_size_x = model.getColumnNum();
        this.map_size_y = model.getRowNum();
        this.picture_width = model.getPictureWidth();
        this.picture_height = model.getPictureHeight();
        this.picture_init_x = model.getPictureWidth();
        this.picture_init_y = model.getPictureHeight();
        this.panel_width = model.getPanelWidth();
        this.panel_height = model.getPanelHeight();
        this.map = typicalModeFrame.typicalArchive.getMap();

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
        return new int[]{
                picture_init_x + (new_point.x - 1) * picture_width, picture_init_y + (new_point.y - 1) * picture_height
        };
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
            offScreenImage = this.createImage(panel_width, panel_height);
        }
        Graphics gImage = offScreenImage.getGraphics();
        gImage.clearRect(0, 0, panel_width, panel_height);
        paint(gImage);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    /**
     * 绘制所有的图片
     *
     * @param g
     * @return void
     */
    public void drawAllImage(Graphics g) {
        drawBackgroundImage(g);
        drawLatticeImage(g);
    }

    /**
     * 绘制背景图片
     *
     * @param g
     * @return void
     */
    public void drawBackgroundImage(Graphics g) {
        try {
            BufferedImage bufImage = ImageIO.read(new File("res/ui/starBackground.png"));
            g.drawImage(bufImage, 0, 0, panel_width, panel_height, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制格子中的图片
     *
     * @param g
     * @return void
     */
    public void drawLatticeImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Lattice.Point new_point;
        for (int i = 1; i <= map_size_x; i++) {
            for (int j = 1; j <= map_size_y; j++) {
                try {
                    new_point = new Lattice.Point(i, j);
                    if (map.getType(new_point) != 0) {
                        BufferedImage bufImage = ImageIO.read(new File(map.getLattice(new_point).getImgPath()));
                        g2d.drawImage(bufImage, picture_init_x + (i - 1) * picture_width,
                                picture_init_y + (j - 1) * picture_height, picture_width, picture_height, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        g2d.dispose();
    }

    /**
     * 删除两个可以消除的格子上的图片，同时画线。
     *
     * @param
     * @return void
     */
    private void removeLatticeImg(Lattice.Point pointOne, Lattice.Point pointTwo) {
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
                TypicalArchive.saveArchiveInfo(map, model, typicalModeFrame.typicalArchive.getTheme(),
                        typicalModeFrame.time);
                System.out.println(true);
//                drawAllImage(g);
                update(g);
//                g.drawImage(null, changeLocation(pointOne)[0], changeLocation(pointOne)[1],
//                        picture_width, picture_height, this);
//                g.drawImage(null, changeLocation(pointTwo)[0], changeLocation(pointTwo)[1],
//                        picture_width, picture_height, this);
                if (map.getAvailableLattices() == null) {
                    TypicalArchive.deleteArchive();
                    typicalModeFrame.musicFlag = false;
                    if (map.getRestLatticeAmount() == 0) {
                        typicalModeFrame.showSuccessDialog();
                    } else {
                        typicalModeFrame.showFailedDialog();
                    }
                }
            } else {
                System.out.println(false);
                update(g);
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
    public void removeLatticeImg(Lattice.Point pointOne, Lattice.Point pointTwo, boolean flag) {
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
                    TypicalArchive.saveArchiveInfo(map, model, typicalModeFrame.typicalArchive.getTheme(),
                            typicalModeFrame.time);
                    System.out.println(true);
                    g.fillRect(changeLocation(pointOne)[0], changeLocation(pointOne)[1],
                            picture_width + 1, picture_height + 1);
                    g.fillRect(changeLocation(pointTwo)[0], changeLocation(pointTwo)[1],
                            picture_width + 1, picture_height + 1);
                    if (map.getAvailableLattices() == null) {
                        TypicalArchive.deleteArchive();
                        if (map.getRestLatticeAmount() == 0) {
                            typicalModeFrame.showSuccessDialog();
                        } else {
                            typicalModeFrame.showFailedDialog();
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
        Graphics g = getGraphics();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        Stroke stroke = new BasicStroke(3.0f);
        g2d.setStroke(stroke);
        Lattice.Point new_point = changeLocation(x, y);
        if (!(x < picture_init_x || x > picture_init_x + map_size_x * picture_width ||
                y < picture_init_y || y > picture_init_y + map_size_y * picture_height || map.isNULL(new_point))) {
            if (clickedNum == 0) {
                point1.x = new_point.x;
                point1.y = new_point.y;
                g2d.drawRect(changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height);
                clickedNum++;
            } else if (clickedNum == 1) {
                point2.x = new_point.x;
                point2.y = new_point.y;
                g2d.drawRect(changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height);
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
            Lattice.Point new_point = changeLocation(x, y);
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
    public void removeBox(Lattice.Point pointOne) {
        if (clickedNum == 1) {
            Graphics g = getGraphics();
            update(g);
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
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        Stroke stroke = new BasicStroke(3.0f);
        g2d.setStroke(stroke);
        int i;
        int width_setOff = picture_width / 2;
        int height_setOff = picture_height / 2;
        Lattice.Point new_point = new Lattice.Point(0, 0);
        ArrayList<Lattice.Point> pointArrayList = (ArrayList<Lattice.Point>) map.getPath();
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
     * 自动连连看
     *
     * @param
     * @return void
     */
    public void autoPlay() {
        List<Lattice.Point> points;
        while ((points = map.getAvailableLattices()) != null) {
            point1 = points.get(0);
            point2 = points.get(1);
            removeLatticeImg(point1, point2);
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        TypicalArchive.deleteArchive();
    }



    public void reDrawPanel() {
        Graphics g = getGraphics();
        update(g);
    }

    //--------------------------鼠标监听器-----------------------------------//

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
