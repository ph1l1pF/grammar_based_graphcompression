Dies ist die Implementierung des grammatik-basierten Graphkompression im Rahmen der Bachelorarbeit von Matthias D�rksen.

Dies Verzeichnis beinhaltet im wesentlichen den Quellcode (Verzeichnis 'src'), drei Beispielgrahen (Verzeichnis 'GraphExcamples)
und die Dokumentation des Quellcodes (Verzeichnis 'doc'). 

Das Eclipse-Projekt kann als ein solches in Eclipse importiert werden.
In dem Projekt wird die GraphSteam-Library benutzt, sodass diese gegebenenfalls noch in Eclipse eingebunden werden muss.
Die notwendigenn Librarys befindet sich in dem Ordner "lib" und k�nnen beispielsweise �ber
"Project"->"Properties"->"Java Build Path"->"Libraries"->"Add JARs..." eingebunden werden.

Die Schritt-f�r-Schritt Simulation kann �ber die Klasse "control.MyFrame" ausgef�hrt werden.
F�r eine Kompression eines zuf�lligen Graphen (ohne Simulationsmodus) muss die Klasse "control.RandomGraph" ausgef�hrt werden.

Um einen Graphen aus einer Text-Datei auszulesen muss sich dierer in einem bestimmte Format befinden.
Daf�r werden zun�chst die Knoten, getrennt durch einen neue Zeile, mittels id':'Knotenlabel angegeben. Die ID muss eine
eindeutige positive Zahl sein.
Anschlie�end werden die Kanten aufgelistet und zun�chst mit '#' (in einer extra Zeile) von den Knoten abgegrenzt.
Eine Kante wird mit Kantenlabel ':' Menge der Startknoten '->' Menge der Zielknoten beschrieben.
In der Menge der Start- und Zielkonten werden die Knoten-Id, getrennt mit ';', angeben.

Wie ein Graph konkret aussieht, kann an diesem Beispielgraphen betrachtet werden:
0:n1
1:n2
2:n3
3:n1
4:n2
5:n3
#
e2:1->0
e2:4->3
e3:1->2
e3:4->5
e1:0;3->1;4


