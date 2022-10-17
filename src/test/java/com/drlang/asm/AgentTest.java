package com.drlang.asm;

public class AgentTest {
    public static void main(String[] args) throws InterruptedException {
        for (String arg : args) {
            System.out.println(arg);
        }
        System.out.println("hello main");
        UserServer userServer = new UserServer();
        userServer.sayHello("luban 大叔");
        userServer.sayHellozxzcz("zczc");
    }
}
