# DI-4_ScotlandYard

Dies ist das Repository für das SE2 Projekt der Gruppe DI-4. Mit Hilfe von SCRUM wird versucht das Brettspiel Scotland Yard als Android-Game umzusetzen.

## Spielbeschreibung
In diesem Spieleklassiker übernimmt ein Spieler die Rolle des geheimnisvollen Schurken Mister X, während die anderen Spieler die Rolle von Detektiven übernehmen, die Mister X fangen müssen. Im Gegensatz zum Original, mit der London-Karte als Spielbrett, wird bei dieser Version die Alpen-Adria Universität Klagenfurt mittels Google Maps und selbst arrangierten Routen als Spielumgebung verwendet.

<img width="281" height="500" src="https://raw.githubusercontent.com/s1lobnig/DI-4_ScotlandYard/master/Map.jpg">

Beim Erstellen des Spiels gibt es die Auswahlmöglichkeiten zu Random Events, Mister X als Bot oder die zufällige Vergabe von Mister X. Werden weder Mister X als Bot noch die zufällige Vergabe gewählt, so wird standardmäßig der Ersteller des Spiels zu Mister X.

Die Platzvergabe beim Spielstart geschieht zufällig. Die Position von Mister X ist nur bei bestimmten Spielzügen ersichtlich. Die Universität ist in eine Anzahl von nummerierten Feldern unterteilt, die jeweils zu Fuß, mit Bus oder Fahrrad erreicht werden können. Mister X erhält zusätzlich die Möglichkeit ein Taxi zu verwenden. Die Detektive - als auch Mister X - dürfen sich auf dem Spielplan mit den dementsprechenden Tickets fortbewegen, dabei ist zu beachten, dass nicht alle Felder mit allen Verkehrsmitteln erreicht werden können. Eine Fahrt geht zudem immer nur bis zum nächsten verbundenen Feld. Jeder Detektiv erhält folgendes Set an normalen Tickets: 5 Fußgeh-Tickets, 4 Fahrrad-Tickets und 2 Bus-Tickets. Mister X erhält eine unbegrenzte Anzahl an normalen Tickets, außerdem das Doppelzug-Ticket und soviele Black-Tickets (Detektive sehen nicht, welches Verkehrsmittel verwendet wurde), wie Detektive am Spiel teilnehmen. 

Nun beginnt das Spiel: Mister X macht seinen Zug immer zuerst. All seine Züge werden auf seinem digitalen Fahrplan gespeichert, dadurch sehen die Detektive, welches Verkehrsmittel er verwendet hat. Zu gewissen Zügen wird der Standort von Mister X preisgegeben. Mister X kann sich aber auch entschließen, einen Doppelzug zu machen. Dazu gibt er ein Kärtchen mit der Aufschrift "2x" ab, und kann dafür 2 Züge hintereinander durchführen. Die Vorgehensweise ist dabei genau so, wie bei einem einzelnen Zug. Mister X hat außerdem auch die sogenannten Black-Tickets zur Verfügung. Diese kann er anstelle der regulären Tickets für jedes beliebige Verkehrsmittel benutzen. Dies macht es natürlich schwieriger für die Detektive, zu ermitteln, wo Mister X sich gerade aufhält. Nachdem Mister X seinen Zug beendet hat, machen die Detektive ihre Züge. Die Detektive dürfen immer nur 1 Ticket in einer Runde benutzen. Sie behalten die Tickets danach nicht. Haben alle Detektive ihren Zug gemacht, ist Mister X wieder am Zug.

Das Spiel endet, wenn es einem Detektiv gelingt, auf das Feld zu ziehen, auf dem sich auch Mister X befindet. Mister X muss sich dann enttarnen und das Spiel ist sofort beendet und die Detektive haben gewonnen. Sollte sich Mister X allerdings zum letzten Mal gezeigt haben, und die Detektive ihn bis dahin nicht gefasst haben, so ist das Spiel ebenfalls beendet und Mister X gewinnt. Eine weitere Möglichkeit für das Spielende ist, dass kein Detektiv sich von seinem Standort aus bewegen kann, weil er keine Tickets mehr hat, oder aber genau die Tickets aufgeraucht hat, mit denen er weiter gehen könnte.

## Features
- Schummelfunktion:
durch das Auslösen des Nähe Sensors kann sich Mister X zwei Züge hintereinander machen.
- Schummeln Melden Funktion:
Hat ein Detektiv erkannt, dass Mister X geschummelt hat, so kann er dies melden.
  - Meldet er falsch, verliert er ein zufällig gewähltes Ticket. 
  - Wird Mister X drei Mal beim Schummeln ertappt, so gewinnen die Detektive das Spiel.
- Chat, in dem die Spieler miteinander kommunizieren können.
- Random Events:
Sie treten mit einer gewissen Wahrscheinlichkeit bei einem Zug auf, dadurch muss der Spieler beispielsweise den Zug rückgängig machen, er "verfährt" sich, oder darf zwei Runden ein gewisses Verkehrsmittel nicht mehr benutzen.
- Mister X als Bot:
Sollten zu wenig Spieler bei einem Spiel mitmachen, kann der Computer das Spielen von Mister X übernehmen. Dieser wählt zufällig seine Routen, versucht aber dennoch, den Detektiven aus dem Weg zu gehen.

## Rollen

**ArchitektIn**: René

**ArchitektIn Stv.**: Stefan und Johann

**GUI-Mensch**: Corinna

**GUI-Mensch Stv.**: Alem

**TesterIn**: Stefan

**TesterIn Stv.**: Selene und René

**CI-ManagerIn**: Selene

**CI-ManagerIn Stv.**: Corinna

**ProduktmanagerIn**: Johann

**ProduktmanagerIn**: Alem

## Zusätzliche Informationen

Unter folgenden Link wurden zusätzliche Dokumente abgelegt:
http://gofile.me/3IWdj/XFPUOUECx

Im folgenden Repository befinden sich die Proof of Concepts für die Netzwerkkommunikation:
https://github.com/r1rasser/ScotlandYard_POCs_Networking


[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=DI-4_ScotlandYard)](https://sonarcloud.io/dashboard?id=DI-4_ScotlandYard)

[![Build Status](https://travis-ci.com/s1lobnig/DI-4_ScotlandYard.svg?branch=master)](https://travis-ci.com/s1lobnig/DI-4_ScotlandYard)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=DI-4_ScotlandYard&metric=coverage)](https://sonarcloud.io/dashboard?id=DI-4_ScotlandYard)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=DI-4_ScotlandYard&metric=bugs)](https://sonarcloud.io/dashboard?id=DI-4_ScotlandYard)
[![Code smells](https://sonarcloud.io/api/project_badges/measure?project=DI-4_ScotlandYard&metric=code_smells)](https://sonarcloud.io/dashboard?id=DI-4_ScotlandYard)
