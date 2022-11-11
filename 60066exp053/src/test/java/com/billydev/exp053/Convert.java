
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



        /**
         * using base16 to encode and decode
         */

        String output = ConvertUtil.encodeUsingApacheCommons(Files.readAllBytes(
                Paths.get(dirCompressedZipFile)));
        System.out.println("Hex of zip file dirCompressed.zip:"+output);
        System.out.println("Hex of zip file dirCompressed.zip after splitDupAndCap:"+ConvertUtil.splitDupAndCapAndReplaceZeroWithOther(output));
        System.out.println("Hex of zip file dirCompressed.zip after splitDupAndCap and back:" + ConvertUtil.lowerDedupAndMerge(ConvertUtil.splitDupAndCapAndReplaceZeroWithOther(output)));
        String[] recStringArray = ConvertUtil.splitIntoLines(ConvertUtil.splitDupAndCapAndReplaceZeroWithOther(output));
        System.out.println("Hex of zip file dirCompressed.zip after splitDupAndCap into String array line by line:\n"
                + Arrays.toString(recStringArray));


        //local directly read in zip file not very usefull
        byte[] dataForWriting = Files.readAllBytes(
                Paths.get(dirCompressedZipFile));

        /**
         * interactive display
         */
        boolean continueValue=true;
        int currentPageNumber=0;
        while(continueValue){
            Scanner scanner = new Scanner(System.in);
            System.out.println(
                    String.format("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nCurrent Page: %s total page: %s,continue ?: ",
                            currentPageNumber,
                            recStringArray.length / LINES_PER_PAGE));
            String userInput = scanner.next();
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            int maxLineOfPage=LINES_PER_PAGE<recStringArray.length?LINES_PER_PAGE:recStringArray.length;
            for (int i = 0; i < maxLineOfPage; i++) {
                System.out.println("                   "+recStringArray[currentPageNumber*LINES_PER_PAGE+i]);
            }
            currentPageNumber++;
            continueValue=currentPageNumber<recStringArray.length/LINES_PER_PAGE;
        }


        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        /**
         * decode hex string to byte[] and write to a file
         */

        byte[] decodedByteArray = ConvertUtil.decodeUsingApacheCommons(output);

        //directly write byte array back to zip file working
        String convertedFileLocation = "/tmp/dirCompressed.ConvertedDecoded.zip";
        Files.write(
                Paths.get(convertedFileLocation),
                decodedByteArray);
        System.out.println("Converted output file of dirCompressed.ConvertedDecoded.zip: "+convertedFileLocation);



        return null;
    }
