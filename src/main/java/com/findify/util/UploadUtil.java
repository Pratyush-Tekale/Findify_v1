package com.findify.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import jakarta.servlet.http.Part;

/**
 * Hardened image upload handling.
 *
 * Nothing user-supplied is trusted:
 *  - the original filename is discarded, a UUID name is generated
 *  - only a fixed whitelist of image extensions is allowed
 *  - the declared content type must be an image
 *  - the first bytes of the file must actually look like that image type
 *  - the size is capped
 *
 * This is what stops somebody uploading shell.jsp and executing it.
 */
public final class UploadUtil {

    public static final long MAX_BYTES = 5L * 1024 * 1024; // 5 MB

    private static final List<String> ALLOWED_EXT =
            Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

    private static final List<String> ALLOWED_MIME =
            Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp");

    private UploadUtil() {
    }

    /** Thrown when the uploaded file is not an acceptable image. */
    public static class InvalidUploadException extends Exception {

        private static final long serialVersionUID = 1L;

        public InvalidUploadException(String message) {
            super(message);
        }
    }

    /**
     * Validates and stores the part.
     *
     * @return the generated file name to store in the database,
     *         or null when no file was submitted at all.
     */
    public static String saveImage(Part part, String uploadPath)
            throws InvalidUploadException, IOException {

        if (part == null || part.getSize() == 0) {
            return null;
        }

        if (part.getSize() > MAX_BYTES) {
            throw new InvalidUploadException("File is larger than 5 MB.");
        }

        String submitted = part.getSubmittedFileName();

        if (submitted == null || submitted.trim().isEmpty()) {
            return null;
        }

        // Strip any path the browser may have sent (../../ etc).
        String baseName = Paths.get(submitted).getFileName().toString();

        String extension = extensionOf(baseName);

        if (!ALLOWED_EXT.contains(extension)) {
            throw new InvalidUploadException(
                    "Only JPG, PNG, GIF and WEBP images are allowed.");
        }

        String contentType = part.getContentType();

        if (contentType == null
                || !ALLOWED_MIME.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new InvalidUploadException(
                    "That file is not a recognised image.");
        }

        if (!looksLikeImage(part)) {
            throw new InvalidUploadException(
                    "That file is not a real image.");
        }

        // The stored name never contains anything the user typed.
        String storedName = UUID.randomUUID().toString() + "." + extension;

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new IOException("Could not create upload directory: " + uploadPath);
        }

        part.write(new File(uploadDir, storedName).getAbsolutePath());

        return storedName;
    }

    /**
     * The upload directory, kept in one place so both servlets agree.
     * Override with -Dfindify.upload.dir=/var/findify/uploads so images
     * survive a redeploy; otherwise it falls back inside the webapp.
     */
    public static String resolveUploadPath(String webappRealPath) {

        String configured = System.getProperty("findify.upload.dir");

        if (configured != null && !configured.trim().isEmpty()) {
            return configured.trim();
        }

        return new File(webappRealPath, "uploads").getAbsolutePath();
    }

    private static String extensionOf(String name) {

        int dot = name.lastIndexOf('.');

        if (dot < 0 || dot == name.length() - 1) {
            return "";
        }

        return name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    /** Checks the file's magic bytes rather than trusting its name. */
    private static boolean looksLikeImage(Part part) throws IOException {

        byte[] head = new byte[12];

        try (InputStream in = part.getInputStream()) {

            int read = in.read(head);

            if (read < 4) {
                return false;
            }
        }

        // JPEG: FF D8 FF
        if ((head[0] & 0xFF) == 0xFF
                && (head[1] & 0xFF) == 0xD8
                && (head[2] & 0xFF) == 0xFF) {
            return true;
        }

        // PNG: 89 50 4E 47
        if ((head[0] & 0xFF) == 0x89
                && head[1] == 'P' && head[2] == 'N' && head[3] == 'G') {
            return true;
        }

        // GIF: "GIF8"
        if (head[0] == 'G' && head[1] == 'I' && head[2] == 'F' && head[3] == '8') {
            return true;
        }

        // WEBP: "RIFF" .... "WEBP"
        if (head[0] == 'R' && head[1] == 'I' && head[2] == 'F' && head[3] == 'F'
                && head[8] == 'W' && head[9] == 'E' && head[10] == 'B' && head[11] == 'P') {
            return true;
        }

        return false;
    }
}
