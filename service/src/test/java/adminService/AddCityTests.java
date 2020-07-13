package adminService;

import extensions.LoggerExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.apache.log4j.Logger;
import wisniewski.jan.persistence.model.City;
import wisniewski.jan.persistence.repository.CityRepository;
import wisniewski.jan.service.service.AdminService;
import wisniewski.jan.service.dto.CreateCityDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoggerExtension.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddCityTests {

    private Logger logger;
    private String exceptionMessage;

    @Mock
    CityRepository cityRepository;

    @InjectMocks
    AdminService adminService;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Test
    @DisplayName("When city is correct adding is successful")
    public void test1() {
        var cityDto = CreateCityDto
                .builder()
                .name("Warszawa")
                .build();

        var cityAdded = City
                .builder()
                .id(1)
                .name("Warszawa")
                .build();

        var cityToAdd = City
                .builder()
                .name("Warszawa")
                .build();

        Mockito
                .when(cityRepository.add(cityToAdd))
                .thenReturn(Optional.of(cityAdded));

        Mockito
                .when(cityRepository.findLast())
                .thenReturn(Optional.of(cityAdded));

        assertEquals(adminService.addCity(cityDto), cityAdded.getId());
    }

    @Test
    @DisplayName("When city starts from lowercase exception has been thrown")
    public void test2() {
        var cityDto = CreateCityDto
                .builder()
                .name("warszawa")
                .build();
        try {
            adminService.addCity(cityDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Add city errors: City : Name should starts from uppercase", exceptionMessage);
    }

    @Test
    @DisplayName("When city already exists on db expceiton has been thrown")
    public void test3() {
        var cityDto = CreateCityDto
                .builder()
                .name("Warszawa")
                .build();

        var cityAdded = City
                .builder()
                .id(1)
                .name("Warszawa")
                .build();

        Mockito
                .when(cityRepository.findByName("Warszawa"))
                .thenReturn(Optional.of(cityAdded));

        try {
            adminService.addCity(cityDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("This city already exists in database", exceptionMessage);
    }

}
