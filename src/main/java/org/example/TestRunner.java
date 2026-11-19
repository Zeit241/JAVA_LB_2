package org.example;

import org.example.annotation.After;
import org.example.annotation.Before;
import org.example.annotation.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    public static void run(Class<?> testClass) throws Exception {
        logger.debug("Получен для запуска тестовый класс {}", testClass.getCanonicalName());

        List<Method> beforeMethods = findMethods(testClass, Before.class);
        List<Method> testMethods = findMethods(testClass, Test.class);
        List<Method> afterMethods = findMethods(testClass, After.class);

        int passed = 0;
        int failed = 0;
        int index = 1;
        for (Method testMethod : testMethods) {
            System.out.println("======================="+ "TEST #" + index+ "==========================");
            index++;
            // на каждый тест свой объект
            Object testObject = testClass.getDeclaredConstructor().newInstance();

            boolean success = true;

            // сначала все Before
            for (Method method : beforeMethods) {
                if (!callMethod(testObject, method)) {
                    success = false;
                }
            }
            // потом сам тест
            if (!callMethod(testObject, testMethod)) {
                success = false;
            }
            // потом все After, даже если тест упал
            for (Method method : afterMethods) {
                if (!callMethod(testObject, method)) {
                    success = false;
                }
            }


            if (success) {
                passed++;
            } else {
                failed++;
            }
        }

        printStatistics(passed, failed);
    }

    private static List<Method> findMethods(Class<?> testClass, Class<? extends Annotation> annotation) {
        List<Method> result = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                result.add(method);
            }
        }
        // чтобы before1 шёл раньше before2
        result.sort((first, second) -> first.getName().compareTo(second.getName()));
        return result;
    }

    private static boolean callMethod(Object testObject, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(testObject);
            return true;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            logger.error("Метод {} упал: {}", method.getName(), cause.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Не удалось вызвать метод {}: {}", method.getName(), e.getMessage());
            return false;
        }
    }

    private static void printStatistics(int passed, int failed) {
        logger.info("Прошло успешно: {}", passed);
        logger.info("Упало: {}", failed);
        logger.info("Всего: {}", passed + failed);
    }
}
