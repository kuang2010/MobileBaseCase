package com.kzy.mobilesafe;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * author: kuangzeyu2019
 * date: 2020/3/15
 * time: 21:19
 * desc:
 */
public class MyTest {

    @Test
    public void test() {
        B b = new B();//int还没初始化时的默认值是0
        //先执行初始化父类的成员变量，再执行父类的构造函数，再执行初始化子类的成员变量，最后执行子类的构造函数
        //所以结果打印 0  6，0是因为父类的方法会被子类覆盖，执行父类构造函数时子类的成员变量a还没被初始化就为0了!
    }

    public class A {
        int a = 3;

        public A() {
            System.out.println("A()" + (8 >> 2));
            print();
        }

        public void print() {
            System.out.println(a);
        }
    }

    public class B extends A {
        int a = 6;

        public B() {
            System.out.println("B()");
            print();
        }

        public void print() {
            System.out.println(a);
        }
    }


    @Test
    public void test2(){
        double d = 1>>2;
        System.out.println(""+d);
    }

    @Test
    public void test3(){
        //ASCII码从小到大排序
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("3","1");
        hashMap.put("1","1");
        hashMap.put("g","1");
        hashMap.put("2","1");
        hashMap.put("e","1");
        hashMap.put("f","1");
        hashMap.put("d","1");
        Set<Map.Entry<String, String>> entries = hashMap.entrySet();
        for (Map.Entry<String, String> entry:entries){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }

        System.out.println("sort==========================================");
        SortedMap<String,String> sortedMap = new TreeMap<>();
        sortedMap.putAll(hashMap);
        Set<Map.Entry<String, String>> entries1 = sortedMap.entrySet();
        for (Map.Entry<String, String> entry:entries1){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        System.out.println("第一个元素的key:" + sortedMap.firstKey()) ;
        System.out.println("key对于的值为:" + sortedMap.get(sortedMap.firstKey())) ;
        System.out.println("最后一个元素的key:" + sortedMap.lastKey()) ;
        System.out.println("key对于的值为:" + sortedMap.get(sortedMap.lastKey())) ;
        System.out.println("返回小于指定范围的集合（键值小于“d”）") ;
        for(Map.Entry<String,String> me:sortedMap.headMap("d").entrySet()){
            System.out.println("\t|- " + me.getKey() + "-->" + me.getValue()) ;
        }
        System.out.println("返回大于指定范围的集合（键值大于等于“d”）") ;
        for(Map.Entry<String,String> me:sortedMap.tailMap("d").entrySet()){
            System.out.println("\t|- " + me.getKey() + "-->" + me.getValue()) ;
        }
        System.out.println("返回部分集合（键值“2”和“d”之间,包括2不包括d）") ;
        for(Map.Entry<String,String> me:sortedMap.subMap("2","d").entrySet()){
            System.out.println("\t|- " + me.getKey() + "-->" + me.getValue()) ;
        }
    }
}
