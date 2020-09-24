package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.user.film.FilmServiceApi;
import com.stylefeng.guns.api.user.film.vo.*;
import com.stylefeng.guns.api.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Tim·Huo
 * @created: 2020/09/23 20:51
 */
@Component
@Service(interfaceClass = FilmServiceApi.class)
public class DefaultFilmServiceImpl implements FilmServiceApi {

    @Autowired
    private MoocBannerTMapper moocBannerTMapper;

    @Autowired
    private MoocFilmTMapper moocFilmTMapper;

    @Autowired
    private MoocCatDictTMapper moocCatDictTMapper;

    @Autowired
    private MoocYearDictTMapper moocYearDictTMapper;

    @Autowired
    private MoocSourceDictTMapper moocSourceDictTMapper;

    /**
     * 获取banner信息
     *
     * @auther: Tim·Huo
     * @return: BannerVO
     * @date: 2020/9/23 9:15 下午
     */
    @Override
    public List<BannerVO> getBanners() {

        List<MoocBannerT> moocBanners = moocBannerTMapper.selectList(null);
        List<BannerVO> result = new ArrayList<>();
        for (MoocBannerT moocBanner : moocBanners) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerUrl(moocBanner.getBannerUrl());
            bannerVO.setBannerId(moocBanner.getUuid() + "");
            bannerVO.setBannerAddress(moocBanner.getBannerAddress());
            result.add(bannerVO);
        }
        return result;

    }

    /**
     * 获取正在热映的电影
     *
     * @auther: Tim·Huo
     * @param: isLimit
     * @param: nums
     * @param: nowPage
     * @param: sortId
     * @param: sourceId
     * @param: yearId
     * @param: catId
     * @return: FilmVO
     * @date: 2020/9/23 9:54 下午
     */
    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();

        // 热映影片的限制条件，影片状态,1-正在热映，2-即将上映，3-经典影片
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        if(isLimit) {
            // 如果是，则限制条数，限制内容为热映影片
            Page<MoocFilmT> page = new Page<>(1, nums);
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            //组织filmInfos
            filmInfos = getFilmInfos(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            filmVO.setFilmInfo(filmInfos);
        }

        return null;
    }

    private List<FilmInfo> getFilmInfos(List<MoocFilmT> moocFilms) {
        List<FilmInfo> filmInfos = new ArrayList<>();
        for (MoocFilmT moocFilmT : moocFilms) {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setShowTime(DateUtil.getDay(moocFilmT.getFilmTime()));
            filmInfo.setScore(moocFilmT.getFilmScore());
            filmInfo.setImgAddress(moocFilmT.getImgAddress());
            filmInfo.setFilmType(moocFilmT.getFilmType());
            filmInfo.setFilmScore(moocFilmT.getFilmScore());
            filmInfo.setFilmName(moocFilmT.getFilmName());
            filmInfo.setFilmId(moocFilmT.getUuid() + "");
            filmInfo.setExpectNum(moocFilmT.getFilmPresalenum());
            filmInfo.setBoxNum(moocFilmT.getFilmBoxOffice());
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }

    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        return null;
    }

    /**
     * 获取即将上映的电影[受欢迎程度做排序]
     *
     * @auther: Tim·Huo
     * @param: isLimit
     * @param: nums
     * @param: nowPage
     * @param: sortId
     * @param: sourceId
     * @param: yearId
     * @param: catId
     * @return: FilmVO
     * @date: 2020/9/24 6:32 上午
     */
    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVo = new FilmVO();
        List<FilmInfo> filmInfos;
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        // 即将上映影片的限制条件，影片状态,1-正在热映，2-即将上映，3-经典影片
        entityWrapper.eq("film_status", "2");
        // 判断是否是首页需要的内容
        if (isLimit) {
            // 如果是，则限制条数，限制内容为即将上映影片
            Page<MoocFilmT> page = new Page<>(1, nums);
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            // 组织FilmInfos
            filmInfos = getFilmInfos(moocFilms);
            filmVo.setFilmNum(moocFilms.size());
            filmVo.setFilmInfo(filmInfos);
        }
        return null;
    }
    /**
     * 获取票房排行榜
     *
     * @auther: Tim·Huo
     * @return: FilmInfo
     * @date: 2020/9/24 6:35 上午
     */
    @Override
    public List<FilmInfo> getBoxRanking() {
        // 默认条件：正在上映的，票房前十名
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_box_office");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    /**
     * 获取受欢迎的榜单（人气排行榜）
     *
     * @auther: Tim·Huo
     * @return: FilmInfo
     * @date: 2020/9/24 6:44 上午
     */
    @Override
    public List<FilmInfo> getExpectRanking() {
        // 默认条件 -> 即将上映，预售前十名
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_preSaleNum");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    /**
     * 获取前10经典影片
     *
     * @auther: Tim·Huo
     * @return: FilmInfo
     * @date: 2020/9/24 6:46 上午
     */
    @Override
    public List<FilmInfo> getTop() {
        // 条件 -> 正在上映的，评分前10名
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "3");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_score");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    /**
     * 查询分类
     *
     * @auther: Tim·Huo
     * @return: CatVO
     * @date: 2020/9/24 3:10 下午
     */
    @Override
    public List<CatVO> getCats() {
        List<CatVO> catVOs = new ArrayList<>();
        // 通过查询实体对象 -MoocCatDictT
        List<MoocCatDictT> moocCats = moocCatDictTMapper.selectList(null);
        //将实体对象转换为业务对象 -CatVO
        for (MoocCatDictT moocCat : moocCats) {
            CatVO catVO = new CatVO();
            catVO.setCatName(moocCat.getShowName());
            catVO.setCatId(moocCat.getUuid() + "");
            catVOs.add(catVO);
        }
        return catVOs;
    }

    /**
     * 查询片源
     *
     * @auther: Tim·Huo
     * @return: SourceVO
     * @date: 2020/9/24 3:11 下午
     */
    @Override
    public List<SourceVO> getSources() {
        List<SourceVO> sourceVOS = new ArrayList<>();
        // 通过查询实体对象 -MoocSourceDictT
        List<MoocSourceDictT> moocSourceDictTS = moocSourceDictTMapper.selectList(null);
        //将实体对象转换为业务对象 -SourceVO
        for (MoocSourceDictT moocSource : moocSourceDictTS) {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceName(moocSource.getShowName());
            sourceVO.setSourceId(moocSource.getUuid() + "");
            sourceVOS.add(sourceVO);
        }
        return sourceVOS;
    }

    /**
     *
     * 查询年代
     * @auther: Tim·Huo
     * @return: YearVO
     * @date: 2020/9/24 3:12 下午
     */
    @Override
    public List<YearVO> getYears() {
        List<YearVO> yearVOS = new ArrayList<>();
        // 通过查询实体对象 -MoocYearDictT
        List<MoocYearDictT> moocYears = moocYearDictTMapper.selectList(null);
        //将实体对象转换为业务对象 -YearVO
        for (MoocYearDictT moocYear : moocYears) {
            YearVO yearVO = new YearVO();
            yearVO.setYearName(moocYear.getShowName());
            yearVO.setYearId(moocYear.getUuid() + "");
            yearVOS.add(yearVO);
        }
        return yearVOS;
    }


}
