package com.configuration.java;

import java.util.Random;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public abstract class ThemeFactory {
    private static final int themeAmount = 2;
    private static final int[] typeList_1 =
            {101,102,103,104,105,106,107,108,109,110};
    private static final int[] typeList_2 =
            {201,202,203,204,205,206,207,208,209,210};

    public static int getThemeAmount() {
        return themeAmount;
    }

    public static Theme getTheme(int choice) {
        if(choice == 1) {
            return new Theme(1, typeList_1, "", new String[1]);
        }else if(choice == 2) {
            return new Theme(2, typeList_2, "", new String[1]);
        }
        throw new RuntimeException();
    }

    public static Theme getRandomTheme() {
//        return ThemeFactory.getTheme(new Random().nextInt(amount)+1);
        return ThemeFactory.getTheme(2);
    }
}
