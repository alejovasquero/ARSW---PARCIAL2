package edu.eci.arsw.weather.services.impl;

import edu.eci.arsw.weather.services.WeatherConnectionService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherConnectionServiceImpl implements WeatherConnectionService {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://api.openweathermap.org/data/2.5/forecast/daily"+
            "?appid=daab1b8eff078d027febd9a173c60d3b";



    public String getCityStats(String city) throws IOException {

        URL obj = new URL(GET_URL+"&q="+city);
        System.out.println(obj.toString());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return response.toString();
        } else {
            System.out.println("GET request not worked");
            return FAILED;
        }
    }

}
