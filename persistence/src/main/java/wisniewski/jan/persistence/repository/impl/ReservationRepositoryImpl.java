package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.Reservation;
import wisniewski.jan.persistence.model.ReservationWithUser;
import wisniewski.jan.persistence.repository.ReservationRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.List;

public class ReservationRepositoryImpl extends AbstractCrudRepository<Reservation, Integer> implements ReservationRepository {

    private DbConnection dbConnection;

    public ReservationRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Reservation> findBySeanceId(Integer seanceId) {
        var sql = """
                select * from reservations where seance_id = :seance_id;
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("seance_id", seanceId)
                        .mapToBean(Reservation.class)
                        .list()
                );
    }

    @Override
    public List<ReservationWithUser> findByEmail(String email) {
        var sql = """
                 select r.id as reservationId, r.seance_id,r.user_id,r.seat_id,u.email as userEmail
                 from reservations r JOIN users u on r.user_id = u.id where u.email = :email;
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("email", email)
                        .mapToBean(ReservationWithUser.class)
                        .list()
                );
    }
}
