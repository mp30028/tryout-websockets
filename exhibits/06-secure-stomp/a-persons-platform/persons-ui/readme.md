# Persons UI Front-End

## Introduction
To interact with the messaging API and to view the mongo db events a simple UI is created and hosted on seperate http server  

## Starting the http server
**i.**   From the folder where this readme resides `cd ./front-end`   
**ii.**  Start the http-server `npx http-server -p 7999 --base-dir ./person/ui -d false`  
**iii.** Test the server is up and running by accessing [this page](http://localhost:7999/front-end/person/ui)  
**iv.**  To access the UI via the Spring Cloud Gateway [use this url](http://localhost:7070/front-end/person/ui)  

### Note:
It is possible that the certificate needed by spring cloud gateway to proxy the requests to the person-api is not in the jvm keystore. In which case it would have to be imported into the keystore using the jdk keytool.  
  
`keytool -import -alias localhost2 -keystore  "C:\Program Files\Java\jdk-21.0.1\lib\security\cacerts" -file G:\wksp-reference\tryout-websockets\exhibits\06-secure-stomp\b-persons-platform-ssl\persons-ui\certs\localhost.crt`

## Resources
 - [http-server documentation](https://github.com/http-party/http-server)