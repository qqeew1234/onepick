document.addEventListener("DOMContentLoaded", () => {
  const searchInput = document.getElementById("searchInput");
  const searchBtn = document.getElementById("searchBtn");
  const popularChipsContainer = document.getElementById("popularChips");
  const resultContainer = document.getElementById("resultContainer");
  const latencyBanner = document.getElementById("latencyBanner");
  const latencyBadge = document.getElementById("latencyBadge");
  const latencyDetail = document.getElementById("latencyDetail");

  const auditInput = document.getElementById("auditInput");
  const auditBtn = document.getElementById("auditBtn");
  const auditResultBox = document.getElementById("auditResultBox");
  const viewQuarantinedBtn = document.getElementById("viewQuarantinedBtn");
  const quarantinedListBox = document.getElementById("quarantinedListBox");

  // 초기 인기 키워드 로드
  loadPopularKeywords();

  // 기본 검색어 자동 실행 (자취생 가성비 무선청소기)
  executeSearch("자취생 가성비 무선청소기");

  searchBtn.addEventListener("click", () => {
    executeSearch(searchInput.value);
  });

  searchInput.addEventListener("keypress", (e) => {
    if (e.key === "Enter") {
      executeSearch(searchInput.value);
    }
  });

  async function loadPopularKeywords() {
    try {
      const res = await fetch("/api/v1/search/popular");
      const keywords = await res.json();
      popularChipsContainer.innerHTML = "";

      keywords.forEach((k) => {
        const btn = document.createElement("button");
        btn.className = "chip-btn";
        btn.textContent = k.keyword;
        btn.addEventListener("click", () => {
          searchInput.value = k.keyword;
          executeSearch(k.keyword);
        });
        popularChipsContainer.appendChild(btn);
      });
    } catch (err) {
      console.error("Failed to load popular keywords", err);
    }
  }

  async function executeSearch(query) {
    const trimmed = query.trim();
    if (!trimmed) return;

    searchInput.value = trimmed;
    const clientStartTime = performance.now();

    try {
      const res = await fetch(`/api/v1/search?q=${encodeURIComponent(trimmed)}`);
      const data = await res.json();
      const clientElapsedMs = (performance.now() - clientStartTime).toFixed(2);

      renderResult(data, clientElapsedMs);
    } catch (err) {
      console.error("Search failed", err);
      resultContainer.innerHTML = `
        <div class="empty-box">
          <h3>검색 처리 중 오류가 발생했습니다.</h3>
          <p>${err.message}</p>
        </div>
      `;
    }
  }

  function renderResult(data, clientElapsedMs) {
    const latencyServer = data.latencyMs;
    latencyBanner.style.display = "flex";
    latencyBadge.innerHTML = `⚡ 엔진 연산: ${latencyServer}ms (Sub-10ms 보장)`;
    latencyDetail.innerHTML = `클라이언트 총 소요: ${clientElapsedMs}ms | 형태소: [${data.parsedMorphemes.join(", ")}]`;

    if (!data.heroItem) {
      resultContainer.innerHTML = `
        <div class="empty-box">
          <h3>🤔 완벽히 부합하는 정답 제품을 찾지 못했습니다</h3>
          <p>${data.prescriptiveReason}</p>
        </div>
      `;
      return;
    }

    const hero = data.heroItem;

    let specsHtml = "";
    if (hero.specs) {
      specsHtml = Object.entries(hero.specs)
        .map(([k, v]) => `<span class="spec-chip">${k}: <strong>${v}</strong></span>`)
        .join("");
    }

    let discountHtml = "";
    if (hero.discountRate) {
      discountHtml = `
        <span class="discount-badge">${hero.discountRate}% OFF</span>
        <span class="original-price">${Number(hero.originalPrice).toLocaleString()}원</span>
      `;
    }

    const heroHtml = `
      <div class="hero-card">
        <div class="hero-tag-bar">
          <span class="hero-pill">🎯 ONE-PICK 정답 (HERO ITEM)</span>
          <span class="clean-badge">🛡️ Zero-Tolerance 바이럴 0.0점 클린 인증</span>
        </div>

        <div class="reason-box">
          <div class="reason-title">💡 ONE-PICK 1줄 선정 사유 (고민 종결)</div>
          <div class="reason-text">"${data.prescriptiveReason}"</div>
        </div>

        <div class="product-content">
          <img class="product-thumb" src="${hero.imageUrl}" alt="${hero.name}" onerror="this.src='https://images.unsplash.com/photo-1558317374-067fb5f30001?w=500&auto=format&fit=crop&q=60'" />
          
          <div class="product-info">
            <div class="brand-label">${hero.brand} · ${hero.category}</div>
            <h2>${hero.name}</h2>

            <div class="price-row">
              <span class="current-price">${hero.formattedPrice}</span>
              ${discountHtml}
            </div>

            <div class="specs-grid">
              ${specsHtml}
            </div>

            <div class="trust-metrics">
              <div class="trust-item">
                <span class="trust-label">실구매 만족도</span>
                <span class="trust-value clean">${hero.sentimentScore}점</span>
              </div>
              <div class="trust-item">
                <span class="trust-label">실구매 불만율</span>
                <span class="trust-value clean">${hero.complaintRate}% (최저)</span>
              </div>
              <div class="trust-item">
                <span class="trust-label">바이럴 필터링</span>
                <span class="trust-value clean">0건 격리완료</span>
              </div>
            </div>

            <a class="btn-affiliate" href="${hero.affiliateUrl}" target="_blank" rel="noopener noreferrer">
              🛒 최저가 구매처 바로가기 (제휴) ➔
            </a>
          </div>
        </div>
      </div>
    `;

    // Alternatives HTML (Max 2)
    let alternativesHtml = "";
    if (data.alternatives && data.alternatives.length > 0) {
      const altCards = data.alternatives
        .map((alt) => {
          const badgeClass = alt.badgeType === "BUDGET_PICK" ? "budget" : "premium";
          const badgeLabel = alt.badgeType === "BUDGET_PICK" ? "가성비 대안" : "하이엔드 대안";

          return `
          <div class="alt-card">
            <span class="alt-badge ${badgeClass}">${badgeLabel}</span>
            <div class="alt-title">${alt.brand} ${alt.name}</div>
            <div class="alt-reason">${alt.keyReason}</div>
            <div class="alt-price">${alt.formattedPrice}</div>
            <a class="alt-btn" href="${alt.affiliateUrl}" target="_blank">최저가 보러가기</a>
          </div>
        `;
        })
        .join("");

      alternativesHtml = `
        <button id="toggleAltBtn" class="alternatives-toggle-btn">
          <span>⚖️ 다른 대안도 참고하시겠습니까? (엄선된 대안 ${data.alternatives.length}개)</span>
          <span id="toggleArrow">▼</span>
        </button>
        <div id="alternativesContainer" class="alternatives-grid" style="display: none;">
          ${altCards}
        </div>
      `;
    }

    resultContainer.innerHTML = heroHtml + alternativesHtml;

    const toggleAltBtn = document.getElementById("toggleAltBtn");
    if (toggleAltBtn) {
      toggleAltBtn.addEventListener("click", () => {
        const grid = document.getElementById("alternativesContainer");
        const arrow = document.getElementById("toggleArrow");
        if (grid.style.display === "none") {
          grid.style.display = "grid";
          arrow.textContent = "▲";
        } else {
          grid.style.display = "none";
          arrow.textContent = "▼";
        }
      });
    }
  }

  // Anti-Viral 검증 시뮬레이터 동작
  auditBtn.addEventListener("click", async () => {
    const text = auditInput.value.trim();
    if (!text) return;

    try {
      const res = await fetch("/api/v1/antiviral/analyze-text", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ text: text }),
      });
      const result = await res.json();

      auditResultBox.style.display = "block";
      if (result.isClean) {
        auditResultBox.style.background = "#ecfdf5";
        auditResultBox.style.color = "#065f46";
        auditResultBox.innerHTML = `✅ <strong>검증 통과:</strong> 광고성/협찬성 문구가 탐지되지 않은 청정 텍스트입니다.`;
      } else {
        auditResultBox.style.background = "#fef2f2";
        auditResultBox.style.color = "#991b1b";
        auditResultBox.innerHTML = `🚨 <strong>바이럴 감지:</strong> ${result.violations.join("<br>• ")}`;
      }
    } catch (err) {
      console.error(err);
    }
  });

  // 격리 상품 목록 보기
  viewQuarantinedBtn.addEventListener("click", async () => {
    try {
      const res = await fetch("/api/v1/antiviral/quarantine-list");
      const list = await res.json();
      quarantinedListBox.style.display = "block";

      if (list.length === 0) {
        quarantinedListBox.innerHTML = "<p>현재 격리된 상품이 없습니다.</p>";
        return;
      }

      const rows = list
        .map(
          (p) => `
        <div style="padding: 10px 0; border-bottom: 1px solid #fee2e2; color: #991b1b;">
          <strong>[격리됨] ${p.brand} - ${p.name}</strong> (바이럴 스코어: ${p.viralScore}점)<br>
          <span style="font-size: 0.85rem; color: #7f1d1d;">사유: ${p.quarantineReason || "광고/협찬 감지"}</span>
        </div>
      `
        )
        .join("");

      quarantinedListBox.innerHTML = `
        <div style="background: #fef2f2; border: 1px solid #fecaca; border-radius: 12px; padding: 16px; margin-top: 14px;">
          <h4 style="color: #991b1b; margin-bottom: 10px;">🛡️ Anti-Viral 엔진에 의해 100% 격리된 상품 목록 (${list.length}건)</h4>
          ${rows}
        </div>
      `;
    } catch (err) {
      console.error(err);
    }
  });
});
