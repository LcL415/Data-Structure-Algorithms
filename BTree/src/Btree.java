import java.util.Arrays;

public class Btree {

    static class Node {
        int[] keys;//关键字
        Node[] children;//孩子
        int keyNumber;//有效关键字的个数
        boolean leaf = true;// 是否是叶子节点
        int t;//最小度数：最少有几个孩子

        public Node(int t) {// t>=2;
            this.t = t;
            this.children = new Node[2 * t];
            this.keys = new int[2 * t - 1];
        }

        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOfRange(keys, 0, keyNumber));
        }

        //多路查找
        Node get(int key) {
            int i = 0;
            while (i < keyNumber) {
                if (keys[i] == key) {
                    return this;
                }
                if (keys[i] > key) {
                    break;
                }
                i++;
            }
            if (leaf) {//叶子节点的情况
                return null;
            }
            return children[i].get(key);
        }

        //向指定索引插入key
        void insertKey(int key, int index) {
            System.arraycopy(keys, index, keys, index + 1, keyNumber - index);
            keys[index] = key;
            keyNumber++;
        }

        //向指定索引插入child
        void insertChild(Node child, int index) {
            System.arraycopy(children, index, children, index + 1, keyNumber - index);
            children[index] = child;
        }

        //移除指定index处的key
        int removeKey(int index) {
            int t = keys[index];
            System.arraycopy(keys, index + 1, keys, index, --keyNumber - index);
            return t;
        }

        //移除最左边的key
        int removeLeftmostkey() {
            return removeKey(0);
        }

        //移除最右边的key
        int removeRightmostkey() {
            return removeKey(keyNumber - 1);
        }

        //移除指定index处的child
        Node removeChild(int index) {
            Node t = children[index];
            System.arraycopy(children, index + 1, children, index, --keyNumber - index);
            return t;
        }

        //移除最左孩子
        Node removeLeftmostchild() {
            return removeChild(0);
        }

        //移除最右孩子
        Node removeRightmostchild() {
            return removeChild(keyNumber);
        }

        //寻找index处孩子左边的兄弟
        Node childLeftsibing(int index) {
            return index > 0 ? children[index - 1] : null;
        }

        //寻找index孩子处右边的兄弟
        Node childRightsibing(int index) {
            return index == keyNumber ? null : children[index + 1];
        }

        //复制当前节点所有key和child到target
        void moveToTarget(Node target) {
            int start = target.keyNumber;
            if (!leaf) {
                for (int i = 0; i <= keyNumber; i++) {
                    target.children[start + i] = children[i];
                }
            }
            for (int i = 0; i < keyNumber; i++) {
                target.keys[target.keyNumber++] = keys[i];
            }
        }
    }

    Node root;

    int t;//树中最小度数
    final int min_key_number;//最小key的数目
    final int max_key_number;//最大key的数目

    public Btree() {
        this(2);
    }

    public Btree(int t) {
        this.t = t;
        root = new Node(t);
        max_key_number = 2 * t - 1;
        min_key_number = t - 1;
    }

    //1. key 是否存在
    public boolean contains(int key) {
        return root.get(key) != null;
    }

    //2. 新增
    public void put(int key) {
        doput(root, key, null, 0);
    }

    private void doput(Node node, int key, Node parent, int index) {
        int i = 0;
        while (node.keyNumber > i) {
            if (node.keys[i] == key) {
                return;
            }
            if (node.keys[i] > key) {
                break;//找到了插入位置 为此时的i；
            }
            i++;
        }
        if (node.leaf) {
            node.insertKey(key, i);

        } else {
            doput(node.children[i], key, node, i);
        }
        if (node.keyNumber == max_key_number) {
            split(node, parent, index);
        }
    }

    //分裂： 1.创建right 节点（分裂后大于当前left节点的）， 把t以后的key 和child 都拷贝过去
    //      2. t-1 处的key插入到parent的index处，index 指的是left作为孩子时的索引
    //      3. right 节点作为parent的孩子插入到index +1 处
    private void split(Node left, Node parent, int index) {
        if (parent == null) {//     分裂的是跟节点
            Node newRoot = new Node(t);
            newRoot.leaf = false;
            newRoot.insertChild(left, 0);
            this.root = newRoot;
            parent = newRoot;
        }
        //分裂： 1.创建right 节点（分裂后大于当前left节点的）， 把t以后的key 和child 都拷贝过去
        Node right = new Node(t);
        right.leaf = left.leaf;
        System.arraycopy(left.keys, t, right.keys, 0, t - 1);
        if (!left.leaf) {//当分裂的不是叶子节点，还需要拷贝孩子
            System.arraycopy(left.children, t, right.children, 0, t);
        }
        right.keyNumber = t - 1;
        left.keyNumber = t - 1;
        //      2. t-1 处的key插入到parent的index处，index 指的是left作为孩子时的索引
        int mid = left.keys[t - 1];
        parent.insertKey(mid, index);
        //      3. right 节点作为parent的孩子插入到index +1 处
        parent.insertChild(right, index + 1);
    }

    //3. 删除
    //case1: 当前节点是叶子节点，没找到
    //case2: 当前节点是叶子节点，找到了
    //case3: 当前节点是非叶子节点，没找到
    //case4: 当前节点是非叶子节点，找到了
    //case5: 删除后key的数目<下限 （不平衡）
    //case6: 根节点

    public void remove(int key) {
        doRemove(null,root,0, key);
    }

    private void doRemove(Node parent,Node node, int index,int key) {
        int i = 0;
        while (i < node.keyNumber) {
            if (node.keys[i] >= key) {//i 找到 代表删除key的所引   没有找到代表到i个孩子继续查找
                break;
            }

            i++;

        }
        if (node.leaf) {//case 1, case 2
            if (!found(node, key, i)) {
                return;
            } else {
                node.removeKey(i);
            }
        } else {//case 3,case 4
            if (!found(node, key, i)) {
                doRemove(node,node.children[i], i,key);
            } else {
                //1. 找到后继key
                Node s = node.children[i + 1];
                while (!s.leaf) {
                    s = s.children[0];
                }
                int skey = s.keys[0];
                //2. 替换待删除的key
                node.keys[i] = skey;
                //3. 删除后继key
                doRemove(node,node.children[i + 1],i+1, skey);
            }
        }
        if (node.keyNumber < min_key_number) {//case 5, case 6
            //如果小于下限 调整平衡
            balance(parent,node,index);
        }
    }

    private void balance(Node parent, Node x, int i) {
        //case 6 根节点
        if (x == root) {
            if (root.keyNumber == 0&&root.children[0]!=null) {
                root=root.children[0];
            }
            return;
        }
        Node left = parent.childLeftsibing(i);
        Node right = parent.childRightsibing(i);
        if (left != null && left.keyNumber > min_key_number) {
            //case 5-1 左边富裕 右旋
            x.insertKey(parent.keys[i - 1], 0);//父节点中前驱的key旋转下来
            if (!left.leaf) {
                x.insertChild(left.removeRightmostchild(), 0);//left中最大的孩子换爹
            }
            parent.keys[i - 1] = left.removeRightmostkey();//left中最大的key旋转上去
            return;
        }
        if (right != null && right.keyNumber > min_key_number) {
            //case 5-2 右边富裕 左旋
            x.insertKey(parent.keys[i], x.keyNumber);//父节点中后继的key旋转下来
            if (!right.leaf) {
                x.insertChild(right.removeLeftmostchild(), x.keyNumber + 1);//right中最小的孩子换爹
            }
            parent.keys[i] = right.removeLeftmostkey();//right中最小的key旋转上去
            return;
        }
        //case 5-3 两边都不富裕 向左合并
        if (left != null) {
            //向左兄弟合并
            parent.removeChild(i);
            left.insertKey(parent.removeKey(i - 1), left.keyNumber);
            x.moveToTarget(left);

        } else {
            //向自己合并
            parent.removeChild(i+1);
            x.insertKey(parent.removeKey(i),x.keyNumber);
            right.moveToTarget(x);
        }
    }

    private static boolean found(Node node, int key, int i) {
        return i < node.keyNumber && node.keys[i] == key;
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}