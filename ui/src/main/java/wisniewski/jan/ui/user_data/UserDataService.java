package wisniewski.jan.ui.user_data;

import wisniewski.jan.persistence.enums.SearchCriterion;
import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.repository.CinemaRepository;
import wisniewski.jan.persistence.repository.impl.CinemaRepositoryImpl;
import wisniewski.jan.ui.exceptions.MenuServiceException;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserDataService {

    private final static Scanner sc= new Scanner(System.in);

    public static Integer getInteger (String msg){
        System.out.println(msg);
        String value = sc.nextLine();
        if (!value.matches("[0-9]")){
            throw new MenuServiceException("value is not a number");
        }
        return Integer.parseInt(value);
    }

    public static SearchCriterion getSearchCriterion(String msg) {
        System.out.println(msg);
        AtomicInteger counter = new AtomicInteger();
        String sortCriterionList = Arrays
                .asList(SearchCriterion.values())
                .stream()
                .map(criterion -> counter.incrementAndGet() + ". " + criterion.toString())
                .collect(Collectors.joining("\n"));
        System.out.println(sortCriterionList);
        int decision;
        do {
            decision = getInteger("Choose correct search criterion number:");
        } while (decision < 1 || decision > SearchCriterion.values().length);
        return SearchCriterion.values()[decision - 1];
    }

    public static String getString(String msg) {
        System.out.println(msg);
        return sc.nextLine();
    }


    public void close () {
        if (sc!=null){
            sc.close();
        }
    }
}
