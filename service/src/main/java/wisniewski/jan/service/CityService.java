package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.repository.CityRepository;
import wisniewski.jan.service.exception.CityServiceException;

@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public String showName (Integer cityId){
        return cityRepository.findById(cityId).orElseThrow(() -> new CityServiceException("Failed")).getName();
    }

}
