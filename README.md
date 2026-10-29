# Iekei Client

Minecraft Java Edition (Fabric) 向けユーティリティクライアント **Iekei Client** の
ClickGUI（設定画面）です。ゲーム内で **[Right Shift]** を押すとメニューが開きます。

- 対象: Minecraft **26.3** / Fabric Loader 0.20.1 / Fabric API 0.165.0+26.3 / Java 25
- Mod ID: `iekeiclient`
- ライセンス: CC0-1.0

```
ClickGUI（ゲーム内）
 ├─ 左レール : 8カテゴリ（Movement / Combat / Player Utility / Render Visual /
 │              Cosmetic / Base Find / Notification / Client Settings）
 ├─ 上部     : タイトル・[Right Shift] 表記・リアルタイム検索・テーマボタン・閉じる
 ├─ 中央     : モジュールカード（2カラム）＋アコーディオン式の詳細設定
 └─ 下部     : アクセントカラー5色・接続ステータス・有効モジュール数
```

## ビルド（jar の生成）

```bash
# リポジトリ直下で
./gradlew build
```

成功すると以下に jar が生成されます。

```
build/libs/iekeiclient-1.0.0.jar
```

> 初回ビルドは Loom が Minecraft / Fabric API / 依存ライブラリをダウンロードするため
> ネットワーク接続が必要です（数分かかります）。

開発中にそのまま起動して確認する場合:

```bash
./gradlew runClient
```

## 導入

1. [Fabric Loader](https://fabricmc.net/use/installer/) で **Minecraft 26.3** のプロファイルを作成
2. `~/.minecraft/mods/` に次の2つを入れる
   - `iekeiclient-1.0.0.jar`（`build/libs` から）
   - **Fabric API**（26.3 対応版）
3. ゲームを起動し、ワールドに入って **[Right Shift]** を押す
   → ClickGUI がフェードイン表示されます。もう一度押すと閉じます。

キー割当はバニラの **オプション → 操作 → キー設定 → Iekei Client → Open ClickGUI**
から変更できます（メニュー内 `Client Settings → Keybind Config` でも表示を確認できます）。

## 使い方

| 操作 | 動作 |
| --- | --- |
| `Right Shift` | ClickGUI の開閉（ゲーム内 / メニュー表示中どちらでも） |
| `Esc` | メニューを閉じる（検索欄にフォーカス中は検索を解除） |
| 左レールのカテゴリ | カテゴリ切り替え |
| 検索欄をクリックして入力 | 全カテゴリ横断でリアルタイム絞り込み（Backspace で削除） |
| カードのヘッダーをクリック | アコーディオン式に詳細設定を展開／収納 |
| カード右側のスイッチ | モジュールの ON / OFF |
| 設定行 | トグル：クリックで切替 / スライダー：ドラッグで数値変更 / 選択：クリックで切替 |
| `Toggle all` | そのカテゴリを一括 ON / OFF |
| パレットボタン / 下部スウォッチ | アクセントカラー変更（紫・赤・青・緑・アンバー） |
| マウスホイール | モジュール一覧のスクロール |

アクセントカラーはトグルスイッチ・スライダー・アイコンの線色・枠線の光りまで
一括で切り替わります（`Theme.setAccent` 以下の CSS 変数相当）。

## 構成

```
src/main/java/com/iekei/IekeiClient.java          … 共通エントリポイント
src/client/java/com/iekei/client/
 ├─ IekeiClientClient.java                        … キーバインド登録・開閉処理
 └─ gui/
     ├─ ClickGuiScreen.java                       … ClickGUI 本体（描画・入力）
     ├─ Registry.java                             … 8カテゴリ / 78モジュールの定義
     ├─ Category.java / Module.java / Setting.java
     ├─ Draw.java                                 … 角丸・グラデ・クリップ・文字描画
     ├─ Icons.java                                … 線画SVG相当のインラインアイコン
     └─ Theme.java                                … アクセントカラー / 配色
src/main/resources/
 ├─ fabric.mod.json
 └─ assets/iekeiclient/ (icon.png, lang/en_us.json, lang/ja_jp.json)
```

## デザイン仕様（Web モックアップ）

`clickgui/` には、この UI のデザイン確認用 Web モックアップ（HTML/CSS/JS）が入っています。
ブラウザで `clickgui/index.html` を開くと、同じ配色・同じモジュール構成の
リッチ版（グラスモーフィズム、アニメーション、テーマカスタマイザ）を確認できます。

```bash
cd clickgui && python3 -m http.server 8000   # http://localhost:8000
```

## 補足

- 本 Mod が実装しているのは **ClickGUI（クライアント画面）そのもの** です。
  各モジュールの ON/OFF 状態と設定値は UI 上で保持・操作できますが、
  実際のチート処理（Fly 等の挙動）は含みません。UI の土台としてお使いください。
- ゲーム内描画は Minecraft の `GuiGraphicsExtractor` による即時描画で、
  背景にワールドが透けるため半透明パネルがガラスのように見えます。
