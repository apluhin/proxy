package ru.sbt.calc;


import java.util.ArrayList;
import java.util.List;

public class CalImpl implements Calculator {


    @Override
    public int hardWord(Integer a, int b) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a + b;

    }

    @Override
    public int hardWord(Integer a, int b, String c) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a + b;

    }

    @Override
    public List<Integer> someList(int a) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < a; i++) {
            list.add(i);
        }
        return list;
    }
}
