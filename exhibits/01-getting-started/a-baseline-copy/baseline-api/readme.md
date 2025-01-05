Address Book back-end application.

Uses JPA to interact with a MySQL database with a JPA Listener to listen for CRUD events.

When the JPA-CRUD events are triggered a handler re-publishes them as Persistence-Events to a Reactor-Flux where Reactive Listeners can pick them up.

There is a Web-Socket Reactive-Listener configured to pick up Persistence-Events published so that they can be displayed in a [Web-Browser](http://localhost:8080/event-listener.html)
