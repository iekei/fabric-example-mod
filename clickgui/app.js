/* ==================================================================
   Iekei Client · ClickGUI mockup behaviour
   Accent theming, search, accordions, custom controls, key handling.
   ================================================================== */

const root = document.documentElement;
const $ = (sel, scope = document) => scope.querySelector(sel);

const PRESETS = [
  { name: 'Neon Purple', short: 'Purple', jp: 'ネオンパープル', color: '#a855f7' },
  { name: 'Cyber Red', short: 'Red', jp: 'サイバーレッド', color: '#ff3b5c' },
  { name: 'Electric Blue', short: 'Blue', jp: 'エレクトリックブルー', color: '#3b82f6' },
  { name: 'Emerald', short: 'Emerald', jp: 'エメラルドグリーン', color: '#10d98a' },
  { name: 'Amber Gold', short: 'Amber', jp: '黄金 / アンバー', color: '#f5a524' },
];

const state = {
  accent: '#a855f7',
  glow: 0.7,
  sound: true,
  toggleKey: 'ShiftRight',
  activeCat: 'movement',
  query: '',
  open: new Set(['fly']),
  modules: {},
};

/* seed module state from the data model */
CATEGORIES.forEach((cat) =>
  cat.modules.forEach((mod) => {
    state.modules[mod.id] = {
      enabled: !!mod.enabled,
      settings: Object.fromEntries(mod.settings.map((s) => [s.id, s.value])),
    };
  })
);

/* ---------------- helpers ---------------- */
function el(tag, cls, html) {
  const n = document.createElement(tag);
  if (cls) n.className = cls;
  if (html != null) n.innerHTML = html;
  return n;
}
const hexToRgb = (hex) => {
  const h = hex.replace('#', '');
  return [
    parseInt(h.slice(0, 2), 16),
    parseInt(h.slice(2, 4), 16),
    parseInt(h.slice(4, 6), 16),
  ];
};
const fmt = (v, step) =>
  step && step < 1 ? Number(v).toFixed(1) : String(Math.round(Number(v)));

const HUMAN_KEYS = {
  ShiftRight: 'Right Shift',
  ShiftLeft: 'Left Shift',
  ControlLeft: 'Left Ctrl',
  ControlRight: 'Right Ctrl',
  AltLeft: 'Left Alt',
  AltRight: 'Right Alt',
  Space: 'Space',
  Escape: 'Esc',
  Backquote: '`',
  CapsLock: 'Caps Lock',
  Tab: 'Tab',
  Enter: 'Enter',
  Backspace: 'Backspace',
  ArrowUp: '↑',
  ArrowDown: '↓',
  ArrowLeft: '←',
  ArrowRight: '→',
};
function humanKey(code) {
  if (!code) return 'None';
  if (HUMAN_KEYS[code]) return HUMAN_KEYS[code];
  if (/^Key[A-Z]$/.test(code)) return code.slice(3);
  if (/^Digit\d$/.test(code)) return code.slice(5);
  if (/^F\d{1,2}$/.test(code)) return code;
  return code.replace(/([A-Z])/g, ' $1').trim();
}

/* ---------------- subtle UI audio ---------------- */
let audioCtx = null;
function tone(freq, dur, type = 'sine', gain = 0.03) {
  if (!state.sound) return;
  try {
    audioCtx = audioCtx || new (window.AudioContext || window.webkitAudioContext)();
    const o = audioCtx.createOscillator();
    const g = audioCtx.createGain();
    o.type = type;
    o.frequency.value = freq;
    g.gain.setValueAtTime(0, audioCtx.currentTime);
    g.gain.linearRampToValueAtTime(gain, audioCtx.currentTime + 0.008);
    g.gain.exponentialRampToValueAtTime(0.0001, audioCtx.currentTime + dur);
    o.connect(g).connect(audioCtx.destination);
    o.start();
    o.stop(audioCtx.currentTime + dur + 0.02);
  } catch (e) {
    /* audio unavailable — silently ignore */
  }
}
const tick = () => tone(760, 0.035, 'triangle', 0.018);
const blip = (on) => tone(on ? 620 : 380, 0.075, 'sine', 0.03);

/* ---------------- toasts ---------------- */
const toastHost = $('#toasts');
function toast(title, sub = '', icon = 'check') {
  const t = el('div', 'toast');
  t.innerHTML = `<span class="ic">${svgIcon(icon)}</span>
    <span class="txt">${title}${sub ? `<small>${sub}</small>` : ''}</span>`;
  toastHost.appendChild(t);
  while (toastHost.children.length > 3) toastHost.firstElementChild.remove();
  setTimeout(() => {
    t.classList.add('out');
    setTimeout(() => t.remove(), 300);
  }, 2400);
}

/* ---------------- theme ---------------- */
function applyAccent(hex, opts = {}) {
  hex = hex.toLowerCase();
  state.accent = hex;
  const [r, g, b] = hexToRgb(hex);
  root.style.setProperty('--accent', hex);
  root.style.setProperty('--accent-color', hex);
  root.style.setProperty('--accent-rgb', `${r}, ${g}, ${b}`);

  /* settings panel + theme module stay in sync */
  if (state.modules.themeselector) state.modules.themeselector.settings.accent = hex;
  document.querySelectorAll('[data-accent-dot]').forEach((d) => {
    d.classList.toggle('active', d.dataset.accentDot.toLowerCase() === hex);
  });
  document.querySelectorAll('[data-theme-hex]').forEach((n) => {
    n.textContent = hex.toUpperCase();
  });
  document.querySelectorAll('[data-theme-swatch]').forEach((n) => {
    n.style.background = hex;
    n.style.color = hex;
  });
  document.querySelectorAll('[data-theme-input]').forEach((n) => {
    n.value = hex;
  });
  const custom = $('#customColor');
  if (custom) custom.value = hex;
  const sw = $('#customSwatch');
  if (sw) {
    sw.style.background = hex;
    sw.style.color = hex;
  }
  $('#customHex').textContent = hex.toUpperCase();
  document.querySelectorAll('#presetGrid .preset').forEach((p) => {
    p.classList.toggle('active', p.dataset.color.toLowerCase() === hex);
  });

  if (!opts.silent) {
    tick();
    if (opts.announce) toast('Accent colour updated', hex.toUpperCase(), 'palette');
  }
}

function setGlow(value) {
  state.glow = value;
  root.style.setProperty('--glow', value);
  const pct = Math.round(value * 100);
  const lbl = $('#glowVal');
  if (lbl) lbl.textContent = pct + '%';
  const range = $('#glowRange');
  if (range) {
    range.value = pct;
    paintRange(range);
  }
  if (state.modules.themeselector) state.modules.themeselector.settings.glow = pct;
  document.querySelectorAll('[data-glow-value]').forEach((n) => {
    n.textContent = pct + '%';
  });
  document.querySelectorAll('[data-glow-range]').forEach((n) => {
    n.value = pct;
    paintRange(n);
  });
  root.style.setProperty('--glow', value);
}

/* ---------------- range painting ---------------- */
function paintRange(input) {
  const min = Number(input.min || 0);
  const max = Number(input.max || 100);
  const val = Number(input.value);
  const pct = ((val - min) / (max || 1)) * 100;
  input.style.setProperty('--pct', pct + '%');
}

/* ---------------- dropdown (portalled so it is never clipped) ---------------- */
let openDD = null;
function closeDropdowns() {
  if (!openDD) return;
  openDD.trigger.closest('.dd')?.classList.remove('open');
  openDD.menu.classList.remove('open');
  openDD = null;
}
document.addEventListener('click', (e) => {
  if (openDD && !openDD.menu.contains(e.target) && !openDD.trigger.contains(e.target)) {
    closeDropdowns();
  }
});
document.addEventListener('scroll', closeDropdowns, true);

/* ---------------- setting controls ---------------- */
function buildSetting(def, mod) {
  const st = state.modules[mod.id];
  const val = st.settings[def.id];

  /* slider ------------------------------------------------------- */
  if (def.type === 'slider') {
    const wrap = el('div', 'slider-setting');
    wrap.innerHTML = `
      <div class="slider-head">
        <span class="label">${def.label}</span>
        <span class="value"${mod.id === 'themeselector' && def.id === 'glow' ? ' data-glow-value' : ''}>${fmt(val, def.step)}${def.unit || ''}</span>
      </div>`;
    const range = el('input');
    range.type = 'range';
    range.min = def.min;
    range.max = def.max;
    range.step = def.step;
    range.value = val;
    range.setAttribute('aria-label', def.label);
    if (mod.id === 'themeselector' && def.id === 'glow') range.dataset.glowRange = '';
    paintRange(range);
    range.addEventListener('input', () => {
      const v = Number(range.value);
      st.settings[def.id] = v;
      wrap.querySelector('.value').textContent = fmt(v, def.step) + (def.unit || '');
      paintRange(range);
      if (mod.id === 'themeselector' && def.id === 'glow') setGlow(v / 100);
      tone(520 + (v / (def.max || 1)) * 260, 0.02, 'sine', 0.012);
    });
    wrap.appendChild(range);
    return wrap;
  }

  /* layout row --------------------------------------------------- */
  const row = el('div', 'setting');
  row.innerHTML = `<div class="label">${def.label}</div>`;

  if (def.type === 'toggle') {
    const sw = el('button', 'switch sm' + (val ? ' on' : ''));
    sw.type = 'button';
    sw.setAttribute('role', 'switch');
    sw.setAttribute('aria-checked', String(!!val));
    sw.setAttribute('aria-label', def.label);
    sw.addEventListener('click', () => {
      const next = !st.settings[def.id];
      st.settings[def.id] = next;
      sw.classList.toggle('on', next);
      sw.setAttribute('aria-checked', String(next));
      if (mod.id === 'sound' && def.id === 'enable') state.sound = next;
      blip(next);
    });
    row.appendChild(sw);
  } else if (def.type === 'select') {
    const dd = el('div', 'dd');
    const btn = el('button', 'dd-btn');
    btn.type = 'button';
    btn.innerHTML = `<span class="val">${val}</span>${svgIcon('chevron-down')}`;
    dd.appendChild(btn);

    const menu = el('div', 'dd-menu portal');
    def.options.forEach((opt) => {
      const b = el('button', 'dd-opt' + (opt === val ? ' sel' : ''));
      b.type = 'button';
      b.innerHTML = `<span>${opt}</span>${svgIcon('check')}`;
      b.addEventListener('click', () => {
        st.settings[def.id] = opt;
        btn.querySelector('.val').textContent = opt;
        menu.querySelectorAll('.dd-opt').forEach((o) => o.classList.remove('sel'));
        b.classList.add('sel');
        closeDropdowns();
        blip(true);
      });
      menu.appendChild(b);
    });
    document.body.appendChild(menu);

    btn.addEventListener('click', (e) => {
      e.stopPropagation();
      const wasOpen = dd.classList.contains('open');
      closeDropdowns();
      if (wasOpen) return;
      dd.classList.add('open');
      menu.classList.add('open');
      const r = btn.getBoundingClientRect();
      const mw = Math.max(r.width, 132);
      menu.style.minWidth = mw + 'px';
      menu.style.left = Math.min(r.left, window.innerWidth - mw - 12) + 'px';
      const mh = menu.offsetHeight;
      const below = r.bottom + 6 + mh < window.innerHeight - 10;
      menu.style.top = (below ? r.bottom + 6 : r.top - mh - 6) + 'px';
      openDD = { trigger: btn, menu };
      tick();
    });
    row.appendChild(dd);
  } else if (def.type === 'color') {
    const field = el('div', 'color-field');
    const sw = el('button', 'color-swatch');
    sw.style.background = val;
    sw.style.color = val;
    const input = el('input');
    input.type = 'color';
    input.value = val;
    input.setAttribute('aria-label', def.label);
    const hex = el('span', 'color-hex', val.toUpperCase());
    sw.appendChild(input);
    input.addEventListener('input', () => {
      st.settings[def.id] = input.value;
      sw.style.background = input.value;
      sw.style.color = input.value;
      hex.textContent = input.value.toUpperCase();
      tone(660, 0.02, 'sine', 0.01);
    });
    field.append(sw, hex);
    row.appendChild(field);
  } else if (def.type === 'text') {
    const wrap = el('div', 'text-wrap');
    const input = el('input', 'text-input');
    input.type = 'text';
    input.value = val;
    input.placeholder = def.placeholder || '';
    input.setAttribute('aria-label', def.label);
    input.addEventListener('input', () => {
      st.settings[def.id] = input.value;
    });
    wrap.appendChild(input);
    row.appendChild(wrap);
  } else if (def.type === 'keybind') {
    const btn = el('button', 'keycap-btn', humanKey(val));
    btn.type = 'button';
    btn.addEventListener('click', () => beginCapture(btn, def, mod));
    row.appendChild(btn);
  } else if (def.type === 'theme') {
    const dots = el('div', 'theme-dots');
    PRESETS.forEach((p) => {
      const b = el('button', 'swatch');
      b.type = 'button';
      b.title = `${p.name} · ${p.jp}`;
      b.dataset.accentDot = p.color;
      b.style.background = p.color;
      b.style.color = p.color;
      b.addEventListener('click', () => applyAccent(p.color, { announce: true }));
      dots.appendChild(b);
    });
    const field = el('div', 'color-field');
    const sw = el('button', 'color-swatch');
    sw.style.background = state.accent;
    sw.style.color = state.accent;
    sw.dataset.themeSwatch = '';
    const input = el('input');
    input.type = 'color';
    input.value = state.accent;
    input.dataset.themeInput = '';
    input.setAttribute('aria-label', 'Custom accent');
    sw.appendChild(input);
    const hex = el('span', 'color-hex', state.accent.toUpperCase());
    hex.dataset.themeHex = '';
    input.addEventListener('input', () => applyAccent(input.value, { silent: true }));
    field.append(sw, hex);
    dots.appendChild(field);
    row.classList.add('theme-setting');
    row.appendChild(dots);
  }

  return row;
}

/* ---------------- keybind capture ---------------- */
let capture = null;
function beginCapture(btn, def, mod) {
  capture = { btn, def, mod };
  btn.classList.add('capturing');
  btn.textContent = 'Press a key…';
  tick();
}
document.addEventListener('keydown', (e) => {
  if (!capture) return;
  e.preventDefault();
  e.stopPropagation();
  e.stopImmediatePropagation();
  const { btn, def, mod } = capture;
  btn.classList.remove('capturing');
  if (e.code === 'Escape') {
    btn.textContent = humanKey(state.modules[mod.id].settings[def.id]);
    capture = null;
    return;
  }
  state.modules[mod.id].settings[def.id] = e.code;
  btn.textContent = humanKey(e.code);
  capture = null;
  if (def.id === 'openkey') {
    state.toggleKey = e.code;
    syncKeyLabels();
    toast('Menu keybind updated', humanKey(e.code), 'keyboard');
  } else {
    toast(`${mod.name} bound`, humanKey(e.code), 'keyboard');
  }
  blip(true);
});

/* ---------------- module card ---------------- */
function buildCard(mod, cat, opts = {}) {
  const st = state.modules[mod.id];
  const card = el('article', 'card');
  card.dataset.id = mod.id;
  if (st.enabled) card.classList.add('enabled');
  if (state.open.has(mod.id)) card.classList.add('open');

  const head = el('div', 'card-head');
  head.tabIndex = 0;
  head.setAttribute('role', 'button');
  head.setAttribute('aria-expanded', String(state.open.has(mod.id)));
  head.innerHTML = `
    <div class="card-ico">${svgIcon(mod.icon)}</div>
    <div class="card-main">
      <div class="card-name">${mod.name}${opts.tag ? `<span class="cat-tag">${cat.name}</span>` : ''}</div>
      <div class="card-desc">${mod.desc}</div>
    </div>
    <div class="card-right">
      ${mod.key ? `<span class="kbd">${mod.key}</span>` : ''}
      <button class="switch${st.enabled ? ' on' : ''}" type="button" role="switch"
        aria-checked="${st.enabled}" aria-label="Enable ${mod.name}"></button>
      ${svgIcon('chevron-down', 'chev')}
    </div>`;

  const setOpen = (open) => {
    card.classList.toggle('open', open);
    head.setAttribute('aria-expanded', String(open));
    if (open) state.open.add(mod.id);
    else state.open.delete(mod.id);
    if (open) {
      setTimeout(() => {
        try {
          card.scrollIntoView({ block: 'nearest', behavior: 'smooth' });
        } catch (e) { /* older engines */ }
      }, 340);
    }
    tick();
  };

  head.addEventListener('click', (e) => {
    if (e.target.closest('.switch')) return;
    setOpen(!card.classList.contains('open'));
  });
  head.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      if (e.target.closest('.switch')) return;
      setOpen(!card.classList.contains('open'));
    }
  });

  const sw = head.querySelector('.switch');
  sw.addEventListener('click', (e) => {
    e.stopPropagation();
    const next = !state.modules[mod.id].enabled;
    state.modules[mod.id].enabled = next;
    card.classList.toggle('enabled', next);
    sw.classList.toggle('on', next);
    sw.setAttribute('aria-checked', String(next));
    refreshCounts();
    blip(next);
    toast(
      `${mod.name} ${next ? 'enabled' : 'disabled'}`,
      `${cat.name} · ${mod.id}`,
      next ? 'power' : 'x'
    );
  });

  const body = el('div', 'card-body');
  const inner = el('div', 'inner');
  const pad = el('div', 'pad');
  mod.settings.forEach((def) => pad.appendChild(buildSetting(def, mod)));
  inner.appendChild(pad);
  body.appendChild(inner);

  card.append(head, body);
  return card;
}

/* ---------------- rendering ---------------- */
const nav = $('#nav');
const content = $('#content');
const searchInput = $('#search');

function enabledCount(cat) {
  return cat.modules.filter((m) => state.modules[m.id].enabled).length;
}

function renderNav() {
  nav.innerHTML = '';
  CATEGORIES.forEach((cat) => {
    const b = el('button', 'nav-item' + (state.activeCat === cat.id && !state.query ? ' active' : ''));
    b.type = 'button';
    b.dataset.cat = cat.id;
    b.setAttribute('aria-current', state.activeCat === cat.id ? 'page' : 'false');
    b.innerHTML = `${svgIcon(cat.icon)}
      <span class="label"><span>${cat.name}</span><span class="jp">${cat.sub}</span></span>
      <span class="count">${enabledCount(cat)}/${cat.modules.length}</span>`;
    b.addEventListener('click', () => {
      state.activeCat = cat.id;
      state.query = '';
      searchInput.value = '';
      renderNav();
      renderContent();
      content.scrollTop = 0;
      tick();
    });
    nav.appendChild(b);
  });
}

function refreshCounts() {
  document.querySelectorAll('.nav-item').forEach((n) => {
    const cat = CATEGORIES.find((c) => c.id === n.dataset.cat);
    if (cat) n.querySelector('.count').textContent = `${enabledCount(cat)}/${cat.modules.length}`;
  });
  const total = Object.values(state.modules).length;
  const active = Object.values(state.modules).filter((m) => m.enabled).length;
  $('#statEnabled').textContent = active;
  $('#statTotal').textContent = total;
  const chip = $('#catCountChip');
  if (chip) {
    const cat = CATEGORIES.find((c) => c.id === state.activeCat);
    if (cat && !state.query) chip.innerHTML = `<b>${enabledCount(cat)}</b> / ${cat.modules.length} active`;
  }
  if (state.query) renderSearchCount();
}

function renderContent() {
  closeDropdowns();
  document.querySelectorAll('.dd-menu.portal').forEach((n) => n.remove());
  content.innerHTML = '';
  if (state.query.trim()) renderSearch();
  else renderCategory();
  renderSearchCount();
}

function renderCategory() {
  const cat = CATEGORIES.find((c) => c.id === state.activeCat) || CATEGORIES[0];
  const head = el('div', 'cat-head');
  head.innerHTML = `
    <div class="cat-ico">${svgIcon(cat.icon)}</div>
    <div class="cat-meta">
      <div class="title-row"><h2>${cat.name}</h2><span class="jp">${cat.sub}</span></div>
      <p>${cat.desc}</p>
    </div>
    <div class="cat-tools">
      <span class="chip" id="catCountChip"><b>${enabledCount(cat)}</b> / ${cat.modules.length} active</span>
      <button class="ghost-btn" id="toggleAll" type="button">${svgIcon('sliders')}<span>Toggle all</span></button>
    </div>`;
  content.appendChild(head);

  const grid = el('div', 'grid');
  cat.modules.forEach((mod, i) => {
    const card = buildCard(mod, cat);
    card.style.animationDelay = Math.min(i * 18, 220) + 'ms';
    grid.appendChild(card);
  });
  content.appendChild(grid);

  $('#toggleAll', head).addEventListener('click', () => {
    const cat2 = CATEGORIES.find((c) => c.id === state.activeCat);
    const allOn = cat2.modules.every((m) => state.modules[m.id].enabled);
    cat2.modules.forEach((m) => (state.modules[m.id].enabled = !allOn));
    renderContent();
    refreshCounts();
    blip(!allOn);
    toast(`${cat2.name} ${allOn ? 'disabled' : 'enabled'}`, `${cat2.modules.length} modules`, 'power');
  });
}

function searchMatches(q) {
  const out = [];
  CATEGORIES.forEach((cat) =>
    cat.modules.forEach((mod) => {
      const hay = [
        mod.name,
        mod.desc,
        cat.name,
        cat.sub,
        ...mod.settings.map((s) => s.label),
      ]
        .join(' ')
        .toLowerCase();
      if (hay.includes(q)) out.push({ mod, cat });
    })
  );
  return out;
}

function renderSearch() {
  const q = state.query.trim().toLowerCase();
  const results = searchMatches(q);

  const banner = el('div', 'search-banner');
  banner.innerHTML = `${svgIcon('search')}
    <span><b>${results.length}</b> module${results.length === 1 ? '' : 's'} found for
    “<b>${escapeHtml(state.query.trim())}</b>”</span>
    <button class="clear" type="button">${svgIcon('x')}Clear</button>`;
  $('.clear', banner).addEventListener('click', clearSearch);
  content.appendChild(banner);

  if (!results.length) {
    const empty = el('div', 'empty');
    empty.innerHTML = `${svgIcon('search')}
      <h3>No modules match your search</h3>
      <p>Try “fly”, “esp”, “finder”, “speed” or a setting name like “range”.</p>`;
    content.appendChild(empty);
    return;
  }

  const grid = el('div', 'grid');
  results.forEach(({ mod, cat }, i) => {
    const card = buildCard(mod, cat, { tag: true });
    card.style.animationDelay = Math.min(i * 14, 180) + 'ms';
    grid.appendChild(card);
  });
  content.appendChild(grid);
}

function escapeHtml(s) {
  return s.replace(/[&<>"]/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[c]));
}

function renderSearchCount() {
  const badge = $('#searchCount');
  if (!state.query.trim()) {
    badge.classList.remove('show');
    badge.textContent = '';
    return;
  }
  badge.textContent = searchMatches(state.query.trim().toLowerCase()).length + ' hits';
  badge.classList.add('show');
}

function clearSearch() {
  state.query = '';
  searchInput.value = '';
  renderNav();
  renderContent();
  searchInput.focus();
}

function syncKeyLabels() {
  const label = humanKey(state.toggleKey);
  $('#headKeyLabel').textContent = `[ ${label} ]`;
  $('#sideKeyLabel').textContent = label;
  const hint = $('#sideKeyHint');
  if (hint) hint.textContent = `[${label}] to Toggle Menu`;
  $('#statKey').textContent = `TOGGLE [${state.toggleKey.toUpperCase()}]`;
  document.querySelectorAll('[data-openkey]').forEach((n) => (n.textContent = label));
}

/* ---------------- theme panel ---------------- */
function buildThemeUI() {
  const grid = $('#presetGrid');
  grid.innerHTML = '';
  PRESETS.forEach((p) => {
    const b = el('button', 'preset');
    b.type = 'button';
    b.dataset.color = p.color;
    b.innerHTML = `<span class="dot" style="background:${p.color};color:${p.color}"></span><span>${p.short}</span>`;
    b.title = `${p.name} · ${p.jp}`;
    b.addEventListener('click', () => applyAccent(p.color, { announce: true }));
    grid.appendChild(b);
  });

  const side = $('#sideSwatches');
  side.innerHTML = '';
  PRESETS.forEach((p) => {
    const b = el('button', 'swatch');
    b.type = 'button';
    b.style.background = p.color;
    b.style.color = p.color;
    b.dataset.accentDot = p.color;
    b.title = p.name;
    b.addEventListener('click', () => applyAccent(p.color, { announce: true }));
    side.appendChild(b);
  });

  $('#customColor').addEventListener('input', (e) => applyAccent(e.target.value, { silent: true, announce: true }));
  const glowRange = $('#glowRange');
  glowRange.addEventListener('input', () => {
    setGlow(Number(glowRange.value) / 100);
    paintRange(glowRange);
  });
  paintRange(glowRange);
  $('.value', $('.theme-slider-row')).style.color = 'var(--accent)';

  const btn = $('#themeBtn');
  const panel = $('#themePanel');
  btn.addEventListener('click', (e) => {
    e.stopPropagation();
    const open = panel.classList.toggle('open');
    btn.classList.toggle('active', open);
    btn.setAttribute('aria-expanded', String(open));
    tick();
  });
  document.addEventListener('click', (e) => {
    if (panel.classList.contains('open') && !panel.contains(e.target) && !btn.contains(e.target)) {
      panel.classList.remove('open');
      btn.classList.remove('active');
      btn.setAttribute('aria-expanded', 'false');
    }
  });
}

/* ---------------- open / close ---------------- */
function setGuiOpen(open) {
  document.body.classList.toggle('is-closed', !open);
  if (open) {
    closeDropdowns();
    tick();
  }
}

/* ---------------- global keys ---------------- */
const isTyping = () => {
  const a = document.activeElement;
  return a && (a.tagName === 'INPUT' || a.tagName === 'TEXTAREA' || a.tagName === 'SELECT');
};

document.addEventListener('keydown', (e) => {
  if (capture) return; // keybind capture owns the keyboard

  if (e.key === 'Escape') {
    if (isTyping() && document.activeElement === searchInput) {
      if (searchInput.value) {
        clearSearch();
        return;
      }
      searchInput.blur();
      setGuiOpen(false);
      return;
    }
    if ($('#themePanel').classList.contains('open')) {
      $('#themePanel').classList.remove('open');
      $('#themeBtn').classList.remove('active');
      $('#themeBtn').setAttribute('aria-expanded', 'false');
      return;
    }
    if (isTyping()) {
      document.activeElement.blur();
      return;
    }
    setGuiOpen(false);
    return;
  }

  if (e.key === '/' && !isTyping()) {
    e.preventDefault();
    setGuiOpen(true);
    searchInput.focus();
    return;
  }

  if (e.code && e.code === state.toggleKey && !isTyping()) {
    e.preventDefault();
    if (!e.repeat) setGuiOpen(document.body.classList.contains('is-closed'));
  }
});

/* ---------------- wiring ---------------- */
$('#brandMark').innerHTML = svgIcon('sparkles');
$('#closedMark').innerHTML = svgIcon('sparkles');
$('#closeBtn').addEventListener('click', () => setGuiOpen(false));
$('#openFromHint').addEventListener('click', () => setGuiOpen(true));

searchInput.addEventListener('input', (e) => {
  state.query = e.target.value;
  renderNav();
  renderContent();
  content.scrollTop = 0;
});
searchInput.addEventListener('keydown', (e) => {
  if (e.key === 'Escape') {
    e.stopPropagation();
    clearSearch();
    searchInput.blur();
  }
});

/* ---------------- boot ---------------- */
function boot() {
  buildThemeUI();
  applyAccent('#a855f7', { silent: true });
  setGlow(0.7);
  renderNav();
  renderContent();
  syncKeyLabels();
  refreshCounts();
  setTimeout(() => {
    toast('Iekei Client loaded', `Press Right Shift to toggle · ${Object.keys(state.modules).length} modules`, 'sparkles');
  }, 700);
}

boot();
