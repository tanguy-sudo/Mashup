## Table des matières
1. [Info générale](#info-générale)
2. [Technologies](#technologies)
3. [Executables](#executables)
4. [Installations](#installations)
5. [Exemples d'utilisation](#exemples-dutilisation)
6. [Collaboration](#collaboration)

## Info Générale
***
Ce projet a été réalisé dans la cadre de ma formation en master informatique à l'université d'Angers en 2eme années pour le cours d'architecture distribuée.

## Technologies
***
Liste des technologies utilisées dans le projet :
* [Git](https://git-scm.com/): Version 2.38.1
* [GitKraken](https://www.gitkraken.com/) Version 20.0.1
* [JAVA](https://www.oracle.com/fr/java/technologies/javase/jdk11-archive-downloads.html) Version 11
* [Intellij IDEA](https://www.jetbrains.com/fr-fr/idea/) Version 17.0.3
* [SpringBoot](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot/2.7.5) Version 2.7.5
* [Apache Maven](https://maven.apache.org/download.cgi) Version 3.6.3  
  
Si vous utilisé le fichier ```run_mvn.sh``` ou ```run_jar.sh```
* [xterm](https://doc.ubuntu-fr.org/xterm)

## Executables
Dans le dossier exécutable se trouve les 4 archives JAR.
```
- ./run_jar.sh : cela exécute les 4 JAR dans des terminaux.
ou
- ./run_mvn.sh : cela compile et exécute tous les modules dans des terminaux.
```

## Installations
***
- Pour la suite, les commandes données sont à exécuter dans un terminal.
- Pour installer le projet vous pouvez le cloner avec une clé SSH, ou bien télécharger le dossier ZIP.
- Vous devez avoir JAVA 11 d'installé sur votre pc ainsi que maven version 3.6.3.
```
- git clone git@github.com:tanguy-sudo/Mashup.git
```
Si vous téléchargez le projet en ZIP il  faudra par la suite le décompresser : 
```
- Sous Windows : 
  - Ouvrez l’Explorateur de fichiers et accédez au dossier compressé.
  - Faites un clic droit pour sélectionner "Extraire tout" , puis suivez les instructions.
- Sous Linux :
  - Installer unzip : sudo apt install unzip
  - Exécuter le commande : unzip Mashup-master.zip
- Sous Mac :
  - Double-cliquez sur le fichier.
  ou
  - Exécuter le commande : unzip Mashup-master.zip
```

### Client
Une fois dans le répertoire **client**.
```
- mvn install
- mvn clean package
- java -jar target/client-1.0-SNAPSHOT-jar-with-dependencies.jar 
```

### Serveur : internalCRM
Une fois dans le répertoire **internalCRM**.
```  
- mvn install
- mvn clean package spring-boot:repackage
- java -jar target/internalCRM-2.7.4.jar 

Le serveur virtualCRM est exposé sur le port 8081
```

### Serveur : rss
Une fois dans le répertoire **rss**.
```
- mvn install
- mvn clean package spring-boot:repackage
- java -jar target/rss-2.7.4.jar

Le serveur rss est exposé sur le port 8082
```

### Serveur : virtualCRM
Une fois dans le répertoire **virtualCRM**.
```
- mvn install
- mvn clean package spring-boot:repackage
- java -jar target/virtualCRM-2.7.4.jar 

Le serveur virtualCRM est exposé sur le port 8083
```

## Exemples d'utilisation
***
Dans un terminal lorsque que vous exécutez la commande 
```java -jar target/client-1.0-SNAPSHOT-jar-with-dependencies.jar ```
le client est lancé et vous pouvez par la suite taper une 
URL que vous souhaitez que le client va exécuter.  
Exemples :
```
- http://localhost:8083/virtualCRM/findLeadsByDate?startDate=2022-11-07T10:00:00&endDate=2023-11-07T14:00:00
- http://localhost:8083/virtualCRM/findLeadsByDate?startDate=2022-10-10T20:00:00&endDate=2022-10-30T20:00:00
- http://localhost:8083/virtualCRM/findLeads?lowAnnualRevenue=34000&highAnnualRevenue=40000&state=Pays%20de%20la%20loire
- http://localhost:8083/virtualCRM/findLeads?lowAnnualRevenue=0&highAnnualRevenue=1000000000&state=FL
```
Dans un terminal lorsque que vous exécutez la commande
```java -jar target/rss-2.7.4.jar ```
le serveur rss est lancé et vous pouvez par la suite aller 
dans un navigateur, puis entrer l'URL suivante :
```
- http://localhost:8082/rss/potentialclients
```
Cela vous affichera un flux RSS des personnes ajoutées il y a moins de 24 heures.

## Collaboration
***
Ce projet a été réalisé en collaboration avec [Guillaume GRENON](https://github.com/GuillaumeG49).
