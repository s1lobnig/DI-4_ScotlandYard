package com.example.scotlandyard.map.motions;

import java.util.Random;

public class RandomEvent {
    private int id;
    private String[][] AllTexts;
    private String Text;
    private Random randomNumber;

    public RandomEvent() {
        randomNumber = new Random();
        this. AllTexts = new String[10][10];
        this.id = randomNumber.nextInt(100) %3;
        //this.id = 2;
        int curID = randomNumber.nextInt(100) %4;
        fillText();

        this.Text = AllTexts[id][curID];
    }




    public int getID(){
        return this.id;
    }
    public String getText(){
        return this.Text;
    }

    private void fillText() {
        //Diese Runde aussetzen
        this.AllTexts[0][0] = "Anstatt dich weiter auf die Suche zu begeben, Spielst du lieber eine Runde Fußball mit deinen Freunden! Diese Runde setzt du aus.";
        this.AllTexts[0][1] = "Du entscheidest dich spontan ein kleines schläfchen einzulegen. Setze diese Runde aus";
        this.AllTexts[0][2] = "Dieser Tag ist ein schöner Tag. Anstatt dich weiter zu bewegen, entscheidest du dich, einen abstecher zum Wörthersee zu machen. Setze diese Runde aus.";
        this.AllTexts[0][3] = "Das ganze suchen hat dich durstig gemacht. Du gönnst dir ein Bier beim Uniwirt und setzt deshalb eine Runde aus";

        //Rückgängig
        this.AllTexts[1][0] = "Du hast keine Lust weiterzugehen und begibst deshalb zu deinem letzten standpunkt zurück.";
        this.AllTexts[1][1] = "Du hast etwas verloren und bist es zur letzten Position suchen gegangen.";
        this.AllTexts[1][2] = "Mir follt nix mehr ein an Text deshalb gehst du jetzt zum letzten Punkt zurück";
        this.AllTexts[1][3] = "Halt. Martin Hitz der Swagger schickt dich zum letzten Punkt zurück.";

        //BEschränkte fortbewegung
        this.AllTexts[2][0] = "Du verletzt dich. Das Fahhrad ist für diesen und die nächsten 2 Runden nicht einsatzfähig.";
        this.AllTexts[2][1] = "Durch eigene Inkompetenz kollidierst du mit einem Hinderniss. Du kannst für die diesen und die nächsten 2 Runden nicht Fahrrad fahren.";
        this.AllTexts[2][2] = "Dein Fahrrad ist kaputt. Es ist für diesen und die nächsten 2 Runden nicht einsatzfähig";
        this.AllTexts[2][3] = "Du bist schlecht auf der Uni deshalb zerstörst du dein Fahrrad. Es ist diesen und die nächsten 2 Runden nicht einsatzbereit.";

        this.AllTexts[3][0] = "DU warst unaufmerksam und hast dich deshalb verirrt";
        this.AllTexts[3][1] = "Du hast einen/r hübsche/n Stundenten/in hinterhergeschaut und bist deshalb falsch gegangen.";
        this.AllTexts[3][2] = "Du wurdest von Martin Hitzs KOllegen entführt und kommst an einen anderen Ort heruas.";
        this.AllTexts[3][3] = "Weine nicht wenn der Regen fällt. DAM DAM. Ähm du kommst wo anders raus als gewollt.";
    }
}
