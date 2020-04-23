from com.headstorm.service import ObjectInstanceService
from com.headstorm.server import JettyServer, SimpleHttpResponse
from java.util.function import Function


server = JettyServer()
service = ObjectInstanceService()

class FunctionWrapper(Function):

    def __init__(self, func):
        self.func = func

    def apply(self, arg):
        return self.func(arg)


def print_post_body(request):
    payload = server.readRequestBody(request)
    endpoint_template = service.allTemplates()[0]
    print(endpoint_template)
    obj = service.createObjectInstance(payload['name'], endpoint_template.guid, payload)

    return SimpleHttpResponse(obj)


def find_object(request):
    guid = request.getPathInfo()[1:]
    obj = service.findEntity(guid)
    if obj is None:
        resp =  SimpleHttpResponse(None)
        resp.statusCode = 404
        return resp
    else:
        return SimpleHttpResponse(obj)

def av(request):
    guid = request.getPathInfo()[1:]
    obj = service.findAttributeValue(guid)
    print(obj)
    if obj is None:
        resp =  SimpleHttpResponse(None)
        resp.statusCode = 404
        return resp
    else:
        return SimpleHttpResponse(obj)


server.addHandler("/status", "GET", FunctionWrapper(lambda x: SimpleHttpResponse("OK")))
server.addHandler("/create", "POST", FunctionWrapper(print_post_body))
server.addHandler("/api/object/*", "GET", FunctionWrapper(find_object))
server.addHandler("/api/av/*", "GET", FunctionWrapper(av))




