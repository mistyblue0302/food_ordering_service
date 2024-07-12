package com.project.food_ordering_service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class JacocoTest {

    private Jacoco jacoco = new Jacoco();

    @Test
    public void 딸기_색깔을_잘_출력하는지_테스트() {
        String actual = jacoco.select("딸기");
        String expected = "빨간색입니다.";
        assertThat(actual).isEqualTo(expected);
    }
}