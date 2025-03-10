package com.eazybytes.common.config;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[]{
                "com.eazybytes.**"
        });
        return xStream;
    }
}
