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
    public int clickedNum = 0;
    public TypicalModeFrame typicalModeFrame;
    private String countDown;
    private Lattice.Point point1 = new Lattice.Point(0, 0);
    private Lattice.Point point2 = new Lattice.Point(0, 0);
    private static boolean musicFlag = true;
    private static double musicValue = 1.0;
    private String[] musics = new String[]{};
    public TypicalArchive typicalArchive;
    private MapModel model;
    private Theme theme;
    private int hour;
    private int minute;
    private int second;
    public int time;
    private Image offScreenImage;

    public TypicalModePanel(TypicalModeFrame typicalModeFrame, TypicalArchive typicalArchive) {
        super();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        initTypicalModePanel(typicalModeFrame, typicalArchive);
    }

    /**
     * 初始化游戏面板
     *
     * @param typicalModeFrame
     * @param typicalArchive
     * @return void
     */
    public void initTypicalModePanel(TypicalModeFrame typicalModeFrame, TypicalArchive typicalArchive) {
        this.typicalModeFrame = typicalModeFrame;
        this.model = typicalArchive.getMapModel();
        this.setBackground(Color.BLACK);
        this.map_size_x = model.getColumnNum();
        this.map_size_y = model.getRowNum();
        this.picture_width = model.getPictureWidth();
        this.picture_height = model.getPictureHeight();
        this.picture_init_x = model.getPictureWidth();
        this.picture_init_y = model.getPictureHeight();
        this.panel_width = model.getPanelWidth();
        this.panel_height = model.getPanelHeight();
        this.typicalArchive = typicalArchive;
        this.theme = typicalArchive.getTheme();
        this.hour = typicalArchive.getHour();
        this.minute = typicalArchive.getMinute();
        this.second = typicalArchive.getSecond();
        this.map = typicalArchive.getMap();
        this.time = hour * 60 * 60 + minute * 60 + second;
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
//        showCountDown();
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
     * 图片双缓冲
     *
     * @param g
     * @return void
     */
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(model.getPictureWidth(), model.getPanelHeight());
        }
        Graphics gImage = offScreenImage.getGraphics();
        gImage.fillRect(0, 0, model.getPictureWidth(), model.getPanelHeight());
        paint(gImage);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void drawAllImage(Graphics g){
        drawBackgroundImage(g);
        drawLatticeImage(g);
    }

    public void drawBackgroundImage(Graphics g){
        try{
            BufferedImage bufImage = ImageIO.read(new File("res/ui/starBackground.png"));
            g.drawImage(bufImage, 0, 0, panel_width, panel_height, null);
        }catch (Exception e){
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
        for (int i = 1; i <= map_size_x; i++) {
            for (int j = 1; j <= map_size_y; j++) {
                try {
                    Lattice.Point new_point = new Lattice.Point(i, j);
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
                TypicalArchive.saveArchiveInfo(map, model, theme, hour, minute, second);
                System.out.println(true);
//                drawAllImage(g);
                validate();
//                g.drawImage(null, changeLocation(pointOne)[0], changeLocation(pointOne)[1],
//                        picture_width, picture_height, this);
//                g.drawImage(null, changeLocation(pointTwo)[0], changeLocation(pointTwo)[1],
//                        picture_width, picture_height, this);
                if (map.getAvailableLattices() == null) {
                    TypicalArchive.deleteArchive();
                    if (map.getRestLatticeAmount() == 0) {
                        showSuccessDialog(typicalModeFrame, typicalModeFrame);
                    } else {
                        showFailedDialog(typicalModeFrame, typicalModeFrame);
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
                    TypicalArchive.saveArchiveInfo(map, model, theme, hour, minute, second);
                    System.out.println(true);
                    g.fillRect(changeLocation(pointOne)[0], changeLocation(pointOne)[1],
                            picture_width + 1, picture_height + 1);
                    g.fillRect(changeLocation(pointTwo)[0], changeLocation(pointTwo)[1],
                            picture_width + 1, picture_height + 1);
                    if (map.getAvailableLattices() == null) {
                        TypicalArchive.deleteArchive();
                        if (map.getRestLatticeAmount() == 0) {
                            showSuccessDialog(typicalModeFrame, typicalModeFrame);
                        } else {
                            showFailedDialog(typicalModeFrame, typicalModeFrame);
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
        Lattice.Point new_point = changeLocation(x, y);
        if (!(x < picture_init_x || x > picture_init_x + map_size_x * picture_width ||
                y < picture_init_y || y > picture_init_y + map_size_y * picture_height || map.isNULL(new_point))) {
            g.setColor(Color.WHITE);
            if (clickedNum == 0) {
                point1.x = new_point.x;
                point1.y = new_point.y;
                g.drawRect(changeLocation(point1)[0], changeLocation(point1)[1], picture_width, picture_height);
                clickedNum++;
            } else if (clickedNum == 1) {
                point2.x = new_point.x;
                point2.y = new_point.y;
                g.drawRect(changeLocation(point2)[0], changeLocation(point2)[1], picture_width, picture_height);
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
                typicalArchive = TypicalArchive.newArchive(model);
                reDrawPanel(typicalArchive);
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
    public void showFailedDialog(TypicalModeFrame owner, TypicalModeFrame parentComponent) {
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

    public void reDrawPanel(TypicalArchive typicalArchive){
        Graphics g = getGraphics();

        initTypicalModePanel(typicalModeFrame, typicalArchive);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panel_width, panel_height);
        drawAllImage(g);
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
