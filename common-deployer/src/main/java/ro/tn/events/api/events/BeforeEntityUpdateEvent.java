package ro.tn.events.api.events;

import com.google.common.base.Preconditions;
import org.springframework.context.ApplicationEvent;
import ro.tn.events.api.entity.BaseEntity;

/**
 * @param <T> tipul entitatii
 * @author Daniel P.
 */
public final class BeforeEntityUpdateEvent<T extends BaseEntity> extends ApplicationEvent {

    private final Class<T> clazz;
    private final T entity;

    public BeforeEntityUpdateEvent(final Object sourceToSet, final Class<T> clazzToSet, final T entityToSet) {
        super(sourceToSet);

        Preconditions.checkNotNull(clazzToSet);
        clazz = clazzToSet;

        Preconditions.checkNotNull(entityToSet);
        entity = entityToSet;
    }

    // API

    public Class<T> getClazz() {
        return clazz;
    }

    public T getEntity() {
        return Preconditions.checkNotNull(entity);
    }

}
