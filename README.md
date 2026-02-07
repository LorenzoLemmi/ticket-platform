# Ticket Platform – Support Dashboard

Applicazione backend sviluppata con **Java e Spring Boot** per la gestione delle richieste di supporto tecnico di un prodotto.  
Il sistema consente agli amministratori di gestire e assegnare i ticket agli operatori, applicando regole di business e controlli di accesso basati sui ruoli.

## 🚀 Tecnologie utilizzate
- Java
- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Security
- MySQL
- Thymeleaf
- Bootstrap
- Maven

## 📌 Funzionalità principali

### Gestione Ticket
- CRUD completo dei ticket
- Aggiornamento dello stato del ticket:
  - Da fare
  - In corso
  - Completato
- Assegnazione obbligatoria dei ticket a operatori disponibili
- Ricerca dei ticket per titolo
- Filtraggio dei ticket per categoria e stato

### Note ai Ticket
- Aggiunta di note ai ticket assegnati
- Visualizzazione delle note con:
  - autore
  - data di creazione
  - contenuto testuale

### Gestione Operatori
- Visualizzazione dei ticket assegnati all’operatore
- Aggiornamento dello stato dei ticket assegnati
- Possibilità di impostare lo stato personale “non disponibile”  
  (solo se non sono presenti ticket in stato *Da fare* o *In corso*)

### Autenticazione e Autorizzazione
- Sistema di autenticazione con utenti salvati a database
- Ruoli supportati:
  - **ADMIN**: gestione completa dei ticket e dashboard amministrativa
  - **OPERATORE**: gestione e aggiornamento dei ticket assegnati
- Controllo degli accessi alle funzionalità tramite Spring Security

### Modellazione dei Dati
- Relazioni tra le entità:
  - Ticket
  - Operatore
  - Categoria
  - Nota
- Utilizzo di JPA/Hibernate per la gestione delle relazioni e della persistenza

### API REST
- Esposizione di API REST per:
  - visualizzazione elenco ticket
  - filtraggio per categoria
  - filtraggio per stato

### Interfaccia Utente
- UI server-side con Thymeleaf
- Dashboard amministrativa in formato tabellare
- Layout responsive realizzato con Bootstrap

## 🧱 Architettura
L’applicazione segue un’architettura a livelli:
- Controller
- Service
- Repository
- Database

## ▶️ Avvio del progetto
1. Clonare il repository
2. Configurare il database MySQL
3. Impostare le credenziali nel file `application.properties`
4. Avviare l’applicazione tramite Spring Boot
