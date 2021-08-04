package com.example.mywebshop.service.impl;

import com.example.mywebshop.entity.Product;
import com.example.mywebshop.service.impl.query.CategoryParameter;
import com.example.mywebshop.service.impl.query.MinimalRatingParameter;
import com.example.mywebshop.service.impl.query.PriceRangeParameter;
import com.example.mywebshop.service.impl.query.SearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SearchFilterTest {

    private EntityManager em;
    private SearchFilter searchFilter;

    @BeforeEach
    void setUp() {
        em = Mockito.mock(EntityManager.class);
        Query mockQuery = Mockito.mock(Query.class);
        Mockito.when(em.createNativeQuery(Mockito.anyString(), Mockito.eq(Product.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(List.of());
        searchFilter = new SearchFilter();
        ReflectionTestUtils.setField(searchFilter, "em", em);
    }

    @Test
    void addParametersThenDelete() {
        searchFilter.addParameter(new CategoryParameter("test_category"));
        searchFilter.addParameter(new SearchParameter("asdf"));
        searchFilter.addParameter(new MinimalRatingParameter(2.));

        boolean empty = ((Map) ReflectionTestUtils.getField(searchFilter, "filters")).isEmpty();
        assertFalse(empty);

        searchFilter.removeParameter(CategoryParameter.class);
        searchFilter.removeParameter(SearchParameter.class);
        searchFilter.removeParameter(MinimalRatingParameter.class);
        empty = ((Map) ReflectionTestUtils.getField(searchFilter, "filters")).isEmpty();
        assertTrue(empty);
    }

    @Test
    void fullQueryMatchTest() {
        searchFilter.addParameter(new CategoryParameter("test_category"));
        searchFilter.addParameter(new SearchParameter("asdf"));
        searchFilter.addParameter(new MinimalRatingParameter(2.));
        searchFilter.sortBy(SearchParameter.class, true);

        searchFilter.getFiltered();

        String expected = "select * from product prd inner join product_major_category pmc on (prd.category_id = pmc.id) " +
                " where pmc.name = 'test_category' and lower(prd.title) like '%asdf%' and prd.rating >= 2.0 order by prd.title ASC  limit 0 offset 0;";
        String queryValue = captureQueryString();

        System.out.println(queryValue);
        assertEquals(expected, queryValue);
    }

    @Test
    void sortingAndPagingTest() {
        searchFilter.addParameter(new CategoryParameter("test_category"));
        searchFilter.addParameter(new SearchParameter("asdf"));
        searchFilter.addParameter(new MinimalRatingParameter(2.));
        searchFilter.addParameter(new PriceRangeParameter(90, 100));
        searchFilter.sortBy(PriceRangeParameter.class, false);

        searchFilter.getFiltered(20, 4);

        String expected = "select * from product prd inner join product_major_category pmc on (prd.category_id = pmc.id)  where pmc.name = " +
                "'test_category' and lower(prd.title) like '%asdf%' and prd.rating >= 2.0 and prd.price between 90 and 100 order by prd.price DESC  limit 20 offset 80;";
        String queryValue = captureQueryString();

        System.out.println(queryValue);
        assertEquals(expected, queryValue);
    }

    @Test
    void noFiltersReturnsAll() {
        searchFilter.getFiltered();

        String expected = "select * from product prd limit 0 offset 0;";
        String queryValue = captureQueryString();

        System.out.println(queryValue);
        assertEquals(expected, queryValue);
    }

    private String captureQueryString() {
        ArgumentCaptor<String> resultQuery = ArgumentCaptor.forClass(String.class);
        Mockito.verify(em).createNativeQuery(resultQuery.capture(), Mockito.eq(Product.class));
        return resultQuery.getValue();
    }
}