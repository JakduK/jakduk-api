package com.jakduk.api.restcontroller.gallery;

import com.jakduk.api.common.ApiConst;
import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.CommonPrincipal;
import com.jakduk.api.restcontroller.EmptyJsonResponse;
import com.jakduk.api.restcontroller.gallery.vo.GalleryOnUploadResponse;
import com.jakduk.api.restcontroller.gallery.vo.GalleryResponse;
import com.jakduk.api.restcontroller.gallery.vo.GalleriesResponse;
import com.jakduk.api.restcontroller.board.vo.UserFeelingResponse;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.simple.BoardFreeSimple;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.service.GalleryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 20 오후 11:17
 */

@Api(tags = "Gallery", description = "사진첩 API")
@RequestMapping("/api")
@RestController
public class GalleryRestController {

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Value("${core.gallery.image.path}")
    private String imagePath;

    @Value("${core.gallery.thumbnail.path}")
    private String thumbnailPath;

    @Autowired
    private GalleryService galleryService;

    @ApiOperation(value = "사진 목록", response = GalleriesResponse.class)
    @RequestMapping(value = "/galleries", method = RequestMethod.GET)
    public GalleriesResponse getGalleries(@RequestParam(required = false) String id,
                                          @RequestParam(required = false, defaultValue = "0") Integer size) {

        if (size < CoreConst.GALLERY_SIZE) size = CoreConst.GALLERY_SIZE;

        List<GalleryOnList> galleries = galleryService.getGalleriesById(id, size);

        if (ObjectUtils.isEmpty(galleries))
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

    @ApiOperation(value = "사진 올리기", response = GalleryOnUploadResponse.class)
    @RequestMapping(value = "/gallery", method = RequestMethod.POST)
    public GalleryOnUploadResponse uploadImage(@RequestParam MultipartFile file) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        if (file.isEmpty())
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        Gallery gallery = null;

        try {
            gallery = galleryService.uploadImage(commonWriter, file.getOriginalFilename(), file.getSize(), file.getContentType(), file.getBytes());
        } catch (IOException ignored) {
        }

        GalleryOnUploadResponse response = new GalleryOnUploadResponse();
        BeanUtils.copyProperties(gallery, response);
        response.setImageUrl(apiServerUrl + imagePath + gallery.getId());
        response.setThumbnailUrl(apiServerUrl + thumbnailPath + gallery.getId());

        return response;
    }

    @ApiOperation(value = "사진 지움", response = EmptyJsonResponse.class)
    @RequestMapping(value = "/gallery/{id}", method = RequestMethod.DELETE)
    public EmptyJsonResponse removeImage(@PathVariable String id) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal principal = UserUtils.getCommonPrincipal();

        galleryService.removeImage(principal.getId(), id);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "사진 정보")
    @RequestMapping(value = "/gallery/{id}", method = RequestMethod.GET)
    public GalleryResponse view(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {

        Boolean isAddCookie = ApiUtils.addViewsCookie(request, response, ApiConst.VIEWS_COOKIE_TYPE.GALLERY, id);
        Map<String, Object> gallery = galleryService.getGallery(id, isAddCookie);

        if (Objects.isNull(gallery)) {
            throw new ServiceException(ServiceError.NOT_FOUND);
        }

        return GalleryResponse.builder()
          .gallery((Gallery) gallery.get("gallery"))
          .next((Gallery) gallery.get("next"))
          .prev((Gallery) gallery.get("prev"))
          .linkedPosts((List<BoardFreeSimple>) gallery.get("linkedPosts"))
          .build();
    }

    @ApiOperation(value = "사진 좋아요 싫어요")
    @RequestMapping(value = "/gallery/{id}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse setGalleryFeeling(@PathVariable String id, @PathVariable CoreConst.FEELING_TYPE feeling) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        Map<String, Object> data = galleryService.setUserFeeling(writer, id, feeling);

        return UserFeelingResponse.builder()
          .feeling((CoreConst.FEELING_TYPE) data.get("feeling"))
          .numberOfLike((Integer) data.get("numberOfLike"))
          .numberOfDislike((Integer) data.get("numberOfDislike"))
          .build();
    }
}
