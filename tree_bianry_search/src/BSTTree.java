public class BSTTree<T extends Comparable<T>> {

    BSTNode<T> root;//根节点

    public Object get(T key) {

        return doget(root, key);
    }

    private Object doget(BSTNode<T> node, T key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {// compareto 返回-1 key<node.key     0=    1>

            return doget(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            return doget(node.right, key);
        } else {
            return node.value;
        }

    }

    //非递归来实现get
    public Object get2(T key) {
        BSTNode<T> node = root;
        while (node != null) {
            int result = key.compareTo(node.key);
            if (result > 0) {
                node = node.right;
            } else if (result < 0) {
                node = node.left;
            } else {
                return node.value;
            }
        }
        return null;
    }

    public Object min() {
        return domin(root);
    }

    public Object domin(BSTNode node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node.value;
        }
        return domin(node.left);
    }

    //非递归的实现
    public Object min2() {
        if (root == null) {
            return null;
        }
        BSTNode p = root;
        while (p.left != null) {
            p = p.left;
        }
        return p.value;
    }
    public Object min(BSTNode<T> node) {
        if (node == null) {
            return null;
        }

        while (node.left != null) {
            node = node.left;
        }
        return node.value;
    }

    public Object max() {
        if (root == null) {
            return null;
        }
        BSTNode p = root;
        while (p.right != null) {
            p = p.right;
        }
        return p.value;
    }
    public Object max(BSTNode<T>node) {
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node.value;
    }

    public void put(T key, Object value) {
        BSTNode<T> node = root;
        BSTNode<T> parent=null;
        while (node != null) {
            parent=node;
            if (key.compareTo(node.key) < 0) {// compareto 返回-1 key<node.key     0=    1>
                node = node.left;
            } else if (key.compareTo(node.key) > 0) {
                node = node.right;
            } else {
                //1.key 有 更新
                node.value=value;
                break;
            }
        }
        //2. key 没有 新增
       if(parent==null){
           root=new BSTNode<T>(key,value);
           return;
       }
        if (key.compareTo(parent.key)<0) {
            parent.left=new BSTNode<T>(key,value);
        }else {
            parent.right=new BSTNode<T>(key,value);
        }

    }
    //找后任节点
    //1. 节点有右子树，此时后任节点为右子树的最小值
    //2. 节点无右子树，自右而来的最近的祖先就是前任
    public Object successor(T key) {

        BSTNode<T> node=root;
        BSTNode<T>parentfromright=null;
        while (node!=null) {

            if (key.compareTo(node.key) < 0) {
                parentfromright=node;
                node=node.left;
            }else if(key.compareTo(node.key)>0){
                node=node.right;
            }else {
                break;
            }
        }
        if (node == null) {
            return null;
        }
        if (node.right != null) {
            return min(node.right);
        }
        return parentfromright!=null?
                parentfromright.value:null;

    }
    //找前任：情况1：当节点有左子树，此时前任就是左子树的最大值
            //情况2： 当节点没有左子树，自左而来的最近的祖先就是前任
    public Object predecessor(T key) {
        //先要找到当前节点
        BSTNode <T>p=root;
        BSTNode<T>ancesterfromleft=null;
        while (p != null) {
            if (key.compareTo(p.key) < 0) {
                p=p.left;
            } else if (key.compareTo(p.key)>0) {
               ancesterfromleft=p;
                p=p.right;
            }else
            {
                break;
            }
        }
        if (p == null) {
            return null;
        }
        //情况1：当节点有左子树，此时前任就是左子树的最大值
        if (p.left != null) {
            return max(p.left);
        }
        //情况2： 当节点没有左子树，自左而来的最近的祖先就是前任
        return ancesterfromleft!=null?
                ancesterfromleft.value:null;

    }

    public Object delete(T  key) {
        BSTNode<T> p = root;
        BSTNode<T> parent = null;
        while (p != null) {
            if (key.compareTo(p.key) < 0) {
                parent = p;
                p = p.left;
            } else if (key.compareTo(p.key) > 0) {
                parent = p;
                p = p.right;
            } else {
                break;
            }
            if (p.left == null ) {
                shift(parent,p,p.right);
            }else if(p.right == null){
                shift(parent,p,p.left);
            }else{
                //1.被删除节点找后继
                BSTNode<T> s=p.right;
                BSTNode<T> sparent=p;
                while (s.left != null) {
                    sparent=s;
                    s=s.left;
                }
                //后继节点即为s
                if (sparent != p) {
                    //删除和后继不相邻，处理后继的后事
                    shift(sparent,s,s.right);//不可能有左孩子
                    s.right=p.right;
                }
                //后继取代被删除的节点
                shift(parent,p,s);
                s.left=p.left;
            }

        }
        return p.value;
    }
    private void shift(BSTNode<T>parent,BSTNode<T>deleted, BSTNode<T>child){
        if (parent == null) {
            root=child;
        } else if (deleted == parent.left) {
            parent.left=child;
        }else {
            parent.right=child;
        }

    }

    static class BSTNode<T> {
        T key;
        Object value;
        BSTNode<T> left;
        BSTNode<T> right;

        public BSTNode(T key) {
            this.key = key;
        }

        public BSTNode(T key, Object value) {
            this.key = key;
            this.value = value;
        }

        public BSTNode(T key, Object value, BSTNode<T> left, BSTNode<T> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

    }

    public static void main(String[] args) {
        BSTNode<String> n1 = new BSTNode("a", "张无忌");
        BSTNode<String> n3 = new BSTNode("c", "宋青书");
        BSTNode<String> n2 = new BSTNode("b", "周芷若", n1, n3);

        BSTNode<String> n5 = new BSTNode("e", "说不得");
        BSTNode<String> n7 = new BSTNode("g", "引力");
        BSTNode<String> n6 = new BSTNode("f", "赵敏", n5, n7);

        BSTNode<String> root = new BSTNode("d", "小昭", n2, n6);
        BSTTree tree = new BSTTree();
        tree.root = root;
        tree.put("x","Liuclin");
        System.out.println(tree.successor("g"));
    }
}