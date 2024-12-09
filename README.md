# Adminitración de Colas a través de Conexiones

Este proyecto se ha comenzado el día 16/10/2024 para ser presentado como Proyecto Final del Ciclo 2 - Agosto 2024 en la [Universidad Tecnológica del Perú](https://www.utp.edu.pe/).

## Documentación

### Correr el código de manera correcta.

### Instrucciones:

1.  Leer [PROBLEM.md](https://github.com/zincognity/Manage-Queue-Connects/blob/main/PROBLEM.md).
2.  Dirigirse a un directorio local a través de la consola.

    ```console
    > cd [directory]
    > git clone https://github.com/zincognity/Manage-Queue-Connects.git
    ```

3.  Dirigirse al archivo [Control.java](https://github.com/zincognity/Manage-Queue-Connects/blob/main/control/Control.java)
4.  Correr dicho archivo para empezar a usar el programa, éste mismo es el servidor y control de las aplicaciones.

    ```console
    > cd ~/[directory]/Manage-Queue-Connects/control
    > javac Control.java
    > java Control.java
    ```

5.  Inicializar el servidor en la parte del menú.
    ![Capture](https://media.discordapp.net/attachments/1313223333436981326/1315772798530093136/Captura_de_pantalla_2024-12-09_a_las_3.11.17_p._m..png?ex=6758a069&is=67574ee9&hm=27c62e128de631b4f43ecfe9c1e606f6f71d2235342ce2f4dd6d978c0d74db14&=&format=webp&quality=lossless&width=1100&height=280)

6.  Una vez iniciado el servidor, se pueden ejecutar los otros clientes de diferente tipo.
7.  Dirigirse al archivo [Atention.java](https://github.com/zincognity/Manage-Queue-Connects/blob/main/atention/Atention.java)
8.  Correr dicho archivo, éste mismo es el cliente de atención, que será el encargado de administrar la información de los tickets para ser atendidos.

    ```console
    > cd ~/[directory]/Manage-Queue-Connects/atention
    > javac Atention.java
    > java Atention.java
    ```

9.  Conectar con el servidor en la parte del menú.
    ![Image](https://media.discordapp.net/attachments/1313223333436981326/1315778846884233266/Captura_de_pantalla_2024-12-09_a_las_3.35.14_p._m..png?ex=6758a60b&is=6757548b&hm=8db8d1d9cae94b5f82805e40bc89bcd51330f618949eda191afa123c6d6963ca&=&format=webp&quality=lossless&width=1100&height=280)

10. Dirigirse al archivo [Tickets.java](https://github.com/zincognity/Manage-Queue-Connects/blob/main/tickets/Tickets.java)
11. Correr el archivo, dicho archivo es el cliente de Tickets, que permitirá crear los tickets para añadirlos a la Queue.

    ```console
    > cd ~/[directory]/Manage-Queue-Connects/tickets
    > javac Tickets.java
    > java Tickets.java
    ```

12. Puedes crear un ticket colocando algún dni registrado que puedes verlos en el archivo [clients.csv]()
    ![Image](https://cdn.discordapp.com/attachments/1313223333436981326/1315781197133123644/Captura_de_pantalla_2024-12-09_a_las_3.png?ex=6758a83b&is=675756bb&hm=78304d538e134cce251750036864ddad411da27847638df2beb791014cb7a001&)
13. Puedes ver el ticket creado en el cliente de atención.
    ![Image](https://media.discordapp.net/attachments/1313223333436981326/1315781755873132544/Captura_de_pantalla_2024-12-09_a_las_3.46.51_p._m..png?ex=6758a8c0&is=67575740&hm=b25b1e20aabddbc41085dc92bee1a3ac90aee746371ec7c094cd8741d060f995&=&format=webp&quality=lossless&width=1558&height=1194)

14. Dirigirse al archivo [Manage.java](https://github.com/zincognity/Manage-Queue-Connects/blob/main/manage/Manage.java)
15. Correr el archivo, dicho archivo es el cliente de Manage, que es el monitor, en este se podrán visualizar las atenciones conectadas al servidor y a quienes están atendiendo.

    ```console
    > cd ~/[directory]/Manage-Queue-Connects/manage
    > javac Manage.java
    > java Manage.java
    ```

16. Esos serían todos los archivos por inicializar, lo demás es total mente intuitivo, con botones interactivos, con inputs, y funcionamientos lógicos además de tablas de datos que permiten ver información detallada en este caso, de los tickets.

## Datos

- La administración de archivos es totalmente intuitiva, por lo cual según el nombre de las carpetas, encontrarán contenido respecto a los archivos y sus funcionalidades.
- Cada cliente tiene su propia configuración, sabiendo que estos estarán conectados a través de una red local, por lo que para conectar varias apliciones o clientes, deberán tener diferente nombre para que sean identificables, se pueden editar en el archivo /config/config.properties.
