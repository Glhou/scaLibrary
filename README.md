# scaLibrary
Student scalarDb project for Keio University Course

## Backend
Java 17 using javalin
## Frontend
node 16.16.0 using Svelte
## Installation 
### Backend
First install docker on your computer

To launch run ```docker compose up -d```

To stop run ```docker compose down```

### Frontend
Install node 16.16.0.

Go to the web folder and enter ```npm install```

Then to run the dev frontend do ```npm run dev -- --open```

#### Building the Frontend
If you want to build the fontend you can do :
```bash
npm run build
npm run preview # if you want to see the result
```
## Installation Old
First install docker on your computer

To launch the databases run ```docker compose up -d```

To load the database you can do ```java -jar scalardb-schema-loader-<VERSION>.jar --config database.properties --schema-file schema.json --coordinator```

After that to launch to server you can do ````./gradlew run``` or ```gradlew.bat run```

If you want to stop the containers use ```docker compose down```
