package com.stylefeng.guns.api.cinema;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.vo.*;

import java.util.List;

public interface CinemaServiceAPI {

    /**
     * 根据CinemaQueryVO，查询影院列表
     *
     * @auther: Tim·Huo
     * @param: cinemaQueryVO
     * @return: CinemaVO
     * @date: 2020/9/26 9:06 下午
     */
    Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO);

    /**
     * 根据条件获取品牌列表[除了就99以外，其他的数字为isActive]，99是全部
     *
     * @auther: Tim·Huo
     * @param: brandId
     * @return: BrandVO
     * @date: 2020/9/26 9:06 下午
     */
    List<BrandVO> getBrands(int brandId);

    /**
     * 获取行政区域列表
     *
     * @auther: Tim·Huo
     * @param: areaId
     * @return: AreaVO
     * @date: 2020/9/26 9:07 下午
     */
    List<AreaVO> getAreas(int areaId);

    /**
     * 获取影厅类型列表
     *
     * @auther: Tim·Huo
     * @param: hallType
     * @return: HallTypeVO
     * @date: 2020/9/26 9:07 下午
     */
    List<HallTypeVO> getHallTypes(int hallType);

    /**
     * 根据影院编号，获取影院信息
     *
     * @auther: Tim·Huo
     * @param: cinemaId
     * @return: CinemaInfoVO
     * @date: 2020/9/26 9:07 下午
     */
    CinemaInfoVO getCinemaInfoById(int cinemaId);

    /**
     * 获取所有电影的信息和对应的放映场次信息，根据影院编号
     *
     * @auther: Tim·Huo
     * @param: cinemaId
     * @return: FilmInfoVO
     * @date: 2020/9/26 9:07 下午
     */
    List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId);

    /**
     * 根据放映场次ID获取放映信息
     *
     * @auther: Tim·Huo
     * @param: fieldId
     * @return: HallInfoVO
     * @date: 2020/9/26 9:07 下午
     */
    HallInfoVO getFilmFieldInfo(int fieldId);

    /**
     * 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     *
     * @auther: Tim·Huo
     * @param: fieldId 播放场次的编号
     * @return: FilmInfoVO
     * @date: 2020/9/26 9:08 下午
     */
    FilmInfoVO getFilmInfoByFieldId(int fieldId);

}
