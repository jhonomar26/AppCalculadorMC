# AppCalculadoraBMI

Aplicación móvil que permite calcular el Índice de Masa Corporal (IMC) y proporciona una clasificación del peso del usuario según el resultado. Además, permite llevar un historial de cálculos y cuenta con autenticación de usuarios.

## Características

- Cálculo del Índice de Masa Corporal (IMC).
- Clasificación en diferentes categorías: bajo peso, peso normal, sobrepeso, obesidad.
- Almacenamiento de historial de cálculos.
- Autenticación de usuarios.
- Sección "Acerca de" con información sobre la app.
- Soporte para múltiples idiomas.

## Tecnologías Utilizadas

- **Kotlin**
- **SQLite (para almacenamiento de historial)**
- **Firebase Authentication (para autenticación de usuarios)**

## Estructura del Proyecto

El proyecto contiene los siguientes archivos principales:

- `SplashActivity.kt` - Pantalla de bienvenida al abrir la aplicación.
- `LoginActivity.kt` - Manejo del inicio de sesión y autenticación de usuarios.
- `RegisterActivity.kt` - Permite registrar nuevos usuarios.
- `MainActivity.kt` - Permite ingresar peso y altura para calcular el IMC.
- `HistorialActivity.kt` - Muestra el historial de cálculos realizados.
- `DatabaseHelper.kt` - Administra la base de datos SQLite para almacenar el historial.
- `AboutActivity.kt` - Muestra información sobre la aplicación.

## Instalación y Configuración

1. Clona este repositorio:
   ```sh
   git clone https://github.com/jhonomar26/AppCalculadoraBMI.git
   ```
2. Abre el proyecto en Android Studio.
3. Configura Firebase Authentication si deseas utilizar la autenticación de usuarios.
4. Compila y ejecuta la aplicación en un dispositivo o emulador.

## Contribuciones

Si deseas contribuir a este proyecto, sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama con tu mejora:
   ```sh
   git checkout -b feature-nueva-funcionalidad
   ```
3. Realiza tus cambios y súbelos al repositorio.
4. Envía un pull request para revisión.

## Licencia

Este proyecto está bajo la licencia MIT. Puedes ver más detalles en el archivo `LICENSE`.
