package com.Ui.java;

import com.control.java.EndlessArchive;

import java.awt.*;

/**
 * main
 *
 * @author 沈佳军
 * @date 2020/12/22
 */
public class EndlessModeMain {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                EndlessArchive endlessArchive = EndlessArchive.readArchiveInfo();
                if (endlessArchive == null){
                    endlessArchive = EndlessArchive.newArchive();
                }
                EndlessModeFrame endlessModeFrame = new EndlessModeFrame(endlessArchive);
                endlessModeFrame.setVisible(true);
            }
        });
    }
}