package ru.sbt.proxy;

import org.junit.Before;
import org.junit.Test;
import ru.sbt.calc.CalImpl;
import ru.sbt.calc.Calculator;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CacheProxyTest {


    Calculator calculator;


    @Before
    public void setUp() throws Exception {
        CalImpl cal = new CalImpl();

        calculator = CacheProxy.cache(cal);


    }


    @Test(timeout = 5000)
    public void testAllIdentity() throws Exception {
        calculator.hardWord(1, 2);
        calculator.hardWord(1, 2);
        calculator.hardWord(1, 2);
        calculator.hardWord(1, 2);
        calculator.hardWord(1, 2);
    }


    @Test
    public void testListSize() {
        List<Integer> a = calculator.someList(1000000);
        assertEquals(a.size(), 2000);
    }

    @Test(timeout = 3500)
    public void testNotAllIdentity() {
        calculator.hardWord(1, 2, "a");
        calculator.hardWord(1, 2, "b");
        calculator.hardWord(1, 2, "c");

    }

    @Test(timeout = 6300)
    public void testAllIdentityTimeout() throws Exception {
        calculator.hardWord(1, 2);
        calculator.hardWord(1, 3);
        calculator.hardWord(1, 2);
        calculator.hardWord(1, 3);
    }
}