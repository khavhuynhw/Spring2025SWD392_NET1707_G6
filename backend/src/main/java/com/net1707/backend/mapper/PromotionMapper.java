package com.net1707.backend.mapper;

import com.net1707.backend.dto.PromotionDTO;
import com.net1707.backend.model.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper extends BaseMapper<PromotionDTO, Promotion>{
    @Override
    public PromotionDTO toDto(Promotion promotion) {
        return PromotionDTO.builder()
                .promotionId(promotion.getPromotionId())
                .promotionName(promotion.getPromotionName())
                .description(promotion.getDescription())
                .discountPercentage(promotion.getDiscountPercentage())
                .minimumAmount(promotion.getMinimumAmount())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .build();
    }

    @Override
    public Promotion toEntity(PromotionDTO dto) {
        return Promotion.builder()
                .promotionName(dto.getPromotionName())
                .description(dto.getDescription())
                .discountPercentage(dto.getDiscountPercentage())
                .minimumAmount(dto.getMinimumAmount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }
}
