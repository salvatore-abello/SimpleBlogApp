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
  - [ ] Creare n controller (da vedere):
     - [ ] PostController
     - [ ] AuthController
     - [ ] UserController (?)
     - [ ] TagController
  - [ ] Possibilità di cambiare password
  - [ ] Creazione di un post
  - [ ] Ottenere il singolo post dal suo ID
  - [ ] Ricerca di una parola (o frase) nei post (found, not found)
  - [ ] Possibilità di switchare tra prod e dev

- [ ] Realizzare il **frontend**
  - [ ] Boh, da decidere appena finisco il backend

- [ ] Robe in più (modo carino per dire che non le farò)
  - [ ] Admin panel
  - [ ] Chi crea i tag? Probabilmente l'admin. Potrebbe essere carino implementare sta roba
  - [ ] Eliminare post? In base a cosa? Probabilmente no

Probabilmente c'è roba che fa veramente schifo, questo è il mio primo esercizio con Java e Angular.
