# squiz.it
You can find the Dockerfile at the follow URL: https://github.com/GruppoPBDMNG-9/squiz.it/tree/master/Dockerfile

#Installation guide

#prerequisites:
- docker installato
- assicurarsi di avere il seguente innoltro delle porte in virtual box:
-----------------------------------------------------------
|	    |          |           |          |            |
|  Protocol | Host IP  | Host Port | Guest IP | Guest Port |
|	    |          |           |          |            |
-----------------------------------------------------------
|   TCP     |127.0.0.1 |    4567   | 0.0.0.0  |    4567    |
-----------------------------------------------------------
|   TCP     |127.0.0.1 |    6379   | 0.0.0.0  |    6379    |
-----------------------------------------------------------


Do the follow instructions to import and start this repository:

# mediante boot2docker

# avviare la macchina virtuale con il seguente comando (da completare con il nome della macchina virtuale)
docker-machine.exe start

# accedere alla macchina virtuale (da completare con il nome della macchina virtuale)
docker-machine.exe ssh 

# clonare la repository con il seguente comando
git clone https://github.com/GruppoPBDMNG-9/squiz.it

# entrare nella cartella squiz.it
cd squiz.it

# effettuare il build 
docker build --tag=gruppo_pbdmng_9/squiz.it Dockerfile

# creare il container 
docker run -d --name URLsquiz -p 4567:4567 -p 6379:6379 gruppo_pbdmng_9/squiz.it

# eseguire il container appena creato 
docker exec -it URLsquiz bash

#lanciare l'esecuzione del server mediante il seguente comando
./start
 
Now open your browser and set the URL to: http://localhost:4567 too see and use our application.
