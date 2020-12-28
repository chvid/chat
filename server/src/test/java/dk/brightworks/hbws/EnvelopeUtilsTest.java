package dk.brightworks.hbws;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EnvelopeUtilsTest {
    public static class Demo {
        String a;
        String b;
    }

    @Test
    public void hello() throws IOException {
        Demo d1 = new Demo();
        d1.a = "A";
        d1.b = "B";
        String serialized = EnvelopeUtils.serializeInEnvelope("header", d1);
        Envelope<Demo> deserialized = EnvelopeUtils.deserializeEnvelope(serialized, Demo.class);

        assertEquals("header", deserialized.getHeader());
        assertEquals("A", deserialized.getBody().a);
        assertEquals("B", deserialized.getBody().b);
    }
}
