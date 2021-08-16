package com.example.mywebshop.service.impl;

import com.example.mywebshop.dto.SearchFilterInfo;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.service.IQueryFilterParameter;
import com.example.mywebshop.service.ISearchFilter;
import com.example.mywebshop.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@SessionScope
@Slf4j
public class SearchFilter implements ISearchFilter {

    private static final String filteringTableName = "product";

    @PersistenceContext
    private EntityManager em;

    private Map<Class<?>, IQueryFilterParameter> filters = new LinkedHashMap<>();
    private Class<? extends IQueryFilterParameter> sortByParameter;
    private boolean sortAscending;
    private final FilterInfoParser filterInfoParser = new FilterInfoParser();

    @Value("${my-values.default-page-size}")
    private int defaultPageSize;

    @Override
    public void addParameter(IQueryFilterParameter parameter) {
        filters.put(parameter.getClass(), parameter);
    }

    @Override
    public void removeParameter(Class<? extends IQueryFilterParameter> parameterType) {
        filters.remove(parameterType);
    }

    @Override
    public void removeParameter(IQueryFilterParameter parameter) {
        filters.remove(parameter.getClass());
    }

    @Override
    public void clearFilters() {
        sortByParameter = null;
        filters.clear();
        filterInfoParser.clear();
    }

    @Override
    public void sortBy(Class<? extends IQueryFilterParameter> parameterType, boolean ascending) {
        this.sortByParameter = parameterType;
        this.sortAscending = ascending;
    }

    @Override
    public void parseAndSetParameters(SearchFilterInfo params) {
        clearFilters();
        List<IQueryFilterParameter> parsedFilters = filterInfoParser.parse(params);
        for (IQueryFilterParameter parsedFilter : parsedFilters) {
            addParameter(parsedFilter);
        }
    }

    @Override
    public SearchFilterInfo getLastParams() {
        return filterInfoParser.getLastParams();
    }

    @Override
    public List<Product> getFiltered() {
        return getFiltered(defaultPageSize, 0);
    }

    @Override
    public List<Product> getFiltered(int pageSize, int pageNum) {
        String initialQuery = Util.concat("select * from ", filteringTableName, " ", IQueryFilterParameter.alias);
        StringBuilder queryBuilder = new StringBuilder(initialQuery);

        if (!filters.isEmpty()) {
            var filterValues = filters.values();
            processJoinParts(queryBuilder, filterValues);
            processWhereParts(queryBuilder, filterValues);
        }
        processSortPart(queryBuilder);
        processPagingPart(pageSize, pageNum, queryBuilder);

        queryBuilder.append(";");

        String resultQuery = queryBuilder.toString();
        log.info("result query '{}'", resultQuery);
        return (List<Product>) em.createNativeQuery(resultQuery, Product.class).getResultList();
    }

    private void processPagingPart(int pageSize, int pageNum, StringBuilder query) {
        String limitOffset = Util.concat(" limit ", String.valueOf(pageSize), " offset ", String.valueOf(pageNum * pageSize));
        query.append(limitOffset);
    }

    private void processSortPart(StringBuilder query) {
        if (sortByParameter != null) {
            IQueryFilterParameter sortParam = filters.get(sortByParameter);
            if (sortParam != null && notEmpty(sortParam.orderByPart())) {
                query.append(" order by ");
                query.append(sortParam.orderByPart());
                query.append(sortAscending ? " ASC " : " DESC ");
            }
        }
    }

    private void processWhereParts(StringBuilder query, Collection<IQueryFilterParameter> filterValues) {
        String and = " and ";
        query.append(" where ");
        for (var filter : filterValues) {
            String wherePart = filter.wherePart();
            if (notEmpty(wherePart)) {
                query.append(wherePart);
                query.append(and);
            }
        }
        // delete the last 'and'
        query.delete(query.length() - and.length(), query.length());
    }

    private void processJoinParts(StringBuilder query, Collection<IQueryFilterParameter> filterValues) {
        for (var filter : filterValues) {
            String joinPart = filter.joinPart();
            if (notEmpty(joinPart)) {
                query.append(" ");
                query.append(joinPart);
                query.append(" ");
            }
        }
    }

    private boolean notEmpty(String s) {
        return s != null && !s.isEmpty();
    }

}
