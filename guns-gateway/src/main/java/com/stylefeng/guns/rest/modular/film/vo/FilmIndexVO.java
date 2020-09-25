package com.stylefeng.guns.rest.modular.film.vo;

import com.stylefeng.guns.api.film.vo.BannerVO;
import com.stylefeng.guns.api.film.vo.FilmInfo;
import com.stylefeng.guns.api.film.vo.FilmVO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Tim·Huo
 * @created: 2020/09/23 17:14
 */
@Data
public class FilmIndexVO {

    /**
     * banner信息
     */
    private List<BannerVO> banners;

    /**
     * 热门电影
     */
    private FilmVO hotFilms;

    /**
     * 即将上映
     */
    private FilmVO soonFilms;

    /**
     * 票房排行榜
     */
    private List<FilmInfo> boxRanking;

    /**
     * 受欢迎的榜单
     */
    private List<FilmInfo> expectRanking;

    /**
     * 前一百
     */
    private List<FilmInfo> top100;
}
