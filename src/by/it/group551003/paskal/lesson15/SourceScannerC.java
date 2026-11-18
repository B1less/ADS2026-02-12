package by.it.group551003.paskal.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceScannerC {

    private static class FileInfo {
        String path;
        String text;

        FileInfo(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    private static String removeComments(String text) {
        StringBuilder result = new StringBuilder(text.length());
        int i = 0;
        int n = text.length();

        while (i < n) {
            char c = text.charAt(i);

            if (i + 1 < n && c == '/' && text.charAt(i + 1) == '/') {
                i += 2;
                while (i < n && text.charAt(i) != '\n'
                        && text.charAt(i) != '\r') {
                    i++;
                }
            } else if (i + 1 < n && c == '/'
                    && text.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < n
                        && !(text.charAt(i) == '*'
                        && text.charAt(i + 1) == '/')) {
                    i++;
                }
                i = Math.min(i + 2, n);
            } else if (c == '"' || c == '\'') {
                char quote = c;
                result.append(c);
                i++;

                while (i < n) {
                    c = text.charAt(i++);
                    result.append(c);

                    if (c == '\\' && i < n) {
                        result.append(text.charAt(i++));
                    } else if (c == quote) {
                        break;
                    }
                }
            } else {
                result.append(c);
                i++;
            }
        }

        return result.toString();
    }

    private static String process(String text) {
        text = text.replaceAll(
                "(?m)^[\\t ]*(?:package|import)[\\t ]+[^;\\r\\n]*;[\\t ]*(?:\\r?\\n|$)",
                ""
        );

        text = removeComments(text);

        StringBuilder result = new StringBuilder(text.length());
        boolean space = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c < 33) {
                if (!space) {
                    result.append(' ');
                    space = true;
                }
            } else {
                result.append(c);
                space = false;
            }
        }

        return result.toString().trim();
    }

    private static int levenshtein(String a, String b) {
        int start = 0;
        int n = a.length();
        int m = b.length();

        if (Math.abs(n - m) >= 10) {
            return 10;
        }

        while (start < n && start < m
                && a.charAt(start) == b.charAt(start)) {
            start++;
        }

        int endA = n;
        int endB = m;

        while (endA > start && endB > start
                && a.charAt(endA - 1) == b.charAt(endB - 1)) {
            endA--;
            endB--;
        }

        a = a.substring(start, endA);
        b = b.substring(start, endB);

        n = a.length();
        m = b.length();

        if (n > m) {
            String temp = a;
            a = b;
            b = temp;

            int t = n;
            n = m;
            m = t;
        }

        int[] previous = new int[n + 1];
        int[] current = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            previous[i] = i;
        }

        for (int j = 1; j <= m; j++) {
            Arrays.fill(current, 10);

            if (j <= 9) {
                current[0] = j;
            }


            int from = Math.max(1, j - 9);
            int to = Math.min(n, j + 9);
            int min = 10;

            for (int i = from; i <= to; i++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

                current[i] = Math.min(
                        Math.min(previous[i] + 1, current[i - 1] + 1),
                        previous[i - 1] + cost
                );

                min = Math.min(min, current[i]);
            }

            if (min >= 10) {
                return 10;
            }

            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[n];
    }

    private static void findCopies(List<FileInfo> files) {
        Map<Integer, List<Integer>> groups = new HashMap<>();

        for (int i = 0; i < files.size(); i++) {
            int length = files.get(i).text.length();

            groups.computeIfAbsent(length, k -> new ArrayList<>()).add(i);
        }

        List<List<Integer>> copies = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            copies.add(new ArrayList<>());
        }

        for (int i = 0; i < files.size(); i++) {
            String text = files.get(i).text;
            int length = text.length();

            List<Integer> candidates = new ArrayList<>();

            for (int len = Math.max(0, length - 9);
                 len <= length + 9; len++) {

                List<Integer> group = groups.get(len);

                if (group != null) {
                    for (int j : group) {
                        if (j > i) {
                            candidates.add(j);
                        }
                    }
                }
            }

            candidates.sort(Integer::compareTo);

            for (int j : candidates) {
                if (levenshtein(text, files.get(j).text) < 10) {
                    copies.get(i).add(j);
                    copies.get(j).add(i);
                }
            }
        }

        for (int i = 0; i < files.size(); i++) {
            if (!copies.get(i).isEmpty()) {
                System.out.println(files.get(i).path);

                for (int j : copies.get(i)) {
                    System.out.println(files.get(j).path);
                }
            }
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Path.of(src);
        List<FileInfo> files = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            byte[] bytes = Files.readAllBytes(p);

                            CharsetDecoder decoder = StandardCharsets.UTF_8
                                    .newDecoder()
                                    .onMalformedInput(CodingErrorAction.REPORT)
                                    .onUnmappableCharacter(CodingErrorAction.REPORT);

                            String text;

                            try {
                                text = decoder.decode(
                                        ByteBuffer.wrap(bytes)
                                ).toString();
                            } catch (MalformedInputException e) {
                                return;
                            }

                            if (text.contains("@Test")
                                    || text.contains("org.junit.Test")) {
                                return;
                            }

                            files.add(new FileInfo(
                                    root.relativize(p).toString(),
                                    process(text)
                            ));

                        } catch (IOException e) {
                            return;
                        }
                    });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        files.sort(Comparator.comparing(f -> f.path));

        findCopies(files);
    }
}
