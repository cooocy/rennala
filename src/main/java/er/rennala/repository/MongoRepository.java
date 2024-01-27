package er.rennala.repository;

public interface MongoRepository<T extends AggregateRoot, ID> extends org.springframework.data.mongodb.repository.MongoRepository<T, ID> {

}
