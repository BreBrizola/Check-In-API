package com.example.Check_In_API.config;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetroFitConfig {

    private static final String API_URL = "http://localhost:8090";

    @Bean
    public Retrofit retroFitCarRental(){
        return new Retrofit.Builder()
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(API_URL)
                .build();
    }

    @Bean
    public CarRentalRetroFitClient carRentalRetroFitClient(){
        return retroFitCarRental().create(CarRentalRetroFitClient.class);
    }
}
