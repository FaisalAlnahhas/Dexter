import os
from com.headstorm.dexter.domain import Template, Attribute, AttributeType, DataType
from com.headstorm.dexter.client import ClientFactory

client = ClientFactory.getClientInstance();


column_attributes = [Attribute.createNew("datatype", AttributeType.DATATYPE)]
column_template = Template.createNew("DataField")
column_template.addRequiredAttributes(column_attributes)
client.createOrUpdateTemplate(column_template)

col1 = client.createOrUpdateInstance(column_template.guid, "id", {'datatype': DataType.STRING})
col2 = client.createOrUpdateInstance(column_template.guid, "birthdate", {'datatype': DataType.STRING})

data_set_attributes = [Attribute.createNew("fields", AttributeType.LIST, AttributeType.RELATIONSHIP)]

data_set_template = Template.createNew("DataSet")
data_set_template.addOptionalAttributes(data_set_attributes)
client.createOrUpdateTemplate(data_set_template)

ds1 = client.createOrUpdateInstance(data_set_template.guid, "table_birthdays", {"fields": [col1.guid, col2.guid]})
client.tagEntity("PII", col2.guid)

