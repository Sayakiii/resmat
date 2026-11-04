# ResMat – Réservation de matériel

Spring Boot 3 · Mustache (.hbs) · MariaDB · Spring Security

## Lancer le projet (Eclipse)
1. **Base de données** (MariaDB) :
   ```sql
   CREATE DATABASE resmat CHARACTER SET utf8mb4;
   CREATE USER 'resmat'@'localhost' IDENTIFIED BY 'resmat';
   GRANT ALL PRIVILEGES ON resmat.* TO 'resmat'@'localhost';
   ```
2. Eclipse : *File > Import > Maven > Existing Maven Projects* → choisir ce dossier.
3. (Optionnel) variables d'environnement `DB_URL`, `DB_USER`, `DB_PASSWORD`
   (*Run Configurations > Environment*). Valeurs par défaut : voir `application.properties`.
4. Clic droit sur `ResMatApplication` > *Run As > Java Application* (ou *Spring Boot App* avec Spring Tools).
5. Ouvrir http://localhost:8080

## Comptes de démonstration (mot de passe : `password`)
| Rôle | E-mail |
|---|---|
| Emprunteur (ROLE_USER) | alice@resmat.fr, bob@resmat.fr, chloe@resmat.fr |
| Gestionnaire (ROLE_GESTIONNAIRE) | gestionnaire@resmat.fr, sophie@resmat.fr |

Les données sont insérées au premier démarrage si la base est vide (`DataLoader`).

## Tests
`Run As > JUnit Test` sur `RepositoriesTest` (base H2 en mémoire, MariaDB non requise).

## Tâches couvertes
Entities · Repositories · Données de test · CRUD TypeMateriel (`/gestionnaire/types`) · Security (login, CSRF, rôles).
