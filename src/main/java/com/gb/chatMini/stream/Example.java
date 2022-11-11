package com.gb.chatMini.stream;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Example {
    public static void main(String[] args) {

        // ���������� ��������� �����
        Foo foo = new Foo() {
            @Override
            public void foo() {
                System.out.println("Hello world!");
            }
        };

        System.out.println(foo.getClass()); // � ���������� ������ ��� ������, ������ ���������� �����

        Foo foo2 = (() -> {
            System.out.println("Hello world 2!");
        });                                 // - �������� ����� ������

        Foo foo3 = () -> System.out.println("Hello world 3!"); // ������ ����������� ����������� ���������.

        foo.raf();
        foo.foo(); // ��� ���� �����

        foo2.raf();
        foo3.foo();

        Foo2 foo21 = (a, b) -> a + b;
        Foo2 foo22 = Integer::sum; // �� �� ����� ����� �����-��������

        // � ��� ���������� ����:
        System.out.println(apply(5, 6, foo22));
        System.out.println(apply(15, 16, Integer::sum)); // ��� ���)))
        System.out.println(apply(15, 16, Example::multiply)); // � ��� ��� ������ �����-�� �����....


        /**
         * ����� ������ ������������ ��������� � ����� ����������� � ������� ��� ����� �������� ��������� �����������
         */


        InterfaceProcessor processor = new InterfaceProcessor();
        processor.actionWith("Hello", str -> {
            System.out.println(str + " world!!!");
        });

        System.out.println(processor.convert(2, String::valueOf));



        /**
         *  �������� ���������� �������������� ����������
         */

        /**
         * Consumer - ���������� �������������� ���������,
         * ��������� �������� � �������� ��������� � ������ �� ����������.
         * Consumer ��������� ������������ � ������, ���� ����������
         * �������� ������ �� ���� � ���������� ��� ��� ��������� �������� �� ��������� ���������.
         */

        Consumer<String> printer = (s) -> System.out.println(s);
        Consumer<String> printer2 = System.out::println; // - ���������� ��������
        printer.accept("Hello");


        /**
         * Predicate<T> (��������) - ���������, � ������� �������� ����������� �������,
         * ���������� �� ���� ��������� ������ T � ������������ �� ������ �������� ���� boolean.
         * ��������� �������� ��������� ������ �� ���������, ����������� ������� ������� ������� (and, or, negate).
         * Predicate - ���, ��� �������, "�������", ��� ������ �� �������� - "������������"
         */

        Predicate<Integer> isOdd = (x) -> x % 2 == 0;

        System.out.println(isOdd.test(5));
        System.out.println(isOdd.test(4));


        /**
         *  Function � ��� ����� ����� ���������, ������� ��������� ���� �������� � ������ ���������.
         *  � ��� ����������� ������ ����������� �����, ������� ��������� �������� ���� T � ������ ��������� ���� R.
         *  ����� �� ���������������� ��������� ������������� ����� ���������� �������� ����� Stream.map.
         */

        Function<String, Integer> mapper = str -> str.length();

        System.out.println(mapper.apply("Hello"));


        /**
         *  Supplier (� ����. � ���������) � �������������� ���������, ������� �� ��������� ������� ����������,
         *  �� ���������� ��������� ������ ���� T.
         */

        Supplier<String> getter = () -> "Supplied object";
        System.out.println(getter.get());

        Supplier<Map<String, Set<Integer>>> mapGetter = HashMap::new; // - ����������� ������

        //��� ������ ������, ����� Supplier ������� ����:
        Map<String, Set<Integer>> map = putInto("123", 2, HashMap::new, TreeSet::new);
        System.out.println(map);







    }
    static Map<String, Set<Integer>> putInto(String key, Integer val,
                        Supplier<Map<String, Set<Integer>>> mapSupplier,
                        Supplier<Set<Integer>> setSupplier){
        Set<Integer> set = setSupplier.get();
        set.add(val);
        Map<String, Set<Integer>> map = mapSupplier.get();
        map.put(key, set);
        return map;
    }

    public static int apply (int x, int y, Foo2 foo2){
        return foo2.foo2(x, y);
    }

    public static int multiply (int x, int y){
        return x * y;
    }
}
