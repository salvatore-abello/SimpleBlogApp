# Simple Blog App
 Semplice blog realizzato utilizzando Spring e Angular

## TODO
- [ ] Realizzare il **backend**
  - [x] Ottenere tutti i post e utenti
  - [x] Ottenere tutti i post di un determinato utente (post + informazioni sull'utente)
  - [x] Ottenere tutti i post con un determinato/i tag
  - [x] Nascondere email, password e id dell'utente quando si fetchano i post
  - [x] Riscrivere tutto usando Mapper e DTO
  - [x] Miglior risposta dal server in caso di exception (path dinamica + altro)
  - [x] Register
     - [x] **PASSWORD HASHATE NEL DATABASE (IMPORTANTISSIMO!1!1!1!1!)**
     - [x] Controllo di email duplicate
     - [x] Controllo sul formato dell'email
     - [x] Controllo sulla lunghezza della password (min 6)
     - [x] Controllo che tutti i field esistano e non siano nulli
     - [x] Errori carini
     - [x] Mettere un controllo sul nome e cognome
  - [x] Login
     - [x] Se il login è andato a buon fine, restituire un JWT
     - [x] Se il login non è andato a buon fine, dirlo all'utente
  - [x] Endpoint raggiungibili solo dagli utenti loggati (o qualcos'altro? Tramite header o cookie? JWT????
     - [x] JWT Secret e expiration date aggiunti ad `application.yaml`
     - [x] Se il token è sbagliato, catchare l'exception e printarlo in modo carino
     - [ ] Capire dove mettere il file `JWTUtil`
  - [x] Creare controller
     - [x] PostController
     - [x] AuthController
     - [x] UserController
     - [x] TagController
  - [x] Ricerca dei post. Se manca uno o più parametri di ricerca citati qua sotto, deve funzionare comunque
     - [x] In base a uno o più tag
     - [x] In base ad una stringa contenuta nel titolo e/o contenuto
     - [x] In base all'owner del post
  - [x] Ottenere il singolo post dal suo ID
  - [x] Ricerca di una parola (o frase) nei post (found, not found)
  - [ ] Possibilità di cambiare password
  - [ ] Creazione di un post
     - [x] TANTI TANTI TANTI problemi con l'inserimento di un post (problema con i tag) **QUASI FIXATO!**
     - [ ] Aggiungere qualcosa per cacheare le tag (non cambiano, quindi che senso ha prenderle di continuo dal db?)
  - [ ] Possibilità di switchare tra prod e dev
  - [ ] **ABILITARE CSRF PROTECTION** (dopo aver finito il frontend)

- [ ] Realizzare il **frontend**
  - [ ] Boh, da decidere appena finisco il backend

- [ ] Robe in più (modo carino per dire che non le farò)
  - [ ] Admin panel
  - [ ] Chi crea i tag? Probabilmente l'admin. Potrebbe essere carino implementare sta roba
  - [ ] Eliminare post? In base a cosa? Probabilmente no
  - [ ] Docker?

Probabilmente c'è roba che fa veramente schifo, questo è il mio primo esercizio con Java e Angular.
