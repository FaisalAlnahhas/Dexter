import os
from com.headstorm.dexter.server import JettyServer, SimpleHttpResponse
from com.headstorm.dexter.client import ClientFactory
from java.util.function import Function

server = JettyServer()
client = ClientFactory.getClientInstance();

class FunctionWrapper(Function):

    def __init__(self, func):
        self.func = func

    def apply(self, arg):
        return self.func(arg)


def print_post_body(request):
    payload = server.readRequestBody(request)
    endpoint_template = client.entitiesWithProperty("name", "Endpoint")
    print(endpoint_template)
    obj = client.createOrUpdateInstance(endpoint_template.guid, payload['name'], payload)
    return SimpleHttpResponse(obj)


def find_object(request):
    guid = request.getPathInfo()[1:]
    obj = client.findEntityByGuid(guid)
    if obj is None:
        resp =  SimpleHttpResponse(None)
        resp.statusCode = 404
        return resp
    else:
        return SimpleHttpResponse(obj)

server.addHandler("/status", "GET", FunctionWrapper(lambda x: SimpleHttpResponse("OK")))
server.addHandler("/create", "POST", FunctionWrapper(print_post_body))
server.addHandler("/api/object/*", "GET", FunctionWrapper(find_object))




