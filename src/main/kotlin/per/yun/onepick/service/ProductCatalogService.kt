package per.yun.onepick.service

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import per.yun.onepick.domain.model.Product
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
            // 1. 무선청소기 카테고리 (가성비 원픽 & 하이엔드 대안)
            Product(
                id = "VC-001",
                name = "차이슨 i10 가성비 무선청소기",
                brand = "디베아",
                category = "무선청소기",
                price = 149000,
                originalPrice = 199000,
                imageUrl = "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/VC-001",
                specs = mapOf("흡입력" to "220W (28000Pa)", "무게" to "1.3kg", "사용시간" to "최대 50분"),
                tags = setOf("무선청소기", "청소기", "가성비", "자취", "원룸", "흡입력", "경량"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 96.8,
                complaintRate = 0.8,
                communityOrganicMentions = 420,
                promotionalBlogCount = 2,
                reviewCount = 3800,
                oneLineReasons = mapOf(
                    "자취" to "원룸/자취생 흡입력과 가성비 종결자, 실구매자 불만율 0.8% 최저",
                    "가성비" to "10만원대 중 유일한 28000Pa 흡입력과 1.3kg 초경량 검증 모델",
                    "원룸" to "원룸 좁은 틈새 보관 용이 및 1인 가구 청소 소요시간 5분 컷"
                ),
                defaultOneLineReason = "10만원대 무선청소기 중 실구매 만족도 96.8점 압도적 1위"
            ),
            Product(
                id = "VC-002",
                name = "싸이클론 V12 오리진 컴팩트 무선청소기",
                brand = "다이슨",
                category = "무선청소기",
                price = 789000,
                originalPrice = 899000,
                imageUrl = "https://images.unsplash.com/photo-1527515637462-cff94eecc1ac?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/VC-002",
                specs = mapOf("흡입력" to "150AW", "무게" to "2.2kg", "레이저슬림" to "플러피 헤드 탑재"),
                tags = setOf("무선청소기", "청소기", "하이엔드", "프리미엄", "흡입력"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 94.2,
                complaintRate = 1.2,
                communityOrganicMentions = 650,
                promotionalBlogCount = 5,
                reviewCount = 5200,
                oneLineReasons = mapOf(
                    "하이엔드" to "미세먼지 완벽 포집과 레이저 디텍트가 필요한 소비자를 위한 프리미엄 정답",
                    "흡입력" to "마이크로 단위 미세먼지 시각화 및 모터 내구성 검증 종결템"
                ),
                defaultOneLineReason = "프리미엄 무선청소기 기준 레이저 플러피 헤드 독보적 기술력"
            ),
            // [바이럴 격리 대상 샘플: 협찬/광고 바이럴로 도배된 청소기]
            Product(
                id = "VC-VIRAL-001",
                name = "블링클린 맥스 파워청소기 (어뷰징 감지 모델)",
                brand = "협찬사X",
                category = "무선청소기",
                price = 219000,
                originalPrice = 399000,
                imageUrl = "https://images.unsplash.com/photo-1563453392212-326f5e854473?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/VC-VIRAL-001",
                specs = mapOf("흡입력" to "과장광고 표기 35000Pa", "무게" to "2.9kg"),
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

            // 2. 무선이어폰 카테고리 (출퇴근/노캔 원픽 & 가성비 대안)
            Product(
                id = "EP-001",
                name = "WF-1000XM5 무선 노이즈캔슬링 이어폰",
                brand = "소니",
                category = "무선이어폰",
                price = 319000,
                originalPrice = 359000,
                imageUrl = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/EP-001",
                specs = mapOf("노이즈캔슬링" to "듀얼 프로세서 V2+QN2e", "배터리" to "이어버드 8시간+케이스 16시간", "코덱" to "LDAC 고음질"),
                tags = setOf("무선이어폰", "이어폰", "노이즈캔슬링", "출퇴근", "하이엔드", "고음질", "통화품질"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 97.4,
                complaintRate = 0.6,
                communityOrganicMentions = 890,
                promotionalBlogCount = 4,
                reviewCount = 4100,
                oneLineReasons = mapOf(
                    "출퇴근" to "지하철/버스 소음 99% 차단과 폼팁 기본 장착으로 출퇴근 집중력 극대화",
                    "노이즈캔슬링" to "현존 무선이어폰 중 가장 정교한 저음/중음 노이즈 캔슬링 프로세서 탑재",
                    "고음질" to "LDAC 지원으로 무손실 고음질 음원 완벽 재생"
                ),
                defaultOneLineReason = "출퇴근 대중교통 노이즈캔슬링 성능 실측 1위 종결 모델"
            ),
            Product(
                id = "EP-002",
                name = "버즈 FE 가성비 노이즈캔슬링 이어폰",
                brand = "삼성전자",
                category = "무선이어폰",
                price = 74000,
                originalPrice = 119000,
                imageUrl = "https://images.unsplash.com/photo-1606220588913-b3aacb4d2f46?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/EP-002",
                specs = mapOf("노이즈캔슬링" to "액티브 노캔 지원", "배터리" to "최대 30시간", "착용감" to "윙팁 기본 탑재"),
                tags = setOf("무선이어폰", "이어폰", "가성비", "노이즈캔슬링", "출퇴근"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 95.1,
                complaintRate = 0.9,
                communityOrganicMentions = 520,
                promotionalBlogCount = 3,
                reviewCount = 6800,
                oneLineReasons = mapOf(
                    "가성비" to "7만원대에 완성도 높은 ANC와 윙팁 고정력 갖춘 갓성비 원픽",
                    "출퇴근" to "분실 부담 없는 7만원대 가격에 안정적인 노이즈캔슬링 탑재"
                ),
                defaultOneLineReason = "10만원 미만 ANC 무선이어폰 중 실패 없는 국민 가성비픽"
            ),

            // 3. 로봇청소기 카테고리
            Product(
                id = "RC-001",
                name = "S8 Pro Ultra 올인원 로봇청소기",
                brand = "로보락",
                category = "로봇청소기",
                price = 1490000,
                originalPrice = 1690000,
                imageUrl = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/RC-001",
                specs = mapOf("기능" to "물걸레 온수세척+열풍건조+자동먼지비움", "흡입력" to "6000Pa", "회피센서" to "3D 구조광"),
                tags = setOf("로봇청소기", "물걸레", "하이엔드", "프리미엄"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 98.2,
                complaintRate = 0.4,
                communityOrganicMentions = 1200,
                promotionalBlogCount = 8,
                reviewCount = 9200,
                oneLineReasons = mapOf(
                    "로봇청소기" to "물걸레 자동세척부터 열풍건조까지 손댈 필요 없는 올인원 완전 자동화 종결템",
                    "물걸레" to "분당 3000회 진동 물걸레와 온수 세척으로 바닥 찌든때 완벽 제거"
                ),
                defaultOneLineReason = "실사용자 만족도 98.2점, 청소 노동 완전 해방 올인원 종결자"
            ),
            Product(
                id = "RC-002",
                name = "스마트 네비 LiDAR 가성비 로봇청소기",
                brand = "샤오미",
                category = "로봇청소기",
                price = 289000,
                originalPrice = 359000,
                imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/RC-002",
                specs = mapOf("센서" to "LDS 라이다 레이저", "흡입력" to "4000Pa", "물걸레" to "전자제어 수통"),
                tags = setOf("로봇청소기", "가성비", "자취", "원룸", "물걸레"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 93.5,
                complaintRate = 1.5,
                communityOrganicMentions = 340,
                promotionalBlogCount = 2,
                reviewCount = 2100,
                oneLineReasons = mapOf(
                    "가성비" to "20만원대 라이다 센서 탑재로 원룸/소형 평수 지도 정밀 매핑 가능",
                    "자취" to "자취방 바닥 먼지와 머리카락 매일 자동 관리 최적의 입문기"
                ),
                defaultOneLineReason = "20만원대 라이다 센서 탑재 가성비 로봇청소기 원픽"
            ),

            // 4. 헤어드라이기 카테고리
            Product(
                id = "HD-001",
                name = "에어센스 BLDC 전문가용 헤어드라이어",
                brand = "JMW",
                category = "헤어드라이기",
                price = 78000,
                originalPrice = 110000,
                imageUrl = "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/HD-001",
                specs = mapOf("모터" to "항공모터 BLDC 3세대", "풍속" to "초속 17m/s", "냉풍" to "완전 미열 없는 즉각 쿨링"),
                tags = setOf("헤어드라이기", "가성비", "강풍", "손상방지"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 98.0,
                complaintRate = 0.5,
                communityOrganicMentions = 980,
                promotionalBlogCount = 3,
                reviewCount = 14500,
                oneLineReasons = mapOf(
                    "가성비" to "미용실 전문가들이 10년째 정착한 완벽한 쿨링과 강력 항공모터 원픽",
                    "헤어드라이기" to "아침 출근 준비 건조시간 5분 단축시키는 강력한 BLDC 항공모터",
                    "강풍" to "미열 제로 완벽 냉풍 전환으로 두피 열감 방지"
                ),
                defaultOneLineReason = "국내 헤어샵 1위 채택, 고장 없는 반영구 항공모터 드라이기"
            ),
            Product(
                id = "HD-002",
                name = "슈퍼소닉 오리진 헤어드라이어",
                brand = "다이슨",
                category = "헤어드라이기",
                price = 449000,
                originalPrice = 499000,
                imageUrl = "https://images.unsplash.com/photo-1560769629-975ec94e6a86?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/HD-002",
                specs = mapOf("열제어" to "지능형 열제어 시스템 (초당 40회 측정)", "노즐" to "플라이어웨이 노즐 포함"),
                tags = setOf("헤어드라이기", "하이엔드", "프리미엄", "손상방지"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 95.8,
                complaintRate = 1.1,
                communityOrganicMentions = 710,
                promotionalBlogCount = 6,
                reviewCount = 8800,
                oneLineReasons = mapOf(
                    "하이엔드" to "극손상모 열손상 제로 지능형 제어와 잔머리 정리 특화 프리미엄픽"
                ),
                defaultOneLineReason = "극손상 모발 열손상 제로를 보장하는 프리미엄 헤어드라이어"
            ),

            // 5. 모니터 카테고리 (개발자 코딩/업무용 원픽)
            Product(
                id = "MN-001",
                name = "27인치 4K UHD IPS 프로 디스플레이 27UP850N",
                brand = "LG전자",
                category = "모니터",
                price = 459000,
                originalPrice = 529000,
                imageUrl = "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500&auto=format&fit=crop&q=60",
                affiliateUrl = "https://affiliate.onepick.kr/p/MN-001",
                specs = mapOf("해상도" to "3840x2160 (4K UHD)", "패널" to "IPS 95% DCI-P3", "포트" to "USB Type-C 90W PD 충전 지원"),
                tags = setOf("모니터", "4K", "고해상도", "개발자", "가성비"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 97.9,
                complaintRate = 0.7,
                communityOrganicMentions = 860,
                promotionalBlogCount = 2,
                reviewCount = 4200,
                oneLineReasons = mapOf(
                    "개발자" to "맥북/노트북 C타입 90W 단일케이블 연결 + 텍스트 가독성 최상 4K IPS 종결",
                    "4K" to "선명한 폰트 렌더링과 DCI-P3 95% 정확한 색역 지원",
                    "모니터" to "개발 및 재택근무 생산성을 200% 올려주는 4K C타입 표준 모니터"
                ),
                defaultOneLineReason = "개발자/직장인 맥북 C타입 호환 4K IPS 모니터 표준 원픽"
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
