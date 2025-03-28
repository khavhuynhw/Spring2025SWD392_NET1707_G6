package com.net1707.backend.service;

import com.net1707.backend.dto.response.QuizQuestionResponse;
import com.net1707.backend.model.*;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.ProductRepository;
import com.net1707.backend.repository.QuestionBankRepository;
import com.net1707.backend.repository.QuizResultRepository;
import com.net1707.backend.service.Interface.IQuizService;
import jakarta.persistence.EntityNotFoundException;
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
        int oilyScore = 0;
        int dryScore = 0;
        int sensitiveScore = 0;

        if (responses.containsKey(QuestionBank.QuestionType.OILINESS.toString())) {
            String oilinessResponse = responses.get(QuestionBank.QuestionType.OILINESS.toString());
            if ("very_oily".equals(oilinessResponse) || "oily".equals(oilinessResponse)) {
                oilyScore += 2;
            } else if ("slightly_oily".equals(oilinessResponse)) {
                oilyScore += 1;
            }
        }

        if (responses.containsKey(QuestionBank.QuestionType.DRYNESS.toString())) {
            String drynessResponse = responses.get(QuestionBank.QuestionType.DRYNESS.toString());
            if ("very_dry".equals(drynessResponse) || "dry".equals(drynessResponse)) {
                dryScore += 2;
            } else if ("slightly_dry".equals(drynessResponse)) {
                dryScore += 1;
            }
        }

        if (responses.containsKey(QuestionBank.QuestionType.SENSITIVITY.toString())) {
            String sensitivityResponse = responses.get(QuestionBank.QuestionType.SENSITIVITY.toString());
            if ("high".equals(sensitivityResponse) || "medium".equals(sensitivityResponse)) {
                sensitiveScore += 2;
            } else if ("low".equals(sensitivityResponse)) {
                sensitiveScore += 1;
            }
        }

        if (oilyScore >= 2 && dryScore >= 2) {
            return SkinType.COMBINATION;
        } else if (oilyScore >= 2) {
            return SkinType.OILY;
        } else if (dryScore >= 2) {
            return SkinType.DRY;
        } else if (sensitiveScore >= 2) {
            return SkinType.SENSITIVE;
        } else {
            return SkinType.NORMAL;
        }
    }

    @Override
    public Set<SkinConcern> determineSkinConcerns(Map<String, String> responses) {
        Set<SkinConcern> concerns = new HashSet<>();

        if (responses.containsKey(QuestionBank.QuestionType.ACNE.toString())) {
            String acneResponse = responses.get(QuestionBank.QuestionType.ACNE.toString());
            if ("yes".equals(acneResponse) || "severe".equals(acneResponse)) {
                concerns.add(SkinConcern.ACNE);
            }
        }

        if (responses.containsKey(QuestionBank.QuestionType.AGING.toString())) {
            String agingResponse = responses.get(QuestionBank.QuestionType.AGING.toString());
            if ("visible".equals(agingResponse) || "concerned".equals(agingResponse)) {
                concerns.add(SkinConcern.AGING);
            }
        }

        if (responses.containsKey(QuestionBank.QuestionType.HYPERPIGMENTATION.toString())) {
            String pigmentResponse = responses.get(QuestionBank.QuestionType.HYPERPIGMENTATION.toString());
            if ("yes".equals(pigmentResponse) || "noticeable".equals(pigmentResponse)) {
                concerns.add(SkinConcern.HYPERPIGMENTATION);
            }
        }

        if (responses.containsKey(QuestionBank.QuestionType.REDNESS.toString())) {
            String rednessResponse = responses.get(QuestionBank.QuestionType.REDNESS.toString());
            if ("yes".equals(rednessResponse) || "frequent".equals(rednessResponse)) {
                concerns.add(SkinConcern.REDNESS);
            }
        }

        if (responses.containsKey(QuestionBank.QuestionType.DRYNESS.toString())) {
            String drynessResponse = responses.get(QuestionBank.QuestionType.DRYNESS.toString());
            if ("very_dry".equals(drynessResponse) || "dry".equals(drynessResponse)) {
                concerns.add(SkinConcern.DRYNESS);
            }
        }

        if (responses.containsKey(QuestionBank.QuestionType.OILINESS.toString())) {
            String oilinessResponse = responses.get(QuestionBank.QuestionType.OILINESS.toString());
            if ("very_oily".equals(oilinessResponse) || "oily".equals(oilinessResponse)) {
                concerns.add(SkinConcern.OILINESS);
            }
        }

        if (responses.containsKey(QuestionBank.QuestionType.SENSITIVITY.toString())) {
            String sensitivityResponse = responses.get(QuestionBank.QuestionType.SENSITIVITY.toString());
            if ("high".equals(sensitivityResponse)) {
                concerns.add(SkinConcern.SENSITIVITY);
            }
        }

        return concerns;
    }

    @Override
    public Map<String, String> processUserResponses(List<QuizQuestionResponse> userResponses) {
        Map<String, String> formattedResponses = new HashMap<>();

        for (QuizQuestionResponse response : userResponses) {
            QuestionBank question = questionBankRepository.findById(response.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));

            formattedResponses.put(question.getType().toString(), response.getResponse());
        }

        return formattedResponses;
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
