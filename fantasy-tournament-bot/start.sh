#! /usr/bin/sh

mvn clean
mvn package
jar tf target/fantasy-tournament-bot-1.0-SNAPSHOT.jar | grep com/glaeriasmite/fantasy/bot/App.class