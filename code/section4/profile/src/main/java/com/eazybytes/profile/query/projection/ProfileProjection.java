package com.eazybytes.profile.query.projection;

import com.eazybytes.profile.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("profile-group")
public class ProfileProjection {

    private final IProfileService iProfileService;

}
