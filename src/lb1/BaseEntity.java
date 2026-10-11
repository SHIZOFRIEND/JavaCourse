
package lb1;

public abstract class BaseEntity {

    private long id;

    protected BaseEntity(long id) {
        this.id = id;
    }

    public long id() {
        return id;
    }
}

