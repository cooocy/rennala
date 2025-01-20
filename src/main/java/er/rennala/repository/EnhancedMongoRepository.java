package er.rennala.repository;

import er.rennala.advice.domain.ref.RefAnalyst;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Enhanced Mongo Repository, add @RefAnalyst.
 */
@RefAnalyst
public interface EnhancedMongoRepository<T, ID> extends MongoRepository<T, ID> {

    @Override
    <S extends T> S insert(S entity);

    @Override
    <S extends T> List<S> insert(Iterable<S> entities);

    @Override
    <S extends T> List<S> findAll(Example<S> example);

    @Override
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    @Override
    <S extends T> List<S> saveAll(Iterable<S> entities);

    @Override
    List<T> findAll();

    @Override
    List<T> findAllById(Iterable<ID> ids);

    @Override
    <S extends T> S save(S entity);

    @Override
    Optional<T> findById(ID id);

    @Override
    boolean existsById(ID id);

    @Override
    long count();

    @Override
    void deleteById(ID id);

    @Override
    void delete(T entity);

    @Override
    void deleteAllById(Iterable<? extends ID> ids);

    @Override
    void deleteAll(Iterable<? extends T> entities);

    @Override
    void deleteAll();

    @Override
    List<T> findAll(Sort sort);

    @Override
    Page<T> findAll(Pageable pageable);

    @Override
    <S extends T> Optional<S> findOne(Example<S> example);

    @Override
    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    <S extends T> long count(Example<S> example);

    @Override
    <S extends T> boolean exists(Example<S> example);

    @Override
    <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);

}
