package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.City;
import wisniewski.jan.persistence.repository.CityRepository;
import wisniewski.jan.service.exception.CityServiceException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public String showNameByCityId(Integer cityId) {
        return cityRepository.findById(cityId).orElseThrow(() -> new CityServiceException("Failed")).getName();
    }

    public String showCities() {
        return cityRepository.findAll()
                .stream()
                .map(city -> city.getId() + ". " + city.getName())
                .collect(Collectors.joining("\n"));
    }

    public City findCityById(Integer cityId) {
        return cityRepository.findById(cityId).orElseThrow(() -> new CityServiceException("Failed"));
    }

    public List<City> getAll() {
        return cityRepository.findAll();
    }

}
