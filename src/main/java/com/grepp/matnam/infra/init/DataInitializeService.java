package com.grepp.matnam.infra.init;

import com.grepp.matnam.app.model.restaurant.RestaurantEmbeddingRepository;
import com.grepp.matnam.app.model.restaurant.RestaurantRepository;
import com.grepp.matnam.app.model.restaurant.document.RestaurantEmbedding;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;

import dev.langchain4j.model.embedding.EmbeddingModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DataInitializeService {

    private final RestaurantRepository restaurantRepository;
    private final EmbeddingModel embeddingModel;
    private final RestaurantEmbeddingRepository restaurantEmbeddingRepository;

    // 프로젝트 시작 시 MongoDB 데이터 초기화
    @Transactional
    public void initializeVector() {

        // 데이터 존재시 실행 x
        if (restaurantEmbeddingRepository.count() > 0) {
            System.out.println("이미 임베딩이 초기화 되었습니다.");
            return;
        }

        // 식당에 있는 모든 데이터를 찾아서 embedding 한후 저장
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<RestaurantEmbedding> embeddings = restaurants
                                                .stream()
                                                .map(entity -> RestaurantEmbedding.fromEntity(entity,
                                                    embeddingModel))
                                                .toList();

        restaurantEmbeddingRepository.saveAll(embeddings);
        System.out.println("Persisting document embeddings...");
    }
}
