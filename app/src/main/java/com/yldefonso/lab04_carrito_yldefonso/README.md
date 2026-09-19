LABORATORIO 04-PROGRAMACIÓN EN MOVILES
¿por qué productos se declara con val y aun así podemos agregarle elementos?
Esto se debe a que val congela la referencia a la lista,pero mutableStateListOf permite ser un objeto mutable debido a sus metodos propios designados,los cuales son add y remove que modifican su contenido interno y no en la variable.
