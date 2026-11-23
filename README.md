# Movilaw

Aplicación Android desarrollada con Kotlin pensada para estudiantes de leyes y abogados.

## Tecnologías utilizadas

- **Kotlin**
- **Jetpack Compose**
- **MVVM (Model-View-ViewModel)**
- **Room**
- **Kotlin Coroutines**
- **StateFlow** 
- **Navigation Compose** 
- **Xano** 

## Arquitectura

El proyecto utiliza el patrón MVVM (Model-View-ViewModel).

- **Model:** representa las estructuras de datos utilizadas por la aplicación.
- **View:** contiene las pantallas e interfaces desarrolladas con Jetpack Compose.
- **ViewModel:** administra la lógica de presentación y los estados de la interfaz.
- **Repository:** gestiona el acceso a las fuentes de datos.
- **DAO:** define las operaciones de acceso a la base de datos local mediante Room.

## Funcionalidades

- Inicio de sesión y gestión de sesión del usuario.
- Pantalla principal de navegación.
- Sección Mi Espacio para gestionar apuntes.
- Perfil de usuario.
- Acceso a la cámara desde Home Screen y el perfil.
- Sección de contactos.
- Consulta de abogados mediante una DB y API externa (Xano).
- Despliegue de informacion de abogados mediante un HTTP GET y registro de nuevos abogados mediante una solicitud HTTP POST (CRUD).

## Integración con Xano

La aplicación utiliza una API desarrollada en Xano para gestionar información de abogados.

Endpoint:

`GET /movilaw_abogados`

`POST /movilaw_abogados`

Los registros contienen los siguientes campos:

- ID.
- Fecha de creación.
- Nombre del abogado.
- RUT.
- Número de caso.
- Nombre del cliente.

## Pruebas

El proyecto cuenta con 3 pruebas unitarias con JUnit 4 y kotlinx-coroutines-test para comprobar el comportamiento del ViewModel para la informacion de los abogados.

Las pruebas utilizan un repositorio falso para evitar depender de una conexión real con la API durante su ejecución.

## Estructura del proyecto

```text
com.example.movilaw/
├── data/
├── model/
├── native/
├── repository/
├── ui/
│   └── screens/
└── viewmodel/
```


