package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.PromotionDTO;

import java.util.List;

public interface IPromotionService {
    List<PromotionDTO> getAllPromotions();
    PromotionDTO getPromotionById(Long id);
    PromotionDTO createPromotion(PromotionDTO promotionDTO);
    PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO);
    void deletePromotion(Long id);
}
