package com.configuration.java;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public final class MiddleModel extends MapModel {
    private final int PANEL_WIDTH = 750;
    private final int PANEL_HEIGHT = 750;
    private final int MAP_SIZE_X = 10;
    private final int MAP_SIZE_Y = 10;
    private final int PICTURE_WIDTH = 60;
    private final int PICTURE_HEIGHT = 60;
    private final int TYPE_NUMBER = 10;
    private Theme theme;
    private int[] latticeTypeList;

    public MiddleModel(Theme theme) {
        this.theme = theme;
        this.latticeTypeList = theme.getLatticeTypeList(TYPE_NUMBER);
    }
    public int getModelType() {
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

    @Override
    public Theme getTheme() {
        return theme;
    }

    public void setLatticeTypeList(int[] latticeTypeArray) {
        if(latticeTypeArray.length != TYPE_NUMBER) {
            throw new RuntimeException("数量不匹配！");
        }
        this.latticeTypeList = latticeTypeArray;
    }

    @Override
    public int getTypeNum() {
        return TYPE_NUMBER;
    }
}
