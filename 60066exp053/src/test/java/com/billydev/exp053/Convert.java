
import org.apache.commons.codec.DecoderException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.ZipOutputStream;

@SpringBootApplication
public class Convert {

    public static final int LINES_PER_PAGE = 30;

    public static void main(String[] args) {
        SpringApplication.run(Convert.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner() throws IOException, DecoderException {

        /**
         * create zip file from a directory
         */
        String sourceFile = "/testGZ";
        String dirCompressedZipFile = "/tmp/dirCompressed.zip";
        FileOutputStream fos = new FileOutputStream(Paths.get(dirCompressedZipFile).toFile());
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);

        ZipDirectory.zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();


        return null;
    }
