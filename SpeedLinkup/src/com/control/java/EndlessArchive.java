package com.control.java;

import com.configuration.java.*;
import com.map.java.Lattice;
import com.map.java.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
import java.util.StringTokenizer;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public class EndlessArchive {
    private Map map;
    private EndlessModel mapModel;
    private Theme theme;
    private int hour;
    private int minute;
    private int second;
    private int score;
    private int roundNum;

    private EndlessArchive(Map map, EndlessModel mapModel, Theme theme,
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

    public static void saveArchiveInfo(EndlessArchive archive) {
        saveArchiveInfo(archive.getMap(), archive.getMapModel(),archive.getTheme(),
                archive.getHour(), archive.getMinute(), archive.getSecond(),
                archive.getScore(), archive.getRoundNum());
    }

    public static void saveArchiveInfo(Map map, MapModel mapModel, Theme theme,
                                       int time, int score, int roundNum) {
        int hour = time / 3600;
        int minute = time / 60 % 60;
        int second = time % 60;
        saveArchiveInfo(map, mapModel, theme, hour, minute, second, score, roundNum);
    }


    public static void saveArchiveInfo(Map map, MapModel mapModel, Theme theme,
                                       int hour, int minute, int second,
                                       int score, int roundNum) {
        try {
            File file = new File("save/EndlessArchive.xml");
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
            Element e_score = doc.createElement("score");
            Element e_round = doc.createElement("round");

            root.appendChild(e_theme);
            root.appendChild(e_types);
            root.appendChild(e_time);
            root.appendChild(e_size);
            root.appendChild(e_map);
            root.appendChild(e_score);
            root.appendChild(e_round);

            e_theme.setAttribute("value", String.valueOf(theme.getThemeType()));
            e_time.setAttribute("hour", String.valueOf(hour));
            e_time.setAttribute("minute", String.valueOf(minute));
            e_time.setAttribute("second", String.valueOf(second));
            e_size.setAttribute("x", String.valueOf(mapModel.getColumnNum()));
            e_size.setAttribute("y", String.valueOf(mapModel.getRowNum()));
            e_score.setAttribute("value", String.valueOf(score));
            e_round.setAttribute("value", String.valueOf(roundNum));

            String types = "";
            for(int i = 0; i < map.getTypeList().length; i++) {
                types = types + map.getTypeList()[i] + "_";
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

    public static EndlessArchive readArchiveInfo() {
        File file = new File("save/EndlessArchive.xml");
        if(!file.exists()) {
            return null;
        }
        try {
            Map map;
            Theme theme;
            int hour, minute, second;
            int x, y;
            int score, roundNum;
            InputStream is = new FileInputStream(file);
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(is);
            Element root = doc.getDocumentElement();
            Element e_theme = (Element) root.getElementsByTagName("theme").item(0);
            Element e_time = (Element) root.getElementsByTagName("time").item(0);
            Element e_size = (Element) root.getElementsByTagName("size").item(0);
            Element e_score = (Element) root.getElementsByTagName("score").item(0);
            Element e_round = (Element) root.getElementsByTagName("round").item(0);
            Element e_types = (Element) root.getElementsByTagName("types").item(0);
            Element e_map = (Element) root.getElementsByTagName("map").item(0);

            StringTokenizer tokenizer = new StringTokenizer(e_types.getTextContent(), "_");
            int[] latticeTypeList = new int[tokenizer.countTokens()];
            int index = 0;
            while(tokenizer.hasMoreTokens()) {
                latticeTypeList[index ++] = Integer.parseInt(tokenizer.nextToken());
            }

            x = Integer.parseInt(e_size.getAttribute("x"));
            y = Integer.parseInt(e_size.getAttribute("y"));
            int[][] typeArray = new int[y + 2][x + 2];
            NodeList rowNodeList = e_map.getElementsByTagName("row");
            if (y + 2 == rowNodeList.getLength()) {
                for (int i = 0; i < rowNodeList.getLength(); i++) {
                    StringTokenizer rowBuilder = new StringTokenizer(rowNodeList.item(i).getTextContent(), "_");
                    int j = 0;
                    while (rowBuilder.hasMoreTokens()) {
                        typeArray[i][j] = Integer.parseInt(rowBuilder.nextToken());
                        j++;
                    }
                }
            }

            theme = ThemeFactory.getTheme(Integer.parseInt(e_theme.getAttribute("value")));
            score = Integer.parseInt(e_score.getAttribute("value"));
            roundNum = Integer.parseInt(e_round.getAttribute("value"));
            hour = Integer.parseInt(e_time.getAttribute("hour"));
            minute = Integer.parseInt(e_time.getAttribute("minute"));
            second = Integer.parseInt(e_time.getAttribute("second"));

            EndlessModel model = ModelFactory.getEndlessModel(theme);
            map = Map.getOldMap(model, typeArray);


            is.close();
            return new EndlessArchive(map, model, theme, hour, minute, second,
                    score, roundNum);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EndlessArchive newArchive() {
        Theme theme = ThemeFactory.getRandomTheme();
        EndlessModel model = ModelFactory.getEndlessModel(theme);
        Map map = Map.getNewMap(model);
//        saveArchiveInfo(map, model, theme,
//                0, 2, 30, 0 ,1);
        return new EndlessArchive(map, model, theme,
                0, 2, 30, 0,1);
    }

    public static EndlessArchive newArchive(int[] addedTypes)  {
        Theme theme = ThemeFactory.getRandomTheme();
        EndlessModel model = ModelFactory.getEndlessModel(theme);
        model.addLatticeTypes(addedTypes);
        Map map = Map.getNewMap(model);
        return new EndlessArchive(map, model, theme,
                0, 3, 30,0, 1);
    }

    public static void deleteArchive() {
        File file = new File("save/EndlessArchive");
        if(file.exists()) {
            System.gc();
            file.delete();
        }
    }

    public Map getMap() {
        return map;
    }

    public EndlessModel getMapModel() {
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

    public int getRoundNum() {
        return roundNum;
    }

//    public static void main(String[] args) {
//        Theme theme = ThemeFactory.getTheme(2);
//        EndlessModel model = ModelFactory.getEndlessModel(theme);
//        model.addLatticeTypes(new int[]{108,109});
//        Map map = Map.getNewMap(model);
//        saveArchiveInfo(map, model, theme, 0,2,30,
//                1200,4);

//        EndlessArchive archive = EndlessArchive.readArchiveInfo();
//        Map map = archive.getMap();
//        int score = archive.getScore();
//        int roundNum = archive.getRoundNum();
//        int hour = archive.getHour();
//        int minute = archive.getMinute();
//        int second = archive.getSecond();
//        System.out.println(score);
//        System.out.println(roundNum);
//        System.out.println(hour);
//        System.out.println(minute);
//        System.out.println(second);
//        map.check();
//    }
}
