export class WsServer {
    constructor() {
        this.handlers = {};
        this.initHandlers = [];
    }

    init() {
        this.socket = new WebSocket((window.location.protocol == "https:" ? "wss:" : "ws:") + window.location.host + "/ws");

        this.socket.onopen = () => {
            for (let h of this.initHandlers) {
                h();
            }
        };

        this.socket.onmessage = event => {
            const envelope = JSON.parse(event.data);
            if (this.handlers[envelope.header]) {
                for (let h of this.handlers[envelope.header]) {
                    h(envelope.body);
                }
            }
        };

        this.socket.onerror = event => {
            console.log("Error from server ", event.data);
        };
    }

    onInit(handler) {
        this.initHandlers.push(handler);
    }

    on(header, handler) {
        if (this.handlers[header] == null) this.handlers[header] = [];
        this.handlers[header].push(handler);
    }

    send(header, body) {
        this.socket.send(JSON.stringify({ header, body }));
    }
}
