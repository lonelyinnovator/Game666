package com.configuration.java;

import java.util.List;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public abstract class MapModel {

    public abstract int getType();

    public abstract int getPanelWidth();

    public abstract int getPanelHeight();

    public abstract int getColumnNum();

    public abstract int getRowNum();

    public abstract int getPictureWidth();

    public abstract int getPictureHeight();

    public abstract int[] getLatticeTypeList();

    public abstract void setLatticeTypeList(int[] latticeTypeArray);
}
