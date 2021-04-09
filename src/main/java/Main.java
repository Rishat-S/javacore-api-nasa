import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {

    static ObjectMapper mapper = new ObjectMapper();
    public static final String URI = "https://api.nasa.gov/planetary/apod?api_key=dcAIg3dLzCvQkBwtAhmhsJOezfgpzKAUQSdrZ8XR";

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(URI);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ApiNasa apiNasa = mapper.readValue(response.getEntity().getContent(), ApiNasa.class);

            saveImageToFile(apiNasa.getHdurl());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveImageToFile(String url) {
        try (InputStream in = new URL(url).openStream()) {
            String[] pathParse = url.split("/");
            Files.copy(in, Paths.get(pathParse[pathParse.length - 1]), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
