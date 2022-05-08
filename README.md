
# Teletrac Test [![test](https://github.com/HoudaOula/teletrac-test/actions/workflows/main.yml/badge.svg)](https://github.com/HoudaOula/teletrac-test/actions/workflows/main.yml)

## Introduction
### What?  
Teletrac Test is a project that provides the following:  

- Accept a JSON payload via HTTP POST
- Mandatory fields validation
- User authentication (Role based)
- Token based authorization
- Date stored in MySQL database 
- Requests auditing 
- Continous Integration


### How? 
When sending a POST request with a Payload as below:

```json
{
    "id": 1, 
    "RecordType": "xxx",
    "DeviceId": "357370040159770",
    "EventDateTime": "2014-05-12T05:09:48",
    "FieldA": 68,
    "FieldB": "cc",
    "FieldC": 123.45
}
```
The service will act as below:
- URL: http://localhost:8080/api/payloads/nocontent -> Returns HTTP 204
- URL: http://localhost:8080/api/payloads/echo -> Returns HTTP 200 and original payload
- URL: http://localhost:8080/api/payloads/device -> Returns HTTP 200 and only 'DeviceId' field
- URL: http://localhost:8080/api/payloads -> Returns HTTP 400
- URL: http://localhost:8080/api/payloads -> Returns HTTP 200 and all payloads
- URL: http://localhost:8080/actuator/httptrace -> Returns HTTP 200 and the requests details
    
## Technology choice  
### Programming languages  
- Java  
### Frameworks  
- Spring  
### Build and CI  
- Maven
- Github Actions [![test](https://github.com/HoudaOula/teletrac-test/actions/workflows/main.yml/badge.svg)](https://github.com/HoudaOula/teletrac-test/runs/6337893722?check_suite_focus=true)
    
## Build and Usage  
First, you need to clone the project:  

```
git clone "https://github.com/HoudaOula/teletrac-test"  
```  

**teletrac-test** is a java project that is built with **Maven**, so you need to build it via the command below:  
```
mvn clean install
```  

To consume this service, you need to configure MySQL database and change the database configuration in the file **database-config.properties** under resources. 
\
\
For tests, a different profile **'test'** and an **H2** database are used. 
\
\
To audit requests, you can access the url: http://localhost:8080/actuator/httptrace

## Contribution
Feel free to contribute, or add issues / feature requests.  
