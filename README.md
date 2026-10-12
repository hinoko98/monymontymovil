<<<<<<< HEAD
# MonyMontyMovil

App móvil (Flutter) de MonyMonty. Reutiliza la misma API que la web
([MonyMontyApi](../MonyMontyApi)): login por sesión con cookie, igual que
hace [MonyMonty](../MonyMonty) (`axios` con `withCredentials: true`).

## Endpoints que usa la app

| Pantalla | Petición | Respuesta esperada |
| --- | --- | --- |
| Login | `POST auth/login` | `201 { token }` + cookie de sesión |
| Arranque | `GET auth/check` | `200 { authenticated }` |
| Home | `GET user/me` | `200 { _id, nombre, apellido, email, avatar }` |
| Cerrar sesión | `GET auth/logout` | `200 { message }` |
| Crear cuenta | `POST user` | `201 { ...usuario }` |
| Recuperar contraseña | `POST auth/recuperar` | `200 { message }` |

Los errores llegan de dos formas y ambas se muestran al usuario tal cual
(`lib/core/api_exception.dart`):

- Validación (`400`): `{ "errors": [{ "msg": "..." }] }`
- Negocio o autenticación (`401`, `404`): `{ "message": "..." }`

El cliente HTTP (`lib/core/api_client.dart`) usa `dio` + `cookie_jar`
persistido en disco para comportarse igual que un navegador: guarda la
cookie que llega en el login y la reenvía en cada petición siguiente, y la
conserva entre reinicios de la app (sesión recordada).

## Apuntar a la API

Por defecto (`lib/core/env.dart`):

- Emulador Android → `http://10.0.2.2:3000/` (así se ve `localhost` del host).
- iOS Simulator → `http://localhost:3000/`.

Para un dispositivo físico o un backend distinto, sobreescribe con:

```bash
flutter run --dart-define=API_BASE_URL=http://192.168.1.10:3000/
```

y corre la API con `EXPRESS_HOST=0.0.0.0` (en su `.env`) para que acepte
conexiones desde la red local. Además agrega esa IP a `CORS_ORIGIN` si vas a
seguir probando la web desde el mismo backend.

No hace falta tocar `CORS_ORIGIN`: CORS es una restricción del navegador y no
aplica a la app móvil. Solo agrégale la IP si vas a seguir abriendo la web
desde ese mismo backend.

### Tráfico HTTP sin TLS

- **Debug**: permitido hacia cualquier host, para poder apuntar a la IP de tu
  PC desde un teléfono físico
  (`android/app/src/debug/res/xml/network_security_config.xml`, y
  `NSAllowsLocalNetworking` en `ios/Runner/Info.plist`).
- **Release**: solo `localhost` y `10.0.2.2`
  (`android/app/src/main/res/xml/network_security_config.xml`). En producción
  la API va por HTTPS, así que no hace falta ninguna excepción.

## Verificar la conexión

`test/api_integration_test.dart` levanta un servidor local que imita los
contratos de la API (mismas rutas, códigos y formas de error) y comprueba el
flujo completo, incluida la cookie de sesión:

```bash
flutter test test/api_integration_test.dart
```

Para probar contra la API real, primero confirma que está arriba:

```bash
curl http://localhost:3000/health
```

Si no responde, revisa la consola donde corre la API: si falla la conexión a
MongoDB (`bad auth`), el servidor no llega a escuchar en el puerto.
=======
# MonyMonty Mobile

Aplicación móvil de **MonyMonty** para la gestión de finanzas personales, desarrollada para **Android e iOS** utilizando **Flutter**.

MonyMonty busca ofrecer una experiencia sencilla para que las personas puedan registrar, organizar y consultar su información financiera desde cualquier lugar.

## Plataformas

* Android
* iOS

## Tecnologías

* **Flutter**
* **Dart**
* Android
* iOS
* API REST de MonyMonty

## Arquitectura

La aplicación móvil funciona como cliente de la plataforma MonyMonty y consume los servicios proporcionados por su API.

```text
┌──────────────────────┐
│    MonyMonty Mobile  │
│                      │
│       Flutter        │
│         Dart         │
└──────────┬───────────┘
           │
           │ API REST
           ▼
┌──────────────────────┐
│    MonyMonty API     │
│                      │
│    Node.js/Express   │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│       MongoDB        │
└──────────────────────┘
```

La aplicación móvil **no contiene directamente la lógica de persistencia del sistema**. La comunicación con los datos y servicios de MonyMonty se realiza mediante la API.


## 🏢 MonyMonty

**MonyMonty** es un producto de **MonteFlor**, orientado a facilitar la gestión de las finanzas personales.

🌐 [monymonty.monteflor.co](https://monymonty.monteflor.co/)

---

> Este repositorio contiene exclusivamente el cliente móvil de MonyMonty. Los servicios de backend y otros productos de MonteFlor se mantienen en repositorios independientes.


## Únete a Nuestra Comunidad

Con MonyMonty, estamos construyendo una comunidad comprometida en capacitar a las personas para alcanzar una salud financiera óptima. ¡Nos
encantaría que te unas a nosotros y comiences a tomar el control de tu futuro financiero hoy mismo!

## Conéctate con Nosotros

Para obtener más información sobre el proyecto, visita nuestras redes sociales y mantente al tanto de las últimas novedades:

<p align="left">

</p>

## Conéctate con el creador

<p align="left"> 
    <a href="https://discord.com/users/Oug#6073" target="_blank" rel="noreferrer">
        <img src="https://raw.githubusercontent.com/danielcranney/readme-generator/main/public/icons/socials/discord.svg" width="32" height="32" />
    </a>
    <a href="https://www.github.com/OugMontiel" target="_blank" rel="noreferrer">
        <img src="https://raw.githubusercontent.com/danielcranney/readme-generator/main/public/icons/socials/github.svg" width="32" height="32" />
    </a>
    <a href="http://www.instagram.com/oug_montiel/" target="_blank" rel="noreferrer">
        <img src="https://raw.githubusercontent.com/danielcranney/readme-generator/main/public/icons/socials/instagram.svg" width="32" height="32" />
    </a>
    <a href="https://www.linkedin.com/in/diego-alejandro-montiel-florez-data-science/" target="_blank" rel="noreferrer">
        <img src="https://raw.githubusercontent.com/danielcranney/readme-generator/main/public/icons/socials/linkedin.svg" width="32" height="32" />
    </a>
</p>

## License

This project is licensed under the GNU Affero General Public License v3.0 (AGPL-3.0).
>>>>>>> f829fe805593074b8898754c3eabb46242660ed5
