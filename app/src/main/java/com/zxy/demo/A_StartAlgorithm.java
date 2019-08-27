package com.zxy.demo;

import android.util.SparseArray;

import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2019/08/27.
 */
public class A_StartAlgorithm {

    private final int S_LINE = 10;//直线
    private final int D_LINE = 14;//对角线

    private SparseArray<Node<Location>> mOpenArray = new SparseArray<>();
    private SparseArray<Node<Location>> mCloseArray = new SparseArray<>();
    private int[][] mSurface;
    private Node<Location> mStart;
    private Node<Location> mEnd;


    public void init(int[][] surface, Node<Location> start, Node<Location> end) {
        mSurface = surface;
        mStart = start;
        mEnd = end;
        mOpenArray.put(getNodeKey(start), start);
    }

    public int getNodeKey(Node<Location> node) {
        int current_node_x = node.Location.X;
        int current_node_y = node.Location.Y;
        return current_node_x + current_node_y * mSurface.length;
    }

    public void execute() {
        Node<Location> current_node = getCurrentNode();
        int key = getNodeKey(current_node);
        mOpenArray.remove(key);
        mCloseArray.put(key, current_node);
        enterOpenArray(current_node);
        if (mOpenArray.get(getNodeKey(mEnd)) != null || mOpenArray.size() == 0) {
            return;
        } else {
            execute();
        }
        LogUtil.e("execute: openArray=" + mOpenArray + " closeArray=" + mCloseArray);
    }

    public void result() {
        int endKey = getNodeKey(mEnd);
        Node<Location> endNode = mOpenArray.get(endKey);
        List<Node<Location>> pathNodes = new ArrayList<>();
        if (endNode != null && endNode.getParent() != null) {
            int lastKey = getNodeKey(endNode.getParent());//终点上一个节点的key
            while (getNodeKey(mStart) != lastKey) {
                Node<Location> pathNode = getCloseArrayNode(lastKey);
                pathNodes.add(pathNode);
                if (pathNode != null && pathNode.getParent() != null) {
                    lastKey = getNodeKey(pathNode.getParent());
                }
            }
            pathNodes.add(mStart);
        }
        LogUtil.e("路径：" + pathNodes);
    }

    private Node<Location> getCloseArrayNode(int pathKey) {
        for (int i = 0; i < mCloseArray.size(); i++) {
            int key = mCloseArray.keyAt(i);
            Node<Location> value = mCloseArray.valueAt(i);
            if (key == pathKey) {
                return value;
            }
        }
        return null;
    }

    private Node<Location> getCurrentNode() {
        Node<Location> current_node = null;
        int mMinF = Integer.MAX_VALUE;
        for (int i = 0; i < mOpenArray.size(); i++) {
            Node<Location> value = mOpenArray.valueAt(i);
            if (value.getF() < mMinF) {
                current_node = value;
            }
        }
        return current_node;
    }


    private void enterOpenArray(Node<Location> current_node) {
        int current_node_x = current_node.Location.X;
        int current_node_y = current_node.Location.Y;
        int currentKey = getNodeKey(current_node);
        SparseArray<Node<Location>> aroundNodes = new SparseArray<>();
        /**
         * 0 1 2
         * 3 * 4
         * 5 6 7
         */
        for (int i = 0; i < 8; i++) {
            int node_key;
            int node_x;
            int node_y;
            int node_g;
            int node_h;
            if (i < 3) {//0 1 2
                node_key = currentKey - 1 - mSurface.length + i;
                node_x = current_node_x - 1 + i;
                node_y = current_node_y - 1;
            } else if (i < 5) {//3   4
                node_key = currentKey - 1 + (i - 3);
                node_x = current_node_x - 1 + (i - 3);
                node_y = current_node_y;
            } else {//5 6 7
                node_key = currentKey - 1 + mSurface.length + (i - 5);
                node_x = current_node_x - 1 + (i - 5);
                node_y = current_node_y + 1;
            }
            if (i == 0 || i == 2 || i == 5 || i == 7) {
                node_g = D_LINE;
            } else {
                node_g = S_LINE;
            }
            node_g += current_node.getG();
            node_h = Math.abs(mEnd.Location.X - node_x) + Math.abs(mEnd.Location.Y - node_y);
            Node<Location> node = new Node<>(new Location(node_x, node_y));
            node.setParent(current_node);
            node.setG(node_g);
            node.setH(node_h);
            aroundNodes.put(node_key, node);
        }
        //附近节点判断
        for (int i = 0; i < aroundNodes.size(); i++) {
            int key = aroundNodes.keyAt(i);
            Node<Location> value = aroundNodes.valueAt(i);
            if ((value.Location.X >= 0 && value.Location.X <= (mSurface.length - 1)) &&
                    (value.Location.Y >= 0 && value.Location.Y <= (mSurface[0].length - 1)) &&
                    mCloseArray.get(key) == null) {//坐标合法且可通过，不在“关闭列表中”
                boolean replace = true;
                if (mOpenArray.get(key) != null) {
                    Node<Location> existValue = mOpenArray.get(key);
                    if (existValue.getG() < value.getG()) {
                        replace = false;//如果“开启列表”已存在当前节点，那么不代替该节点
                    }
                }
                if (replace) {
                    mOpenArray.put(key, value);
                }
            }
        }
    }

    public static class Location {

        public final int X;
        public final int Y;

        public Location(int x, int y) {
            X = x;
            Y = y;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "X=" + X +
                    ", Y=" + Y +
                    '}';
        }
    }

    public static class Node<T> {

        public final T Location;

        private Node<T> mParent;
        private int mG;//G值，起点到当前点的实际距离
        private int mH;//H值，当前点到终点的预估距离

        public Node(T location) {
            Location = location;
        }

        public Node<T> getParent() {
            return mParent;
        }

        public void setParent(Node<T> parent) {
            mParent = parent;
        }

        public int getG() {
            return mG;
        }

        public void setG(int g) {
            mG = g;
        }

        public int getH() {
            return mH;
        }

        public void setH(int h) {
            mH = h;
        }

        public int getF() {
            return getG() + getH();
        }

        @Override
        public String toString() {
            return "Node{" +
                    "Location=" + Location +
                    ", mParent=" + mParent +
                    ", mG=" + mG +
                    ", mH=" + mH +
                    '}';
        }
    }
}
