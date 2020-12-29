package com.configuration.java;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public abstract class ModelFactory {
    public static MapModel getTypicalModel(int choice, Theme theme) {
        if(choice == 1) {
            return new SimpleModel(theme);
        }
        if(choice == 2) {
            return new MiddleModel(theme);
        }
        if(choice == 3) {
            return new HardModel(theme);
        }
        throw new RuntimeException();
    }

//    public static EndlessModel getEndlessModel(int[] latticeTypeList) {
//        return new EndlessModel(latticeTypeList);
//    }

    public static FlopModel getFlogModel(Theme theme) {
        return new FlopModel(theme);
    }
}
