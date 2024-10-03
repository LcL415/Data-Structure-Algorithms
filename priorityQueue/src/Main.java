import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public interface queue<E>{
        boolean offer(E value);

        E poll();

        E peek();

        boolean isempty();

        boolean isFull();
    }
    public interface Priority{
        int priority();
    }
    static class Entry implements Priority{
        String value;
        int priority;

        public  Entry(String value, int priority) {
            this.value = value;
            this.priority = priority;
        }

        @Override
        public int priority() {
            return priority;
        }
        public String toString(){
            return "("+value+"priority="+priority+")";
        }
    }
    //优先级队列基于无序数组的实现
    public static class PriorityQueue1<E extends Priority>implements queue<E>{
        Priority[] array;
        int size;
        public PriorityQueue1(int capacity){
            array=new Priority[capacity];
        }

        @Override
        public boolean offer(E value) {
            if (isFull()) {

                return false;
            }
            array[size++]=value;
            return true;
        }

        private int selectMax(){//返回优先值最高的索引值
            int max=0;
            for (int i = 0; i <size ; i++) {
                if (array[i].priority() > array[max].priority()) {
                    max=i;
                }
            }
            return max;
        }
        private void remove(int index){
            if(index<size-1){
                System.arraycopy(array,index+1,array,index,size-1-index);
            }
            size--;
        }
        @Override
        public E poll() {
            if (isempty()) {

                return null;
            }
            int max=selectMax();
           E e=(E) array[max];
            remove(max);
            return e;
        }

        @Override
        public E peek() {
            if (isempty()) {

                return null;
            }
            int max=selectMax();
            return (E) array[max];
        }

        @Override
        public boolean isempty() {
            return size==0;
        }

        @Override
        public boolean isFull() {
            return size==array.length;
        }
    }

    //优先级队列基于有序数组的实现
    public static class PriorityQueue2<E extends Priority>implements queue<E>{
        Priority[] array;
        int size;
        public PriorityQueue2(int capacity){
            array=new Priority[capacity];
        }


        private void insert(E e){
            int i=size-1;
            while(i>=0&&array[i].priority()>e.priority()){
                array[i+1]=array[i];
                i--;
            }
            array[i+1]=e;
        }
        public boolean offer(E value) {
            if (isFull()) {

                return false;
            }
            insert(value);
            size++;
            return true;
        }

        private int selectMax(){//返回优先值最高的索引值
            int max=0;
            for (int i = 0; i <size ; i++) {
                if (array[i].priority() > array[max].priority()) {
                    max=i;
                }
            }
            return max;
        }
        private void remove(int index){
            if(index<size-1){
                System.arraycopy(array,index+1,array,index,size-1-index);
            }
            size--;
        }
        @Override
        public E poll() {
            if (isempty()) {

                return null;
            }

            E e=(E) array[size-1];
            array[--size]=null;//help GC
            return e;
        }

        @Override
        public E peek() {
            if (isempty()) {

                return null;
            }

            return (E) array[size-1];
        }

        @Override
        public boolean isempty() {
            return size==0;
        }

        @Override
        public boolean isFull() {
            return size==array.length;
        }
    }

    //优先级队列用大顶堆实现
    public static class PriorityQueue3<E extends Priority>implements queue<E> {
        Priority[] array;
        int size;

        public PriorityQueue3(int capacity) {
            array = new Priority[capacity];
        }


        private void insert(E e) {
            int i = size - 1;
            while (i >= 0 && array[i].priority() > e.priority()) {
                array[i + 1] = array[i];
                i--;
            }
            array[i + 1] = e;
        }

        //先把要加入的新元素加入尾部，然后向上与parent比较 更新
        public boolean offer(E value) {
            if (isFull()) {
                return false;
            }
            int child = size++;
            int parent = (child - 1) / 2;
            while (child > 0 && value.priority() > array[parent].priority()) {
                array[child] = array[parent];
                child = parent;
                parent = (child - 1) / 2;
            }
            array[child] = value;
            return true;
        }

        private int selectMax() {//返回优先值最高的索引值
            int max = 0;
            for (int i = 0; i < size; i++) {
                if (array[i].priority() > array[max].priority()) {
                    max = i;
                }
            }
            return max;
        }

        private void remove(int index) {
            if (index < size - 1) {
                System.arraycopy(array, index + 1, array, index, size - 1 - index);
            }
            size--;
        }

        private void swap(int i, int j) {
            Priority p = array[j];
            array[j] = array[i];
            array[i] = p;
        }

        private void down(int parent) {
            int left = 2 * parent + 1;
            int right = left + 1;
            int max = parent;
            if (left < size && array[left].priority() > array[max].priority()) {
                max = left;

            }
            if (right < size && array[right].priority() > array[max].priority()) {
                max = right;
            }
            if (parent != max) {
                swap(max, parent);
                down(max);
            }
        }

        //1.先把数组头和尾部进行交换
        //2. 再依次向下与child的优先级比较
        public E poll() {
            if (isempty()) {
                return null;
            }
            swap(0, size - 1);
            size--;
            Priority e = array[size];
            array[size] = null;//help GC
            down(0);
            return (E) e;
        }

        @Override
        public E peek() {
            if (isempty()) {

                return null;
            }

            return (E) array[0];
        }

        @Override
        public boolean isempty() {
            return size == 0;
        }

        @Override
        public boolean isFull() {
            return size == array.length;
        }


    }
    public static class ListNode {
        ListNode next;
        int val;


        public ListNode(ListNode next, int val) {
            this.next = next;
            this.val = val;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            ListNode p = this;
            while (p.next != null) {
                sb.append(p.val).append(",");
                p = p.next;
            }

            sb.append(p.val).append("]");

            return sb.toString();
        }
    }

    // 用小顶堆来实现多个有序链表的合并
    public  static class MinHeap{


            ListNode[]array;
            int size;

            public MinHeap(int capacity) {
                array=new ListNode[capacity];
            }
            public boolean offer(ListNode value) {
                if (isFull()) {
                    return false;
                }
                int child=size++;
                int parent=(child-1)/2;
                while(child>0 && value.val<array[parent].val){
                    array[child]=array[parent];
                    child=parent;
                    parent=(child-1)/2;
                }
                array[child]=value;
                return true;
            }
            private void down(int parent){
                int left=2*parent+1;
                int right=left+1;
                int min=parent;
                if (left<size&&array[left].val <array[min].val ){
                    min=left;

                }
                if (right<size&&array[right].val<array[min].val) {
                    min=right;
                }
                if(parent!=min){
                    swap(min,parent);
                    down(min);
                }
            }
            private void swap(int i,int j){
                ListNode p=array[j];
                array[j]=array[i];
                array[i]=p;
            }

            //1.先把数组头和尾部进行交换
            //2. 再依次向下与child的优先级比较
            public ListNode poll() {
                if (isempty()) {
                    return null;
                }
                swap(0,size-1);
                size--;
                ListNode e=array[size];
                array[size]=null;//help GC
                down(0);
                return  e;
            }





            public boolean isempty() {
                return size==0;
            }


            public boolean isFull() {
                return size==array.length;
            }
        public static ListNode merge(ListNode[] list){
            MinHeap heap =new MinHeap(list.length);
            for (ListNode h : list) {
                if (h != null) {
                    heap.offer(h);
                }
            }
            ListNode s=new ListNode(null,-1);
            ListNode t=s;
            while (!heap.isempty()) {

                ListNode min=heap.poll();
                t.next=min;
                t=min;
                if (min.next != null) {
                    heap.offer(min.next);
                }

            }
            return s.next;
        }
        }






    public static void main(String[] args) {

        PriorityQueue3<Entry>queue=new PriorityQueue3<>(5);
        queue.offer(new Entry("task1",5));
        queue.offer(new Entry("task2",10));
        queue.offer(new Entry("task3",3));
        System.out.println(queue.poll());
        PriorityQueue1<Entry>heap=new PriorityQueue1<>(3);
        ListNode n6=new ListNode(null,6);
        ListNode n5=new ListNode(n6,5);
        ListNode n4=new ListNode(n5,4);
        ListNode n3=new ListNode(null,3);
        ListNode n2=new ListNode(n3,2);
        ListNode n1=new ListNode(n2,1);
        ListNode[]List={n1,n4};
        ListNode m=new MinHeap(2).merge(List);
        System.out.println(m);


    }
}
