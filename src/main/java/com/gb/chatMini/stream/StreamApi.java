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
         * Стрим конвеер операций, которые выполняются над каждым элементом потока.
         * Делятся на промежуточные и терминальные.
         *
         * Промежуточные - действия над элементами, т.е. стрим продолжает существоать (тип возвращаемого значения Stream)
         * Терминальные - действия над самим стримом (преобразовать, использовать, отправить...)(тип возвращаемого значения НЕ Stream)
         *
         * Промежуточные: фильтрация (filter), преобразование (map), аггрегация (reduce), прочие пользовательские действия (forEach)...
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


        // map - преобразование каджого элемента стрима
        // результат - стрим нового типа
        Stream.of(1, 2, 3, 4, 5, 6, 7)
                .map(x -> x * 4)
                .filter(x -> x > 9)
                .forEach(x -> System.out.print(x + " "));
        System.out.println("");


        // reduce - аггрегация компонентов цепочки данных
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


        // collectors - это как reduce, только уже написанные, можно ими просто пользоваться

        List<Integer> list = Stream.of(1, 2, 3, 4, 5, 6, 7)
                .collect(Collectors.toList());
        System.out.println(list);
        System.out.println("");


        Map<String, Integer> map = Stream.of("a", "a", "aaa", "b", "b", "C")
                .map(String::toLowerCase)
                .collect(Collectors.toMap(
                        Function.identity(), // то же x -> x //это keyMapper - что делать с ключем
                        x -> 1, // это valueMapper - что делать со значением
                        Integer::sum // это МержФункция - что делать, если повторяется ключ
                ));

        System.out.println(map);
        System.out.println("");




    }
}
