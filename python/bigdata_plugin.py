from com.headstorm.domain import Template, Attribute, AttributeType, DataType
from com.headstorm.service import ObjectInstanceService

service = ObjectInstanceService()

#
# class Table:
#
#     def __init__(self, name):
#         self.name = name
#         self.fields = []
#
# class DataField:
#
#     def __init__(self, name, datatype):
#         self.name = name
#         self.datatype = datatype
#


tag_attributes = [Attribute.createNew("name", AttributeType.STRING)]
tag_template = Template.createNew("Tag")
service.createOrUpdateTemplate(tag_template)

pii_tag = service.createObjectInstance("PII", tag_template.guid, None)

column_attributes = [Attribute.createNew("datatype", AttributeType.DATATYPE)]
column_template = Template.createNew("DataField")
column_template.addRequiredAttributes(column_attributes)
service.createOrUpdateTemplate(column_template)

col1 = service.createObjectInstance("id", column_template.guid, {'datatype': DataType.STRING})
col2 = service.createObjectInstance("birthdate", column_template.guid, {'datatype': DataType.STRING})

data_set_attributes = [Attribute.createNew("fields", AttributeType.LIST, AttributeType.RELATIONSHIP)]
# for x in data_set_attributes:
#     service.createOrUpdateAttribute(x)

data_set_template = Template.createNew("DataSet")
data_set_template.addOptionalAttributes(data_set_attributes)
service.createOrUpdateTemplate(data_set_template)

ds1 = service.createObjectInstance("table_birthdays", data_set_template.guid, {"fields": [col1.guid, col2.guid]})
service.addRelationship(pii_tag.guid, col2.guid)

# service.addRelationship(ds1.guid, col1.guid)
# service.addRelationship(ds1.guid, col2.guid)

returned = service.find(ds1.guid)
print(returned)

print(returned.attributeValueList[0])
print(service.findAttributeValue(returned.attributeValueList[0].guid))


