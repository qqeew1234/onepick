# AGENTS.md — ONE-PICK

선택의 피로를 원천 차단하고 입력 조건에 부합하는 **단 하나의 정답(Hero Pick)**만 도출하는 결정 지정형 커머스 플랫폼.
상세 기획 및 화면 사양은 [spec_prd.md](file:///C:/Users/qqeew/Projects/onepick/spec_prd.md), [spec_design.md](file:///C:/Users/qqeew/Projects/onepick/spec_design.md)를 따른다.

---

## 1. Environment & Tech Stack

| 구분 | 사양 | 설정 파일 |
| :--- | :--- | :--- |
| **JDK (런타임/빌드)** | **Java 25** (Amazon Corretto 25) | `build.gradle.kts` (`jvmToolchain(25)`), `.idea/misc.xml` |
| **Build Tool** | Gradle 9.8.0 (Wrapper `gradlew.bat` / `gradlew`) | `gradle/wrapper/gradle-wrapper.properties` |
| **Backend Framework** | Spring Boot 3.4.3 (`spring-boot-starter-web`) | `build.gradle.kts` |
| **Language** | Kotlin 2.1.10 (`jvm`, `plugin.spring`) | `build.gradle.kts` |
| **Async & Streaming** | Kotlin Coroutines Core 1.10.1 (`kotlinx-coroutines-core`) | `build.gradle.kts` |
| **NLP & Morphology** | Apache Lucene Nori 9.12.0 (`lucene-analysis-nori`) | `build.gradle.kts` |
| **Serialization** | Jackson Module Kotlin 2.18.2 | `build.gradle.kts` |
| **Test Framework** | JUnit 5 Platform, Spring Boot Test, MockMvc | `src/test/` |
| **Frontend** | Vanilla HTML5 / Modern CSS3 / Vanilla ES6+ JS (외부 번들러·NPM 배제) | `src/main/resources/static/` |
| **Web Typography** | Pretendard Variable Font (공식 jsDelivr CDN) | `src/main/resources/static/index.html` |
| **Storage & State** | In-Memory 전용 (재시작 시 초기화, 외부 DB 종속 없음) | `DeterministicSearchEngine` |
| **Port & Profiles** | `http://localhost:8080` | `application.properties` |

---

## 2. Commands

```bash
./gradlew bootRun   # 애플리케이션 실행 → http://localhost:8080
./gradlew test      # 단위/통합 테스트 검증 (커밋 전 필수)
```

---

## 3. Project Structure

```
src/main/kotlin/per/yun/onepick/
├── controller/     # SearchController (/prescriptive, /top-picks, /feedback), AntiViralController, PipelineController
├── domain/model/   # Product, PrescriptiveSearchModels (DTO, 가격분석, 동적대체 판정 모델)
├── engine/
│   ├── search/     # DeterministicSearchEngine (Sub-10ms 인메모리 단일 정답/대안 산출)
│   ├── morphology/ # KoreanMorphologyParser (Lucene Nori), TagDictionary
│   └── antiviral/  # AntiViralEngine (협찬·어뷰징 바이럴 사전 격리)
├── pipeline/       # AsyncDataPipeline (Kotlin Channel 기반 비동기 인덱싱)
└── service/        # ProductCatalogService (더미 카탈로그), SearchPrescriptionService
src/main/resources/static/
├── index.html      # 시맨틱 마크업 (GNB, 홈, Hero Pick, 가격차트, 이원화스펙, 대안, Top-Picks, 로그인모달)
├── style.css       # Pretendard 기반 반응형 디자인 토큰 CSS
└── app.js          # Canvas 가격 차트 렌더러, 탭 스위처, 실시간 검색 비동기 스크립트
```

---

## 4. Core Rules & Constraints

1. **단일 정답(Hero Pick) 필수**:
   - 검색 결과는 반드시 **단 1개의 Hero Pick**과 **최대 2개의 대안(`alternatives`: 가성비 1개, 상위성능 1개)**만 반환한다.
   - 모든 정답에는 `prescriptiveReason`(1줄 선정 사유)과 `nonTargetReason`(비추천 대상 역타깃 고지)이 반드시 포함되어야 한다.
2. **수익 이해상충 격리 (Revenue-Blind)**:
   - 랭킹 산출에 제휴 수수료율 데이터를 절대 참조하지 않는다.
   - 바이럴 감지(`viralScore > 0` 또는 `isQuarantined == true`) 상품은 검색 인덱스에서 영구 배제한다.
3. **동적 대체 (Dynamic Fallback, `FN-FALLBACK-01`)**:
   - 품절(`isOutOfStock`), 링크 소멸(`!isLinkAlive`), 가격 상한선 초과(`price > priceCeiling`) 시 1순위 대안 상품을 즉시 Hero Pick으로 승격한다.
4. **가격 적정성 판정 (FN-PRICE-01)**:
   - 90일 최저가의 5% 이내 `ALL_TIME_LOW`, 90일 평균가의 5% 이내 `FAIR_PRICE`, 90일 평균가 5% 초과 인상 시 `PRICE_SPIKE` 경고를 출력한다.
5. **홈 화면 첫 접속 UX**:
   - 첫 진입 시 특정 제품이 자동 검색되어 상세 페이지가 노출되지 않도록 유지하며, 검색창과 카테고리별 상위 1위 대표 라인업(`top-picks`)을 노출한다.
6. **Zero-Dependency Frontend**:
   - 가벼운 로딩 속도를 위해 외부 React/Vue/NPM 번들러를 임의 도입하지 않고 표준 바닐라 웹 스택을 유지한다.
7. **테스트 검증 의무**:
   - 코드 수정 후 반드시 `./gradlew test`를 실행하여 통과를 확인하고, 신규 기능 추가 시 `src/test/kotlin`에 검증 테스트를 작성한다.
