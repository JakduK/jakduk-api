package com.jakduk.core.common.util;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * Created by pyohwanjang on 2017. 2. 22..
 */

public class FileUtils {

    /**
     * 파일 저장
     *
     * @param imagePath     파일 최상위 경로
     * @param localDate     파일 작성일 (년/월/일 로 폴더 나뉘어짐)
     * @param fileName      파일 제목 (확장자 제외)
     * @param contentType   콘텐츠 타입
     * @param size          콘텐츠 크기
     * @param bytes         콘텐츠
     * @throws IOException  예외 처리 필요함
     */
    public static void writeFile(String imagePath, LocalDate localDate, String fileName, String contentType, long size, byte[] bytes) throws IOException {

        // 사진 포맷.
        String formatName = StringUtils.split(contentType, "/")[1];

        Path imageDirPath = Paths.get(imagePath, String.valueOf(localDate.getYear()),
                String.valueOf(localDate.getMonthValue()), String.valueOf(localDate.getDayOfMonth()));

        if (Files.notExists(imageDirPath, LinkOption.NOFOLLOW_LINKS))
            Files.createDirectories(imageDirPath);

        // 사진 경로.
        Path imageFilePath = imageDirPath.resolve(fileName + "." + formatName);

        // 사진 저장.
        if (Files.notExists(imageFilePath, LinkOption.NOFOLLOW_LINKS)) {
            if ("gif".equals(formatName)) {
                Files.write(imageFilePath, bytes);
            } else {

                double scale = CoreConst.GALLERY_MAXIMUM_CAPACITY < size ?
                        CoreConst.GALLERY_MAXIMUM_CAPACITY / (double) size : 1;

                InputStream originalInputStream = new ByteArrayInputStream(bytes);

                Thumbnails.of(originalInputStream)
                        .scale(scale)
                        .toFile(imageFilePath.toFile());
            }
        }
    }

    /**
     * 파일 스트림 읽기
     *
     * @param imagePath     파일 최상위 경로
     * @param localDate     파일 작성일 (년/월/일 로 폴더 나뉘어짐)
     * @param fileName      파일 제목 (확장자 제외)
     * @param contentType   콘텐츠 타입
     * @throws IOException  예외 처리 필요함
     */
    public static ByteArrayOutputStream readFile(String imagePath, LocalDate localDate, String fileName, String contentType) throws IOException {

        // 사진 포맷.
        String formatName = StringUtils.split(contentType, "/")[1];

        Path filePath = Paths.get(imagePath, String.valueOf(localDate.getYear()), String.valueOf(localDate.getMonthValue()),
                String.valueOf(localDate.getDayOfMonth()), fileName + "." + formatName);

        if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath.toString()));
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);

            int imageByte;

            while ((imageByte = in.read()) != -1){
                byteStream.write(imageByte);
            }

            in.close();
            return byteStream;

        } else {
            throw new ServiceException(ServiceError.NOT_FOUND_GALLERY_FILE);
        }
    }
}
