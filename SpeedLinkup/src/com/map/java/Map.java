package com.map.java;

import com.configuration.java.MapModel;
import com.configuration.java.Theme;

import java.util.*;

/**
 *@author 王瀚霆
 */

public class Map {

    private int rowNum;
    private int columnNum;
    private int typeNum;
    private Lattice[][] lattices;
    private int restLatticeAmount;
    private int[] typeList;
    private Path path;

    /**
     *构建新地图
     */
    public static Map getNewMap(MapModel mapModel, Theme theme) {
        return new Map(mapModel.getRowNum(), mapModel.getColumnNum(),
                mapModel.getLatticeTypeList());
    }

    /**
     *从一个数组中获取一个地图
     */
    public static Map getOldMap(MapModel mapModel, Theme theme, int[][] typeArray) {
        return new Map(mapModel.getRowNum(), mapModel.getColumnNum(),
                mapModel.getLatticeTypeList(), typeArray);
    }

    /**
     * 获取指定坐标的方块种类,如果为空则返回 0
     */
    public int getType(Lattice.Point point) {
        if (lattices[point.y][point.x] != null) {
            return lattices[point.y][point.x].getType();
        }
        return 0;
    }

    /**
     * 判断是否符合相消的条件
     * x为横向坐标，y为纵向坐标
     */
    public boolean judge(Lattice.Point point1, Lattice.Point point2, boolean changePath) {
        this.path = new Path(point1);
        int x1 = point1.x, y1 = point1.y;
        int x2 = point2.x, y2 = point2.y;
        if (lattices[y1][x1] == null || lattices[y2][x2] == null) {
            return false;
        }
        if (lattices[y1][x1].getType() == lattices[y2][x2].getType()) {
            if (judgeHorizon(point1, point2, changePath)) {
//                System.out.println(getPath());
//                System.out.println("Horizon");
                return true;
            }
            if (judgeVertical(point1, point2, changePath)) {
//                System.out.println(getPath());
//                System.out.println("Vertical");
                return true;
            }
            if (judgeTurnOnce(point1, point2, changePath)) {
//                System.out.println(getPath());
//                System.out.println("once");
                return true;
            }
            if (judgeTurnTwice(point1, point2, changePath)) {
//                System.out.println(getPath());
//                System.out.println("twice");
                return true;
            }
        }
        return false;
    }
    public boolean judge(Lattice.Point point1, Lattice.Point point2) {
        return judge(point1, point2, true);
    }

    /**
     * 判断指定坐标的格子是否为空
     */
    public boolean isNULL(Lattice.Point point) {
        return lattices[point.y][point.x] == null;
    }

    /**
     * 将指定坐标的格子置空
     */
    public void removeLattice(Lattice.Point point) {
        this.restLatticeAmount -= 1;
        lattices[point.y][point.x] = null;
    }

    /**
     *获取剩余的格子数量
     */
    public int getRestLatticeAmount() {
        return restLatticeAmount;
    }

    /**
     * 获取两个能相消的格子的路径 必须能相消！
     */
    public List<Lattice.Point> getPath() {
        return path.getPathPoints();
    }

    /**
     * 获取两个能相消的格子，如果没有返回 null
     */
    public List<Lattice.Point> getAvailableLattices() {
        if (restLatticeAmount == 0) {
            return null;
        }
        for (int index = 0; index < typeNum; index++) {
            List<Lattice.Point> points = new ArrayList<>();
            for (int i = 1; i < rowNum + 1; i++) {
                for (int j = 1; j < columnNum + 1; j++) {
                    if (lattices[i][j] != null && lattices[i][j].getType() == typeList[index]) {
                        points.add(new Lattice.Point(j, i));
                    }
                }
            }
            for (int i = 0; i < points.size() - 1; i++) {
                for (int j = i + 1; j < points.size(); j++) {
                    if (judge(points.get(i), points.get(j), false)) {
                        return Arrays.asList(points.get(i), points.get(j));
                    }
                }
            }

        }
        System.err.println("剩余数量：" + getRestLatticeAmount());
        check();
//        return null;
        throw new RuntimeException();
    }

    /**
     * 获取指定位置的 Lattice
     */
    public Lattice getLattice(Lattice.Point point) {
        return lattices[point.y][point.x];
    }

    /**
     * 随机获取任意（可以是不能相消的）一组格子的坐标
     */
    public List<Lattice.Point> getSameLatticePoints() {
        Lattice.Point[] result = new Lattice.Point[2];
        if (restLatticeAmount > ((rowNum * columnNum) >> 3)) {
            int type = -1;
            for (int i = 1; i < rowNum + 1; i++) {
                for (int j = 1; j < columnNum + 1; j++) {
                    if (lattices[i][j] != null) {
                        if (type == -1) {
                            type = lattices[i][j].getType();
                            result[0] = new Lattice.Point(j, i);
                        } else if (type == lattices[i][j].getType()) {
                            result[1] = new Lattice.Point(j, i);
                            return Arrays.asList(result);
                        }
                    }
                }
            }
        } else {
            Random random = new Random();
            int type;
            while (true) {
                int x = random.nextInt(columnNum) + 1;
                int y = random.nextInt(rowNum) + 1;
                if (lattices[y][x] != null) {
                    type = lattices[y][x].getType();
                    result[0] = new Lattice.Point(x, y);
                    break;
                }
            }
            List<Lattice.Point> points = new ArrayList<>();
            for (int i = 1; i < rowNum + 1; i++) {
                for (int j = 1; j < columnNum + 1; j++) {
                    if (lattices[i][j] != null && lattices[i][j].getType() == type) {
                        Lattice.Point point = new Lattice.Point(j, i);
                        if (!point.equals(result[0])) {
                            points.add(point);
                        }
                    }
                }
            }
            if (points.size() == 0) {
                System.out.println(result[0]);
                System.out.println(getType(result[0]));
                throw new RuntimeException("已空！");
            }
            result[1] = points.get(random.nextInt(points.size()));
            return Arrays.asList(result);
        }
        return Arrays.asList(result);
    }

    public int[] getTypeList() {
        return typeList;
    }

    public void setTypeList(int[] typeList) {
        for(int i : typeList) {
            if(i == 0) {
                throw new RuntimeException("type不得为0！");
            }
        }
        this.typeList = typeList;
    }

    public int getTypeNum() {
        return typeNum;
    }

    public void setLimitLattice(int type, int limit_amount) {
        if(limit_amount < 2) {
            throw new RuntimeException("一个种类的数量必须大于等于2!");
        }
        boolean flag = true;
        int index;
        for(index = 0; index < typeList.length; index++) {
            if(typeList[index] == type) {
                flag = false;
                break;
            }
        }
        if(flag) {
            throw new RuntimeException("种类列表中不包含此种类！");
        }
        ArrayList<Lattice.Point> points = new ArrayList<>();
        for(int i = 0; i < rowNum+2; i++) {
            for(int j = 0; j < columnNum+2; j++) {
                if(lattices[i][j] != null && lattices[i][j].getType() == type) {
                    points.add(new Lattice.Point(j, i));
                }
            }
        }
        int new_amount = (new Random().nextInt(limit_amount >> 1) )<< 1;
        System.out.println("新的数量:" + new_amount);
        int present_size = points.size();
        while(present_size > new_amount || present_size > limit_amount) {

            Lattice.Point point1;
            int index1 = new Random().nextInt(points.size());
            point1 = points.get(index1);
            points.remove(index1);
            present_size --;

            Lattice.Point point2;
            int index2 = new Random().nextInt(points.size());
            point2 = points.get(index2);
            points.remove(index2);
            present_size --;

            int new_type = 0;
            while(new_type == type || new_type == 0) {
                new_type = typeList[new Random().nextInt(typeList.length)];
            }
            lattices[point1.y][point1.x] = new Lattice(new_type);
            lattices[point2.y][point2.x] = new Lattice(new_type);
        }
    }

    public void setLattice(Lattice lattice, Lattice.Point point) {
        if(lattice != null) {
            lattices[point.y][point.x] = lattice;
        }
    }


    //--------------------------------------------私有-------------------------------------------------------
    /**
     * 【rowNum：行数，columnNum：列数，typeNum：格子种类数】
     * 实际行数为 rowNum+2
     * 实际列数为 columnNum+2
     * 行数列数不得同时为奇数
     */
    private Map(int rowNum, int columnNum, int[] typeList) {
        if (rowNum % 2 == 1 && columnNum % 2 == 1) {
            throw new RuntimeException("地图格子数量不得为奇数");
        }
        Set set = new HashSet();
        for(int type: typeList) {
            if(type == 0) {
                throw new RuntimeException("种类值不得为0！");
            }
            set.add(type);
        }
        if(set.size() < typeList.length) {
            throw new RuntimeException("种类数存在重复数据！");
        }
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.typeNum = typeList.length;
        this.typeList = typeList;
        this.restLatticeAmount = rowNum * columnNum;
        initMap(typeList);
    }

    private Map(int rowNum, int columnNum, int[] typeList, int[][] typeArray) {
        if (rowNum % 2 == 1 && columnNum % 2 == 1) {
            throw new RuntimeException("地图格子数量不得为奇数");
        }
        Set set = new HashSet();
        for(int type: typeList) {
            if(type <= 0) {
                throw new RuntimeException("种类值不得为负数！");
            }
            set.add(type);
        }
        if(set.size() < typeList.length) {
            throw new RuntimeException("种类数存在重复数据！");
        }
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.typeNum = typeList.length;
        this.typeList = typeList;
        this.restLatticeAmount = 0;
        this.lattices = new Lattice[rowNum + 2][columnNum + 2];
        for (int i = 0; i < rowNum + 2; i++) {
            for (int j = 0; j < columnNum + 2; j++) {
                if (typeArray[i][j] != 0) {
                    lattices[i][j] = new Lattice(typeArray[i][j]);
                    restLatticeAmount ++;
                } else {
                    lattices[i][j] = null;
                }
            }
        }
    }

    /**
     * 初始化地图
     */
    private void initMap(int[] typeList) {
        lattices = new Lattice[rowNum + 2][columnNum + 2];
        Random random = new Random();
        int[] typeNumArray = new int[typeNum];
        int account = 0;
        while (account < rowNum * columnNum - typeNum) {
            int x = account % columnNum + 1;
            int y = account / columnNum + 1;
            int index = random.nextInt(typeNum);
            int type = typeList[index];
            typeNumArray[index]++;
            lattices[y][x] = new Lattice(type);
            account++;
        }
        while (account < rowNum * columnNum) {
            int x = account % columnNum + 1;
            int y = account / columnNum + 1;
            int type = -1;
            for (int i = 0; i < typeNumArray.length; i++) {
                if (typeNumArray[i] % 2 == 1) {
                    type = typeList[i];
                    break;
                }
            }
            if (type == -1) {
                type = typeList[random.nextInt(typeNum)];
            }

            int randomInt = random.nextInt(rowNum * columnNum - typeNum);
            int r_x = randomInt % columnNum + 1;
            int r_y = randomInt / columnNum + 1;
            lattices[y][x] = new Lattice(lattices[r_y][r_x].getType());
            lattices[r_y][r_x] = new Lattice(type);
            int index;
            for(index = 0; index < typeList.length; index++) {
                if(typeList[index] == type) {
                    break;
                }
            }
            typeNumArray[index]++;
            account++;
        }
    }

    /**
     * 判断是否能
     */
    private boolean judgeVertical(Lattice.Point point1, Lattice.Point point2, boolean changePath) {
        if (point1.equals(point2)) {
            return false;
        }
        if (point1.x != point2.x) {
            return false;
        }
        Path.PathNode node = path.getTerminationPointNode();
        if (point1.y < point2.y) {
            for (int i = point1.y + 1; i < point2.y; i++) {
                if(changePath) {
                    path.addPoint(new Lattice.Point(point1.x, i));
                }
                if (lattices[i][point1.x] != null) {
                    if(changePath) {
                        path.deleteNewPoints(node);
                    }
                    return false;
                }
            }
        } else {
            for (int i = point1.y - 1; i > point2.y; i--) {
                if(changePath) {
                    path.addPoint(new Lattice.Point(point1.x, i));
                }
                if (lattices[i][point1.x] != null) {
                    if(changePath) {
                        path.deleteNewPoints(node);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否能
     */
    private boolean judgeHorizon(Lattice.Point point1, Lattice.Point point2, boolean changePath) {
        if (point1.equals(point2)) {
            return false;
        }
        if (point1.y != point2.y) {
            return false;
        }
        Path.PathNode node = path.getTerminationPointNode();
        if (point1.x < point2.x) {
            for (int j = point1.x + 1; j < point2.x; j++) {
                if(changePath) {
                    path.addPoint(new Lattice.Point(j, point1.y));
                }
                if (lattices[point1.y][j] != null) {
                    if(changePath) {
                        path.deleteNewPoints(node);
                    }
                    return false;
                }
            }
        } else {
            for (int j = point1.x - 1; j > point2.x; j--) {
                if(changePath) {
                    path.addPoint(new Lattice.Point(j, point1.y));
                }
                if (lattices[point1.y][j] != null) {
                    if(changePath) {
                        path.deleteNewPoints(node);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否可以一个拐点相消
     */
    private boolean judgeTurnOnce(Lattice.Point point1, Lattice.Point point2, boolean changePath) {
        if (point1.equals(point2)) {
            return false;
        }
        Path.PathNode node = path.getTerminationPointNode();
        Lattice.Point m = new Lattice.Point(point1.x, point2.y);
        if (lattices[m.y][m.x] == null) {
            if (judgeVertical(point1, m, changePath)) {
                if(changePath) {
                    path.addPoint(m);
                }
                if (judgeHorizon(m, point2, changePath)) {
                    return true;
                }
            }
            if(changePath) {
                path.deleteNewPoints(node);
            }
        }
        Lattice.Point n = new Lattice.Point(point2.x, point1.y);
        if (lattices[n.y][n.x] == null) {
            if (judgeHorizon(point1, n, changePath)) {
                if(changePath) {
                    path.addPoint(n);
                }
                if (judgeVertical(n, point2, changePath)) {
                    return true;
                }
            }
            if(changePath) {
                path.deleteNewPoints(node);
            }
        }
        if(changePath) {
            path.deleteNewPoints(node);
        }
        return false;
    }

    /**
     * 判断是否可以两个拐点相消
     */
    private boolean judgeTurnTwice(Lattice.Point point1, Lattice.Point point2, boolean changePath) {
        Path.PathNode node = path.getTerminationPointNode();

        int x1 = point1.x - 1;
        while (x1 > -1 && lattices[point1.y][x1] == null) {
            if(changePath) {
                path.addPoint(new Lattice.Point(x1, point1.y));
            }
                if (judgeTurnOnce(new Lattice.Point(x1, point1.y), point2, changePath)) {
                    return true;
                }
            x1--;
        }
        if(changePath) {
            path.deleteNewPoints(node);
        }

        int x2 = point1.x + 1;
        while (x2 < rowNum + 2 && lattices[point1.y][x2] == null) {
            if(changePath) {
                path.addPoint(new Lattice.Point(x2, point1.y));
            }
                if (judgeTurnOnce(new Lattice.Point(x2, point1.y), point2, changePath)) {
                    return true;
                }
            x2++;
        }
        if(changePath) {
            path.deleteNewPoints(node);
        }

        int y1 = point1.y - 1;
        while (y1 > -1 && lattices[y1][point1.x] == null) {
            if(changePath) {
                path.addPoint(new Lattice.Point(point1.x, y1));
            }
                if (judgeTurnOnce(new Lattice.Point(point1.x, y1), point2, changePath)) {
                    return true;
                }
            y1--;
        }
        if(changePath) {
            path.deleteNewPoints(node);
        }

        int y2 = point1.y + 1;
        while (y2 < columnNum + 2 && lattices[y2][point1.x] == null) {
            if(changePath) {
                path.addPoint(new Lattice.Point(point1.x, y2));
            }
                if (judgeTurnOnce(new Lattice.Point(point1.x, y2), point2, changePath)) {
                    return true;
                }
            y2++;
        }
        if(changePath) {
            path.deleteNewPoints(node);
        }

        return false;
    }

    /**
     * @描述： 内部类，负责获取路径
     */
    private static class Path {
        private PathNode startPointNode;
        private PathNode terminationNode;

        private Path(Lattice.Point startPoint) {
            this.startPointNode = new PathNode(startPoint);
            this.terminationNode = this.startPointNode;
        }

        private PathNode getTerminationPointNode() {
            return terminationNode;
        }

        private void addPoint(Lattice.Point point) {
            PathNode newNode = new PathNode(point);
            terminationNode.next = newNode;
            terminationNode = newNode;
        }

        private void deleteNewPoints(PathNode node) {
            terminationNode = node;
            terminationNode.next = null;
        }

        private List<Lattice.Point> getPathPoints() {
            PathNode node = startPointNode.next;
            List<Lattice.Point> results = new ArrayList<>();
            while (node != null) {
                results.add(node.point);
                node = node.next;
            }
            return results;
        }

        public static class PathNode {
            private Lattice.Point point;
            private PathNode next;

            private void pathNode(Lattice.Point point) {
                this.point = point;
                this.next = null;
            }

            private PathNode(Lattice.Point point) {
                this.point = point;
                this.next = null;
            }
        }
    }

    //--------------------------------------------测试函数-------------------------------------------------------

    //remove before launching
    public static void main(String[] args) {
        Map map = new Map(10, 10, new int[]{101,102,103,104,105,106,107,108,109});
//        map.setLimitLattice(-1, 4);
//        map.autoPlay();
//        NormalArchive.deleteArchive();
        map.dosPlay();
    }

    //remove before launching
    public void check() {
        for (int i = 0; i < rowNum + 2; i++) {
            for (int j = 0; j < columnNum + 2; j++) {
                if (lattices[i][j] == null) {
                    System.out.print(0 + "  ");
                } else {
                    System.out.print(lattices[i][j].getType() + "  ");
                }
            }
            System.out.println();
        }
    }

    //remove before launching
    public void getStatic() {
        for(int index = 0; index < typeList.length; index ++) {
            System.out.print(typeList[index] + ":");
            int amount = 0;
            for(int row = 0; row < rowNum+2; row ++) {
                for(int column = 0; column < columnNum +2; column ++) {
                    if(lattices[row][column] != null && lattices[row][column].getType() == typeList[index]) {
                        amount ++;
                    }
                }
            }
            System.out.print(amount + "|");
        }
        System.out.println();
    }

    //remove before launching
    private void click(Lattice.Point point1, Lattice.Point point2) {
        if (judge(point1, point2)) {
            removeLattice(point1);
            removeLattice(point2);
        }
    }

    private void autoPlay() {
        while (getAvailableLattices() != null) {
            check();
            List<Lattice.Point> points = getAvailableLattices();
            System.out.println(points.get(0));
            System.out.println(points.get(1));
            System.out.println(getType(points.get(0)) + " " + getType(points.get(1)));
            click(points.get(0), points.get(1));
            System.out.println(getRestLatticeAmount()+ "\n");
        }
    }

    private void dosPlay() {
        while (true) {
            check();
            getStatic();
            int x1 = new Scanner(System.in).nextInt();
            int y1 = new Scanner(System.in).nextInt();
            int x2 = new Scanner(System.in).nextInt();
            int y2 = new Scanner(System.in).nextInt();
            click(new Lattice.Point(x1, y1), new Lattice.Point(x2, y2));
        }
    }
}