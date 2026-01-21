package com.viridial.feature.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridial.feature.services.FeatureService;

@RestController
@RequestMapping("/features")
public class FeatureResource {
    @Autowired
    private FeatureService featureService;

}

