# Art Dealer

## Description
<!-- [Provide a brief description of the game, including its objective, gameplay mechanics, and any unique features.] -->
 Art Dealer is a card-based game, written in Java by Dustin Yochim, Ellis Twiggs Jr, Jonathan Schweighauser, Adam Loepker, and Yosef Alqufidi in the Spring 2024 Semester as part of the Introduction to Software Profession class at the University of Missouri - Saint
 Louis.

 ## Introduction
 Welcome to Art Dealer, the thrilling card game where you go head-to-head against the computer, known as the "Art Dealer," in a battle of wits and pattern recognition. Your mission? Unravel the mystery behind the dealer's card selection pattern and outsmart them at their own game.

In each round, you'll be presented with a challenge: pick four cards that you believe the Art Dealer will also choose. But beware, the dealer's selections are shrouded in secrecy, and it's up to you to decipher the pattern behind their choices.

Successfully match the dealer's picks and earn a point. Rack up two points, and you'll advance to the next round, where a new and even more perplexing pattern awaits.

Do you have what it takes to outmaneuver the Art Dealer and emerge victorious in this captivating game of strategy and deduction? It's time to find out in Art Dealer!

 ## Screenshots
 ### Welcome Screen
 <img width="1012" alt="Screenshot 2024-04-30 at 11 40 38 AM" src="https://github.com/DustinYochim/Art-Dealer/assets/70305015/dd6d05b7-4ff5-4f3b-98bd-a9e659099977">

 ### Instructions Screen
 <img width="1012" alt="Screenshot 2024-04-30 at 11 41 16 AM" src="https://github.com/DustinYochim/Art-Dealer/assets/70305015/3f7b0c88-bfdb-452f-961f-7d61aac7e017">

 ### Game Screen
 <img width="1012" alt="Screenshot 2024-04-30 at 11 42 02 AM" src="https://github.com/DustinYochim/Art-Dealer/assets/70305015/1587bab2-b57e-47c8-b34a-ed61bc11bc5a">


 ### Goodbye Screen
<img width="1012" alt="Screenshot 2024-04-30 at 11 42 06 AM" src="https://github.com/DustinYochim/Art-Dealer/assets/70305015/17ba74d0-6d43-47a2-b837-cbfd05717959">

## Programmers Guide
1. To run the program, clone the repository from https://github.com/DustinYochim/Art-Dealer down to your local machine.
2. Then open the program in your favorite IDE (we developed using IntelliJ).
3. Once you have the program open in your IDE, to build the program you may have to select the Main class.
4. The "Main" class of this project is App.java.
5. Once you have the program open in your IDE and have selected App.java as the "Main" class. Click the "Run" option in your IDE to start the App!

## How to Play
### Selection Process
1. You will pick four cards, one by one.
2. Each time you make a selection, the program will display the chosen card immediately.
3. If you attempt to pick a card that has already been chosen in the current round, you’ll receive an error message and be asked to choose another card.
4. If you attempt to pick the same 4 cards more than once in a round you will be forced to pick your cards again.

### Round Structure
1. After you pick four cards, the Art Dealer will make its selection based on a predetermined pattern.
2. The Art Dealer’s selections will be displayed to you with a red border, indicating which cards it has chosen.
3. You can choose to continue playing more rounds or stop the program altogether.

### History Display
1. All selected cards, along with an indication of which cards were chosen by the Art Dealer, will be added to the history.
2. Cards denoted with a * were chosen by the dealer.
3. You can view the history to keep track of your progress.

