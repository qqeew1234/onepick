package per.yun.onepick.service

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import per.yun.onepick.domain.model.*
import per.yun.onepick.pipeline.AsyncDataPipeline

@Service
class ProductCatalogService(
    private val pipeline: AsyncDataPipeline
) {

    @PostConstruct
    fun initCatalog() {
        val initialProducts = getInitialProductDataset()
        val initialReviews = getInitialProductReviews()
        pipeline.processAndIndexBatch(initialProducts, initialReviews)
    }

    fun getInitialProductDataset(): List<Product> {
        return listOf(
            // 1. [SPEC 메인 샘플] 원룸 가성비 무선청소기 Hero Pick
            Product(
                id = "PRD-VC-2026-001",
                name = "슬림 파워 무선청소기 V10 Lite",
                brand = "테크라이프",
                category = "무선청소기",
                price = 189000,
                priceCeiling = 220000,
                originalPrice = 239000,
                imageUrl = "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-vc-001",
                prescriptiveReason = "동급 대비 최경량(1.2kg) 및 원룸 마루 흡입력 최적화",
                nonTargetReason = "실평수 25평 이상 거주자(배터리 용량 부족) 및 카펫 위주 거주 환경",
                isOutOfStock = false,
                isLinkAlive = true,
                tags = setOf("무선청소기", "청소기", "원룸", "가성비", "자취", "흡입력", "경량", "저소음"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 96.5,
                complaintRate = 0.8,
                communityOrganicMentions = 580,
                promotionalBlogCount = 1,
                reviewCount = 4200,
                oneLineReasons = mapOf(
                    "원룸" to "동급 대비 최경량(1.2kg) 및 원룸 마루 흡입력 최적화",
                    "가성비" to "10만원대 유일의 150AW 실측 흡입력과 1.2kg 초경량 조합",
                    "자취" to "좁은 원룸 틈새 거치 완벽 지원 및 1인 가구 3초 청소 종결",
                    "저소음" to "야간 원룸 층간소음 걱정 없는 68dB 에코 모드 탑재"
                ),
                defaultOneLineReason = "동급 대비 최경량(1.2kg) 및 원룸 마루 흡입력 최적화",
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 219000, "오픈마켓A"),
                    PriceChartPointDto("2026-08-15", 215000, "스마트스토어B"),
                    PriceChartPointDto("2026-09-01", 205000, "오픈마켓B"),
                    PriceChartPointDto("2026-09-20", 199000, "이커머스D"),
                    PriceChartPointDto("2026-10-10", 195000, "오픈마켓A"),
                    PriceChartPointDto("2026-10-24", 189000, "오픈마켓C (최저가)")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("suction", "실측 흡입력", "150", "AW"),
                        KeyMetricDto("weight", "본체 무게", "1.2", "kg"),
                        KeyMetricDto("runtime", "일반 사용 시간", "40", "분")
                    ),
                    verifiedPros = listOf(
                        "1.2kg 초경량 무게로 손목 회전 및 천장/틈새 청소 시 피로도 제로",
                        "마룻바닥 미세먼지 및 반려동물 털 흡입 성능 동급 1위 실측 검증",
                        "벽걸이 타공 없이 자립 가능한 스탠드 거치대 기본 동봉"
                    ),
                    verifiedCons = listOf(
                        "터보 모드 가동 시 배터리 지속 시간이 8분으로 급감",
                        "먼지통 용량이 0.35L로 2~3회 청소 후 비움 필요",
                        "두꺼운 러그/카펫 위에서는 브러시 회전 저항 발생"
                    ),
                    specSections = listOf(
                        SpecSectionDto(
                            sectionName = "성능 및 배터리",
                            items = listOf(
                                SpecItemDto("실측 흡입력", "150 AW"),
                                SpecItemDto("일반 모드 지속시간", "40 분"),
                                SpecItemDto("터보 모드 지속시간", "8 분"),
                                SpecItemDto("완충 소요 시간", "3.5 시간"),
                                SpecItemDto("배터리 용량", "2,500 mAh (리튬이온)")
                            )
                        ),
                        SpecSectionDto(
                            sectionName = "물리 규격 및 위생",
                            items = listOf(
                                SpecItemDto("본체 무게", "1.2 kg"),
                                SpecItemDto("먼지통 용량", "0.35 L"),
                                SpecItemDto("필터 규격", "H13 헤파 필터 (물세척 가능)"),
                                SpecItemDto("소음도", "에코 68dB / 터보 78dB"),
                                SpecItemDto("살균 기능", "- (데이터 미제공)") // EC-03 결측치 표기
                            )
                        ),
                        SpecSectionDto(
                            sectionName = "보증 및 사후지원",
                            items = listOf(
                                SpecItemDto("모터 무상보증", "10년 무상보증"),
                                SpecItemDto("배터리 무상보증", "1년 무상교환"),
                                SpecItemDto("KC 안전인증", "R-R-TL-V10LITE")
                            )
                        )
                    )
                ),
                officialCatalog = OfficialCatalogDto(
                    sourceCatalogUrl = "https://techlife.co.kr/catalog/v10lite.pdf",
                    rawImages = listOf(
                        "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=800&auto=format&fit=crop&q=80",
                        "https://images.unsplash.com/photo-1527515637462-cff94eecc1ac?w=800&auto=format&fit=crop&q=80"
                    )
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-VC-2026-002",
                        productName = "베이직 청소기 에어",
                        brand = "샤오미 라이프",
                        thumbnailUrl = "https://images.unsplash.com/photo-1563453392212-326f5e854473?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 119000,
                        differentiatingReason = "흡입력은 100AW로 낮으나 가격이 7만 원 저렴",
                        affiliateUrl = "https://link.onepick.kr/out/prd-vc-002"
                    ),
                    AlternativeDto(
                        type = "HIGH_END",
                        typeLabel = "상위 성능 대안",
                        productId = "PRD-VC-2026-003",
                        productName = "맥스프로 사일런트 200",
                        brand = "다이슨 테크",
                        thumbnailUrl = "https://images.unsplash.com/photo-1527515637462-cff94eecc1ac?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 349000,
                        differentiatingReason = "배터리 탈착식 및 가동시간 60분, 소음 65dB 이하 극저소음",
                        affiliateUrl = "https://link.onepick.kr/out/prd-vc-003"
                    )
                )
            ),

            // 1-1. 가성비 대안 모델 등록
            Product(
                id = "PRD-VC-2026-002",
                name = "베이직 청소기 에어",
                brand = "샤오미 라이프",
                category = "무선청소기",
                price = 119000,
                priceCeiling = 140000,
                imageUrl = "https://images.unsplash.com/photo-1563453392212-326f5e854473?w=500&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-vc-002",
                prescriptiveReason = "10만원 초반대 기본기에 충실한 초경량 원룸용 무선청소기",
                nonTargetReason = "대형 평수 및 딥클리닝(매트리스/침구) 용도로는 파워 부족",
                tags = setOf("무선청소기", "청소기", "가성비", "자취", "원룸"),
                viralScore = 0.0,
                realBuyerSentimentScore = 91.0,
                complaintRate = 1.8,
                communityOrganicMentions = 290,
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("suction", "실측 흡입력", "100", "AW"),
                        KeyMetricDto("weight", "본체 무게", "1.1", "kg"),
                        KeyMetricDto("runtime", "일반 사용 시간", "30", "분")
                    ),
                    verifiedPros = listOf("11만원대 압도적 가격", "1.1kg 가벼운 핸들링"),
                    verifiedCons = listOf("흡입력 다소 약함", "충전 시간 4.5시간"),
                    specSections = emptyList()
                )
            ),

            // 1-2. 상위 성능 대안 모델 등록
            Product(
                id = "PRD-VC-2026-003",
                name = "맥스프로 사일런트 200",
                brand = "다이슨 테크",
                category = "무선청소기",
                price = 349000,
                priceCeiling = 400000,
                imageUrl = "https://images.unsplash.com/photo-1527515637462-cff94eecc1ac?w=500&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-vc-003",
                prescriptiveReason = "배터리 탈착식 및 60분 가동, 65dB 이하 극저소음 하이엔드",
                nonTargetReason = "단순 원룸 거주자에게는 과스펙 및 가격 부담",
                tags = setOf("무선청소기", "청소기", "하이엔드", "저소음", "프리미엄"),
                viralScore = 0.0,
                realBuyerSentimentScore = 95.8,
                complaintRate = 0.9,
                communityOrganicMentions = 450,
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("suction", "실측 흡입력", "200", "AW"),
                        KeyMetricDto("weight", "본체 무게", "1.8", "kg"),
                        KeyMetricDto("runtime", "일반 사용 시간", "60", "분")
                    ),
                    verifiedPros = listOf("탈착식 배터리로 연속 사용 가능", "65dB 정숙성"),
                    verifiedCons = listOf("1.8kg로 약간 묵직함", "가격대 30만원 중반"),
                    specSections = emptyList()
                )
            ),

            // 2. [모니터 카테고리] 개발자 / 4K / 가성비 모니터
            Product(
                id = "PRD-MN-2026-001",
                name = "27인치 4K UHD IPS 프로 디스플레이 27UP850N",
                brand = "LG전자",
                category = "모니터",
                price = 449000,
                priceCeiling = 520000,
                originalPrice = 529000,
                imageUrl = "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-mn-001",
                prescriptiveReason = "맥북 C타입 90W PD 충전과 텍스트 가독성 최상 4K IPS 종결",
                nonTargetReason = "FPS 고주사율 게이밍(144Hz 이상) 목적 사용자에게는 60Hz로 부적합",
                tags = setOf("모니터", "4K", "고해상도", "개발자", "가성비", "IPS", "맥북"),
                viralScore = 0.0,
                realBuyerSentimentScore = 98.1,
                complaintRate = 0.6,
                communityOrganicMentions = 920,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 519000, "공식몰"),
                    PriceChartPointDto("2026-09-01", 489000, "오픈마켓A"),
                    PriceChartPointDto("2026-10-01", 469000, "오픈마켓B"),
                    PriceChartPointDto("2026-10-24", 449000, "오픈마켓C (최저가)")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("resolution", "해상도", "3840x2160", "4K UHD"),
                        KeyMetricDto("panel", "패널 종류", "IPS", ""),
                        KeyMetricDto("pd_charge", "USB-C 충전", "90", "W")
                    ),
                    verifiedPros = listOf(
                        "단일 C타입 케이블로 화면 출력 + 90W 맥북 동시 고속 충전 지원",
                        "코딩 및 텍스트 작업 시 눈 피로도 최저 수준의 안티글레어 및 선명한 폰트",
                        "피벗(세로회전), 높낮이 조절이 가능한 견고한 순정 스탠드"
                    ),
                    verifiedCons = listOf(
                        "주사율이 60Hz로 고주사율 게이밍에는 아쉬움",
                        "내장 스피커 음질이 평이하여 외장 스피커 권장"
                    ),
                    specSections = listOf(
                        SpecSectionDto(
                            sectionName = "디스플레이 규격",
                            items = listOf(
                                SpecItemDto("화면 크기", "68.4 cm (27인치)"),
                                SpecItemDto("해상도", "3840 x 2160 (4K UHD)"),
                                SpecItemDto("색 영역", "DCI-P3 95% (CIE1976)"),
                                SpecItemDto("밝기", "400 cd/m² (VESA DisplayHDR 400)")
                            )
                        ),
                        SpecSectionDto(
                            sectionName = "연결성 및 부가기능",
                            items = listOf(
                                SpecItemDto("USB-C 포트", "90W Power Delivery 지원"),
                                SpecItemDto("스탠드 기능", "틸트, 높낮이, 피벗 지원"),
                                SpecItemDto("색온도 보정센서", "- (데이터 미제공)") // 결측치
                            )
                        )
                    )
                ),
                officialCatalog = OfficialCatalogDto(
                    sourceCatalogUrl = "https://www.lge.co.kr/monitors/27up850n",
                    rawImages = listOf(
                        "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&auto=format&fit=crop&q=80"
                    )
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-MN-2026-002",
                        productName = "27인치 QHD 75Hz 아이케어 모니터",
                        brand = "필립스",
                        thumbnailUrl = "https://images.unsplash.com/photo-1586210579191-33b45e38fa2c?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 249000,
                        differentiatingReason = "해상도는 QHD이나 가격이 20만원 저렴하며 가독성 우수",
                        affiliateUrl = "https://link.onepick.kr/out/prd-mn-002"
                    ),
                    AlternativeDto(
                        type = "HIGH_END",
                        typeLabel = "상위 성능 대안",
                        productId = "PRD-MN-2026-003",
                        productName = "32인치 4K UHD 나노IPS 블랙 32UQ85R",
                        brand = "LG전자",
                        thumbnailUrl = "https://images.unsplash.com/photo-1547119957-637f8679db1e?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 799000,
                        differentiatingReason = "32인치 대화면 및 명암비 2배 나노IPS Black 패널 탑재",
                        affiliateUrl = "https://link.onepick.kr/out/prd-mn-003"
                    )
                )
            ),

            // 3. [로봇청소기] 올인원 / 자취 / 가성비
            Product(
                id = "PRD-RC-2026-001",
                name = "S8 Pro Ultra 올인원 로봇청소기",
                brand = "로보락",
                category = "로봇청소기",
                price = 1490000,
                priceCeiling = 1690000,
                originalPrice = 1690000,
                imageUrl = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-rc-001",
                prescriptiveReason = "물걸레 자동세척부터 온풍건조까지 손댈 필요 없는 올인원 완전 자동화 종결자",
                nonTargetReason = "문턱 높이 2.5cm 이상 거주지 및 복층 단독주택 환경에는 단독 운영 불가",
                tags = setOf("로봇청소기", "물걸레", "하이엔드", "프리미엄", "올인원"),
                viralScore = 0.0,
                realBuyerSentimentScore = 98.5,
                complaintRate = 0.4,
                communityOrganicMentions = 1200,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 1650000, "오픈마켓"),
                    PriceChartPointDto("2026-09-01", 1580000, "라이브커머스"),
                    PriceChartPointDto("2026-10-01", 1520000, "공식스토어"),
                    PriceChartPointDto("2026-10-24", 1490000, "최저가몰")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("suction", "실측 흡입력", "6000", "Pa"),
                        KeyMetricDto("mop_vibration", "물걸레 진동수", "3000", "회/분"),
                        KeyMetricDto("docking", "도크 올인원", "세척/건조/비움", "")
                    ),
                    verifiedPros = listOf(
                        "손댈 필요 없는 온풍 물걸레 건조로 걸레 쉰내 제로 검증",
                        "3D 구조광 센서로 바닥 전선 및 양말 완벽 우회 회피",
                        "카펫 진입 시 오토 리프팅으로 카펫 젖음 원천 차단"
                    ),
                    verifiedCons = listOf(
                        "스테이션 부피가 커서 가로 50cm 공간 확보 필수",
                        "초기 구매 비용 140만원대로 고가"
                    ),
                    specSections = listOf(
                        SpecSectionDto(
                            sectionName = "청소 및 센서 스펙",
                            items = listOf(
                                SpecItemDto("흡입력", "6,000 Pa"),
                                SpecItemDto("센서 종류", "LDS 라이다 + 3D 구조광 카메라"),
                                SpecItemDto("도크 기능", "자동먼지비움, 물걸레세척, 열풍건조")
                            )
                        )
                    )
                ),
                officialCatalog = OfficialCatalogDto(
                    sourceCatalogUrl = "https://roborock.co.kr",
                    rawImages = listOf("https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=800&auto=format&fit=crop&q=80")
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-RC-2026-002",
                        productName = "스마트 네비 LiDAR 로봇청소기 E10",
                        brand = "샤오미",
                        thumbnailUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 289000,
                        differentiatingReason = "자동 세척은 없으나 라이다 센서 탑재로 120만원 절약",
                        affiliateUrl = "https://link.onepick.kr/out/prd-rc-002"
                    )
                )
            ),

            // 4. [무선이어폰] 출퇴근 / 노이즈캔슬링
            Product(
                id = "PRD-EP-2026-001",
                name = "WF-1000XM5 무선 노이즈캔슬링 이어폰",
                brand = "소니",
                category = "무선이어폰",
                price = 319000,
                priceCeiling = 359000,
                imageUrl = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-ep-001",
                prescriptiveReason = "지하철/버스 소음 99% 차단과 폼팁 기본 장착으로 출퇴근 집중력 극대화",
                nonTargetReason = "귓구멍이 작아 폼팁 압박감에 민감한 사용자에게는 착용감 호불호 발생",
                tags = setOf("무선이어폰", "이어폰", "노이즈캔슬링", "출퇴근", "하이엔드", "음질"),
                viralScore = 0.0,
                realBuyerSentimentScore = 97.4,
                complaintRate = 0.6,
                communityOrganicMentions = 890,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 359000, "정가"),
                    PriceChartPointDto("2026-09-01", 339000, "오픈마켓"),
                    PriceChartPointDto("2026-10-24", 319000, "최저가")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("anc", "노이즈캔슬링", "듀얼 V2+QN2e", ""),
                        KeyMetricDto("battery", "배터리 수명", "8+16", "시간"),
                        KeyMetricDto("codec", "고음질 코덱", "LDAC", "")
                    ),
                    verifiedPros = listOf("지하철 중저음 소음 완벽 상쇄", "LDAC 무손실 음질"),
                    verifiedCons = listOf("케이스 표면 유광 재질로 스크래치 취약"),
                    specSections = emptyList()
                ),
                officialCatalog = OfficialCatalogDto(
                    sourceCatalogUrl = "https://sony.co.kr",
                    rawImages = listOf("https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=800&auto=format&fit=crop&q=80")
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-EP-2026-002",
                        productName = "버즈 FE 무선 노이즈캔슬링 이어폰",
                        brand = "삼성전자",
                        thumbnailUrl = "https://images.unsplash.com/photo-1606220588913-b3aacb4d2f46?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 74000,
                        differentiatingReason = "7만원대 가격에 안정적인 ANC와 분실 부담 없는 윙팁 장착",
                        affiliateUrl = "https://link.onepick.kr/out/prd-ep-002"
                    )
                )
            ),

            // 5. [바이럴 격리 대상 샘플: 협찬/광고 바이럴 어뷰징 감지 모델]
            Product(
                id = "VC-VIRAL-001",
                name = "블링클린 맥스 파워청소기 (어뷰징 감지 모델)",
                brand = "협찬사X",
                category = "무선청소기",
                price = 219000,
                priceCeiling = 250000,
                imageUrl = "https://images.unsplash.com/photo-1563453392212-326f5e854473?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://link.onepick.kr/out/prd-viral",
                tags = setOf("무선청소기", "가성비", "자취"),
                viralScore = 85.0,
                isQuarantined = true,
                quarantineReason = "체험단 100건 집중 배포 및 '소정의 원고료 지원' 블로그 도배 감지",
                realBuyerSentimentScore = 61.2,
                complaintRate = 12.8,
                communityOrganicMentions = 15,
                promotionalBlogCount = 180,
                reviewCount = 600
            ),

            // 6. [동적 대체(Fallback) 테스트 전용 상품]
            // 가격 상한선을 초과하여 상한선 위반으로 즉시 Fallback 되는 상품
            Product(
                id = "PRD-SPIKE-001",
                name = "인기 급상승 프리미엄 청소기 (상한선 초과 샘플)",
                brand = "하이엔드랩",
                category = "무선청소기",
                price = 299000,
                priceCeiling = 240000, // 상한선 24만원인데 현재가 29.9만원 -> Fallback 트리거
                imageUrl = "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=500&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-spike",
                prescriptiveReason = "강력한 모터와 흡입력 제공",
                tags = setOf("단독급상승", "급상승"),
                viralScore = 0.0,
                realBuyerSentimentScore = 99.9,
                communityOrganicMentions = 5000,
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-VC-2026-001",
                        productName = "슬림 파워 무선청소기 V10 Lite",
                        brand = "테크라이프",
                        currentPrice = 189000,
                        differentiatingReason = "상한선 초과된 기존 상품 대비 11만원 저렴하며 원룸 최적화",
                        affiliateUrl = "https://link.onepick.kr/out/prd-vc-001"
                    )
                )
            )
        )
    }

    private fun getInitialProductReviews(): Map<String, List<String>> {
        return mapOf(
            "VC-VIRAL-001" to listOf(
                "업체로부터 소정의 원고료를 지원받아 작성된 후기입니다.",
                "무상 제공받았지만 정말 강추합니다 ㅋㅋㅋㅋㅋ",
                "체험단 후기: 너무 좋아요 대박 대박 대박!"
            )
        )
    }
}
