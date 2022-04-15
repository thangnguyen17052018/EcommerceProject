package com.nminhthang.admin.setting.country;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nminhthang.common.entity.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    @WithMockUser(username = "hau@gmail.com", password = "123456hau", roles = "ADMIN")
    public void testListCountry() throws Exception {
        String url = "/countries/list";
        MvcResult mvcResult = mockMvc.perform(get(url))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

        Assertions.assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "hau@gmail.com", password = "123456hau", roles = "ADMIN")
    public void testSaveCountry() throws Exception {
        String url = "/countries/save";
        String countryName = "Zamaica";

        Country country = new Country(countryName, "ZC");

        MvcResult mvcResult = mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Integer countryId = Integer.parseInt(jsonResponse);

        Optional<Country> savedCountry = countryRepository.findById(countryId);


        Assertions.assertThat(savedCountry).isPresent();
        System.out.println("Country ID: " + jsonResponse);

    }

    @Test
    @WithMockUser(username = "hau@gmail.com", password = "123456hau", roles = "ADMIN")
    public void testUpdateCountry() throws Exception {
        String url = "/countries/save";
        String countryName = "Armenia";
        Integer countryId = 7;
        String countryCode = "AM";
        Country country = new Country(countryId ,countryName, countryCode);

        mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().string(String.valueOf(countryId)));

        Optional<Country> savedCountry = countryRepository.findById(countryId);

        Assertions.assertThat(savedCountry).isPresent();
    }

    @Test
    @WithMockUser(username = "hau@gmail.com", password = "123456hau", roles = "ADMIN")
    public void testDeleteCountry() throws Exception {
        Integer countryId = 253;
        String url = "/countries/delete/" + countryId;

        mockMvc.perform(get(url).with(csrf())).andExpect(status().isOk());

        Optional<Country> savedCountry = countryRepository.findById(countryId);

        Assertions.assertThat(savedCountry).isNotPresent();
    }

}
