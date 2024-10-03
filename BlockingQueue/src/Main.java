import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    //阻塞队列 用锁来保证线程安全
    //用条件变量让poll或者offer线程进入等待而不是不断地循环尝试让cpu空转
    public interface BlockingQueue<E> {
        void offer(E e) throws InterruptedException;

        boolean offer(E e, long timeout) throws InterruptedException;

        E poll() throws InterruptedException;

    }

    public static class BlockingQueue1<E> implements BlockingQueue<E> {
        private final E[] array;
        private int head;
        private int tail;
        private int size;

        public BlockingQueue1(int capacity) {
            array = (E[]) new Object[capacity];

        }

        private ReentrantLock lock = new ReentrantLock();
        private Condition headWaits = lock.newCondition();
        private Condition tailWaits = lock.newCondition();

        private boolean isEmpty() {
            return size == 0;
        }

        private boolean isFull() {
            return size == array.length;
        }

        @Override
        public void offer(E e) throws InterruptedException {
            lock.lockInterruptibly();
            try {
                while (isFull()) {
                    tailWaits.await();
                }
                array[tail] = e;
                if (++tail == array.length) {
                    tail = 0;
                }
                size++;
                headWaits.signal();//来解锁poll中等待队列非空的线程

            } finally {
                lock.unlock();
            }

        }

        //超时等待
        @Override
        public boolean offer(E e, long timeout) throws InterruptedException {
            lock.lockInterruptibly();
            try {
                long t = TimeUnit.MICROSECONDS.toNanos(timeout);
                while (isFull()) {
                    if (t <= 0) {
                        return false;
                    }
                    t = tailWaits.awaitNanos(t);//avaitNanos()的返回值代表的是剩余时间
                }
                array[tail] = e;
                if (++tail == array.length) {
                    tail = 0;
                }
                size++;
                headWaits.signal();//来解锁poll中等待队列非空的线程
                return true;

            } finally {
                lock.unlock();
            }

        }

        @Override
        public E poll() throws InterruptedException {
            lock.lockInterruptibly();
            try {
                while (isEmpty()) {
                    headWaits.await();
                }
                E e = array[head];
                array[head] = null;
                if (++head == array.length) {
                    head = 0;
                }
                size--;
                tailWaits.signal();//激活等待不满的offer线程
                return e;

            } finally {
                lock.unlock();
            }
        }

        @Override
        public String toString() {
            return "BlockingQueue1{" +
                    "array=" + Arrays.toString(array) +

                    '}';
        }
    }

    public static class BlockingQueue2<E> implements BlockingQueue<E> {
        private final E[] array;
        private int head;
        private int tail;
        private AtomicInteger size = new AtomicInteger();//原子性可以保证只被读一次，保证了多线程安全

        public BlockingQueue2(int capacity) {
            array = (E[]) new Object[capacity];

        }

        private ReentrantLock taillock = new ReentrantLock();
        private ReentrantLock headlock = new ReentrantLock();
        private Condition headWaits = headlock.newCondition();
        private Condition tailWaits = taillock.newCondition();

        private boolean isEmpty() {
            return size.get() == 0;
        }

        private boolean isFull() {
            return size.get() == array.length;
        }

        @Override
        public void offer(E e) throws InterruptedException {
            taillock.lockInterruptibly();
            int c;//添加前的元素个数
            try {
                //1.队列满则等待
                while (isFull()) {
                    tailWaits.await();
                }
                //2.不满入队
                array[tail] = e;
                if (++tail == array.length) {
                    tail = 0;
                }
                //3.修改size
                //size++;
                c=size.getAndIncrement();
                if(c+1<array.length){
                    tailWaits.signal();
                }
            } finally {
                taillock.unlock();
            }
            if (c==0) {
                headlock.lock();
                try {
                    headWaits.signal();
                }//来解锁poll中等待队列非空的线程}
                finally {
                    headlock.unlock();
                }
            }

        }

        //超时等待
        @Override
        public boolean offer(E e, long timeout) throws InterruptedException {
            headlock.lockInterruptibly();
            try {
                long t = TimeUnit.MICROSECONDS.toNanos(timeout);
                while (isFull()) {
                    if (t <= 0) {
                        return false;
                    }
                    t = tailWaits.awaitNanos(t);//avaitNanos()的返回值代表的是剩余时间
                }
                array[tail] = e;
                if (++tail == array.length) {
                    tail = 0;
                }
                //size++;
                size.getAndIncrement();
                headlock.lock();
                try {
                    headWaits.signal();//来解锁poll中等待队列非空的线程
                } finally {
                    headlock.unlock();
                }
                return true;

            } finally {
                headlock.unlock();
            }

        }

        @Override
        public E poll() throws InterruptedException {
            E e;
            int c;//取走前的元素个数
            headlock.lockInterruptibly();
            try {
                //1.队列空则等待
                while (isEmpty()) {
                    headWaits.await();
                }
                //2.不空则入队
                 e = array[head];
                array[head] = null;
                if (++head == array.length) {
                    head = 0;
                }
                //3.修改size
                //size--;
                c=size.getAndDecrement();
                if(c>1){
                    headWaits.signal();
                }




            } finally {
                headlock.unlock();
            }
            if (c== array.length) {
                taillock.lock();
                try {
                    tailWaits.signal();//激活等待不满的offer线程
                } finally {
                    taillock.unlock();
                }
            }
            return e;
        }

        @Override
        public String toString() {
            return "BlockingQueue1{" +
                    "array=" + Arrays.toString(array) +

                    '}';
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new BlockingQueue1<>(3);
        Thread t1 = new Thread(() -> {
            try {
                System.out.println(System.currentTimeMillis() + "Begin");
                queue.offer("task1");
                System.out.println(queue);
                queue.offer("task2");
                System.out.println(queue);
                queue.offer("task3");
                System.out.println(queue);
                queue.offer("task4");
                System.out.println(queue);
                System.out.println(System.currentTimeMillis() + "End");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "生产者");
        t1.start();
        Thread.sleep(2000);
        queue.poll();
    }
}