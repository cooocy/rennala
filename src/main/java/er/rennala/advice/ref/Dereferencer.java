package er.rennala.advice.ref;

import java.util.List;

/**
 * <p> Dereferencer, used to find the referenced objects by id(ids) or any fields.
 * <p> This is an example.
 * <p> class UserPO(Long id, String name, String password, String avatar): The entity of db table(user).
 * <p> class UserShadow(Long id, String name, String avatar): The shadow of user, can be used in other object.
 * <p> You can implement Dereferencer like this:
 * <p> UserRepositoryImpl implements Dereferencer<UserShadow, UserPO, Long> {}
 *
 * @param <X> You can convert Y to X.
 * @param <Y> The Type of querying return.
 * @param <V> The type of querying field.
 */
public interface Dereferencer<X, Y, V> {

    /**
     * Find one.
     *
     * @param fieldValue find one by this value.
     * @return [nullable]
     */
    Y findOne(V fieldValue);

    /**
     * Find many.
     *
     * @param fieldValues find many by these values.
     * @return [nonnull] maybe empty list.
     */
    List<Y> findMany(List<V> fieldValues);

    /**
     * If someone can not be found, use this method to get a default value.
     * And you can also return null, but you should handle the null value in your code.
     *
     * @param fieldValue find one by this value.
     * @return [nullable] null means don't fallback. Let null happens.
     */
    Y fallback(V fieldValue);

    /**
     * Convert Y to X.
     *
     * @param y the object found.
     * @return [nullable] If y is null, return null or return a default value.
     */
    X convert(Y y);

}
