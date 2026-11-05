# Gestion de flota de drones (DSA - Minimo 1)

Trabajo de DSA de la EETAC. Autor: Eric Manel Lopez Somoza

Es una aplicacion en Java para gestionar drones, pilotos, un almacen de mantenimiento y planes de vuelo.

## Como esta hecho

- Los drones y los pilotos los guardo en HashMap para encontrarlos rapido por su id.
- El almacen es una cola (LinkedList), porque el primer dron que entra es el primero que se repara.
- Los planes de vuelo estan en un ArrayList.
- La fachada (DroneManagerImpl) es un Singleton y tiene trazas de log4j en todos los metodos.


## Estado actual
- Parte II: hecha. Interfaz, fachada, log4j y 6 tests con JUnit que pasan bien.
- Parte III: hecha. Se puede probar con Postman y funcionan todas las operaciones.

## Problemas que he tenido
- El cliente HTTP de IntelliJ pide licencia, asi que use Postman.
- Si repites una peticion sin reiniciar el servidor da 409, porque el dron o el piloto ya existen.

## Pendiente

se podria guardar la informacion en una base de datos en vez de en memoria.


## DEFINICION PARTE 3
El servicio se llama DroneService y esta en la clase DroneService.java. Usa la fachada
de la parte II para hacer todo el trabajo. La direccion base es:

http://localhost:8080/dsa/
## Operaciones

### 1. Anadir un dron
- Metodo y ruta: POST /drones
- Peticion: un JSON con el id, nombre, fabricante y modelo.
  {"id": "D01", "nombre": "Halcon", "fabricante": "DJI", "modelo": "Mavic 3"}
- Respuesta: el dron creado, con horasVuelo a 0 y operativo a true.
- Codigos: 201 si se crea bien, 400 si faltan datos, 409 si ya existe un dron con ese id.

### 2. Anadir un piloto
- Metodo y ruta: POST /pilots
- Peticion: un JSON con el id, nombre y apellidos.
  {"id": "P01", "nombre": "Eric", "apellidos": "Lopez"}
- Respuesta: el piloto creado.
- Codigos: 201 si se crea bien, 400 si faltan datos, 409 si ya existe.

### 3. Listado de drones ordenado por horas de vuelo
- Metodo y ruta: GET /drones
- Peticion: no lleva nada.
- Respuesta: una lista de drones, del que mas horas tiene al que menos.
- Codigos: 200.

### 4. Listado de pilotos ordenado por horas de vuelo
- Metodo y ruta: GET /pilots
- Peticion: no lleva nada.
- Respuesta: una lista de pilotos, del que mas horas tiene al que menos.
- Codigos: 200.

### 5. Guardar un dron en el almacen
- Metodo y ruta: POST /warehouse/{idDrone}, por ejemplo POST /warehouse/D01
- Peticion: no lleva body, el id del dron va en la ruta.
- Respuesta: el dron, que ahora sale con operativo a false.
- Codigos: 200 si se guarda, 404 si el dron no existe, 409 si ya estaba en el almacen.

### 6. Reparar el dron del almacen
- Metodo y ruta: PUT /warehouse/repair
- Peticion: no lleva nada. Se repara el primero que entro en el almacen.
- Respuesta: el dron reparado, que vuelve a estar operativo.
- Codigos: 200 si se repara, 404 si el almacen esta vacio.

### 7. Crear un plan de vuelo
- Metodo y ruta: POST /flightplans
- Peticion: un JSON con el dron, el piloto, la fecha, las horas y las posiciones.
  {"idDrone": "D01", "idPilot": "P01", "fecha": "2026-11-10T10:00", "horas": 2,
  "posiciones": [{"latitude": 41.275, "longitude": 1.987}]}
- Respuesta: el plan creado, con un id que genera el programa.
- Codigos: 201 si se crea bien, 400 si faltan datos o la fecha esta mal escrita,
  404 si el dron o el piloto no existen, 409 si el dron o el piloto ya tienen un plan
  en esas horas o si el dron esta en el almacen.

### 8. Planes de vuelo de un piloto
- Metodo y ruta: GET /pilots/{idPilot}/flightplans, por ejemplo GET /pilots/P01/flightplans
- Peticion: no lleva body.
- Respuesta: la lista de planes de ese piloto.
- Codigos: 200, o 404 si el piloto no existe.

### 9. Planes de vuelo de un dron
- Metodo y ruta: GET /drones/{idDrone}/flightplans
- Peticion: no lleva body.
- Respuesta: la lista de planes de ese dron.
- Codigos: 200, o 404 si el dron no existe.

## Errores

Cuando algo falla, la respuesta lleva un JSON con un mensaje que explica el error:



