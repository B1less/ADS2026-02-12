package by.it.group551003.paskal.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class SourceScannerA {

    private static final Pattern PACKAGE_IMPORT = Pattern.compile(
            "(?m)^[\\t ]*(?:package|import)[\\t ]+[^;\\r\\n]*;[\\t ]*(?:\\r?\\n|$)"
    );

    private static class FileInfo {
        String path;
        int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }

    private static void collectFiles(File directory, List<File> files) {
        File[] entries = directory.listFiles();
        if (entries == null) return;

        for (File entry : entries) {
            if (entry.isDirectory()) {
                collectFiles(entry, files);
            } else if (entry.isFile() && entry.getName().endsWith(".java")) {
                files.add(entry);
            }
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<File> files = new ArrayList<>();
        List<FileInfo> results = new ArrayList<>();

        collectFiles(root.toFile(), files);

        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);

        for (File file : files) {
            try {
                decoder.reset();
                String text = decoder.decode(ByteBuffer.wrap(Files.readAllBytes(file.toPath()))).toString();

                if (text.contains("@Test") || text.contains("org.junit.Test")) continue;

                text = PACKAGE_IMPORT.matcher(text).replaceAll("");

                int start = 0;
                int end = text.length();
                while (start < end && text.charAt(start) < 33) start++;
                while (end > start && text.charAt(end - 1) < 33) end--;
                text = text.substring(start, end);

                int size = text.getBytes(StandardCharsets.UTF_8).length;
                String relativePath = root.relativize(file.toPath()).toString();
                results.add(new FileInfo(relativePath, size));

            } catch (IOException ignored) {
            }
        }

        results.sort(
                Comparator.comparingInt((FileInfo f) -> f.size)
                        .thenComparing(f -> f.path)
        );

        for (FileInfo file : results) {
            System.out.println(file.size + " " + file.path);
        }
    }
}