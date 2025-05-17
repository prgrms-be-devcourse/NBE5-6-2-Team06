package com.grepp.matnam.app.model.restaurant.document;

import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "restaurants")
public class RestaurantEmbedding {

    @Id
    private String id;

    //키워드 + 설명 요약
    private String text;

    //text 임베딩 배열
    private float[] embedding;

    private String mood;

    // 식당 이름
    private String restaurantName;

    private String category;

    private String address;

    private String mainFood;

    private String openTime;

    //평점
    private Float googleRating;


    public RestaurantEmbedding(Restaurant entity, TextSegment segment, Embedding embedding) {
        this.id = String.valueOf(entity.getRestaurantId());
        this.restaurantName=entity.getName();
        this.category=entity.getCategory();
        this.mainFood=entity.getMainFood();
        this.googleRating=entity.getGoogleRating();
        this.openTime=entity.getOpenTime();
        this.address=entity.getAddress();

        this.text=segment.text();
        this.embedding=embedding.vector();
    }
    //문장으로 변환
    //식당 entity 받아서 -> 문장 변환 -> 임베딩 모델을 통해 벡터화
    public static RestaurantEmbedding fromEntity(Restaurant entity, EmbeddingModel model){
        String fullText = buildKeywordText(entity);
        TextSegment segment = TextSegment.from(fullText);
        Embedding embedding = model.embed(segment).content();
        return new RestaurantEmbedding(entity, segment, embedding);
    }
    public static String buildKeywordText(Restaurant entity){
        List<String> keywords = new ArrayList<>();
        if(entity.isGoodTalk()) keywords.add("goodTalk");
        if(entity.isClean()) keywords.add("clean");
        if(entity.isTerrace()) keywords.add("isTerrace");
        if(entity.isGoodMenu()) keywords.add("goodMenu");
        if(entity.isBigStore()) keywords.add("bigStore");
        if(entity.isGoodMusic()) keywords.add("goodMusic");
        if(entity.isLongStay()) keywords.add("longStay");
        if(entity.isManyDrink()) keywords.add("manyDrink");
        if(entity.isGoodPicture()) keywords.add("goodPicture");
        if(entity.isGoodView()) keywords.add("goodView");

        String keywordText = String.join(" ", keywords);

        return String.format(
            "식당: %s, 메인 음식: %s, 카테고리: %s, 키워드: [%s], 설명: %s, 구글 평점: %f",
            entity.getName(),
            entity.getMainFood(),
            entity.getCategory(),
            keywordText.isEmpty() ? "없음" : keywordText,
            entity.getSummary() == null ? "" : entity.getSummary(),
            entity.getGoogleRating()
        );
    }

    // 생성된 식당 객체에 대해 내부적으로 임베딩을 실행 -> 내부 텍스트 임베딩 하여 this.text, this.embedding 각각 저장
    public void embed(EmbeddingModel model) {
        TextSegment segment = TextSegment.from(this.toString());
        this.text = segment.text();
        Embedding embedding = model.embed(segment).content();
        this.embedding = embedding.vector();
    }
}
