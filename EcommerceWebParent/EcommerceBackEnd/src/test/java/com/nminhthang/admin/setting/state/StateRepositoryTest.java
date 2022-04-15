package com.nminhthang.admin.setting.state;

import com.nminhthang.admin.setting.state.StateRepository;
import com.nminhthang.common.entity.Country;
import com.nminhthang.common.entity.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTest {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateState() {
        Integer countryId = 2;
        Country VNCountry = entityManager.find(Country.class, countryId);
        State state1 = stateRepository.save(new State("Nha Trang", VNCountry));
        State state2 = stateRepository.save(new State("Can Tho", VNCountry));
        State state3 = stateRepository.save(new State("An Giang", VNCountry));
        State state4 = stateRepository.save(new State("Kien Giang", VNCountry));

        Assertions.assertThat(state1).isNotNull();
        Assertions.assertThat(state1.getId()).isGreaterThan(0);

    }

    @Test
    public void testListStatesByCountry() {
        Integer countryId = 2;
        Country VNCountry = entityManager.find(Country.class, countryId);

        List<State> listStates = stateRepository.findByCountryOrderByNameAsc(VNCountry);

        listStates.forEach(System.out::println);

        Assertions.assertThat(listStates.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateState() {
        Integer stateId = 3;
        String stateName = "Ha Giang";

        State state = stateRepository.findById(stateId).get();
        state.setName(stateName);

        State updatedState = stateRepository.save(state);

        Assertions.assertThat(updatedState.getName()).isEqualTo(stateName);

    }

    @Test
    public void testGetState() {
        Integer stateId = 3;
        State state = stateRepository.findById(stateId).get();

        Assertions.assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testDeleteState() {
        Integer stateId = 4;
        stateRepository.deleteById(4);

        Optional<State> deletedState = stateRepository.findById(stateId);

        Assertions.assertThat(deletedState).isNotPresent();
    }

}
