# API testing con postman/newman y rest assured

## This folder contains

- Evidences

```bash
# evidence_Newman_run_collection_PROD_environment.mp4
# evidence_Newman_run_collection_QA_environment.mp4
# evidence_PostMan_run_collection_PROD_environment.mp4
# evidence_PostMan_run_collection_QA_environment.mp4
# evidence_run_javat.mp4
```

- postman and newman

newman-report-html

```bash
# newman-run-report-PROD-2020-05-27-02-08-21-917-0.html
# newman-run-report-QA-2020-05-27-02-11-14-384-0.html
```

postman-collection

```bash
# ProyectoSegundaMano-mare.postman_collection.json
```

postman-environment

```bash
# PROD.postman_environment.json
# QA.postman_environment.json
```

- Proyecto_REST-assured

```bash
This is the Java project
```

- TestPlan.xlsx

```bash
Here you find scope, risk, acceptance criteria, etc
```

- TestCases.xls

```bash
Here you find the test cases developed in postman, newman and java
```


## Requirement to run java project

1.- IDE IntelliJ  IDEA 2020.1.1 ( community Edition )
2.- Java JDK 11.0.7
3.- Gradle project

## To run Java project folder

1.- Download Proyecto_REST-assured folder
2.- Import this project in your IDE IntelliJ  IDEA 2020.1.1 ( community Edition )
3.- Go this path /src/test/java/suitTest you find  the CreateAnnouncementEndToEnd.java file
4.- Run this CreateAnnouncementEndToEnd.java file all test should pass

## Requirement to run Postman collection

1.- Download the "postman-collection" and postman-environment
2.- Import all environment and collection file in your Postman tool 
3.- Select any environment and run the collection test

## Requirement to run NEWMAN collection - S.O. LINUX 

You must have newman istalled and node version >=10, you could use the following commands

```bash
# npm install -g n
# npm install newman
- npm install -g newman@4
# npm ls -remote
# npm install v10.9.0
```

1.- Downloaded postman-collection and put all the file in a same folder
2.- Open your terminal
3.- Stand in folder where is all the files
4.- Now you are able to run the newman

```bash
# newman run ProyectoSegundaMano.postman_collection.json  -e PROD.postman_environment.json
# newman run ProyectoSegundaMano.postman_collection.json  -e QA.postman_environment.json
```

If you want with iterations

```bash
# newman run ProyectoSegundaMano.postman_collection.json  -e PROD.postman_environment.json -n 2
```

If you want HTML report

```bash
# newman run ProyectoSegundaMano.postman_collection.json -e PROD.postman_environment.json -r reporte.html
```

If you want run the collection with delay

```bash
# newman run ProyectoSegundaMano.postman_collection.json  -e PROD.postman_environment.json --delay-request 10
```

If you want run the collection with timeout

```bash
# newman run ProyectoSegundaMano.postman_collection.json  -e PROD.postman_environment.json --timeout-request 10
```
 


