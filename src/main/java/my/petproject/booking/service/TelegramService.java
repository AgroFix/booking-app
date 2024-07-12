package my.petproject.booking.service;

import my.petproject.booking.dto.accommodation.AccommodationResponseDto;
import my.petproject.booking.dto.booking.BookingResponseDto;

public interface TelegramService {
    void sendNotification(Long chatId, String message);

    void sendAccommodationCreateMessage(AccommodationResponseDto accommodationResponseDto);

    void sendAccommodationUpdateMessage(AccommodationResponseDto accommodationResponseDto);

    void sendBookingCreateMessage(BookingResponseDto bookingResponseDto);

    void sendBookingUpdateMessage(BookingResponseDto bookingResponseDto);
}
