package platform;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class MagicNumbers {
    private MagicNumbers() {

    }

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final Integer MOVIE_PRICE = 2;
    public static final Integer PREMIUM_ACCOUNT_PRICE = 10;
    public static final Integer FREE_PREMIUM_MOVIES_NUMBER = 15;
    public static final Integer MAX_RATING = 5;

}
