#!/usr/bin/env bash

DEFAULT_JAR_NAME="runnable-template-1.0-SNAPSHOT.jar"
JAR_NAME=${DEFAULT_JAR_NAME}

if [[ -n $1 ]]
then
    JAR_NAME=$1
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd ${DIR}/..

./gradlew clean buildJar

java -jar build/libs/${JAR_NAME} rego.template.Template