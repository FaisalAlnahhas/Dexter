package com.headstorm.service;

import com.headstorm.domain.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectInstanceService extends ObjectRegistry<ObjectInstance> {

    private final ObjectRegistry<Template> templates = new ObjectRegistry<>(Template.class);
    private final ObjectRegistry<Attribute> attributes = new ObjectRegistry<>(Attribute.class);
    private final ObjectRegistry<AttributeValue> attributeValues = new ObjectRegistry<>(AttributeValue.class);

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

    public void createOrUpdateAttributeValue(String objectGuid, String attributeName, Object value) {
        if (value == null) {
            return;
        }
        ObjectInstance instance = find(objectGuid);
        Attribute attribute = attributes.findByProperty("name", attributeName);
        createOrUpdateAttributeValue(instance, attribute, value);
    }

    public AttributeValue createOrUpdateAttributeValue(ObjectInstance objectInstance, Attribute attribute, Object value) {
        if (value == null) {
            // How should we notify the caller that the value is null?
            return null;
        }

        clearAttributeValue(objectInstance, attribute);
        AttributeValue newValue = AttributeValue.createNew(objectInstance, attribute, value);
        session.save(newValue);
        return newValue;
    }

    public void clearAttributeValue(ObjectInstance instance, Attribute attribute) {
        Object existingValue = instance.attributeValueMap.getOrDefault(attribute, null);
        if (existingValue != null) {
            if (existingValue instanceof List) {
                for (Object o : (List)existingValue) {
                    Entity e = (Entity)o;
                    delete(e.guid);
                }
            } else {
                delete(((Entity)existingValue).guid);
            }
        }
    }

    public AttributeValue findAttributeValue(String guid) {
        return attributeValues.find(guid);
    }

    private List<AttributeValue> createOrUpdateListAttributeValue(ObjectInstance objectInstance, Attribute attribute, List<Object> values) {
        return values.stream().map(x -> createOrUpdateAttributeValue(objectInstance, attribute, x)).collect(Collectors.toList());
    }

    public ObjectInstance createObjectInstance(String name, String templateGuid, Map<String, Object> attributeValues) {
        Template t =  templates.find(templateGuid);
        ObjectInstance i = ObjectInstance.createNew(name, t, attributeValues);
        List<AttributeValue> values = new ArrayList<>();
        if (attributeValues != null) {
            for (String attName : attributeValues.keySet()) {
                Object value = attributeValues.getOrDefault(attName, null);
                Attribute att = attributes.findByProperty("name", attName);
                if (value instanceof List) {
                    values.addAll(createOrUpdateListAttributeValue(i, att, (List)value));
                } else {
                    values.add(createOrUpdateAttributeValue(i, att, value));
                }
            }
        }
        i.attributeValueList = values;
        createOrUpdate(i);
        return i;
    }

    public Object findEntity(String guid) {
        Iterator<Entity> x  = session.query(Entity.class, "MATCH (a) where a.guid = $guid return a", Map.of("guid", guid)).iterator();
        List<Object> castObject = new ArrayList<>();
        if (x.hasNext()) {
            return x.next();
        } else {
            return null;
        }
    }

    public void addRelationship(String sourceGuid, String targetGuid) {
        Entity source = find(sourceGuid);
        Entity target = find(targetGuid);
        EntityRelationship rel = new EntityRelationship();
        rel.source = source;
        rel.target = target;
        session.save(rel);
    }

    public Iterable<ObjectInstance> instanceOfType(String typeName) {
        return session.query(ObjectInstance.class,
                "MATCH (a:ObjectInstance) WHERE a.type = $typeName RETURN a",
                Map.of("typeName", typeName));
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
