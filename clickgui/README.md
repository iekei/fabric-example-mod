# Iekei Client — ClickGUI Web Mockup

A standalone, dependency-free web mockup of the **Iekei Client** ClickGUI
(Minecraft Java Edition · Fabric Mod), designed in a Soar-Client-inspired
glassmorphism style.

```
clickgui/
├── index.html    # markup shell (header, sidebar, theme panel, toasts)
├── styles.css    # glass design system, accent CSS variables, animations
├── icons.js      # 70+ inline stroke-only SVG icons (Lucide-style geometry)
├── data.js       # 8 categories · 78 modules · settings schema
├── app.js        # search, accordion, toggles, sliders, dropdowns, theming
└── assets/       # AI-generated voxel-night backdrop
```

## Run

Any static server works — no build step:

```bash
cd clickgui
python3 -m http.server 8000
# open http://localhost:8000
```

## Interactive behaviour

| Action | Result |
| --- | --- |
| `Right Shift` | Fade the whole ClickGUI in / out of the game view |
| `Esc` | Close the GUI (or clear the search field first) |
| `/` | Focus the live module search |
| Type in search | Instantly filters all 78 modules across every category |
| Click a module card | Smooth accordion expansion with its real settings |
| Card switch | Enables / disables the module (accent glow, count, toast) |
| Sidebar swatch / header “Theme” | Preset accents + custom colour picker + glow slider |
| “Toggle all” | Bulk enable / disable the current category |
| Keybind Config | Click the keycap, press any key → becomes the GUI toggle |

The accent colour is driven by three CSS variables (`--accent`, `--accent-rgb`,
`--glow`); every switch, slider track, icon stroke, glow and gradient derives
from them, so a theme change animates the entire interface at once.

## Notes

* All icons are hand-written inline `<svg>` line art — `fill: none`, 1.6px
  stroke, `currentColor` so they inherit the live accent colour.
* Modules, settings, ranges and units follow the production design spec
  (Fly speed, Killaura range/APS, BaseFinder sensitivity, Webhook URL, …).
* UI blips are synthesised with WebAudio and can be muted in
  *Client Settings → Sound Effects*.
