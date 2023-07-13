#!/bin/bash
java -jar scalardb-schema-loader-3.9.0.jar --config scalardb.properties --schema-file schema.json --coordinator &
exec /bin/bash -c "./gradlew run"
