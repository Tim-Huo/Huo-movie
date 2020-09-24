package com.stylefeng.guns.rest.modular.film.vo;

import com.stylefeng.guns.api.user.film.vo.CatVO;
import com.stylefeng.guns.api.user.film.vo.SourceVO;
import com.stylefeng.guns.api.user.film.vo.YearVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: Tim·Huo
 * @created: 2020/09/24 15:20
 */
@Data
public class FilmConditionVO implements Serializable {

    private List<CatVO> catInfo;

    private List<SourceVO> sourceInfo;

    private List<YearVO> yearInfo;
}
