# Persons UI Front-End

## Introduction
To interact with the messaging API and to view the mongo db events a simple UI is created and hosted on seperate http server  

## Starting the http server 
**i.**   From the folder where this readme resides `cd ./front-end`   
**ii.**  Start the http-server `npx http-server -p 7999 --base-dir ./person/ui -d false`  
**iii.** To enable SSL/TLS use `npx http-server -p 7999 --base-dir ./person/ui -d false -S true -C "./certs/localhost.crt" -K "./certs/localhost.key"`  
**iv.** Test the server is up and running by accessing [this page](http://localhost:7999/front-end/person/ui)  
**v.**  To access the UI via the Spring Cloud Gateway [use this url](http://localhost:7070/front-end/person/ui)  


## Resources
 - [http-server documentation](https://github.com/http-party/http-server)