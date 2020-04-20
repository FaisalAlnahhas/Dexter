from com.headstorm.service import ObjectInstanceService
from com.headstorm.domain import Template, Attribute, AttributeType

service = ObjectInstanceService()

endpoint_attributes = Attribute.createNew("host", AttributeType.STRING), Attribute.createNew("path", AttributeType.STRING), Attribute.createNew("fields", AttributeType.LIST)
endpoint_template = Template.createNew("Endpoint")
endpoint_template.addRequiredAttributes(endpoint_attributes)

service.createOrUpdateTemplate(endpoint_template)

a = service.createObjectInstance("api1", endpoint_template.guid, {"host": "http://my_host1", "path": "/api/farm1"})
# v = service.createObjectInstance("api2", endpoint_template.guid, {"host": "http://my_host2", "path": "/api/farm2"})
# s = service.createObjectInstance("api3", endpoint_template.guid, {"host": "http://my_host3", "path": "/api/farm3"})
