package fr.juanwolf.mysqlbinlogreplicator.nested.requester;

import fr.juanwolf.mysqlbinlogreplicator.nested.SQLRelationship;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Created by juanwolf on 10/08/15.
 */
public class ManyToOneRequester<T, N> extends SQLRequester {

    public ManyToOneRequester() {
        super();
        super.sqlRelationship = SQLRelationship.MANY_TO_ONE;
    }

    public ManyToOneRequester(String entryTableName, String exitTableName, RowMapper<T> rowMapper,
                              RowMapper<N> foreignMapper) {
        super(entryTableName, exitTableName, rowMapper, foreignMapper);
        super.sqlRelationship = SQLRelationship.MANY_TO_ONE;
    }

    @Override
    public N queryForeignEntity(String foreignKey, String primaryKey, String value) {
        return  (N) jdbcTemplate.queryForObject("SELECT FROM " + exitTableName
                + "INNER JOIN " + super.entryTableName + " ON "
                + super.entryTableName + "." + foreignKey + "=" + exitTableName + "." + primaryKey
                + "WHERE " + primaryKey + "=" + value, foreignRowMapper);
    }

    @Override
    public List<T> reverseQueryEntity(String foreignKey, String primaryKey, String value) {
        return (List<T>) jdbcTemplate.queryForList("SELECT FROM " + entryTableName
                + "INNER JOIN " + super.entryTableName + " ON "
                + exitTableName + "." + foreignKey + "=" + entryTableName + "." + primaryKey
                + "WHERE " + primaryKey + "=" + value , rowMapper);
    }
}