# squiz.it
You can find the Dockerfile at the follow URL: https://github.com/GruppoPBDMNG-9/squiz.it/tree/master/Dockerfile

#Installation guide
Remeber to set port into VirtuBox to 4567

Do the follow instructions to import and start this repository:
- docker-machine.exe start VMNAME (usually dev)
- docker-machine.exe ssh VNAME
- git clone https://github.com/GruppoPBDMNG-9/squiz.it
- cd squiz.it
- docker build --tag=gruppo_pbdmng_9/squiz.it Dockerfile
- docker run -d --name URLsquiz -p 4567:4567 gruppo_pbdmng_9/squiz.it
- docker exec -it URLsquiz bash
- ./start
 
Now open your browser and set the URL to: http://localhost:4567 too see and use our application.
