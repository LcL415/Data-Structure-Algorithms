import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class treeNode {
    treeNode left;
    int val;
    String val1;
    treeNode right;

    public treeNode(int val) {
        this.val = val;
    }
    public treeNode(String val) {
        this.val1 = val;
    }

    public treeNode(treeNode left, int val, treeNode right) {
        this.left = left;
        this.val = val;
        this.right = right;
    }

    public static boolean isSymmetric(treeNode root) {
        return check(root.left, root.right);
    }

    //递归检验是否是对称树
    public static boolean check(treeNode left, treeNode right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        if (left.val != right.val) {
            return false;
        }
        return check(left.left, right.right) && check(left.right, right.left);
    }

    public static void main(String[] args) {
        String[] a = {"2", "1", "-", "3", "*"};

        treeNode root = new treeNode(new treeNode(
                new treeNode("4"), "2", new treeNode("4")),
                "1",
                new treeNode(new treeNode("4"), "2", new treeNode("4")));
        root.consturctExpressionTree(a);
        root.maxDepth3(root);
        System.out.println(root.maxDepth3(root));
        //前中遍历的非递归实现-栈  用栈来记录来时的路
        LinkedList<treeNode> stack = new LinkedList<>();
        treeNode current = root;
        treeNode pop = null;
        while (!(current == null) || !stack.isEmpty()) {
            if (!(current == null)) {
                //System.out.print(current.val+"\t");
                stack.push(current);//待处理了左子树
                System.out.print("前" + current.val + "\t");
                current = current.left;//处理左子树
            } else {
                /*pop=stack.pop();
                System.out.print(pop.val+"\t");
                current=pop.right;*/
                // 后序遍历
                treeNode peek = stack.peek();
                if (peek.right == null) {//没有右子树
                    System.out.print("中" + peek.val + "\t");
                    pop = stack.pop();
                    System.out.print("后" + pop.val + "\t");
                } else if (peek.right == pop) {
                    pop = stack.pop();
                    System.out.print("后" + pop.val + "\t");
                } else {//待处理右子树
                    System.out.print("中" + peek.val + "\t");
                    current = peek.right;
                }

            }
        }


    }

    //前中后遍历的递归实现
    //前序遍历
    static void preOrder(treeNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.val + "\t");
        preOrder(node.left);
        preOrder(node.right);

    }

    //中序遍历
    static void inOrder(treeNode node) {
        if (node == null) {
            return;
        }
        inOrder(node.left);
        System.out.print(node.val + "\t");
        inOrder(node.right);
    }

    //后序遍历
    static void postOrder(treeNode node) {
        if (node == null) {
            return;
        }
        postOrder(node.left);

        postOrder(node.right);
        System.out.print(node.val + "\t");
    }


    //最深节点深度 用递归实现 效率最高
    //如果求最小深度，只需要找到第一个叶子节点所在的层，所以用层序遍历效率高
    public int maxDepth(treeNode node) {
        if (node == null) {
            return 0;
        }
        int d1 = maxDepth(node.left);
        int d2 = maxDepth(node.right);
        return Integer.max(d1, d2) + 1;
    }

    //节点深度用非递归实现 后续遍历 栈里的元素个数最大值就是深度
    public int maxDepth2(treeNode node) {
        treeNode current = node;
        treeNode pop = null;
        LinkedList<treeNode> stack = new LinkedList<>();
        int max = 0;
        while (current != null || !stack.isEmpty()) {
            if (current != null) {
                stack.push(current);
                if (stack.size() > max) {
                    max = stack.size();
                }
                current = current.left;
            } else {
                if (stack.peek().right == null || stack.peek().right == pop) {
                    pop = stack.pop();
                } else {
                    current = stack.peek().right;
                }
            }
        }
        return max;
    }

    //用队列实现层序遍历来实现
    public int maxDepth3(treeNode root) {
        Queue<treeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                treeNode poll = queue.poll();
                // System.out.print(poll.val);
                if (poll.left != null) {
                    queue.offer(poll.left);
                }
                if (poll.right != null) {
                    queue.offer(poll.right);
                }
            }
            depth++;
        }
        return depth;
    }

    //翻转二叉树
    private static void fn(treeNode node) {
        if (node == null) {
            return;
        }
        treeNode t = node.left;
        node.left = node.right;
        node.right = t;
        fn(node.left);
        fn(node.right);
    }

    public treeNode invertTree(treeNode root) {
        fn(root);
        return root;

    }

    //根据后缀表达式来建树：1.遇到数字入栈 2. 遇到运算符出栈，先出栈的是运算符的右孩子 建立节点关系再入栈
    public treeNode consturctExpressionTree(String[] tokens) {
        LinkedList<treeNode> stack = new LinkedList<>();
        for (String a : tokens) {
            switch (a) {
                case "+", "-", "*", "/" -> {
                    treeNode parent = new treeNode(a);
                    parent.right = stack.pop();
                    parent.left = stack.pop();
                    stack.push(parent);
                }
                default -> {
                    stack.push(new treeNode(a));
                }
            }

        }
        postOrder(stack.peek());
        return stack.peek();
    }
    //根据中序与后续遍历的结果来构造二叉树
    public treeNode buildTree(int[]inorder,int[]postorder){
        //先找到根节点
        if(inorder.length==0){
            return null;
        }
        int rootvalue=postorder[postorder.length-1];
        treeNode root=new treeNode(rootvalue);
        //找到根节点在inorder中间的位置,分割左右子树
        for (int i = 0; i < inorder.length; i++) {
            if(inorder[i]==rootvalue){
                int[] inorderleft=Arrays.copyOfRange(inorder,0,i);//这个函数含头不含尾
                int[] postorderleft = Arrays.copyOfRange(postorder, 0, i);
                int[] inorderright=Arrays.copyOfRange(inorder,i+1,inorder.length);
                int[] postorderright = Arrays.copyOfRange(postorder, i, postorder.length-1);
                root.left=buildTree(inorderleft,postorderleft);
                root.right=buildTree(inorderright,postorderright);
                break;
            }

        }
        return root;
    }


}