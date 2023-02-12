import org.hardsign.models.LocalDateRange;
import org.hardsign.utils.DateParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateParserTests {

    private LocalDateTime startLocalDateTime;
    private LocalDateTime toLocalDateTime;

    @Before
    public void setUp() {
        startLocalDateTime = LocalDateTime.of(LocalDate.of(2007, 2, 25), LocalTime.of(12, 23, 45));
        toLocalDateTime = LocalDateTime.of(LocalDate.of(2017, 5, 18), LocalTime.of(18, 7, 15));
    }

    @Test
    public void Should_ParseDateRange() {
        ensureParseDateRangeLocal(
                "25.02.2007 12:23:45-18.05.2017 18:07:15",
                new LocalDateRange(startLocalDateTime, toLocalDateTime));
    }

    @Test
    public void Should_ParseDateRange_WhenSpacesAroundSlitter() {
        ensureParseDateRangeLocal(
                "25.02.2007 12:23:45 - 18.05.2017 18:07:15",
                new LocalDateRange(startLocalDateTime, toLocalDateTime));
    }


    @Test
    public void Should_Parse_WhenTimePartNotDefined() {
        ensureParseDate("25.02.2007", LocalDateTime.of(2007, 2, 25, 0, 0));
    }

    @Test
    public void Should_ParseDate_WithoutSeconds() {
        ensureParseDate("25.02.2007 09:14", LocalDateTime.of(2007, 2, 25, 9, 14));
    }

    private void ensureParseDate(String input, LocalDateTime expected) {
        var parser = new DateParser();

        var date = parser.parseDate(input);

        Assertions.assertTrue(date.isPresent());
        Assertions.assertEquals(date.get(), expected);
    }

    private void ensureParseDateRangeLocal(String input, LocalDateRange expected) {
        var parser = new DateParser();

        var dateRange = parser.parseDateRangeLocal(input);

        Assertions.assertTrue(dateRange.isPresent());
        Assertions.assertEquals(dateRange.get(), expected);
    }
}
