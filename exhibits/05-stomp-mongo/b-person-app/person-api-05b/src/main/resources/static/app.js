const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:7075/person-api-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/db-event', (r) => {
        showReceivedData(r.body);

    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#dataReceivedArea").show();
    }
    else {
        $("#dataReceivedArea").hide();
    }
    $("#dataReceived").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendData() {
	const dataToSendObject = {
        destination: "/app/person/insert",
		body: $("#dataToSend").val()
    }    
    console.log({dataToSendObject});
    stompClient.publish(dataToSendObject);
}

function showReceivedData(message) {
    $("#dataReceived").append("<tr><td><pre>" + JSON.stringify(JSON.parse(message),null,4) + "</pre></td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#sendData" ).click(() => sendData());
});

