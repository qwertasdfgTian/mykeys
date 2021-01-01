package com.lt.service;

import com.lt.domain.Medicines;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.dto.MedicinesDto;
import com.lt.vo.DataGridView;

import java.util.List;

public interface MedicinesService {

    /**
     * 分页查询
     *
     * @param medicinesDto
     * @return
     */
    DataGridView listMedicinesPage(MedicinesDto medicinesDto);

    /**
     * 根据ID查询
     *
     * @param medicinesId
     * @return
     */
    Medicines getOne(Long medicinesId);

    /**
     * 添加
     *
     * @param medicinesDto
     * @return
     */
    int addMedicines(MedicinesDto medicinesDto);

    /**
     * 修改
     *
     * @param medicinesDto
     * @return
     */
    int updateMedicines(MedicinesDto medicinesDto);

    /**
     * 根据ID删除
     *
     * @param medicinesIds
     * @return
     */
    int deleteMedicinesByIds(Long[] medicinesIds);

    /**
     * 查询所有可用生产厂家
     */
    List<Medicines> selectAllMedicines();

    /**
     * 调整库存
     */
    int updateMedicinesStorage(Long medicinesId,Long medicinesStockNum);

}
