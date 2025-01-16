var stompClient = null;

function createStompClient(url, eventTopic){
	const stompClient = new StompJs.Client({
	    brokerURL: url
	});

	stompClient.onConnect = (frame) => {
	    setConnected(true);
	    console.log('Connected: ' + frame);
	    stompClient.subscribe(eventTopic, (r) => {
	        showEvents(r.body);
	
	    });
	};

	stompClient.onWebSocketError = (error) => {
	    console.error('Error with websocket', error);
	};

	stompClient.onStompError = (frame) => {
	    console.error('Broker reported error: ' + frame.headers['message']);
	    console.error('Additional details: ' + frame.body);
	};
	return stompClient;
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#eventsTable").show();
    }
    else {
        $("#eventsTable").hide();
    }
    $("#event").html("");
}

function connect() {
	const brokerUrlInput = $("#brokerUrlInput").val();
	const eventsTopicInput = $("#eventsTopicInput").val();
	stompClient = createStompClient(brokerUrlInput, eventsTopicInput);
    stompClient.activate();
    setConnected(true);
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    stompClient = null;
    console.log("Disconnected");
}

function sendData() {
	const dataToSend = $("#dataToSend").val();
	const sendToTopic = $("#sendToTopic").val();
	const receiveFromTopic= $("#receiveFromTopic").val();
	
	const dataToSendObject = {
        destination: sendToTopic,
		body: dataToSend
    }    
    console.log({dataToSendObject});
    stompClient.publish(dataToSendObject);
   	stompClient.subscribe(receiveFromTopic, (r) => {
		showReceivedData(r.body);
	});
}

function showEvents(message) {
    $("#event").append("<tr><td><pre>" + JSON.stringify(JSON.parse(message),null,4) + "</pre></td></tr>");
}

function showReceivedData(message) {
    $("#data").html("<tr><td><pre>" + JSON.stringify(JSON.parse(message),null,4) + "</pre></td></tr>");
}


$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#sendData" ).click(() => sendData());
});

