from com.headstorm.service import ObjectInstanceService
from com.headstorm.domain import Template, Attribute, AttributeType

service = ObjectInstanceService()

tag_attributes = [Attribute.createNew("name", AttributeType.STRING)]
tag_template = Template.createNew("Tag")
service.createOrUpdateTemplate(tag_template)

pii_tag = service.createObjectInstance("PII", tag_template.guid, None)

column_attributes = [Attribute.createNew("type", AttributeType.DATATYPE)]
column_template = Template.createNew("DataField")
service.createOrUpdateTemplate(column_template)

data_set_attributes = [Attribute.createNew("col1", AttributeType.RELATIONSHIP), Attribute.createNew("col2", AttributeType.RELATIONSHIP)]
data_set_template = Template.createNew("DataSet")
service.createOrUpdateTemplate(data_set_template)

ds1 = service.createObjectInstance("table_birthdays", data_set_template.guid, {})

col1 = service.createObjectInstance("col_id", column_template.guid, {'type': 'STRING'})
col2 = service.createObjectInstance("col_birthdate", column_template.guid, {'type': 'STRING'})

service.addRelationship(ds1.guid, col1.guid)
service.addRelationship(ds1.guid, col2.guid)
service.addRelationship(pii_tag.guid, col2.guid)


