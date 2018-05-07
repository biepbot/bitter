var MessageSocket = function(address) {
    // no support for "http://..." links yet
    
    this.address = address;
    this.socket = new WebSocket("ws://" + this.address);
    
    this.close = function() {
        this.socket.close();
    };
    
    this.send = function(obj) {
        this.socket.send(JSON.stringify(obj));
    };
    
    this.socket.onerror = function(e) {
        console.error('The following error has occurred while initialsing the socket;');
        console.error(e);
    };
    
    this.addEventListener = function(event, func) {
        this.socket.addEventListener(event,func);
    };
    
    this.removeEventListener = function(event, func) {
        this.socket.removeEventListener(event,func);
    };
};