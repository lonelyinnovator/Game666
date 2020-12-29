package com.Ui.java;

import com.configuration.java.ModelFactory;
import com.configuration.java.ThemeFactory;
import com.control.java.TypicalArchive;

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
                TypicalArchive typicalArchive = TypicalArchive.readArchiveInfo();
                if (typicalArchive == null){
                    typicalArchive = TypicalArchive.newArchive(ModelFactory.getTypicalModel(2, ThemeFactory.getTheme(2)));
                }
                TypicalModeFrame typicalModeFrame = new TypicalModeFrame(typicalArchive);
                typicalModeFrame.setVisible(true);
            }
        });
    }
}