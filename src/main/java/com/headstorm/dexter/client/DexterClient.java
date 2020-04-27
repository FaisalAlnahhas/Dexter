package com.headstorm.dexter.client;

import com.headstorm.dexter.domain.Entity;
import com.headstorm.dexter.domain.ObjectInstance;
import com.headstorm.dexter.domain.Template;

import java.util.List;
import java.util.Map;

public interface DexterClient {

    public List<Entity> getAll();
    public void delete(String guid);

    public Template createOrUpdateTemplate(Template template);
    public ObjectInstance createOrUpdateInstance(String templateName, String instanceName, Map<String, Object> attributeValues);

    public Template findTemplateByGuid(String guid);
    public ObjectInstance findObjectInstanceByGuid(String guid);
    public ObjectInstance updateInstanceAttributeValue(ObjectInstance objectInstance, String attributeName, Object value);
    public Entity findEntityByGuid(String guid);

    public void tagEntity(String tagName, String entityGuid);

    public List<Entity> entitiesWithProperty(String propertyName, Object value);
    public List<ObjectInstance> instancesOfType(String templateName);
    public List<Entity> entitiesTaggedWith(String tagName);

}
