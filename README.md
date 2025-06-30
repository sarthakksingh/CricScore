
# 🏏 CricScore - A Simple Offline Cricket Scoring App

**CricScore** is an elegant, lightweight Android app built with Jetpack Compose that allows you to score and track local cricket matches **without needing an internet connection**. It’s perfect for gully matches, practice sessions, and offline scorekeeping.

---

## 🚀 Features

- ⚙️ **Match Setup**
  - Choose teams, overs, number of players.
  - Select opening batsmen and batting side.
  
- 📊 **Live Scoring**
  - Intuitive scoreboard interface.
  - Buttons for runs, wickets, wides, no balls, and extras.
  - Dynamic over tracking with ball-by-ball result history.

- 🔄 **Undo Feature**
  - Undo any ball with a single tap.

- 🧠 **Smart Run-Out Dialog**
  - Select who got out from dropdown.
  - Enter new batsman and strike position.

- 🆓 **No Ball & Free Hit Logic**
  - Accurately record no-ball runs and free hit results.
  - Handles edge cases like runs on no-ball before free hit.

- 📈 **Match Summary**
  - View innings-wise scorecards.
  - Ball-by-ball over breakdown.
  - Real-time updates via shared ViewModel.
  - Winning summary (by runs or wickets).

- 💾 **Match History** 
  - Save and view past matches.

---

## 🧱 Tech Stack

- **Kotlin** & **Jetpack Compose**
- **Navigation Component**
- **MVVM Architecture**
- **State Management** with `remember`, `mutableStateOf`, and `ViewModel`
- **Material3 UI** with Compose
- **Offline Only** – no network dependency

---



## 🛠️ How to Build

1. Clone the repo:
   ```bash
   https://github.com/sarthakksingh/CricScore.git
   ```

2. Open the project in **Android Studio ** .

3. Run the app on a device or emulator (no API key or backend needed).

---

## 🧠 Future Improvements
- Suggestions are welcomed
- 🎨 Dark mode and themes

---

## 🤝 Contributing

Pull requests and suggestions are welcome! For major changes, open an issue first to discuss what you'd like to change.

---

## 📄 License

MIT License © 2025 [Sarthak Singh]
