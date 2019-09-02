package io.github.sullis.emoji.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmojiStringBuilderTest {
    @Test
    public void happyPath() {
        EmojiStringBuilder sb = new EmojiStringBuilder();
        sb.append("Hello World: beer ")
                .beer_mug()
                .append(" flag ")
                .flag_united_states_of_america();
        assertEquals("Hello World: beer 🍺 flag 🇺🇸", sb.toString());
    }

}
