package com.jakduk.restcontroller;

import com.jakduk.model.db.Gallery;
import com.jakduk.service.CommonService;
import com.jakduk.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by pyohwan on 16. 3. 20.
 */

@RestController
@RequestMapping("/api/gallery")
public class GalleryRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private CommonService commonService;

    @Autowired
    private GalleryService galleryService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Gallery uploadImage(@RequestParam(required = true) MultipartFile file,
                            HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (file.isEmpty()) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));
        }

        Gallery gallery = galleryService.uploadImage(locale, file);

        return gallery;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void removeImage(@PathVariable String id,
                            HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        galleryService.removeImage(locale, id);
    }
}
