package com.petrovma92.tests;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Created by root on 23.06.17.
 */
public class RunAgainRule implements TestRule {

    @Override
    public Statement apply(Statement statement, Description description) {
        return new RunAgainStatement(statement, description);
    }

    public class RunAgainStatement extends Statement {
        private Statement statement;
        private Description description;

        public RunAgainStatement(Statement statement, Description description) {
            this.statement = statement;
            this.description = description;
        }

        @Override
        public void evaluate() throws Throwable {
            Unstable unstable = description.getAnnotation(Unstable.class);

            if (unstable != null) {
                int times = unstable.value();
                int iter = times;
                while (iter > 1) {
                    try {
                        statement.evaluate();
                        break;
                    } catch (Throwable t) {
                        System.err.println("Try №"+(1+times-iter)+" failed");
                        t.printStackTrace();
                        iter--;
                    }
                }
                if(iter < 2) {
                    System.err.println("\n\nLast try!");
                    statement.evaluate();
                }
            }
            else statement.evaluate();
        }
    }
}