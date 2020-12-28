import React from "react";
import ReactDOM from "react-dom";

import { formatTime, randomName } from "./utils.js";
import { WsServer } from "./hsws.js";

export class Main extends React.Component {
    constructor() {
        super();
        this.username = randomName();
        this.state = {
            messages: []
        };
        this.wsServer = new WsServer();
        this.wsServer.on("newChatLine", m => {
            this.setState({ messages: [...this.state.messages.slice(Math.max(0, this.state.messages.length - 10)), m] });
            window.scrollTo(0, document.body.scrollHeight);
        });
        this.wsServer.on("ping", m => {
            // this.setState({ messages: [... this.state.messages.slice(Math.max(0, this.state.messages.length - 10)), "ping: " + m] });
        });
        this.wsServer.onInit(() => {
            this.wsServer.send("post", {
                username: this.username,
                date: new Date().getTime(),
                message: "Hello"
            });
        });
    }

    componentDidMount() {
        this.wsServer.init();
    }

    sendMessage() {
        this.wsServer.send("post", {
            username: this.username,
            date: new Date().getTime(),
            message: this.state.message
        });
        this.setState({ message: "" });
    }

    render() {
        return (
            <div>
                <div className="main">
                    <div className="lines">
                        {this.state.messages.map(m => (
                            <div className="line">
                                <p className="message">{m.message}</p>
                                <p className="signature">
                                    {m.username} - {formatTime(m.date)}
                                </p>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="bottom">
                    <div className="controls">
                        <textarea
                            className="textarea"
                            placeholder="Message ..."
                            value={this.state.message}
                            onChange={e => this.setState({ message: e.target.value })}
                            onKeyPress={e => {
                                if (e.key == "Enter") {
                                    e.preventDefault();
                                    this.sendMessage();
                                }
                            }}
                        ></textarea>
                    </div>
                </div>
            </div>
        );
    }
}
