package com.configuration.java;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public abstract class ModelFactory {
    public static MapModel getModel(int choice, Theme theme) {
        if(choice == 1) {
            return new SimpleModel(theme.getLatticeTypeList(SimpleModel.TYPE_NUMBER));
        }
        if(choice == 2) {
            return new MiddleModel(theme.getLatticeTypeList(MiddleModel.TYPE_NUMBER));
        }
        if(choice == 3) {
            return new HardModel(theme.getLatticeTypeList(HardModel.TYPE_NUMBER));
        }
        throw new RuntimeException();
    }
}
