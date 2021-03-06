package com.zxp.demo.feign;

import com.zxp.api.account.AccountAPI;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name = "account-service", url = "127.0.0.1:8083")
public interface AccountFeignClient extends AccountAPI {
}
