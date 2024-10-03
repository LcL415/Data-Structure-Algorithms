import java.util.PriorityQueue;

public class Miniheap {
    int[] array;
    int size;
    private Miniheap heap;

    public Miniheap() {
    }
    public Miniheap(int k, int[]nums){
        heap=new Miniheap(k);
        for(int num:nums){
            add(num);
        }
    }
    public boolean isfull(){
        return size==array.length;
    }
    //模仿数据流中的加入操作，用堆来实现找在数据流中第K大的值是最好的方法
    public int add(int val){
        if (!heap.isfull()) {
            heap.offer(val);
        }else if (val > heap.peek()) {
            heap.replace(val);
        }
        return heap.peek();
    }

    public Miniheap(int capacity) {
        this.array = new int[capacity];
    }

    public Miniheap(int[] array) {
        this.array = array;
        this.size = array.length;
        heapify();
    }

    private void heapify() {
        //找到最后一个非叶子节点 公式：size/2-1
        for (int i = size / 2 - 1; i >= 0; i--) {
            down(i);
        }
    }

    private void down(int parent) {
        int left = parent * 2 + 1;
        int right = left + 1;
        int min = parent;
        if (left < size && array[left] <array[min]) {
            min = left;
        }
        if (right < size && array[right] <array[min]) {
            min = right;
        }
        if (min != parent) {
            swap(min, parent);
            down(min);
        }
    }

    private void swap(int i, int j) {
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    public int peek() {
        return array[0];
    }

    public int poll() {
        int top = array[0];
        swap(0, size - 1);
        size--;
        down(0);
        return top;
    }

    public int poll(int index) {
        int deleted = array[index];
        swap(index, size - 1);
        size--;
        down(index);
        return deleted;
    }

    public void replace(int replaced) {
        array[0] = replaced;
        down(0);
    }

    public boolean offer(int offered) {
        if (size == array.length) {
            return false;
        }
        up(offered);
        size++;
        return true;
    }

    private void up(int offered) {
        int child = size;
        while (child > 0) {
            int parent = (child - 1) / 2;
            if (offered < array[parent]) {
                array[child] = array[parent];
            } else {
                break;
            }
            child = parent;
        }
        array[child] = offered;
    }
    //求数组第K大的元素
    //1. 向小顶堆中放入前k个元素
    //2. 剩余元素若<=堆顶元素，则略过
    //          若>堆顶元素，则替换堆顶元素 这样小顶堆始终保留目前为止 前k大的元素， 循环解释 堆顶元素为第k大元素
    public int findKthLargest(int[]numbers,int k){
        Miniheap heap=new Miniheap(k);
        for (int i = 0; i < k; i++) {
            heap.offer(numbers[i]);
        }
        for (int i = k; i <numbers.length ; i++) {
            if (numbers[i] > heap.peek()) {
                heap.replace(numbers[i]);
            }
        }
        return heap.peek();
    }
    // 用自带的priorityqueue 来实现寻找数据流中的中位数
    // 左边大顶堆 右边小顶堆  两边个数一样时，左边个数加一， 加的这个数据要先加到右边小顶堆中，然后再弹出最小的再加入到左边大顶堆
    //两边个数不一样时，右边个数加一， 加的这个数据要先加到左边小顶堆中，然后再弹出最大的再加入到右边小顶堆
    public void addNum(int num){
        if (left.size() == rignt.size()) {
            rignt.offer(num);
            left.offer(rignt.poll());
        }else {
            left.offer(num);
            rignt.offer(left.poll());
        }

    }
    public double findeMedian(){
        if (left.size() == rignt.size()) {
            return (left.peek()+rignt.peek())/2.0;
        }else {
            return left.peek();
        }
    }
    // 用栏目大表达式传入构造来改成大顶堆
    private PriorityQueue<Integer>left=new PriorityQueue<>((a,b)->Integer.compare(b,a));
    //默认构造为小顶堆
    private PriorityQueue<Integer>rignt=new PriorityQueue<>();

    public static void main(String[] args) {


        Miniheap heap = new Miniheap(3, new int[]{4,5,8,2});
        System.out.println(heap.add(10));
        System.out.println(heap.add(6));
        Miniheap heap2=new Miniheap();
        heap2.addNum(1);
        heap2.addNum(2);
        heap2.addNum(3);
        heap2.addNum(4);
        heap2.addNum(10);
        System.out.println(heap2.findeMedian());


    }
}