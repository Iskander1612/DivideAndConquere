package org.example.dnc.io;


import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;


public final class CsvWriter {
    private final Path path;


    public CsvWriter(String file) { this.path = Paths.get(Objects.requireNonNull(file)); }


    public void headerIfNew() throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent() == null ? Paths.get(".") : path.getParent());
            Files.writeString(path,
                    "algo,n,trial,timeNanos,depth,comparisons,swaps,allocations,result\n",
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        }
    }


    public void appendRow(String row) throws IOException {
        Files.writeString(path, row + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}