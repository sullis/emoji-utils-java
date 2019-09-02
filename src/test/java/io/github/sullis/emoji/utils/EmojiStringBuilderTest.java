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

    @Test
    public void flags() {
        EmojiStringBuilder sb = new EmojiStringBuilder();
        sb.append("Flags: ")
                .flag_canada()
                .flag_united_states_of_america()
                .flag_mexico()
                .flag_european_union()
                .flag_british_virgin_islands();
        assertEquals("Flags: \uD83C\uDDE8\uD83C\uDDE6\uD83C\uDDFA\uD83C\uDDF8\uD83C\uDDF2\uD83C\uDDFD\uD83C\uDDEA\uD83C\uDDFA\uD83C\uDDFB\uD83C\uDDEC", sb.toString());
    }
}
