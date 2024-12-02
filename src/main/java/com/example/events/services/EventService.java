package com.example.events.services;

import com.example.events.dto.EventDto;
import com.example.events.dto.EventRequestDto;
import com.example.events.dto.ReviewDto;
import com.example.events.dto.UserResponseDto;
import com.example.events.exceptions.AppException;
import com.example.events.model.EventType;
import com.example.events.repositories.AttendeeRepository;
import com.example.events.repositories.EventRepository;
import com.example.events.repositories.ReviewRepository;
import com.example.events.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final UserService userService;
    private final EventRepository eventRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<EventDto> getEvents() {
        return eventRepository.getEvents();
    }
    public List<EventDto> getEventsByFilter(EventRequestDto requestDto) {
        // Parse eventDate as LocalDate (ignoring the time)
        LocalDate parsedDate = requestDto.getEventDate() != null
                ? LocalDate.parse(requestDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : null;

        return eventRepository.getEventsByFilter(
                requestDto.getTitle(),
                parsedDate,
                requestDto.getEventType()
        );
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteEvent(eventId);
    }

    public void updateEvent(Long eventId, EventRequestDto eventRequestDto) {
        Timestamp eventDate = Timestamp.valueOf(eventRequestDto.getEventDate());
        eventRepository.updateEvent(eventId, eventRequestDto.getTitle(), eventRequestDto.getDescription(), eventRequestDto.getLocation(), eventRequestDto.getEventType(), eventDate);
    }

    public void createEvent(EventRequestDto eventRequestDto) {
        Timestamp createdOn = Timestamp.from(Instant.now());
        Timestamp eventDate = Timestamp.valueOf(eventRequestDto.getEventDate());
        int userId = userService.getCurrentUserId().intValue();
        EventDto eventDto = EventDto.builder()
                .title(eventRequestDto.getTitle())
                .description(eventRequestDto.getDescription())
                .location(eventRequestDto.getLocation())
                .eventType(eventRequestDto.getEventType())
                .eventDate(eventDate)
                .createdOn(createdOn)
                .userId(userId)
                .build();
        eventRepository.createEvent(eventDto);
    }


    public List<EventDto> getRegisteredEvents() {
        Long userId = userService.getCurrentUserId();
        return eventRepository.getRegisteredEvents(userId);
    }

    public List<EventType> getEventTypes() {
        return Arrays.stream(EventType.values()).toList();
    }

    public ByteArrayInputStream generateEventExcel(Long eventId) {
        EventDto eventDto = eventRepository.getEventById(eventId);
        List<ReviewDto> reviews = reviewRepository.getReviews(eventId);
        List<UserResponseDto> attendees = userRepository.getUsersByEventId(eventId);

        int attendeeCount = attendees.size();
        double avgRating = reviews.stream()
                .mapToDouble(ReviewDto::getRating)
                .average()
                .orElse(0.0);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Renginio ataskaita");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Renginio pavadinimas");
            headerRow.createCell(1).setCellValue("Dalyvių skaičius");
            headerRow.createCell(2).setCellValue("Įvertinimų vidurkis");
            sheet.setColumnWidth(0, 30*256);
            sheet.setColumnWidth(1, 30*256);
            sheet.setColumnWidth(2, 30*256);

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(eventDto.getTitle());
            dataRow.createCell(1).setCellValue(attendeeCount);
            dataRow.createCell(2).setCellValue(avgRating);

            sheet.createRow(2);
            Row temp = sheet.createRow(3);
            temp.createCell(0).setCellValue("Užsiregistravę dalyviai");

            Row userHeaderRow = sheet.createRow(4);
            userHeaderRow.createCell(0).setCellValue("Vardas");
            userHeaderRow.createCell(1).setCellValue("Pavardė");
            userHeaderRow.createCell(2).setCellValue("El. paštas");

            int userRowNumber = 5;
            for (UserResponseDto attendee : attendees) {
                Row userDataRow = sheet.createRow(userRowNumber++);
                userDataRow.createCell(0).setCellValue(attendee.getFirstName());
                userDataRow.createCell(1).setCellValue(attendee.getLastName());
                userDataRow.createCell(2).setCellValue(attendee.getEmail());
            }
            sheet.createRow(userRowNumber);

            Row reviewHeaderRow = sheet.createRow(userRowNumber+1);
            reviewHeaderRow.createCell(0).setCellValue("Įvertinimas");
            reviewHeaderRow.createCell(1).setCellValue("Atsiliepimas");

            int reviewRowNumber = userRowNumber+2;
            for(ReviewDto review : reviews) {
                Row reviewDataRow = sheet.createRow(reviewRowNumber++);
                reviewDataRow.createCell(0).setCellValue(review.getRating());
                reviewDataRow.createCell(1).setCellValue(review.getText());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new AppException("Failed to generate Excel file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
