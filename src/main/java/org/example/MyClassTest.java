package org.example;

import org.example.annotation.After;
import org.example.annotation.Before;
import org.example.annotation.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClassTest {

    private static final Logger logger = LoggerFactory.getLogger(MyClassTest.class);

    @Before
    public void before1() {
        logger.debug("Выполняется метод #1 before()");
    }

    @Before
    public void before2() {
        logger.debug("Выполняется метод #2 before()");
    }

    @After
    public void after1() {
        logger.debug("Выполняется метод #1 after()");
    }

    @After
    public void after2() {
        logger.debug("Выполняется метод #2 after()");
    }

    @Test
    public void getSumTest_twoPositiveNumber() {
        logger.debug("Выполняется метод getSumTest_twoPositiveNumber()");
        MathClass mathClass = new MathClass();
        int result = mathClass.getSum(2, 3);
        if (result != 5) {
            throw new RuntimeException("Ожидалось 5, получено " + result);
        }
    }

    @Test
    public void getSumTest_twoNegativeNumber() {
        logger.debug("Выполняется метод getSumTest_twoNegativeNumber()");
        MathClass mathClass = new MathClass();
        int result = mathClass.getSum(-4, -6);
        if (result != -10) {
            throw new RuntimeException("Ожидалось -10, получено " + result);
        }
    }

    @Test
    public void getMultiplicationTest_twoPositiveNumber() {
        logger.debug("Выполняется метод getMultiplicationTest_twoPositiveNumber()");
        MathClass mathClass = new MathClass();
        int result = mathClass.getMultiplication(3, 5);
        if (result != 15) {
            throw new RuntimeException("Ожидалось 15, получено " + result);
        }
    }

    @Test
    public void getDivisionTest_byZero() {
        logger.debug("Выполняется метод getDivisionTest_byZero()");
        MathClass mathClass = new MathClass();
        mathClass.getDivision(10, 0);
    }
}
