package com.headstorm;

import com.headstorm.domain.Attribute;
import com.headstorm.domain.ObjectInstance;
import com.headstorm.domain.Template;
import com.headstorm.service.ObjectInstanceService;
import com.headstorm.service.ObjectRegistry;
import org.python.util.PythonInterpreter;

import java.io.File;

public class Application {

    public static void main(String[] args) {

        ObjectRegistry<Attribute> service = new ObjectRegistry<Attribute>(Attribute.class);
        ObjectRegistry<Template> templates = new ObjectRegistry<Template>(Template.class);
        ObjectRegistry<ObjectInstance> objects = new ObjectRegistry<ObjectInstance>(ObjectInstance.class);
        ObjectInstanceService ois = new ObjectInstanceService();

        service.deleteAll();
        templates.deleteAll();
        objects.deleteAll();
        ois.deleteAll();


        try (PythonInterpreter pythonInterpreter = new PythonInterpreter()) {
            final File folder = new File("python");
            for (final File f : folder.listFiles()){
                if (!f.getName().contains(".py")) {
                    continue;
                }
                pythonInterpreter.execfile(f.getAbsolutePath());
            }
        }
    }
}
