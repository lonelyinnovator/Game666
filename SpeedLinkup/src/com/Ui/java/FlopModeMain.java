package com.Ui.java;

import com.configuration.java.ModelFactory;
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
                FlopModeFrame flopModeFrame = new FlopModeFrame(archive);
                flopModeFrame.setVisible(true);
                flopModeFrame.setResizable(true);
            }
        });
    }
}
