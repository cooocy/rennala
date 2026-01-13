package er.rennala.query.page;

import er.rennala.query.Order;
import org.springframework.lang.Nullable;

/**
 * marker page 分页查询参数.
 */
@Deprecated
public class MarkerPageQueryArgs<M> {

    /**
     * [1, 501]
     */
    public int limit;

    /**
     * id offset 标记.
     * 若 order = asc, where id >= idMarker;
     * 若 order = desc, where id <= idMarker;
     * 若 order = null, 由 Repository.impl 决定默认顺序.
     *
     * @see Order
     */
    @Nullable
    public final M idMarker;

    /**
     * 若 order 为 null, 由 Repository.impl 决定默认顺序.
     */
    @Nullable
    public final Order order;

    /**
     * @param limit 不在范围内的, 会被强制赋值到 [1, 501]
     */
    public MarkerPageQueryArgs(int limit) {
        this.setLimit(limit);
        this.idMarker = null;
        this.order = null;
    }

    /**
     * @param limit    不在范围内的, 会被强制赋值到 [1, 501]
     * @param idMarker x
     */
    public MarkerPageQueryArgs(int limit, M idMarker) {
        this.setLimit(limit);
        this.idMarker = idMarker;
        this.order = null;
    }

    /**
     * @param limit 不在范围内的, 会被强制赋值到 [1, 501]
     * @param order x
     */
    public MarkerPageQueryArgs(int limit, Order order) {
        this.setLimit(limit);
        this.idMarker = null;
        this.order = order;
    }

    /**
     * @param limit    不在范围内的, 会被强制赋值到 [1, 501]
     * @param idMarker x
     * @param order    x
     */
    public MarkerPageQueryArgs(int limit, M idMarker, Order order) {
        this.setLimit(limit);
        this.idMarker = idMarker;
        this.order = order;
    }

    private void setLimit(int limit) {
        this.limit = limit;
        if (this.limit < 1) {
            this.limit = 1;
        }
        if (this.limit > 501) {
            this.limit = 501;
        }
    }

}
