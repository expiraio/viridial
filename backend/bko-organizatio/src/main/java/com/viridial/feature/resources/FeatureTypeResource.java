package com.viridial.feature.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridial.feature.services.FeatureTypeService;

@RestController
@RequestMapping("/feature-types")
public class FeatureTypeResource {
    @Autowired
    private FeatureTypeService featureTypeService;

}

