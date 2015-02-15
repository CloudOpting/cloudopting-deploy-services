package eu.cloud.cloudopting.repository;

import eu.cloud.cloudopting.domain.Applications;
import eu.cloud.cloudopting.domain.PersistentAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.tn.events.api.repository.GenericRepository;

/**
 * @author Daniel P.
 */
public interface ApplicationsRepository extends GenericRepository<Applications, Long> {
}
