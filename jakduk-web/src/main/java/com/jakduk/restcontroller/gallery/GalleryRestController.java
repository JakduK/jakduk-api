package com.jakduk.restcontroller.gallery;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jakduk.common.ApiConst;
import com.jakduk.common.ApiUtils;
import com.jakduk.common.CommonConst;
import com.jakduk.exception.ServiceError;
import com.jakduk.exception.ServiceException;
import com.jakduk.model.db.Gallery;
import com.jakduk.model.simple.BoardFreeOnGallery;
import com.jakduk.model.simple.GalleryOnList;
import com.jakduk.restcontroller.EmptyJsonResponse;
import com.jakduk.restcontroller.gallery.vo.GalleryOnUploadResponse;
import com.jakduk.restcontroller.gallery.vo.GalleryResponse;
import com.jakduk.restcontroller.vo.GalleriesResponse;
import com.jakduk.restcontroller.vo.UserFeelingResponse;
import com.jakduk.service.CommonService;
import com.jakduk.service.GalleryService;

/**
 * @author pyohwan
 * 16. 3. 20 오후 11:17
 */

@Api(tags = "사진첩", description = "사진 관련")
@RestController
@RequestMapping("/api")
public class GalleryRestController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private GalleryService galleryService;

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Value("${gallery.image.path}")
    private String imagePath;

    @Value("${gallery.thumbnail.path}")
    private String thumbnailPath;

    @ApiOperation(value = "사진 목록", produces = "application/json", response = GalleriesResponse.class)
    @RequestMapping(value = "/galleries", method = RequestMethod.GET)
    public GalleriesResponse getGalleries(@RequestParam(required = false) String id,
                                          @RequestParam(required = false, defaultValue = "0") int size,
                                          HttpServletRequest request) {

        if (size < CommonConst.GALLERY_SIZE) size = CommonConst.GALLERY_SIZE;

        List<GalleryOnList> galleries = galleryService.getGalleriesById(id, size);

        if (Objects.isNull(galleries))
            throw new ServiceException(ServiceError.NOT_FOUND);

        List<ObjectId> ids = galleries.stream()
                .map(gallery -> new ObjectId(gallery.getId()))
                .collect(Collectors.toList());

        Map<String, Integer> usersLikingCount = galleryService.getGalleryUsersLikingCount(ids);
        Map<String, Integer> usersDislikingCount = galleryService.getGalleryUsersDislikingCount(ids);

        GalleriesResponse response = new GalleriesResponse();
        response.setGalleries(galleries);
        response.setUsersLikingCount(usersLikingCount);
        response.setUsersDislikingCount(usersDislikingCount);

        return response;
    }

    @ApiOperation(value = "사진 올리기", produces = "application/json", response = GalleryOnUploadResponse.class)
    @RequestMapping(value = "/gallery", method = RequestMethod.POST)
    public GalleryOnUploadResponse uploadImage(@RequestParam() MultipartFile file) {

        if (! commonService.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        if (file.isEmpty())
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        Gallery gallery = galleryService.uploadImage(file);

        return GalleryOnUploadResponse.builder()
                .id(gallery.getId())
                .name(gallery.getName())
                .fileName(gallery.getFileName())
                .writer(gallery.getWriter())
                .size(gallery.getSize())
                .fileSize(gallery.getFileSize())
                .contentType(gallery.getContentType())
                .status(gallery.getStatus())
                .imageUrl(apiServerUrl + imagePath + gallery.getId())
                .thumbnailUrl(apiServerUrl + thumbnailPath + gallery.getId())
                .build();
    }

    @ApiOperation(value = "사진 지움", produces = "application/json", response = EmptyJsonResponse.class)
    @RequestMapping(value = "/gallery/{id}", method = RequestMethod.DELETE)
    public EmptyJsonResponse removeImage(@PathVariable String id) {

        if (! commonService.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        galleryService.removeImage(id);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "사진 정보", produces = "application/json")
    @RequestMapping(value = "/gallery/{id}", method = RequestMethod.GET)
    public GalleryResponse view(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {

        Boolean isAddCookie = ApiUtils.addViewsCookie(request, response, ApiConst.COOKIE_VIEWS_TYPE.GALLERY, id);
        Map<String, Object> gallery = galleryService.getGallery(id, isAddCookie);

        if (Objects.isNull(gallery)) {
            throw new ServiceException(ServiceError.NOT_FOUND);
        }

        return GalleryResponse.builder()
          .gallery((Gallery) gallery.get("gallery"))
          .next((Gallery) gallery.get("next"))
          .prev((Gallery) gallery.get("prev"))
          .linkedPosts((List<BoardFreeOnGallery>) gallery.get("linkedPosts"))
          .build();
    }

    @ApiOperation(value = "사진 좋아요 싫어요", produces = "application/json")
    @RequestMapping(value = "/gallery/{id}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse setGalleryFeeling(@PathVariable String id, @PathVariable CommonConst.FEELING_TYPE feeling) {

        if (! commonService.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        Map<String, Object> data = galleryService.setUserFeeling(id, feeling);

        return UserFeelingResponse.builder()
          .feeling((CommonConst.FEELING_TYPE) data.get("feeling"))
          .numberOfLike((Integer) data.get("numberOfLike"))
          .numberOfDislike((Integer) data.get("numberOfDislike"))
          .build();
    }
}
