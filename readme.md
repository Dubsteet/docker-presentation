# Praxisbeispiel 1 - Erstellen eines eigenen Images
Im ersten Beispiel war das Ziel ein Dockerimage eines Javaprogramms zu erstellen, damit es in einem Container gestartet werden kann. Es handelt sich dabei um ein Javaprogramm, welches eine Zufallszahl generiert und anschließend in der Konsole ausgibt.
Im Vortrag wurde das vorhandene Javaprogramm erweitert und anschließend die Dockerfile gezeigt.
## Aufbau der Dockerfile
```
FROM openjdk
```
Lädt als Grundlage das [offizielle OpenJDK Image](https://hub.docker.com/_/openjdk) herunter. Da Java dort bereits installiert ist sind dafür keine weiteren Schritte erforderlich.
```
COPY . /src/java
```
Durch den Punkt werden alle Dateien aus diesem Ordner in das Image in /src/java kopiert. In diesem Fall ist das nur die Datei _Beispiel.java_.
```
WORKDIR /src/java 
```
Setzt das Working Directory auf das Verzeichnis "/src/java". Dadurch werden alle weiteren Befehle in diesem Verzeichnis ausgeführt. (Vergleichbar mit cd unter Linux)
```
RUN ["javac", "Beispiel.java"]
```
Führt den Befehl "javac Beispiel.java" aus. Dadurch wird das Javaprogramm kompiliert.
```
ENTRYPOINT ["java", "Beispiel"]
```
Der Entrypoint gibt an, was beim Start des Containers ausgeführt werden soll.
## Erstellen des Dockerimages und starten des Containers
```
docker build -t beispiel-app:0.1 .
```
Dieser Befehl wird benötigt um das Image zu bauen. Das Image wird nun als beispiel-app mit der Version 0.1 angelegt. 
```
docker image ls
```
Man erhält eine Auflistung aller Images. Für das weitere Vorgehen ist die _Image ID_ wichtig. 
```
docker run <IMAGE ID>
```
Es wird ein Container des Images mit der _Image ID_ gestartet. Bei der Image ID werden jedoch nur die ersten Zeichen benötigt, wenn diese eindeutig sind. In der Konsole ist jetzt der Output des Containers zu sehen. Die generierten Zufallszahlen sind auszulesen. 
Um den Container zu verlassen wird die Tastenkombination _STRG + C_ benötigt. Der Container läuft nun jedoch im Hintergrund weiter. 
```
docker ps
```
Mit Docker lassen sich alle momentan laufenden Container auflisten. Wichtig um den Container zu stoppen ist die _Container ID_ 
```
docker container stop <Container ID>
```
Hiermit lässt sich der Container mit der angegebenen ID beenden.

# Praxisbeispiel 2 - Starten eines Minecraft Servers mit Docker Compose
Im zweiten Praxisbeispiel wird ein Minecraft Server in einem Docker Container gestartet. Hierfür wird die Funktion _docker-compose_ verwendet. Mit dieser lässt sich ein Container mit Parametern aus einer yml Datei starten. Dadurch muss nicht bei jedem Start des Containers der _docker run_ Befehl mit vielen Parametern verwendet werden.
## Aufbau der docker-compose.yml
```
version: "3"

services:
  minecraft:
    image: itzg/minecraft-server
```
Es wird die Docker-Compose Version 3 verwendet und ein Service mit dem Namen "minecraft" erstellt. Dafür wird ein [bereits vorhandenes Image](https://github.com/itzg/docker-minecraft-server) verwendet. Dieses bietet eine Menge Einstellungsmöglichkeiten und ist bereits sehr ausgereift.
```
    ports:
        - 25565:25565
    volumes:
        - "mc:/data"
``` 
Um auf den Server zu verbinden muss der Port _25565_ aus dem Container freigegeben werden. Dies ist der Standartport eines Minecraft Servers. 
Außerdem wird ein Volume benötigt. Ohne dieses würde der Container bei jedem neustart ein neues anonymes Volume starten und nicht den Speicherstand vor dem Neustart laden.
```
    environment:
      EULA: "TRUE"
      VERSION: 1.18.1
      TYPE: "PAPER"
      MAX_PLAYERS: 30
```
Hier werden die Umgebungsvariablen gesetzt. Zuerst muss der [Minecraft EULA](https://account.mojang.com/documents/minecraft_eula) zugestimmt werden. Anschließend die Minecraft Version _1.18.1_ verwendet. Als Server Software wird [PaperMC](https://papermc.io/) verwendet. Die maximale Spielerzahl wird auf 30 gesetzt. Viele weitere Einstellungsmöglickeiten sind in der guten Dokumentation des [verwendeten Images](https://github.com/itzg/docker-minecraft-server) vorhanden.
```
    restart: unless-stopped
volumes:
  mc:
```
Nun wird noch die Restart Policy darauf gesetzt, dass der Server auch beim Absturz neustartet, bis er manuell gestoppt wird. Außerdem muss erneut das oben erstelle Volume angegeben werden.
## Starten des Containers mit docker-compose
Die Datei wird nun als _docker-compose.yml_ in ein Verzeichnis gelegt. Mit dem Terminal wird nun in diesen navigiert.
```
docker-compose up -d
```
Um den Container zu starten ist nun nur dieser Befehl benötigt. Der Minecraftserver startet nun selbstständig und ist kurze Zeit später erreichbar.
```
docker-compose logs -f
```
Mit diesem Befehl kann nun die Konsole des Servers gelesen werden. Wenn eine neue Nachricht in der Konsole erscheint wird diese außerdem automatisch angezeigt.
```
docker-compose stop
```
Mit diesem Befehl kann der Container nun wieder beendet werden.