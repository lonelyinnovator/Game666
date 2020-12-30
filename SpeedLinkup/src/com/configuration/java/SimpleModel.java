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

    private final int PANEL_WIDTH = 750;
    private final int PANEL_HEIGHT = 750;
    private final int MAP_SIZE_X = 10;
    private final int MAP_SIZE_Y = 10;
    private final int PICTURE_WIDTH = 60;
    private final int PICTURE_HEIGHT = 60;
    private final int TYPE_NUMBER = 10;
    private int[] latticeTypeList;
    private Theme theme;

    public SimpleModel(Theme theme) {
        this.theme = theme;
        this.latticeTypeList = theme.getLatticeTypeList(TYPE_NUMBER);
    }

    public int getModelType() {
        return 1;
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

    @Override
    public Theme getTheme() {
        return theme;
    }

    public void setLatticeTypeList(int[] latticeTypeArray) {
        if (latticeTypeArray.length != TYPE_NUMBER) {
            throw new RuntimeException("数量不匹配！");
        }
        this.latticeTypeList = latticeTypeArray;
    }

    @Override
    public int getTypeNum() {
        return TYPE_NUMBER;
    }
}
