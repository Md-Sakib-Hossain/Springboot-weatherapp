package com.weather.weatherApi.model;

public class WeatherDetails {
    private Hourly hourly;

    public Hourly getHourly() {
        return hourly;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public static class Hourly {
        private String[] time;
        private double[] temperature_2m;
        private double[] wind_speed_10m;
        private double[] relative_humidity_2m;
        private double[] pressure_msl;
        private String[] weatherCondition;  // Add weather condition field

        // Getters and Setters
        public String[] getTime() {
            return time;
        }

        public void setTime(String[] time) {
            this.time = time;
        }

        public double[] getTemperature_2m() {
            return temperature_2m;
        }

        public void setTemperature_2m(double[] temperature_2m) {
            this.temperature_2m = temperature_2m;
        }

        public double[] getWind_speed_10m() {
            return wind_speed_10m;
        }

        public void setWind_speed_10m(double[] wind_speed_10m) {
            this.wind_speed_10m = wind_speed_10m;
        }

        public double[] getRelative_humidity_2m() {
            return relative_humidity_2m;
        }

        public void setRelative_humidity_2m(double[] relative_humidity_2m) {
            this.relative_humidity_2m = relative_humidity_2m;
        }

        public double[] getPressure_msl() {
            return pressure_msl;
        }

        public void setPressure_msl(double[] pressure_msl) {
            this.pressure_msl = pressure_msl;
        }

        public String[] getWeatherCondition() {
            return weatherCondition;
        }

        public void setWeatherCondition(String[] weatherCondition) {
            this.weatherCondition = weatherCondition;
        }
    }
}
