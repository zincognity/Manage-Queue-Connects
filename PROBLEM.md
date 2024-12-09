# Caso de estudio Queue

## Problema:

<div style="text-align: justify"> 
El principal problema que se aborda con este sistema es la gestión eficiente de las citas cuando se tienen recursos limitados, como en un servicio al cliente o sistema de atención en un consultorio. Cuando la cola se llena, el sistema debe asegurarse de que los usuarios no sean atendidos de forma simultánea, y debe esperar a que se libere espacio para continuar con el proceso de atención. La dificultad radica en coordinar la entrada y salida de elementos de la cola, especialmente cuando se deben gestionar recursos como un contador único que atiende a los clientes de manera secuencial.

## Objetivos:

1. Objetivo general: Desarrollar un sistema de gestión de citas en Java que implemente un modelo de cola para gestionar la atención de los clientes de manera eficiente, asegurando que los recursos sean utilizados de forma óptima.
2. Objetivos específicos:

- Implementar una cola que almacene las solicitudes de los clientes.
- Crear un controlador que gestione las solicitudes y el proceso de atención.
- Simular un sistema donde solo se atiende a un cliente a la vez, esperando cuando la cola esté llena.
- Optimizar el uso de los recursos para garantizar que la atención sea secuencial y sin conflictos.
- Implementar un sistema de base de datos para almacenar de manera segura las citas y los datos de los clientes, garantizando la integridad y confidencialidad de la información.

## Fundamento teórico:

El sistema se basa en varios conceptos fundamentales de la informática y programación:

- Colas (Queues): Son estructuras de datos que siguen el principio FIFO (First In, First Out), donde los primeros elementos en entrar son los primeros en salir. Este principio es esencial para simular la gestión de citas, donde el primer cliente en llegar es el primero en ser atendido.
- Controladores y Recursos Limitados: Un controlador es un componente que coordina la ejecución de procesos o la interacción entre distintos elementos. En este caso, el controlador maneja las solicitudes de los clientes, asegurando que se atiendan en orden y que el contador esté disponible para cada cita.
- Multihilos (Threads): El sistema probablemente requiere el uso de múltiples hilos de ejecución para simular la espera de los recursos y el procesamiento de los tickets de citas. El uso de hilos garantiza que el sistema sea eficiente y pueda manejar múltiples solicitudes de manera simultánea, sin bloquear la atención del contador.

## Planeamiento de solución:

Para resolver el problema planteado, el sistema utiliza lo siguiente:

1. Componentes

- Cola de Tickets: La cola almacena las solicitudes de citas. Los clientes se agregan a la cola en el orden en que llegan, y el contador atiende a los clientes de forma secuencial.
- Controlador de Citas: El controlador se encarga de gestionar las interacciones entre el sistema y los usuarios, verificando el estado de la cola y asignando al contador para atender las solicitudes. El controlador también se encarga de notificar cuando la cola está llena y de esperar hasta que haya espacio disponible.
- Contador (Recurso): Este es el componente que atiende a los clientes de la cola. Dado
  que solo puede atender a un cliente a la vez, el contador se asegura de liberar el
  recurso una vez que ha terminado de atender a un cliente y permitir la atención del
  siguiente en la cola.
- Simulación de Espera: Cuando la cola está llena, el sistema activa una señal de
  espera que se desactiva solo cuando se libera espacio en la cola. Esto garantiza que el
  sistema no intentará atender más clientes de los que puede manejar.

2. Herramientas Utilizadas
   El sistema fue desarrollado utilizando las siguientes herramientas y tecnologías:

- Java: Lenguaje de programación utilizado para implementar la lógica del sistema debido
  a su robustez y flexibilidad.
- Visual Studio Code (VS Code): Entorno de desarrollo integrado (IDE) utilizado para
  escribir, depurar y gestionar el código de manera eficiente.
- Swing: Librería de Java utilizada para crear la interfaz gráfica de usuario (GUI),
  proporcionando una experiencia visual interactiva.
- Java Net (Sockets): Se utilizó para la comunicación entre el cliente y el servidor
  mediante sockets, asegurando que las solicitudes de los clientes sean gestionadas en
  tiempo real.
- Archivos CSV: Para almacenar y cargar los datos de los clientes de manera eficiente,
se emplearon archivos CSV que permiten una rápida persistencia y carga de datos.
<div/>
