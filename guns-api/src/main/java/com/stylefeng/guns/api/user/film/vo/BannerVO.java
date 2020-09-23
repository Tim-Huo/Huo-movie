package com.stylefeng.guns.api.user.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 横幅
 * @author: Tim·Huo
 * @created: 2020/09/23 16:48
 */
@Data
public class BannerVO implements Serializable {

    private String bannerId;

    private String bannerAddress;

    private String bannerUrl;
}
