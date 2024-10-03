


import java.util.*;

public class Duilie {
    //合并两个有序数组或者一个数组内部排序
      //方法1：递归
    public static void merge(int[] a1, int i, int iend, int j, int jend, int[] a2, int k) {
        if(i>iend){
            System.arraycopy(a1,j,a2,k,jend-j+1);
            return;
        }
        if(j>jend){
            System.arraycopy(a1,i,a2,k,iend-i+1);
            return;
        }
        if(a1[i]<a1[j])
        {
            a2[k]=a1[i];
            merge(a1,i+1,iend,j,jend,a2,k+1);
        }
        else {
            a2[k]=a1[j];
            merge(a1, i, iend,j+1,jend,a2,k+1);
        }
    }
    //方法2： 双指针
    public static void merge2(int[]a1,int i,int iend, int j,int jend, int[]a2){
        int k=0;
        while(i<=iend&&j<=jend){
            if(a1[i]<=a1[j]){
                a2[k]=a1[i];
                i++;
            }
            else {
                a2[k]=a1[j];
                j++;
            }
            k++;
        }
        if(i>iend){
            System.arraycopy(a1,j,a2,k,jend-j+1);
        }
        else {
            System.arraycopy(a1,i,a2,k,iend-i+1);
        }
    }
  // 队列
    public interface queue<E>{
        boolean offer(E value);

        E poll();

        E peek();

        boolean isempty();

        boolean isFull();
  }
    // 队列的链表实现
  public static class LinkedListQueue<E> implements queue<E>, Iterable<E>{
        @Override
        public boolean offer(E value) {
            if (isFull()){
                return false;
            }
            Node<E>added=new Node<>(value,head);
            tail.next=added;
            tail=added;
            size++;
            return true;
        }

        @Override
        public E poll() {//获取第一个节点的值，并且移除
            if(isempty()) {
                return null;
            }
            Node<E>first=head.next;
            head.next=first.next;
            if(first==tail){
                tail=head;
            }
            size--;
            return first.value;
        }

        @Override
        public E peek() { //获取第一个节点的值
            if(isempty()){
            return null;
            }
            return head.next.value;
        }

        @Override
        public boolean isempty() {
            return head==tail;
        }

        @Override
        public boolean isFull() {

            return size==capacity;
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                Node<E>p=head.next;
                public boolean hasNext() {
                    return p!=head;
                }

                @Override
                public E next() {
                    E value=p.value;
                    p=p.next;
                    return value;
                }
            };
        }

        private static class Node<E>{
            E value;
            Node<E> next;

            public Node(E value, Node<E> next) {
                this.value = value;
                this.next = next;
            }
        }
        private Node<E> head=new Node<>(null,null);
        private Node<E> tail=head;
        private int size;
        private int capacity=Integer.MAX_VALUE;

        public LinkedListQueue(int capacity) {
            this.capacity = capacity;
            tail.next= head;
        }

        public LinkedListQueue() {
            tail.next=head;
        }
    }

    // 队列的环形数组实现
    public static class ArrayQueue1<E>implements queue<E>,Iterable<E>{
        private E[]array;
        private int head=0;
        private int tail=0;

        public ArrayQueue1(int capacity) {
            array =(E[]) new Object[capacity+1];
        }

        @Override
        public boolean offer(E value) {
            if(isFull()){
                return false;
            }
           array[tail]=value;
            tail=(tail+1)%array.length;
            return true;
        }

        @Override
        public E poll() {
            if (isempty()) {
                return null;
            }
            E value=array[head];
            head=(head+1)%array.length;
            return value;
        }

        @Override
        public E peek() {
            if (isempty()) {
                return null ;
            }
            return array[head];
        }

        @Override
        public boolean isempty() {
            return head==tail;
        }

        @Override
        public boolean isFull() {
            return (tail+1)%array.length==head;
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                int p=head;
                @Override
                public boolean hasNext() {
                    return p!=head;
                }

                @Override
                public E next() {
                    E value=array[p];
                    p=(p+1)%array.length;
                    return value;
                }
            };
        }
    }

    public static class ArrayQueue2<E>implements queue<E>,Iterable<E>{
        private  E [] array;
        private int head=0;
        private int tail=0;

        public ArrayQueue2(int capacity) {
            array=(E[])new Object[capacity];
        }

        @Override
        public boolean offer(E value) {
            if(isFull()){
                return false;
            }
            //tail 他本身不是索引，是要根据tail的值计算出索引  Integer to unsignedlong 用来考虑
            // tail 或者 head 超过边界的情况
            array[(int)Integer.toUnsignedLong(tail)%array.length]=value;
            tail++;
            return true;
        }

        @Override
        public E poll() {
            if (isempty()) {
                return null;
            }
            E value = array[(int)Integer.toUnsignedLong(head)% array.length];
            head++;
            return value;
        }

        @Override
        public E peek() {
            if (isempty()) {
                return null;
            }
            return array[(int)Integer.toUnsignedLong(head) % array.length];
        }

        @Override
        public boolean isempty() {
            return head==tail;
        }

        @Override
        public boolean isFull() {
            return tail-head==array.length;
        }

        @Override
        public Iterator<E> iterator() {

            return new Iterator<E>() {
                int p=head;
                @Override
                public boolean hasNext() {
                    return p!=head;
                }

                @Override
                public E next() {
                    E value=array[(int)Integer.toUnsignedLong(p)%array.length];
                    p++;
                    return value;
                }
            };
        }
    }


    //二叉树的层序遍历
    public static class TreeNode{
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode( TreeNode left, int val,TreeNode right) {

            this.left = left;
            this.val = val;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.valueOf(this.val);
        }
    }

    // 栈 stack
    // 二叉树的z字型遍历
    public static void main(String[] args) {

        //int a[]={1,3,4,5,2,6,7,9};
        //int b[]=new int[a.length];
        //merge2(a,0,3,4,7,b);
        //System.out.printf(Arrays.toString(b));

      /*LinkedListQueue<Integer> a= new LinkedListQueue<>();
        a.offer(3);
        a.offer(2);
        a.offer(3);
        System.out.println(a.poll());*/
        TreeNode root=new TreeNode(
                new TreeNode(
                        new TreeNode(4),2,new TreeNode(5)),
                     1,
                        new TreeNode(new TreeNode(6),3,new TreeNode(7)));
        List<List<Integer>> result=new ArrayList<>();
        LinkedListQueue<TreeNode>queue=new LinkedListQueue<>();
         queue.offer(root);
         int c1=1;//当前层数的节点数
        boolean odd=true;
        while (!queue.isempty()) {
            LinkedList<Integer>level=new LinkedList<>();
            int c2=0;//下一层节点数

            for (int i = 0; i < c1; i++) {
                TreeNode n=queue.poll();
                //System.out.print(n +" ");
                if(odd){
                    level.offerLast(n.val);
                }
                else {
                    level.offerFirst(n.val);
                }
                //level.add(n.val);
                if (n.left != null) {
                    queue.offer(n.left);
                    c2++;
                }
                if (n.right != null) {
                    queue.offer(n.right);
                    c2++;
                }
            }
            odd=!odd;
            result.add(level);
            c1=c2;
            //System.out.println();


        }
        System.out.println(result);















    }

}