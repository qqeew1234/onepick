// ONE-PICK Frontend Application Script

let currentSearchData = null;
let currentChartDays = 90;
let chartPointsCache = [];
let chartCanvas = null;
let chartCtx = null;
let currentHeroId = null;

document.addEventListener('DOMContentLoaded', () => {
  chartCanvas = document.getElementById('priceChartCanvas');
  if (chartCanvas) {
    chartCtx = chartCanvas.getContext('2d');
  }

  // Setup Event Listeners
  setupEventListeners();

  // Initial Load with default query
  executeSearch('원룸 가성비 무선청소기');
});

function setupEventListeners() {
  const searchInput = document.getElementById('searchInput');
  const searchBtn = document.getElementById('searchBtn');

  // Search trigger
  searchBtn.addEventListener('click', () => {
    executeSearch(searchInput.value);
  });

  searchInput.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
      executeSearch(searchInput.value);
    }
  });

  // Quick chips
  document.querySelectorAll('.quick-chip').forEach(chip => {
    chip.addEventListener('click', () => {
      document.querySelectorAll('.quick-chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
      const query = chip.getAttribute('data-q');
      searchInput.value = query;
      executeSearch(query);
    });
  });

  // Price chart range buttons (30일 / 90일 / 180일)
  document.querySelectorAll('.range-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.range-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      currentChartDays = parseInt(btn.getAttribute('data-days'), 10);
      renderPriceChart();
    });
  });

  // Tab Switcher (CMP-05)
  const tabStandard = document.getElementById('tabStandard');
  const tabCatalog = document.getElementById('tabCatalog');
  const paneStandard = document.getElementById('tabContentStandard');
  const paneCatalog = document.getElementById('tabContentCatalog');

  tabStandard.addEventListener('click', () => {
    tabStandard.classList.add('active');
    tabCatalog.classList.remove('active');
    paneStandard.style.display = 'block';
    paneCatalog.style.display = 'none';
  });

  tabCatalog.addEventListener('click', () => {
    tabCatalog.classList.add('active');
    tabStandard.classList.remove('active');
    paneCatalog.style.display = 'block';
    paneStandard.style.display = 'none';
    lazyLoadCatalogImages();
  });

  // Alternatives Accordion (CMP-06)
  const accordionToggle = document.getElementById('alternativesToggleBtn');
  const accordionBody = document.getElementById('alternativesBody');
  const accordionArrow = document.getElementById('accordionArrow');

  accordionToggle.addEventListener('click', () => {
    const isClosed = accordionBody.style.display === 'none' || !accordionBody.style.display;
    if (isClosed) {
      accordionBody.style.display = 'block';
      accordionArrow.classList.add('rotated');
    } else {
      accordionBody.style.display = 'none';
      accordionArrow.classList.remove('rotated');
    }
  });

  // Canvas Hover Tooltip
  if (chartCanvas) {
    chartCanvas.addEventListener('mousemove', handleChartHover);
    chartCanvas.addEventListener('mouseleave', () => {
      const tooltip = document.getElementById('chartTooltip');
      if (tooltip) tooltip.style.display = 'none';
    });
  }
}

async function executeSearch(query) {
  if (!query || query.trim() === '') return;

  const executionBadge = document.getElementById('executionBadge');
  const executionTimeText = document.getElementById('executionTimeText');
  const headerLatency = document.getElementById('headerLatency');

  try {
    const startTime = performance.now();
    const response = await fetch(`/api/v1/search/prescriptive?q=${encodeURIComponent(query.trim())}`);
    const data = await response.json();
    const clientTime = (performance.now() - startTime).toFixed(1);

    currentSearchData = data;

    // Execution badge update
    const backendMs = data.executionTimeMs || 1.8;
    executionTimeText.textContent = `엔진 연산: ${backendMs}ms (네트워크 포함 ${clientTime}ms)`;
    executionBadge.style.display = 'inline-flex';
    if (headerLatency) {
      headerLatency.textContent = `Latency: ${backendMs}ms`;
    }

    renderSearchResponse(data);
  } catch (err) {
    console.error('Search request failed:', err);
    showEdgeCase('네트워크 오류가 발생했습니다', '잠시 후 다시 시도해 주세요.');
  }
}

function renderSearchResponse(data) {
  const fallbackNoticeBanner = document.getElementById('fallbackNoticeBanner');
  const fallbackNoticeText = document.getElementById('fallbackNoticeText');
  const edgeCaseContainer = document.getElementById('edgeCaseContainer');
  const resultSection = document.getElementById('resultSection');

  // 1. Fallback Notice
  if (data.fallbackNotice) {
    fallbackNoticeText.textContent = data.fallbackNotice;
    fallbackNoticeBanner.style.display = 'flex';
  } else {
    fallbackNoticeBanner.style.display = 'none';
  }

  // 2. Edge Case Check (EC-01, EC-02)
  if (!data.heroPick) {
    resultSection.style.display = 'none';
    showEdgeCase('단 하나의 정답을 확정할 수 없습니다', data.notice || '검색 조건에 부합하는 클린 검증 상품이 없습니다.');
    return;
  }

  edgeCaseContainer.style.display = 'none';
  resultSection.style.display = 'block';

  const hero = data.heroPick;
  currentHeroId = hero.productId;

  // Reset feedback state
  const feedbackArea = document.getElementById('feedbackActionArea');
  const feedbackResult = document.getElementById('feedbackResultMsg');
  if (feedbackArea) feedbackArea.style.display = 'flex';
  if (feedbackResult) feedbackResult.style.display = 'none';

  // Hero Basic Info
  document.getElementById('heroImage').src = hero.thumbnailUrl || 'https://via.placeholder.com/400?text=No+Image';
  document.getElementById('heroBrand').textContent = hero.brand || '브랜드';
  document.getElementById('heroName').textContent = hero.productName;
  document.getElementById('heroReason').textContent = hero.prescriptiveReason;
  document.getElementById('heroNonTarget').textContent = hero.nonTargetReason || '전체 사용자 권장';
  document.getElementById('heroPrice').textContent = Number(hero.currentPrice).toLocaleString();
  document.getElementById('heroBuyLink').href = hero.affiliateUrl || '#';

  // CMP-03 Price Status Badge & Warning
  const priceBadge = document.getElementById('priceBadge');
  const spikeWarning = document.getElementById('priceSpikeWarning');
  const status = hero.priceAnalysis.status;
  const statusLabel = hero.priceAnalysis.statusLabel;

  priceBadge.className = 'price-status-badge';
  if (status === 'ALL_TIME_LOW') {
    priceBadge.classList.add('all-time-low');
    spikeWarning.style.display = 'none';
  } else if (status === 'FAIR_PRICE') {
    priceBadge.classList.add('fair-price');
    spikeWarning.style.display = 'none';
  } else if (status === 'PRICE_SPIKE') {
    priceBadge.classList.add('price-spike');
    // FN-PRICE-01 UI 출력 규칙
    spikeWarning.style.display = 'block';
  }
  priceBadge.textContent = statusLabel;

  // Price Stats
  document.getElementById('statMinPrice').textContent = `${Number(hero.priceAnalysis.minPrice90Days).toLocaleString()}원`;
  document.getElementById('statAvgPrice').textContent = `${Number(hero.priceAnalysis.avgPrice90Days).toLocaleString()}원`;
  document.getElementById('statMaxPrice').textContent = `${Number(hero.priceAnalysis.maxPrice90Days).toLocaleString()}원`;

  // Price Chart Points
  chartPointsCache = hero.priceAnalysis.chartPoints || [];
  renderPriceChart();

  // CMP-05 Tab 1 Standard Specs Content
  renderStandardSpecs(hero.standardizedSpecs);

  // CMP-05 Tab 2 Official Catalog Content
  setupCatalogTab(hero.officialCatalog);

  // CMP-06 Alternatives
  renderAlternatives(data.alternatives);
}

function showEdgeCase(title, message) {
  const edgeCaseContainer = document.getElementById('edgeCaseContainer');
  const edgeCaseTitle = document.getElementById('edgeCaseTitle');
  const edgeCaseMsg = document.getElementById('edgeCaseMsg');

  edgeCaseTitle.textContent = title;
  edgeCaseMsg.textContent = message;
  edgeCaseContainer.style.display = 'block';
}

/* ========================================================
 * CMP-04 Price History Chart (Custom Canvas Renderer)
 * ======================================================== */
let renderedPoints = [];

function renderPriceChart() {
  if (!chartCanvas || !chartCtx || chartPointsCache.length === 0) return;

  const dpr = window.devicePixelRatio || 1;
  const rect = chartCanvas.getBoundingClientRect();
  chartCanvas.width = rect.width * dpr;
  chartCanvas.height = 240 * dpr;
  chartCtx.scale(dpr, dpr);

  const width = rect.width;
  const height = 240;
  const padding = { top: 30, right: 30, bottom: 40, left: 60 };

  chartCtx.clearRect(0, 0, width, height);

  // Filter points based on currentChartDays
  let points = [...chartPointsCache];
  if (currentChartDays === 30 && points.length > 3) {
    points = points.slice(-3);
  } else if (currentChartDays === 90 && points.length > 5) {
    points = points.slice(-5);
  }

  const prices = points.map(p => p.price);
  const minPrice = Math.min(...prices) * 0.95;
  const maxPrice = Math.max(...prices) * 1.05;
  const avgPrice = prices.reduce((a, b) => a + b, 0) / prices.length;

  const chartW = width - padding.left - padding.right;
  const chartH = height - padding.top - padding.bottom;

  function getY(price) {
    const ratio = (price - minPrice) / (maxPrice - minPrice);
    return height - padding.bottom - (ratio * chartH);
  }

  function getX(index) {
    if (points.length <= 1) return padding.left + chartW / 2;
    return padding.left + (index / (points.length - 1)) * chartW;
  }

  // 1. Draw Reference Guideline: 90일 평균선 (Dashed blue)
  const avgY = getY(avgPrice);
  chartCtx.beginPath();
  chartCtx.setLineDash([4, 4]);
  chartCtx.strokeStyle = '#93c5fd';
  chartCtx.lineWidth = 1.5;
  chartCtx.moveTo(padding.left, avgY);
  chartCtx.lineTo(width - padding.right, avgY);
  chartCtx.stroke();
  chartCtx.setLineDash([]);

  chartCtx.font = '10px Pretendard, sans-serif';
  chartCtx.fillStyle = '#2563eb';
  chartCtx.fillText(`평균가 (${Math.round(avgPrice).toLocaleString()}원)`, width - padding.right - 95, avgY - 6);

  // 2. Draw Trend Line
  renderedPoints = [];
  chartCtx.beginPath();
  points.forEach((pt, idx) => {
    const x = getX(idx);
    const y = getY(pt.price);
    renderedPoints.push({ x, y, pt });

    if (idx === 0) {
      chartCtx.moveTo(x, y);
    } else {
      chartCtx.lineTo(x, y);
    }
  });
  chartCtx.strokeStyle = '#2563eb';
  chartCtx.lineWidth = 3;
  chartCtx.stroke();

  // Draw Area under curve
  chartCtx.lineTo(getX(points.length - 1), height - padding.bottom);
  chartCtx.lineTo(getX(0), height - padding.bottom);
  chartCtx.closePath();
  const gradient = chartCtx.createLinearGradient(0, padding.top, 0, height - padding.bottom);
  gradient.addColorStop(0, 'rgba(37, 99, 235, 0.18)');
  gradient.addColorStop(1, 'rgba(37, 99, 235, 0.01)');
  chartCtx.fillStyle = gradient;
  chartCtx.fill();

  // 3. Draw Data Points & X Axis Labels
  renderedPoints.forEach((rp, idx) => {
    // Circle
    chartCtx.beginPath();
    chartCtx.arc(rp.x, rp.y, 5, 0, Math.PI * 2);
    chartCtx.fillStyle = idx === renderedPoints.length - 1 ? '#10b981' : '#2563eb';
    chartCtx.fill();
    chartCtx.lineWidth = 2;
    chartCtx.strokeStyle = '#ffffff';
    chartCtx.stroke();

    // Date Label
    chartCtx.font = '11px Pretendard, sans-serif';
    chartCtx.fillStyle = '#64748b';
    chartCtx.textAlign = 'center';
    chartCtx.fillText(rp.pt.date.slice(5), rp.x, height - padding.bottom + 18);
  });
}

function handleChartHover(e) {
  const tooltip = document.getElementById('chartTooltip');
  if (!tooltip || renderedPoints.length === 0) return;

  const rect = chartCanvas.getBoundingClientRect();
  const mouseX = e.clientX - rect.left;
  const mouseY = e.clientY - rect.top;

  let closest = null;
  let minDist = 30;

  renderedPoints.forEach(p => {
    const dist = Math.hypot(p.x - mouseX, p.y - mouseY);
    if (dist < minDist) {
      minDist = dist;
      closest = p;
    }
  });

  if (closest) {
    tooltip.innerHTML = `
      <strong>${closest.pt.date}</strong><br>
      최저가: <strong>${Number(closest.pt.price).toLocaleString()}원</strong><br>
      판매처: ${closest.pt.seller || '공식 제휴몰'}
    `;
    tooltip.style.left = `${closest.x + 10}px`;
    tooltip.style.top = `${closest.y - 40}px`;
    tooltip.style.display = 'block';
  } else {
    tooltip.style.display = 'none';
  }
}

/* ========================================================
 * CMP-05 Tab 1 Standard Specs Renderer
 * ======================================================== */
function renderStandardSpecs(specs) {
  if (!specs) return;

  // 1. Key Metrics 3-Second Cards
  const metricsGrid = document.getElementById('heroKeyMetricsGrid');
  metricsGrid.innerHTML = '';
  (specs.heroKeyMetrics || []).forEach(m => {
    const card = document.createElement('div');
    card.className = 'metric-card';
    card.innerHTML = `
      <div class="metric-label">${m.label}</div>
      <div class="metric-value">${m.value}<span class="metric-unit">${m.unit}</span></div>
    `;
    metricsGrid.appendChild(card);
  });

  // 2. Pros & Cons Bullets
  const prosList = document.getElementById('verifiedProsList');
  prosList.innerHTML = '';
  (specs.verifiedPros || []).forEach(pro => {
    const li = document.createElement('li');
    li.textContent = pro;
    prosList.appendChild(li);
  });

  const consList = document.getElementById('verifiedConsList');
  consList.innerHTML = '';
  (specs.verifiedCons || []).forEach(con => {
    const li = document.createElement('li');
    li.textContent = con;
    consList.appendChild(li);
  });

  // 3. Specs Sections Full Table (EC-03 결측치 표기)
  const sectionsContainer = document.getElementById('specSectionsContainer');
  sectionsContainer.innerHTML = '';
  (specs.specSections || []).forEach(sec => {
    const card = document.createElement('div');
    card.className = 'spec-section-card';
    let rowsHtml = '';
    (sec.items || []).forEach(item => {
      const isMissing = item.value.includes('미제공');
      const valClass = isMissing ? 'missing-val' : '';
      rowsHtml += `
        <tr>
          <td class="spec-key">${item.label}</td>
          <td class="spec-val ${valClass}">${item.value}</td>
        </tr>
      `;
    });

    card.innerHTML = `
      <div class="spec-section-title">${sec.sectionName}</div>
      <table class="spec-table">
        <tbody>${rowsHtml}</tbody>
      </table>
    `;
    sectionsContainer.appendChild(card);
  });
}

/* ========================================================
 * CMP-05 Tab 2 Official Catalog Setup & Lazy Loading
 * ======================================================== */
let catalogObserver = null;

function setupCatalogTab(catalog) {
  const catalogPdfLink = document.getElementById('catalogPdfLink');
  const catalogViewer = document.getElementById('catalogImagesViewer');
  const errorFallback = document.getElementById('catalogErrorFallback');
  const fallbackLink = document.getElementById('catalogFallbackLink');

  if (!catalog) {
    catalogViewer.innerHTML = '<p style="color: var(--gray-500); padding: 20px;">제공된 공식 카탈로그 정보가 없습니다.</p>';
    return;
  }

  catalogPdfLink.href = catalog.sourceCatalogUrl || '#';
  fallbackLink.href = catalog.sourceCatalogUrl || '#';
  errorFallback.style.display = 'none';

  catalogViewer.innerHTML = '';
  (catalog.rawImages || []).forEach((imgUrl, i) => {
    const img = document.createElement('img');
    img.className = 'catalog-img-item';
    img.dataset.src = imgUrl;
    img.alt = `제조사 공식 상세페이지 ${i + 1}`;
    img.onerror = () => {
      errorFallback.style.display = 'block';
    };
    catalogViewer.appendChild(img);
  });
}

function lazyLoadCatalogImages() {
  const images = document.querySelectorAll('.catalog-img-item[data-src]');
  if ('IntersectionObserver' in window) {
    if (catalogObserver) catalogObserver.disconnect();
    catalogObserver = new IntersectionObserver((entries, observer) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          const img = entry.target;
          img.src = img.dataset.src;
          img.removeAttribute('data-src');
          observer.unobserve(img);
        }
      });
    }, { rootMargin: '100px' });

    images.forEach(img => catalogObserver.observe(img));
  } else {
    images.forEach(img => {
      img.src = img.dataset.src;
      img.removeAttribute('data-src');
    });
  }
}

/* ========================================================
 * CMP-06 Alternatives Renderer
 * ======================================================== */
function renderAlternatives(alternatives) {
  const container = document.getElementById('alternativesList');
  container.innerHTML = '';

  if (!alternatives || alternatives.length === 0) {
    container.innerHTML = '<p style="color: var(--gray-500); grid-column: 1/-1;">현재 추가 대안이 필요 없는 완벽한 단일 정답입니다.</p>';
    return;
  }

  alternatives.forEach(alt => {
    const card = document.createElement('div');
    card.className = 'alt-card';
    const isCost = alt.type === 'COST_EFFECTIVE';
    const badgeClass = isCost ? 'cost-effective' : 'high-end';

    card.innerHTML = `
      <div>
        <div class="alt-header">
          <span class="alt-type-badge ${badgeClass}">${alt.typeLabel || '대안 상품'}</span>
          <span style="font-size: 0.8rem; color: var(--gray-500);">${alt.brand || ''}</span>
        </div>
        <h4 class="alt-name">${alt.productName}</h4>
        <div class="alt-price">${Number(alt.currentPrice).toLocaleString()}원</div>
        <div class="alt-diff-box">
          💡 <strong>차별화 포인트</strong>: ${alt.differentiatingReason}
        </div>
      </div>
      <a href="${alt.affiliateUrl || '#'}" target="_blank" rel="noopener noreferrer" class="btn-alt-link">
        대안 상품 확인하기
      </a>
    `;
    container.appendChild(card);
  });
}

/* ========================================================
 * Post-Purchase Feedback Submission (PRD 3)
 * ======================================================== */
async function sendFeedback(isMatch) {
  if (!currentHeroId) return;

  const feedbackArea = document.getElementById('feedbackActionArea');
  const feedbackResult = document.getElementById('feedbackResultMsg');

  try {
    const res = await fetch('/api/v1/search/feedback', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        productId: currentHeroId,
        isMatch: isMatch,
        comment: isMatch ? '사유와 일치함' : '사유와 불일치'
      })
    });
    const result = await res.json();
    feedbackArea.style.display = 'none';
    feedbackResult.style.display = 'block';
    feedbackResult.textContent = `✓ ${result.message || '피드백이 안전하게 반영되었습니다.'}`;
  } catch (err) {
    console.error('Feedback failed:', err);
  }
}
