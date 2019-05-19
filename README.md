# DI-4_ScotlandYard

Repository für das SE2 Projekt der Gruppe DI-4. Mit Hilfe von SCRUM wird versucht das Brettspiel Scotland Yard für Android umzusetzen.

## Spielbeschreibung
In diesem Spieleklassiker übernimmt ein Spieler die Rolle des geheimnisvollen Schurken Mister X, während die anderen Spieler die Rolle von Detektiven übernehmen, die dann Mister X fangen müssen. Im Gegensatz zum Original, mit der Londonkarte als Spielbrett, gestalten wir die Alpen-Adria Universität Klagenfurt mittels Google Maps und selbst arrangierten Routen als Spielumgebung. 
![Screenshot der Map](https://raw.githubusercontent.com/s1lobnig/DI-4_ScotlandYard/master/Screenshot_20190518-144325.jpg | width=100)

Beim Erstellen der Spiels kann eine zufällige Vergabe von Mister X gewählt werden, andernfalls einigen sich die Spieler auf eine Person.

Zunächst wählt jeder Detektiv eine Farbe. Die Platzvergabe beim Spielstart geschieht zufällig. Die Position von Mister X ist nur bei bestimmten Spielzügen ersichtlich. Die Universität ist in eine Anzahl von nummerierten Feldern unterteilt, die jeweils zu Fuß, mit Bus oder Fahrrad angefahren werden können. Mister X erhällt noch die Möglichkeit, ein Taxi zu verwenden. Die Detektive - sowie auch Mister X - dürfen sich auf dem Spielplan mit den dementsprechenden Tickets fortbewegen. Dabei ist zu beachten, dass nicht alle Felder mit allen Verkehrsmitteln angefahren werden können. Eine Fahrt geht außerdem immer nur bis zum nächsten verbundenen Feld der gleichen Art. Jeder Detektiv erhält 5 Fußgeh-Tickets, 4 Fahrrad-Tickets und 2 Bus-Tickets. Mister X erhält eine unbegrenzte Anzahl an normalen Tickets, außerdem das Doppelzug Ticket und soviele von den Black Tickets (Detektive sehen nicht, welches Verkehrsmittel verwendet wurde), wie Detektive am Spiel teilnehmen. 

Nun beginnt das Spiel: Mister X macht seinen Zug immer zuerst. Alle seine Züge werden auf seinem digitalen Fahrplan gespeichert. Dadurch sehen die Detektive, welches Verkehrsmittel er verwendet hat. Zu gewissen Zügen wird der Standort von Mister X preisgegeben. Mister X kann sich aber auch entschließen, einen Doppelzug zu machen. Dazu gibt er ein Kärtchen mit der Aufschrift "2x" ab, und kann dafür 2 Züge hintereinander durchführen. Die Vorgehensweise ist dabei genau so, wie bei einem einzelnen Zug. Mister X hat außerdem auch die sog. Black Tickets zu seiner Verfügung. Diese kann er anstelle der regulären Tickets für jedes beliebige Verkehrsmittel benutzen. Dies macht es natürlich schwierig für die Detektive zu ermitteln, wo Mister X sich gerade aufhält. Danach machen die Detektive ihren Zug. Die Detektive dürfen immer nur 1 Ticket in einer Runde benutzen. Sie behalten die Tickets danach nicht. Haben alle Detektive ihren Zug gemacht, ist Mister X wieder dran.

Das Spiel endet, wenn es einem Detektiv gelingt, auf das gleiche Feld zu ziehen, auf dem sich auch Mister X befindet. Mister X muss sich dann enttarnen und das Spiel ist sofort beendet. Nachdem sich Mister X zum letzten Mal gezeigt hat, ist das Spiel ebenfalls beendet. Sollten ihn die Detektive bis dahin nicht gefasst haben, hat er nun das Spiel gewonnen.

## Angedachte Features
Ein Chat, in dem die Detektive miteinander kommunizieren können.

Random Events, welche mit eienr gewissen Wahrscheinlichkeit bei einem Zug auftreten. Dadurch muss der Spieler beispielsweise den Zug rückgängig machen oder darf zwei Runden ein gewisses Verkehrsmittel nicht mehr benutzen.


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

[![Build Status](https://travis-ci.com/s1lobnig/DI-4_ScotlandYard.svg?branch=master)](https://travis-ci.com/s1lobnig/DI-4_ScotlandYard)
[![Quality gate](https://sonarcloud.io/api/project_badges/measure?project=DI-4_ScotlandYard&metric=alert_status)](https://sonarcloud.io/dashboard?id=DI-4_ScotlandYard)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=DI-4_ScotlandYard&metric=coverage)](https://sonarcloud.io/dashboard?id=DI-4_ScotlandYard)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=DI-4_ScotlandYard&metric=bugs)](https://sonarcloud.io/dashboard?id=DI-4_ScotlandYard)
[![Code smells](https://sonarcloud.io/api/project_badges/measure?project=DI-4_ScotlandYard&metric=code_smells)](https://sonarcloud.io/dashboard?id=DI-4_ScotlandYard)
