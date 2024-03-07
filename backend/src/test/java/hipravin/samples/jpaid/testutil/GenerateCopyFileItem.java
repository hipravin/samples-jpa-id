package hipravin.samples.jpaid.testutil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class GenerateCopyFileItem {

    record Item(long id, String name, String description) {
    }

    public static void main(String[] args) {
        String descriptionStub = "stub description for id ";
        Stream<Item> items = LongStream.rangeClosed(1, 1_000_000)
                .mapToObj(id -> new Item(id, "name" + id, descriptionStub + id));

        Path itemsFile = Paths.get("import/items.txt");

        try(BufferedWriter bw = Files.newBufferedWriter(itemsFile);
            PrintWriter writer = new PrintWriter(bw)) {
            items.forEach(item -> {
                writer.printf("%s\t%s\t%s\t2024-02-23 20:30:00.646935%n", item.id(), item.name(), item.description());
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
