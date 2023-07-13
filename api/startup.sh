#!/bin/bash
LOADER_URL=$(curl -s https://api.github.com/repos/scalar-labs/scalardb/releases/latest | grep /scalardb-schema-loader | cut -c 31- | cut -d '"' -f 2);
if [ ! -f scalardb-schema-loader.jar ]
then
curl -L -o scalardb-schema-loader.jar $LOADER_URL;
fi
java -jar scalardb-schema-loader.jar --config scalardb.properties --schema-file schema.json --coordinator &
exec /bin/bash -c "./gradlew run"
