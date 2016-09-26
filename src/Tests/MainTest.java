import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lee on 9/19/16.
 */
public class MainTest {
    @Test
    public void checkLetterIsInWord() throws Exception {
        String userEntry = "a";
        String secretWord = "ambulance";
        assertTrue(Main.checkLetter(userEntry, secretWord));
    }

    @Test
    public void createBlankedWordFromSecretWord() throws Exception {
        String blankedWordTest = "_ _ _ _  _ _ _ _ ";
        String secretWord = "life vest";
        String blankedWord = secretWord.replaceAll("[\\S]", "_ ");

        assertTrue(blankedWord.equalsIgnoreCase(blankedWordTest));
    }
}