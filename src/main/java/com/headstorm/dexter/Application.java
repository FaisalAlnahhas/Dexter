package com.headstorm.dexter;

import com.headstorm.dexter.client.ClientFactory;
import com.headstorm.dexter.client.DexterClient;
import com.headstorm.dexter.domain.*;
import com.headstorm.dexter.server.JettyServer;
import org.python.util.PythonInterpreter;

import java.io.File;

public class Application {

    public static void main(String[] args) {

        DexterClient client = ClientFactory.getClient();

        for (Entity e: client.getAll()) {
            client.delete(e.guid);
        }

        JettyServer server = new JettyServer();

        try (PythonInterpreter pythonInterpreter = new PythonInterpreter()) {
            final File folder = new File("python");
            for (final File f : folder.listFiles()){
                if (!f.getName().contains(".py")) {
                    continue;
                }
                pythonInterpreter.execfile(f.getAbsolutePath());
            }
        }
        try {
            server.start(8080);
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
