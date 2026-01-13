package er.rennala.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import er.rennala.advice.ref.DereferenceEnhancedMapper;

import java.io.Serializable;
import java.util.Optional;

/**
 * <p> 持久层实体抽象用 Mapper
 *
 * @param <T> {@link AbstractPersistentObject}
 */
public interface PersistentObjectMapper<T extends AbstractPersistentObject> extends DereferenceEnhancedMapper<T> {

    default Optional<T> selectOptionalForUpdate(Serializable id) {
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.last("FOR UPDATE");
        return Optional.ofNullable(selectOne(qw));
    }

}
