package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaConditionResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldsResponseVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/cinema/")
public class CinemaController {

    //cache = "lru" ： 启动dubbo本地结果缓存
    @Reference(interfaceClass = CinemaServiceAPI.class, cache = "lru", connections = 10, check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    /**
     * 查询影院列表
     *
     * @auther: Tim·Huo
     * @param: cinemaQueryVO
     * @return: ResponseVo
     * @date: 2020/9/26 8:39 下午
     */
    @RequestMapping("getCinemas")
    public ResponseVo getCinemas(CinemaQueryVO cinemaQueryVO) {

        try{
            // 获取三个集合，然后封装成一个对象返回即可
            List<BrandVO> brands = cinemaServiceAPI.getBrands(cinemaQueryVO.getBrandId());
            List<AreaVO> areas = cinemaServiceAPI.getAreas(cinemaQueryVO.getDistrictId());
            List<HallTypeVO> hallTypes = cinemaServiceAPI.getHallTypes(cinemaQueryVO.getHallType());

            CinemaConditionResponseVO cinemaConditionResponseVO = new CinemaConditionResponseVO();
            cinemaConditionResponseVO.setAreaList(areas);
            cinemaConditionResponseVO.setBrandList(brands);
            cinemaConditionResponseVO.setHalltypeList(hallTypes);

            return ResponseVo.success(cinemaConditionResponseVO);
        }catch (Exception e) {
            log.error("获取条件列表失败", e);
            return ResponseVo.serviceFail("获取影院查询条件失败");
        }
    }

    /**
     * 获取影院列表的查询条件
     *
     * @auther: Tim·Huo
     * @param: cinemaQueryVO
     * @return: ResponseVo
     * @date: 2020/9/26 8:44 下午
     */
    @RequestMapping("getCondition")
    public ResponseVo getCondition(CinemaQueryVO cinemaQueryVO){
        try{
            // 获取三个集合，然后封装成一个对象返回即可
            List<BrandVO> brands = cinemaServiceAPI.getBrands(cinemaQueryVO.getBrandId());
            List<AreaVO> areas = cinemaServiceAPI.getAreas(cinemaQueryVO.getDistrictId());
            List<HallTypeVO> hallTypes = cinemaServiceAPI.getHallTypes(cinemaQueryVO.getHallType());

            CinemaConditionResponseVO cinemaConditionResponseVO = new CinemaConditionResponseVO();
            cinemaConditionResponseVO.setAreaList(areas);
            cinemaConditionResponseVO.setBrandList(brands);
            cinemaConditionResponseVO.setHalltypeList(hallTypes);

            return ResponseVo.success(cinemaConditionResponseVO);
        }catch (Exception e) {
            log.error("获取条件列表失败", e);
            return ResponseVo.serviceFail("获取影院查询条件失败");
        }
    }

    /**
     * 获取播放场次
     *
     * @auther: Tim·Huo
     * @param: cinemaId
     * @return: ResponseVo
     * @date: 2020/9/26 8:45 下午
     */
    @RequestMapping("getFields")
    public ResponseVo getFields(Integer cinemaId){
        try{

            CinemaInfoVO cinemaInfoById = cinemaServiceAPI.getCinemaInfoById(cinemaId);

            List<FilmInfoVO> filmInfoByCinemaId = cinemaServiceAPI.getFilmInfoByCinemaId(cinemaId);

            CinemaFieldsResponseVO cinemaFieldResponseVO = new CinemaFieldsResponseVO();
            cinemaFieldResponseVO.setCinemaInfo(cinemaInfoById);
            cinemaFieldResponseVO.setFilmList(filmInfoByCinemaId);

            return ResponseVo.success(IMG_PRE,cinemaFieldResponseVO);
        }catch (Exception e){
            log.error("获取播放场次失败",e);
            return ResponseVo.serviceFail("获取播放场次失败");
        }
    }

    /**
     * 获取场次详细信息
     *
     * @auther: Tim·Huo
     * @param: cinemaId 影院编号
     * @param: fieldId  播放场次的编号
     * @return: ResponseVo
     * @date: 2020/9/26 8:46 下午
     */
    @PostMapping("getFieldInfo")
    public ResponseVo getFieldInfo(Integer cinemaId,Integer fieldId){
        try{

            CinemaInfoVO cinemaInfoById = cinemaServiceAPI.getCinemaInfoById(cinemaId);
            FilmInfoVO filmInfoByFieldId = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);
            HallInfoVO filmFieldInfo = cinemaServiceAPI.getFilmFieldInfo(fieldId);

            // 造几个销售的假数据，后续会对接订单接口
            filmFieldInfo.setSoldSeats("1,2,3");

            CinemaFieldResponseVO cinemaFieldResponseVO = new CinemaFieldResponseVO();
            cinemaFieldResponseVO.setCinemaInfo(cinemaInfoById);
            cinemaFieldResponseVO.setFilmInfo(filmInfoByFieldId);
            cinemaFieldResponseVO.setHallInfo(filmFieldInfo);

            return ResponseVo.success(IMG_PRE,cinemaFieldResponseVO);
        }catch (Exception e){
            log.error("获取选座信息失败",e);
            return ResponseVo.serviceFail("获取选座信息失败");
        }
    }

}
