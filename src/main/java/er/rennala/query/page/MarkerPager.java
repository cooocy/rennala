package er.rennala.query.page;

import java.util.List;

/**
 * marker page 分页查询结果.
 */
@Deprecated
public class MarkerPager<R, M> {

    public final List<R> records;

    public final M nextMarker;

    public MarkerPager(List<R> records) {
        this.records = records;
        this.nextMarker = null;
    }

    public MarkerPager(List<R> records, M nextMarker) {
        this.records = records;
        this.nextMarker = nextMarker;
    }

}
