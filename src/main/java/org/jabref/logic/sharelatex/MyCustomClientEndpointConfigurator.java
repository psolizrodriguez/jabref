package org.jabref.logic.sharelatex;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;

public class MyCustomClientEndpointConfigurator extends ClientEndpointConfig.Configurator {

    private final String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:53.0) Gecko/20100101 Firefox/53.0";
    private final String serverOrigin;

    public MyCustomClientEndpointConfigurator(String serverOrigin) {
        super();
        this.serverOrigin = serverOrigin;
    }

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {

        headers.put("User-Agent", Arrays.asList(userAgent));
        headers.put("origin", Arrays.asList(serverOrigin));
    }
}
