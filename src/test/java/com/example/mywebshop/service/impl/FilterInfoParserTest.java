package com.example.mywebshop.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.example.mywebshop.dto.SearchFilterInfo;
import com.example.mywebshop.service.IQueryFilterParameter;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FilterInfoParserTest {
    @Test
    public void testParse() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(10.0);
        searchFilterInfo.setMaxPrice(3);
        searchFilterInfo.setCategory("Category");
        searchFilterInfo.setSearchQuery("Search Query");
        searchFilterInfo.setMinPrice(1);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(4, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(1);
        assertEquals("prd.price between 1 and 3", getResult.wherePart());
        IQueryFilterParameter getResult1 = actualParseResult.get(2);
        assertEquals("prd.rating >= 10.0", getResult1.wherePart());
        assertEquals("prd.rating", getResult1.orderByPart());
        assertEquals("prd.price", getResult.orderByPart());
        IQueryFilterParameter getResult2 = actualParseResult.get(3);
        assertEquals("pmc.name", getResult2.orderByPart());
        assertEquals("inner join product_major_category pmc on (prd.category_id = pmc.id)", getResult2.joinPart());
        IQueryFilterParameter getResult3 = actualParseResult.get(0);
        assertEquals("prd.title", getResult3.orderByPart());
        assertEquals("pmc.name = 'Category'", getResult2.wherePart());
        assertEquals("lower(prd.title) like '%search query%'", getResult3.wherePart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testParse10() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(10.0);
        searchFilterInfo.setMaxPrice(null);
        searchFilterInfo.setCategory("Category");
        searchFilterInfo.setSearchQuery("Search Query");
        searchFilterInfo.setMinPrice(null);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(3, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(1);
        assertEquals("prd.rating >= 10.0", getResult.wherePart());
        assertEquals("prd.rating", getResult.orderByPart());
        IQueryFilterParameter getResult1 = actualParseResult.get(2);
        assertEquals("pmc.name", getResult1.orderByPart());
        assertEquals("inner join product_major_category pmc on (prd.category_id = pmc.id)", getResult1.joinPart());
        IQueryFilterParameter getResult2 = actualParseResult.get(0);
        assertEquals("prd.title", getResult2.orderByPart());
        assertEquals("pmc.name = 'Category'", getResult1.wherePart());
        assertEquals("lower(prd.title) like '%search query%'", getResult2.wherePart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testParse2() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(null);
        searchFilterInfo.setMaxPrice(3);
        searchFilterInfo.setCategory("Category");
        searchFilterInfo.setSearchQuery("Search Query");
        searchFilterInfo.setMinPrice(1);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(3, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(1);
        assertEquals("prd.price between 1 and 3", getResult.wherePart());
        assertEquals("prd.price", getResult.orderByPart());
        IQueryFilterParameter getResult1 = actualParseResult.get(2);
        assertEquals("pmc.name", getResult1.orderByPart());
        assertEquals("inner join product_major_category pmc on (prd.category_id = pmc.id)", getResult1.joinPart());
        IQueryFilterParameter getResult2 = actualParseResult.get(0);
        assertEquals("prd.title", getResult2.orderByPart());
        assertEquals("pmc.name = 'Category'", getResult1.wherePart());
        assertEquals("lower(prd.title) like '%search query%'", getResult2.wherePart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testParse3() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(10.0);
        searchFilterInfo.setMaxPrice(0);
        searchFilterInfo.setCategory("Category");
        searchFilterInfo.setSearchQuery("Search Query");
        searchFilterInfo.setMinPrice(1);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(4, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(1);
        assertEquals("prd.price between 1 and 2147483647", getResult.wherePart());
        IQueryFilterParameter getResult1 = actualParseResult.get(2);
        assertEquals("prd.rating >= 10.0", getResult1.wherePart());
        assertEquals("prd.rating", getResult1.orderByPart());
        assertEquals("prd.price", getResult.orderByPart());
        IQueryFilterParameter getResult2 = actualParseResult.get(3);
        assertEquals("pmc.name", getResult2.orderByPart());
        assertEquals("inner join product_major_category pmc on (prd.category_id = pmc.id)", getResult2.joinPart());
        IQueryFilterParameter getResult3 = actualParseResult.get(0);
        assertEquals("prd.title", getResult3.orderByPart());
        assertEquals("pmc.name = 'Category'", getResult2.wherePart());
        assertEquals("lower(prd.title) like '%search query%'", getResult3.wherePart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testParse4() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(10.0);
        searchFilterInfo.setMaxPrice(null);
        searchFilterInfo.setCategory("Category");
        searchFilterInfo.setSearchQuery("Search Query");
        searchFilterInfo.setMinPrice(1);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(4, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(1);
        assertEquals("prd.price between 1 and 2147483647", getResult.wherePart());
        IQueryFilterParameter getResult1 = actualParseResult.get(2);
        assertEquals("prd.rating >= 10.0", getResult1.wherePart());
        assertEquals("prd.rating", getResult1.orderByPart());
        assertEquals("prd.price", getResult.orderByPart());
        IQueryFilterParameter getResult2 = actualParseResult.get(3);
        assertEquals("pmc.name", getResult2.orderByPart());
        assertEquals("inner join product_major_category pmc on (prd.category_id = pmc.id)", getResult2.joinPart());
        IQueryFilterParameter getResult3 = actualParseResult.get(0);
        assertEquals("prd.title", getResult3.orderByPart());
        assertEquals("pmc.name = 'Category'", getResult2.wherePart());
        assertEquals("lower(prd.title) like '%search query%'", getResult3.wherePart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testParse5() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(10.0);
        searchFilterInfo.setMaxPrice(3);
        searchFilterInfo.setCategory(null);
        searchFilterInfo.setSearchQuery("Search Query");
        searchFilterInfo.setMinPrice(1);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(3, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(1);
        assertEquals("prd.price between 1 and 3", getResult.wherePart());
        assertEquals("prd.price", getResult.orderByPart());
        IQueryFilterParameter getResult1 = actualParseResult.get(2);
        assertEquals("prd.rating", getResult1.orderByPart());
        IQueryFilterParameter getResult2 = actualParseResult.get(0);
        assertEquals("prd.title", getResult2.orderByPart());
        assertEquals("prd.rating >= 10.0", getResult1.wherePart());
        assertEquals("lower(prd.title) like '%search query%'", getResult2.wherePart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testParse6() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(10.0);
        searchFilterInfo.setMaxPrice(3);
        searchFilterInfo.setCategory("");
        searchFilterInfo.setSearchQuery("Search Query");
        searchFilterInfo.setMinPrice(1);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(3, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(1);
        assertEquals("prd.price between 1 and 3", getResult.wherePart());
        assertEquals("prd.price", getResult.orderByPart());
        IQueryFilterParameter getResult1 = actualParseResult.get(2);
        assertEquals("prd.rating", getResult1.orderByPart());
        IQueryFilterParameter getResult2 = actualParseResult.get(0);
        assertEquals("prd.title", getResult2.orderByPart());
        assertEquals("prd.rating >= 10.0", getResult1.wherePart());
        assertEquals("lower(prd.title) like '%search query%'", getResult2.wherePart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testParse7() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(10.0);
        searchFilterInfo.setMaxPrice(3);
        searchFilterInfo.setCategory("Category");
        searchFilterInfo.setSearchQuery(null);
        searchFilterInfo.setMinPrice(1);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(3, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(1);
        assertEquals("prd.rating >= 10.0", getResult.wherePart());
        assertEquals("prd.rating", getResult.orderByPart());
        IQueryFilterParameter getResult1 = actualParseResult.get(2);
        assertEquals("pmc.name", getResult1.orderByPart());
        assertEquals("inner join product_major_category pmc on (prd.category_id = pmc.id)", getResult1.joinPart());
        IQueryFilterParameter getResult2 = actualParseResult.get(0);
        assertEquals("prd.price", getResult2.orderByPart());
        assertEquals("pmc.name = 'Category'", getResult1.wherePart());
        assertEquals("prd.price between 1 and 3", getResult2.wherePart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testParse8() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(10.0);
        searchFilterInfo.setMaxPrice(3);
        searchFilterInfo.setCategory("Category");
        searchFilterInfo.setSearchQuery("Search Query");
        searchFilterInfo.setMinPrice(null);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(4, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(1);
        assertEquals("prd.price between 0 and 3", getResult.wherePart());
        IQueryFilterParameter getResult1 = actualParseResult.get(2);
        assertEquals("prd.rating >= 10.0", getResult1.wherePart());
        assertEquals("prd.rating", getResult1.orderByPart());
        assertEquals("prd.price", getResult.orderByPart());
        IQueryFilterParameter getResult2 = actualParseResult.get(3);
        assertEquals("pmc.name", getResult2.orderByPart());
        assertEquals("inner join product_major_category pmc on (prd.category_id = pmc.id)", getResult2.joinPart());
        IQueryFilterParameter getResult3 = actualParseResult.get(0);
        assertEquals("prd.title", getResult3.orderByPart());
        assertEquals("pmc.name = 'Category'", getResult2.wherePart());
        assertEquals("lower(prd.title) like '%search query%'", getResult3.wherePart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testParse9() {
        // Arrange
        FilterInfoParser filterInfoParser = new FilterInfoParser();

        SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
        searchFilterInfo.setMinRating(null);
        searchFilterInfo.setMaxPrice(3);
        searchFilterInfo.setCategory("all");
        searchFilterInfo.setSearchQuery("Search Query");
        searchFilterInfo.setMinPrice(1);

        // Act
        List<IQueryFilterParameter> actualParseResult = filterInfoParser.parse(searchFilterInfo);

        // Assert
        assertEquals(2, actualParseResult.size());
        IQueryFilterParameter getResult = actualParseResult.get(0);
        assertEquals("lower(prd.title) like '%search query%'", getResult.wherePart());
        IQueryFilterParameter getResult1 = actualParseResult.get(1);
        assertEquals("prd.price between 1 and 3", getResult1.wherePart());
        assertEquals("prd.price", getResult1.orderByPart());
        assertEquals("prd.title", getResult.orderByPart());
        assertSame(searchFilterInfo, filterInfoParser.getLastParams());
    }

    @Test
    public void testConstructor() {
        // Arrange, Act and Assert
        SearchFilterInfo lastParams = (new FilterInfoParser()).getLastParams();
        assertNull(lastParams.getCategory());
        assertEquals("SearchFilterInfo(searchQuery=null, category=null, minPrice=null, maxPrice=null, minRating=null)",
                lastParams.toString());
        assertNull(lastParams.getSearchQuery());
        assertNull(lastParams.getMinRating());
        assertNull(lastParams.getMinPrice());
        assertNull(lastParams.getMaxPrice());
    }
}

