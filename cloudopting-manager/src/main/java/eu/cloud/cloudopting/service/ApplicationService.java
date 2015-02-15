package eu.cloud.cloudopting.service;

import eu.cloud.cloudopting.domain.Applications;
import org.springframework.stereotype.Service;
import ro.tn.events.api.service.BaseService;

import javax.transaction.Transactional;

/**
 * @author Daniel P.
 */

public interface ApplicationService extends BaseService<Applications> {
}
