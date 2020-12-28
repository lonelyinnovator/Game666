package com.control.java;

import com.configuration.java.Theme;
import com.configuration.java.MapModel;
import com.configuration.java.ModelFactory;
import com.configuration.java.ThemeFactory;
import com.map.java.Lattice;
import com.map.java.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import sun.reflect.generics.tree.TypeSignature;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.StringTokenizer;

/**
 * @姓名 王瀚霆
 * @学号 2019302841
 * @描述
 */
public final class NormalArchive {

    private Map map;
    private MapModel mapModel;
    private Theme theme;
    private int hour;
    private int minute;
    private int second;

    private NormalArchive(Map map, MapModel mapModel, Theme theme, int hour, int minute, int second) {
        this.map = map;
        this.mapModel = mapModel;
        this.theme = theme;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public static void saveArchiveInfo(Map map, MapModel mapModel, Theme theme, int hour, int minute, int second) {
        try {
            File file = new File("save/NormalArchive.xml");
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Element root = doc.createElement("info");

            Element e_model = doc.createElement("mapModel");
            Element e_theme = doc.createElement("theme");
            Element e_types = doc.createElement("types");
            Element e_time = doc.createElement("time");
            Element e_size = doc.createElement("size");
            Element e_map = doc.createElement("map");

            root.appendChild(e_model);
            root.appendChild(e_theme);
            root.appendChild(e_types);
            root.appendChild(e_time);
            root.appendChild(e_size);
            root.appendChild(e_map);

            e_model.setAttribute("value", String.valueOf(mapModel.getType()));
            e_theme.setAttribute("value", String.valueOf(theme.getThemeType()));
            e_time.setAttribute("hour", String.valueOf(hour));
            e_time.setAttribute("minute", String.valueOf(minute));
            e_time.setAttribute("second", String.valueOf(second));
            e_size.setAttribute("x", String.valueOf(mapModel.getColumnNum()));
            e_size.setAttribute("y", String.valueOf(mapModel.getRowNum()));

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NormalArchive readArchiveInfo() {
        File file = new File("save/NormalArchive.xml");
        if (!file.exists()) {
            return null;
        }
        try {
            Map map;
            MapModel mapModel;
            Theme theme;
            int hour, minute, second;
            int x, y;
            InputStream is = new FileInputStream(file);
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(is);
            Element root = doc.getDocumentElement();
            Element e_theme = (Element) root.getElementsByTagName("theme").item(0);
            theme = ThemeFactory.getTheme(Integer.parseInt(e_theme.getAttribute("value")));
            Element e_model = (Element) root.getElementsByTagName("mapModel").item(0);
            mapModel = ModelFactory.getModel(Integer.parseInt(e_model.getAttribute("value")), theme);
            Element e_time = (Element) root.getElementsByTagName("time").item(0);
            hour = Integer.parseInt(e_time.getAttribute("hour"));
            minute = Integer.parseInt(e_time.getAttribute("minute"));
            second = Integer.parseInt(e_time.getAttribute("second"));
            Element e_size = (Element) root.getElementsByTagName("size").item(0);
            x = Integer.parseInt(e_size.getAttribute("x"));
            y = Integer.parseInt(e_size.getAttribute("y"));
            int[][] typeArray = new int[y + 2][x + 2];
            Element e_map = (Element) root.getElementsByTagName("map").item(0);
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
            is.close();
            map = Map.getOldMap(mapModel, theme, typeArray);
            return new NormalArchive(map, mapModel, theme, hour, minute, second);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NormalArchive newArchive(MapModel mapModel, int hour, int minute, int second) {
        Theme theme = ThemeFactory.getRandomTheme();
        Map map = Map.getNewMap(mapModel, theme);
        saveArchiveInfo(map, mapModel, theme, hour, minute, second);
        return new NormalArchive(map, mapModel, theme, hour, minute, second);
    }

    public static void deleteArchive() {
        File file = new File("save/NormalArchive.xml");
        if(file.exists()) {
            System.gc();
            file.delete();
        }
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
}