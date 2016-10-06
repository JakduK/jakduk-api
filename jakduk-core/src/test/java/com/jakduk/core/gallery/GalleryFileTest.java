package com.jakduk.core.gallery;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by pyohwan on 16. 10. 6.
 */
public class GalleryFileTest {

    @Test
    public void 디렉터리내용확인() {
        Path path = Paths.get("/home/pyohwan/storage/image");

        try {
            DirectoryStream<Path> ds = Files.newDirectoryStream(path);

            for (Path file : ds)
                System.out.println(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
