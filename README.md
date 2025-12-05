# Bowling League 

## Project Overview
The Bowling League project is a JavaFX application designed to simulate a bowling game and an overall league. It features a tournament format with bracket representation, moving beyond a simple text-based interface to a graphical UI. 
The application manages teams, tracks scores, and determines winners through a bracket-style tournament.

## Team Members & Roles
* **Jordan Charlie** - Front End
* **Branson McLaughlin** - Scrum Master
* **Gage Trevino** - Back End
* **Hamza Quadri** - Backup Back End
* **Evan Ernst** - Backup Front End

### Key Classes
* **App:** The main JavaFX application that sets up the UI, teams, and players.
* **Bracket:** Manages the tournament structure, pairs teams, tracks rounds, and determines the final champion.
* **Frame:** Represents a single bowling frame, storing rolls and checking for strikes/spares.
* **Matchup:** Represents a pairing between two teams, handling scores and bye weeks.
* **Pin:** Tracks the status (knocked down/standing) of a single pin.
* **Player:** Models a bowler with a name and associated team.
* **RackOfPins:** Visually represents pins using JavaFX circles and handles pin mechanics.
* **Score:** Tracks rolls, calculates totals (including bonuses), and manages frame completion.
* **Team:** Represents a team with a name, score, and collection of players.

## Diagrams
* [UML Design (Lucidchart)](https://lucid.app/lucidchart/4194f3b3-3c46-413c-94eb-c20405986bd3/edit?page=HWEp-vi-RSFO&invitationId=inv_e9be37c7-7623-4987-8c8b-88fe8b27f08e#)
* [Sequence Diagram (Lucidchart)](https://lucid.app/lucidchart/3e57aeb6-ded4-4b28-b9af-d6026c90ccc5/edit?page=0_0&invitationId=inv_588c706a-854f-4e33-9d29-102b8a431756#)

## Current Features
* Visual representation of bowling pins and lanes.
* Tournament bracket system that tracks progression.
* Logic for frames, rolls, and basic scoring.
* Team and player management.
