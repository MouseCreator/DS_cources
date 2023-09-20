package edu.coursera.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        AtomicInteger a = new AtomicInteger();
        System.out.println(a.get());
    }
}
