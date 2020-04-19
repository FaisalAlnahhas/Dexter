from com.headstorm.service import ObjectInstanceService

# templates = new ObjectRegistry<Template>(Template.class)
service = ObjectInstanceService()
templates = service.allTemplates()

for t in templates:
    print(t)

endpoint_template = service.findByName("Endpoint")[0]
new_endpoint = service.createObjectInstance("from_python", endpoint_template.guid, {"host": "http://my_host", "path": "/api/farm"})




# from java.lang import System
#
#
# print('Running on Java version: ' + System.getProperty('java.version'))
# print('Unix time from Java: ' + str(System.currentTimeMillis()))
#
