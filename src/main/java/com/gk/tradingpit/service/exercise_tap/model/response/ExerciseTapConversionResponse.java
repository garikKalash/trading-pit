package com.gk.tradingpit.service.exercise_tap.model.response;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseTapConversionResponse {
    private Integer id;
    private List<Commission> commissions;
}
