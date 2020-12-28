package com.configuration.java;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public final class SimpleModel extends MapModel {
//    private static final int PANEL_WIDTH = 1000;
//    private static final int PANEL_HEIGHT = 800;
//    private static final int COLUMN_NUMBER = 10;
//    private static final int ROW_NUM = 10;
//    private static final int PICTURE_WIDTH = 65;
//    private static final int PICTURE_HEIGHT = 65;
//    private static final List<Integer> LATTICE_TYPE_LIST =

    private final int PANEL_WIDTH = 720;
    private final int PANEL_HEIGHT = 720;
    private final int MAP_SIZE_X = 10;
    private final int MAP_SIZE_Y = 10;
    private final int PICTURE_WIDTH = 60;
    private final int PICTURE_HEIGHT = 60;
    public static final int TYPE_NUMBER = 10;
    private int[] latticeTypeList;

    public SimpleModel(int[] latticeTypeList) {
        if(latticeTypeList.length != TYPE_NUMBER) {
            throw new RuntimeException("与设定的type number 不一致！");
        }
        this.latticeTypeList = latticeTypeList;
    }

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
        return latticeTypeList;
    }

    public void setLatticeTypeList(int[] latticeTypeArray) {
        this.latticeTypeList = latticeTypeArray;
    }
}
