package dk.brightworks.hbws;

public class Envelope<T> {
    private String header;
    private T body;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String toString() {
        return "header: " + header + ", body: " + body;
    }
}
