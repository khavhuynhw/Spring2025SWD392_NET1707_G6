package com.net1707.backend.service;

import com.net1707.backend.model.*;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.ProductRepository;
import com.net1707.backend.repository.QuestionBankRepository;
import com.net1707.backend.repository.QuizResultRepository;
import com.net1707.backend.service.Interface.IQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService implements IQuizService {
    private final QuestionBankRepository questionBankRepository;
    private final QuizResultRepository quizResultRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public QuizService(QuestionBankRepository questionBankRepository, QuizResultRepository quizResultRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.questionBankRepository = questionBankRepository;
        this.quizResultRepository = quizResultRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public QuizResult processQuizResponses(Customer customer, Map<String, String> responses) {
        SkinType skinType = determineSkinType(responses);
        Set<SkinConcern> skinConcerns = determineSkinConcerns(responses);
        if (skinType == null) {
            skinType = SkinType.NORMAL;
        }
        QuizResult quizResult = QuizResult.builder()
                .customer(customer)
                .responses(responses)
                .recommendedSkinType(skinType)
                .recommendedConcerns(skinConcerns)
                .build();

        updateCustomerProfile(customer, skinType, skinConcerns);

        return quizResultRepository.save(quizResult);
    }

    @Override
    public SkinType determineSkinType(Map<String, String> responses) {
        if (responses.containsKey("oiliness") &&
                (responses.get("oiliness").equals("very_oily") || responses.get("oiliness").equals("oily"))) {

            if (responses.containsKey("dryness") &&
                    (responses.get("dryness").equals("dry") || responses.get("dryness").equals("very_dry"))) {
                return SkinType.COMBINATION;
            } else {
                return SkinType.OILY;
            }
        }


        if (responses.containsKey("dryness") &&
                (responses.get("dryness").equals("very_dry") || responses.get("dryness").equals("dry"))) {
            return SkinType.DRY;
        }


        if (responses.containsKey("sensitivity") &&
                (responses.get("sensitivity").equals("high") || responses.get("sensitivity").equals("medium"))) {
            return SkinType.SENSITIVE;
        }


        return SkinType.NORMAL;
    }

    @Override
    public Set<SkinConcern> determineSkinConcerns(Map<String, String> responses) {
        Set<SkinConcern> concerns = new HashSet<>();

        // Acne concern
        if (responses.containsKey("acne") &&
                responses.get("acne").equals("yes")) {
            concerns.add(SkinConcern.ACNE);
        }

        // Aging concern
        if (responses.containsKey("aging") &&
                (responses.get("aging").equals("visible") || responses.get("aging").equals("concerned"))) {
            concerns.add(SkinConcern.AGING);
        }

        // Hyperpigmentation concern
        if (responses.containsKey("hyperpigmentation") &&
                (responses.get("hyperpigmentation").equals("yes") || responses.get("hyperpigmentation").equals("noticeable"))) {
            concerns.add(SkinConcern.HYPERPIGMENTATION);
        }

        // Redness concern
        if (responses.containsKey("redness") &&
                (responses.get("redness").equals("yes") || responses.get("redness").equals("frequent"))) {
            concerns.add(SkinConcern.REDNESS);
        }

        // Dryness concern
        if (responses.containsKey("dryness") &&
                (responses.get("dryness").equals("yes") || responses.get("dryness").equals("persistent"))) {
            concerns.add(SkinConcern.DRYNESS);
        }

        // Oiliness concern
        if (responses.containsKey("oiliness") &&
                (responses.get("oiliness").equals("yes") || responses.get("oiliness").equals("excessive"))) {
            concerns.add(SkinConcern.OILINESS);
        }

        // Sensitivity concern
        if (responses.containsKey("sensitivity") &&
                (responses.get("sensitivity").equals("yes") || responses.get("sensitivity").equals("high"))) {
            concerns.add(SkinConcern.SENSITIVITY);
        }

        return concerns;
    }

    @Override
    public void updateCustomerProfile(Customer customer, SkinType skinType, Set<SkinConcern> skinConcerns) {
        customer.setPrimarySkinType(skinType);
        customer.setSkinConcerns(skinConcerns);
        customerRepository.save(customer);
    }

    @Override
    public List<Product> recommendProducts(QuizResult quizResult) {
        Customer customer = quizResult.getCustomer();
        List<Product> candidateProducts = new ArrayList<>(
                productRepository.findProductsBySkinType(quizResult.getRecommendedSkinType())
        );

        // Add products for each recommended skin concern
        for (SkinConcern concern : quizResult.getRecommendedConcerns()) {
            candidateProducts.addAll(
                    productRepository.findProductsBySkinConcern(concern)
            );
        }

        // Remove duplicates
        candidateProducts = candidateProducts.stream().distinct().collect(Collectors.toList());

        // Filter out products that are out of stock
        candidateProducts = candidateProducts.stream()
                .filter(p -> p.getStockQuantity() > 0)
                .collect(Collectors.toList());

        // Score each product and sort descending by the weighted score
        candidateProducts.sort((p1, p2) -> Double.compare(
                calculateProductScore(p2, quizResult, customer),
                calculateProductScore(p1, quizResult, customer)
        ));

        // Return top 5 recommendations
        return candidateProducts.stream().limit(5).collect(Collectors.toList());
    }

    private double calculateProductScore(Product product, QuizResult quizResult, Customer customer) {
        double score = 0.0;

        // Define weights (these values can be externalized or adjusted as needed)
        final double SKIN_TYPE_WEIGHT = 0.4;
        final double SKIN_CONCERN_WEIGHT = 0.3;
        final double AGE_WEIGHT = 0.15;
        final double CLIMATE_WEIGHT = 0.15;

        // Skin type match
        if (product.getSuitableSkinTypes().contains(quizResult.getRecommendedSkinType())) {
            score += SKIN_TYPE_WEIGHT;
        }

        // Skin concern matching: add fraction corresponding to number of matches
        if (quizResult.getRecommendedConcerns() != null && !quizResult.getRecommendedConcerns().isEmpty()) {
            int matches = 0;
            for (SkinConcern concern : quizResult.getRecommendedConcerns()) {
                if (product.getTargetsConcerns().contains(concern)) {
                    matches++;
                }
            }
            score += SKIN_CONCERN_WEIGHT * ((double) matches / quizResult.getRecommendedConcerns().size());
        }

        // Age compatibility check
        if (customer.getAgeRange() != null && product.getMinimumAgeRecommended() != null && product.getMaximumAgeRecommended() != null) {
            int customerAge = customer.getAgeRange();
            if (customerAge >= product.getMinimumAgeRecommended() && customerAge <= product.getMaximumAgeRecommended()) {
                score += AGE_WEIGHT;
            }
        }

        // Climate zone compatibility
        if (customer.getClimateZone() != null && product.getSuitableClimateZones() != null) {
            if (product.getSuitableClimateZones().contains(customer.getClimateZone())) {
                score += CLIMATE_WEIGHT;
            }
        }

        // Boost score for sensitive skin if applicable
        if (customer.getPrimarySkinType() == SkinType.SENSITIVE && Boolean.TRUE.equals(product.getSuitableForSensitiveSkin())) {
            score += 0.1; // Additional boost; adjust as needed
        }

        return score;
    }

    @Override
    public List<QuestionBank> getAllQuizQuestions() {
        return questionBankRepository.findAll();
    }
}
