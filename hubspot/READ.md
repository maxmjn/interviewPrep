## Dependencies
* Gson - for json de-serialization and serialization
* Apache Common - utilities for String evaluation
* Junit Jupiter - for Junit
* Maven Shade plugin - build, package executable jar
* Jacoco - for code coverage

## Overview of program flow
```
HttpHandler class
    - to make GET and POST calls
Processor
    - retrieve GET response
    - build POST request
    - return POST response
Application(main)
    - scan user input for GET and POST endpoints
    - invoke Processor
    - display response

```

## Build
Java8 required<br>
Maven build tool is required<br>

- After un-zip source code, run below command from folder that contains "pom.xml"
```
 mvn clean install
```
* This should create "target" folder with "Hubspot.jar"

## Run
Navigate into above "target" folder<br>
```
java -jar Hubspot.jar
```

## Output

```
$ java -jar Hubspot.jar
GET endpoint:
https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=6812db25b5283edd31fe7683baf6
POST endpoint:
https://candidate.hubteam.com/candidateTest/v3/problem/result?userKey=6812db25b5283edd31fe7683baf6
{"message":"Results match! Congratulations!"}
$
```