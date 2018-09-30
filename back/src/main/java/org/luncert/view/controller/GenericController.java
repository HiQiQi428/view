package org.luncert.view.controller;

import javax.servlet.http.HttpServletResponse;

import org.luncert.simpleutils.JsonResult;
import org.luncert.view.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("generic")
public class GenericController {

    @Autowired
    ImageService imageService;

    @GetMapping("loadImage")
    public String loadImage(String picName, final HttpServletResponse response) {
        JsonResult JsonResult = imageService.loadImage(picName, response);
        if (JsonResult != null)
            return JsonResult.toString();
        else
            return null;
    }

}