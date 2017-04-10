package com.jakduk.core.common.util;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import lombok.Builder;
import lombok.Getter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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
     * 이미지 파일 저장
     *
     * @param imagePath     파일 최상위 경로
     * @param localDate     파일 작성일 (년/월/일 로 폴더 나뉘어짐)
     * @param fileName      파일 제목 (확장자 제외)
     * @param contentType   콘텐츠 타입
     * @param size          콘텐츠 크기
     * @param bytes         콘텐츠
     * @throws IOException  예외 처리 필요함
     */
    public static void writeImageFile(String imagePath, LocalDate localDate, String fileName, String contentType, long size, byte[] bytes) throws IOException {

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

                InputStream inputStream = new ByteArrayInputStream(bytes);

                Thumbnails.of(inputStream)
                        .scale(scale)
                        .toFile(imageFilePath.toFile());
            }
        }
    }

    /**
     * 작은 이미지 파일 저장
     *
     * @param imagePath     파일 최상위 경로
     * @param localDate     파일 작성일 (년/월/일 로 폴더 나뉘어짐)
     * @param fileName      파일 제목 (확장자 제외)
     * @param contentType   콘텐츠 타입
     * @param width         줄일 가로 길이
     * @param height        줄일 세로 길이
     * @param bytes         콘텐츠
     * @throws IOException  예외 처리 필요함
     */
    public static void writeSmallImageFile(String imagePath, LocalDate localDate, String fileName, String contentType,
                                           Integer width, Integer height, byte[] bytes) throws IOException {

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

            InputStream inputStream = new ByteArrayInputStream(bytes);

            Thumbnails.of(inputStream)
                    .size(width, height)
                    .crop(Positions.CENTER)
                    .toFile(imageFilePath.toFile());
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
    public static ByteArrayOutputStream readImageFile(String imagePath, LocalDate localDate, String fileName, String contentType) throws IOException {

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

    /**
     * URL에서 파일을 읽어와 FileInfo 객체로 반환
     *
     * @param fileUrl URL
     * @throws IOException 예외 처리 필요함
     */
    public static FileInfo getBytesByUrl(String fileUrl) throws IOException {

        URL url = new URL(fileUrl);

        URLConnection urlConnection = url.openConnection();
        String contentType = urlConnection.getContentType();
        Long contentLength = urlConnection.getContentLengthLong();

        BufferedInputStream in = new BufferedInputStream(url.openStream());
        byte[] bytes = IOUtils.toByteArray(in);

        return FileInfo.builder()
                .contentType(contentType)
                .contentLength(contentLength)
                .bytes(bytes)
                .build();
    }

    @Builder
    @Getter
    public static class FileInfo {

        private String contentType;
        private Long contentLength;
        private byte[] bytes;
    }
}
