package com.configuration.java;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public class PuzzleModel extends MapModel{
    private final int PANEL_WIDTH = 900;
    private final int PANEL_HEIGHT = 750;
    private final int MAP_SIZE_X = 10;
    private final int MAP_SIZE_Y = 10;
    private final int PICTURE_WIDTH = 60;
    private final int PICTURE_HEIGHT = 60;
    private int[] latticeTypeArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    public int getType() {
        return 2;
    }

    public int getPanelWidth() {
        return PANEL_WIDTH;
    }

    public int getPanelHeight() {
        return PANEL_HEIGHT;
    }

    public int getColumnNum() {
        return MAP_SIZE_X;
    }

    public int getRowNum() {
        return MAP_SIZE_Y;
    }

    public int getPictureWidth() {
        return PICTURE_WIDTH;
    }

    public int getPictureHeight() {
        return PICTURE_HEIGHT;
    }

    public int[] getLatticeTypeList() {
        return latticeTypeArray;
    }

    public void setLatticeTypeList(int[] latticeTypeArray) {
        this.latticeTypeArray = latticeTypeArray;
    }
}
