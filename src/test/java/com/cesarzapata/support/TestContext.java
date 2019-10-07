package com.cesarzapata.support;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;
import org.mockito.Mockito;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class TestContext {
    private Object payload;
    private ObjectMapper objectMapper;

    public TestContext(Object payload) {
        this.payload = payload;
        this.objectMapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        JavalinJackson.configure(objectMapper);
    }

    public Context create() {
        HttpServletRequest req = mockServletPayload();
        HttpServletResponse res = Mockito.mock(HttpServletResponse.class);
        return new Context(req, res, new HashMap<>());
    }

    private HttpServletRequest mockServletPayload() {
        HttpServletRequest result = Mockito.mock(HttpServletRequest.class);
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsString(payload).getBytes();
            InputStream is = new ByteArrayInputStream(bytes);
            when(result.getInputStream()).thenReturn(new MockServletInputStream(is));
        } catch (IOException e) {
            fail(e.getMessage());
        }

        return result;
    }

    private class MockServletInputStream extends ServletInputStream {
        private InputStream inputStream;

        public MockServletInputStream(InputStream is) {
            inputStream = is;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }
    }
}
