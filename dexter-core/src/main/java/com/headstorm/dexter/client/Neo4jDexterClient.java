package com.headstorm.dexter.client;

import com.headstorm.dexter.domain.*;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.*;
import java.util.stream.Collectors;

class Neo4jDexterClient implements DexterClient {

    private final Session session;
    private final ObjectRegistry<Template> templates;
    private final ObjectRegistry<Attribute> attributes;
    private final ObjectRegistry<ObjectInstance> instances;
    private final ObjectRegistry<Tag> tags;

    public static Neo4jDexterClient createNew(Session session) {
        return new Neo4jDexterClient(session);
    }

    private Neo4jDexterClient(Session session) {
        this.session = session;
        templates = new ObjectRegistry<>(Template.class, session);
        attributes = new ObjectRegistry<>(Attribute.class, session);
        instances = new ObjectRegistry<>(ObjectInstance.class, session);
        tags = new ObjectRegistry<>(Tag.class, session);
    }

    @Override
    public List<Entity> getAll() {
        Iterable<Entity>  resultIterable = session.query(Entity.class,
                "MATCH (a) RETURN a", new HashMap<>());
        return iterableToList(resultIterable);
    }

    @Override
    public void delete(String guid) {
        Entity entity = findEntityByGuid(guid);
        session.delete(entity);
    }

    @Override
    public Template createOrUpdateTemplate(Template template) {
        return templates.createOrUpdate(template);
    }

    @Override
    public ObjectInstance createOrUpdateInstance(String templateGuid, String instanceName, Map<String, Object> attributeValues) {
        Template t =  templates.find(templateGuid);
        ObjectInstance i = ObjectInstance.createNew(instanceName, t, attributeValues);
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
        instances.createOrUpdate(i);
        return i;
    }


    @Override
    public List<Entity> entitiesWithProperty(String propertyName, Object value) {
        String queryTemplate = "MATCH (a) WHERE a.%s = $value RETURN a";
        Iterable<Entity>  resultIterable = session.query(Entity.class,
                String.format(queryTemplate, propertyName),
                Map.of("value", value));
        return iterableToList(resultIterable);
    }

    @Override
    public List<ObjectInstance> instancesOfType(String templateName) {
        Iterable<ObjectInstance>  resultIterable = session.query(ObjectInstance.class,
                "MATCH (a:ObjectInstance) WHERE a.type = templateName RETURN a",
                Map.of("templateName", templateName));
        return iterableToList(resultIterable);
    }

    @Override
    public Template findTemplateByGuid(String guid) {
        Iterable<Template>  resultIterable = session.query(Template.class,
                "MATCH (a:Template) WHERE a.guid = $guid RETURN a",
                Map.of("guid", guid));
        return getOrNull(resultIterable);
    }

    @Override
    public ObjectInstance findObjectInstanceByGuid(String guid) {
        Iterable<ObjectInstance>  resultIterable = session.query(ObjectInstance.class,
                "MATCH (a:ObjectInstance) WHERE a.guid = $guid RETURN a",
                Map.of("guid", guid));
        return getOrNull(resultIterable);
    }

    @Override
    public ObjectInstance updateInstanceAttributeValue(ObjectInstance objectInstance, String attributeName, Object value) {
        createOrUpdateAttributeValue(objectInstance.guid, attributeName, value);
        return findObjectInstanceByGuid(objectInstance.guid);
    }

    @Override
    public Entity findEntityByGuid(String guid) {
        Iterator<Entity> x  = session.query(Entity.class, "MATCH (a) where a.guid = $guid return a", Map.of("guid", guid)).iterator();
        List<Object> castObject = new ArrayList<>();
        if (x.hasNext()) {
            return x.next();
        } else {
            return null;
        }
    }

    @Override
    public void tagEntity(String tagName, String entityGuid) {
        Entity tagEntity = getOrNull(entitiesWithProperty("name", tagName));
        Tag tag;
        if (tagEntity == null) {
            tag = tags.createOrUpdate(Tag.createNew(tagName));
        } else {
            tag = (Tag) tagEntity;
        }

        addRelationship(tag.guid, entityGuid);
    }

    @Override
    public List<Entity> entitiesTaggedWith(String tagName) {
        return null;
    }

    private void createOrUpdateAttributeValue(String objectGuid, String attributeName, Object value) {
        if (value == null) {
            return;
        }
        ObjectInstance instance = instances.find(objectGuid);
        Attribute attribute = attributes.findByProperty("name", attributeName);
        createOrUpdateAttributeValue(instance, attribute, value);
    }

    private AttributeValue createOrUpdateAttributeValue(ObjectInstance objectInstance, Attribute attribute, Object value) {
        if (value == null) {
            // How should we notify the caller that the value is null?
            return null;
        }
        clearAttributeValue(objectInstance, attribute);
        AttributeValue newValue = AttributeValue.createNew(objectInstance, attribute, value);
        session.save(newValue);
        return newValue;
    }

    private void clearAttributeValue(ObjectInstance instance, Attribute attribute) {
        Object existingValue = getOrNull(instance.valuesForAttribute(attribute.name));
        if (existingValue != null) {
            if (existingValue instanceof List) {
                for (Object o : (List)existingValue) {
                    System.out.println(o);
                    Entity e = (Entity)o;
                    session.delete(e.guid);
                }
            } else {
                session.delete(((Entity)existingValue).guid);
            }
        }
    }

    private List<AttributeValue> createOrUpdateListAttributeValue(ObjectInstance objectInstance, Attribute attribute, List<Object> values) {
        return values.stream().map(x -> createOrUpdateAttributeValue(objectInstance, attribute, x)).collect(Collectors.toList());
    }

    private void addRelationship(String sourceGuid, String targetGuid) {
        Entity source = findEntityByGuid(sourceGuid);
        Entity target = findEntityByGuid(targetGuid);
        EntityRelationship rel = new EntityRelationship();
        rel.source = source;
        rel.target = target;
        session.save(rel);
    }

    private static <T> List<T> iterableToList(Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        List<T> outputList = new ArrayList<>();
        while (iterator.hasNext()) {
            outputList.add(iterator.next());
        }
        return outputList;
    }

    private List<Attribute> getTemplateAttributes(String templateGuid) {
        Iterable<Attribute> iterableAttributes = session.query(Attribute.class,
                "MATCH (template:Template)<--(att:Attribute) WHERE template.guid = $guid RETURN att",
                Map.of("guid", templateGuid));
        return iterableToList(iterableAttributes);
    }

    private <T> T getOrNull(List<T> objects) {
        if (objects == null) {
            return null;
        }
        if (objects.size() == 1) {
            return objects.get(0);
        } else {
            return null;
        }
    }

    private <T> T getOrNull(Iterable<T> objects) {
        Iterator<T> objIterator = objects.iterator();
        if (objIterator.hasNext()) {
            return objIterator.next();
        } else {
            return null;
        }
    }

    private static class ObjectRegistry<T extends Entity> implements Service<T> {

        private static final int DEPTH_LIST = 1;
        private static final int DEPTH_ENTITY = 1;
        private final Class<T> clazz;
        private final Session session;

        public ObjectRegistry(Class<T> clazz, Session session) {
            this.clazz = clazz;
            this.session = session;
        }

        @Override
        public Iterable<T> findAll() {
            return session.loadAll(clazz, DEPTH_LIST);
        }

        @Override
        public T find(String guid) {
            return session.load(clazz, guid, DEPTH_ENTITY);
        }

        public T findByProperty(String property, String value ) {
            String template = String.format("MATCH (a) WHERE a.%s = $value return a", property);
            Iterable<T> results = session.query(clazz, template, Map.of("value", value));
            Iterator<T> iterator = results.iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                return null;
            }
        }

        @Override
        public void delete(String guid) {
            session.delete(session.load(clazz, guid));
        }

        @Override
        public T createOrUpdate(T entity) {
            session.save(entity, DEPTH_ENTITY);
            return find(entity.guid);
        }

        public void deleteAll() {
            for (T x : findAll()) {
                delete(x.guid);
            }
        }
    }
}
