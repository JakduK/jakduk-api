package com.jakduk.api.restcontroller.gallery;

import com.jakduk.api.common.ApiConst;
import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.common.vo.AuthUserProfile;
import com.jakduk.api.restcontroller.EmptyJsonResponse;
import com.jakduk.api.restcontroller.gallery.vo.GalleriesResponse;
import com.jakduk.api.restcontroller.gallery.vo.GalleryDetail;
import com.jakduk.api.restcontroller.gallery.vo.GalleryResponse;
import com.jakduk.api.restcontroller.gallery.vo.GalleryUploadResponse;
import com.jakduk.api.restcontroller.vo.UserFeelingResponse;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.service.GalleryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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

    @Value("${api.gallery.image.url.path}")
    private String imagePath;

    @Value("${api.gallery.thumbnail.url.path}")
    private String thumbnailPath;

    @Autowired
    private GalleryService galleryService;

    @Resource
    private ApiUtils apiUtils;

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

    @ApiOperation(value = "사진 올리기")
    @RequestMapping(value = "/gallery", method = RequestMethod.POST)
    public GalleryUploadResponse uploadImage(@RequestParam MultipartFile file) {

        if (file.isEmpty())
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        Gallery gallery = null;

        try {
            gallery = galleryService.uploadImage(commonWriter, file.getOriginalFilename(), file.getSize(), file.getContentType(), file.getBytes());
        } catch (IOException ignored) {
        }

        GalleryUploadResponse response = new GalleryUploadResponse();

        assert gallery != null;
        BeanUtils.copyProperties(gallery, response);
        response.setImageUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, gallery.getId()));
        response.setThumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, gallery.getId()));

        return response;
    }

    @ApiOperation(value = "사진 지움", response = EmptyJsonResponse.class)
    @RequestMapping(value = "/gallery/{id}", method = RequestMethod.DELETE)
    public EmptyJsonResponse removeImage(@PathVariable String id) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        AuthUserProfile authUserProfile = UserUtils.getAuthUserProfile();

        galleryService.removeImage(authUserProfile.getId(), id);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "사진 상세")
    @RequestMapping(value = "/gallery/{id}", method = RequestMethod.GET)
    public GalleryResponse view(
            @ApiParam(value = "Gallery ID", required = true) @PathVariable String id,
            HttpServletRequest request,
            HttpServletResponse response) {

        Boolean isAddCookie = ApiUtils.addViewsCookie(request, response, ApiConst.VIEWS_COOKIE_TYPE.GALLERY, id);

        Gallery gallery = galleryService.findOneById(id);

        if (isAddCookie)
            galleryService.increaseViews(gallery);

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        /*
        글 상세
         */
        GalleryDetail galleryDetail = new GalleryDetail();
        BeanUtils.copyProperties(gallery, galleryDetail);

        Integer numberOfLike = ObjectUtils.isEmpty(gallery.getUsersLiking()) ? 0 : gallery.getUsersLiking().size();
        Integer numberOfDisLike = ObjectUtils.isEmpty(gallery.getUsersDisliking()) ? 0 : gallery.getUsersDisliking().size();

        galleryDetail.setNumberOfLike(numberOfLike);
        galleryDetail.setNumberOfDislike(numberOfDisLike);
        galleryDetail.setImageUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, gallery.getId()));
        galleryDetail.setThumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, gallery.getId()));

        if (Objects.nonNull(commonWriter))
            galleryDetail.setMyFeeling(ApiUtils.getMyFeeling(commonWriter, gallery.getUsersLiking(), gallery.getUsersDisliking()));

        return GalleryResponse.builder()
          .gallery(galleryDetail)
//          .next((Gallery) gallery.get("next"))
//          .prev((Gallery) gallery.get("prev"))
//          .linkedPosts((List<BoardFreeSimple>) gallery.get("linkedPosts"))
          .build();
    }

    @ApiOperation(value = "사진 좋아요 싫어요")
    @RequestMapping(value = "/gallery/{id}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse setGalleryFeeling(@PathVariable String id, @PathVariable CoreConst.FEELING_TYPE feeling) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter writer = UserUtils.getCommonWriter();

        Map<String, Object> data = galleryService.setUserFeeling(writer, id, feeling);

        return UserFeelingResponse.builder()
          .myFeeling((CoreConst.FEELING_TYPE) data.get("feeling"))
          .numberOfLike((Integer) data.get("numberOfLike"))
          .numberOfDislike((Integer) data.get("numberOfDislike"))
          .build();
    }
}
