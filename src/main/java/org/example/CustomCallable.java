package org.example;

import java.util.concurrent.Callable;

public class CustomCallable implements Callable {
    @Override
    public Object call() throws Exception {
        return "I am custom Callable";
    }

}
