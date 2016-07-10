package com.jakduk.restcontroller.gallery;

import com.jakduk.common.CommonConst;
import com.jakduk.exception.SuccessButNoContentException;
import com.jakduk.model.db.Gallery;
import com.jakduk.model.simple.GalleryOnList;
import com.jakduk.service.CommonService;
import com.jakduk.service.GalleryService;
import com.jakduk.restcontroller.vo.GalleriesResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by pyohwan on 16. 3. 20.
 */

@RestController
@RequestMapping("/api")
public class GalleryRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private CommonService commonService;

    @Autowired
    private GalleryService galleryService;

    // 사진 목록
    @RequestMapping(value = "/galleries", method = RequestMethod.GET)
    public GalleriesResponse getGalleries(@RequestParam(required = false) String id,
                             @RequestParam(required = false, defaultValue = "0") int size,
                             HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (size < CommonConst.GALLERY_SIZE) size = CommonConst.GALLERY_SIZE;

        List<GalleryOnList> galleries = galleryService.getGalleriesById(id, size);

        if (Objects.isNull(galleries))
            throw new SuccessButNoContentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));

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

    // 사진 올리기.
    @RequestMapping(value = "/gallery", method = RequestMethod.POST)
    public Gallery uploadImage(@RequestParam(required = true) MultipartFile file,
                            HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (file.isEmpty()) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));
        }

        Gallery gallery = galleryService.uploadImage(locale, file);

        return gallery;
    }

    // 사진 삭제.
    @RequestMapping(value = "/gallery/{id}", method = RequestMethod.DELETE)
    public void removeImage(@PathVariable String id,
                            HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        galleryService.removeImage(locale, id);
    }

}
