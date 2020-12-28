package com.Ui.java;

import com.configuration.java.ModelFactory;
import com.configuration.java.ThemeFactory;
import com.control.java.NormalArchive;

import java.awt.*;

/**
 * main
 *
 * @author 沈佳军
 * @date 2020/12/22
 */
public class TypicalModeMain {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                NormalArchive normalArchive = NormalArchive.readArchiveInfo();
                if (normalArchive == null){
                    normalArchive = NormalArchive.newArchive(ModelFactory.getModel(2, ThemeFactory.getTheme(2)), 0, 2, 0);
                }
                TypicalModeFrame typicalModeFrame = new TypicalModeFrame(normalArchive);
                typicalModeFrame.setVisible(true);
            }
        });
    }
}