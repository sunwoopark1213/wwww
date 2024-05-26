package com.example.newww;

import java.util.List;

public class WeatherResponse {
    public Main main;
    public List<Weather> weather;
    public String name;

    public class Main {
        public double temp;
        public double temp_min;
        public double temp_max;
    }

    public class Weather {
        public String main;
        public String description;
    }
}
