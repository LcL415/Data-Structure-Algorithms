public class Tree {
    public class treeNode {
        treeNode left;
        int val;
        treeNode right;

        public treeNode(treeNode left, int val, treeNode right) {
            this.left = left;
            this.val = val;
            this.right = right;
        }

        public treeNode(int val) {
            this.val = val;
        }
    }

    //practice 1：删除节点:先找到要删除的节点，如果只有右孩子，右孩子顶上去，如果只有左孩子，左孩子顶上去，如果左右孩子都有，右侧后任顶上去
    //递归实现
    public treeNode delete(treeNode node, int key) {
        if (node == null) {
            return null;
        }
        if (key < node.val) {
            node.left = delete(node.left, key);
        }
        if (key > node.val) {
            node.right = delete(node.right, key);
        }
        if (node.right == null) {
            return node.left;
        }
        if (node.left == null) {
            return node.right;
        }
        treeNode s = node.right;
        while (s.left != null) {
            s = s.left;
        }
        s.right = delete(node.right, s.val);
        s.left = node.left;
        return s;

    }

    //非递归实现：
    public Object delete2(treeNode node, int key) {
        if (node == null) {
            return null;
        }
        treeNode parent = null;
        while (node != null) {
            if (key < node.val) {
                parent = node;
                node = node.left;
            } else if (key > node.val) {
                parent = node;
                node = node.right;
            } else {
                break;
            }
        }
        if (node.right == null) {
            shift(parent, node, node.left);
        } else if (node.left == null) {
            shift(parent, node, node.right);
        } else {
            treeNode s = node.right;
            treeNode sp = node;
            while (s.left != null) {
                sp = s;
                s = s.left;
            }
            if (sp != node) {
                shift(sp, s, s.right);
                s.right = node.right;
            }
            shift(parent, node, s);
            s.left = node.left;
        }

        return node.val;
    }

    //托孤方法: 被删除节点是右孩子就放右边，被删除节点是左孩子就放左边
    public void shift(treeNode parent, treeNode deleted, treeNode child) {
        if (parent == null) {
            parent=child;
        } else if (parent.left == deleted) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}