package com.example.scotlandyard.map.motions;

import java.security.SecureRandom;
import java.util.Random;

public class RandomEvent {
    private int id;
    private String[][] allTexts;
    private String text;
    private Random randomNumber;
    private int secID;

    public RandomEvent() {
        randomNumber = new SecureRandom();
        this.allTexts = new String[10][10];
        this.id = randomNumber.nextInt(100) % 4;
        secID = randomNumber.nextInt(100) % 4;
        fillText();

        this.text = allTexts[id][secID];
    }


    public int getID() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public int getSecID() {
        return this.secID;
    }

    private void fillText() {
        //Diese Runde aussetzen
        this.allTexts[0][0] = "Anstatt dich weiter auf die Suche zu begeben, Spielst du lieber eine Runde Fußball mit deinen Freunden! Diese Runde setzt du aus.";
        this.allTexts[0][1] = "Du entscheidest dich spontan ein kleines schläfchen einzulegen. Setze diese Runde aus";
        this.allTexts[0][2] = "Dieser Tag ist ein schöner Tag. Anstatt dich weiter zu bewegen, entscheidest du dich, einen abstecher zum Wörthersee zu machen. Setze diese Runde aus.";
        this.allTexts[0][3] = "Das ganze suchen hat dich durstig gemacht. Du gönnst dir ein Bier beim Uniwirt und setzt deshalb eine Runde aus";

        //Rückgängig
        this.allTexts[1][0] = "Du hast keine Lust weiterzugehen und begibst deshalb zu deinem letzten standpunkt zurück.";
        this.allTexts[1][1] = "Du hast etwas verloren und bist es zur letzten Position suchen gegangen.";
        this.allTexts[1][2] = "Mir follt nix mehr ein an text deshalb gehst du jetzt zum letzten Punkt zurück";
        this.allTexts[1][3] = "Halt. Martin Hitz der Swagger schickt dich zum letzten Punkt zurück.";

        //BEschränkte fortbewegung
        this.allTexts[2][0] = "Du verletzt dich. Das Fahhrad ist für diesen und die nächsten 2 Runden nicht einsatzfähig.";
        this.allTexts[2][1] = "Durch eigene Inkompetenz kollidierst du mit einem Hinderniss. Du kannst für die diesen und die nächsten 2 Runden nicht Fahrrad fahren.";
        this.allTexts[2][2] = "Dein Fahrrad ist kaputt. Es ist für diesen und die nächsten 2 Runden nicht einsatzfähig";
        this.allTexts[2][3] = "Du bist schlecht auf der Uni deshalb zerstörst du dein Fahrrad. Es ist diesen und die nächsten 2 Runden nicht einsatzbereit.";

        this.allTexts[3][0] = "DU warst unaufmerksam und hast dich deshalb verirrt";
        this.allTexts[3][1] = "Du hast einen/r hübsche/n Stundenten/in hinterhergeschaut und bist deshalb falsch gegangen.";
        this.allTexts[3][2] = "Du wurdest von Martin Hitzs KOllegen entführt und kommst an einen anderen Ort heruas.";
        this.allTexts[3][3] = "Weine nicht wenn der Regen fällt. DAM DAM. Ähm du kommst wo anders raus als gewollt.";
    }

    public void setID(int randomEvent) {
        this.id = randomEvent;
    }
}
