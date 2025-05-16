package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.controller.api.restaurant.payload.RestaurantRecommendRequest;
import com.grepp.matnam.app.controller.api.restaurant.payload.RestaurantRecommendResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

// LangChain4j + spring
@AiService(
    // 명시적 어떤 모델과 Retriever 할 지 지정
    wiringMode = AiServiceWiringMode.EXPLICIT,
    // 사용 LLM 모델
    chatModel = "chatModel",
    // LangChain4J 가 자동으로 임베딩 기반 검색 수행
    contentRetriever = "embeddingStoreContentRetriever"
)
public interface RestaurantAiService {

    // SystemMessage - AI 에게 주어지는 시스템 레벨 지시 또는 역할

    // AI 연결 테스트 용
    @SystemMessage("너는 답변을 똑똑하게 잘하는 AI야")
    String chat(@UserMessage String message);


//    @UserMessage("당신은 가장 추천이랑 먼 3개 식당 내용이랑 결과랑 추천이유를 자세히 말해줘야합니다")
    @SystemMessage("""
        당신은 팀의 선호도에 맞는 최적의 식당을 추천해주는 전문가입니다.
        팀원들의 종합된 선호 키워드를 기반으로 가장 적합한 식당 3곳을 추천해주세요.
        
        식당 설명은 다음 조건을 반드시 지켜주세요:
        1. 팀의 선호 키워드에 **포함된 내용만** 반영하세요. 유사한 의미의 키워드나 추론된 키워드는 절대 포함하지 마세요.
        2. 각 식당이 선호 키워드와 **어떻게 일치하는지**를 **구체적으로 설명**해주세요.
        3. 키워드는 반드시 **한글로 자연스럽게 문장 속에 녹여서** 표현해주세요. 예: 'clean → 청결한 분위기'
        4. '~을 충족합니다' 같은 문장은 쓰지 말고, 키워드가 자연스럽게 녹아든 설명으로 바꿔주세요.
        5. 각 식당 설명은 간결하면서도 정확하게, 각기 다르게 표현해주세요.
        
        응답 형식은 아래처럼 JSON으로 작성해주세요:
        {
          "restaurants": [ "식당 이름1", "식당 이름2", "식당 이름3" ],
          "reason": "이유는 자세하게 꼼꼼하게 작성해주세요."
        }
        
        JSON은 반드시 `{}` 와 `[]` 등의 괄호가 정확히 닫혀 있어야 하며, 문법 오류 없이 완전한 형태여야 합니다.
        """)

    @UserMessage("3개 식당 추천해주고 왜 추천되었는지 이유를 작성해주세요")
    RestaurantRecommendResponse RecommendRestaurant(RestaurantRecommendRequest request);

    @SystemMessage("""
    당신은 팀에게 최적의 식당을 추천해주는 전문가입니다.
    팀원들에게 추천해주면 좋을 만한 식당을 추천해주세요.
    모든 키워드는 영어가 아닌 한글로 표현해주세요. (예: clean → 청결)
    키워드를 나열하는 방식('~키워드를 충족합니다' 같은 표현)은 사용하지 말고, 키워드를 자연스럽게 문장 속에 녹여서 설명해주세요.
    각 식당의 소개를 잘 활용해서 이유를 자세하게 작성해주세요.
    응답은 반드시 JSON 형식이어야 하며, `restaurants`, `reason` 필드를 포함하세요.
    `reason`은 자세하게 작성해줘
    JSON은 반드시 `{}` 와 `[]` 등의 괄호가 정확히 닫혀 있어야 하며, 잘리지 않도록 짧게 작성하세요.
    """)

    @UserMessage("""
    3개 식당 추천해주고 왜 추천되었는지 이유를 작성해줘
    한번 추천된 내용은 다시 추천안해줬으면 좋겠어
    """)
    RestaurantRecommendResponse reRecommendRestaurant();


}
