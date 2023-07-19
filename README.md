# scaLibrary
Student scalarDb project for Keio University Course by Omichi Mayuko, Ishiyama Takahiro and Louédec Glenn.

## Backend
Java 17 using javalin
## Frontend
node 16.16.0 using Svelte
## Installation 
### Backend & Frontend
First install docker on your computer

To launch run ```docker compose up -d```

To stop run ```docker compose down```

### Frontend Dev
Install node 16.16.0.

Go to the web folder and enter ```npm install```

Then to run the dev frontend do ```npm run dev -- --open```

(If you used docker for the backend you might need to stop the web container)

## Installation Old
First install docker on your computer

To launch the databases run ```docker compose up -d```

To load the database you can do ```java -jar scalardb-schema-loader-<VERSION>.jar --config database.properties --schema-file schema.json --coordinator```

After that to launch to server you can do ````./gradlew run``` or ```gradlew.bat run```

If you want to stop the containers use ```docker compose down```
