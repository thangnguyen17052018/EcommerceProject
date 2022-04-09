package com.nminhthang.admin.currency;

import com.nminhthang.common.entity.Currency;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CurrencyRepositoryTest {

    @Autowired
    CurrencyRepository currencyRepository;

    @Test
    public void testCreateCurrencies() {
        List<Currency> listCurrencies = Arrays.asList(
                new Currency("Euro", "€", "EUR"),
                new Currency("Vietnamese Dong", "₫", "VND"),
                new Currency("British Pound", "£", "GPB"),
                new Currency("Russian Ruble", "₽", "RUB"),
                new Currency("South Korea Won", "₩", "KRW"),
                new Currency("Chinese Yuan", "¥", "CNY"),
                new Currency("Japanese Yen", "円", "JPY"),
                new Currency("United States Dollar", "$", "USD")
        );

        Iterable<Currency> savedListCurrencies = currencyRepository.saveAll(listCurrencies);

        Assertions.assertThat(savedListCurrencies).isNotNull();
    }

    @Test
    public void testListAllCurrenciesByOrderByNameAsc() {
        List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();

        Assertions.assertThat(listCurrencies.size()).isGreaterThan(0);
        listCurrencies.forEach(System.out::println);
    }

}
