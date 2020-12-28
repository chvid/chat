package dk.brightworks.hbws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class EnvelopeUtils {
    private static Gson gson = new GsonBuilder().setLenient().serializeSpecialFloatingPointValues().create();

    public static String serializeInEnvelope(String header, Object body) throws IOException {
        Envelope envelope = new Envelope();
        envelope.setHeader(header);
        envelope.setBody(body);
        return gson.toJson(envelope);
    }

    public static <T> Envelope<T> deserializeEnvelope(String json, Class<T> klass) throws IOException {
        Envelope<T> envelope = gson.fromJson(json, Envelope.class);
        envelope.setBody(gson.fromJson(gson.toJson(envelope.getBody()), klass));
        return envelope;
    }

    public static String findHeader(String json) {
        Envelope envelope = gson.fromJson(json, Envelope.class);
        return envelope.getHeader();
    }
}