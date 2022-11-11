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

        // Внутренний анонимный класс
        Foo foo = new Foo() {
            @Override
            public void foo() {
                System.out.println("Hello world!");
            }
        };

        System.out.println(foo.getClass()); // у анонимного класса нет класса, только порядковый номер

        Foo foo2 = (() -> {
            System.out.println("Hello world 2!");
        });                                 // - анонимка через лямбду

        Foo foo3 = () -> System.out.println("Hello world 3!"); // лямбда максимально сокращенным вариантом.

        foo.raf();
        foo.foo(); // так тоже можно

        foo2.raf();
        foo3.foo();

        Foo2 foo21 = (a, b) -> a + b;
        Foo2 foo22 = Integer::sum; // то же самое через метод-референс

        // А вот прикольная тема:
        System.out.println(apply(5, 6, foo22));
        System.out.println(apply(15, 16, Integer::sum)); // или так)))
        System.out.println(apply(15, 16, Example::multiply)); // а вот так вообще какая-то магия....


        /**
         * Далее вводим Интерфейсный процессор и много интерфейсов и смотрим как можно кидаться функциями интерфейсов
         */


        InterfaceProcessor processor = new InterfaceProcessor();
        processor.actionWith("Hello", str -> {
            System.out.println(str + " world!!!");
        });

        System.out.println(processor.convert(2, String::valueOf));



        /**
         *  Разберем встроенные функциональные интерфейсы
         */

        /**
         * Consumer - встроенный функциональный интерфейс,
         * принимает значение в качестве аргумента и ничего не возвращает.
         * Consumer интерфейс используется в случае, если необходимо
         * передать объект на вход и произвести над ним некоторые операции не возвращая результат.
         */

        Consumer<String> printer = (s) -> System.out.println(s);
        Consumer<String> printer2 = System.out::println; // - аналогично верхнему
        printer.accept("Hello");


        /**
         * Predicate<T> (предикат) - интерфейс, с помощью которого реализуется функция,
         * получающая на вход экземпляр класса T и возвращающая на выходе значение типа boolean.
         * Интерфейс содержит различные методы по умолчанию, позволяющие строить сложные условия (and, or, negate).
         * Predicate - это, как говорят, "условие", или совсем по простому - "проверяльщик"
         */

        Predicate<Integer> isOdd = (x) -> x % 2 == 0;

        System.out.println(isOdd.test(5));
        System.out.println(isOdd.test(4));


        /**
         *  Function — это более общий интерфейс, который принимает один аргумент и выдает результат.
         *  В нем применяется единый абстрактный метод, который принимает аргумент типа T и выдает результат типа R.
         *  Одним из распространенных вариантов использования этого интерфейса является метод Stream.map.
         */

        Function<String, Integer> mapper = str -> str.length();

        System.out.println(mapper.apply("Hello"));


        /**
         *  Supplier (с англ. — поставщик) — функциональный интерфейс, который не принимает никаких аргументов,
         *  но возвращает некоторый объект типа T.
         */

        Supplier<String> getter = () -> "Supplied object";
        System.out.println(getter.get());

        Supplier<Map<String, Set<Integer>>> mapGetter = HashMap::new; // - Эффективный пример

        //Или другой пример, когда Supplier создаем сами:
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
