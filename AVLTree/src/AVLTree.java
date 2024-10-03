public class AVLTree {
    static void

    class avlnode {
        int key;
        Object value;
        avlnode left;
        avlnode right;
        int height = 1;

        public avlnode(int key) {
            this.key = key;
        }

        public avlnode(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public avlnode(int key, Object value, avlnode left, avlnode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    //求节点高度
    private int height(avlnode node) {
        return node == null ? 0 : node.height;
    }

    //更新节点高度（新增，删除，选择）
    private void updateheight(avlnode node) {
        node.height = Integer.max(height(node.left), height(node.right)) + 1;
    }

    //平衡因子（Balence factor）=左子树高度——右子树高度
    private int bf(avlnode node) {
        return height(node.left) - height(node.right);
    }

    //参数： 要旋转的节点 返回值：新的跟节点
    private avlnode rightRotate(avlnode red) {
        avlnode yellow = red.left;
        avlnode green = yellow.right;
        yellow.right = red;//黄色上位
        red.left = green;//绿色换爹
        updateheight(red);//先更新红色再黄色，顺序不能变
        updateheight(yellow);
        return yellow;
    }

    private avlnode leftRotate(avlnode red) {
        avlnode yellow = red.right;
        avlnode green = yellow.left;
        yellow.left = red;
        red.right = green;
        updateheight(red);
        updateheight(yellow);
        return yellow;
    }

    private avlnode leftRightRotate(avlnode node) {
        node.left = leftRotate(node.left);
        return rightRotate(node);
    }

    private avlnode rightLeftRotate(avlnode node) {
        node.right = rightRotate(node.right);
        return leftRotate(node);
    }

    private avlnode balance(avlnode node) {
        if (node == null) {
            return null;
        }
        int bf = bf(node);
        if (bf > 1 && bf(node.left) >= 0) {//LL
            return rightRotate(node);
        } else if (bf > 1 && bf(node.left) < 0) {//LR
            return leftRightRotate(node);
        } else if (bf < -1 && bf(node.right) > 0) {//RL
            return rightLeftRotate(node);
        } else if (bf < -1 && bf(node.right) <= 0) {//RR
            return leftRotate(node);
        }
        return node;
    }
    avlnode root;
    private void put(int key, Object val){
        root=doput(root,key,val);
    }
    private avlnode doput(avlnode node, int key, Object val){
        if (node == null) {
            return new avlnode(key,val);
        }
        if(node.key==key){
            node.value=val;
        }
        if(node.key<key){
            node.right=doput(node.right,key,val);
        }else {
            node.left=doput(node.left,key,val);
        }
        updateheight(node);
        return balance(node);
    }
    public void remove(int key){

    }
    private avlnode doremove(avlnode node,int key){
        //1.node=null
        if (node == null) {
            return null;
        }
        //2. 没找到key
        if (key < node.key) {
            node.left  = doremove(node.left, key);
        }else if (key>node.key){
            node.right=doremove(node.right,key);
        }
        //3.找到key 没孩子 一个孩子 俩孩子
        else {
            if (node.left == null & node.right == null) {
                return null;
            } else if (node.left == null) {
                node=node.right;
            } else if (node.right == null) {
                node=node.left;
            }else {
                //找后继
                avlnode s=node.right;
                while ((s.left!=null)){
                    s=s.left;
                }
                s.right=doremove(node.right,s.key);
                s.left=node.left;
                node=s;
            }
        }
        //4.更新高度
        updateheight(node);
        //5.balence
        return balance(node);
    }

}