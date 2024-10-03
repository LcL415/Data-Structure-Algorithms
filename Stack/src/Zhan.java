import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Zhan {




    //栈的接口
    public interface Stack<E> {
        //向栈内压入元素
        boolean push(E value);

        //向栈顶弹出元素
        E pop();

        //返回栈顶元素不弹出
        E peek();

        boolean isEmpty();

        boolean isFull();
    }

    //栈的链表实现
    public static class LinkedListStack<E> implements Stack<E>, Iterable<E> {

        private int capacity;
        private int size;
        private Node<E> head = new Node<>(null, null);
        public LinkedListStack(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public boolean push(E value) {
            if (isFull()) {
                return false;
            }
            head.next = new Node<>(value, head.next);
            size++;
            return true;
        }

        @Override
        public E pop() {
            if (isEmpty()) {
                return null;
            }
            Node<E> first = head.next;
            head.next = first.next;
            size--;
            return first.value;
        }

        @Override
        public E peek() {
            if (isEmpty()) {
                return null;
            }
            return head.next.value;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean isFull() {
            return size == capacity;
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                Node<E> p = head.next;

                @Override
                public boolean hasNext() {
                    return p != null;
                }

                @Override
                public E next() {
                    E value = p.value;
                    p = p.next;
                    return value;
                }
            };
        }

        private static class Node<E> {
            E value;
            Node<E> next;

            public Node(E value, Node<E> next) {
                this.value = value;
                this.next = next;
            }
        }
    }

    //栈的数组实现
    //从数组的右边当作top 也从右边开始遍历
    public static class Arraystack<E> implements Stack<E>, Iterable<E> {
        private E[] array;
        private int top;//栈顶指针

        public Arraystack(int capacity) {
            this.array = (E[]) new Object[capacity];
        }

        @Override
        public boolean push(E value) {
            if (isFull()) {
                return false;
            }
            array[top] = value;
            top++;
            return true;
        }

        @Override
        public E pop() {
            if (isEmpty()) {
                return null;
            }
            E value = array[top - 1];
            top--;

            return value;
        }

        @Override
        public E peek() {
            if (isEmpty()) {
                return null;
            }
            E value = array[top - 1];

            return value;
        }

        @Override
        public boolean isEmpty() {
            return top == 0;
        }

        @Override
        public boolean isFull() {
            return top == array.length;
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                int p = top;

                @Override
                public boolean hasNext() {
                    return p > 0;
                }

                @Override
                public E next() {
                    E value = array[p - 1];
                    p--;
                    return value;
                }
            };
        }
    }

    //栈的练习题 有效的括号
    /*遇到左括号，把要配对的右括号放入栈顶
        遇到右括号，与栈顶元素相比较
        若相等 弹出栈顶元素
         不相等直接返回false*/
    public static boolean isValid(String s) {
        Arraystack<Character> stack = new Arraystack<>(s.length());
        for (int i = 0; i < s.length() ; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(')');}
            else if (c == '[') {
                stack.push(']');
            }
            else if(c=='{'){
                stack.push('}');
            }
            else {
                if(!stack.isEmpty() && c==stack.peek()){
                    stack.pop();
                }
                else {
                    return false;
                }
            }
        }

        return stack.isEmpty();

    }

    //逆波兰表达式（后缀表达式）
    public static int evalRPN(String[]tokens){
        LinkedList<Integer>stack=new LinkedList<>();
        for(String t:tokens){
            switch(t){
                case "+"->{
                    Integer b=stack.pop();
                    Integer a=stack.pop();
                    stack.push(a+b);
                }
                case "-"->{
                    Integer b=stack.pop();
                    Integer a=stack.pop();
                    stack.push(a-b);
                }
                case "*"->{
                    Integer b=stack.pop();
                    Integer a=stack.pop();
                    stack.push(a*b);
                }
                case "/"->{
                    Integer b=stack.pop();
                    Integer a=stack.pop();
                    stack.push(a/b);
                }
                default -> {
                    stack.push(Integer.parseInt(t));
                }
            }
        }
        return stack.pop();
    }

    //中缀表达式
    //规则：1. 遇到非运算符 直接拼串
    //2. 遇到运算符：如果他的优先级比栈顶的高，入栈，否则要把栈里>=他的都出栈，他再入栈
    //3. 遍历完成，栈里剩余运算符依次出栈
    //4. 带（）：左括号直接入栈，左括号优先级设置为0，右括号就把栈里到左括号为止的所有运算符出栈

    // 计算运算符的优先级
    static int priority(char c)  {
        return switch(c){
            case'*','/'->2;
            case'+','-'->1;
            case'('->0;
                default -> throw new IllegalArgumentException("illegal:"+c);

        };
    }
    static String infixToSuffix(String exp){
        LinkedList<Character>stack=new LinkedList<>();
        StringBuilder sb=new StringBuilder(exp.length());
        for (int i = 0; i < exp.length(); i++) {
            char c=exp.charAt(i);
            switch(c){
                case'+','-','*','/'->{
                    if(stack.isEmpty()){
                        stack.push(c);
                    }else{
                        if(priority(c)>priority(stack.peek())){
                            stack.push(c);
                        }else{
                            while(!stack.isEmpty()&&stack.peek()>=priority(c)){
                                sb.append(stack.pop());
                            }
                            stack.push(c);
                        }
                    }

                }
                case'('->{
                    stack.push(c);

                }
                case')'->{
                    while(stack.peek()!='('){
                        sb.append(stack.pop());
                    }
                    stack.pop();
                }
                default->{
                    sb.append(c);
                }

            }
        }
        while(!stack.isEmpty()){
            sb.append(stack.pop());
        }
        return sb.toString();
    }

    // 用两个栈来模拟队列
    public static class simulation{
        Arraystack<Integer>s1=new Arraystack<>(100);
        Arraystack<Integer>s2=new Arraystack<>(100);


        public void push(int x){//向队列尾部添加
            s2.push(x);
        }
        public int pop(){//从队列头部移除
            if(s1.isEmpty()){
                while(!s2.isEmpty()){
                    s1.push(s2.pop());
                }
            }
            return s1.pop();
        }
        public int peek(){//从队列头部获取
            if(s1.isEmpty()){
                while(!s2.isEmpty()){
                    s1.push(s2.pop());
                }
            }
            return s1.peek();
        }
        public boolean empty(){
            return s1.isEmpty()&&s2.isEmpty();
        }


    }

    //用一个队列来模拟栈
    public static class simulation2{
        private int size=0;
        LinkedList<Integer> queue=new LinkedList<>();
        public  void push(int x){

            queue.offer(x);
            for (int i = 0; i < size; i++) {
                queue.offer(queue.poll());
            }
            size++;
        }
        public  int pop(){
            size--;
            return queue.poll();
        }
        public int top(){
            return queue.peek();
        }
        public boolean empty(){
            return      queue.isEmpty();
        }
    }

    //双端队列
    //双向环形列表来实现双端队列
    public interface Deque<E>{
        boolean offerFirst(E e);
        boolean offerLast(E e);
        E pollFirst();
        E pollLast();
        E peekFirst();
        E peekLast();
        boolean isEmpty();
        boolean isFull();
    }
    public static class LinkedListDeque<E>implements Deque<E>,Iterable<E>{


        static class Node<E>{
            Node<E> prev;
            E value;
            Node<E>next;

            public Node(Node<E> prev, E value, Node<E> next) {
                this.prev = prev;
                this.value = value;
                this.next = next;
            }
        }
        int capacity;
        int size;
        Node<E>sentinel=new Node<>(null,null,null);

        public LinkedListDeque(int capacity) {
            this.capacity = capacity;
            sentinel.next=sentinel;
            sentinel.prev=sentinel;
        }
        @Override
        public boolean offerFirst(E e) {
            if (isFull()) {
                return false;
            }
            Node<E> a=sentinel;
            Node<E>b=sentinel.next;
            Node<E>added= new Node(a,e,b);
            a.next=added;
            b.prev=added;
            size++;

            return true;
        }

        @Override
        public boolean offerLast(E e) {
            if (isFull()) {

                return false;
            }
            Node<E> a=sentinel.prev;
            Node<E>b=sentinel;
            Node<E>added= new Node(a,e,b);
            a.next=added;
            b.prev=added;
            size++;
            return true;
        }

        @Override
        public E pollFirst() {
            if(isEmpty()){
                return null;
            }
            Node<E>a=sentinel;
            Node<E>removed=sentinel.next;
            Node<E>b=removed.next;
            a.next=b;
            b.prev=a;
            size--;
            return removed.value;
        }

        @Override
        public E pollLast() {
            if(isEmpty()){
                return null;
            }

            Node<E>removed=sentinel.prev;
            Node<E>a=removed.prev;
            Node<E>b=sentinel;
            a.next=b;
            b.prev=a;
            size--;
            return removed.value;
        }

        @Override
        public E peekFirst() {
            if(isEmpty()){
                return null;
            }
            return sentinel.next.value;
        }

        @Override
        public E peekLast() {
            if(isEmpty()){
                return null;
            }
            return sentinel.prev.value;
        }

        @Override
        public boolean isEmpty() {
            return size==0;
        }

        @Override
        public boolean isFull() {
            return size==capacity;
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                Node<E>p=sentinel.next;
                @Override
                public boolean hasNext() {
                    return p!=sentinel;
                }

                @Override
                public E next() {
                    E value=p.value;
                    p=p.next;

                    return value;
                }
            };
        }

    }

    // 循环数组来实现双端队列  tail 停下来的位置不储存数据 会浪费一个位置
    //尤其注意环形数组 的index不是简单的加减，要分别重写inc和dec的函数来实现
// offerLast: 先添加元素再tail++；  offerFirst: 先head--,再添加元素；
// pollFirst:先获取要移除的值 head++;  polllast: 先tail--；再获取要移除的值
    //head=tail 空； head 到tail 的距离等于数组长度-1； 满
    public static class ArrayDeque1<E>implements Deque<E>,Iterable<E>{
        static int inc(int i, int length){
            if (i+1>=length) {
                return 0;
            }
            return i+1;
        }
        static int dec(int i,int length){
            if(i-1<0){
                return length-1;
            }
            return i-1;
        }
        E[]array;
        int head;
        int tail;

        public ArrayDeque1(int capacity) {
            array= (E[])new Object[capacity+1];//因为会浪费一个tail的位置 所以初始化时候多一个位置
        }


        @Override
        public boolean offerFirst(E e) {
            if (isFull()) {
                return false;
            }
            head=dec(head,array.length);
            array[head]=e;
            return true;
        }

        @Override
        public boolean offerLast(E e) {
            if (isFull()) {
                return false;
            }

            array[tail]=e;
           tail=inc(tail,array.length);
            return true;
        }

        @Override
        public E pollFirst() {
            if (isEmpty()) {
                return null;
            }
            E e=array[head];
            array[head]=null;// help GC
            head=inc(head,array.length);
            return e;
        }

        @Override
        public E pollLast() {
            if (isEmpty()) {
                return null;
            }
            tail=dec(tail,array.length);
            E e=array[tail];
            array[tail]=null;//help GC
            return e;
        }

        @Override
        public E peekFirst() {
            if (isEmpty()) {
                return null;
            }

            return array[head];
        }

        @Override
        public E peekLast() {
            if (isEmpty()) {
                return null;
            }

            return array[dec(tail,array.length)];
        }

        @Override
        public boolean isEmpty() {
            return head==tail;
        }

        @Override
        public boolean isFull() {
            if(tail>head){
                return tail-head== array.length-1;
            }
            else if(tail<head){
                return head-tail==1;
            }
            else {
                return false;
            }

        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                int p=head;
                @Override
                public boolean hasNext() {
                    return p!=tail;
                }

                @Override
                public E next() {
                    E e=array[p];
                    p=inc(p,array.length);
                    return e;
                }
            };
        }
    }
    public static void main(String[] args) {
        /*Arraystack<Integer> a = new Arraystack<>(3);
        a.push(1);
        a.push(2);
        a.push(3);
        System.out.println(a.pop());
        System.out.println(a.pop());
        System.out.println(a.pop());

        System.out.println(a.isEmpty());*/


       // System.out.println(isValid("[](){}"));
     /*   String[] token={"2","1","+","3","*"};
        System.out.println(infixToSuffix("(a+b)*c"));*/
        /*simulation a=new simulation();
        a.push(1);
        a.push(2);
        a.pop();
        System.out.println(a.peek());
*/
      /*  simulation2 a=new simulation2();
        a.push(1);
        a.push(2);
        a.push(3);
        System.out.println(a.pop());
        System.out.println(a.pop());*/
        ArrayDeque1<Integer>deque=new ArrayDeque1<>(3);
        deque.offerFirst(1);
        deque.offerLast(3);
        deque.offerFirst(5);
        deque.pollFirst();
        deque.pollLast();
        System.out.println(deque.pollLast());

    }


}
