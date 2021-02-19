Modificarile majore aduse scheletului s-au efectuat in clasele IntersectionFactory, 
IntersectionHandlerFactory, ReaderHandlerFactory.
In clasa Pedestrians s-au adus mici modificari prin folosirea notifyAll pentru a notifica masinile 
in urmatoarele situatii: 
    - start;
    - schimbarea culorii semaforului(rosu pt ele);
    - schimbarea culorii semaforului(verde pt ele);
    - sfarsit.
In pachetul utils a fost adaugata clasa generica Pair, cu ajutorul careia putem forma perechi de 2 
obiecte diferite.

In clasa IntersectionFactory se creeaza, daca nu exista, instanta pentru intersectia ceruta. 
Altfel, se returneaza instanta cache-uita.
Clasele care reprezinta tipurile de intersectii sunt: 
    - ComplexMaintenanceIntersection, care are urmatorii membrii:
        - freeLanes, numarul noilor benzi;
        - totalInitialLanes, numarul benzilor vechi;
        - passingCarsInOneGo, numarul maxim de masini la o trecere pentru o banda;
        - arrivingSemaphore, semafor folosit ca mutex pentru a realiza operatiile legate de sosirea 
unei masini;
        - initialLanesCarsQueues, lista cozilor care contin masinile incolonate in ordinea in care 
au ajuns pe fiecare banda veche;
        - newLanesQueuesOfOldLanes, coada cu benzile vechi pentru fiecare banda noua;
        - CyclicBarrier carsBarrier, bariera folosita pentru a sincroniza toate masinile;
    - CrosswalkIntersection, care are urmatorii membrii:
        - pedestrians, pietonii;
    - PriorityIntersection, care are urmatorii membrii:
        - noCarsWithHighPriority, numarul masinilor cu prioritate;
        - noCarsWithLowPriority, numarul masinilor fara prioritate;
        - noCarsWithHighPriorityInIntersection, variabila atomica folosita pentru a retine numarul 
de masini cu prioritate aflate in intersectie;
        - semaphoreForCarsWithLowPriority, semafor pentru masinile fara prioritate care pot intra 
doar cand nu se afla nicio masina cu prioritate in intersectie;
    - RailroadIntersection, care are urmatorii membrii:
        - arrivingSemaphore, semafor folosit ca mutex pentru a realiza operatiile legate de sosirea 
unei masini;
        - firstSideQueue, coada folosita pentru a introduce masinile in ordinea in care sosesc de 
pe primul sens;
        - secondSideQueue, coada folosita pentru a introduce masinile in ordinea in care sosesc de 
pe al doilea sens;
        - carsBarrier, bariera folosita pentru a sincroniza toate masinile;
    - SimpleMaintenanceIntersection, care are urmatorii membrii:
        - noCarsPassingAtOnce, numarul exact de masini care vor circula alternativ dintr-un sens pe 
o singura banda;
        - firstSideBarrier, bariera pentru a sincroniza masinile de pe primul sens de mers;
        - secondSideBarrier, bariera pentru a sincroniza masinile de pe al doilea sens de mers;
        - firstSideSemaphore, semafor pentru a sincroniza masinile de pe primul sens de mers;
        - secondSideSemaphore, semafor pentru a sincroniza masinile de pe al doilea sens de mers;
    - SimpleMaxXCarRoundaboutIntersection, care are urmatorii membrii:
        - noLanes, numarul de directii;
        - roundaboutPassingTime, timpul ca masina sa paraseasca sensul giratoriu;
        - maxNoCarsPassingAtOnce, numarul maxim de masini care pot trece sensul giratoriu venind 
din aceeasi directie;
        - semaphores, semafoare pentru fiecare directie care limiteaza numarul maxim de masini care 
pot trece;
    - SimpleNRoundaboutIntersection, care are urmatorii membrii:
        - maxNoCarsPassingAtOnce, numarul maxim de masini care pot trece prin sensul giratoriu;
        - roundaboutTime, timpul ca masina sa paraseasca sensul giratoriu;
        - semaphore, semaforul limiteaza numarul de masini care pot intra in sensul giratoriu;
    - SimpleSemaphoreIntersection, care nu are niciun membru
    - SimpleStrict1CarRoundaboutIntersection, care are urmatorii membrii:
        - noLanes, numarul de directii;
        - roundaboutPassingTime, timpul ca masina sa paraseasca sensul giratoriu;
        - semaphores, pentru fiecare directie exista un semafor care limiteaza accesul pentru o 
singura masina;
        - barrier, bariera ca sa sincronizam numarul de masini pentru toate directiile;
    - SimpleStrictXCarRoundaboutIntersection, care are urmatorii membrii:
        - noLanes, numarul de directii;
        - roundaboutPassingTime, timpul ca masina sa paraseasca sensul giratoriu;
        - exactNoCarsPassingAtOnce, numarul exact de masini care pot trece sensul giratoriu venind 
din aceeasi directie;
        - semaphores, pentru fiecare directie exista un semafor care limiteaza numarul de masini 
care pot trece;
        - allCarsReachedBarrier, bariera ca sa sincronizam toate masinile;
        - barrier, bariera ca sa sincronizam numarul specificat de masini pentru toate directiile.

In ReaderHandlerFactory exista functia getHandler care returneaza sub forma unor clase anonime 
implementari pentru citirea si initializarea tipurilor de intersectii.

In IntersectionHandlerFactory exista functia getHandler care returneaza implementari ale 
IntersectionHandler sub forma unor clase anonime.
Aici este prezenta logica principala.
Astfel, de mentionat ar fi, pe langa mesajele de afisare specifice si alte calcule, in cazul 
intersectiei:
    - simple_n_roundabout: se foloseste un semafor deoarece masina va intra in sensul giratoriu 
doar daca nu sunt mai multe masini decat numarul specificat;
    - simple_strict_1_car_roundabout: masina poate intra in sensul giratoriu doar daca:
        1) nu exista deja alta masina de pe aceeasi directie care vrea sa intre sau care a intrat 
in sens;
        2) din fiecare directie exista o masina care doreste sa intre.
        Folosim, prin urmare, cate un semafor initializat cu 1 pentru fiecare directie si o bariera 
initializata cu numarul specificat de masini.
    - simple_strict_x_car_roundabout: masina poate fi selectata sa intre in sensul giratoriu doar 
daca:
        1) toate masinile au ajuns la sensul giratoriu;
        2) nu a fost atins inca numarul specificat de masini care pot intra dintr-o directie.
        Folosim, prin urmare, o bariera initializata cu numarul total de masini si un semafor 
pentru fiecare directie initializat cu numarul specificat de masini.
        Masina poate intra doar daca s-a atins numarul specificat de masini pentru toate directiile 
si folosim o bariera initializata cu numarul specificat de masini inmultit cu numarul directiilor.
    - simple_max_x_car_roundabout: masina poate intra in sensul giratoriu doar daca numarul maxim 
de masini pentru o directie nu este depasit. Folosim astfel un semafor pentru fiecare directie 
initializat cu numarul maxim specificat de masini.
    - simple_maintenance: masinile circula alternativ un numar exact si trebuie indeplinite 2 
conditii:
        1) sa se adune numarul de masini specificat pe sensul lor;
        2) sa existe permisiunea circularii pe sensul lor.
        Folosim, prin urmare, bariera si semafor atat pentru primul sens de mers cat si pentru cel 
de-al doilea sens de mers. Pentru a permite masinilor din sensul celalalt sa circule trebuie ca 
toate masinile din sensul curent sa fi trecut de punctul in lucru. Pentru fiecare masina trecuta 
din sensul curent o alta masina din sensul opus va castiga permisiune
    - complex_maintenance: operatiile legate de sosirea unei masini reprezinta o regiune critica 
deoarece masinile trebuie sa porneasca in ordinea in care au ajuns. Astfel, folosim un semafor 
initializat cu 1, care se comporta ca un mutex.
        Repartitia pe noile benzi incepe in momentul in care toate masinile s-au incolonat, pentru 
care folosim o bariera.
        Verificam daca masina se afla in varful vreunei cozi utilizand syncronized ca sa ne 
asiguram ca un singur thread efectueaza operatii pe structurile noastre de date.
    - railroad: Folosim cozi pentru a introduce masina in coada corespunzatoare sensului pe care se 
deplaseaza. Inainte ca trenul sa plece, trebuie ca toate masinile sa ajunga la calea ferata, motiv 
pentru care folosim o bariera.
        Masina poate pleaca abia atunci cand ii vine randul.Avem o abordare de tip busy waiting: 
masina asteapta pana cand se afla in varful cozii sensului sau de mers, apoi afisam mesajul si o 
scoatem din coada.
