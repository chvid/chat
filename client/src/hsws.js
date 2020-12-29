export class WsServer {
    constructor() {
        this.messageHandlers = {};
        this.openHandlers = [];
        this.closeHandlers = [];
        this.errorHandlers = [];
    }

    init() {
        this.socket = new WebSocket((window.location.protocol == "https:" ? "wss:" : "ws:") + window.location.host + "/ws");

        this.socket.onopen = () => {
            for (let h of this.openHandlers) {
                h();
            }
        };

        this.socket.onmessage = event => {
            const envelope = JSON.parse(event.data);
            if (this.messageHandlers[envelope.header]) {
                for (let h of this.messageHandlers[envelope.header]) {
                    h(envelope.body);
                }
            }
        };

        this.socket.onerror = event => {
            for (let h of this.errorHandlers) {
                h(event.message);
            }
        };

        this.socket.onclose = event => {
            for (let h of this.closeHandlers) {
                h(event.reason);
            }
        }
    }

    onOpen(handler) {
        this.openHandlers.push(handler);
    }

    onClose(handler) {
        this.closeHandlers.push(handler);
    }

    onError(handler) {
        this.errorHandlers.push(handler);
    }

    onMessage(header, handler) {
        if (this.messageHandlers[header] == null) this.messageHandlers[header] = [];
        this.messageHandlers[header].push(handler);
    }

    send(header, body) {
        this.socket.send(JSON.stringify({ header, body }));
    }
}
