package wisniewski.jan.service.repository.generic;

import com.google.common.base.CaseFormat;
import org.atteo.evo.inflector.English;
import org.jdbi.v3.core.Jdbi;
import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.enums.Genre;
import wisniewski.jan.persistence.enums.SeatState;
import wisniewski.jan.persistence.exception.AbstractCrudRepositoryException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {

    private final Jdbi jdbi;

    private final Class<T> entityType;

    private final Class<ID> entityId;

    private final String tableName;

    public AbstractCrudRepository(DbConnection dbConnection) {
        jdbi = dbConnection.getJdbi();
        entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        entityId = (Class<ID>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        tableName = English.plural(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityType.getSimpleName()));
    }


    @Override
    public Optional<T> add(T item) {
        var SQL = new StringBuilder()
                .append("insert into ")
                .append(tableName)
                .append(getColumnNamesForInsert())
                .append(" values ")
                .append(getColumnValuesForInsert(item))
                .append(";")
                .toString();

        var insertedRows = jdbi.withHandle(handle -> handle.execute(SQL));
        if (insertedRows > 0) {
            return findLast();
        }
        return Optional.empty();
    }

    private String getColumnNamesForInsert() {
        return " ( " + Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().toLowerCase().equals("id"))
                .map(field -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()))
                .collect(Collectors.joining(", ")) + " ) ";
    }

    private String getColumnValuesForInsert(T item) {
        return " ( " + Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().toLowerCase().equals("id"))
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        if (field.getType().equals(String.class) || field.getType().equals(LocalDate.class)
                                || field.getType().equals(LocalDateTime.class) || field.getType().equals(Genre.class)) {
                            return "'" + field.get(item) + "'";
                        }
                        return field.get(item).toString();
                    } catch (Exception e) {
                        throw new AbstractCrudRepositoryException(e.getMessage());
                    }
                })
                .collect(Collectors.joining(", ")) + " ) ";
    }

    @Override
    public Optional<T> update(T item) {
        try {
            Field idField = entityType.getDeclaredField("id");
            idField.setAccessible(true);
            String idToUpdate = idField.get(item).toString();
            var SQL = new StringBuilder()
                    .append("update ")
                    .append(tableName)
                    .append(" set ")
                    .append(fieldsUpdator(item)
                            .entrySet()
                            .stream()
                            .map(e -> e.getKey() + " = " + e.getValue())
                            .collect(Collectors.joining(", "))
                    )
                    .append(" where id = ")
                    .append(idToUpdate)
                    .toString();
            var updatedRows = jdbi.withHandle(handle -> handle.execute(SQL));
            if (updatedRows > 0) {
                System.out.println("updated rows: " + updatedRows);
            }
        } catch (Exception e) {
            throw new AbstractCrudRepositoryException(e.getMessage());
        }
        return Optional.of(item);
    }

    private Map<String, String> fieldsUpdator(T item) {
        Field[] declaredFields = entityType.getDeclaredFields();
        Map<String, String> update = new HashMap<>();
        Arrays.stream(declaredFields)
                .filter(f -> !f.getName().equals("id"))
                .forEach(f -> {
                    f.setAccessible(true);
                    try {
                        String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, f.getName());
                        if (f.getType().equals(String.class) ||
                                f.getType().equals(SeatState.class) ||
                                f.getType().equals(Genre.class) ||
                                f.getType().equals(LocalDateTime.class)
                        ) {
                            update.put(fieldName, "'"+f.get(item).toString()+"'");
                        } else {
                            update.put(fieldName, f.get(item).toString());
                        }
                    } catch (Exception e) {
                        throw new AbstractCrudRepositoryException(e.getMessage());
                    }
                });
        return update;
    }

    @Override
    public Optional<T> findById(ID id) {
        var SQL = new StringBuilder()
                .append("select * from ")
                .append(tableName)
                .append(" where id = ")
                .append(id)
                .append(";")
                .toString();

        return jdbi.withHandle(handle -> handle
                .createQuery(SQL)
                .mapToBean(entityType)
                .findFirst());
    }

    @Override
    public Optional<T> findLast() {
        var SQL = new StringBuilder()
                .append("select * from ")
                .append(tableName)
                .append(" order by id desc ")
                .append("limit 1;")
                .toString();

        return jdbi.withHandle(handle -> handle
                .createQuery(SQL)
                .mapToBean(entityType)
                .findFirst());
    }

    @Override
    public List<T> findAll() {
        var SQL = new StringBuilder()
                .append("select *")
                .append(" from ")
                .append(tableName)
                .append(";")
                .toString();

        return jdbi.withHandle(handle -> handle
                .createQuery(SQL)
                .mapToBean(entityType)
                .list()
        );
    }

    @Override
    public List<T> findAllById(List<ID> ids) {
        String idsToFind = ids
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        var SQL = new StringBuilder()
                .append("select * from ")
                .append(tableName)
                .append(" where id in (")
                .append(idsToFind)
                .append(");")
                .toString();

        return jdbi.withHandle(handle -> handle
                .createQuery(SQL)
                .mapToBean(entityType)
                .list());
    }

    @Override
    public Boolean deleteById(ID id) {
        var SQL = new StringBuilder()
                .append("delete from ")
                .append(tableName)
                .append(" where id = ")
                .append(id)
                .append(";")
                .toString();
        var deletedRows = jdbi.withHandle(handle -> handle
                .createUpdate(SQL)
                .execute());
        return deletedRows == 1;
    }
}
