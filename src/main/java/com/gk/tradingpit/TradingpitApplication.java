package com.gk.tradingpit;

import com.gk.tradingpit.controller.dto.request.ClientClickDto;
import com.gk.tradingpit.controller.dto.request.ClientConversionDto;
import com.gk.tradingpit.service.RegisterService;
import com.gk.tradingpit.service.exercise_tap.ExerciseTapService;
import com.gk.tradingpit.service.exercise_tap.model.request.ExerciseTapClick;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class TradingpitApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingpitApplication.class, args);
    }

}
