package com.headstorm;

import com.headstorm.domain.Attribute;
import com.headstorm.domain.AttributeValue;
import com.headstorm.domain.ObjectInstance;
import com.headstorm.domain.Template;
import com.headstorm.neo4j.Neo4jSessionFactory;
import com.headstorm.server.JettyServer;
import com.headstorm.service.ObjectInstanceService;
import com.headstorm.service.ObjectRegistry;
import org.python.util.PythonInterpreter;

import java.io.File;

public class Application {

    public static void main(String[] args) {


        Neo4jSessionFactory.configureBasicAuthSession(System.getenv("NEO4J_URI"),
                System.getenv("NEO4J_USER"),
                System.getenv("NEO4J_PW"));


        ObjectRegistry<Attribute> service = new ObjectRegistry<Attribute>(Attribute.class);
        ObjectRegistry<Template> templates = new ObjectRegistry<Template>(Template.class);
        ObjectRegistry<ObjectInstance> objects = new ObjectRegistry<ObjectInstance>(ObjectInstance.class);
        ObjectRegistry<AttributeValue> values = new ObjectRegistry<AttributeValue>(AttributeValue.class);
        ObjectInstanceService ois = new ObjectInstanceService();

        values.deleteAll();
        service.deleteAll();
        templates.deleteAll();
        objects.deleteAll();
        ois.deleteAll();


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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
