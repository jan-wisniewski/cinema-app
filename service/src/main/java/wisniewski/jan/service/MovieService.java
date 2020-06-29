package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.persistence.repository.MovieRepository;
import wisniewski.jan.persistence.repository.SeanceRepository;
import wisniewski.jan.service.exception.MovieServiceException;

@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final SeanceRepository seanceRepository;

    public String showTitle (Integer movieId){
        return movieRepository.findById(movieId).orElseThrow(() -> new MovieServiceException("Failed")).getTitle();
    }



}
