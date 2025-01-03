package er.rennala.advice.domain.ref;

import java.util.List;

/**
 * RefAnalyzer, used to find the referenced objects by id(ids) or any fields.
 * This is an example.
 * class UserPO(Long id, String name, String password, String avatar): The entity of db table(user).
 * class UserShadow(Long id, String name, String avatar): The shadow if user, you can call it UserDTO.
 * You can implement RefAnalyzer like this:
 * UserRepositoryImpl implements RefAnalyzer<UserShadow, UserPO, Long> {}
 *
 * @param <X> You can convert Y to X.
 * @param <Y> The Type of querying return.
 * @param <V> The type of querying field.
 */
public interface RefAnalyzer<X, Y, V> {

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
