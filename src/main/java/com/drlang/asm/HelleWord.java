package com.drlang.asm;

import java.util.function.Consumer;

public class HelleWord {
    public static void main(String[] args) {
        Consumer<String> consumer = System.out::println;
        consumer.accept("hello word");
    }
}
