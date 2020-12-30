package com.Ui.java;

import com.configuration.java.ModelFactory;
import com.configuration.java.ThemeFactory;
import com.control.java.FlopArchive;

import java.awt.*;

public class FlopModeMain {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FlopArchive archive = FlopArchive.readArchiveInfo();
                if (archive == null){
                    archive = FlopArchive.newArchive();
                }
//                MainFrame mainFrame = new MainFrame(normalArchive);
                FlopModeFrame flopModeFrame = new FlopModeFrame(archive);
                flopModeFrame.setVisible(true);
                flopModeFrame.setResizable(true);
//                mainFrame.setVisible(true);
//                mainFrame.setResizable(true);
            }
        });
    }
}
