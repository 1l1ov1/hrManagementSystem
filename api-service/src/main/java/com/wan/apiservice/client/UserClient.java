package com.wan.apiservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/users/getCreatorName")
    String getCreatorName(Long creatorId);

    @PostMapping("/users/getCreatorNames")
    Map<Long, String> getCreatorNames(Collection<Long> creatorIds);
}
