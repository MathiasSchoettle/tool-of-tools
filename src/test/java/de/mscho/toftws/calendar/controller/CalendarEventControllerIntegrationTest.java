package de.mscho.toftws.calendar.controller;

import de.mscho.toftws.calendar.service.CalendarEventService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@WebMvcTest(CalendarEventController.class)
public class CalendarEventControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CalendarEventService service;

    @MockBean
    private Logger logger;

    @Test
    public void Valid_Event_Returns_is_Ok() throws Exception {

        String json = "{\"title\": \"SINOS Wöchentliches Meeting\",\"hexColor\":\"#4a8fff\",\"startDateTime\": \"2022-02-07T08:00:00Z\",\"duration\": 1800}";

        mvc.perform(post("/calendar/event/timespan/weekly")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
    }

    @Test
    public void Invalid_Event_Returns_Bad_Request() throws Exception {

        String json = "{\"title\": \" \",\"hexColor\":\"not_a_color\",\"startDateTime\": \"2022-02-07T08:00:00Z\",\"duration\": -230}";

        mvc.perform(post("/calendar/event/timespan/weekly")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());
    }
}
