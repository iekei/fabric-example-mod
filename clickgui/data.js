/* ------------------------------------------------------------------
   Iekei Client · ClickGUI mockup data model
   Every module owns a list of settings. Setting types:
     toggle  · slider · select · color · text · keybind · theme
------------------------------------------------------------------- */

const CATEGORIES = [
  {
    id: 'movement',
    name: 'Movement',
    sub: '移動系',
    icon: 'footprints',
    desc: 'Travel faster, higher and further than the vanilla game allows.',
    modules: [
      { id: 'fly', name: 'Fly', icon: 'feather', key: 'F', enabled: true, desc: 'Allows sustained flight in survival.',
        settings: [
          { id: 'speed', label: 'Speed', type: 'slider', min: 0.1, max: 5, step: 0.1, value: 1.6, unit: 'x' },
          { id: 'mode', label: 'Mode', type: 'select', options: ['Vanilla', 'Creative', 'Glide'], value: 'Vanilla' },
          { id: 'antikick', label: 'Anti-Kick', type: 'toggle', value: true },
        ] },
      { id: 'speed', name: 'Speed', icon: 'wind', enabled: true, desc: 'Multiplies your ground movement speed.',
        settings: [
          { id: 'multiplier', label: 'Multiplier', type: 'slider', min: 1, max: 10, step: 0.1, value: 2.4, unit: 'x' },
          { id: 'mode', label: 'Mode', type: 'select', options: ['Strafe', 'Omni', 'Legacy'], value: 'Strafe' },
          { id: 'autojump', label: 'Auto Jump', type: 'toggle', value: true },
        ] },
      { id: 'noslowdown', name: 'NoSlowdown', icon: 'gauge', key: 'N', enabled: true, desc: 'Removes the slowdown from using items.',
        settings: [
          { id: 'items', label: 'Items Only', type: 'toggle', value: false },
          { id: 'webs', label: 'Ignore Webs', type: 'toggle', value: true },
        ] },
      { id: 'jesus', name: 'Jesus', icon: 'waves', desc: 'Walk on water, lava and other liquids.',
        settings: [
          { id: 'liquid', label: 'Liquid', type: 'select', options: ['Water', 'Lava', 'Both'], value: 'Both' },
          { id: 'sneak', label: 'Sneak to Sink', type: 'toggle', value: true },
        ] },
      { id: 'step', name: 'Step', icon: 'stairs', enabled: true, desc: 'Instantly climbs up blocks.',
        settings: [
          { id: 'height', label: 'Height', type: 'slider', min: 0.5, max: 2.5, step: 0.1, value: 1.0, unit: 'b' },
          { id: 'instant', label: 'Instant', type: 'toggle', value: true },
        ] },
      { id: 'highjump', name: 'HighJump', icon: 'arrow-up', desc: 'Jump several blocks into the air.',
        settings: [
          { id: 'multiplier', label: 'Multiplier', type: 'slider', min: 1, max: 8, step: 0.1, value: 2.0, unit: 'x' },
          { id: 'glide', label: 'Glide Down', type: 'toggle', value: false },
        ] },
      { id: 'safewalk', name: 'SafeWalk', icon: 'shield', enabled: true, desc: 'Stops you from walking off edges.',
        settings: [
          { id: 'sneak', label: 'Force Sneak', type: 'toggle', value: true },
          { id: 'air', label: 'Include Air Blocks', type: 'toggle', value: false },
        ] },
      { id: 'spider', name: 'Spider', icon: 'bug', desc: 'Climb walls like a spider.',
        settings: [
          { id: 'speed', label: 'Climb Speed', type: 'slider', min: 0.2, max: 3, step: 0.1, value: 1.0, unit: 'x' },
          { id: 'look', label: 'Only When Looking Up', type: 'toggle', value: true },
        ] },
      { id: 'inventorymove', name: 'InventoryMove', icon: 'box', desc: 'Move while an inventory screen is open.',
        settings: [
          { id: 'gui', label: 'Allow in GUIs', type: 'toggle', value: true },
        ] },
      { id: 'airjump', name: 'AirJump', icon: 'cloud', desc: 'Jump again while already airborne.',
        settings: [
          { id: 'height', label: 'Jump Height', type: 'slider', min: 0.5, max: 2, step: 0.1, value: 0.9, unit: 'b' },
          { id: 'limit', label: 'Max Jumps', type: 'slider', min: 1, max: 10, step: 1, value: 3, unit: 'x' },
        ] },
      { id: 'entityspeed', name: 'EntitySpeed', icon: 'zap', desc: 'Speeds up boats, horses and minecarts.',
        settings: [
          { id: 'speed', label: 'Speed', type: 'slider', min: 1, max: 8, step: 0.1, value: 2.5, unit: 'x' },
        ] },
      { id: 'blink', name: 'Blink', icon: 'timer', desc: 'Suspends outgoing packets, then teleports.',
        settings: [
          { id: 'duration', label: 'Hold Duration', type: 'slider', min: 100, max: 3000, step: 50, value: 800, unit: 'ms' },
          { id: 'auto', label: 'Auto Send', type: 'toggle', value: true },
        ] },
      { id: 'fastladder', name: 'FastLadder', icon: 'ladder', desc: 'Climb ladders at high speed.',
        settings: [
          { id: 'speed', label: 'Speed', type: 'slider', min: 1, max: 5, step: 0.1, value: 2.0, unit: 'x' },
        ] },
      { id: 'parkour', name: 'Parkour', icon: 'footprints', desc: 'Automatically jumps at ledge edges.',
        settings: [
          { id: 'auto', label: 'Auto Jump', type: 'toggle', value: true },
          { id: 'sprint', label: 'Require Sprint', type: 'toggle', value: false },
        ] },
    ],
  },

  {
    id: 'combat',
    name: 'Combat',
    sub: '戦闘系',
    icon: 'swords',
    desc: 'Combat assistance with humanised timing and rotation.',
    modules: [
      { id: 'killaura', name: 'Killaura', icon: 'swords', key: 'R', enabled: true, desc: 'Attacks nearby entities automatically.',
        settings: [
          { id: 'range', label: 'Range', type: 'slider', min: 2.5, max: 7, step: 0.1, value: 4.2, unit: 'b' },
          { id: 'aps', label: 'APS', type: 'slider', min: 1, max: 20, step: 0.5, value: 9.5, unit: '/s' },
          { id: 'target', label: 'Priority', type: 'select', options: ['Closest', 'Lowest HP', 'Angle'], value: 'Closest' },
          { id: 'players', label: 'Target Players', type: 'toggle', value: true },
        ] },
      { id: 'triggerbot', name: 'Triggerbot', icon: 'crosshair', desc: 'Attacks whatever is under your crosshair.',
        settings: [
          { id: 'delay', label: 'Delay', type: 'slider', min: 0, max: 500, step: 10, value: 60, unit: 'ms' },
        ] },
      { id: 'aimassist', name: 'AimAssist', icon: 'target', enabled: true, desc: 'Gently nudges your aim towards targets.',
        settings: [
          { id: 'smooth', label: 'Smoothness', type: 'slider', min: 1, max: 10, step: 0.1, value: 4.5, unit: '' },
          { id: 'fov', label: 'FOV', type: 'slider', min: 30, max: 180, step: 5, value: 90, unit: '°' },
          { id: 'vertical', label: 'Vertical Assist', type: 'toggle', value: false },
        ] },
      { id: 'autoclicker', name: 'AutoClicker', icon: 'mouse', desc: 'Clicks for you at a set CPS.',
        settings: [
          { id: 'cps', label: 'CPS', type: 'slider', min: 1, max: 20, step: 0.5, value: 12, unit: '/s' },
          { id: 'button', label: 'Button', type: 'select', options: ['Left', 'Right', 'Both'], value: 'Left' },
          { id: 'jitter', label: 'Jitter', type: 'toggle', value: true },
        ] },
      { id: 'reach', name: 'Reach', icon: 'ruler', desc: 'Extends your attack range.',
        settings: [
          { id: 'distance', label: 'Distance', type: 'slider', min: 3, max: 6, step: 0.1, value: 3.6, unit: 'b' },
        ] },
      { id: 'velocity', name: 'Velocity', icon: 'wind', enabled: true, desc: 'Reduces knockback taken from hits.',
        settings: [
          { id: 'horizontal', label: 'Horizontal', type: 'slider', min: 0, max: 100, step: 1, value: 35, unit: '%' },
          { id: 'vertical', label: 'Vertical', type: 'slider', min: 0, max: 100, step: 1, value: 60, unit: '%' },
        ] },
      { id: 'criticals', name: 'Criticals', icon: 'zap', desc: 'Forces every hit to be a critical hit.',
        settings: [
          { id: 'mode', label: 'Mode', type: 'select', options: ['Packet', 'Jump', 'MiniJump'], value: 'Packet' },
        ] },
      { id: 'bowaimbot', name: 'BowAimbot', icon: 'move', desc: 'Predicts and aims bow shots for you.',
        settings: [
          { id: 'predict', label: 'Prediction', type: 'slider', min: 0, max: 2, step: 0.05, value: 0.8, unit: 'x' },
        ] },
      { id: 'fastbow', name: 'FastBow', icon: 'activity', desc: 'Shoots arrows with almost no cooldown.',
        settings: [
          { id: 'instant', label: 'Instant Release', type: 'toggle', value: true },
        ] },
      { id: 'targetstrafe', name: 'TargetStrafe', icon: 'orbit', desc: 'Circles around your current target.',
        settings: [
          { id: 'distance', label: 'Radius', type: 'slider', min: 1, max: 6, step: 0.1, value: 3.0, unit: 'b' },
          { id: 'direction', label: 'Direction', type: 'select', options: ['Clockwise', 'Counter', 'Random'], value: 'Clockwise' },
        ] },
      { id: 'hitbox', name: 'HitBox', icon: 'bracket', desc: 'Enlarges entity hitboxes.',
        settings: [
          { id: 'size', label: 'Size', type: 'slider', min: 0.1, max: 2, step: 0.05, value: 0.6, unit: 'b' },
        ] },
      { id: 'fastuse', name: 'FastUse', icon: 'aperture', desc: 'Uses items such as food far faster.',
        settings: [
          { id: 'speed', label: 'Speed', type: 'slider', min: 1, max: 5, step: 0.1, value: 2.5, unit: 'x' },
        ] },
    ],
  },

  {
    id: 'utility',
    name: 'Player Utility',
    sub: 'ユーティリティ',
    icon: 'wrench',
    desc: 'Quality of life automation for grinding and survival.',
    modules: [
      { id: 'fastbreak', name: 'FastBreak', icon: 'pickaxe', enabled: true, desc: 'Mines blocks faster.',
        settings: [
          { id: 'speed', label: 'Speed', type: 'slider', min: 1, max: 5, step: 0.1, value: 2.0, unit: 'x' },
          { id: 'packet', label: 'Packet Mode', type: 'toggle', value: true },
        ] },
      { id: 'fastplace', name: 'FastPlace', icon: 'hand', desc: 'Removes the block placement delay.',
        settings: [
          { id: 'delay', label: 'Delay', type: 'slider', min: 0, max: 4, step: 1, value: 0, unit: 't' },
        ] },
      { id: 'autoarmor', name: 'AutoArmor', icon: 'shield', enabled: true, desc: 'Equips the best armour automatically.',
        settings: [
          { id: 'hotbar', label: 'Allow Hotbar', type: 'toggle', value: false },
        ] },
      { id: 'autoeat', name: 'AutoEat', icon: 'utensil', desc: 'Eats when your hunger drops.',
        settings: [
          { id: 'threshold', label: 'Hunger Threshold', type: 'slider', min: 2, max: 19, step: 1, value: 14, unit: '/20' },
        ] },
      { id: 'autototem', name: 'AutoTotem', icon: 'ghost', enabled: true, desc: 'Keeps a totem in your offhand.',
        settings: [
          { id: 'health', label: 'Swap Below', type: 'slider', min: 1, max: 20, step: 1, value: 10, unit: 'hp' },
        ] },
      { id: 'autotool', name: 'AutoTool', icon: 'hammer', desc: 'Selects the fastest tool when mining.',
        settings: [
          { id: 'silk', label: 'Respect Silk Touch', type: 'toggle', value: false },
          { id: 'durability', label: 'Stop Low Durability', type: 'toggle', value: true },
        ] },
      { id: 'cheststealer', name: 'ChestStealer', icon: 'chest', desc: 'Empties chests into your inventory.',
        settings: [
          { id: 'delay', label: 'Delay', type: 'slider', min: 0, max: 500, step: 10, value: 60, unit: 'ms' },
        ] },
      { id: 'inventorycleaner', name: 'InventoryCleaner', icon: 'trash', desc: 'Drops junk items automatically.',
        settings: [
          { id: 'auto', label: 'Auto Drop', type: 'toggle', value: true },
          { id: 'keep', label: 'Keep Gapples', type: 'toggle', value: true },
        ] },
      { id: 'nofall', name: 'NoFall', icon: 'cloud', enabled: true, desc: 'Cancels fall damage.',
        settings: [
          { id: 'mode', label: 'Mode', type: 'select', options: ['Packet', 'NoGround', 'Spoof'], value: 'Packet' },
        ] },
      { id: 'scaffold', name: 'Scaffold', icon: 'layers', desc: 'Places blocks beneath you as you walk.',
        settings: [
          { id: 'mode', label: 'Mode', type: 'select', options: ['Normal', 'Tower', 'Legit'], value: 'Normal' },
          { id: 'rotate', label: 'Silent Rotate', type: 'toggle', value: true },
        ] },
      { id: 'fastdrop', name: 'FastDrop', icon: 'log-out', desc: 'Drops items without the delay.',
        settings: [
          { id: 'speed', label: 'Speed', type: 'slider', min: 1, max: 5, step: 1, value: 3, unit: 'x' },
        ] },
      { id: 'autorespawn', name: 'AutoRespawn', icon: 'rotate-ccw', desc: 'Respawns instantly after dying.',
        settings: [
          { id: 'enable', label: 'Enabled', type: 'toggle', value: true },
        ] },
      { id: 'autodisconnect', name: 'AutoDisconnect', icon: 'log-out', desc: 'Leaves the server at low health.',
        settings: [
          { id: 'health', label: 'Health', type: 'slider', min: 1, max: 20, step: 1, value: 6, unit: 'hp' },
        ] },
      { id: 'antiafk', name: 'AntiAFK', icon: 'activity', desc: 'Avoids AFK kicks with small moves.',
        settings: [
          { id: 'mode', label: 'Mode', type: 'select', options: ['Swing', 'Walk', 'Rotate'], value: 'Rotate' },
          { id: 'interval', label: 'Interval', type: 'slider', min: 5, max: 120, step: 5, value: 30, unit: 's' },
        ] },
    ],
  },

  {
    id: 'render',
    name: 'Render Visual',
    sub: '視覚・描画系',
    icon: 'eye',
    desc: 'Client-side visualisation of the world around you.',
    modules: [
      { id: 'xray', name: 'Xray', icon: 'diamond', desc: 'Reveals ores through solid blocks.',
        settings: [
          { id: 'opacity', label: 'Transparency', type: 'slider', min: 0, max: 100, step: 1, value: 70, unit: '%' },
          { id: 'ores', label: 'Ores Only', type: 'toggle', value: true },
        ] },
      { id: 'esp', name: 'ESP', icon: 'bracket', enabled: true, desc: 'Highlights entities through walls.',
        settings: [
          { id: 'mode', label: 'Style', type: 'select', options: ['Outline', 'Skeleton', 'Box'], value: 'Outline' },
          { id: 'color', label: 'Color', type: 'color', value: '#a855f7' },
        ] },
      { id: 'tracers', name: 'Tracers', icon: 'route', desc: 'Draws lines from you to targets.',
        settings: [
          { id: 'color', label: 'Line Color', type: 'color', value: '#3b82f6' },
          { id: 'thickness', label: 'Thickness', type: 'slider', min: 1, max: 5, step: 0.5, value: 2, unit: 'px' },
        ] },
      { id: 'nametags', name: 'Nametags', icon: 'tag', desc: 'Enlarges and restyles nametags.',
        settings: [
          { id: 'size', label: 'Size', type: 'slider', min: 0.5, max: 3, step: 0.1, value: 1.4, unit: 'x' },
          { id: 'health', label: 'Show Health', type: 'toggle', value: true },
        ] },
      { id: 'fullbright', name: 'Fullbright', icon: 'sun', enabled: true, desc: 'Removes darkness client-side.',
        settings: [
          { id: 'brightness', label: 'Brightness', type: 'slider', min: 1, max: 15, step: 1, value: 12, unit: 'lvl' },
        ] },
      { id: 'freecam', name: 'Freecam', icon: 'camera', desc: 'Detaches the camera from your body.',
        settings: [
          { id: 'speed', label: 'Fly Speed', type: 'slider', min: 0.5, max: 5, step: 0.1, value: 1.5, unit: 'x' },
        ] },
      { id: 'storageesp', name: 'StorageESP', icon: 'chest', desc: 'Highlights chests and shulkers.',
        settings: [
          { id: 'color', label: 'Color', type: 'color', value: '#f5a524' },
        ] },
      { id: 'itemesp', name: 'ItemESP', icon: 'image', desc: 'Highlights dropped items on the ground.',
        settings: [
          { id: 'enable', label: 'Enabled', type: 'toggle', value: true },
        ] },
    ],
  },

  {
    id: 'cosmetic',
    name: 'Cosmetic',
    sub: '見た目系',
    icon: 'palette',
    desc: 'Purely visual tweaks — sky, animations and effects.',
    modules: [
      { id: 'customsky', name: 'CustomSky', icon: 'cloud-moon', desc: 'Replaces the vanilla skybox texture.',
        settings: [
          { id: 'sky', label: 'Skybox', type: 'select', options: ['Galaxy', 'Sunset', 'Nebula'], value: 'Nebula' },
        ] },
      { id: 'custommodel', name: 'CustomModel', icon: 'box', desc: 'Swaps the model used by held tools.',
        settings: [
          { id: 'model', label: 'Model', type: 'select', options: ['Vanilla', 'Slim 1.7', 'Glow'], value: 'Vanilla' },
        ] },
      { id: 'customanimations', name: 'CustomAnimations', icon: 'move', enabled: true, desc: 'Changes the first-person hand motion.',
        settings: [
          { id: 'anim', label: 'Animation', type: 'select', options: ['1.7', 'Smooth', 'Modern'], value: '1.7' },
        ] },
      { id: 'cape', name: 'Cape / Cosmetics', icon: 'shirt', desc: 'Shows capes and cosmetic accessories.',
        settings: [
          { id: 'cape', label: 'Show Cape', type: 'toggle', value: true },
          { id: 'wings', label: 'Show Wings', type: 'toggle', value: false },
        ] },
      { id: 'timechanger', name: 'TimeChanger', icon: 'clock', desc: 'Pins the world time client-side.',
        settings: [
          { id: 'time', label: 'Time', type: 'slider', min: 0, max: 24000, step: 100, value: 13000, unit: 't' },
        ] },
      { id: 'weatherchanger', name: 'WeatherChanger', icon: 'cloud-rain', desc: 'Forces a weather state client-side.',
        settings: [
          { id: 'weather', label: 'Weather', type: 'select', options: ['Clear', 'Rain', 'Thunder'], value: 'Clear' },
        ] },
      { id: 'lowfire', name: 'LowFire', icon: 'flame', enabled: true, desc: 'Lowers the fire overlay while burning.',
        settings: [
          { id: 'height', label: 'Overlay Height', type: 'slider', min: 0, max: 100, step: 1, value: 30, unit: '%' },
        ] },
      { id: 'hitcolor', name: 'HitColor', icon: 'droplet', desc: 'Tints the screen when you take a hit.',
        settings: [
          { id: 'color', label: 'Color', type: 'color', value: '#ff3b5c' },
        ] },
      { id: 'particles', name: 'ParticleCustomizer', icon: 'sparkles', desc: 'Amplifies hit and critical particles.',
        settings: [
          { id: 'amount', label: 'Amount', type: 'slider', min: 0, max: 300, step: 10, value: 120, unit: '%' },
        ] },
      { id: 'motionblur', name: 'MotionBlur', icon: 'aperture', desc: 'Adds a subtle trailing screen blur.',
        settings: [
          { id: 'strength', label: 'Strength', type: 'slider', min: 0, max: 10, step: 0.1, value: 3.5, unit: '' },
        ] },
      { id: 'crosshair', name: 'CustomCrosshair', icon: 'focus', desc: 'Reshapes the crosshair to your taste.',
        settings: [
          { id: 'size', label: 'Size', type: 'slider', min: 2, max: 20, step: 1, value: 9, unit: 'px' },
          { id: 'thickness', label: 'Thickness', type: 'slider', min: 1, max: 4, step: 0.5, value: 2, unit: 'px' },
        ] },
    ],
  },

  {
    id: 'basefind',
    name: 'Base Find',
    sub: '拠点捜索系',
    icon: 'home',
    desc: 'Anubis-style structure and stash scanning for anarchy servers.',
    modules: [
      { id: 'basefinder', name: 'BaseFinder', icon: 'home', enabled: true, desc: 'Detects man-made block clusters.',
        settings: [
          { id: 'sensitivity', label: 'Sensitivity', type: 'slider', min: 1, max: 10, step: 1, value: 6, unit: '' },
          { id: 'range', label: 'Scan Range', type: 'slider', min: 32, max: 512, step: 32, value: 128, unit: 'b' },
        ] },
      { id: 'chestfinder', name: 'ChestFinder', icon: 'chest', desc: 'Scans for containers and logs coordinates.',
        settings: [
          { id: 'range', label: 'Range', type: 'slider', min: 16, max: 256, step: 16, value: 96, unit: 'b' },
          { id: 'chat', label: 'Log to Chat', type: 'toggle', value: true },
        ] },
      { id: 'portalfinder', name: 'PortalFinder', icon: 'door', desc: 'Detects nether portals nearby.',
        settings: [
          { id: 'enable', label: 'Enabled', type: 'toggle', value: true },
        ] },
      { id: 'bedfinder', name: 'BedFinder', icon: 'bed', desc: 'Locates beds for respawn anchors.',
        settings: [
          { id: 'enable', label: 'Enabled', type: 'toggle', value: true },
        ] },
      { id: 'spawnerfinder', name: 'SpawnerFinder', icon: 'scan', desc: 'Finds mob spawners underground.',
        settings: [
          { id: 'enable', label: 'Enabled', type: 'toggle', value: true },
        ] },
      { id: 'illegalblockfinder', name: 'IllegalBlockFinder', icon: 'alert-triangle', desc: 'Flags blocks that should not exist.',
        settings: [
          { id: 'enable', label: 'Enabled', type: 'toggle', value: true },
        ] },
      { id: 'stashfinder', name: 'StashFinder', icon: 'layers', desc: 'Identifies high-volume storage stashes.',
        settings: [
          { id: 'threshold', label: 'Min Containers', type: 'slider', min: 2, max: 64, step: 1, value: 12, unit: '' },
        ] },
      { id: 'newchunks', name: 'NewChunks', icon: 'map-pin', desc: 'Marks chunks generated by other players.',
        settings: [
          { id: 'color', label: 'Highlight', type: 'color', value: '#10d98a' },
        ] },
      { id: 'trailfinder', name: 'TrailFinder', icon: 'route', desc: 'Traces movement paths of players.',
        settings: [
          { id: 'line', label: 'Show Lines', type: 'toggle', value: true },
        ] },
      { id: 'worldguardfinder', name: 'WorldGuardFinder', icon: 'lock', desc: 'Detects claims and protected regions.',
        settings: [
          { id: 'enable', label: 'Enabled', type: 'toggle', value: true },
        ] },
    ],
  },

  {
    id: 'notify',
    name: 'Notification',
    sub: '通知・警報系',
    icon: 'bell',
    desc: 'Alerts that reach you outside the game window.',
    modules: [
      { id: 'basealert', name: 'BaseAlert', icon: 'bell', enabled: true, desc: 'Notifies when a base is detected.',
        settings: [
          { id: 'system', label: 'System Sound', type: 'toggle', value: true },
        ] },
      { id: 'playeralert', name: 'PlayerAlert', icon: 'user-check', desc: 'Warns when players come close.',
        settings: [
          { id: 'distance', label: 'Trigger Distance', type: 'slider', min: 8, max: 256, step: 8, value: 64, unit: 'b' },
        ] },
      { id: 'visualalert', name: 'VisualAlert', icon: 'alert-triangle', desc: 'Shows on-screen popup warnings.',
        settings: [
          { id: 'enable', label: 'Enabled', type: 'toggle', value: true },
        ] },
      { id: 'soundalert', name: 'SoundAlert', icon: 'volume', desc: 'Plays a detection sound alert.',
        settings: [
          { id: 'volume', label: 'Volume', type: 'slider', min: 0, max: 100, step: 5, value: 60, unit: '%' },
        ] },
      { id: 'webhook', name: 'WebhookNotifier', icon: 'link', desc: 'Sends detections to a Discord webhook.',
        settings: [
          { id: 'url', label: 'Webhook URL', type: 'text', value: 'https://discord.com/api/webhooks/…', placeholder: 'Paste webhook URL' },
        ] },
      { id: 'autologalert', name: 'AutoLogOnAlert', icon: 'log-out', desc: 'Disconnects when an alert fires.',
        settings: [
          { id: 'health', label: 'Health Threshold', type: 'slider', min: 1, max: 20, step: 1, value: 8, unit: 'hp' },
          { id: 'distance', label: 'Distance Threshold', type: 'slider', min: 8, max: 128, step: 8, value: 32, unit: 'b' },
        ] },
    ],
  },

  {
    id: 'settings',
    name: 'Client Settings',
    sub: '設定',
    icon: 'settings',
    desc: 'Keybinds, accent theme and client-wide preferences.',
    modules: [
      { id: 'keybind', name: 'Keybind Config', icon: 'keyboard', desc: 'The key that opens and closes this menu.',
        settings: [
          { id: 'openkey', label: 'Menu Toggle Key', type: 'keybind', value: 'ShiftRight' },
        ] },
      { id: 'themeselector', name: 'Theme Selector', icon: 'palette', desc: 'Accent colour used across the whole client.',
        settings: [
          { id: 'accent', label: 'Accent', type: 'theme', value: '#a855f7' },
          { id: 'glow', label: 'Glow Intensity', type: 'slider', min: 0, max: 100, step: 5, value: 70, unit: '%' },
        ] },
      { id: 'sound', name: 'Sound Effects', icon: 'music', enabled: true, desc: 'Subtle UI clicks and toggle blips.',
        settings: [
          { id: 'enable', label: 'Enabled', type: 'toggle', value: true },
        ] },
    ],
  },
];
