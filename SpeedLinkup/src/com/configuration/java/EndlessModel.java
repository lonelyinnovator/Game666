package com.configuration.java;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import java.util.HashSet;
import java.util.Set;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public class EndlessModel extends MapModel{
    private final int PANEL_WIDTH = 900;
    private final int PANEL_HEIGHT = 750;
    private final int MAP_SIZE_X = 10;
    private final int MAP_SIZE_Y = 10;
    private final int PICTURE_WIDTH = 60;
    private final int PICTURE_HEIGHT = 60;
    private final int TYPE_NUMBER = 10; //最基本的种类数
    private Theme theme;
    private int typeNum;
    private int[] latticeTypeList;

    public EndlessModel(Theme theme) {
        this.typeNum = TYPE_NUMBER;
        this.latticeTypeList = theme.getLatticeTypeList(typeNum);
    }

    public int getModelType() {
        return 4;
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

    public Theme getTheme() {
        return theme;
    }

    public int getTypeNum() {
        return typeNum;
    }

    public void addLatticeTypes(int[] newLatticeTypes) {
        Set<Integer> set = new HashSet<>();
        for(int type : this.latticeTypeList) {
            set.add(type);
        }
        for(int type: newLatticeTypes) {
            if(type <= 0) {
                throw new RuntimeException("type必须为正数");
            }
            set.add(type);
        }
        this.typeNum = set.size();
        this.latticeTypeList = new int[typeNum];
        int index = 0;
        for(int type: set) {
            latticeTypeList[index++] = type;
        }
    }

    public void setLatticeTypeList(int[] latticeTypeArray) {
        this.typeNum = latticeTypeArray.length;
        this.latticeTypeList = latticeTypeArray;
    }
}
