package eu.cloud.cloudopting.service.impl;

import eu.cloud.cloudopting.domain.Applications;
import eu.cloud.cloudopting.domain.Customizations;
import eu.cloud.cloudopting.repository.CustomizationRepository;
import eu.cloud.cloudopting.service.ApplicationService;
import eu.cloud.cloudopting.service.CustomizationService;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import ro.tn.events.api.service.AbstractService;

import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Created by danielpo on 19/03/2015.
 */
@Service
@Transactional
public class CustmizationServiceImpl extends AbstractService<Customizations> implements CustomizationService {

    @Inject
    CustomizationRepository customizationRepository;

    /**
     * Constructorul service-ului.
     */
    public CustmizationServiceImpl() {
        super(Customizations.class);
    }

    @Override
    protected PagingAndSortingRepository<Customizations, Long> getDao() {
        return customizationRepository;
    }

    @Override
    protected JpaSpecificationExecutor<Customizations> getSpecificationExecutor() {
        return customizationRepository;
    }
}
