package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.film.FilmServiceApi;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 影院模块
 * @author: Tim·Huo
 * @created: 2020/09/23 16:15
 */
@RestController
@RequestMapping("/film/")
public class FilmController {


    @Reference(interfaceClass = FilmServiceApi.class)
    private FilmServiceApi filmServiceApi;
    /*
    * API网关：
    *   1、功能聚合【API聚合】
    *   好处：
    *       1、六个接口1次请求，同一时刻节省了五次http请求
    *       2、同一个接口对外暴露，降低了前后端分离开发的难度和复杂度
    *   坏处：
    *       1、一次请求数据过多，容易出现问题
    *
    * */
    /**
     * 获取首页信息
     *
     * @auther: Tim·Huo
     * @return: ResponseVO
     * @date: 2020/9/24 6:58 上午
     */
    @GetMapping("getIndex")
    public ResponseVO getIndex() {
        FilmIndexVO filmIndexVO = new FilmIndexVO();
        // 获取banner信息
        filmIndexVO.setBanners(filmServiceApi.getBanners());

        // 获取正在热映的电影
        filmIndexVO.setHotFilms(filmServiceApi.getHotFilms(true, 8, 1, 99, 99, 99, 99));

        // 获取即将上映的电影
        filmIndexVO.setSoonFilms(filmServiceApi.getSoonFilms(true, 8, 1, 99, 99, 99, 99));

        // 获取票房排行榜
        filmIndexVO.setBoxRanking(filmServiceApi.getBoxRanking());

        // 获取受欢迎的榜单
        filmIndexVO.setExpectRanking(filmServiceApi.getExpectRanking());

        // 获取前一百
        filmIndexVO.setTop100(filmServiceApi.getTop());

        return ResponseVO.success(filmIndexVO);

    }

}
