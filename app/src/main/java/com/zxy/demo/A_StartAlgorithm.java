package com.zxy.demo;

import android.util.SparseArray;

import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zxy on 2019/08/27.
 */
public class A_StartAlgorithm {

    /**
     * 标记为障碍物
     */
    public static final int SURFACE_BARRIER_TAG = 1;

    private final int S_LINE = 10;//直线
    private final int D_LINE = 14;//对角线

    private SparseArray<Node<Location>> mOpenArray = new SparseArray<>();//“开启”列表
    private SparseArray<Node<Location>> mCloseArray = new SparseArray<>();//“关闭”列表
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
        LogUtil.e("当前路径节点：" + current_node);
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

    public List<Node<Location>> result() {
        int endKey = getNodeKey(mEnd);
        Node<Location> endNode = mOpenArray.get(endKey);
        List<Node<Location>> pathNodes = new ArrayList<>();
        pathNodes.add(mEnd);
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
        Collections.reverse(pathNodes);
        return pathNodes;
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
        SparseArray<Node<Location>> aroundNodes = new SparseArray<>();
        /*
         * (x-1,y-1)  (x,y-1) (x+1,y-1)
         * (x-1,y)    (x,y)   (x+1,y)
         * (x-1,y+1)  (x,y+1) (x+1,y+1)
         * 计算周围的节点，进行下一步
         */
        for (int i = 0; i < 3; i++) {
            int node_x;
            int node_y;
            int node_key;
            int node_g;
            int node_h;
            //x-1
            node_x = current_node_x - 1;
            node_y = current_node_y - 1 + i;
            node_key = node_x + node_y * mSurface.length;
            if (i == 0 || i == 2) {
                node_g = D_LINE;
            } else {
                node_g = S_LINE;
            }
            node_g += current_node.getG();
            node_h = (Math.abs(mEnd.Location.X - node_x) + Math.abs(mEnd.Location.Y - node_y)) * S_LINE;
            if ((node_x >= 0 && node_x < mSurface.length) && (node_y >= 0 && node_y < mSurface[0].length) && mSurface[node_x][node_y] != SURFACE_BARRIER_TAG && mCloseArray.get(node_key) == null) {
                Node<Location> node = new Node<>(new Location(node_x, node_y));
                node.setParent(current_node);
                node.setG(node_g);
                node.setH(node_h);
                aroundNodes.put(node_key, node);
            }
            //x
            node_x += 1;
            node_key = node_x + node_y * mSurface.length;
            if (i == 1) {
                node_g = 0;
            } else {
                node_g = S_LINE;
            }
            node_g += current_node.getG();
            node_h = (Math.abs(mEnd.Location.X - node_x) + Math.abs(mEnd.Location.Y - node_y)) * S_LINE;
            if ((node_x >= 0 && node_x < mSurface.length) && (node_y >= 0 && node_y < mSurface[0].length) && mSurface[node_x][node_y] != SURFACE_BARRIER_TAG && mCloseArray.get(node_key) == null) {
                Node<Location> node = new Node<>(new Location(node_x, node_y));
                node.setParent(current_node);
                node.setG(node_g);
                node.setH(node_h);
                aroundNodes.put(node_key, node);
            }
            //x+1
            node_x += 1;
            node_key = node_x + node_y * mSurface.length;
            if (i == 0 || i == 2) {
                node_g = D_LINE;
            } else {
                node_g = S_LINE;
            }
            node_g += current_node.getG();
            node_h = (Math.abs(mEnd.Location.X - node_x) + Math.abs(mEnd.Location.Y - node_y)) * S_LINE;
            if ((node_x >= 0 && node_x < mSurface.length) && (node_y >= 0 && node_y < mSurface[0].length) && mSurface[node_x][node_y] != SURFACE_BARRIER_TAG && mCloseArray.get(node_key) == null) {
                Node<Location> node = new Node<>(new Location(node_x, node_y));
                node.setParent(current_node);
                node.setG(node_g);
                node.setH(node_h);
                aroundNodes.put(node_key, node);
            }
        }
        LogUtil.e("aroundNodes:" + aroundNodes);
        //附近节点判断
        for (int i = 0; i < aroundNodes.size(); i++) {
            int key = aroundNodes.keyAt(i);
            Node<Location> value = aroundNodes.valueAt(i);
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

    public static class Location {

        public final int X;
        public final int Y;

        public Location(int x, int y) {
            X = x;
            Y = y;
        }

        @Override
        public String toString() {
            return "(" + X + "," + Y + ")";
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
            return Location + "--len=" + getF();
        }
    }
}
