package com.kzy.mobilesafe;

import org.junit.Test;

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
}
