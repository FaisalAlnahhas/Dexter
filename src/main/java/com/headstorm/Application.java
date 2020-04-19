package com.headstorm;

import com.headstorm.domain.Attribute;
import com.headstorm.domain.AttributeType;
import com.headstorm.domain.ObjectInstance;
import com.headstorm.domain.Template;
import com.headstorm.service.ObjectInstanceService;
import com.headstorm.service.ObjectRegistry;
import org.python.util.PythonInterpreter;

import java.util.Map;

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

        Attribute host = service.createOrUpdate(Attribute.createNew("host", AttributeType.STRING));
        Attribute path = service.createOrUpdate(Attribute.createNew("path", AttributeType.STRING));
        Attribute ct = service.createOrUpdate(Attribute.createNew("contenttype", AttributeType.STRING));

        Template t = Template.createNew("Endpoint");
        t.addRequiredAttribute(host);
        t.addRequiredAttribute(path);
        t.addOptionalAttribute(ct);
        templates.createOrUpdate(t);

        System.out.println(t);
        System.out.println(templates.find(t.guid));

        for (Template x : ois.allTemplates()) {
            System.out.println(x);
            System.out.println(x.guid);
        }

//        ObjectInstance endpoint1 = ObjectInstance.createNew("api1", t, null);
//        ObjectInstance endpoint2 = ObjectInstance.createNew("api2", t, null);
//        ObjectInstance endpoint3 = ObjectInstance.createNew("api3", t, null);
//        objects.createOrUpdate(endpoint1);
//        objects.createOrUpdate(endpoint2);
//        objects.createOrUpdate(endpoint3);



        ois.createObjectInstance("api1", t.guid, Map.of("host", "http://google.com", "path", "/query"));
        ois.createObjectInstance("api2", t.guid, Map.of("host", "http://website2.com", "path", "/api/v1/search"));
        ois.createObjectInstance("api3", t.guid, Map.of("host", "http://localhost", "path", "/api/v1/new", "contenttype", "application/json"));
//        for (Attribute x : ois.getTemplateAttributes(t.guid)) {
//            System.out.println(x);
//        }

        try (PythonInterpreter pythonInterpreter = new PythonInterpreter()) {
            pythonInterpreter.execfile("python/driver.py");
        }
    }
}
