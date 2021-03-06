package fr.juanwolf.mysqlbinlogreplicator.nested.requester;

import fr.juanwolf.mysqlbinlogreplicator.nested.SQLRelationship;
import org.springframework.jdbc.core.RowMapper;

/**
 * Created by juanwolf on 10/08/15.
 */
public class OneToOneRequester<T, N> extends SQLRequester {

    public OneToOneRequester() {
        super();
        super.sqlRelationship = SQLRelationship.ONE_TO_ONE;
    }

    public OneToOneRequester(String entryTableName, String exitTableName, RowMapper<T> rowMapper,
                             RowMapper<N> foreignRowMapper) {
        super(entryTableName, exitTableName, rowMapper, foreignRowMapper);
        super.sqlRelationship = SQLRelationship.ONE_TO_ONE;
    }

    @Override
    public N queryForeignEntity(String foreignKey, String primaryKey, String value) {
        return (N) jdbcTemplate.queryForObject("SELECT * FROM " + getForeignTablePath() + " "
                + "INNER JOIN " + getEntryTablePath() + " ON "
                + getEntryTablePath() + "." + foreignKey + "=" + getForeignTablePath() + "." + primaryKey + " "
                + "WHERE " + getForeignTablePath() + "." + primaryKey + "=" + value , foreignRowMapper);
    }

    @Override
    public T reverseQueryEntity(String foreignKey, String primaryKey, String value) {
        final String sql = "SELECT * FROM " + getEntryTablePath() + " "
                + "INNER JOIN " + getForeignTablePath() + " ON "
                +  getEntryTableName() + "." + foreignKey + "=" + getForeignTablePath() + "." + primaryKey + " "
                + "WHERE " + getForeignTablePath() + "." + primaryKey + "=" + value;
        T mainObject = (T) jdbcTemplate.queryForObject(sql, rowMapper);
        return mainObject;
    }
}
