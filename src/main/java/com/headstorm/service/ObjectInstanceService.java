package com.headstorm.service;

import com.headstorm.domain.Attribute;
import com.headstorm.domain.AttributeValue;
import com.headstorm.domain.ObjectInstance;
import com.headstorm.domain.Template;

import java.util.Map;

public class ObjectInstanceService extends ObjectRegistry<ObjectInstance> {

    private final ObjectRegistry<Template> templates = new ObjectRegistry<>(Template.class);
    private final ObjectRegistry<Attribute> attributes = new ObjectRegistry<>(Attribute.class);

    public ObjectInstanceService() {
        super(ObjectInstance.class);
    }

    public Iterable<Template> allTemplates() {
        return templates.findAll();
    }

    public Attribute createOrUpdateAttribute(Attribute attribute) {
        return attributes.createOrUpdate(attribute);
    }

    public Template createOrUpdateTemplate(Template template) {
        return templates.createOrUpdate(template);
    }

    public ObjectInstance createObjectInstance(String name, String templateGuid, Map<String, Object> attributeValues) {
        Template t =  templates.find(templateGuid);
        ObjectInstance i = ObjectInstance.createNew(name, t, attributeValues);
        createOrUpdate(i);
        if (attributeValues != null) {
            for (Attribute att : t.getAllAttributes()) {
                AttributeValue av = new AttributeValue(i, att, attributeValues.getOrDefault(att.name, ""));
                session.save(av);
            }
        }
        return i;
    }

    public Iterable<Template> findByName(String name) {
        return session.query(Template.class,
                "MATCH (template:Template) WHERE template.name = $name RETURN template",
                Map.of("name", name));
    }

    public Iterable<Attribute> getTemplateAttributes(String templateGuid) {
        return session.query(Attribute.class,
                "MATCH (template:Template)<--(att:Attribute) WHERE template.guid = $guid RETURN att",
                Map.of("guid", templateGuid));
    }

}
