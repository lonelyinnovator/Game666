package com.control.java;

import com.configuration.java.*;
import com.map.java.Lattice;
import com.map.java.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.text.StyledEditorKit;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public class FlopArchive {
    private Map map;
    private FlopModel mapModel;
    private Theme theme;
    private int hour;
    private int minute;
    private int second;
    private int promptNum;
    private int timeIncreaseNum;

    private FlopArchive(Map map, FlopModel mapModel, Theme theme,
                        int hour, int minute, int second,
                        int promptNum, int timeIncreaseNum) {
        this.map = map;
        this.mapModel = mapModel;
        this.theme = theme;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.promptNum = promptNum;
        this.timeIncreaseNum = timeIncreaseNum;
    }

    public static void saveArchiveInfo(FlopArchive archive) {
        saveArchiveInfo(archive.getMap(), archive.getMapModel(), archive.getTheme(),
                archive.getHour(), archive.getMinute(), archive.getSecond(),
                archive.getPromptNum(), archive.getTimeIncreaseNum());
    }

    public static void saveArchiveInfo(Map map, FlopModel mapModel, Theme theme,
                                       int time, int promptNum, int timeIncreaseNum) {
        int hour = time / 3600;
        int minute = time / 60 % 60;
        int second = time / 60;
        saveArchiveInfo(map, mapModel, theme, hour, minute, second, promptNum, timeIncreaseNum);
    }

    private static void saveArchiveInfo(Map map, FlopModel mapModel, Theme theme,
                                       int hour, int minute, int second,
                                       int promptNum, int timeIncreaseNum) {
        try {
            File file = new File("save/FlopArchive.xml");
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Element root = doc.createElement("info");

            Element e_theme = doc.createElement("theme");
            Element e_types = doc.createElement("types");
            Element e_time = doc.createElement("time");
            Element e_size = doc.createElement("size");
            Element e_map = doc.createElement("map");
            Element e_promptNum = doc.createElement("promptNum");
            Element e_timeIncreaseNum = doc.createElement("timeIncreaseNum");

            root.appendChild(e_theme);
            root.appendChild(e_types);
            root.appendChild(e_time);
            root.appendChild(e_size);
            root.appendChild(e_map);
            root.appendChild(e_promptNum);
            root.appendChild(e_timeIncreaseNum);

            e_theme.setAttribute("value", String.valueOf(theme.getThemeType()));
            e_time.setAttribute("hour", String.valueOf(hour));
            e_time.setAttribute("minute", String.valueOf(minute));
            e_time.setAttribute("second", String.valueOf(second));
            e_size.setAttribute("x", String.valueOf(mapModel.getColumnNum()));
            e_size.setAttribute("y", String.valueOf(mapModel.getRowNum()));
            e_promptNum.setAttribute("value", String.valueOf(promptNum));
            e_timeIncreaseNum.setAttribute("value", String.valueOf(timeIncreaseNum));

            String types = "";
            for(int i = 0; i < mapModel.getLatticeTypeList().length; i++) {
                types = types + mapModel.getLatticeTypeList()[i] + "_";
            }
            e_types.setTextContent(types);

            for (int i = 0; i < mapModel.getRowNum() + 2; i++) {
                Element e_row = doc.createElement("row");
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < mapModel.getColumnNum() + 2; j++) {
                    builder.append(map.getType(new Lattice.Point(j, i))).append("_");
                }
                e_row.setTextContent(builder.toString());
                e_map.appendChild(e_row);
            }

            doc.appendChild(root);

            Transformer ts = TransformerFactory.newInstance().newTransformer();
            ts.setOutputProperty(OutputKeys.INDENT, "yes");
            ts.transform(new DOMSource(doc), new StreamResult(file));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FlopArchive readArchiveInfo() {
        File file = new File("save/FlopArchive.xml");
        if (!file.exists()) {
            return null;
        }
        try {
            Map map;
            Theme theme;
            int hour, minute, second;
            int x, y;
            int promptNum, timeIncreaseNum;
            InputStream is = new FileInputStream(file);
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(is);
            Element root = doc.getDocumentElement();
            Element e_theme = (Element) root.getElementsByTagName("theme").item(0);
            Element e_time = (Element) root.getElementsByTagName("time").item(0);
            Element e_size = (Element) root.getElementsByTagName("size").item(0);
            Element e_map = (Element) root.getElementsByTagName("map").item(0);
            Element e_types = (Element) root.getElementsByTagName("types").item(0);
            Element e_promptNum = (Element) root.getElementsByTagName("promptNum").item(0);
            Element e_timeIncreaseNum = (Element) root.getElementsByTagName("timeIncreaseNum").item(0);

            StringTokenizer tokenizer = new StringTokenizer(e_types.getTextContent(), "_");
            int[] latticeTypeList = new int[tokenizer.countTokens()];
            int index = 0;
            while(tokenizer.hasMoreTokens()){
                latticeTypeList[index++] = Integer.parseInt(tokenizer.nextToken());
            }

            x = Integer.parseInt(e_size.getAttribute("x"));
            y = Integer.parseInt(e_size.getAttribute("y"));
            int[][] typeArray = new int[y + 2][x + 2];
            NodeList rowList = e_map.getElementsByTagName("row");
            if (y + 2 == rowList.getLength()) {
                for (int i = 0; i < rowList.getLength(); i++) {
                    StringTokenizer sk = new StringTokenizer(rowList.item(i).getTextContent(), "_");
                    int j = 0;
                    while (sk.hasMoreTokens()) {
                        typeArray[i][j] = Integer.parseInt(sk.nextToken());
                        j++;
                    }
                }
            }

            theme = ThemeFactory.getTheme(Integer.parseInt(e_theme.getAttribute("value")));
            hour = Integer.parseInt(e_time.getAttribute("hour"));
            minute = Integer.parseInt(e_time.getAttribute("minute"));
            second = Integer.parseInt(e_time.getAttribute("second"));
            promptNum = Integer.parseInt(e_promptNum.getAttribute("value"));
            timeIncreaseNum = Integer.parseInt(e_timeIncreaseNum.getAttribute("value"));
            FlopModel model = ModelFactory.getFlogModel(theme);
            model.setLatticeTypeList(latticeTypeList);
            map = Map.getOldMap(model, typeArray);

            is.close();
            return new FlopArchive(map, model, theme, hour, minute, second, promptNum, timeIncreaseNum);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static FlopArchive newArchive() {
        Theme theme = ThemeFactory.getRandomTheme();
        FlopModel model = ModelFactory.getFlogModel(theme);
        Map map = Map.getNewMap(model);
//        saveArchiveInfo(map, model, theme, 0, 2, 30);
        return new FlopArchive(map, model, theme, 0, 2, 30,
                3, 3);
    }

    public static void deleteArchive() {
        File file = new File("save/FlopArchive.xml");
        if(file.exists()) {
            System.gc();
            file.delete();
        }
    }

    public Map getMap() {
        return map;
    }

    public FlopModel getMapModel() {
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

    public int getPromptNum() {
        return promptNum;
    }

    public int getTimeIncreaseNum() {
        return timeIncreaseNum;
    }

//    public static void main(String[] args) {
//        Theme theme = ThemeFactory.getTheme(2);
//        FlopModel model = ModelFactory.getFlogModel(theme);
//        model.addLatticeTypes(new int[]{1,2,3,4});
//        Map map = Map.getNewMap(model);
//        map.setLimitLattice(1, 4);
//        map.setLimitLattice(2, 4);
//        map.setLimitLattice(3, 4);
//        map.setLimitLattice(4, 4);
//        saveArchiveInfo(map, model, theme, 0);

//        FlopArchive archive = FlopArchive.newArchive();
//        saveArchiveInfo(archive.map, archive.mapModel, archive.theme,
//                archive.hour, archive.minute, archive.second,
//                archive.promptNum, archive.promptNum);
//
//        FlopArchive archive = readArchiveInfo();
//        Map map = archive.getMap();
//        int hour = archive.getHour();
//        int minute = archive.getMinute();
//        int second = archive.getSecond();
//        int promptNum = archive.getPromptNum();
//        int timeIncreaseNum = archive.getTimeIncreaseNum();
//        System.out.println(hour);
//        System.out.println(minute);
//        System.out.println(second);
//        System.out.println(promptNum);
//        System.out.println(timeIncreaseNum);
//        map.check();
//        int[] types = map.getTypeList();
//        for(int type: types) {
//            System.out.println(type);
//        }
//    }
}
