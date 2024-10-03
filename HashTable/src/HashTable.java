import java.util.*;

public class HashTable {
    // 哈希算法 摘要算法 散列算法  生成哈希码
    //哈希表中的节点类
    static class Entry {
        int hash;//哈希码
        Object key;//键
        Object value;//值
        Entry next;

        public Entry(int hash, Object key, Object value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        Entry[] table = new Entry[16];
        int size = 0; //元素个数
        float loadFactor = 0.75f;// 12 阈值
        int threshold = (int) (loadFactor * table.length);

        //为了增加效率，将求模运算替换为按位与运算---前提是数组长度是2的n次方
        // hash%数组长度==hash&（数组长度-1）
        Object get(int hash, Object key) {
            int idx = hash & (table.length - 1);
            if (table[idx] == null) {
                return null;
            }
            Entry p = table[idx];
            while (p != null) {
                if (p.key.equals(key)) {//object 的比较如果用==就是比较的地址值，要用。equals
                    return p.value;
                }
                p = p.next;
            }
            return null;
        }

        public Object get(Object key) {
            int hash = Hash(key);
            return get(hash, key);
        }

        void put(int hash, Object key, Object value) {
            int idx = hash & (table.length - 1);
            if (table[idx] == null) {//有空位直接新增
                table[idx] = new Entry(hash, key, value);

            } else {//无空位 沿着链表查找 有重复的key更新，没有的话在结尾新增
                Entry p = table[idx];
                while (true) {
                    if (p.key.equals(key)) {
                        p.value = value;
                        return;
                    }
                    if (p.next == null) {
                        break;
                    }
                    p = p.next;
                }
                p.next = new Entry(hash, key, value);

                size++;
                if (size > threshold) {
                    resize();
                }
            }
        }

        public void put(Object key, Object value) {
            int hash = Hash(key);
            put(hash, key, value);

        }

        private static int Hash(Object key) {
            return key.hashCode();
        }

        private void resize() {
            Entry[] newTable = new Entry[table.length << 1];//*2 相当于左移一位
            for (int i = 0; i < table.length; i++) {
                Entry p = table[i];//拿到每个链表的头
                if (p != null) {
                    //拆分链表 移动到新数组:
                    // 规律： 一个链表最多拆成两个 ；
                    // hash & table.length==0的一组； 不等于0 的另一组
                    Entry a = null;
                    Entry b = null;
                    Entry aHead = null;
                    Entry bHead = null;
                    while (p != null) {
                        if ((p.hash & table.length) == 0) {

                            if (a != null) {
                                a.next = p;
                            } else {
                                aHead = p;
                            }
                            a = p;
                        } else {
                            if (b != null) {
                                b.next = p;
                            } else {
                                bHead = p;
                            }
                            b = p;
                        }
                        p = p.next;
                    }
                    if (a != null) {
                        a.next = null;
                        newTable[i] = aHead;
                    }
                    if (b != null) {
                        b.next = null;
                        newTable[i + table.length] = bHead;
                    }
                }
            }
            table = newTable;
            threshold = (int) (loadFactor * table.length);
        }

        Object remove(int hash, Object key) {
            int idx = hash & (table.length - 1);
            if (table[idx] == null) {
                return null;
            }
            Entry p = table[idx];
            Entry pre = null;
            while (p != null) {
                if (p.key.equals(key)) {
                    if (pre == null) {
                        table[idx] = p.next;
                    } else {
                        pre.next = p.next;
                    }
                    size--;
                    return p.value;
                }
                pre = p;
                p = p.next;
            }
            return null;
        }

        public Object remove(Object key) {
            int hash = Hash(key);
            return remove(hash, key);
        }
    }

    // ex01 两数之和
    //给定一个整数数组nums和一个目标target 在数组中找出和为目标值的两个整数 并返回他们对应的下标
    //思路： 循环遍历数组 拿到每个数字x， 然后以target-x作为key到hash表中查找
    //若找到了 返回x和他配对的索引即可， 若没找到 将x作为key， 他的索引作为value放入hash 表中
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int x = nums[i];
            int y = target - x;
            if (map.containsKey(y)) {
                return new int[]{i, map.get(y)};
            } else {
                map.put(x, i);
            }
            i++;
        }
        return null;
    }

    // ex02 给一个字符串s,请你找出其中不含重复字符的最长子串的长度
    //用两个指针begin和end来表示字串开始结束位置
    //用hash表来检查重复的字符
    //从左向右查看每个字符 如果遇到不是重复的 调整end 如果遇到重复的 调整begin 将当前的字符放入hash表中
    // end-begin+1 就是当前子串长度
    public static int lengthOfLongestSubstring(String s) {
        HashMap<Character, Integer> Map = new HashMap<>();
        int max = 0;
        int begin = 0;
        for (int end = 0; end < s.length(); end++) {
            char ch = s.charAt(end);
            if (Map.containsKey(ch)) {
                begin = Math.max(begin, Map.get(ch) + 1);// 防止begin指针回退，否则abba 这种案例通不过
                Map.put(ch, end);
            } else {
                Map.put(ch, end);
            }
            max = Math.max(max, end - begin + 1);
        }
        return max;
    }

    //ex03 字母异位词分组 //ab ba//eat eta ate aet
    //解法1：
    //遍历字符串 每个字符串的字符重新排序后作为key
    //所谓分组，其实就是准备一个集合，把这些单词加入到key相同的集合中
    //返回分组结果
    public static List<List<String>> groupAnagrams(String[] strs) {
        HashMap<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] charArray = str.toCharArray();
            Arrays.sort(charArray);
            String key = new String(charArray);
            List<String> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(str);
        }
        return new ArrayList<>(map.values());
    }

    //ex03 解法2：如果字符串中只有小写字母，那就只有26种可能 用一个长度为26的整数数组作为字符串的key
    static class ArrayKey {
        int[] key = new int[26];

        //用intelji自带的equalsandhashcode 生成这俩方法
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ArrayKey arrayKey = (ArrayKey) o;
            return Arrays.equals(key, arrayKey.key);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(key);
        }

        public ArrayKey(String str) {
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                key[ch - 97]++;//97 代表的是a
            }
        }
    }

    public static List<List<String>> groupAnagrams2(String[] strs) {
        HashMap<ArrayKey, List<String>> map = new HashMap<>();
        for (String str : strs) {
            ArrayKey key = new ArrayKey(str);
            List<String> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(str);
        }
        return new ArrayList<>(map.values());
    }

    //ex04 判断有没有重复的元素
    public boolean containsDuplicate(int[] nums) {
        HashMap<Integer, Object> map = new HashMap<>();
        Object value = new Object();//因为在这个例子中我们用不到value值 所以可以直接固定
        for (int key : nums) {
            if (map.put(key, value) == null) {//map 的put方法是返回上次key的地方 这里类似与HashSet 的add方法
                return true;
            }

        }
        return false;
    }

    //ex05 找出只出现一次的数字 除了某个元素出现一次 其余都出现两次
    //方法1：用hashset 解决
    public int singleNumber(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (!set.add(num)) {
                set.remove(num);
            }
        }
        return set.toArray(new Integer[0])[0];
    }

    //方法2：用亦或解决效率更高 两两出现的时候考虑异或
    public int singleNumber2(int[] nums) {
        int num = nums[0];
        for (int i = 1; i < nums.length; i++) {
            num = num ^ nums[i];
        }
        return num;
    }

    //e06 判断两个单词是否为字母异位词
    private int[] getKey(String s) {
        int[] array = new int[26];
        char[] h = s.toCharArray();
        for (char c : h) {
            array[c - 97]++;
        }


        return array;
    }

    public boolean isAnagram(String s, String t) {
        return Arrays.equals(getKey(s), getKey(t));
    }
    //e07 找第一个不重复的字符
    public int firstUniq(String s){
        int[]array=new int[26];
        char[] ch = s.toCharArray();
        for (char c : ch) {
            array[c-97]++;
        }
        for (int i = 0; i < ch.length; i++) {
            if (array[ch[i]-97] == 1) {
                return i;
            }
        }
        return -1;
    }
    //e08 出现次数最多的单词
    //将paragraph截取为单词 将单词加入map集合 本身作为key，出现次数作为value，在map中找到value最大值并且返回他的key
    public String mostCommonWord(String paragraph,String[]banned){
        String[] split = paragraph.toLowerCase().split("[^A-Za-z]+");
        Set<String> set = Set.of(banned);
        HashMap<String,Integer>map=new HashMap<>();
        for (String key : split) {
            if (!set.contains(key)) {
                map.compute(key,(k,v)->v==null?1:v+1);
            }
        }
        Optional<Map.Entry<String, Integer>> optional = map.entrySet().stream().max(Map.Entry.comparingByKey());
        return optional.map(Map.Entry::getKey).orElse(null);
    }
    //e08 改良版
    public String mostCommonWord2(String paragraph, String bannned){
        Set<String>set=Set.of(bannned);
        HashMap<String,Integer>map=new HashMap<>();
        char[] chars = paragraph.toLowerCase().toCharArray();
        StringBuilder sb=new StringBuilder();
        for (char ch : chars) {
            if (ch >= 'a' && ch <= 'z') {
                sb.append(ch);
            }else {
                String key=sb.toString();
                if (!set.contains(key)) {
                    map.compute(key,(k,v)->v==null?1:v+1);
                }
                sb.setLength(0);
            }
        }
        if (!sb.isEmpty()) {
            String key = sb.toString();
            if (!set.contains(key)) {
                map.compute(key, (k, v) -> v == null ? 1 : v + 1);
            }
        }
        int max=0;
        String maxKey=null;
        for(Map.Entry<String, Integer>e: map.entrySet()){
            Integer value=e.getValue();
            if (value > max) {
                max=value;
                maxKey=e.getKey();
            }
        }
        return maxKey;
    }
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Object obj = new Object();
            System.out.println(obj.hashCode());

        }
        //字符串 自制生成hashcode
        // 原则：相同字符串生成相同hash code 尽量让不同的字符串生成不同的hash code
        String s1 = "abc";
        int hash = 0;
        for (int i = 0; i < s1.length(); i++) {
            char c = s1.charAt(i);
            System.out.println((int) c);
            hash = (hash << 5) - hash + c;//—> hash*31+c 这里的*31就是系统底层hashCode生成string hash值的权重
        }
        System.out.println(hash);
        String s = "abba";
        System.out.println(lengthOfLongestSubstring(s));
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> lists = groupAnagrams2(strs);
        System.out.println(lists);
    }


}