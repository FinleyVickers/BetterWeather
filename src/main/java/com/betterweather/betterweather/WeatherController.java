package com.betterweather.betterweather;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    // Probably don't need to define labels, but intellij gets angy if I don't
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


    // Exit button
    @FXML
    private void exitPressed() {
        System.exit(0);
    }

    // current weather (Enter button)
    @FXML
    protected void currentWeatherInCity() throws UnirestException, FileNotFoundException {
        // City is city (getting input)
        String city = cityInput.getText();
        // Music files
//        String rain = "target/classes/com/weatherfx/weatherfx/rain.mp3";
//        String fall = "target/classes/com/weatherfx/weatherfx/fall.mp3";
//        String snow = "target/classes/com/weatherfx/weatherfx/snow.mp3";
//        String forest = "target/classes/com/weatherfx/weatherfx/forest.mp3";
        // Replace the spaces in the city input with %20 for a valid request, and put into lowercase. 3
        city = city.replaceAll("\\s+", "%20");
        city = city.toLowerCase(Locale.ROOT);
        // Send request to open weather
        HttpResponse<JsonNode> response = Unirest.get("https://community-open-weather-map.p.rapidapi.com/weather?q=" + city + "&lang=en&units=metric")
                .header("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .header("x-rapidapi-key", "98f907ac21msh865ff2cca16a00dp10b078jsnacf3b7435fb1")
                .asJson();
        // Json stuff
        JsonNode rootNode = response.getBody();
        JSONObject rootObj = rootNode.getObject();
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
        // Play music
        // Temporarily removed because music stops after ~1 minute. Will probably add back later

        /*if (isRain < 532) {
            Media sound = new Media(new File(rain).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } else if(isRain < 623 && isRain > 599) {
            Media sound = new Media(new File(snow).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
        } else if(isRain > 700 && isRain < 782){
            Media sound = new Media(new File(forest).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
        } else {
            Media sound = new Media(new File(fall).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
        }*/

    }
    // Five-day forecast
    @FXML
    protected void fiveDayWeatherInCity() throws UnirestException {
        // City is city (getting input)
        String city = cityInput.getText();
        // Music files
//        String rain = "target/classes/com/weatherfx/weatherfx/rain.mp3";
//        String fall = "target/classes/com/weatherfx/weatherfx/fall.mp3";
//        String snow = "target/classes/com/weatherfx/weatherfx/snow.mp3";
//        String forest = "target/classes/com/weatherfx/weatherfx/forest.mp3";
        // Replace the spaces in the city input with %20 for a valid request, and put into lowercase.
        city = city.replaceAll("\\s+", "%20");
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
    }

    // Initialize (I have no idea what this does, ide generated it for me, it breaks program if I remove it.)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}