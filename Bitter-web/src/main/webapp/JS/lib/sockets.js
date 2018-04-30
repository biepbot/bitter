var MessageSocket = function(address) {
    // no support for "http://..." links yet
    
    this.address = address;
    var socket = new WebSocket("ws://" + this.address);
    
    this.close = function() {
        socket.close();
    };
    
    this.send = function(obj) {
        socket.send(JSON.stringify(obj));
    };
    
    socket.onerror = function(e) {
        console.error('The following error has occurred while initialsing the socket;');
        console.error(e);
    };
    
    this.addEventListener = function(event, func) {
        socket.addEventListener(event,func);
    };
    
    this.removeEventListener = function(event, func) {
        socket.removeEventListener(event,func);
    };
};