package com.map.java;

/**
 *@author 王瀚霆
 */
public class Lattice {
    /**
     * int type: map中格子的 type
     * 内部公开类 Point: 格子的坐标
     *      Point 属性：【x：横坐标， y：纵坐标】
     *      Point 方法：【equals】
     */
    private int type;
    private String imgPath;

    public Lattice(int type) {
        this.type = type;
        int imgIndex = type % 100;
        int fileIndex = type / 100;
        this.imgPath = String.format("res/material%d/%d.png", fileIndex, imgIndex);
    }

    public int getType() {
        return type;
    }

    public String getImgPath() {
        return imgPath;
    }

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return "x " + this.x + " y " + this.y + " ";
        }

        public boolean equals(Point point) {
            return x == point.x && y == point.y;
        }
    }
}
