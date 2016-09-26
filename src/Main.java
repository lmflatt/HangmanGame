import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

public class Main {

    public static List<String> secretWords = new ArrayList<>(Arrays.asList("bicycle", "ambulance", "pear tree", "starfish", "life vest"));
    public static List<String> howHanged = new ArrayList<>();
    public static List<String> hangMan = new ArrayList<>();
    public static List<String> correctGuesses = new ArrayList<>();
    public static String statement = "Enter letters to try and guess the secret word!";
    public static String userEntry;
    public static String userGuesses = "";
    public static Random randomizer = new Random();
    public static String secretWord = secretWords.get(randomizer.nextInt(secretWords.size()));
    public static String blankedWord = secretWord.replaceAll("[\\S]", "_ ");
    public static int hangingIndex = 1;

    public static void main(String[] args) {
        Spark.init();

        HangArray.populateHowHanged();
        hangMan.add(howHanged.get(0));

        for (int i = 1; i <= 7; i++) {
            hangMan.add(null);
        }

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();

                    if (blankedWord.equalsIgnoreCase(secretWord)) {
                        statement = "Congratulations! You won!";
                    }
                    else if (hangingIndex == (howHanged.size() - 1)) {
                        statement = "You lose!\n" + "The word was " + secretWord + ".";
                    }

                    m.put("hang0", hangMan.get(0));
                    m.put("hang1", hangMan.get(1));
                    m.put("hang2", hangMan.get(2));
                    m.put("hang3", hangMan.get(3));
                    m.put("hang4", hangMan.get(4));
                    m.put("hang5", hangMan.get(5));
                    m.put("hang6", hangMan.get(6));
                    m.put("hang7", hangMan.get(7));
                    m.put("word", blankedWord);
                    m.put("statement", statement);

                    return new ModelAndView(m, "index.html");
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/guess",
                ((request, response) -> {
                    userEntry = request.queryParams("guess");
                    boolean wordContainsLetter = false;

                    if (userGuesses.contains(userEntry)) {
                        statement = "You have already guessed '" + userEntry + "'.";
                        response.redirect("/");
                        return "";
                    }
                    else if (userEntry.equalsIgnoreCase(" ") && userEntry.equalsIgnoreCase("")) {
                        statement = "Please enter a letter. Do not enter a space or leave empty.\n";
                        response.redirect("/");
                        return "";
                    } else {
                        wordContainsLetter = checkLetter(userEntry, secretWord);
                    }

                    userGuesses += userEntry;

                    if (wordContainsLetter) {
                        correctGuesses.add(userEntry);
                        blankedWord = addLetter(secretWord);
                    }
                    else {
                        hangMan.add(hangingIndex, howHanged.get(hangingIndex));
                        hangingIndex++;
                    }

                    response.redirect("/");
                    return "";
                })
        );


    }

    public static boolean checkLetter(String userEntry, String secretWord) {
        return (secretWord.contains(userEntry));
    }

    public static String addLetter(String secretWord) {
        String blankedWord;
        String regexConstructor = "[^";

        for (String s : correctGuesses) {
            regexConstructor += s;
        }
        regexConstructor += "( )]";

        blankedWord = secretWord.replaceAll(regexConstructor, "_ ");
        return blankedWord;
    }
}