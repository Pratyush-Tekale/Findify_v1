package com.findify.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gemini-based semantic verification for lost-and-found claims.
 *
 * Compares:
 * 1. Finder's original private item description
 * 2. Claimant's submitted description
 *
 * Gemini returns:
 * - match: true/false
 * - confidence: 0-100
 * - reasoning: short explanation
 *
 * Requires GEMINI_API_KEY environment variable.
 */
public class GeminiMatcher {

    private static final String MODEL = "gemini-3.6-flash";

    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
            + MODEL
            + ":generateContent";

    public static class Result {

        public final boolean match;
        public final int confidence;
        public final String reasoning;

        public Result(boolean match, int confidence, String reasoning) {
            this.match = match;
            this.confidence = confidence;
            this.reasoning = reasoning;
        }
    }

    /**
     * Sends both descriptions to Gemini and asks whether they describe
     * the same physical item.
     */
    public static Result compare(String originalDescription,
                                  String submittedDescription) {

        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {

            System.out.println(
                    "GEMINI_API_KEY not set — cannot AI-verify this claim."
            );

            return new Result(
                    false,
                    0,
                    "AI verification unavailable (missing API key)."
            );
        }

        try {

        	String prompt =

        	        "You are a strict verification system for a lost-and-found claim.\n\n"

        	        + "ORIGINAL description (written privately by the "
        	        + "person who found the item):\n"
        	        + safe(originalDescription)
        	        + "\n\n"

        	        + "CLAIMANT description (written by someone claiming "
        	        + "the item is theirs):\n"
        	        + safe(submittedDescription)
        	        + "\n\n"

        	        + "Your job is NOT simply to decide whether the two "
        	        + "descriptions sound similar. Your job is to determine "
        	        + "whether the claimant has provided enough consistent "
        	        + "details to reasonably identify the SAME physical item.\n\n"

        	        + "Analyze the descriptions carefully and separately "
        	        + "compare the following details:\n"
        	        + "- color\n"
        	        + "- brand\n"
        	        + "- model\n"
        	        + "- size\n"
        	        + "- shape\n"
        	        + "- visible marks\n"
        	        + "- scratches\n"
        	        + "- dents\n"
        	        + "- stickers or labels\n"
        	        + "- exact position of stickers or labels\n"
        	        + "- contents\n"
        	        + "- accessories\n"
        	        + "- color of accessories\n"
        	        + "- location of contents\n"
        	        + "- pockets or compartments\n"
        	        + "- distinctive characteristics\n"
        	        + "- condition\n\n"

        	        + "IMPORTANT VERIFICATION RULES:\n\n"

        	        + "1. EXACT DETAILS MATTER.\n"
        	        + "If the original description contains a distinctive "
        	        + "detail, treat that detail as important evidence.\n\n"

        	        + "2. CONTRADICTIONS ARE SERIOUS.\n"
        	        + "If the claimant gives a detail that directly contradicts "
        	        + "the original description, significantly reduce the "
        	        + "confidence and consider the claim a NO MATCH.\n\n"

        	        + "Examples of contradictions:\n"
        	        + "- original says navy, claimant says red\n"
        	        + "- original says left shoulder strap, claimant says right "
        	        + "shoulder strap\n"
        	        + "- original says black charger, claimant says white charger\n"
        	        + "- original says Lenovo, claimant says Dell\n"
        	        + "- original says silver laptop, claimant says black laptop\n"
        	        + "- original says front pocket, claimant says rear pocket\n\n"

        	        + "3. MISSING DETAILS ARE NOT THE SAME AS MATCHING DETAILS.\n"
        	        + "If the original says the charger was black and the claimant "
        	        + "only says 'charger', do NOT treat that as confirmation "
        	        + "that the claimant knows the charger was black. It is simply "
        	        + "an unspecified detail.\n\n"

        	        + "4. DISTINCTIVE DETAILS HAVE MORE WEIGHT.\n"
        	        + "A generic detail such as 'backpack' or 'laptop' is weak "
        	        + "evidence. A specific detail such as a faded orange tag on "
        	        + "the left shoulder strap or a dent in a particular corner "
        	        + "is strong evidence.\n\n"

        	        + "5. DO NOT GIVE A HIGH SCORE JUST BECAUSE MANY GENERIC "
        	        + "DETAILS MATCH.\n"
        	        + "Several generic matches must not outweigh an important "
        	        + "contradiction.\n\n"

        	        + "6. DIFFERENT WORDING IS ACCEPTABLE.\n"
        	        + "The claimant does not need to use the exact same words. "
        	        + "For example, 'front pocket' and 'front compartment' may "
        	        + "describe the same location.\n\n"

        	        + "7. GENERAL OR VAGUE STATEMENTS SHOULD NOT BE TREATED "
        	        + "AS PROOF.\n"
        	        + "For example, 'there was a mark on the laptop' is weaker "
        	        + "evidence than 'there was a small dent near the upper-right "
        	        + "corner'.\n\n"

        	        + "8. BE CONSERVATIVE.\n"
        	        + "If the descriptions contain significant uncertainty or "
        	        + "important contradictions, prefer a lower confidence score "
        	        + "rather than assuming the claimant is correct.\n\n"

        	        + "CONFIDENCE GUIDELINES:\n"
        	        + "90-100 = Very strong match with multiple specific details "
        	        + "and no meaningful contradictions.\n"
        	        + "75-89 = Likely match, but some specific details are missing "
        	        + "or uncertain.\n"
        	        + "50-74 = Uncertain. Some details match, but evidence is weak "
        	        + "or there are notable inconsistencies.\n"
        	        + "25-49 = Unlikely match. Important details conflict or too "
        	        + "many identifying details are missing.\n"
        	        + "0-24 = Strong evidence that the items are different.\n\n"

        	        + "A direct contradiction involving a highly distinctive "
        	        + "detail should normally prevent a confidence score above "
        	        + "70 unless there is a strong reason to believe the apparent "
        	        + "contradiction is only a wording or interpretation issue.\n\n"

        	        + "Finally, give a short reasoning sentence explaining the "
        	        + "most important matching details and any important "
        	        + "contradictions or missing details.\n\n"

        	        + "Respond ONLY with one JSON object on a single line.\n"
        	        + "Do not use markdown.\n"
        	        + "Do not use ```.\n"
        	        + "Use exactly these fields:\n"
        	        + "{\"match\":true,"
        	        + "\"confidence\":95,"
        	        + "\"reasoning\":\"Short explanation here\"}";

            String requestBody =
                    "{\"contents\":[{\"parts\":[{\"text\":\""
                    + jsonEscape(prompt)
                    + "\"}]}],"
                    + "\"generationConfig\":{\"temperature\":0}}";

            HttpClient client =
                    HttpClient.newBuilder()
                            .connectTimeout(Duration.ofSeconds(10))
                            .build();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(API_URL + "?key=" + apiKey))
                            .timeout(Duration.ofSeconds(20))
                            .header("Content-Type", "application/json")
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(requestBody)
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {

                System.out.println(
                        "Gemini API error "
                        + response.statusCode()
                        + ": "
                        + response.body()
                );

                return new Result(
                        false,
                        0,
                        "AI verification failed (API error "
                        + response.statusCode()
                        + ")."
                );
            }

            String generatedText =
                    extractGeneratedText(response.body());

            if (generatedText.isBlank()) {

                System.out.println(
                        "Gemini response did not contain generated text."
                );

                return new Result(
                        false,
                        0,
                        "AI response could not be extracted."
                );
            }

            System.out.println(
                    "Gemini raw verdict: " + generatedText
            );

            return parseVerdict(generatedText);

        } catch (Exception e) {

            e.printStackTrace();

            return new Result(
                    false,
                    0,
                    "AI verification failed ("
                    + e.getClass().getSimpleName()
                    + ")."
            );
        }
    }

    /**
     * Extracts the generated text from Gemini's response.
     *
     * The important part here is that this regex understands escaped
     * characters inside a JSON string. The previous version could stop
     * reading the text too early.
     */
    private static String extractGeneratedText(String responseBody) {

        Pattern pattern =
                Pattern.compile(
                        "\"text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"",
                        Pattern.DOTALL
                );

        Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {

            return jsonUnescape(matcher.group(1));
        }

        return "";
    }

    /**
     * Reads Gemini's JSON verdict.
     */
    private static Result parseVerdict(String text) {

        boolean match = false;
        int confidence = 0;
        String reasoning = "AI response could not be parsed.";

        /*
         * Gemini may occasionally return markdown despite being told
         * not to. Remove it before parsing.
         */
        text = text.trim();

        if (text.startsWith("```")) {

            text = text.replaceFirst("^```(?:json)?\\s*", "");
            text = text.replaceFirst("\\s*```$", "");

            text = text.trim();
        }

        /*
         * Extract match.
         */
        Pattern matchPattern =
                Pattern.compile(
                        "\"match\"\\s*:\\s*(true|false)",
                        Pattern.CASE_INSENSITIVE
                );

        Matcher matchMatcher =
                matchPattern.matcher(text);

        if (matchMatcher.find()) {

            match =
                    Boolean.parseBoolean(
                            matchMatcher.group(1)
                    );
        }

        /*
         * Extract confidence.
         */
        Pattern confidencePattern =
                Pattern.compile(
                        "\"confidence\"\\s*:\\s*(\\d+)"
                );

        Matcher confidenceMatcher =
                confidencePattern.matcher(text);

        if (confidenceMatcher.find()) {

            try {

                confidence =
                        Integer.parseInt(
                                confidenceMatcher.group(1)
                        );

                confidence =
                        Math.min(
                                100,
                                Math.max(
                                        0,
                                        confidence
                                )
                        );

            } catch (NumberFormatException e) {

                confidence = 0;
            }
        }

        /*
         * Extract reasoning.
         *
         * This version also understands escaped quotes inside the
         * reasoning string.
         */
        Pattern reasoningPattern =
                Pattern.compile(
                        "\"reasoning\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"",
                        Pattern.DOTALL
                );

        Matcher reasoningMatcher =
                reasoningPattern.matcher(text);

        if (reasoningMatcher.find()) {

            reasoning =
                    jsonUnescape(
                            reasoningMatcher.group(1)
                    );

        } else {

            /*
             * If reasoning cannot be found, don't destroy the useful
             * match/confidence result.
             */
            reasoning =
                    match
                    ? "The descriptions were judged to be consistent."
                    : "The descriptions were judged not to match.";
        }

        return new Result(
                match,
                confidence,
                reasoning
        );
    }

    /**
     * Prevents null descriptions from causing errors.
     */
    private static String safe(String s) {

        return s == null ? "" : s.trim();
    }

    /**
     * Escapes text so it can safely be inserted into a JSON request.
     */
    private static String jsonEscape(String s) {

        if (s == null) {
            return "";
        }

        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Converts escaped JSON characters back into normal text.
     */
    private static String jsonUnescape(String s) {

        if (s == null) {
            return "";
        }

        return s
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}