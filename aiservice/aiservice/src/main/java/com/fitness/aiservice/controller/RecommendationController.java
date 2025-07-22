package com.fitness.aiservice.controller;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations") // Base URI
public class RecommendationController {

    private final RecommendationService recommendationService;

    // ✅ Get recommendations for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendation(@PathVariable String userId) {
        List<Recommendation> recommendations = recommendationService.getUserRecommendation(userId);
        return ResponseEntity.ok(recommendations);
    }

    // ✅ Get recommendation for a specific activity
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendation(@PathVariable String activityId) {
        Recommendation recommendation = recommendationService.getActivityRecommendation(activityId);
        return ResponseEntity.ok(recommendation);
    }
}
