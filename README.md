# Dexter
A data catalog tool

### Download the repo

1. `git clone` the repo
2. `cd` into the cloned folder
3. Set any variables needed for the neo4j driver
4. `./gradlew run`
This will start the application and webserver, and connect it to Neo4J

#### Embedded Driver
The embedded driver is selected by default. This will use an in-memory instance of Neo4j, and will not persist data.

#### Bolt Driver
To connect to a running instance of Neo4j, set the `NEO4J_URI` environment variable, and user/pw environment variables
```
export NEO4J_URI='bolt://localhost'
export NE4J_USER='<your username>'
export NEO4J_PW='<your pw>'
``` 


### Adding Plugins
To add a plugin, add your python file to the `python` folder. Plugins are automatically loaded and executed.