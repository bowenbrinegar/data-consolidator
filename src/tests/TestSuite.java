package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestSuite {
    public static void main(String[] args) {
        JUnitCore runner = new JUnitCore();
        runner.addListener(new ExecutionListener());
        Result result = runner.run(
            BaselineTest.class
        );

        if (result.wasSuccessful()) {
            System.out.println("All Test Passed");
        } else {
            System.out.println(result.getFailureCount() + " test(s) failed");
        }
    }
}