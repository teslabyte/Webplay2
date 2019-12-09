$(function () {
    "use strict";

    var header = $('#header');
    var content = $('#content');
    var input = $('#input');
    var status = $('#status');
    var tcbutton = $('#tcbutton');
    var authbutton = $('#authbutton');
    var identbutton = $('#identbutton');
    var userid = $('#userid');
    var usertc = $('#usertc');
    var istransfer = false;
    var myName = false;
    var author = null;
    var logged = false;
    var socket = atmosphere;
    var subSocket;
    var transport = 'websocket';

    // We are now ready to cut the request
    var request = {
        url: document.location.protocol + "//" + document.location.host + '/webplay',
        contentType: "application/json",
        logLevel: 'debug',
        transport: transport,
        trackMessageLength: true,
        enableProtocol: true,
        fallbackTransport: 'long-polling'};


    request.onOpen = function (response) {
        content.html($('<p>', { text: 'Atmosphere connected using ' + response.transport }));
        input.removeAttr('disabled').focus();
        status.text('Choose name:');
        transport = response.transport;
    };

    // For demonstration of how you can customize the fallbackTransport using the onTransportFailure function
    request.onTransportFailure = function (errorMsg, request) {
        atmosphere.util.info(errorMsg);
        if (window.EventSource) {
            request.fallbackTransport = "sse";
        }
        header.html($('<h3>', { text: 'Atmosphere Chat. Default transport is WebSocket, fallback is ' + request.fallbackTransport }));
    };

    request.onMessage = function (response) {               //activates after every message
        var message = response.responseBody;
        try {
            var json = jQuery.parseJSON(message);           //this is the response FROM the backend
            if(istransfer == true && json.message.includes("transfer")) {
                changeTransferPage(json.message);
                istransfer = false;
            }
            if(json.message.includes("farm finish")) document.getElementById("tcbutton").innerHTML = "Farming is finished";
        } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message.data);
            return;
        }


        if (!logged && myName) {
            logged = true;
            status.text(myName + ': ').css('color', 'white');
            input.removeAttr('disabled').focus();
        } else {
            input.removeAttr('disabled');

            var me = json.author == author;
            var date = typeof(json.time) == 'string' ? parseInt(json.time) : json.time;
            addMessage(json.author, json.message, me ? 'white' : 'black', new Date(date));
        }
    };

    request.onClose = function (response) {
        logged = false;
    };

    request.onError = function (response) {
        content.html($('<p>', { text: 'Sorry, but there\'s some problem with your '
            + 'socket or the server is down' }));
    };

    subSocket = socket.subscribe(request);

    function pushToSubsocket(message){
        subSocket.push(atmosphere.util.stringifyJSON(message));
    }

    input.keydown(function (e) {
        if (e.keyCode === 13) {                   // 13 is ENTER
            var msg = $(this).val();

            // First message is always the author's name
            if (author == null) {
                author = msg;
            }

            pushToSubsocket({ author: author, message: msg });
            $(this).val('');

            input.attr('disabled', 'disabled');
            if (myName === false) {
                myName = msg;
            }
        }
    });

    function addMessage(author, message, color, datetime) {
        content.append('<p><span style="color:' + color + '">' + author + '</span> @ ' + +(datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime.getHours()) + ':'
            + (datetime.getMinutes() < 10 ? '0' + datetime.getMinutes() : datetime.getMinutes())
            + ': ' + message +'</p>');
    }

    //takes ID and Code from input boxes and sends them to backend
    tcbutton.click(function() {
        userid.attr('disabled','disabled');
        usertc.attr('disabled','disabled');
        tcbutton.attr('disabled','disabled');
        var Server = setServer();
        var OsVersion = setOsVersion();
        istransfer = true;
        var transferCode = document.getElementById("userid").value +
            ' ' + document.getElementById("usertc").value;
        var FullCommand = "farm " + transferCode + " " + Server + " " + OsVersion;
        pushToSubsocket({ author: 'test', message: FullCommand});
    });

    authbutton.click(function() {
        setServer();
        var auth = document.getElementById("auth").value;
        pushToSubsocket({ author: 'test', message: 'auth ' + auth});
    });

    identbutton.click(function() {
        setServer();
        var ident = document.getElementById("ident").value;
        pushToSubsocket({ author: 'test', message: 'identifier ' + ident});
    });


    function setServer(){
        var select = document.getElementById("serverselect");
        if(select.options[0].selected) return "glb";
        return "jp";
    }

    function setOsVersion(){
        var select = document.getElementById("osselect");
        if(select.options[0].selected) return "android";
        return "ios";
    }

    function changeTransferPage(transferCode){
        document.getElementById("tcbutton").innerHTML = "Farming...";
        document.getElementById("tctext").innerHTML = "Your new transfer code (wait for button to change to Finished to use it):";
        var words = transferCode.split(" ");
        document.getElementById("usertc").value = words[3];
    }
});
