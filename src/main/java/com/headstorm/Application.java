package com.headstorm;

import org.python.util.PythonInterpreter;

public class Application {

    public static void main(String[] args) {
        try (PythonInterpreter pythonInterpreter = new PythonInterpreter()) {
            pythonInterpreter.execfile("python/driver.py");
        }
    }
}
