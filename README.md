# Nombre del Proyecto
API de Cálculo de Porcentaje
API para realizar cálculos de porcentaje y consultar el historial de llamadas.

## Decisiones técnicas
Diseñada para realizar cálculos con porcentajes dinámicos obtenidos desde un servicio externo,
además de tener una cache distribuida con Redis, reintento ante fallos y rate limite.

Esta API desarrollada bajo una arquitectura hexagonal, permite que sea fácilmente mantenible y escalable en el tiempo.
Esta arquitectura tiene como beneficio la separación de responsabilidades, ya que la lógica de negocio es independiente
de la infraestructura, por lo que se puede cambiar la tecnología (por ejemplo, de PostgreSQL a MongoDB)
sin afectar la lógica de negocio.


## Tecnologías Usadas

- Java 21
- Spring Boot
- PostgreSQL
- Redis
- Docker
- wiremock

## Requisitos

- JDK 21
- Docker y Docker Compose

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Christomo3128/percentageapi.git
2. Agregar variables de entorno
   Para probar desde local se deberá configurar en el IDE las siguientes variables.
   - REDIS_HOST=127.0.0.1;REDIS_PASSWORD=torres;SPRING_DATASOURCE_PASSWORD=postgres;SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/test;SPRING_DATASOURCE_USERNAME=postgres;WIREMOCK_HOST=http://127.0.0.1:8089;REDIS_TIME_TO_LIVE=1800000

3. Ejecutar el docker-compose.yml, ubicarse en la carpeta donde se encuentra el archivo y ejecutar:
    docker-compose up -d
4. Probar el Servicio
   Una vez levantado los servicios; probar los siguientes endpoints
    * Cálculo de porcentaje dinámico
      Las variables num1 y num2 puede modificarse para ejecutar la operación, según las pruebas que deseen hacer, este endpoint se conecta con un servicio Mock creado en wiremock, entregando siempre 10%, si se requiere hacer cambio de porcentaje
      se deberá ingresar en docker al siguiente archivo /home/wiremock/mappings/example-mock.json y modificar el campo amount.
      El puerto de este endpoint es variable, debido a que se configuró con réplicas(3).
      Nota. Importar la curl en postman
      curl --location --request GET 'localhost:8083/api/v1/calculation?num1=5&num2=5' --header 'Content-Type: application/json'
    * Listar historial de llamadas
      Los params pueden ser modificables según como se requiera que muestre el listado.
      Nota. Importar la curl en postman
      curl --location --request GET 'localhost:8083/api/v1/calculation/call-history?page=0&size=10'
Nota: se podrán deshabilitar algunos servicios para manejar escenarios de caídas, por ejemplo, si se detiene el servicio de porcentaje, 
   y el porcentaje está almacenado en redis, el servicio debería seguir funcionando durante el tiempo de vida del valor en caché.