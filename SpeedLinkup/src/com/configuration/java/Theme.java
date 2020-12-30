package com.configuration.java;

import java.util.Arrays;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public final class Theme {
    private int themeType;
    private int[] themeLatticeTypeList;
    private String background_img_path;
    private String[] music_path;

    public Theme(int themeType, int[] themeLatticeTypeList, String background_img_path, String[] music_path) {
        this.themeType = themeType;
        this.themeLatticeTypeList = themeLatticeTypeList;
        this.background_img_path = background_img_path;
        this.music_path = music_path;
    }

    public int getThemeType() {
        return themeType;
    }

    public String getBackgroundImgPath() {
        return background_img_path;
    }

    public String[] getBackgroundMusicPaths() {
        return music_path;
    }

    public int[] getLatticeTypeList(int typeNum) {
        if(typeNum > themeLatticeTypeList.length) {
            throw new RuntimeException("超出数组数量");
        }
        return Arrays.copyOf(themeLatticeTypeList, typeNum);
    }
}
