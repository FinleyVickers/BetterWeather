package com.betterweather.betterweather;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class WeatherController extends MainApplication implements Initializable {
    //Define stuff
    // Probably don't need to define labels, but intellij gets angy if I don't. I wish I could collapse them.
    @FXML
    public TextField tempOut;
    public TextField weatherOut;
    public TextField cityInput;
    public Button exit;
    public Label tempLabel;
    public ImageView weatherImage;
    public Pane mainPanel;
    public Button cityEnter;
    public Label weatherLabel;
    public Label fiveDayLabel;
    public Label dayOneLabel;
    public Label dayTwoLabel;
    public Label dayThreeLabel;
    public Label dayFiveLabel;
    public Label dayFourLabel;
    public TextField dayOneOut;
    public TextField dayTwoOut;
    public TextField dayThreeOut;
    public TextField dayFourOut;
    public TextField dayFiveOut;
    public Button fiveDayButton;
    public Label weatherTitle;
    public Label alertsLabel;
    public TextField alertTitle;
    public TextArea alertDescription;
    public TextField alertDate;
    public TextField alertExpires;


    // Exit button
    @FXML
    private void exitPressed() {
        System.exit(0);
    }

    // current weather (Enter button)
    @FXML
    public void currentWeatherInCity() throws UnirestException, FileNotFoundException {
        // City is city (getting input)
        String city = cityInput.getText();
        // Replace the spaces in the city input with %20 for a valid request, and put into lowercase.
        city = city.replaceAll("\\s+", "%20").toLowerCase(Locale.ENGLISH);
        HttpResponse<JsonNode> response = Unirest.get("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=b7b61a2308e043d9b9f949af01f090fc&units=metric")
                .asJson();
        // Json stuff
        JsonNode rootNode = response.getBody();
        JSONObject rootObj = rootNode.getObject();
        // Coordinates
        JSONObject coord = rootObj.getJSONObject("coord");
        int lon = coord.getInt("lon");
        int lat = coord.getInt("lat");
        // Weather vars
        JSONArray main_weather = rootObj.getJSONArray("weather");
        JSONObject weather_option = main_weather.getJSONObject(0);
        String weather = weather_option.getString("description");
        String weatherIcon = weather_option.getString("icon");
        // Weather id
        int isRain = weather_option.getInt("id");
        // Temp vars
        JSONObject main_temp = rootObj.getJSONObject("main");
        double temp = main_temp.getDouble("temp");
        tempOut.setText(temp + "°C");
        weatherOut.setText(String.valueOf(weather));
        // Set image
        Image image = new Image(new FileInputStream("Images/" + weatherIcon + "@2x.png"));
        weatherImage.setImage(image);

        // Check for weather alerts in user inputted city
        HttpResponse<JsonNode> response2 = Unirest.get("https://weatherbit-v1-mashape.p.rapidapi.com/alerts?lat=" + lat + "&lon=" + lon)
                .header("x-rapidapi-host", "weatherbit-v1-mashape.p.rapidapi.com")
                .header("x-rapidapi-key", "98f907ac21msh865ff2cca16a00dp10b078jsnacf3b7435fb1")
                .asJson();
        JsonNode rootNode2 = response2.getBody();
        JSONObject rootObj2 = rootNode2.getObject();
        JSONArray alerts = rootObj2.getJSONArray("alerts");
        System.out.println(alerts);
        // Check if there are any alerts
        if (alerts.length() == 0) {
            // No alerts
            System.out.println("No alerts");
            alertTitle.setText("No alerts");
        } else {
            // Alerts
            System.out.println("Alerts");
            // Alerts vars
            JSONObject alert = alerts.getJSONObject(0);
            String title = alert.getString("severity");
            String description = alert.getString("description");
            String date = alert.getString("effective_local");
            String expires = alert.getString("ends_local");
            // Alerts output
            alertTitle.setText(title);
            alertDescription.setText(description);
            alertDate.setText(date);
            alertExpires.setText(expires);
        }
        // Test alerts output with fake data
        /*
        alertTitle.setText("Test Alert");
        alertDescription.setText("Test Description");
        alertDate.setText("Test Date");
        alertExpires.setText("Test Expires");
         */

    }
    // Five-day forecast
    @FXML
    protected void fiveDayWeatherInCity() throws UnirestException {
        // City is city (getting input)
        String city = cityInput.getText();
        // Replace the spaces in the city input with %20 for a valid request, and put into lowercase.
        city = city.replaceAll("\\s+", "%20");
        // WTF does local mean?
        city = city.toLowerCase(Locale.ROOT);
        HttpResponse<JsonNode> response = Unirest.get("https://community-open-weather-map.p.rapidapi.com/forecast/daily?q=" + city + "&cnt=5&units=metric&lang=en")
                .header("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .header("x-rapidapi-key", "98f907ac21msh865ff2cca16a00dp10b078jsnacf3b7435fb1")
                .asJson();
        // Json stuff
        JsonNode rootNode = response.getBody();
        JSONObject rootObj = rootNode.getObject();
        JSONArray main_week = rootObj.getJSONArray("list");
        // Day One
        JSONObject dayOne = main_week.getJSONObject(0);
        JSONObject dayOneTempObj = dayOne.getJSONObject("temp");
        Double dayOneTemp = dayOneTempObj.getDouble("day");
        dayOneOut.setText(dayOneTemp + "°C");
        // Day Two
        JSONObject dayTwo = main_week.getJSONObject(1);
        JSONObject dayTwoTempObj = dayTwo.getJSONObject("temp");
        Double dayTwoTemp = dayTwoTempObj.getDouble("day");
        dayTwoOut.setText(dayTwoTemp + "°C");
        // Day Three
        JSONObject dayThree = main_week.getJSONObject(2);
        JSONObject dayThreeTempObj = dayThree.getJSONObject("temp");
        Double dayThreeTemp = dayThreeTempObj.getDouble("day");
        dayThreeOut.setText(dayThreeTemp + "°C");
        // Day Four
        JSONObject dayFour = main_week.getJSONObject(3);
        JSONObject dayFourTempObj = dayFour.getJSONObject("temp");
        Double dayFourTemp = dayFourTempObj.getDouble("day");
        dayFourOut.setText(dayFourTemp + "°C");
        // Day Five
        JSONObject dayFive = main_week.getJSONObject(4);
        JSONObject dayFiveTempObj = dayFive.getJSONObject("temp");
        Double dayFiveTemp = dayFiveTempObj.getDouble("day");
        dayFiveOut.setText(dayFiveTemp + "°C");

        // Test output with fake data
        /*
        dayOneOut.setText("10°C");
        dayTwoOut.setText("20°C");
        dayThreeOut.setText("30°C");
        dayFourOut.setText("40°C");
        dayFiveOut.setText("50°C");
         */
    }

    // Initialize (I have no idea what this does, ide generated it for me, it breaks program if I remove it)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}