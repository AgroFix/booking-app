package my.petproject.booking.telegram;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import my.petproject.booking.config.TelegramBotConfig;
import my.petproject.booking.exception.EntityNotFoundException;
import my.petproject.booking.model.Amenity;
import my.petproject.booking.model.Booking;
import my.petproject.booking.model.User;
import my.petproject.booking.repository.BookingRepository;
import my.petproject.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    public static final String ADMIN_MESSAGE_TEMPLATE =
            "Booking for user %s "
                    + "%s by location: %s will be expired on %s";
    private static final String USER_FIND_EXCEPTION = "Can't find user by chat id ";
    private static final String UNSUBSCRIBED_EXCEPTION = "You are not subscribed to notifications";
    private static final String EMAIL_REGEX =
            "^[\\w]{1,}[\\w.+-]{0,}@[\\w-]{2,}([.][a-zA-Z]{2,}|[.][\\w-]{2,}[.][a-zA-Z]{2,})$";
    private static final String START_COMMAND = "/start";
    private static final String BOOKING_COMMAND = "/booking";
    private static final String STOP_COMMAND = "/stop";
    private static final String START_COMMAND_DESCRIPTION = "Click to start using the bot";
    private static final String BOOKING_COMMAND_DESCRIPTION = "Click to view your bookings";
    private static final String STOP_COMMAND_DESCRIPTION = "Click to unsubscribe from the bot";
    private static final String ERROR_SETTING_COMMANDS_MESSAGE =
            "Error setting bot`s command list: ";
    private static final String WARNINGS_TIME = "0 0 13 * * *";
    private static final String UNKNOWN_COMMAND = "Unknown command!";
    private static final String UNSUBSCRIBED_MESSAGE = "You have unsubscribed "
            + "from notifications";
    private static final String HELLO_MESSAGE = "Hello, %s! Please, write your email to register.";
    private static final String REGISTERED_MESSAGE =
            "Great, now you will receive notifications about your tasks. "
                    + "You can stop that by /stop";
    private static final String RE_REGISTRATION_MESSAGE =
            "It seems that you have already subscribed to receive notifications about your tasks";
    private static final String USER_MESSAGE_TEMPLATE =
            "Your booking by location: %s will be expired on %s";
    private static final String BOOKINGS_HEADER = "Your bookings: ";
    private static final String BOOKING_INFO_TEMPLATE =
            "\n\nType: %s\nLocation: %s\nSize: %s\nCheck-in date: %s"
                  + "\nCheck-out date: %s\nAmenities: ";
    private static final String NON_EXIST_EMAIL =
            "Sorry, no user with this email address was found";
    private static final String NO_EXPIRED_BOOKING_MESSAGE =
            "No expired bookings today!";

    private static final String NO_AMENITIES = "No amenities";
    private static final Long DEFAULT_EMPTY_CHAT_ID = -1L;
    private static final Integer ONE_DAY = 1;
    private static final Integer ZERO_MESSAGES = 0;
    private static final int TRAILING_COMMA_AND_SPACE_LENGTH = 2;

    private final TelegramBotConfig telegramBotConfig;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public TelegramBot(UserRepository userRepository,
                       TelegramBotConfig telegramBotConfig,
                       BookingRepository bookingRepository) {
        super(telegramBotConfig.getToken());
        this.userRepository = userRepository;
        this.telegramBotConfig = telegramBotConfig;
        this.bookingRepository = bookingRepository;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(START_COMMAND, START_COMMAND_DESCRIPTION));
        listOfCommands.add(new BotCommand(STOP_COMMAND, BOOKING_COMMAND_DESCRIPTION));
        listOfCommands.add(new BotCommand(BOOKING_COMMAND, STOP_COMMAND_DESCRIPTION));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(ERROR_SETTING_COMMANDS_MESSAGE + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            sendFunctionalMessageToUser(update);
        }
    }

    public void prepareAndSendMessage(Long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerUser(Long chatId, String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail.isPresent()) {
            User user = userByEmail.get();
            Long telegramChatId = user.getTelegramChatId();
            if (telegramChatId.equals(DEFAULT_EMPTY_CHAT_ID)) {
                userRepository.save(user.setTelegramChatId(chatId));
                prepareAndSendMessage(chatId, REGISTERED_MESSAGE);
            } else {
                prepareAndSendMessage(chatId, RE_REGISTRATION_MESSAGE);
            }
        } else {
            prepareAndSendMessage(chatId, NON_EXIST_EMAIL);
        }
    }

    private void sendFunctionalMessageToUser(Update update) {
        Message message = update.getMessage();
        String messageText = message.getText();
        Long chatId = message.getChatId();

        switch (messageText) {
            case STOP_COMMAND:
                handleStopCommand(chatId);
                break;
            case START_COMMAND:
                handleStartCommand(chatId, message);
                break;
            case BOOKING_COMMAND:
                handleBookingCommand(chatId);
                break;
            default:
                if (messageText.matches(EMAIL_REGEX)) {
                    registerUser(chatId, messageText);
                } else {
                    handleUnknownCommand(chatId);
                }
                break;
        }
    }

    private void handleStopCommand(Long chatId) {
        User user = findUserByChatId(chatId);
        user.setTelegramChatId(DEFAULT_EMPTY_CHAT_ID);
        userRepository.save(user);
        prepareAndSendMessage(chatId, UNSUBSCRIBED_MESSAGE);
    }

    private void handleStartCommand(Long chatId, Message message) {
        prepareAndSendMessage(chatId, String.format(HELLO_MESSAGE,
                message.getChat().getFirstName()));
    }

    private void handleUnknownCommand(Long chatId) {
        prepareAndSendMessage(chatId, UNKNOWN_COMMAND);
    }

    private void handleBookingCommand(Long chatId) {
        if (!userRepository.existsUserByTelegramChatId(chatId)) {
            prepareAndSendMessage(chatId, UNSUBSCRIBED_EXCEPTION);
            return;
        }
        User user = findUserByChatId(chatId);
        prepareAndSendMessage(chatId, formBookingMessage(user));
    }

    private String formBookingMessage(User user) {
        List<Booking> bookings = bookingRepository
                .findAllBookingsWithAmenitiesByUserId(user.getId());
        StringBuilder message = new StringBuilder(BOOKINGS_HEADER);
        for (var booking : bookings) {
            message.append(String.format(BOOKING_INFO_TEMPLATE,
                    booking.getAccommodation().getType(),
                    booking.getAccommodation().getLocation(),
                    booking.getAccommodation().getSize(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate()));
            List<Amenity> amenities = booking.getAccommodation().getAmenities();
            if (amenities.isEmpty()) {
                message.append(NO_AMENITIES);
            } else {
                for (var amenity : amenities) {
                    message.append(amenity.getName()).append(", ");
                }
                message.delete(message.length() - TRAILING_COMMA_AND_SPACE_LENGTH,
                        message.length());
            }
        }
        return message.toString();
    }

    @Scheduled(cron = WARNINGS_TIME)
    @Transactional
    public void sendWarnings() {
        int counterOfSendingMessageToUser = ZERO_MESSAGES;
        List<Booking> allBookings = bookingRepository.findAll();
        for (var booking : allBookings) {
            if (booking.getCheckOutDate().equals(LocalDate.now().plusDays(ONE_DAY))
                    && booking.getStatus() != Booking.Status.CANCELED) {
                booking.setStatus(Booking.Status.EXPIRED);
                prepareAndSendMessage(booking.getUser().getTelegramChatId(),
                        formUserMessage(booking));
                prepareAndSendMessage(telegramBotConfig.getAdminChatId(),
                        formAdminMessage(booking));
                counterOfSendingMessageToUser++;
            }
        }
        if (counterOfSendingMessageToUser == ZERO_MESSAGES) {
            prepareAndSendMessage(telegramBotConfig.getAdminChatId(), NO_EXPIRED_BOOKING_MESSAGE);
        }
    }

    private String formUserMessage(Booking booking) {
        return String.format(USER_MESSAGE_TEMPLATE, booking.getAccommodation()
                .getLocation(), booking.getCheckOutDate());
    }

    private String formAdminMessage(Booking booking) {
        User user = booking.getUser();
        return String.format(
                ADMIN_MESSAGE_TEMPLATE,
                user.getFirstName(),
                user.getLastName(),
                booking.getAccommodation().getLocation(),
                booking.getCheckOutDate()
        );
    }

    private User findUserByChatId(Long id) {
        try {
            return userRepository.findUserByTelegramChatId(id).orElseThrow(()
                    -> new EntityNotFoundException(USER_FIND_EXCEPTION
                    + id));
        } catch (EntityNotFoundException exception) {
            prepareAndSendMessage(id, UNSUBSCRIBED_EXCEPTION);
            throw new RuntimeException(UNSUBSCRIBED_EXCEPTION);
        }
    }
}
