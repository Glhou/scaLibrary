# scaLibrary
Student scalarDb project for Keio University Course

## Installation 
First install docker on your computer

To launch run ```docker compose up -d```

To stop run ```docker compose down```

## Installation Old
First install docker on your computer

To launch the databases run ```docker compose up -d```

To load the database you can do ```java -jar scalardb-schema-loader-<VERSION>.jar --config database.properties --schema-file schema.json --coordinator```

After that to launch to server you can do ````./gradlew run``` or ```gradlew.bat run```

If you want to stop the containers use ```docker compose down```
