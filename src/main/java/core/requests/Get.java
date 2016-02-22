package core.requests;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dasein.cloud.utils.requester.*;
import org.dasein.cloud.utils.requester.streamprocessors.StreamProcessor;

/**
 * Created by vmunthiu on 2/22/2016.
 */
@Singleton
public class Get {
    private static HttpClientBuilder httpClientBuilder;
    private static StreamProcessor streamProcessor;
    private static RequestBuilder requestBuilder;

    @Inject
    private Get(HttpClientBuilder httpClientBuilder, StreamProcessor streamProcessor, RequestBuilder requestBuilder) throws DaseinRequestException {
        if(!requestBuilder.getMethod().equalsIgnoreCase(HttpGet.METHOD_NAME))
            throw new DaseinRequestException("Wrong http method initialziation", null);

        this.httpClientBuilder = httpClientBuilder;
        this.streamProcessor = streamProcessor;
        this.requestBuilder = requestBuilder;
    }

    public static <T> T get(String uri, Class<T> classType) throws DaseinRequestException {
        requestBuilder.setUri(uri);

        return new DaseinRequestExecutor<T>(httpClientBuilder, requestBuilder.build(),
                new DaseinResponseHandler<T>(streamProcessor, classType)).execute();
    }

    public static <T, V> V get(String uri, ObjectMapper<T, V> mapper, Class<T> classType) throws DaseinRequestException {
        requestBuilder.setUri(uri);

        return new DaseinRequestExecutor<V>(httpClientBuilder, requestBuilder.build(),
                new DaseinResponseHandlerWithMapper<T, V>(streamProcessor, mapper, classType)).execute();
    }
}
