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

        	        "You are a STRICT lost-and-found ownership verification system.\n\n"

        	        + "Your task is NOT to determine whether two descriptions are "
        	        + "generally similar. Your task is to determine whether the "
        	        + "claimant has demonstrated that they likely owned the EXACT "
        	        + "physical item described by the finder.\n\n"

        	        + "ORIGINAL PRIVATE FINDER DESCRIPTION:\n"
        	        + safe(originalDescription)
        	        + "\n\n"

        	        + "CLAIMANT DESCRIPTION:\n"
        	        + safe(submittedDescription)
        	        + "\n\n"

        	        + "CRITICAL PRINCIPLE:\n"
        	        + "A generic description is NOT evidence of ownership.\n"
        	        + "The claimant must independently provide specific details that "
        	        + "are consistent with the private finder description.\n\n"

        	        + "STRICT VERIFICATION RULES:\n\n"

        	        + "1. GENERIC DETAILS HAVE ALMOST NO VALUE.\n"
        	        + "Words such as phone, laptop, bag, wallet, black, blue, "
        	        + "backpack, charger, bottle, book, headphones, or similar "
        	        + "generic descriptions provide very little evidence by themselves.\n\n"

        	        + "2. SPECIFIC DETAILS ARE REQUIRED FOR A HIGH SCORE.\n"
        	        + "Look for distinctive details such as:\n"
        	        + "- exact brand and model\n"
        	        + "- unusual color combination\n"
        	        + "- scratches\n"
        	        + "- dents\n"
        	        + "- cracks\n"
        	        + "- stickers\n"
        	        + "- logos in unusual locations\n"
        	        + "- unique markings\n"
        	        + "- serial-number-like information when provided\n"
        	        + "- distinctive accessories\n"
        	        + "- unusual damage\n"
        	        + "- exact placement of marks\n"
        	        + "- unusual contents\n"
        	        + "- distinctive wear patterns\n\n"

        	        + "3. DO NOT AWARD CREDIT FOR DETAILS THAT ARE ONLY PRESENT "
        	        + "IN THE ORIGINAL DESCRIPTION.\n"
        	        + "If the finder says 'small scratch on the upper-right corner' "
        	        + "and the claimant says only 'it has a scratch', the claimant "
        	        + "has NOT demonstrated knowledge of the specific scratch.\n\n"

        	        + "4. THE CLAIMANT MUST PROVIDE THE SPECIFIC DETAIL.\n"
        	        + "Do not infer hidden knowledge from vague descriptions.\n"
        	        + "Do not assume that a generic statement refers to a distinctive "
        	        + "detail in the original description.\n\n"

        	        + "5. CONTRADICTIONS ARE VERY SERIOUS.\n"
        	        + "A contradiction in brand, model, color, size, shape, location "
        	        + "of a distinctive mark, accessory, damage, or other identifying "
        	        + "property should heavily reduce the score.\n\n"

        	        + "Examples:\n"
        	        + "Finder: black Lenovo laptop.\n"
        	        + "Claimant: silver Dell laptop.\n"
        	        + "Result: NO MATCH.\n\n"

        	        + "Finder: blue backpack with a red keychain attached to the "
        	        + "left zipper.\n"
        	        + "Claimant: blue backpack with a red keychain attached to the "
        	        + "right zipper.\n"
        	        + "Result: strong contradiction and very low confidence.\n\n"

        	        + "6. MISSING INFORMATION IS NOT MATCHING INFORMATION.\n"
        	        + "If the finder provides a distinctive feature and the claimant "
        	        + "does not mention it, do NOT count it as a match.\n\n"

        	        + "7. DO NOT REWARD LENGTH.\n"
        	        + "A long claimant description is not automatically stronger. "
        	        + "Only independently provided, consistent and specific details "
        	        + "should increase confidence.\n\n"

        	        + "8. DO NOT REWARD PARAPHRASING OF GENERIC FACTS.\n"
        	        + "For example:\n"
        	        + "'black backpack' and 'a dark-colored bag' are NOT strong evidence.\n"
        	        + "'Nike backpack with a torn red zipper on the front pocket' "
        	        + "is substantially stronger evidence.\n\n"

        	        + "9. BE EXTREMELY CONSERVATIVE.\n"
        	        + "When evidence is insufficient, choose a LOW confidence score.\n"
        	        + "Do not give the claimant the benefit of the doubt.\n"
        	        + "Do not invent information that is not explicitly stated.\n"
        	        + "Do not infer ownership merely because the descriptions describe "
        	        + "the same category of object.\n\n"

        	        + "10. HIGH CONFIDENCE MUST BE RARE.\n"
        	        + "90-100: Only when the claimant independently provides MULTIPLE "
        	        + "highly distinctive details that agree with the original and "
        	        + "there are NO meaningful contradictions.\n\n"

        	        + "80-89: Strong evidence with several specific identifying details. "
        	        + "This should be uncommon.\n\n"

        	        + "65-79: Some meaningful evidence exists, but not enough for "
        	        + "strong verification. This should normally require careful "
        	        + "human review.\n\n"

        	        + "40-64: Weak or mostly generic evidence.\n\n"

        	        + "20-39: Very weak evidence, major missing details, or significant "
        	        + "inconsistencies.\n\n"

        	        + "0-19: Strong evidence that the descriptions refer to different "
        	        + "items or almost no useful matching evidence.\n\n"

        	        + "11. IMPORTANT SCORE LIMIT:\n"
        	        + "If the claimant description contains only generic information "
        	        + "and does not independently provide at least TWO distinctive "
        	        + "details consistent with the original description, confidence "
        	        + "MUST NOT exceed 59.\n\n"

        	        + "12. ANOTHER SCORE LIMIT:\n"
        	        + "If there is only ONE distinctive matching detail and no other "
        	        + "meaningful identifying evidence, confidence MUST NOT exceed 69.\n\n"

        	        + "13. CONTRADICTION LIMIT:\n"
        	        + "If there is a clear contradiction involving an important "
        	        + "identifying characteristic, confidence MUST NOT exceed 30 "
        	        + "unless the contradiction is clearly caused by wording ambiguity.\n\n"

        	        + "14. DO NOT ASSUME.\n"
        	        + "If something is not explicitly stated by the claimant, treat "
        	        + "it as UNKNOWN, not as a match.\n\n"

        	        + "15. FINAL DECISION.\n"
        	        + "Set match=true ONLY when the evidence supports that the two "
        	        + "descriptions likely refer to the same physical item.\n"
        	        + "Set match=false when evidence is weak, generic, contradictory, "
        	        + "or insufficient.\n\n"

        	        + "Remember: this is an ownership verification system, not a "
        	        + "semantic similarity test. When uncertain, score LOWER.\n\n"

        	        + "Return ONLY one valid JSON object on one line.\n"
        	        + "No markdown.\n"
        	        + "No code fences.\n"
        	        + "No additional text.\n\n"

        	        + "Use exactly these fields:\n"
        	        + "{\"match\":false,\"confidence\":25,"
        	        + "\"reasoning\":\"Brief explanation of the strongest evidence, "
        	        + "missing evidence, and contradictions.\"}";

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