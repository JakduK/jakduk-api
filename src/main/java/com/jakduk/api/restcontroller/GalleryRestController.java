package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.annotation.SecuredUser;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.UrlGenerationUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.gallery.*;
import com.jakduk.api.restcontroller.vo.user.SessionUser;
import com.jakduk.api.service.GalleryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 사진첩 API
 *
 * @author pyohwan
 * 16. 3. 20 오후 11:17
 */

@RequestMapping("/api")
@RestController
public class GalleryRestController {

    @Autowired private UrlGenerationUtils urlGenerationUtils;
    @Autowired private GalleryService galleryService;

    // 사진 목록
    @GetMapping("/galleries")
    public GalleriesResponse getGalleries(
            @RequestParam(required = false) String id, // 이 ID 이후부터 목록 가져옴
            @RequestParam(required = false, defaultValue = "0") Integer size // 페이지 사이즈
    ) {

        if (size < Constants.GALLERY_SIZE) size = Constants.GALLERY_SIZE;

        List<GalleryOnList> galleries = galleryService.getGalleries(id, size);

        return new GalleriesResponse(galleries);
    }

    // 사진 올리기
    @SecuredUser
    @PostMapping("/gallery")
    public GalleryUploadResponse uploadImage(@RequestParam MultipartFile file) throws IOException {

        if (file.isEmpty())
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        String contentType = file.getContentType();

        if (! StringUtils.startsWithIgnoreCase(contentType, "image/"))
            throw new ServiceException(ServiceError.FILE_ONLY_IMAGE_TYPE_CAN_BE_UPLOADED);

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        Gallery gallery = galleryService.uploadImage(commonWriter, file.getOriginalFilename(), file.getSize(),
                contentType, file.getBytes());

        GalleryUploadResponse response = new GalleryUploadResponse();

        BeanUtils.copyProperties(gallery, response);
        response.setImageUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.LARGE, gallery.getId()));
        response.setThumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()));

        return response;
    }

    // 사진 지움
    @DeleteMapping("/gallery/{id}")
    public EmptyJsonResponse removeImage(
            @PathVariable String id, // 사진 ID"
            @RequestBody(required = false) LinkedItemForm form,
            HttpServletRequest request) {

        if (! AuthUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        SessionUser sessionUser = AuthUtils.getAuthUserProfile();

        galleryService.deleteGallery(id, sessionUser.getId());

        // form이 null이 아니면 글, 댓글 편집시 호출 했기 때문에 gallery를 바로 지우면 안된다. 글/댓글 편집 완료 시 실제로 gallery를 지워야 한다.
        // session에 저장해 두자.
        if (Objects.nonNull(form))
            JakdukUtils.setSessionOfGalleryIdsForRemoval(request, form.getFrom(), form.getItemId(), id);

        return EmptyJsonResponse.newInstance();
    }

    // 사진 상세
    @GetMapping("/gallery/{id}")
    public GalleryResponse view(
            @PathVariable String id) {

        return galleryService.getGalleryDetail(id);
    }

}
