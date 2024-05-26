package com.example.newww;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView cityName, temperature, minMaxTemp, weatherDescription;
    private ImageView weatherImage;
    private Button btnRecommend;
    private double tempInCelsius;  // 현재 온도를 저장할 변수 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.tvCityName);
        temperature = findViewById(R.id.tvTemperature);
        minMaxTemp = findViewById(R.id.tvMinMaxTemp);
        weatherDescription = findViewById(R.id.weatherDescription);
        weatherImage = findViewById(R.id.weatherImage);
        btnRecommend = findViewById(R.id.Recommend);  // 버튼 초기화

        WeatherService service = RetrofitClient.getRetrofitInstance().create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(36.5, 127.0, "30e6204a33ae0d6b9cfaaf173a177e7b");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    cityName.setText("도시: " + weatherResponse.name);
                    tempInCelsius = weatherResponse.main.temp - 273.15;
                    temperature.setText("온도: " + String.format("%.2f", tempInCelsius) + "°C");
                    minMaxTemp.setText("최저: " + String.format("%.2f", weatherResponse.main.temp_min - 273.15) + "°C, 최고: " + String.format("%.2f", weatherResponse.main.temp_max - 273.15) + "°C");
                    weatherDescription.setText(weatherResponse.weather.get(0).description);
                    switch (weatherResponse.weather.get(0).main.toLowerCase()) {
                        case "clouds":
                            weatherImage.setImageResource(R.drawable.cloudy);
                            break;
                        case "clear":
                            weatherImage.setImageResource(R.drawable.sunny);
                            break;
                        case "rain":
                            weatherImage.setImageResource(R.drawable.rainy);
                            break;
                        default:
                            weatherImage.setImageResource(R.drawable.ic_launcher_background);
                            break;
                    }
                } else {
                    Log.e(TAG, "응답 실패: " + response.message());
                    cityName.setText("데이터를 불러오는데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e(TAG, "요청 실패: ", t);
                cityName.setText("데이터를 불러오는데 실패했습니다.");
            }
        });

        // 버튼 클릭 리스너 추가
        btnRecommend.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
            intent.putExtra("CURRENT_TEMP", (float) tempInCelsius);
            startActivity(intent);
        });
    }
}
