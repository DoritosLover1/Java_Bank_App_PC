package main.java.com.bankapp.APIservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.json.*;

public final class ExchangeService {

    private static final String API_URL = "https://api.collectapi.com/economy/allCurrency";
    private static final String API_KEY = "apikey 1SrwQTqVmSe9mfoNbu7J8I:3l2nZHEVYDCCj6eg7fpHg1";

    public static List<Double> getUSDandEURInformations() {
        List<Double> datas = new ArrayList<>();

        try {
            URI url = new URI(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("authorization", API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonReader jsonReader = Json.createReader(new java.io.StringReader(response.toString()));
            JsonObject json = jsonReader.readObject();
            JsonArray results = json.getJsonArray("result");

            for (JsonObject obj : results.getValuesAs(JsonObject.class)) {
                String name = obj.getString("name");
                if (name.contains("USD") || name.contains("EUR")) {
                	double selling = obj.getJsonNumber("selling").doubleValue();
                    datas.add(selling);
                }
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return datas;
    }
}
