package com.lt.service;

import com.lt.dto.InventoryLogDto;
import com.lt.vo.DataGridView;

/**
 * @Author: Mr.Tian
 */
public interface InventoryLogService {
  /**
   * 分页查询
   *
   * @param inventoryLogDto
   * @return
   */
  DataGridView listInventoryLogPage(InventoryLogDto inventoryLogDto);
}
