package my.petproject.booking.service.impl;

import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.accommodation.AccommodationResponseDto;
import my.petproject.booking.dto.booking.BookingResponseDto;
import my.petproject.booking.model.User;
import my.petproject.booking.repository.UserRepository;
import my.petproject.booking.service.TelegramService;
import my.petproject.booking.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private static final String NEW_ACCOMMODATION_MESSAGE_TEMPLATE =
            "New accommodation was created: \nType: %s\nLocation: %s\nSize: "
                   + "%s\nDaily Rate: %s\nAvailability: %s";

    private static final String UPDATED_ACCOMMODATION_MESSAGE_TEMPLATE =
            "Accommodation by id: %d was updated\nNew Type: %s\nNew Location: "
                   + "%s\nNew Size: %s\nNew Daily Rate: %s\nNew Availability: %s";

    private static final String NEW_BOOKING_MESSAGE_TEMPLATE =
            "You ordered a new booking for: %s\n"
                   + "To get more info about your bookings type /bookings";

    private static final String UPDATED_BOOKING_MESSAGE_TEMPLATE =
            "You updated an existing booking!\n"
                   + "To get new info about your bookings type /bookings";

    @Value("${telegram.admin.chatId}")
    private Long adminChatId;

    private final TelegramBot telegramBot;
    private final UserRepository userRepository;

    @Override
    public void sendNotification(Long chatId, String message) {
        telegramBot.prepareAndSendMessage(chatId, message);
    }

    @Override
    public void sendAccommodationCreateMessage(AccommodationResponseDto accommodationResponseDto) {
        String message = String.format(NEW_ACCOMMODATION_MESSAGE_TEMPLATE,
                accommodationResponseDto.getType(),
                accommodationResponseDto.getLocation(),
                accommodationResponseDto.getSize(),
                accommodationResponseDto.getRate(),
                accommodationResponseDto.getAvailability());
        sendNotification(adminChatId, message);
    }

    @Override
    public void sendAccommodationUpdateMessage(AccommodationResponseDto accommodationResponseDto) {
        String message = String.format(UPDATED_ACCOMMODATION_MESSAGE_TEMPLATE,
                accommodationResponseDto.getId(),
                accommodationResponseDto.getType(),
                accommodationResponseDto.getLocation(),
                accommodationResponseDto.getSize(),
                accommodationResponseDto.getRate(),
                accommodationResponseDto.getAvailability());
        sendNotification(adminChatId, message);
    }

    @Override
    public void sendBookingCreateMessage(BookingResponseDto bookingResponseDto) {
        User user = userRepository.findById(bookingResponseDto.getUserId()).orElseThrow();
        Long chatId = user.getTelegramChatId();
        String message = String.format(NEW_BOOKING_MESSAGE_TEMPLATE,
                bookingResponseDto.getCheckInDate());
        sendNotification(chatId, message);
    }

    @Override
    public void sendBookingUpdateMessage(BookingResponseDto bookingResponseDto) {
        User user = userRepository.findById(bookingResponseDto.getUserId()).orElseThrow();
        Long chatId = user.getTelegramChatId();
        sendNotification(chatId, UPDATED_BOOKING_MESSAGE_TEMPLATE);
    }
}
