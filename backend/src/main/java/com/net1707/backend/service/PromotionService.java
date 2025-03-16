package com.net1707.backend.service;

import com.net1707.backend.dto.PromotionDTO;
import com.net1707.backend.mapper.PromotionMapper;
import com.net1707.backend.model.Promotion;
import com.net1707.backend.repository.PromotionRepository;
import com.net1707.backend.service.Interface.IPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService implements IPromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Override
    public List<PromotionDTO> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PromotionDTO getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + id));
        return promotionMapper.toDto(promotion);
    }

    @Override
    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {
        Promotion promotion = Promotion.builder()
                .promotionName(promotionDTO.getPromotionName())
                .description(promotionDTO.getDescription())
                .discountPercentage(promotionDTO.getDiscountPercentage())
                .minimumAmount(promotionDTO.getMinimumAmount())
                .startDate(promotionDTO.getStartDate())
                .endDate(promotionDTO.getEndDate())
                .build();


        return promotionMapper.toDto(promotionRepository.save(promotion));
    }

    @Override
    public PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + id));

        promotion.setPromotionName(promotionDTO.getPromotionName());
        promotion.setDescription(promotionDTO.getDescription());
        promotion.setDiscountPercentage(promotionDTO.getDiscountPercentage());
        promotion.setMinimumAmount(promotionDTO.getMinimumAmount());
        promotion.setStartDate(promotionDTO.getStartDate());
        promotion.setEndDate(promotionDTO.getEndDate());

        return promotionMapper.toDto(promotionRepository.save(promotion));
    }

    @Override
    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + id));
        promotionRepository.delete(promotion);
    }
}
