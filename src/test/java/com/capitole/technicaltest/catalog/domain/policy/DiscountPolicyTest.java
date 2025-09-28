package com.capitole.technicaltest.catalog.domain.policy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DiscountPolicyTest {

    @Test
    void shouldApply25ForHomeAndKitchen() {
        int result = DiscountPolicy.calculate("Home & Kitchen", "SKU0001");
        assertEquals(25, result);
    }

    @Test
    void shouldApply15ForElectronics() {
        int result = DiscountPolicy.calculate("Electronics", "SKU0002");
        assertEquals(15, result);
    }

    @Test
    void shouldApply30IfSkuEndsWith5() {
        int result = DiscountPolicy.calculate("Clothing", "SKU0005");
        assertEquals(30, result);
    }

    @Test
    void shouldChooseHighestDiscountWhenMultipleApply() {
        int result = DiscountPolicy.calculate("Electronics", "SKU0005");
        // Electronics = 15%, ends in 5 = 30%, wins 30
        assertEquals(30, result);
    }

    @Test
    void shouldReturnZeroWhenNoDiscountsApply() {
        int result = DiscountPolicy.calculate("Toys", "SKU0001");
        assertEquals(0, result);
    }
}
