Kafka
	docker compose up (avec le fichier docker-compose.yml fourni)
	Faire un docker compose rm à la fin, sinon, il reste des topics ouverts
	
Consul
	docker run -it --name consul --rm -p 8500:8500 consul:1.11.3
	
Lancement du service avion
	java -jar target/avion-service-1.0.jar
	
Lancement du service hotel
	java -jar target/hotel-service-1.0.jar

Lancement du service de compensation
	java -jar target/rollback-service-1.0.jar
	
Exécution
	http :8080/hotels	// listes hotels 
	http :9000/avions	// listes avions 
	
Normalement, cela fonctionne.

on insère un nouvel élément, avec la commande:

	http POST :8080/hotels ville="Barcelone" nom="grand Hotel"

Le service Hotel prévient le service Avions en fin de transaction
	http :9000/avions	//avion {destination="Barcelone"

Pour montrer la compensation :

http POST :8080/hotels ville="Madrid" nom="petit Hotel"
Le service Hotel prévient le service Avions en fin de transaction mais Avion plante
	http :9000/avions	//pas d'avions vers Madrid
LA compensation est appelée et l'hotel passe à "annulé)

