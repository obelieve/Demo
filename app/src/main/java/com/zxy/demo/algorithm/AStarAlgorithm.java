package com.zxy.demo.algorithm;


import com.zxy.demo.utils.RouteLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zxy on 2019/08/27.
 */
public class AStarAlgorithm {

    private static final String TAG = AStarAlgorithm.class.getSimpleName();
    /**
     * 标记为障碍物
     */
    public static final int SURFACE_BARRIER_TAG = 1;

    private final int S_LINE = 10;//直线
    private final int D_LINE = 14;//对角线

    private List<Node> mOpenList;//“开启”列表
    private List<Node> mCloseList;//“关闭”列表
    private int[][] mSurface;
    private Node mStart;
    private Node mEnd;
    private boolean mIsReverse;//路线是否进行反向选择


    /**
     * 执行，返回路径结果
     *
     * @return
     */
    public List<Node> execute(int[][] surface, Node start, Node end) {
        mSurface = surface;
        mIsReverse = false;
        if (start.X > end.X) {
            mIsReverse = true;
        }
        if (mIsReverse) {
            mStart = end;
            mEnd = start;
        } else {
            mStart = start;
            mEnd = end;
        }
        mCloseList = new ArrayList<>();
        mOpenList = new ArrayList<>();
        mOpenList.add(mStart);
        RouteLog.log(TAG, "初始化：Start节点=" + mStart + " End节点=" + mEnd + " \"开启列表：\"" + mOpenList);
        searching();
        return result();
    }

    /**
     * 路径搜索，直到找到终点
     */
    private void searching() {
        Node current_node = mOpenList.remove(mOpenList.size() - 1);
        //RouteLog.log(TAG,"当前路径节点：" + current_node);
        mCloseList.add(current_node);
        addNodeToOpenStack(current_node);
        if (mOpenList.contains(mEnd) || mOpenList.isEmpty()) {
            RouteLog.log(TAG,"找到终点：" + mEnd + " openStack.isEmpty=" + mOpenList.isEmpty());
        } else {
            searching();
        }
    }


    /**
     * 返回路径结果
     *
     * @return
     */
    private List<Node> result() {
        if (!mOpenList.contains(mEnd)) {
            return Collections.EMPTY_LIST;
        }
        int index = mOpenList.lastIndexOf(mEnd);
        Node endNode = mOpenList.get(index);//返回终点节点
        List<Node> pathNodes = new ArrayList<>();
        pathNodes.add(endNode);
        if (endNode != null && endNode.getParent() != null) {
            Node lastNode = endNode.getParent();//路径的上一个节点
            while (!mStart.equals(lastNode)) {
                pathNodes.add(lastNode);
                lastNode = lastNode.getParent();
            }
            pathNodes.add(lastNode);
        }
        if (!mIsReverse) {
            Collections.reverse(pathNodes);//从终点->起点
        }
        RouteLog.log(TAG,"返回节点路径结果，size = " + pathNodes.size());
        return pathNodes;
    }

    /**
     * 查找周围节点，并加入到open列表中（备选路径）
     *
     * @param current_node open列表中，具有最小F值的节点
     */
    private void addNodeToOpenStack(Node current_node) {
        int current_node_x = current_node.X;
        int current_node_y = current_node.Y;
        List<Node> aroundNodes = new ArrayList<>();
        /*
         * (x-1,y-1)  (x,y-1) (x+1,y-1)
         * (x-1,y)    (x,y)   (x+1,y)
         * (x-1,y+1)  (x,y+1) (x+1,y+1)
         * 计算周围的节点，进行下一步
         */
        for (int i = 0; i < 3; i++) {
            int node_x;
            int node_y;
            int node_g;
            int node_h;
            //x-1
            node_x = current_node_x - 1;
            node_y = current_node_y - 1 + i;
            if (i == 0 || i == 2) {
                node_g = D_LINE;
            } else {
                node_g = S_LINE;
            }
            node_g += current_node.getG();
            node_h = getNode_h(node_x, node_y);
            if ((node_x >= 0 && node_x < mSurface.length) && (node_y >= 0 && node_y < mSurface[0].length) && mSurface[node_x][node_y] != SURFACE_BARRIER_TAG && !mCloseList.contains(new Node(node_x, node_y))) {
                Node node = new Node(node_x, node_y);
                node.setParent(current_node);
                node.setG(node_g);
                node.setH(node_h);
                aroundNodes.add(node);
            }
            //x
            node_x += 1;
            if (i == 1) {
                node_g = 0;
            } else {
                node_g = S_LINE;
            }
            node_g += current_node.getG();
            node_h = getNode_h(node_x, node_y);
            if ((node_x >= 0 && node_x < mSurface.length) && (node_y >= 0 && node_y < mSurface[0].length) && mSurface[node_x][node_y] != SURFACE_BARRIER_TAG && !mCloseList.contains(new Node(node_x, node_y))) {
                Node node = new Node(node_x, node_y);
                node.setParent(current_node);
                node.setG(node_g);
                node.setH(node_h);
                aroundNodes.add(node);
            }
            //x+1
            node_x += 1;
            if (i == 0 || i == 2) {
                node_g = D_LINE;
            } else {
                node_g = S_LINE;
            }
            node_g += current_node.getG();
            node_h = getNode_h(node_x, node_y);
            if ((node_x >= 0 && node_x < mSurface.length) && (node_y >= 0 && node_y < mSurface[0].length) && mSurface[node_x][node_y] != SURFACE_BARRIER_TAG && !mCloseList.contains(new Node(node_x, node_y))) {
                Node node = new Node(node_x, node_y);
                node.setParent(current_node);
                node.setG(node_g);
                node.setH(node_h);
                aroundNodes.add(node);
            }
        }
        //RouteLog.log(TAG,"当前节点的附近节点 aroundNodes:" + aroundNodes);
        //附近节点判断
        for (int i = 0; i < aroundNodes.size(); i++) {
            Node node = aroundNodes.get(i);
            if (mOpenList.contains(node)) {
                int index = mOpenList.indexOf(node);
                Node openNode = mOpenList.get(index);//open列表中存在该节点
                if (node.getG() < openNode.getG()) {//如果open列表的节点G值比较大，那么代替该节点
                    mOpenList.set(index, node);
                }
            } else {
                mOpenList.add(node);
            }
        }
        aroundNodes = null;
        //RouteLog.log(TAG,"排序前 OpenStack:" + mOpenList);
        Collections.sort(mOpenList, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getF() < o2.getF()) {//越小越靠前
                    return 1;
                } else if (o1.getF() == o2.getF()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        //RouteLog.log(TAG,"排序后 OpenStack:" + mOpenList);
    }

    /**
     * 估算距离
     *
     * @param node_x
     * @param node_y
     * @return
     */
    private int getNode_h(int node_x, int node_y) {
        return (Math.abs(mEnd.X - node_x) + Math.abs(mEnd.Y - node_y)) * S_LINE;
    }

    public List<Node> getOpenList() {
        return mOpenList;
    }

    public List<Node> getCloseList() {
        return mCloseList;
    }

    private static void main(String[] args) {
        /**
         * Test
         *
         * 00*0
         * 0010
         * 0110
         * 0*00
         */
        int[][] ints = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ints[i][j] = 0;
            }
        }
        ints[2][1] = 1;
        ints[2][2] = 1;
        ints[1][2] = 1;
        AStarAlgorithm algorithm = new AStarAlgorithm();
        AStarAlgorithm.Node end = new AStarAlgorithm.Node(1, 3);
        AStarAlgorithm.Node start = new AStarAlgorithm.Node(2, 0);
        List<Node> nodes = algorithm.execute(ints, start, end);
        System.out.println("结果 nodes：" + nodes);
    }


    public static class Node {

        public final int X;
        public final int Y;

        private Node mParent;
        private int mG;//G值，起点到当前点的实际距离
        private int mH;//H值，当前点到终点的预估距离

        public Node(int x, int y) {
            X = x;
            Y = y;
        }

        public Node getParent() {
            return mParent;
        }

        public void setParent(Node parent) {
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
        public boolean equals(Object obj) {
            if (obj instanceof Node) {
                Node node = (Node) obj;
                if (node.X == X && node.Y == Y) {
                    return true;
                } else {
                    return false;
                }
            }
            return super.equals(obj);
        }

        @Override
        public String toString() {
            return "Node{" + "(" + X + "," + Y + ")" +
                    ", Parent=" + mParent +
                    ", G=" + mG +
                    ", H=" + mH +
                    '}';
        }
    }
}
