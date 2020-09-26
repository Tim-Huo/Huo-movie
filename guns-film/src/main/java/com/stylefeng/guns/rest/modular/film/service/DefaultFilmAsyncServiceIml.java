package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.film.FilmAsyncServiceApi;
import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.api.film.vo.FilmDescVO;
import com.stylefeng.guns.api.film.vo.ImgVO;
import com.stylefeng.guns.rest.common.persistence.dao.MoocActorTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocFilmInfoTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocActorT;
import com.stylefeng.guns.rest.common.persistence.model.MoocFilmInfoT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 异步调用接口实现
 * @author: Tim·Huo
 * @created: 2020/09/26 07:12
 */
@Component
@Service(interfaceClass = FilmAsyncServiceApi.class)
public class DefaultFilmAsyncServiceIml implements FilmAsyncServiceApi {

    @Autowired
    private MoocFilmInfoTMapper moocFilmInfoTMapper;

    @Autowired
    private MoocActorTMapper moocActorTMapper;


    private MoocFilmInfoT getFilmInfo(String filmId) {
        MoocFilmInfoT moocFilmInfoT = new MoocFilmInfoT();
        moocFilmInfoT.setFilmId(filmId);
        moocFilmInfoT = moocFilmInfoTMapper.selectOne(moocFilmInfoT);

        return moocFilmInfoT;
    }

    /**
     * 获取影片描述信息
     *
     * @auther: Tim·Huo
     * @param: filmId
     * @return: FilmDescVO
     * @date: 2020/9/26 7:16 上午
     */
    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setBiography(moocFilmInfoT.getBiography());
        filmDescVO.setFilmId(filmId);

        return filmDescVO;
    }

    /**
     * 获取图片信息
     *
     * @auther: Tim·Huo
     * @param: filmId
     * @return: ImgVO
     * @date: 2020/9/26 7:16 上午
     */
    @Override
    public ImgVO getImg(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        String filmImgStr = moocFilmInfoT.getFilmImgs();
        // 图片地址以逗号分隔的链接URL
        String[] filImgs = filmImgStr.split(",");
        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(filImgs[0]);
        imgVO.setImg01(filImgs[1]);
        imgVO.setImg02(filImgs[2]);
        imgVO.setImg03(filImgs[3]);
        imgVO.setImg04(filImgs[4]);

        return imgVO;
    }

    /**
     * 获取导演信息
     *
     * @auther: Tim·Huo
     * @param: filmId
     * @return: ActorVO
     * @date: 2020/9/26 7:17 上午
     */
    @Override
    public ActorVO getDectInfo(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        // 获取导演编号
        Integer directorId = moocFilmInfoT.getDirectorId();
        MoocActorT moocActorT = moocActorTMapper.selectById(directorId);
        ActorVO actorVO = new ActorVO();
        actorVO.setImgAddress(moocActorT.getActorImg());
        actorVO.setDirectorName(moocActorT.getActorName());

        return actorVO;
    }

    /**
     * 获取演员信息
     *
     * @auther: Tim·Huo
     * @param: filmId
     * @return: ActorVO
     * @date: 2020/9/26 7:17 上午
     */
    @Override
    public List<ActorVO> getActors(String filmId) {
        List<ActorVO> actors = moocActorTMapper.getActors(filmId);

        return actors;
    }
}
