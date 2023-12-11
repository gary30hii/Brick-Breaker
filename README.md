# COMP2042_CW_hcygh2

# Brick Breaker - Game Overview

## Table of Contents
- [1. Overview](#1-overview)
- [2. User Interface](#2-user-interface)
  - [2.1 Main Menu Screen](#21-main-menu-screen)
  - [2.2 Gameplay Screen](#22-gameplay-screen)
  - [2.3 Leaderboard Screen](#23-leaderboard-screen)
  - [2.4 Game Over/Win Screens](#24-game-overwin-screens)
- [3. Gameplay Mechanics](#3-gameplay-mechanics)
  - [3.1 Objective](#31-objective)
  - [3.2 Progression Through Levels](#32-progression-through-levels)
  - [3.3 Types of Bricks](#33-types-of-bricks)
  - [3.4 Scoring System](#34-scoring-system)
  - [3.5 Bonus Mechanics](#35-bonus-mechanics)
  - [3.6 Game End](#36-game-end)
- [4. Saving and Loading](#4-saving-and-loading)
  - [4.1 Saving Your Game](#41-saving-your-game)
  - [4.2 Loading Your Game](#42-loading-your-game)
- [5. Sound and Music](#5-sound-and-music)
- [6. Controls and Interactions](#6-controls-and-interactions)

## 1. Overview

**Title:** Brick Breaker  
**Theme:** NBA Golden State Warriors  
**Genre:** Puzzle / Arcade  
**Platforms:** Java-supported (Windows, macOS, Linux)  

**Description:**  
Experience the thrill of basketball meets strategic brick breaking in Brick Breaker. Themed around the NBA's Golden State Warriors, this game offers dynamic visuals and sounds, embodying the spirit of the team and basketball.

## 2. User Interface

### 2.1 Main Menu Screen
- **Start:** New game at level 1 with 5 lives.
- **Load Game:** Resume a saved game.
- **Leaderboard:** View top scores.
- **Mute/Unmute Button:** Audio control.

### 2.2 Gameplay Screen
- Themed bricks and paddle.
- Score and level indicators.
- Heart icons for lives.

### 2.3 Leaderboard Screen
- Displays high scores.

### 2.4 Game Over/Win Screens
- Shows current and highest scores.

## 3. Gameplay Mechanics

### 3.1 Objective
Clear the court-themed screen of bricks by bouncing a basketball with a paddle. Starts at level 1, increasing in difficulty.

### 3.2 Progression Through Levels
- **Design:** Varied brick arrangements.
- **Advancement:** More rows every two levels.
- **Difficulty:** Lock blocks from level 16; all lock blocks from level 21.

### 3.3 Types of Bricks
- **Normal:** 1 point, then disappears.
- **Heart:** Extra life.
- **Mysterious:** Bonus item.
- **Star:** Super Ball mode for 5 seconds.
- **Lock Block:** Changes type after first hit.

### 3.4 Scoring System
- **Points:** 1 point per hit (except lock blocks).
- **Super Ball:** Aggressive play, no heart loss.

### 3.5 Bonus Mechanics
- **Items:** From mysterious bricks, activated on paddle contact.
- **Split Ball:** Two additional balls without life penalty.

### 3.6 Game End
Ends when lives are lost or all levels are cleared. High scores can be entered on the leaderboard.

## 4. Saving and Loading

### 4.1 Saving Your Game
- **Method:** Press "S" during gameplay.
- **Confirmation:** On-screen message.
- **Stored Data:** Score, level, lives, brick state, bonuses, paddle position.

### 4.2 Loading Your Game
- **From Main Menu:** Choose "Load Game."
- **Resume:** Picks up from the last saved state.

## 5. Sound and Music

- **In-Game:** "M" key toggles sound.
- **Menu Screens:** Clickable mute/unmute button.

## 6. Controls and Interactions

- **Paddle Movement:** Left and right arrow keys.
- **Sound Toggle:** "M" key.
- **Save Game:** "S" key.
