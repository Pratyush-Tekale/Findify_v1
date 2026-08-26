package com.findify.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * V2 verification-question answer comparison.
 *
 * Still no AI/semantic matching or spelling-correction, but tolerant of the
 * kind of variation a real person actually types: different capitalization,
 * punctuation (commas, periods, apostrophes), extra whitespace, and words
 * given in a different order — e.g. "Books Wallet", "books, wallet", and
 * "wallet books" all now count as the same answer. A genuine misspelling
 * (e.g. "walet") is still marked wrong; that would need fuzzy/edit-distance
 * matching, which is a bigger V3-style change.
 */
public class AnswerMatcher {

    public static boolean isMatch(String correctAnswer, String submittedAnswer) {

        if (correctAnswer == null || submittedAnswer == null) {
            return false;
        }

        String normalizedCorrect = normalize(correctAnswer);
        String normalizedSubmitted = normalize(submittedAnswer);

        if (normalizedCorrect.isEmpty() || normalizedSubmitted.isEmpty()) {
            return false;
        }

        // Fast path: identical once normalized.
        if (normalizedCorrect.equals(normalizedSubmitted)) {
            return true;
        }

        // Order-independent word match, so punctuation/word-order
        // differences ("books, wallet" vs "wallet books") still count.
        return wordSet(normalizedCorrect).equals(wordSet(normalizedSubmitted));
    }

    // Lowercase, trim, strip punctuation, collapse repeated whitespace.
    private static String normalize(String s) {
        return s.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static Set<String> wordSet(String normalized) {
        return new HashSet<>(Arrays.asList(normalized.split(" ")));
    }
}