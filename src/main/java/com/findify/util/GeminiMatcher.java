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

        	        "You are an AI-assisted verification system for the Findify "
        	        + "campus lost-and-found system.\n\n"

        	        + "Your task is to compare a private description written by the "
        	        + "person who found an item with a description submitted by a "
        	        + "person claiming that item.\n\n"

        	        + "The purpose is to estimate how strongly the claimant's description "
        	        + "supports that both descriptions refer to the SAME physical item.\n\n"

        	        + "IMPORTANT: You are an assistant for a human administrator. "
        	        + "You must NOT make the final ownership decision. "
        	        + "The administrator will review your result and decide whether "
        	        + "the claim should be approved or rejected.\n\n"

        	        + "ORIGINAL PRIVATE FINDER DESCRIPTION:\n"
        	        + safe(originalDescription)
        	        + "\n\n"

        	        + "CLAIMANT DESCRIPTION:\n"
        	        + safe(submittedDescription)
        	        + "\n\n"

        	        + "EVALUATE THE TWO DESCRIPTIONS USING THE FOLLOWING RULES.\n\n"


        	        // =========================================================
        	        // 1. SPECIFIC DETAILS
        	        // =========================================================

        	        + "1. SPECIFIC IDENTIFYING DETAILS ARE THE STRONGEST EVIDENCE.\n"
        	        + "Give strong weight to details such as exact brand, model, "
        	        + "specific color combinations, distinctive scratches, cracks, dents, "
        	        + "stickers, unusual markings, logo placement, unusual accessories, "
        	        + "unique damage, special contents, serial-number-like information, "
        	        + "or other characteristics that could distinguish one physical item "
        	        + "from another item of the same type.\n\n"


        	        // =========================================================
        	        // 2. GENERIC DETAILS
        	        // =========================================================

        	        + "2. GENERIC DETAILS HAVE LOW EVIDENTIARY VALUE.\n"
        	        + "Words such as phone, laptop, bag, wallet, bottle, headphones, "
        	        + "black, blue, backpack, charger, book, or similar common descriptions "
        	        + "should receive only weak weight.\n\n"

        	        + "For example, 'black headphones' by itself is weak evidence because "
        	        + "many people can own black headphones.\n\n"


        	        // =========================================================
        	        // 3. NATURAL PARAPHRASING
        	        // =========================================================

        	        + "3. DIFFERENT WORDING IS ACCEPTABLE.\n"
        	        + "The claimant does not need to use exactly the same words as the finder.\n"
        	        + "Natural paraphrasing should receive credit when the physical meaning "
        	        + "is clearly consistent.\n\n"

        	        + "Examples:\n"
        	        + "'charging case' and 'charging box' may describe the same thing.\n"
        	        + "'front pocket' and 'front compartment' may describe the same location.\n"
        	        + "'small mark beside the camera' and 'tiny scratch near the camera' "
        	        + "may describe the same physical feature when the context supports it.\n\n"


        	        // =========================================================
        	        // 4. CLAIMANT MUST PROVIDE THE DETAIL
        	        // =========================================================

        	        + "4. ONLY CLAIMANT-PROVIDED DETAILS COUNT AS POSITIVE EVIDENCE.\n"
        	        + "If the finder mentions a distinctive feature but the claimant does "
        	        + "not mention it, do not treat that feature as matching.\n\n"

        	        + "For example, if the finder says 'red sticker on the back of the case' "
        	        + "and the claimant only says 'I had the charging case', the claimant "
        	        + "has not demonstrated knowledge of the red sticker.\n\n"


        	        // =========================================================
        	        // 5. MISSING INFORMATION
        	        // =========================================================

        	        + "5. MISSING INFORMATION IS NEUTRAL.\n"
        	        + "Do not automatically treat a missing detail as a contradiction.\n"
        	        + "Missing information should generally neither strongly increase nor "
        	        + "strongly decrease confidence.\n\n"


        	        // =========================================================
        	        // 6. CONTRADICTIONS
        	        // =========================================================

        	        + "6. SPECIFIC CONTRADICTIONS ARE VERY STRONG NEGATIVE EVIDENCE.\n"
        	        + "If the claimant gives a detail that conflicts with an important "
        	        + "physical characteristic in the finder description, significantly "
        	        + "reduce confidence.\n\n"

        	        + "Important contradictions include different brand, different model, "
        	        + "different color when color is distinctive, different size, different "
        	        + "shape, different damage, different sticker, different accessory, "
        	        + "different logo placement, or different position of a distinctive mark.\n\n"


        	        // =========================================================
        	        // 7. CONTRADICTION EXAMPLES
        	        // =========================================================

        	        + "Example 1:\n"
        	        + "Finder: black Lenovo laptop.\n"
        	        + "Claimant: silver Dell laptop.\n"
        	        + "This is a strong contradiction and should result in LOW confidence.\n\n"

        	        + "Example 2:\n"
        	        + "Finder: blue backpack with a red keychain attached to the left zipper.\n"
        	        + "Claimant: blue backpack with a red keychain attached to the right zipper.\n"
        	        + "The keychain position is a distinctive contradiction and should "
        	        + "strongly reduce confidence.\n\n"


        	        // =========================================================
        	        // 8. MULTIPLE CONTRADICTIONS
        	        // =========================================================

        	        + "7. MULTIPLE DISTINCTIVE CONTRADICTIONS MUST DOMINATE GENERIC MATCHES.\n"
        	        + "If two or more important identifying details contradict the finder "
        	        + "description, the overall confidence must be LOW even if the claimant "
        	        + "correctly identifies the general item category, brand, basic color, "
        	        + "or location.\n\n"

        	        + "Do NOT allow several generic similarities to compensate for multiple "
        	        + "specific contradictions.\n\n"


        	        // =========================================================
        	        // 9. SINGLE MAJOR CONTRADICTION
        	        // =========================================================

        	        + "8. A SINGLE MAJOR CONTRADICTION SHOULD ALSO SIGNIFICANTLY REDUCE SCORE.\n"
        	        + "If one highly distinctive feature clearly contradicts the finder "
        	        + "description, do not give a high confidence score unless there is a "
        	        + "clear wording ambiguity or another strong explanation.\n\n"


        	        // =========================================================
        	        // 10. DO NOT REWARD COPYING GENERIC DETAILS
        	        // =========================================================

        	        + "9. DO NOT REWARD GENERIC INFORMATION JUST BECAUSE IT MATCHES.\n"
        	        + "A claimant who says 'black boAt headphones from the library' has not "
        	        + "demonstrated strong ownership merely because the finder also wrote "
        	        + "'black boAt headphones from the library'.\n\n"

        	        + "Specific physical details matter much more than category, brand, "
        	        + "basic color, or common location information.\n\n"


        	        // =========================================================
        	        // 11. OVERALL COMBINATION OF EVIDENCE
        	        // =========================================================

        	        + "10. COMBINE MEANINGFUL EVIDENCE.\n"
        	        + "Several moderately specific matching details can together provide "
        	        + "strong evidence even when no single detail is extremely unique.\n\n"

        	        + "For example, matching brand + color + accessory + specific wear mark "
        	        + "+ specific damage location can be strong evidence when there are no "
        	        + "important contradictions.\n\n"


        	        // =========================================================
        	        // 12. LOCATION AND DATE
        	        // =========================================================

        	        + "11. LOCATION AND CONTEXT CAN SUPPORT A CLAIM.\n"
        	        + "Matching location, date, or circumstances may provide supporting "
        	        + "evidence, but these should not outweigh a clear contradiction in a "
        	        + "distinctive physical feature.\n\n"


        	        // =========================================================
        	        // 13. NO ASSUMPTIONS
        	        // =========================================================

        	        + "12. DO NOT ASSUME OR INVENT INFORMATION.\n"
        	        + "If something is not explicitly stated by the claimant, treat it as "
        	        + "UNKNOWN.\n"
        	        + "Do not assume the claimant knows a hidden detail.\n"
        	        + "Do not add facts that are not present in the descriptions.\n\n"


        	        // =========================================================
        	        // 14. DESCRIPTION LENGTH
        	        // =========================================================

        	        + "13. DO NOT REWARD DESCRIPTION LENGTH.\n"
        	        + "A long description is not automatically stronger than a short one. "
        	        + "Only meaningful and consistent evidence should increase confidence.\n\n"


        	        // =========================================================
        	        // 15. SCORE GUIDELINES
        	        // =========================================================

        	        + "14. CONFIDENCE SCORE GUIDELINES:\n\n"

        	        + "90-100 = Very strong evidence. Multiple highly useful identifying "
        	        + "details independently match and there are no meaningful contradictions. "
        	        + "Use this range only when the evidence is genuinely strong.\n\n"

        	        + "80-89 = Strong evidence. Several meaningful physical details match "
        	        + "and there are no important contradictions.\n\n"

        	        + "70-79 = Good evidence. Multiple useful details match, but some "
        	        + "important information is missing or uncertain.\n\n"

        	        + "55-69 = Moderate evidence. There are some meaningful matches, but "
        	        + "the evidence is incomplete or relies partly on generic information.\n\n"

        	        + "40-54 = Weak to moderate evidence. Mostly generic similarities or "
        	        + "limited useful overlap.\n\n"

        	        + "20-39 = Weak evidence. Few meaningful matches, significant missing "
        	        + "information, or multiple inconsistencies.\n\n"

        	        + "0-19 = Very weak evidence or strong evidence that the descriptions "
        	        + "refer to different physical items.\n\n"


        	        // =========================================================
        	        // 16. HARD ANTI-FRAUD LIMITS
        	        // =========================================================

        	        + "15. ANTI-FRAUD SCORE LIMITS:\n\n"

        	        + "If the claimant contradicts TWO OR MORE important distinctive "
        	        + "details, confidence MUST NOT exceed 39.\n\n"

        	        + "If the claimant contradicts ONE highly distinctive identifying "
        	        + "detail, confidence SHOULD normally NOT exceed 49.\n\n"

        	        + "If the claimant provides only generic details and no meaningful "
        	        + "specific identifying information, confidence SHOULD normally NOT "
        	        + "exceed 59.\n\n"


        	        // =========================================================
        	        // 17. TRUE MATCH RULE
        	        // =========================================================

        	        + "16. TRUE MATCH RULE.\n"
        	        + "Set match=true when the overall evidence reasonably supports that "
        	        + "the claimant and finder are describing the same physical item.\n\n"

        	        + "A true match can use natural paraphrasing and does not require "
        	        + "the claimant to repeat every detail.\n\n"


        	        // =========================================================
        	        // 18. FALSE MATCH RULE
        	        // =========================================================

        	        + "17. FALSE MATCH RULE.\n"
        	        + "Set match=false when the evidence is weak, mostly generic, contains "
        	        + "important contradictions, or is insufficient to reasonably support "
        	        + "the same physical item.\n\n"


        	        // =========================================================
        	        // 19. FINAL PRINCIPLE
        	        // =========================================================

        	        + "18. FINAL PRINCIPLE.\n"
        	        + "This is an ownership-evidence system, not a simple semantic "
        	        + "similarity checker.\n"
        	        + "Reward meaningful independently supplied details.\n"
        	        + "Penalize specific contradictions heavily.\n"
        	        + "Treat missing information as unknown.\n"
        	        + "Do not let generic similarities hide specific contradictions.\n"
        	        + "When the evidence is uncertain, choose the lower reasonable score.\n\n"


        	        // =========================================================
        	        // 20. OUTPUT FORMAT
        	        // =========================================================

        	        + "Return ONLY one valid JSON object on one line.\n"
        	        + "Do not use markdown.\n"
        	        + "Do not use code fences.\n"
        	        + "Do not add explanations outside the JSON object.\n\n"

        	        + "Use exactly these fields:\n"
        	        + "{\\\"match\\\":false,"
        	        + "\\\"confidence\\\":25,"
        	        + "\\\"reasoning\\\":\\\"Briefly explain the strongest matching evidence, "
        	        + "important missing evidence, and any significant contradictions.\\\"}";

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