package com.stylefeng.guns.rest.modular.film;

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

    @GetMapping("getIndex")
    public ResponseVO getIndex() {


        return null;
    }

}
