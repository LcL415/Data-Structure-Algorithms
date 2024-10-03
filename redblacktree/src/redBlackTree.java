
//红黑树特性：所以节点都有两种颜色，所以null都视为黑色，
// 红色节点不能相邻，跟节点是黑色的，
// 从跟到任意一个叶子节点 路径中的黑色节点数一样
public class redBlackTree {

    enum Color {
        RED, BLACK;
    }

    private Node root;

    private static class Node {
        int key;
        Object value;
        Node left;
        Node right;
        Node parent;
        Color color =Color.RED;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        //工具方法：是否是左孩子
        boolean isLeftchild() {
            return parent != null && parent.left == this;
        }

        //叔叔
        Node uncle() {
            if (parent == null || parent.parent == null) {
                return null;
            }
            if (parent.isLeftchild()) {
                return parent.parent.right;
            } else {
                return parent.parent.left;
            }
        }

        //兄弟
        Node sibling() {
            if (parent == null) {
                return null;
            }
            if (this.isLeftchild()) {
                return parent.right;
            } else {
                return parent.left;
            }
        }
    }

    boolean isRed(Node node) {
        return node != null && node.color ==Color.RED;
    }
    boolean isBlack(Node node){
        return node==null||node.color==Color.BLACK;
    }
    //右旋1.parent 2. 再旋转方法内 新根的父子关系
    private void rightRotate(Node pink){
        Node parent=pink.parent;
        Node yellow=pink.left;
        Node green=yellow.right;
        if (green!=null) {
            green.parent=pink;
        }
        yellow.right=pink;
        yellow.parent=parent;
        pink.left=green;
        pink.parent=yellow;
        if (parent == null) {
            root=yellow;
        }
        if (parent.left == pink) {
            parent.left=yellow;
        }else {
            parent.right=yellow;
        }
    }
    private void leftRotate(Node pink){
        Node parent=pink.parent;
        Node yellow=pink.right;
        Node green=yellow.left;
        if (green!=null) {
            green.parent=pink;
        }
        yellow.left=pink;
        yellow.parent=parent;
        pink.right=green;
        pink.parent=yellow;
        if (parent == null) {
            root=yellow;
        }
        if (parent.left == pink) {
            parent.left=yellow;
        }else {
            parent.right=yellow;
        }
    }
    public void put(int key, Object value){
        Node p=root;
        Node parent=null;
        while (p != null) {
            parent=p;
            if (key < p.key) {
                p=p.left;
            } else if (key > p.key) {
                p=p.right;
            }else{
                p.value=value;
                return;
            }
        }
        Node inserted=new Node(key,value);
        if (parent == null) {
            root= inserted;
        } else if (key < parent.key) {
            parent.left=inserted;
            inserted.parent=parent;
        }else {
            parent.right=inserted;
            inserted.parent=parent;
        }
        fixRedRed(inserted);
    }
    void fixRedRed(Node x){
        //case1:插入节点是根节点，变黑即可
        if (x == root) {
            x.color=Color.BLACK;
            return;
        }
        //case2:插入节点的父亲是黑色，无需操作
        if (isBlack(x.parent)) {
            return;
        }
        //case3：插入节点父亲是红色 叔叔为红色，需要将父亲变黑，叔叔变黑，祖父变红，然后递归处理祖父
        Node parent=x.parent;
        Node uncle=x.uncle();
        Node grandparent=parent.parent;
        if (isRed(uncle)) {
            parent.color=Color.BLACK;
            uncle.color=Color.BLACK;
            grandparent.color=Color.RED;
            fixRedRed(grandparent);
            return;
        }
        //case4：插入节点父亲为红色，叔叔为黑色，触发红红相邻
        //左左
        if (parent.isLeftchild() && x.isLeftchild()) {
            parent.color=Color.BLACK;
            grandparent.color=Color.RED;
            rightRotate(grandparent);
        } else if (parent.isLeftchild()&&!x.isLeftchild()) {//左右
            leftRotate(parent);
            x.color=Color.BLACK;
            grandparent.color=Color.RED;
            rightRotate(grandparent);
        }else if(!parent.isLeftchild()&&!x.isLeftchild()){//右右
            parent.color=Color.BLACK;
            grandparent.color=Color.RED;
            leftRotate(grandparent);
        }else {
            rightRotate(parent);
            x.color=Color.BLACK;
            grandparent.color=Color.RED;
            leftRotate(grandparent);
        }
        //

    }
    Node find(int key){
        Node p=root;
        while (p != null) {
            if (key < p.key) {
                p=p.left;
            } else if (key>p.key) {
                p=p.right;
            }else {
                return p;
            }
        }
        return null;
    }
    Node findReplaced(Node deleted){
        if (deleted.left == null && deleted.right == null) {
            return null;
        }
        if (deleted.left == null) {
            return deleted.right;
        }
        if(deleted.right==null){
            return deleted.left;
        }
        Node p=deleted.right;
        while (p.left != null) {
            p=p.left;
        }
        return p;
    }
    public void remove(int key){
        Node deleted=find(key);
        if (deleted == null) {
            return;
        }
        doRemove(deleted);
    }

    private void doRemove(Node deleted) {
        Node replaced=findReplaced(deleted);
        Node parent = deleted.parent;
        if (replaced == null) {//没孩子
            if (deleted == root) {//删除的是跟节点没孩子
                root=null;
            }else {//删除的不是跟节点 没孩子
                if (deleted.isLeftchild()) {
                    parent.left=null;
                }else {
                    parent.right=null;
                }
                deleted.parent=null;

            }
            return;
        }
        if (deleted.left == null || deleted.right == null) {//只有一个孩子
            if(deleted==root){//删除的是跟节点有一个孩子
                root.key=replaced.key;
                root.value=replaced.value;
                root.left=null;
                root.right=null;
            }else {//删除的不是跟节点 有一个孩子
                if (deleted.isLeftchild()) {
                    parent.left=replaced;
                }else {
                    parent.right=replaced;
                }
                replaced.parent=parent;
                deleted.left=deleted.right=deleted.parent=null;//help GC
            }
            return;
        }
        //有俩孩子 李代桃僵法 先把要删除节点和后继节点的键和值互换，然后删除后继节点
        int t=deleted.key;
        deleted.key=replaced.key;
        replaced.key=t;
        Object t2=deleted.value;
        deleted.value=replaced.value;
        replaced.value=t2;
        doRemove(replaced);
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}