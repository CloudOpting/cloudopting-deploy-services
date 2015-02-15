package eu.cloud.cloudopting.web.rest;

import eu.cloud.cloudopting.domain.Applications;
import eu.cloud.cloudopting.service.ApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ro.tn.events.api.constants.QueryConstants;
import ro.tn.events.api.controller.AbstractController;
import ro.tn.events.api.service.BaseService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Daniel P.
 */
@RestController
@RequestMapping("/api")
public class ApplicationResource extends AbstractController<Applications> {

    @Inject
    ApplicationService applicationService;

    /**
     * Default contructor.
     *
     */
    public ApplicationResource() {
        super(Applications.class);
    }


    @Override
    protected BaseService<Applications> getService() {
        return applicationService;
    }

    /**
     * This method returns a list of Applications
     *
     * @param page       Page number
     * @param size       Page size
     * @param sortBy     Sort by field
     * @param sortOrder  Sort order
     * @param filterObj  Search object
     * @param uriBuilder UriComponentsBuilder
     * @param response   HttpServletResponse
     * @return applications list
     */
    @RequestMapping(value = "/list", params = {QueryConstants.PAGE, QueryConstants.SIZE,
            QueryConstants.SORT_BY, QueryConstants.SORT_ORDER},
            method = RequestMethod.GET)
    @ResponseBody
    public final Page<Applications> findAllPaginatedAndSorted(
            @RequestParam(QueryConstants.PAGE) final int page,
            @RequestParam(QueryConstants.SIZE) final int size,
            @RequestParam(QueryConstants.SORT_BY) final String sortBy,
            @RequestParam(QueryConstants.SORT_ORDER) final String sortOrder,
            @RequestParam(QueryConstants.FILTER) final String filterObj,
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response) {
        return findPaginatedAndSortedWithFilter(page, size, sortBy, sortOrder, filterObj, uriBuilder, response);
    }

    /**
     * This method returns a single Applications instance.
     *
     * @param id         the id to be returned
     * @param uriBuilder UriComponentsBuilder
     * @param response   HttpServletResponse
     * @return applications instance
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public final Applications findOne(@PathVariable("id") final Long id, final UriComponentsBuilder uriBuilder,
                                               final HttpServletResponse response) {
        return findOneInternal(id, uriBuilder, response);
    }


    /**
     * This method creates a new Applications object
     *
     * @param applications
     * @param uriBuilder
     * @param response
     */
    @RequestMapping(value="/create",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public final void create(@RequestBody Applications applications, final UriComponentsBuilder uriBuilder,
                             final HttpServletResponse response) {
        createInternal(applications, uriBuilder, response);
    }

}
