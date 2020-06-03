package wisniewski.jan.persistence.connection;

import org.jdbi.v3.core.Jdbi;

public class DbConnection {
    private final String username;
    private final String password;
    private final String url;
    private final Jdbi jdbi;

    public DbConnection(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.jdbi = Jdbi.create(url, password, username);
    }

    public Jdbi getJdbi() {
        return jdbi;
    }
}
