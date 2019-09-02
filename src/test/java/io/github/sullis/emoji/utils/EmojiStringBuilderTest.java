package io.github.sullis.emoji.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmojiStringBuilderTest {
    @Test
    public void happyPath() {
        EmojiStringBuilder sb = new EmojiStringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        sb.append(" ");
        sb.waving_hand_sign();
        sb.beer_mug();
        sb.flag_united_states_of_america();
        System.out.println("TEST: " + sb.toString());
        assertEquals("Hello World FIXME", sb.toString());
    }

}
