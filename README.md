# media-holder

Media holder service is ready for store files on a web server

## Getting Started

### Installing

* If directory /media-holder not exist You need to create it in suitable place
* Go to the directory /media-holder
* Clone the git repository:
```
git clone git@github.com:WladislavGrodno/media-holder.git
```

* Setup properties of microservice in the file of
/src/main/resources/application.properties 
* Create .jar of the project:
```
mvn clean package
```

### Running

* Run Postgres SQL-server
* Run this microservice:
```
java -jar target/media-holder-0.0.1-SNAPSHOT.jar 
```

WARNING! You can't build or run this service without database.

