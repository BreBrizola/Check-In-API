package com.example.Check_In_API.config;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Configuration
public class RetroFitConfig {

    private static final String API_URL = "http://localhost:8090";

    @Bean
    public Retrofit retroFitCarRental(ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(API_URL)
                .build();
    }

    @Bean
    public CarRentalRetroFitClient carRentalRetroFitClient(Retrofit retrofit) {
        return retrofit.create(CarRentalRetroFitClient.class);
    }
}
