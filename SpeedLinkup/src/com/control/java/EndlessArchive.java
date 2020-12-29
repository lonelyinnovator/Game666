//package com.control.java;
//
//import com.configuration.java.*;
//import com.map.java.Lattice;
//import com.map.java.Map;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Source;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.StringTokenizer;
//
///**
// * @姓名 王瀚霆
// * @学号 2019302841
// * @描述
// */
//public class EndlessArchive {
//    private Map map;
//    private MapModel mapModel;
//    private Theme theme;
//    private int hour;
//    private int minute;
//    private int second;
//    private int score;
//    private int roundNum;
//
//    private EndlessArchive(Map map, MapModel mapModel, Theme theme,
//                           int hour, int minute, int second,
//                           int score, int roundNum) {
//        this.map = map;
//        this.mapModel = mapModel;
//        this.theme = theme;
//        this.hour = hour;
//        this.minute = minute;
//        this.second = second;
//        this.score = score;
//        this.roundNum = roundNum;
//    }
//
//    public static void saveArchiveInfo(Map map, MapModel mapModel, Theme theme,
//                                       int hour, int minute, int second,
//                                       int score, int roundNum) {
//        try {
//            File file = new File("save/EndlessArchive.xml");
//            Document doc = DocumentBuilderFactory
//                    .newInstance()
//                    .newDocumentBuilder()
//                    .newDocument();
//            Element root = doc.createElement("info");
//
//            Element e_theme = doc.createElement("theme");
//            Element e_types = doc.createElement("types");
//            Element e_time = doc.createElement("time");
//            Element e_size = doc.createElement("size");
//            Element e_map = doc.createElement("map");
//            Element e_score = doc.createElement("score");
//            Element e_round = doc.createElement("round");
//
//            root.appendChild(e_theme);
//            root.appendChild(e_types);
//            root.appendChild(e_time);
//            root.appendChild(e_size);
//            root.appendChild(e_map);
//            root.appendChild(e_score);
//            root.appendChild(e_round);
//
//            e_theme.setAttribute("value", String.valueOf(theme.getThemeType()));
//            e_time.setAttribute("hour", String.valueOf(hour));
//            e_time.setAttribute("minute", String.valueOf(minute));
//            e_time.setAttribute("second", String.valueOf(second));
//            e_size.setAttribute("x", String.valueOf(mapModel.getColumnNum()));
//            e_size.setAttribute("y", String.valueOf(mapModel.getRowNum()));
//            e_score.setAttribute("value", String.valueOf(score));
//            e_round.setAttribute("value", String.valueOf(roundNum));
//
//            String types = "";
//            for(int i = 0; i < map.getTypeList().length; i++) {
//                types = types + map.getTypeList()[i] + "_";
//            }
//            e_types.setTextContent(types);
//
//            for (int i = 0; i < mapModel.getRowNum() + 2; i++) {
//                Element e_row = doc.createElement("row");
//                StringBuilder builder = new StringBuilder();
//                for (int j = 0; j < mapModel.getColumnNum() + 2; j++) {
//                    builder.append(map.getType(new Lattice.Point(j, i))).append("_");
//                }
//                e_row.setTextContent(builder.toString());
//                e_map.appendChild(e_row);
//            }
//
//            doc.appendChild(root);
//
//            Transformer ts = TransformerFactory.newInstance().newTransformer();
//            ts.setOutputProperty(OutputKeys.INDENT, "yes");
//            ts.transform(new DOMSource(doc), new StreamResult(file));
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static EndlessArchive readArchiveInfo() {
//        File file = new File("save/EndlessArchive.xml");
//        if(!file.exists()) {
//            return null;
//        }
//        try {
//            Map map;
//            Theme theme;
//            int hour, minute, second;
//            int x, y;
//            int score, roundNum;
//            InputStream is = new FileInputStream(file);
//            Document doc = DocumentBuilderFactory
//                    .newInstance()
//                    .newDocumentBuilder()
//                    .parse(is);
//            Element root = doc.getDocumentElement();
//            Element e_theme = (Element) root.getElementsByTagName("theme").item(0);
//            theme = ThemeFactory.getTheme(Integer.parseInt(e_theme.getAttribute("value")));
//            Element e_score = (Element) root.getElementsByTagName("score").item(0);
//            score = Integer.parseInt(e_score.getAttribute("value"));
//            Element e_round = (Element) root.getElementsByTagName("round").item(0);
//            roundNum = Integer.parseInt(e_round.getAttribute("value"));
//
//            Element e_time = (Element) root.getElementsByTagName("time").item(0);
//            hour = Integer.parseInt(e_time.getAttribute("hour"));
//            minute = Integer.parseInt(e_time.getAttribute("minute"));
//            second = Integer.parseInt(e_time.getAttribute("second"));
//            Element e_size = (Element) root.getElementsByTagName("size").item(0);
//            x = Integer.parseInt(e_size.getAttribute("x"));
//            y = Integer.parseInt(e_size.getAttribute("y"));
//            Element e_types = (Element) root.getElementsByTagName("types").item(0);
//            StringTokenizer typeBuilder = new StringTokenizer(e_types.getTextContent(), "_");
//            int[] latticeTypeList = new int[typeBuilder.countTokens()];
//            int index = 0;
//            while(typeBuilder.hasMoreTokens()) {
//                latticeTypeList[index ++] = Integer.parseInt(typeBuilder.nextToken());
//            }
//            int[][] typeArray = new int[y + 2][x + 2];
//            Element e_map = (Element) root.getElementsByTagName("map").item(0);
//            NodeList rowNodeList = e_map.getElementsByTagName("row");
//            if (y + 2 == rowNodeList.getLength()) {
//                for (int i = 0; i < rowNodeList.getLength(); i++) {
//                    StringTokenizer rowBuilder = new StringTokenizer(rowNodeList.item(i).getTextContent(), "_");
//                    int j = 0;
//                    while (rowBuilder.hasMoreTokens()) {
//                        typeArray[i][j] = Integer.parseInt(rowBuilder.nextToken());
//                        j++;
//                    }
//                }
//            }
//            is.close();
//            EndlessModel model = ModelFactory.getEndlessModel(latticeTypeList);
//            map = Map.getOldMap(model, theme, typeArray);
//            return new EndlessArchive(map, model, theme, hour, minute, second, score, roundNum);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public Map getMap() {
//        return map;
//    }
//
//    public MapModel getMapModel() {
//        return mapModel;
//    }
//
//    public Theme getTheme() {
//        return theme;
//    }
//
//    public int getHour() {
//        return hour;
//    }
//
//    public int getMinute() {
//        return minute;
//    }
//
//    public int getSecond() {
//        return second;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    public int getRoundNum() {
//        return roundNum;
//    }
//
//    public static void main(String[] args) {
////        EndlessModel model = ModelFactory.getEndlessModel(new int[]{101,102,103,104,105,106,107});
////        model.addLatticeTypes(new int[]{108,109});
////        saveArchiveInfo(Map.getNewMap(model, ThemeFactory.getTheme(2)),
////                model, ThemeFactory.getTheme(2), 0,2,30,
////                1200,4);
//
//
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
//}
