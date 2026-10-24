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
            // 1. [무선청소기] 슬림 파워 무선청소기 V10 Lite
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
                                SpecItemDto("살균 기능", "- (데이터 미제공)")
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

            // 1-1. 가성비 대안
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
                communityOrganicMentions = 290
            ),

            // 1-2. 상위 성능 대안
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
                communityOrganicMentions = 450
            ),

            // 2. [모니터] 27인치 4K UHD IPS 프로 디스플레이
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
                                SpecItemDto("스탠드 기능", "틸트, 높낮이, 피벗 지원")
                            )
                        )
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
                    )
                )
            ),

            // 3. [로봇청소기] S8 Pro Ultra 올인원 로봇청소기
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

            // 4. [무선이어폰] WF-1000XM5 노이즈캔슬링 이어폰
            Product(
                id = "PRD-EP-2026-001",
                name = "WF-1000XM5 무선 노이즈캔슬링 이어폰",
                brand = "소니",
                category = "무선이어폰",
                price = 319000,
                priceCeiling = 359000,
                originalPrice = 359000,
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

            // 5. [기계식키보드] 독거미 AULA F87 Pro 유무선 기계식 키보드
            Product(
                id = "PRD-KB-2026-001",
                name = "AULA F87 Pro 가스켓 유무선 기계식 키보드",
                brand = "AULA (독거미)",
                category = "기계식키보드",
                price = 54900,
                priceCeiling = 68000,
                originalPrice = 69000,
                imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-kb-001",
                prescriptiveReason = "가스켓 마운트와 저소음 피치축/그레이축으로 5만원대 타건감 및 폼떡 사운드 종결",
                nonTargetReason = "숫자패드(텐키) 입력이 필수적인 엑셀 회계/사무 직군 사용자",
                tags = setOf("기계식키보드", "키보드", "독거미", "가스켓", "가성비", "저소음", "블루투스"),
                viralScore = 0.0,
                realBuyerSentimentScore = 98.7,
                complaintRate = 0.5,
                communityOrganicMentions = 2100,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 64000, "직구대행"),
                    PriceChartPointDto("2026-09-01", 59000, "국내정발"),
                    PriceChartPointDto("2026-10-24", 54900, "정발특가")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("structure", "구조", "풀 가스켓 5단 흡음", ""),
                        KeyMetricDto("connection", "연결 방식", "블루투스/2.4G/유선", "3모드"),
                        KeyMetricDto("battery", "배터리 용량", "4,000", "mAh")
                    ),
                    verifiedPros = listOf(
                        "5만원대 가격에서 나올 수 없는 10~20만원대 커스텀 키보드급 조약돌 타건음",
                        "공장 윤활 완벽 적용으로 통울림 및 스프링 소음 원천 차단",
                        "대용량 배터리로 무선 LED OFF 기준 최대 3주 연속 사용"
                    ),
                    verifiedCons = listOf(
                        "텐키리스 배열로 숫자 키패드 부재",
                        "전용 소프트웨어 한글화 수준이 다소 미흡"
                    ),
                    specSections = listOf(
                        SpecSectionDto(
                            sectionName = "스위치 및 키캡",
                            items = listOf(
                                SpecItemDto("기본 스위치", "LEOBOG 저소음 피치축 v2 (리니어)"),
                                SpecItemDto("키캡 재질", "PBT 이중사출 체리 프로파일"),
                                SpecItemDto("핫스왑 지원", "3핀/5핀 전키 핫스왑 지원")
                            )
                        )
                    )
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "HIGH_END",
                        typeLabel = "상위 성능 대안",
                        productId = "PRD-KB-2026-002",
                        productName = "MX Mechanical 로우프로파일 무선 키보드",
                        brand = "로지텍",
                        thumbnailUrl = "https://images.unsplash.com/photo-1595225476474-87563907a212?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 179000,
                        differentiatingReason = "사무용 풀배열 지원 및 이지스위치 3대 기기 즉시 전환 완벽 호환",
                        affiliateUrl = "https://link.onepick.kr/out/prd-kb-002"
                    )
                )
            ),

            // 6. [헤어드라이기] 다이슨 슈퍼소닉 Origin HD08
            Product(
                id = "PRD-HD-2026-001",
                name = "슈퍼소닉 헤어드라이어 HD08 Origin",
                brand = "다이슨",
                category = "헤어드라이기",
                price = 429000,
                priceCeiling = 490000,
                originalPrice = 499000,
                imageUrl = "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-hd-001",
                prescriptiveReason = "110,000rpm 초고속 디지털 모터와 초당 40회 지능형 열제어로 모발 손상 없이 3분 건조",
                nonTargetReason = "단순 머리 건조 목적 대비 40만원대 구매 비용이 부담스러운 알뜰 사용자",
                tags = setOf("헤어드라이기", "드라이기", "모발보호", "하이엔드", "바람", "손상방지"),
                viralScore = 0.0,
                realBuyerSentimentScore = 98.2,
                complaintRate = 0.5,
                communityOrganicMentions = 1450,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 499000, "백화점정가"),
                    PriceChartPointDto("2026-09-01", 459000, "공식몰특가"),
                    PriceChartPointDto("2026-10-24", 429000, "라이브혜택가")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("motor", "모터 회전수", "110,000", "rpm"),
                        KeyMetricDto("temp_sensor", "온도 제어", "초당 40", "회 측정"),
                        KeyMetricDto("weight", "무게 밸런스", "중심 모터 설계", "")
                    ),
                    verifiedPros = listOf(
                        "손잡이에 모터가 있어 팔 피로감 없는 완벽한 무게중심",
                        "열 손상 없는 바람 건조로 탈색/염색 모발 윤기 보존 1위 검증",
                        "플라이어웨이 노즐로 잔머리 눌러주기 3초 완성"
                    ),
                    verifiedCons = listOf(
                        "비싼 가격대",
                        "전원 어댑터 플러그 부피가 큰 편"
                    ),
                    specSections = listOf(
                        SpecSectionDto(
                            sectionName = "바람 및 기술 사양",
                            items = listOf(
                                SpecItemDto("풍속 조절", "3단계 정밀 조절"),
                                SpecItemDto("풍온 조절", "4단계 정밀 조절 (28도~100도)"),
                                SpecItemDto("소비 전력", "1,600 W")
                            )
                        )
                    )
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-HD-2026-002",
                        productName = "에어샷 플라즈마 BLDC 드라이기",
                        brand = "유닉스",
                        thumbnailUrl = "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 139000,
                        differentiatingReason = "BLDC 항공모터 탑재로 30만원 절약하며 강력 풍속 유지",
                        affiliateUrl = "https://link.onepick.kr/out/prd-hd-002"
                    )
                )
            ),

            // 7. [공기청정기] LG 퓨리케어 360도 공기청정기 Hit
            Product(
                id = "PRD-AP-2026-001",
                name = "퓨리케어 360도 공기청정기 Hit",
                brand = "LG전자",
                category = "공기청정기",
                price = 289000,
                priceCeiling = 330000,
                originalPrice = 349000,
                imageUrl = "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-ap-001",
                prescriptiveReason = "18평 전방위 커버 360도 흡입과 극초미세먼지 99.999% 항균 H13 필터 탑재",
                nonTargetReason = "원룸 5평 이하 초소형 공간(본체 바닥 면적 약 30cm 차지)",
                tags = setOf("공기청정기", "미세먼지", "필터", "헤파필터", "가성비", "LG"),
                viralScore = 0.0,
                realBuyerSentimentScore = 97.9,
                complaintRate = 0.7,
                communityOrganicMentions = 730,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 339000, "공식몰"),
                    PriceChartPointDto("2026-09-01", 310000, "오픈마켓"),
                    PriceChartPointDto("2026-10-24", 289000, "특가몰")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("coverage", "청정 면적", "60", "㎡ (18평)"),
                        KeyMetricDto("filter", "필터 등급", "트루 헤파 99.999%", ""),
                        KeyMetricDto("sensor", "센서 정밀도", "PM 1.0 극초미세", "")
                    ),
                    verifiedPros = listOf(
                        "모든 방향에서 공기를 흡입해 사각지대 없는 빠른 공기 정화",
                        "LG ThinQ 앱 연동으로 귀가 전 원격 청정 및 실시간 공기질 모니터링",
                        "취침 모드 가동 시 팬 소음 23dB로 시계 초침보다 조용함"
                    ),
                    verifiedCons = listOf(
                        "순정 필터 교체 비용 6~7만원대 발생",
                        "바퀴(무빙휠)는 별도 구매 악세서리"
                    ),
                    specSections = emptyList()
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-AP-2026-002",
                        productName = "스마트 공기청정기 4 컴팩트",
                        brand = "샤오미",
                        thumbnailUrl = "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 79000,
                        differentiatingReason = "원룸 데스크 위에 둘 수 있는 소형 사이즈와 8만원 이하 가성비",
                        affiliateUrl = "https://link.onepick.kr/out/prd-ap-002"
                    )
                )
            ),

            // 8. [가습기] 조지루시 가열식 가습기 EE-DC50
            Product(
                id = "PRD-HM-2026-001",
                name = "밥솥형 가열식 안심 가습기 EE-DC50",
                brand = "조지루시",
                category = "가습기",
                price = 249000,
                priceCeiling = 280000,
                originalPrice = 289000,
                imageUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-hm-001",
                prescriptiveReason = "100도 끓여서 분사하는 완벽 멸균과 필터 없는 밥솥 구조로 통세척 10초 종결",
                nonTargetReason = "어린 유아가 뛰어다니는 가정(뜨거운 수증기 화상 주의 필수) 및 전기세 민감 가구",
                tags = setOf("가습기", "가열식", "가열식가습기", "통세척", "비염", "세균"),
                viralScore = 0.0,
                realBuyerSentimentScore = 98.6,
                complaintRate = 0.8,
                communityOrganicMentions = 1680,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 280000, "직구"),
                    PriceChartPointDto("2026-09-01", 265000, "정발특가"),
                    PriceChartPointDto("2026-10-24", 249000, "시즌최저가")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("heating", "가열 온도", "100", "℃ 멸균"),
                        KeyMetricDto("capacity", "물탱크 용량", "4.0", "L"),
                        KeyMetricDto("cleaning", "세척 방식", "필터리스 스테인리스 통세척", "")
                    ),
                    verifiedPros = listOf(
                        "물을 팔팔 끓여 가습하므로 세균/곰팡이 방출 원천 차단",
                        "밥솥 뚜껑 열고 닦으면 끝나는 압도적 관리 편의성 (복잡한 부품 제로)",
                        "방 안 온도가 훈훈해지는 자연 난방 효과"
                    ),
                    verifiedCons = listOf(
                        "초음파식 대비 소비전력(최대 985W 가열 시)이 높음",
                        "물 끓는 소리가 예민한 사람에게는 거슬릴 수 있음"
                    ),
                    specSections = emptyList()
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-HM-2026-002",
                        productName = "스마트 초음파 완벽분해 가습기",
                        brand = "미로",
                        thumbnailUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 99000,
                        differentiatingReason = "전기세가 거의 안 나오는 30W 초음파식이며 15만원 절약",
                        affiliateUrl = "https://link.onepick.kr/out/prd-hm-002"
                    )
                )
            ),

            // 9. [스마트워치] 갤럭시 워치7 44mm 블루투스
            Product(
                id = "PRD-SW-2026-001",
                name = "갤럭시 워치7 44mm 블루투스",
                brand = "삼성전자",
                category = "스마트워치",
                price = 329000,
                priceCeiling = 370000,
                originalPrice = 369000,
                imageUrl = "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-sw-001",
                prescriptiveReason = "3nm 최신 AP 탑재로 버벅임 제로 및 바이오액티브 센서 기반 정밀 수면/운동 코칭",
                nonTargetReason = "애플 아이폰 단독 사용자 (iOS 기기 연동 불가)",
                tags = setOf("스마트워치", "워치", "갤럭시워치", "수면", "헬스케어", "삼성"),
                viralScore = 0.0,
                realBuyerSentimentScore = 96.8,
                complaintRate = 0.9,
                communityOrganicMentions = 1120,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 369000, "출고가"),
                    PriceChartPointDto("2026-09-01", 345000, "카드할인가"),
                    PriceChartPointDto("2026-10-24", 329000, "온라인최저가")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("ap", "프로세서", "3nm 엑시노스 W1000", ""),
                        KeyMetricDto("gps", "GPS 규격", "이중 주파수 L1+L5", ""),
                        KeyMetricDto("sensor", "센서", "바이오액티브 2세대", "")
                    ),
                    verifiedPros = listOf(
                        "앱 실행 및 전환 속도가 전작 대비 3배 빨라져 체감 쾌적도 최상",
                        "수면 무호흡 조기 감지 및 실시간 혈관 건강 지수 분석",
                        "야외 러닝 시 빌딩 숲에서도 오차 없는 이중 주파수 GPS"
                    ),
                    verifiedCons = listOf(
                        "배터리 지속 시간이 1.5~2일로 매일/격일 충전 필수"
                    ),
                    specSections = emptyList()
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-SW-2026-002",
                        productName = "스마트 밴드 9 Pro",
                        brand = "샤오미",
                        thumbnailUrl = "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 64900,
                        differentiatingReason = "배터리가 14일 지속되며 26만원 저렴한 알림/운동 추적기",
                        affiliateUrl = "https://link.onepick.kr/out/prd-sw-002"
                    )
                )
            ),

            // 10. [커피머신] 네스프레소 버츄오 팝 캡슐커피머신
            Product(
                id = "PRD-CF-2026-001",
                name = "버츄오 팝 캡슐커피머신",
                brand = "네스프레소",
                category = "커피머신",
                price = 119000,
                priceCeiling = 149000,
                originalPrice = 149000,
                imageUrl = "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-cf-001",
                prescriptiveReason = "바코드 회전 추출 센트리퓨전 기술로 원터치 풍성한 벨벳 크레마와 머그잔 가득 홈카페",
                nonTargetReason = "호환 캡슐(스타벅스/마트용 오리지널 캡슐) 다양성을 최우선하는 사용자",
                tags = setOf("커피머신", "캡슐머신", "캡슐커피", "네스프레소", "홈카페", "가성비"),
                viralScore = 0.0,
                realBuyerSentimentScore = 97.2,
                complaintRate = 0.8,
                communityOrganicMentions = 980,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 149000, "정가"),
                    PriceChartPointDto("2026-09-01", 129000, "캡슐패키지"),
                    PriceChartPointDto("2026-10-24", 119000, "공식할인가")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("rpm", "회전 추출", "4,000", "rpm 센트리퓨전"),
                        KeyMetricDto("sizes", "추출 컵 사이즈", "4가지", "에스프레소~머그"),
                        KeyMetricDto("warmup", "예열 시간", "30", "초")
                    ),
                    verifiedPros = listOf(
                        "원버튼으로 바코드를 읽어 캡슐별 최적의 물 양과 온도 자동 세팅",
                        "일반 캡슐머신과 비교 불가능한 두껍고 쫀득한 크레마 층 형성",
                        "컴팩트하고 다채로운 팝 컬러로 좁은 주방 인테리어 포인트"
                    ),
                    verifiedCons = listOf(
                        "전용 버츄오 캡슐만 사용 가능 (호환 캡슐 선택폭 좁음)",
                        "물탱크 용량이 0.6L로 다소 작은 편"
                    ),
                    specSections = emptyList()
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-CF-2026-002",
                        productName = "에센자 미니 C30 오리지널 커피머신",
                        brand = "네스프레소",
                        thumbnailUrl = "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 99000,
                        differentiatingReason = "전 세계 모든 브랜드 호환 캡슐 사용 가능 및 초슬림 폭 8.4cm",
                        affiliateUrl = "https://link.onepick.kr/out/prd-cf-002"
                    )
                )
            ),

            // 11. [게이밍 마우스] 로지텍 G PRO X SUPERLIGHT 2
            Product(
                id = "PRD-MS-2026-001",
                name = "G PRO X SUPERLIGHT 2 (지슈라 2)",
                brand = "로지텍",
                category = "게이밍마우스",
                price = 179000,
                priceCeiling = 210000,
                originalPrice = 219000,
                imageUrl = "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=600&auto=format&fit=crop&q=80",
                affiliateUrl = "https://link.onepick.kr/out/prd-ms-001",
                prescriptiveReason = "60g 초경량과 HERO 2 센서, LIGHTFORCE 옵티컬 기계식 스위치로 무지연 e스포츠 종결",
                nonTargetReason = "무거운 마우스 그립감을 선호하거나 다버튼 매크로(MMO)가 필요한 게이머",
                tags = setOf("게이밍마우스", "마우스", "지슈라", "무선마우스", "초경량", "로지텍"),
                viralScore = 0.0,
                realBuyerSentimentScore = 98.9,
                complaintRate = 0.4,
                communityOrganicMentions = 2400,
                priceHistory = listOf(
                    PriceChartPointDto("2026-08-01", 209000, "정가"),
                    PriceChartPointDto("2026-09-01", 189000, "특가"),
                    PriceChartPointDto("2026-10-24", 179000, "역대최저가")
                ),
                standardizedSpecs = StandardizedSpecsDto(
                    heroKeyMetrics = listOf(
                        KeyMetricDto("weight", "초경량 무게", "60", "g"),
                        KeyMetricDto("sensor", "센서", "HERO 2 (32,000 DPI)", ""),
                        KeyMetricDto("polling", "무선 폴링레이트", "최대 4,000", "Hz")
                    ),
                    verifiedPros = listOf(
                        "손목에 부하가 전혀 없는 60g 밸런스와 완벽한 코팅 그립감",
                        "광축 하이브리드 스위치로 더블클릭 고장 가능성 원천 차단",
                        "USB-C 포트 전환 및 최대 95시간 연속 구동 배터리"
                    ),
                    verifiedCons = listOf(
                        "마우스 1개에 17만원대의 높은 진입장벽",
                        "DPI 전환 버튼이 바닥면에 위치"
                    ),
                    specSections = emptyList()
                ),
                predefinedAlternatives = listOf(
                    AlternativeDto(
                        type = "COST_EFFECTIVE",
                        typeLabel = "가성비 대안",
                        productId = "PRD-MS-2026-002",
                        productName = "바이퍼 V3 하이퍼스피드 무선 마우스",
                        brand = "레이저",
                        thumbnailUrl = "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=300&auto=format&fit=crop&q=80",
                        currentPrice = 89000,
                        differentiatingReason = "건전지 방식 최대 280시간 가동 및 9만원 저렴한 가성비",
                        affiliateUrl = "https://link.onepick.kr/out/prd-ms-002"
                    )
                )
            ),

            // 12. [바이럴 격리 대상 샘플: 협찬/광고 바이럴 어뷰징 감지 모델]
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

            // 13. [동적 대체(Fallback) 테스트 전용 상품]
            Product(
                id = "PRD-SPIKE-001",
                name = "인기 급상승 프리미엄 청소기 (상한선 초과 샘플)",
                brand = "하이엔드랩",
                category = "무선청소기",
                price = 299000,
                priceCeiling = 240000,
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
