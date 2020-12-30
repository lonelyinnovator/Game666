package com.configuration.java;

import java.util.Random;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public abstract class ThemeFactory {
    private static final int themeAmount = 6;
    private static final int[] typeList_1 =
            {101,102,103,104,105,106,107,108,109,110};
    private static final int[] typeList_2 =
            {201,202,203,204,205,206,207,208,209,210};
    private static final int[] typeList_3 =
            {301,302,303,304,305,306,307,308,309,310};
    private static final int[] typeList_4 =
            {401,402,403,404,405,406,407,408,409,410};
    private static final int[] typeList_5 =
            {501,502,503,504,505,506,507,508,509,510};
    private static final int[] typeList_6 =
            {601,602,603,604,605,606,607,608,609,610};



    public static int getThemeAmount() {
        return themeAmount;
    }

    public static Theme getTheme(int choice) {
        if(choice == 1) {
            return new Theme(1, typeList_1, "", new String[1]);
        }else if(choice == 2) {
            return new Theme(2, typeList_2, "", new String[1]);
        }else if(choice == 3) {
            return new Theme(3, typeList_3, "", new String[1]);
        }else if(choice == 4) {
            return new Theme(4, typeList_3, "", new String[1]);
        }else if(choice == 5) {
            return new Theme(5, typeList_6, "", new String[1]);
        }else if(choice == 6) {
            return new Theme(6, typeList_6, "", new String[1]);
        }
        throw new RuntimeException();
    }

    public static Theme getRandomTheme() {
        return ThemeFactory.getTheme(new Random().nextInt(themeAmount)+1);
//        return ThemeFactory.getTheme(2);
    }
}
