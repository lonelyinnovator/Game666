package com.control.java;

import com.configuration.java.Theme;
import com.configuration.java.MapModel;
import com.map.java.Lattice;
import com.map.java.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public class EndlessArchive {
    private Map map;
    private MapModel mapModel;
    private Theme theme;
    private int hour;
    private int minute;
    private int second;
    private int score;
    private int roundNum;

    private EndlessArchive(Map map, MapModel mapModel, Theme theme,
                           int hour, int minute, int second,
                           int score, int roundNum) {
        this.map = map;
        this.mapModel = mapModel;
        this.theme = theme;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.score = score;
        this.roundNum = roundNum;
    }

    public static EndlessArchive getNewEndlessArchive(MapModel mapModel, Theme theme) {
        return null;
    }

    public Map getMap() {
        return map;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public Theme getTheme() {
        return theme;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getScore() {
        return score;
    }
}
