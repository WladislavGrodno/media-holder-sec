package com.education.project.media.holder.mediaholder.tools;

import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class PathChain {
    private final Path storageRootPath;

    public PathChain(@NotNull Path storageRootPath) {
        this.storageRootPath = storageRootPath;
    }

    public Path path(@NotNull UUID id) throws IOException {
        return createChain(chain(id));
    }

    public Path path(@NotNull String filePath,
                     @NotNull String fileName) {
        return Paths.get(filePath).resolve(fileName);
    }

    public void cleanPath(@NotNull UUID id) throws IOException {
        cleanChain(chain(id));
    }

    private byte[] chain(UUID id){
        ByteBuffer chain = ByteBuffer.wrap(new byte[16]);
        chain.putLong(id.getMostSignificantBits());
        chain.putLong(id.getLeastSignificantBits());
        return chain.array();
    }

    private Path createChain(byte[] chain) throws IOException {
        Path path = storageRootPath;
        if (!Files.exists(path)) Files.createDirectories(path);

        for (int pos = 0; pos < chain.length; pos++) {
            path = path.resolve(
                    String.format("%02x%02x", chain[pos], chain[++pos]));
            if (!Files.exists(path)) Files.createDirectories(path);
        }
        return path;
    }

    private void cleanChain(byte[] chain) throws IOException {
        Path[] path = new Path[8];
        int pathIdx = 0;
        int chainIdx = 0;

        path[pathIdx++] = storageRootPath.resolve(String.format(
                "%02x%02x", chain[chainIdx++], chain[chainIdx++]));
        while(pathIdx < 8)
            path[pathIdx++] = path[pathIdx - 2].resolve(String.format(
                    "%02x%02x", chain[chainIdx++], chain[chainIdx++]));

        while (pathIdx > 0){
            if (Files.exists(path[--pathIdx])) {
                try (DirectoryStream<Path> directory =
                             Files.newDirectoryStream(path[pathIdx])) {
                    if (!directory.iterator().hasNext())
                        Files.delete(path[pathIdx]);
                }
                if (Files.exists(path[pathIdx])) pathIdx = 0;
            }
        }
    }
}
