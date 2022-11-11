package com.gb.chatMini.stream;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamApi {
    public static void main(String[] args) {

        /**
         * Stream
         *
         *
         * ����� ������� ��������, ������� ����������� ��� ������ ��������� ������.
         * ������� �� ������������� � ������������.
         *
         * ������������� - �������� ��� ����������, �.�. ����� ���������� ����������� (��� ������������� �������� Stream)
         * ������������ - �������� ��� ����� ������� (�������������, ������������, ���������...)(��� ������������� �������� �� Stream)
         *
         * �������������: ���������� (filter), �������������� (map), ���������� (reduce), ������ ���������������� �������� (forEach)...
         *
         */

        // forEach
        Stream.of(1, 2, 3, 4, 5)
                .forEach(x -> System.out.print(x + " "));
        System.out.println("");


        // filter
        Stream.of(1, 2, 3, 4, 5, 6, 7)
                .filter((x) -> x % 2== 0)
                .forEach(System.out::print);
        System.out.println("");


        // map - �������������� ������� �������� ������
        // ��������� - ����� ������ ����
        Stream.of(1, 2, 3, 4, 5, 6, 7)
                .map(x -> x * 4)
                .filter(x -> x > 9)
                .forEach(x -> System.out.print(x + " "));
        System.out.println("");


        // reduce - ���������� ����������� ������� ������
        Optional<Integer> reduce = Stream.of(1, 2, 3, 4, 5, 6, 7)
                .reduce((x, y) -> x * y);
        reduce.ifPresent(System.out::println);
        System.out.println("");

        Integer res = Stream.of(1, 2, 3, 4, 5, 6, 7)
//                .reduce(1, (x, y) -> x * y);
                .reduce(0, Integer::sum);
        System.out.println(res);
        System.out.println("");

        ArrayList<Integer> result = Stream.of(1, 2, 3, 4, 5, 6, 7)
                .reduce(new ArrayList<>(),
                        (list, item) -> {
                    list.add(item);
                    return list;
                        },
                        (l, r) -> l);
        System.out.println(result);
        System.out.println("");


        // collectors - ��� ��� reduce, ������ ��� ����������, ����� ��� ������ ������������

        List<Integer> list = Stream.of(1, 2, 3, 4, 5, 6, 7)
                .collect(Collectors.toList());
        System.out.println(list);
        System.out.println("");


        Map<String, Integer> map = Stream.of("a", "a", "aaa", "b", "b", "C")
                .map(String::toLowerCase)
                .collect(Collectors.toMap(
                        Function.identity(), // �� �� x -> x //��� keyMapper - ��� ������ � ������
                        x -> 1, // ��� valueMapper - ��� ������ �� ���������
                        Integer::sum // ��� ����������� - ��� ������, ���� ����������� ����
                ));

        System.out.println(map);
        System.out.println("");




    }
}
