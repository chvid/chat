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
        this.wsServer.onMessage("newChatLine", m => {
            this.setState({ messages: [...this.state.messages.slice(Math.max(0, this.state.messages.length - 10)), m] });
            window.scrollTo(0, document.body.scrollHeight);
        });
        this.wsServer.onMessage("ping", m => {
            // this.setState({ messages: [... this.state.messages.slice(Math.max(0, this.state.messages.length - 10)), "ping: " + m] });
        });
        this.wsServer.onOpen(() => {
            this.setState({ status: "running"});
            this.wsServer.send("post", {
                username: this.username,
                date: new Date().getTime(),
                message: "Hello"
            });
        });
        this.wsServer.onError((e) => {
            console.log("onError", e);
            this.setState({ status: "error: " +e });
        });
        this.wsServer.onClose((e) => {
            console.log("onClose", e);
            this.setState({ status: "close: " +e });
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
                {
                    this.state.status != "running" &&
                    <div className="overlay">
                        <div className="dialog">{this.state.status}</div>
                    </div>
                }
                <div className="main">
                    <div className="lines">
                        {this.state.messages.map((m, i) => (
                            <div className="line" key={i}>
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
